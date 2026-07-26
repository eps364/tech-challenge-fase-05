# Roteiro de regravação do pitch - SUS Conecta

Roteiro sincronizado com `apresentacao_aps.html`.

## Como usar sem parecer leitura

- Use cada parágrafo como uma ideia, e não como uma frase para decorar.
- Olhe rapidamente para o roteiro, volte para a câmera e conte a ideia com suas
  palavras.
- Nas telas com números, aponte primeiro para o slide e depois explique o que
  chamou sua atenção.
- As expressões "a gente", "na prática" e "o ponto aqui" são intencionais:
  deixam a fala mais próxima de uma conversa.
- Se trocar uma palavra durante a gravação, continue. O sentido é mais
  importante do que repetir o texto exatamente.

## Controle de tempo

- Fala planejada: aproximadamente **7 minutos**.
- Gravação esperada, com pausas e troca de slides: **7 min 10 s a 7 min 30 s**.
- Limite de segurança para encerrar: **7 min 45 s**.
- Limite do enunciado: **8 minutos**.

> Não tente preencher todo o tempo disponível. A margem final absorve pausas,
> pequenas hesitações e o tempo de troca de tela.

## O que é real e o que é demonstrativo

| Elemento apresentado | Classificação | Como explicar |
|---|---|---|
| Bases públicas do IBGE, SISAB/Previne Brasil e Dados Abertos do SUS | Real | São dados oficiais, públicos e agregados |
| Números nacionais exibidos nos slides | Real, calculado pelo projeto | São resultados reproduzíveis da análise das bases oficiais |
| Jardim Esperança, Parque das Flores, Vila Nova e Centro | Fictício | São territórios criados apenas para demonstrar o produto |
| Nomes de UBS, indicadores, metas, equipes e ações desses territórios | Simulado | Não representam municípios, unidades ou pessoas reais |

## Tipos de dados utilizados

| Tipo de dado agregado | Fonte principal | Uso no projeto |
|---|---|---|
| População estimada por município | IBGE, estimativa de 2025 | Dimensionar os territórios e calcular indicadores relativos |
| Cadastros vinculados por município | SISAB, dezembro de 2024 | Construir uma aproximação de cobertura de vínculo |
| Percentuais de indicadores preventivos | SISAB/Previne Brasil, 3º quadrimestre de 2024 | Identificar lacunas agregadas de acompanhamento preventivo |
| Quantidade de UBS e estabelecimentos | Dados Abertos do SUS | Acrescentar contexto de estrutura territorial |
| Leitos e UTI | Dados Abertos do SUS | Análise exploratória de contexto; não entram na prioridade do produto |

As bases possuem competências diferentes. A análise é um recorte exploratório,
não um painel em tempo real e não uma medição de risco individual, qualidade
assistencial, fila de espera ou impacto causal.

---

## Slide 1 - O que é o SUS Conecta

**Tempo-alvo: 0:00 a 0:55**

> Oi, eu sou Luiz Saraiva. Eu e o Emerson Silva desenvolvemos juntos o SUS
> Conecta para a Fase 5 do Tech Challenge.
>
> Antes dos dados, o projeto em uma frase: o SUS Conecta ajuda coordenadores da
> Atenção Primária e de UBS a decidir qual território deve receber primeiro uma
> ação preventiva.
>
> Ele reúne indicadores agregados, organiza os territórios por prioridade,
> explica os motivos dessa ordem e ajuda a transformar a decisão em uma ação
> com equipe, prazo, meta e acompanhamento.
>
> A pergunta que ele responde é simples: se não dá para atuar em todos os
> lugares ao mesmo tempo, onde começar e por quê?
>
> Ele não escolhe pacientes nem toma decisões clínicas. A busca ativa continua
> sendo um trabalho da própria equipe; o sistema só dá à gestão um ponto de
> partida territorial claro.

**[AVANCE]**

---

## Slide 2 - De onde vieram os dados

**Tempo-alvo: 0:55 a 2:00**

> Para entender se essa necessidade realmente tinha escala, a gente analisou
> dados públicos, oficiais e sempre agregados.
>
> A primeira base foi a estimativa populacional do IBGE de 2025. Ela permitiu
> comparar municípios de tamanhos diferentes. Depois usamos os cadastros
> vinculados à APS do SISAB, de dezembro de 2024, e os indicadores preventivos
> do SISAB/Previne Brasil, do terceiro quadrimestre de 2024.
>
> Também analisamos dados de UBS e estabelecimentos dos Dados Abertos do SUS
> para entender a estrutura disponível. Leitos e UTI entraram apenas na análise
> exploratória e não fazem parte da prioridade do produto.
>
> Os números desta tela são resultados reais da nossa análise: 5.571
> municípios, cerca de 213,4 milhões de habitantes e 100.242 registros brutos
> de indicadores. A média ponderada desses indicadores ficou em 47,34, numa
> escala de zero a cem.
>
> Como as bases são de períodos diferentes, esse é um recorte exploratório, não
> uma fotografia do SUS em tempo real.

**[AVANCE]**

---

## Slide 3 - A necessidade revelada pelos dados

**Tempo-alvo: 2:00 a 3:05**

> Depois de reunir as fontes, a gente relacionou as bases pelo código do
> município, padronizou os percentuais e aplicou critérios iguais em todo o
> conjunto.
>
> Também processamos 47.839 registros da base de UBS e 46.086 da base de
> cadastro vinculado. Esses volumes não representam pessoas atendidas.
>
> O que chamou nossa atenção foi o seguinte: a aproximação nacional de vínculo
> com a APS ficou em 38,11%. Entre os municípios com pelo menos 20 mil
> habitantes, 1.091 ficaram abaixo de 50%. Nesse mesmo recorte, 276 tiveram
> média inferior a 40% nos indicadores preventivos.
>
> A necessidade percebida não foi a falta de dados. Eles já existem, mas estão
> separados em fontes e períodos diferentes. O desafio é transformar esses
> sinais em uma decisão: onde olhar primeiro, por quê e como acompanhar a
> resposta.
>
> Isso indica uma oportunidade de organização territorial, não uma conclusão
> sobre qualidade do atendimento ou um problema clínico.

**[AVANCE]**

---

## Slide 4 - Como a solução responde

**Tempo-alvo: 3:05 a 4:10**

> A partir dessa necessidade, a gente criou uma fila de territórios que mostra
> onde começar e por que cada local está naquela posição.
>
> Jardim Esperança e os demais territórios do painel são fictícios. Nomes de
> UBS, percentuais, metas e equipes também foram simulados.
>
> A regra é simples: vínculo baixo junto com pelo menos um indicador preventivo
> abaixo da meta gera prioridade alta. Apenas um desses sinais gera prioridade
> média. Se todos atingem as referências, a prioridade é baixa.
>
> Em Jardim Esperança, o vínculo está em 42%, para uma meta de 50%. Condições
> crônicas está em 32%, para uma meta de 60%, e o acompanhamento pré-natal está
> em 72%, para uma meta de 85%.
>
> Por isso ele aparece com prioridade alta. O painel apresenta indicadores,
> metas e motivos, mas a decisão final continua sendo da coordenação.

**[AVANCE]**

---

## Slide 5 - Da prioridade para a ação

**Tempo-alvo: 4:10 a 5:15**

> Identificar a prioridade é só o começo. A informação precisa virar uma ação
> que a equipe consiga executar e acompanhar.
>
> A coordenação registra o foco preventivo, o objetivo, a equipe, o período e
> uma meta agregada. Assim, um indicador abaixo da meta vira um trabalho
> organizado.
>
> No exemplo, a equipe de Saúde da Família tem sete dias para realizar uma ação
> relacionada ao acompanhamento de condições crônicas. A meta é de 80 contatos
> agregados, e 54 já foram registrados. Isso representa 67,5% da execução
> planejada.
>
> Esses números são fictícios. O progresso mostra a execução da ação; não
> identifica quem foi contatado nem significa melhora clínica.
>
> A coordenação consegue organizar a resposta e voltar ao painel para acompanhar
> o que está aberto, em andamento ou concluído.

**[AVANCE]**

---

## Slide 6 - Como a solução funciona

**Tempo-alvo: 5:15 a 6:00**

> O funcionamento pode ser resumido em cinco passos.
>
> Primeiro, entram os indicadores agregados do território. Depois, o sistema
> compara os valores com as metas e organiza a fila de prioridades. A
> coordenação abre o território, entende os motivos e escolhe onde atuar.
>
> A prioridade escolhida vira uma ação com foco, equipe, prazo e meta. Por fim,
> o resultado agregado volta para o painel, permitindo acompanhar o trabalho.
>
> A tecnologia organiza a informação, mas a validação do contexto e a decisão
> continuam com quem conhece o território.

**[AVANCE]**

---

## Slide 7 - Valor e impacto esperado

**Tempo-alvo: 6:00 a 6:50**

> O principal diferencial é justamente conectar três coisas que normalmente
> ficam separadas: o dado, a decisão e a execução.
>
> Para a gestão, a expectativa é reduzir o trabalho com planilhas e tornar a
> prioridade mais fácil de justificar. Para a UBS, ficam claros o foco, a meta
> e o prazo, sem usar dados pessoais na priorização.
>
> O impacto ainda é uma hipótese que precisa ser validada. Em um piloto, a
> gente mediria o tempo necessário para priorizar os territórios e a proporção
> de ações concluídas com resultado registrado.
>
> Neste momento, a proposta é melhorar a organização da resposta territorial.
> A gente não afirma impacto clínico nem causal.

**[AVANCE]**

---

## Slide 8 - Encerramento

**Tempo-alvo: 6:50 a 7:20**

> O próximo passo é validar a solução com coordenadores e ajustar as metas à
> realidade local.
>
> Em resumo, o SUS Conecta transforma dados públicos e agregados em uma
> prioridade explicável e em uma ação acompanhável. Ele ajuda a responder onde
> atuar primeiro, por quê e como acompanhar o trabalho, sem expor dados
> individuais e sem substituir a equipe de saúde. Obrigado.

---

## Frases curtas para responder à banca

### Os dados são reais?

> As bases e os números nacionais da análise são reais, públicos e agregados.
> Já os territórios e as ações da demonstração foram criados por nós e são
> fictícios.

### O sistema prioriza pacientes?

> Não. Ele olha para territórios e unidades usando indicadores agregados. Não
> existe score individual, prontuário ou decisão clínica.

### O score usa inteligência artificial?

> Não nesta versão. A regra é direta e pode ser auditada: ela compara o
> indicador com a meta e mostra por que chegou aquela prioridade.

### O produto prova impacto na saúde?

> Ainda não. O MVP apresenta uma hipótese de ganho operacional. Impacto na
> saúde só pode ser avaliado em um piloto adequado.

### Por que as competências das bases são diferentes?

> Porque usamos as publicações oficiais que estavam disponíveis, e cada fonte
> tem seu próprio calendário. Por isso a análise é um recorte exploratório, não
> um retrato em tempo real.

## Expressões que devem ser evitadas

Evitar:

- "pacientes de risco";
- "o sistema decide";
- "o produto comprova que";
- "dados em tempo real";
- "previne internações";
- "a IA escolhe quem será atendido".

Preferir:

- "territórios prioritários";
- "apoio à decisão";
- "a análise sugere";
- "recorte exploratório";
- "impacto esperado";
- "regra determinística e explicável".

## Checklist de gravação

- Apresentar os dois participantes apenas uma vez, na abertura.
- Manter um cronômetro visível e avançar o slide nos marcadores `[AVANCE]`.
- Aos 4 minutos e 10 segundos, iniciar o slide 5.
- Aos 6 minutos e 50 segundos, iniciar o encerramento.
- Se houver atraso, cortar detalhes do slide 6; nunca acelerar a conclusão.
- Encerrar até 7 minutos e 45 segundos para preservar margem.
- Não abrir código, terminal ou documentação técnica neste vídeo de pitch.
- Gravar o vídeo técnico separadamente, seguindo
  `roteiro-video-mvp-tecnico.md`.
