# Avaliacao do video de pitch e conformidade da entrega

Data da avaliacao: 2026-07-26.

Materiais conferidos:

- [video publicado no YouTube](https://www.youtube.com/watch?v=J10DTc7Rg7U);
- [enunciado original do hackathon](../contexto/hackathon/enunciado-original.pdf),
  especialmente as paginas 3 a 6;
- transcricao disponibilizada pelo YouTube;
- documentacao, codigo, analises e evidencias E2E deste repositorio.

## Parecer executivo

O pitch apresenta uma proposta coerente, responsavel e bem conectada ao
problema de gestao territorial da APS. A narrativa explica o problema, usa
evidencias agregadas, apresenta uma regra transparente e preserva corretamente
o limite entre apoio operacional e decisao clinica.

O principal problema e objetivo: o video tem **8 minutos e 51 segundos**, mas o
enunciado limita o pitch a **no maximo 8 minutos**. Portanto, na forma atual, o
video nao atende ao limite. A recomendacao e entregar uma versao entre 7
minutos e 20 segundos e 7 minutos e 40 segundos, mantendo margem de seguranca.

Tambem existem riscos de entrega fora do video:

1. nao existe um relatorio unico que consolide todos os itens obrigatorios do
   enunciado;
2. nao foi localizado um documento `.txt` ou `.doc` de entrega com o link para
   um Drive publico contendo todos os materiais.

O desenvolvimento completo esta na branch publica
`codex-oportunidades-sus-analise`. A equipe informou que o merge para `main`
sera feito quando os materiais estiverem finalizados, portanto isso e uma
etapa planejada de fechamento e nao um risco atual.

## O que funciona bem no pitch

### Problema e usuario

O video identifica uma decisao concreta: o coordenador de APS precisa escolher
qual territorio deve receber primeiro uma acao de busca ativa e entender o
motivo. Isso e mais forte do que apresentar apenas um conjunto generico de
indicadores.

### Evidencias e limites

O pitch usa IBGE, SISAB e Dados Abertos do Ministerio da Saude para sustentar a
relevancia da oportunidade. Ele tambem faz uma distincao correta:

- os resultados nacionais sao evidencias agregadas;
- a leitura e uma interpretacao territorial;
- a utilidade para o coordenador ainda e uma hipotese a validar;
- o produto nao diagnostica, nao prediz agravamento e nao prova impacto
  clinico.

Essa disciplina de comunicacao e um dos pontos mais maduros da apresentacao.

### Solucao e regra explicavel

O exemplo de Jardim Esperanca torna a proposta compreensivel:

- vinculo APS de 42% para meta de 50%;
- condicoes cronicas em 32% para meta de 60%;
- pre-natal em 72% para meta de 85%;
- prioridade `HIGH` pela combinacao de baixo vinculo com ao menos um indicador
  preventivo abaixo da meta.

O video deixa claro que a prioridade e uma fila operacional, nao uma
classificacao de risco clinico.

### Fechamento do ciclo

A passagem de prioridade para acao, e de acao para progresso agregado, e a
melhor parte da proposta de valor. O exemplo de 54 contatos para uma meta de 80
mostra que o sistema nao para no alerta: ele acompanha a execucao.

### Descricao do YouTube

A descricao publicada esta adequada. Ela identifica a dupla, resume a
proposta, cita fontes agregadas e declara os limites de privacidade e
interpretacao.

## O que deve ser melhorado

### 1. Duracao acima do limite

Este e o unico ponto objetivamente invalido no video:

| Item | Regra | Situacao encontrada |
| --- | --- | --- |
| Video do pitch | Maximo de 8 minutos | **8:51 - nao conforme** |

Nao e recomendavel contar com tolerancia da banca. O alvo deve ser inferior a
7:40, e nao 7:59.

### 2. Distribuicao do tempo

A divisao aproximada observada foi:

| Bloco | Intervalo aproximado | Duracao |
| --- | --- | --- |
| Introducao e problema | 0:02 a 1:09 | 1:07 |
| Fontes, metodo e resultados | 1:09 a 4:09 | 3:00 |
| Solucao e regra do MVP | 4:09 a 7:07 | 2:58 |
| Impacto | 7:07 a 8:07 | 1:00 |
| Proximo passo e encerramento | 8:07 a 8:51 | 0:44 |

O bloco de dados ocupa tempo demais para um pitch. A analise e importante, mas
deve sustentar a historia em cerca de 60 a 90 segundos. O tempo recuperado deve
reforcar diferencial, impacto e proximos passos.

### 3. Funcoes dos integrantes

O video apresenta Luiz Saraiva e Emerson Silva, mas nao informa a funcao de
cada integrante. O enunciado sugere explicitamente nome e funcao. A versao
final deve incluir uma frase curta com a divisao real de responsabilidades,
sem inventar papeis.

### 4. Diferencial ainda implicito

O diferencial aparece de forma indireta quando o video contrasta planilhas
dispersas com uma fila territorial explicavel. Vale torna-lo explicito:

> O diferencial nao e apenas exibir indicadores. E transformar sinais
> agregados em uma prioridade explicavel e ligar essa decisao a uma acao
> acompanhavel, sem usar dados individuais.

### 5. Proximos passos pouco desenvolvidos

O pitch menciona corretamente a validacao com coordenadores e equipes de UBS,
mas quase nao apresenta evolucoes futuras. Uma versao mais forte pode citar,
de forma breve:

- validar metas e linguagem com coordenadores locais;
- testar a rotina com um conjunto agregado fornecido por uma rede parceira;
- avaliar integracao futura apenas com governanca, seguranca e dados
  territoriais adequados.

Esses itens devem ser apresentados como proximos experimentos, nao como
funcionalidades ja entregues.

### 6. Fluidez da fala

A transcricao registra limpeza de garganta por volta de 0:23 e varias
repeticoes, como "devemos devemos", "com equipe, com equipe", "ela ela" e "em
forma de, em forma agregada". Isso alonga o video e reduz a sensacao de
seguranca.

A transcricao automatica tambem confundiu termos como IBGE, SISAB, APS e UBS.
Como reconhecimento automatico pode errar, isso nao prova pronuncia incorreta,
mas indica que vale articular esses termos mais lentamente.

Recomendacao pratica: gravar por blocos curtos, remover hesitacoes na edicao e
usar pausas silenciosas entre slides.

### 7. Acabamento visual

O deck tem identidade consistente, bons titulos e hierarquia clara. Entretanto,
a gravacao mostra abas e barra do navegador, barra do Windows e elementos do
desktop. Os textos secundarios e rodapes tambem ficam pequenos no player
normal do YouTube.

Para uma nova gravacao:

- usar modo de tela inteira;
- ocultar barra do Windows e notificacoes;
- fechar abas e aplicativos pessoais;
- ampliar textos que precisem ser lidos;
- evitar que legendas cubram a informacao principal;
- gravar em 1920 x 1080, quando possivel.

## Auditoria segundo o enunciado

| Exigencia ou orientacao | Situacao | Acao recomendada |
| --- | --- | --- |
| Tema: inovacao para otimizar o atendimento no SUS | Conforme | Manter o recorte de gestao territorial da APS. |
| Equipe com no maximo 5 pessoas | Conforme | A dupla possui 2 integrantes. |
| Front-end nao obrigatorio | Conforme | API, arquitetura e demonstracao por cliente HTTP sao suficientes. |
| Pitch com no maximo 8 minutos | **Nao conforme** | Regravar ou reeditar para menos de 8 minutos. |
| Apresentar nome e funcao de cada membro | Parcial | Os nomes aparecem; faltam as funcoes reais. |
| Explicar problema e relevancia | Conforme | A decisao territorial esta clara e apoiada por dados. |
| Explicar solucao e funcionamento | Conforme | A regra e o ciclo prioridade-acao-progresso sao explicados. |
| Destacar diferencial | Parcial | Tornar explicito o elo entre prioridade explicavel e execucao. |
| Explicar impacto e casos de uso | Parcial | Beneficios aparecem; reforcar um caso operacional concreto. |
| Propor proximos passos | Parcial | A validacao aparece; incluir experimentos futuros breves. |
| Video do MVP com no maximo 8 minutos | Pendente | Usar o roteiro tecnico de aproximadamente 7:35. |
| Demonstracao pratica por Swagger, Postman ou equivalente | Pendente | O Bruno atende ao objetivo quando as chamadas forem gravadas. |
| Relatorio do projeto | **Risco** | Consolidar os documentos existentes em um relatorio unico. |
| Repositorio de codigo acessivel | Conforme no desenvolvimento | A branch completa e publica; concluir o merge planejado antes da entrega. |
| Documento `.txt` ou `.doc` com Drive publico | Nao verificado | Criar, testar em janela anonima e entregar pela plataforma FIAP. |

## Prioridades antes da entrega

### Criticas

1. Substituir o pitch de 8:51 por uma versao com menos de 8 minutos.
2. Criar o relatorio unico com os sete itens obrigatorios.
3. Preparar o `.txt` ou `.doc` com um Drive publico e testar todos os links
   fora da conta dos autores.

### Importantes

1. Informar a funcao real de cada integrante.
2. Tornar o diferencial explicito.
3. Gravar o video tecnico em tela inteira e com margem de tempo.
4. Mostrar uma chamada de erro `422` ou a evidencia E2E que comprova a regra de
   conclusao.

## Estrutura recomendada para uma versao curta do pitch

| Tempo | Conteudo |
| --- | --- |
| 0:00-0:45 | Equipe, funcoes, problema e pergunta central. |
| 0:45-1:45 | Tres numeros de evidencia, fontes e uma limitacao. |
| 1:45-4:30 | Solucao, regra, diferencial e ciclo de uso. |
| 4:30-6:15 | Beneficios e caso operacional de Jardim Esperanca. |
| 6:15-7:05 | Validacao e proximos passos. |
| 7:05-7:30 | Limites, frase final e agradecimento. |

O video tecnico deve permanecer separado do pitch, porque o enunciado solicita
dois videos distintos.
