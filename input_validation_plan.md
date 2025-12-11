# Plano de Input Validation – Projeto Partida de Futebol

## 1. Objetivo

Fortalecer a validação de entradas de dados (Input Validation) na API de gerenciamento de partidas de futebol, alinhada às práticas de segurança do MercadoLibre e às regras de uso de MCPs definidas em `mcp-usage.md`, sem quebrar a arquitetura nem as regras de negócio já existentes.

---

## 2. Escopo

- **Incluído**
- **Controllers HTTP**:
  - `ClubeController` (`/clubes`)
  - `EstadioController` (`/estadios`)
  - `PartidaController` (`/partidas`)
- **Camadas analisadas para validação**:
  - DTOs de entrada (ex.: `ClubeDto`, `EstadioDto`, `PartidaDto`)
  - Controllers (parâmetros simples – path/query)
  - Services (regras de negócio e consistência)

- **Excluído nesta fase**
- Autenticação/autorização.
- Criptografia, transporte seguro (HTTPS, certificados).
- Refatorações de arquitetura em grande escala.

---

## 3. Arquitetura atual e padrões a preservar

- **Stack**: Spring Boot 3.x, Java 17.
- **Padrão de camadas**:
  - Controller → Service (`business/`) → Repository (`infrastructure/`) → Entity (`infrastructure/entitys`) → DTO (`dto/`).
- **Princípios já existentes (a manter)**:
  - Uso de DTOs para expor/receber dados.
  - Regras de negócio concentradas em Services.
  - Controllers finos, delegando a Services.
  - Uso de `@RequiredArgsConstructor` para injeção.

Para Input Validation, o padrão-alvo será:
- **Sintaxe/formato/tamanho** → preferencialmente em DTOs + Bean Validation.
- **Regras de negócio/coerência entre campos** → Services.

---

## 4. Endpoints mapeados

### 4.1. `ClubeController` – `/clubes`

- **POST `/clubes`**
  - Body: `ClubeDto`.
- **PUT `/clubes/{id}`**
  - Path: `id` (Integer)
  - Body: `ClubeDto`.
- **DELETE `/clubes/{id}/inativar`**
  - Path: `id` (Integer).
- **GET `/clubes/{id}`**
  - Path: `id` (Integer).
- **GET `/clubes`**
  - Query params opcionais:
    - `nome` (String)
    - `uf` (String)
    - `dataCriacao` (LocalDate)
    - `ativo` (Boolean)
- **GET `/clubes/{id}/retrospecto`**
  - Path: `id` (Integer).
- **GET `/clubes/retrospecto`**
- **GET `/clubes/{id}/retrospecto/{adversarioId}`**
  - Path: `id`, `adversarioId` (Integer).
- **GET `/clubes/{id}/confronto/{adversarioId}`**
  - Path: `id`, `adversarioId` (Integer).
- **GET `/clubes/ranking`**
  - Query: `criterio` (String, default "pontos").

### 4.2. `EstadioController` – `/estadios`

- **POST `/estadios`**
  - Body: `EstadioDto`.
- **PUT `/estadios/{id}`**
  - Path: `id` (Integer)
  - Body: `EstadioDto`.
- **GET `/estadios/{id}`**
  - Path: `id` (Integer).
- **GET `/estadios`**
  - Query: `nome` (String, opcional).

### 4.3. `PartidaController` – `/partidas`

- **POST `/partidas`**
  - Body: `PartidaDto`.
- **PUT `/partidas/{id}`**
  - Path: `id` (Integer)
  - Body: `PartidaDto`.
- **DELETE `/partidas/{id}`**
  - Path: `id` (Integer).
- **GET `/partidas/{id}`**
  - Path: `id` (Integer).
- **GET `/partidas`**
  - Query params opcionais:
    - `clubeMandanteId` (Integer)
    - `clubeVisitanteId` (Integer)
    - `estadioPartidaId` (Integer)
    - `dataHora` (LocalDateTime)
    - `golsMandante` (Integer)
    - `golsVisitante` (Integer)

---

## 5. Fontes de entrada e pontos de validação

Nesta fase do plano, apenas **mapeamento conceitual** (sem listar ainda todos os campos de DTO). A próxima etapa detalhará campo a campo.

### 5.1. Tipos de entrada

- **Request Body (JSON)**
  - `ClubeDto`, `EstadioDto`, `PartidaDto` em POST/PUT.
- **Path Variables**
  - `id`, `adversarioId`, etc. (Integer).
- **Query Params**
  - Filtros de listagem (nome, uf, datas, IDs relacionados, critérios de ranking, etc.).

### 5.2. Locais esperados de validação

- **Bean Validation em DTOs**
  - Uso de anotações como `@NotNull`, `@Size`, `@Pattern`, `@Positive`, etc.
  - Controllers devem usar `@Valid` quando receberem DTOs.
- **Validações de negócio em Services**
  - Ex.: datas coerentes (não no futuro onde não faz sentido), clubes diferentes na partida, estádio existente, etc.
- **Tratamento de erros**
  - `ResponseStatusException` ou mapeamento global para violação de validação.

A análise de código posterior (Services/DTOs) vai classificar cada campo como:
- **Coberto** (validação suficiente).
- **Parcial** (algumas regras, mas faltam limites ou formatos).
- **Não validado**.

---

## 6. Diretrizes de Input Validation (MercadoLibre + boas práticas)

### 6.1. Princípios gerais

- **Whitelist > blacklist**:
  - Validar valores aceitos (ex.: UF com 2 letras válidas, enums para critérios de ranking).
- **Restrições de tamanho e formato**:
  - Strings com `@Size(min, max)`.
  - `@Pattern` para campos com formato fechado (quando aplicável).
- **Validação semântica**:
  - Datas coerentes (`LocalDate`, `LocalDateTime`).
  - Inteiros positivos para IDs, gols, etc.
- **Mensagens de erro seguras**:
  - Não vazar detalhes internos de banco ou infraestrutura.

### 6.2. Integração com MCPs MercadoLibre (conforme `mcp-usage.md`)

> Observação: no ambiente atual (Cascade), os MCPs não são diretamente invocáveis, mas o plano assume que, no Windsurf, você os utilizará como parte do fluxo de segurança.

- **`meli_appsec_codeguard`**
  - Usar para:
    - Revisar alterações de validação de entrada.
    - Detectar pontos vulneráveis a injeção, XSS, validação fraca.
  - Aplicar sempre que:
    - Novos endpoints forem criados.
    - Novas regras de validação forem adicionadas ou alteradas.

- **`genai_code_review`**
  - Usar para:
    - Revisão de código antes de merges importantes que mexam com Controllers/Services/DTOs.
    - Obter análise priorizada por severidade sobre mudanças de segurança.

- **`backend` (fury_for_development / API docs / specs)**
  - Relevante principalmente se/quando esta API interagir com serviços Fury oficiais.
  - A política é: consultar `search_sdk_docs`, `search_api_docs` ou `search_api_specs` **antes** de implementar integrações com APIs Fury.

---

## 7. Estratégia de validação incremental (visão macro)

A ser aplicada nas próximas fases (sem ainda alterar código):

1. **DTOs primeiro**
   - Mapear campos de `ClubeDto`, `EstadioDto`, `PartidaDto`.
   - Para cada campo:
     - Definir obrigatoriedade (`@NotNull`/`@NotBlank` quando aplicável).
     - Definir tamanho máximo razoável de strings.
     - Definir formatos/esquemas (ex.: padrões de data se entrassem como string).

2. **Controllers: parâmetros simples**
   - Validar que:
     - IDs de path são positivos (quando fizer sentido).
     - Query params que representam enums usem um conjunto conhecido de valores.
     - Datas em query são parseadas corretamente, com tratamento de erro consistente.

3. **Services: coerência de regra de negócio**
   - Reforçar regras já existentes e complementar onde faltar:
     - Clubes diferentes na partida (mandante ≠ visitante).
     - Datas de partida coerentes com datas de criação de clubes/estádios.
     - Não permitir valores negativos para gols.

4. **Tratamento uniforme de erros**
   - Padronizar respostas para violações de validação.
   - Separar mensagens internas (log) de mensagens externas (API).

5. **Ciclo com MCPs** (no Windsurf)
   - Após implementar lote de validações (por módulo):
     - Rodar `meli_appsec_codeguard` focado no módulo alterado.
     - Rodar `genai_code_review` sobre o diff/branch.

---

## 8. Próximas fases (planejamento)

### Fase A – Detalhar campos e validações atuais

- Inspecionar `ClubeDto`, `EstadioDto`, `PartidaDto` e Services relacionados.
- Para cada endpoint/campo:
  - Classificar validação (coberta/parcial/não validada).
  - Identificar riscos (alto/médio/baixo).

### Fase B – Definir mudanças propostas

- Para cada lacuna de validação:
  - Propor anotações Bean Validation específicas.
  - Propor validações adicionais em Services (quando for regra de negócio).
  - Listar testes unitários recomendados (casos válidos e inválidos).

### Fase C – Implementação incremental

- Implementar as validações em pequenos lotes (por controller/módulo).
- Após cada lote:
  - Rodar testes.
  - No Windsurf, rodar `meli_appsec_codeguard` + `genai_code_review`.

---

Este plano ainda é **puramente descritivo**. As próximas iterações vão detalhar campo a campo e, só depois da sua aprovação, partir para mudanças de código e criação de testes unitários para cada nova validação.
