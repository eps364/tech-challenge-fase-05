param(
  [string]$BaseUrl = "http://localhost:8215"
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"
Add-Type -AssemblyName System.Net.Http

$repositoryRoot = Split-Path -Parent $PSScriptRoot
$composeFile = Join-Path $repositoryRoot "docker-compose.e2e.yml"
$reportDirectory = Join-Path $repositoryRoot "docs\tecnico\e2e"
$reportPath = Join-Path $reportDirectory "relatorio_execucao_e2e.md"
$httpClient = [System.Net.Http.HttpClient]::new()
$report = [System.Text.StringBuilder]::new()

function Invoke-Compose {
  param([string[]]$Arguments)

  & docker compose -f $composeFile @Arguments
  if ($LASTEXITCODE -ne 0) {
    throw "Docker Compose failed: $($Arguments -join ' ')"
  }
}

function Invoke-ApiRequest {
  param(
    [string]$Method,
    [string]$Path,
    [string]$Payload,
    [int[]]$ExpectedStatus
  )

  $requestMethod = [System.Net.Http.HttpMethod]::new($Method)
  $request = [System.Net.Http.HttpRequestMessage]::new($requestMethod, "$BaseUrl$Path")
  if (-not [string]::IsNullOrEmpty($Payload)) {
    $request.Content = [System.Net.Http.StringContent]::new(
      $Payload,
      [System.Text.Encoding]::UTF8,
      "application/json")
  }

  $response = $null
  try {
    $response = $httpClient.SendAsync($request).GetAwaiter().GetResult()
    $content = $response.Content.ReadAsStringAsync().GetAwaiter().GetResult()
    $result = [pscustomobject]@{
      StatusCode = [int]$response.StatusCode
      Content = $content
    }
  } finally {
    $request.Dispose()
    if ($null -ne $response) {
      $response.Dispose()
    }
  }

  if ($ExpectedStatus -notcontains $result.StatusCode) {
    throw "Unexpected HTTP status $($result.StatusCode) for $Method $Path. Body: $($result.Content)"
  }
  return $result
}

function Assert-Condition {
  param([bool]$Condition, [string]$Message)

  if (-not $Condition) {
    throw "E2E assertion failed: $Message"
  }
}

function Format-Json {
  param([string]$Content)

  if ([string]::IsNullOrWhiteSpace($Content)) {
    return "{}"
  }
  return ($Content | ConvertFrom-Json | ConvertTo-Json -Depth 20)
}

function Add-ReportLine {
  param([string]$Line = "")

  [void]$report.AppendLine($Line)
}

function Add-TraceStep {
  param(
    [string]$Title,
    [string]$Endpoint,
    [string]$Received,
    [string[]]$Processed,
    [pscustomobject]$Response,
    [string[]]$Assertions
  )

  Add-ReportLine "## $Title"
  Add-ReportLine
  Add-ReportLine ("**Endpoint:** ``{0}``" -f $Endpoint)
  Add-ReportLine
  Add-ReportLine "### Recebido"
  Add-ReportLine '```json'
  Add-ReportLine (Format-Json $Received)
  Add-ReportLine '```'
  Add-ReportLine
  Add-ReportLine "### Processado"
  foreach ($item in $Processed) {
    Add-ReportLine "- $item"
  }
  Add-ReportLine
  Add-ReportLine "### Output"
  Add-ReportLine "HTTP $($Response.StatusCode)"
  Add-ReportLine '```json'
  Add-ReportLine (Format-Json $Response.Content)
  Add-ReportLine '```'
  Add-ReportLine
  Add-ReportLine "### Assercoes"
  foreach ($assertion in $Assertions) {
    Add-ReportLine "- PASS: $assertion"
  }
  Add-ReportLine
}

try {
  if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    throw "Docker CLI was not found. Install or start Docker Desktop before running this script."
  }

  # This Compose file and volume are dedicated to E2E. The developer stack on 8205 is untouched.
  Invoke-Compose -Arguments @("down", "--volumes", "--remove-orphans")
  Invoke-Compose -Arguments @("up", "--detach", "--build")

  $health = $null
  $deadline = (Get-Date).AddSeconds(90)
  while ((Get-Date) -lt $deadline) {
    try {
      $health = Invoke-ApiRequest -Method "GET" -Path "/actuator/health" -Payload $null -ExpectedStatus @(200)
      if (($health.Content | ConvertFrom-Json).status -eq "UP") {
        break
      }
    } catch {
      Start-Sleep -Seconds 2
    }
  }
  if ($null -eq $health -or ($health.Content | ConvertFrom-Json).status -ne "UP") {
    throw "The E2E service did not become healthy within 90 seconds."
  }

  $today = (Get-Date).Date
  $territoryPayload = [ordered]@{
    code = "E2E-APS-001"
    name = "Territorio E2E Gestao Ativa"
    unitName = "UBS E2E"
    linkedPopulationPercent = 42.00
    dataCompetence = "2026-06"
    indicators = @(
      [ordered]@{ focus = "CHRONIC_CONDITIONS"; score = 32.00; target = 60.00 },
      [ordered]@{ focus = "PRENATAL_CARE"; score = 72.00; target = 85.00 }
    )
  } | ConvertTo-Json -Depth 10

  $createdTerritory = Invoke-ApiRequest -Method "POST" -Path "/api/v1/territories" -Payload $territoryPayload -ExpectedStatus @(201)
  $territoryOutput = $createdTerritory.Content | ConvertFrom-Json
  Assert-Condition ($territoryOutput.priority.level -eq "HIGH") "The territory must be HIGH priority."
  Assert-Condition ($territoryOutput.priority.reasons.Count -eq 3) "The priority explanation must contain linkage and two indicator reasons."
  $territoryId = $territoryOutput.id

  $actionPayload = [ordered]@{
    focus = "CHRONIC_CONDITIONS"
    objective = "Reconnect people with chronic conditions to preventive follow-up"
    responsibleTeam = "ESF E2E"
    plannedStart = $today.ToString("yyyy-MM-dd")
    plannedEnd = $today.AddDays(7).ToString("yyyy-MM-dd")
    targetCount = 80
    notes = "E2E aggregate demonstration. No patient records."
  } | ConvertTo-Json -Depth 10

  $createdAction = Invoke-ApiRequest -Method "POST" -Path "/api/v1/territories/$territoryId/actions" -Payload $actionPayload -ExpectedStatus @(201)
  $actionOutput = $createdAction.Content | ConvertFrom-Json
  Assert-Condition ($actionOutput.status -eq "PLANNED") "A new action must start as PLANNED."
  Assert-Condition ($actionOutput.performedCount -eq 0) "A new action must start with zero performed contacts."
  $actionId = $actionOutput.id

  $progressPayload = [ordered]@{
    status = "IN_PROGRESS"
    performedCount = 54
    resultNotes = "54 aggregate contacts completed."
  } | ConvertTo-Json -Depth 10

  $updatedAction = Invoke-ApiRequest -Method "PATCH" -Path "/api/v1/actions/$actionId/progress" -Payload $progressPayload -ExpectedStatus @(200)
  $updatedActionOutput = $updatedAction.Content | ConvertFrom-Json
  Assert-Condition ($updatedActionOutput.status -eq "IN_PROGRESS") "The action must become IN_PROGRESS."
  Assert-Condition ($updatedActionOutput.progressPercent -eq 67.50) "Progress must equal 67.50 percent."

  $invalidCompletionPayload = [ordered]@{
    status = "COMPLETED"
    performedCount = 0
    resultNotes = ""
  } | ConvertTo-Json -Depth 10

  $invalidCompletion = Invoke-ApiRequest -Method "PATCH" -Path "/api/v1/actions/$actionId/progress" -Payload $invalidCompletionPayload -ExpectedStatus @(422)
  $invalidCompletionOutput = $invalidCompletion.Content | ConvertFrom-Json
  Assert-Condition ($invalidCompletionOutput.status -eq 422) "Completion without contacts must return 422."

  $territoryDetails = Invoke-ApiRequest -Method "GET" -Path "/api/v1/territories/$territoryId" -Payload $null -ExpectedStatus @(200)
  $territoryDetailsOutput = $territoryDetails.Content | ConvertFrom-Json
  Assert-Condition ($territoryDetailsOutput.actions.Count -eq 1) "The created action must be persisted."
  Assert-Condition ($territoryDetailsOutput.actions[0].performedCount -eq 54) "The persisted action must retain 54 contacts."
  Assert-Condition ($territoryDetailsOutput.actions[0].status -eq "IN_PROGRESS") "The rejected completion must not mutate the action."

  $dashboard = Invoke-ApiRequest -Method "GET" -Path "/api/v1/dashboard" -Payload $null -ExpectedStatus @(200)
  $dashboardOutput = $dashboard.Content | ConvertFrom-Json
  Assert-Condition ($dashboardOutput.highPriorityTerritoryCount -eq 1) "The dashboard must show one high-priority territory."
  Assert-Condition ($dashboardOutput.openActionCount -eq 1) "The dashboard must show one open action."

  New-Item -ItemType Directory -Path $reportDirectory -Force | Out-Null
  Add-ReportLine "# Relatorio de execucao E2E - APS"
  Add-ReportLine
  Add-ReportLine "Gerado em: $(Get-Date -Format 'yyyy-MM-ddTHH:mm:ssK')"
  Add-ReportLine
  Add-ReportLine ("Este fluxo usa a API real no Docker em ``{0}``, PostgreSQL dedicado e uma base limpa." -f $BaseUrl)
  Add-ReportLine "Nenhum dado individual, prontuario ou identificador de paciente e enviado."
  Add-ReportLine
  Add-ReportLine "## Ambiente"
  Add-ReportLine
  Add-ReportLine '- Compose: `docker-compose.e2e.yml`'
  Add-ReportLine '- Servico: `aps-e2e-service` na porta 8215'
  Add-ReportLine '- Banco: `aps-e2e-postgres`, schema criado pelo Flyway'
  Add-ReportLine "- Massa demonstrativa automatica: desabilitada"
  Add-ReportLine "- Healthcheck: HTTP $($health.StatusCode), status UP"
  Add-ReportLine

  Add-TraceStep -Title "1. Criacao de territorio prioritario" -Endpoint "POST /api/v1/territories" -Received $territoryPayload -Processed @(
    "CreateTerritoryUseCase valida o codigo e persiste o territorio no PostgreSQL.",
    "PriorityCalculator compara vinculo 42% com a meta de 50%.",
    "Os indicadores de condicoes cronicas (32%/60%) e prenatal (72%/85%) tambem ficam abaixo da meta.",
    "A combinacao dos dois sinais resulta em prioridade HIGH com motivos explicitos."
  ) -Response $createdTerritory -Assertions @(
    "HTTP 201 Created.",
    "priority.level = HIGH.",
    "priority.reasons contem os tres sinais esperados."
  )

  Add-TraceStep -Title "2. Criacao de acao territorial" -Endpoint "POST /api/v1/territories/{territoryId}/actions" -Received $actionPayload -Processed @(
    "CreateSearchActionUseCase confirma que o territorio existe.",
    "SearchAction cria a acao com status PLANNED e performedCount igual a zero.",
    "O adapter JPA persiste meta, prazo, equipe e foco no PostgreSQL."
  ) -Response $createdAction -Assertions @(
    "HTTP 201 Created.",
    "status = PLANNED.",
    "performedCount = 0."
  )

  Add-TraceStep -Title "3. Registro de progresso agregado" -Endpoint "PATCH /api/v1/actions/{actionId}/progress" -Received $progressPayload -Processed @(
    "UpdateSearchActionProgressUseCase recupera a acao persistida.",
    "SearchAction aceita 54 contatos agregados e altera o status para IN_PROGRESS.",
    "O output calcula progressPercent como 54 / 80, equivalente a 67,50%."
  ) -Response $updatedAction -Assertions @(
    "HTTP 200 OK.",
    "status = IN_PROGRESS.",
    "progressPercent = 67.50."
  )

  Add-TraceStep -Title "4. Bloqueio de conclusao invalida" -Endpoint "PATCH /api/v1/actions/{actionId}/progress" -Received $invalidCompletionPayload -Processed @(
    "SearchAction rejeita COMPLETED quando performedCount e zero.",
    "ApsExceptionHandler traduz a regra de dominio para RFC 9457 com status 422.",
    "A acao existente nao e alterada pela requisicao invalida."
  ) -Response $invalidCompletion -Assertions @(
    "HTTP 422 Unprocessable Entity.",
    "O problema retornado informa erro de validacao."
  )

  Add-TraceStep -Title "5. Leitura persistida do territorio" -Endpoint "GET /api/v1/territories/{territoryId}" -Received "{}" -Processed @(
    "GetTerritoryDetailsUseCase le territorio, indicadores e acoes do PostgreSQL.",
    "A prioridade e recalculada em tempo de consulta; ela nao e uma coluna persistida.",
    "A acao permanece IN_PROGRESS com 54 contatos porque a conclusao invalida foi recusada."
  ) -Response $territoryDetails -Assertions @(
    "HTTP 200 OK.",
    "A acao criada esta presente no retorno.",
    "performedCount = 54 e status = IN_PROGRESS apos a tentativa invalida."
  )

  Add-TraceStep -Title "6. Painel apos o ciclo operacional" -Endpoint "GET /api/v1/dashboard" -Received "{}" -Processed @(
    "GetDashboardUseCase consolida prioridades e acoes abertas a partir do PostgreSQL.",
    "O painel identifica um territorio HIGH e uma acao ainda aberta."
  ) -Response $dashboard -Assertions @(
    "HTTP 200 OK.",
    "highPriorityTerritoryCount = 1.",
    "openActionCount = 1."
  )

  $containerLogs = (& docker compose -f $composeFile logs --timestamps --tail 20 aps-e2e-service | Out-String).Trim()
  if ($LASTEXITCODE -ne 0) {
    throw "Could not collect E2E container logs."
  }
  Add-ReportLine "## Logs tecnicos do container"
  Add-ReportLine
  Add-ReportLine "Os logs abaixo comprovam inicializacao e migracao. A trilha de negocio acima e produzida pelo executor HTTP, pois o dominio nao registra dados operacionais sensiveis em logs."
  Add-ReportLine
  Add-ReportLine '```text'
  Add-ReportLine $containerLogs
  Add-ReportLine '```'
  Add-ReportLine
  Add-ReportLine "## Resultado"
  Add-ReportLine
  Add-ReportLine "PASS: o fluxo E2E executou contra a aplicacao Docker e PostgreSQL dedicados."
  Add-ReportLine "O relatorio mostra, para cada etapa, o que a API recebeu, a regra/caso de uso aplicado e o output efetivamente retornado."

  [System.IO.File]::WriteAllText(
    $reportPath,
    $report.ToString().TrimEnd(),
    [System.Text.UTF8Encoding]::new($false))
  Write-Host "E2E completed successfully. Report: $reportPath"
} finally {
  $httpClient.Dispose()
}
