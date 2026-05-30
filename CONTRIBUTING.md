Guia de Contribuição — Mobix

## Estratégia de Branches

```
main       → código estável, só recebe merge ao fim de cada sprint
develop    → branch de integração da sprint atual
feat/      → branches individuais por issue
```

> **Regra:** `main` é protegida. Todo código passa por PR revisado antes do merge.

### Nomenclatura de branches
```bash
git checkout develop
git checkout -b feat/issue-08-veiculo-dao
```

---

## Fluxo por Issue

```bash
# 1. Partir sempre da develop atualizada
git checkout develop && git pull origin develop

# 2. Criar branch da issue
git checkout -b feat/issue-09-cartao-dao

# 3. Commitar referenciando a issue
git commit -m "feat: implementar CartaoDAO com busca e atualização de saldo #9"

# 4. Subir a branch
git push origin feat/issue-09-cartao-dao

# 5. Abrir Pull Request: feat/... → develop
# 6. Outro membro revisa e aprova
# 7. Merge — GitHub fecha a issue automaticamente
```

### Ao fim de cada sprint
```bash
git checkout main && git pull origin main
git merge develop && git push origin main
# Criar Release em: Releases → Draft a new release → tag: v1.0-sprint1
```

---

## Labels

| Label | Uso |
|---|---|
| `camada: model` | Classes, VOs, abstratas |
| `camada: dao` | Acesso a dados, SQL, JDBC |
| `camada: bo` | Regras de negócio |
| `camada: view` | Interfaces com o usuário |
| `infra` | Config, banco, conexão |
| `testes` | JUnit, Mockito |
| `documentacao` | README, diagramas |
| `épico: frota` | Épico 1 |
| `épico: bilhetagem` | Épico 2 |
| `épico: painel` | Épico 3 |

## Milestones

| Milestone | Meta |
|---|---|
| Sprint 1 | Modelo de Dados |
| Sprint 2 | Persistência JDBC |
| Sprint 3 | Regras de Negócio e Testes |
| Sprint 4 | Views e Fluxo Final |

---

## Backlog de Issues

### 🟦 Sprint 1 — Modelo de Dados

**#1 · `[INFRA]` Configurar estrutura de pacotes do projeto**
`infra`
* Criar pacotes: `view`, `bo`, `dao`, `model.usuario`, `model.cartao`, `interfaces`, `infra`, `exception`
* Configurar `pom.xml`: JUnit 5, Mockito, PostgreSQL JDBC Driver
* Adicionar `.gitignore` para `/target` e `/.idea`
* Criar árvore de pacotes principal e adicionar no README

**#2 · `[MODEL]` Criar classe abstrata Cartao e subclasses polimórficas**
`camada: model` `épico: bilhetagem`
* `Cartao` (abstract): `id`, `saldo`, `titular` | `adicionarSaldo()`, `debitar()`, `calcularTarifa()` abstract
* `CartaoComum`: sem desconto
* `CartaoEstudante`: 50% de desconto
* `CartaoIdoso`: isenção/gratuidade

**#3 · `[MODEL]` Criar VeiculoVO com atributos placa, capacidade e tipo**
`camada: model` `épico: frota`
* Atributos: `placa`, `capacidade`, `tipo`
* Construtor completo + getters
* Sem lógica de negócio

**#4 · `[MODEL]` Criar RotaVO com List\<String\> para paradas**
`camada: model` `épico: frota`
* Atributos: `id`, `nomeLinha`, `status`, `paradas (List<String>)`, `horarioEstimado`
* Construtor: `RotaVO(int id, String nomeLinha)`
* Métodos: `adicionarParada()`, `getParadas()`, `getStatus()`, `setStatus()`, `getHorarioEstimado()`, `setHorarioEstimado()`

**#5 · `[MODEL]` Criar hierarquia de usuários e interface Autenticavel**
`camada: model` `infra`
* Interface `Autenticavel`: `autenticar(String login, String senha) : boolean`
* `Usuario`: `login`, `senha`
* `Administrador extends Usuario implements Autenticavel`
* `Motorista extends Usuario implements Autenticavel` | atributo `cnh`

**#25 · `[MODEL]` Criar classe ParadaRotaVO para composição**
`camada: model` `épico: frota`
* Atributos: `id`, `idRota`, `nomeParada`, `ordem`
* Construtor completo + getters

**#35 · `[INFRA]` Criar docker-compose.yml para ambiente PostgreSQL**
`infra`
* Criar `docker-compose.yml` na raiz do projeto subindo container PostgreSQL 15 com credenciais padronizadas para toda a equipe
* Definir variáveis: `POSTGRES_DB=mobix`, `POSTGRES_USER=mobix`, `POSTGRES_PASSWORD=mobix`, porta `5432`
* Adicionar volume para persistência dos dados entre reinicializações do container
* Incluir instrução de uso no README: `docker-compose up -d`
* qualquer integrante executa `docker-compose up -d` e o banco sobe pronto para uso, sem configuração manual

---

### 🟨 Sprint 2 — Persistência JDBC

**#6 · `[INFRA]` Criar script SQL das tabelas**
`infra` `camada: dao`
* **Tabela `tb_veiculo`**: `placa` (PK), `capacidade`, `tipo`
* **Tabela `tb_cartao`**: `id` (PK), `saldo`, `titular`, `tipo`
* **Tabela `tb_rota`**: `id` (PK), `nome_linha`, `status`, `horario_estimado`
* **Tabela `tb_parada_rota`**: `id` (PK), `id_rota` (FK), `nome_parada`, `ordem`
* **Tabela `tb_monitoramento`**: `id` (PK), `viagem_id` (FK), `status`, `horario`
* tb_parada_rota a tabela não aparece no diagrama arquitetural do README

**#7 · `[INFRA]` Implementar ConnectionFactory**
`infra`
* Método estático `getConnection() : Connection`
* Tratamento de `SQLException`
* Critério de aceite: conexão abre e fecha sem erro com o banco local

**#8 · `[DAO]` Implementar VeiculoDAO**
`camada: dao` `épico: frota`
* `salvar(VeiculoVO) : boolean` → `INSERT INTO tb_veiculo`
* `buscarPorPlaca(String) : VeiculoVO` → `SELECT * FROM tb_veiculo WHERE placa = ?`
* Usar `PreparedStatement` e `ConnectionFactory`
* Decorar a classe com a anotação `@Repository` do Spring para permitir a injeção de dependência na camada BO

**#9 · `[DAO]` Implementar CartaoDAO**
`camada: dao` `épico: bilhetagem`
* `buscarPorId(int) : Cartao` → instancia subclasse correta pelo campo `tipo` (fábrica)
* `atualizarSaldo(Cartao) : boolean` → `UPDATE tb_cartao SET saldo = ? WHERE id = ?`
* Decorar a classe com a anotação `@Repository` do Spring para permitir a injeção de dependência na camada BO

**#10 · `[DAO]` Implementar RotaDAO**
`camada: dao` `épico: frota` `épico: painel`
* `buscarRotaPorId(int) : RotaVO`
* `atualizarStatusRota(int, String) : boolean`
* `atualizarHorarioEstimado(int, String) : boolean`
* `listarRotasAtivas() : List<RotaVO>` → `SELECT * FROM rotas INNER JOIN viagens ... WHERE status = 'EM_CURSO'`
* Decorar a classe com a anotação `@Repository` do Spring para permitir a injeção de dependência na camada BO
* Usar `PreparedStatement` e `ConnectionFactory` em todos os métodos

**#26 · `[DAO]` Implementar inserção de Cartão e gerenciamento de Paradas**
`camada: dao` `épico: bilhetagem` `épico: frota`
* `salvar(Cartao) : boolean` na CartaoDAO → `INSERT INTO tb_cartao (id, saldo, titular, tipo)`
* `salvarParadas(int idRota, List<String> paradas) : void` na RotaDAO
* `buscarParadasPorRota(int idRota) : List<String>` na RotaDAO
* CartaoDAO no diagrama não tem `salvar()` , `salvarParadas()`, `buscarParadasPorRota()`. Atualizar o diagrama de classes no README.
* Usar `PreparedStatement` e `ConnectionFactory` em todos os métodos

---

### 🟥 Sprint 3 — Regras de Negócio e Testes

**#11 · `[BO]` Implementar FrotaBO**
`camada: bo` `épico: frota` · **Depende de #8, #10**
* `cadastrarVeiculo(VeiculoVO)`: chama `buscarPorPlaca()` antes — rejeita duplicatas
* `ativarViagem(int idRota, String placa)`: busca rota, atualiza status para `EM_CURSO`
* Decorar a classe com a anotação `@Service` do Spring e injetar as DAOs correspondentes via construtor (`@Autowired`)

**#12 · `[BO]` Implementar BilhetagemBO**
`camada: bo` `épico: bilhetagem` · **Depende de #9, #14**
* `recarregarCartao(int, double)`: busca cartão, `adicionarSaldo()`, persiste
* `processarEmbarque(int, double)`: `calcularTarifa()`, `debitar()` → lança `SaldoInsuficienteException` se saldo insuficiente
* Decorar a classe com a anotação `@Service` do Spring e injetar as DAOs correspondentes via construtor (`@Autowired`)

**#13 · `[BO]` Implementar PainelBO**
`camada: bo` `épico: painel` · **Depende de #10**
* `atualizarStatusVeiculo(int, String)`
* `atualizarHorarioEstimado(int, String)`
* `buscarProximasPartidas() : List<RotaVO>`
* Decorar a classe com a anotação `@Service` do Spring e injetar as DAOs correspondentes via construtor (`@Autowired`)
* PainelBO no diagrama não tem `atualizarHorarioEstimado()`. Atualizar no diagrama de classes do README

**#14 · `[EXCEPTION]` Criar SaldoInsuficienteException**
`camada: bo` `épico: bilhetagem`
* `<<exception>>` extends `Exception`
* Construtor: `SaldoInsuficienteException(String mensagem)`

**#15 · `[TESTE]` Testes JUnit — polimorfismo de tarifa**
`testes` `épico: bilhetagem`
* `CartaoComumTest`, `CartaoEstudanteTest`, `CartaoIdosoTest`
* Verificar `calcularTarifa()`, `debitar()` e `adicionarSaldo()` para cada tipo

**#16 · `[TESTE]` Testes JUnit — BilhetagemBO com Mockito**
`testes` `épico: bilhetagem` · **Depende de #12, #14**
* Mock de `CartaoDAO`
* `processarEmbarque()` → saldo ok → débito realizado
* `processarEmbarque()` → saldo insuficiente → exceção lançada
* `recarregarCartao()` → saldo atualizado

**#17 · `[TESTE]` Testes JUnit — FrotaBO com Mockito**
`testes` `épico: frota` · **Depende de #11**
* Mock de `VeiculoDAO` e `RotaDAO`
* `cadastrarVeiculo()` → placa nova → salvo
* `cadastrarVeiculo()` → placa duplicada → não salva
* `ativarViagem()` → status `EM_CURSO`

**#27 · `[EXCEPTION]` Criar PersistenciaException**
`camada: bo` `infra`
* `<<exception>>` extends `RuntimeException`
* Construtor: `PersistenciaException(String mensagem, Throwable causa)`
* **Justificativa:** Permitirá que os métodos da DAO capturem a `SQLException` (checada) e lancem uma `PersistenciaException` (não-checada), limpando as assinaturas dos métodos (throws) na BO.

**#28 · `[BO]` Atualizar BilhetagemBO com Emissão de Cartão**
`camada: bo` `épico: bilhetagem` · **Depende de #26**
* `emitirCartao(Cartao)`: recebe o cartão preenchido da view e encaminha para `CartaoDAO.salvar(cartao)`
* `BilhetagemBO` no diagrama de classes não tem `emitirCartao()` O diagrama mostra apenas `recarregarCartao()` e `processarEmbarque()`. Atualizar o diagrama no README

---

### 🟩 Sprint 4 — Views e Fluxo Final

**#18 · `[VIEW]` Implementar CadastroVeiculoView**
`camada: view` `épico: frota` · **Depende de #11**
* Captura `placa`, `capacidade`, `tipo` via `Scanner`
* Chama `FrotaBO.cadastrarVeiculo()`
* Exibe confirmação ou mensagem de duplicidade

**#19 · `[VIEW]` Implementar PainelBordoView**
`camada: view` `épico: frota` · **Depende de #11**
* Captura `idRota` e `placaVeiculo` via `Scanner`
* Chama `FrotaBO.ativarViagem()`
* Exibe `"Viagem em curso"`

**#20 · `[VIEW]` Implementar GerenciaBilheteriaView**
`camada: view` `épico: bilhetagem` · **Depende de #12**
* Menu: `1) Emitir cartão` | `2) Recarregar cartão`
* Captura `idCartao` e `valorRecarga` via `Scanner`
* Exibe `"Recarga efetuada com sucesso!"`

**#21 · `[VIEW]` Implementar ValidadorView**
`camada: view` `épico: bilhetagem` · **Depende de #12, #14**
* Simula leitura do ID do cartão via `Scanner`
* Chama `BilhetagemBO.processarEmbarque()`
* Exibe `"Catraca Liberada"` ou trata `SaldoInsuficienteException` com mensagem amigável

**#22 · `[VIEW]` Implementar TerminalPainelView**
`camada: view` `épico: painel` · **Depende de #13**
* Loop com `Thread.sleep()` simulando atualização periódica
* Chama `PainelBO.buscarProximasPartidas()`
* Exibe tabela formatada: `nomeLinha`, `status`, `horarioEstimado`

**#23 · `[VIEW]` Implementar DispositivoGPSView**
`camada: view` `épico: painel` · **Depende de #13**
* Simula envio de status via `Scanner`
* Chama `PainelBO.atualizarStatusVeiculo()` e `atualizarHorarioEstimado()`
* Exibe confirmação de atualização

**#24 · `[DOC]` Auditoria final e atualização do README**
`documentacao`
* Verificar commits de todos os integrantes
* Atualizar README com diagramas finais
* Fechar issues pendentes das sprints anteriores

### 🟡 Funcionalidades gerais do app — issue-épico por funcionalidade 


### #29 · `[FUNCIONALIDADE]` Cadastro de Veículo
 
**Labels:** `funcionalidade` `épico: frota`
**Milestone:** Sprint 4 (fecha quando a view estiver pronta)
**Depende de:** #3, #8, #11, #17, #18
 
---
 
> representa a funcionalidade completa de **Cadastro de Veículo** (Épico 1 · Funcionalidade 1). Fica aberta até todas as sub-issues estarem concluídas.
 
**Diagrama de Sequência:** Épico 1 · Funcionalidade 1 (ver README)
 
#### Fluxo principal
 
* Administrador insere `placa`, `capacidade` e `tipo` via `CadastroVeiculoView`
* `FrotaBO.cadastrarVeiculo()` chama `VeiculoDAO.buscarPorPlaca()` — rejeita duplicatas
* Se placa nova → `VeiculoDAO.salvar(veiculoVO)` persiste na `tb_veiculo`
* View exibe `"Veículo cadastrado com sucesso!"` ou mensagem de duplicidade
#### Sub-issues que compõem esta funcionalidade
 
* `#3` · Criar VeiculoVO com atributos placa, capacidade e tipo
* `#8` · Implementar VeiculoDAO
* `#11` · Implementar FrotaBO
* `#17` · Testes JUnit — FrotaBO com Mockito
* `#18` · Implementar CadastroVeiculoView
#### Critério de aceite
 
O fluxo completo (View → BO → DAO → BD) executa sem erro, duplicatas são rejeitadas com mensagem clara e o veículo é persistido corretamente na `tb_veiculo`.
 
---
 
### #30 · `[FUNCIONALIDADE]` Ativação de Rota
 
**Labels:** `funcionalidade` `épico: frota`
**Milestone:** Sprint 4 (fecha quando a view estiver pronta)
**Depende de:** #4, #10, #11, #17, #19, #25, #26
 
---
 
> representa a funcionalidade completa de **Ativação de Rota** (Épico 1 · Funcionalidade 2). Fica aberta até todas as sub-issues estarem concluídas.
 
**Diagrama de Sequência:** Épico 1 · Funcionalidade 2 (ver README)
 
#### Fluxo principal
 
* Motorista informa `idRota` e `placaVeiculo` via `PainelBordoView`
* `FrotaBO.ativarViagem()` chama `RotaDAO.buscarRotaPorId()` para validar a rota
* `RotaDAO.atualizarStatusRota(idRota, "EM_CURSO")` persiste o novo status
* View exibe `"Viagem em curso"`
#### Sub-issues que compõem esta funcionalidade
 
* `#4` · Criar RotaVO com `List<String>` para paradas
* `#10` · Implementar RotaDAO
* `#11` · Implementar FrotaBO
* `#17` · Testes JUnit — FrotaBO com Mockito
* `#19` · Implementar PainelBordoView
* `#25` · Criar ParadaRotaVO para composição
* `#26` · Implementar inserção de Cartão e gerenciamento de Paradas
#### Critério de aceite
 
O Motorista consegue ativar uma rota existente, o status é atualizado para `EM_CURSO` no banco e a view exibe a confirmação corretamente.
 
---
 
### #31 · `[FUNCIONALIDADE]` Emissão e Recarga de Cartão
 
**Labels:** `funcionalidade` `épico: bilhetagem`
**Milestone:** Sprint 4 (fecha quando a view estiver pronta)
**Depende de:** #2, #9, #12, #20, #26, #28
 
---
 
> representa a funcionalidade completa de **Emissão e Recarga de Cartão** (Épico 2 · Funcionalidade 1). Fica aberta até todas as sub-issues estarem concluídas.
 
**Diagrama de Sequência:** Épico 2 · Funcionalidade 1 (ver README)
 
#### Fluxo principal — Emissão
 
* Administrador seleciona a opção `"Emitir cartão"` em `GerenciaBilheteriaView`
* `BilhetagemBO.emitirCartao(cartao)` encaminha para `CartaoDAO.salvar(cartao)`
* Cartão é persistido na `tb_cartao` com o tipo correto (`comum`, `estudante` ou `idoso`)
* View exibe confirmação de emissão
#### Fluxo principal — Recarga
 
* Administrador seleciona a opção `"Recarregar cartão"` e informa `idCartao` e `valorRecarga`
* `BilhetagemBO.recarregarCartao(idCartao, valor)` busca o cartão via `CartaoDAO.buscarPorId()`
* Chama `cartao.adicionarSaldo(valor)` e persiste via `CartaoDAO.atualizarSaldo(cartao)`
* View exibe `"Recarga efetuada com sucesso!"`
#### Sub-issues que compõem esta funcionalidade
 
* `#2` · Criar classe abstrata Cartao e subclasses polimórficas
* `#9` · Implementar CartaoDAO
* `#12` · Implementar BilhetagemBO
* `#20` · Implementar GerenciaBilheteriaView
* `#26` · Implementar inserção de Cartão e gerenciamento de Paradas
* `#28` · Atualizar BilhetagemBO com Emissão de Cartão
#### Critério de aceite
 
É possível emitir um cartão novo de qualquer tipo e persistir no banco. A recarga busca o cartão correto, atualiza o saldo e confirma a operação na view.
 
---
 
### #32 · `[FUNCIONALIDADE]` Validação de Embarque
 
**Labels:** `funcionalidade` `épico: bilhetagem`
**Milestone:** Sprint 4 (fecha quando a view estiver pronta)
**Depende de:** #2, #9, #12, #14, #15, #16, #21, #27
 
---
 
> representa a funcionalidade completa de **Validação de Embarque** (Épico 2 · Funcionalidade 2). Fica aberta até todas as sub-issues estarem concluídas.
 
**Diagrama de Sequência:** Épico 2 · Funcionalidade 2 (ver README)
 
#### Fluxo principal
 
* Passageiro aproxima o cartão no validador (leitura do ID via `ValidadorView`)
* `BilhetagemBO.processarEmbarque(idCartao, tarifaBase)` busca o cartão via `CartaoDAO.buscarPorId()`
* `cartao.calcularTarifa(tarifaBase)` calcula o valor polimorficamente conforme o tipo real
* **Saldo insuficiente** → lança `SaldoInsuficienteException` → view exibe alerta de erro
* **Saldo ok** → `cartao.debitar(valor)` → `CartaoDAO.atualizarSaldo(cartao)` → view exibe `"Catraca Liberada"`
#### Sub-issues que compõem esta funcionalidade
 
* `#2` · Criar classe abstrata Cartao e subclasses polimórficas
* `#9` · Implementar CartaoDAO
* `#12` · Implementar BilhetagemBO
* `#14` · Criar SaldoInsuficienteException
* `#15` · Testes JUnit — polimorfismo de tarifa
* `#16` · Testes JUnit — BilhetagemBO com Mockito
* `#21` · Implementar ValidadorView
* `#27` · Criar PersistenciaException
#### Critério de aceite
 
Cartão com saldo suficiente libera a catraca e debita o valor correto conforme o tipo. Cartão sem saldo lança `SaldoInsuficienteException` e exibe mensagem amigável. Os três tipos de cartão (`Comum`, `Estudante`, `Idoso`) calculam a tarifa corretamente.
 
---
 
### #33 · `[FUNCIONALIDADE]` Monitoramento de Status (GPS)
 
**Labels:** `funcionalidade` `épico: painel`
**Milestone:** Sprint 4 (fecha quando a view estiver pronta)
**Depende de:** #10, #13, #23
 
---
 
> representa a funcionalidade completa de **Monitoramento de Status via GPS** (Épico 3 · Funcionalidade 1). Fica aberta até todas as sub-issues estarem concluídas.
 
**Diagrama de Sequência:** Épico 3 · Funcionalidade 1 (ver README)
 
#### Fluxo principal
 
* Módulo de Bordo (GPS) envia coordenadas e status via `DispositivoGPSView`
* `PainelBO.atualizarStatusVeiculo(idViagem, status)` chama `RotaDAO.atualizarStatusRota()`
* `PainelBO.atualizarHorarioEstimado(idViagem, horario)` persiste o novo horário
* Banco atualiza `tb_monitoramento` e `tb_rota`
* View exibe confirmação de atualização
#### Sub-issues que compõem esta funcionalidade
 
* `#10` · Implementar RotaDAO
* `#13` · Implementar PainelBO
* `#23` · Implementar DispositivoGPSView
#### Critério de aceite
 
O Módulo de Bordo consegue enviar status (`EM_CURSO`, `ATRASADO`, etc.) e novo horário estimado, e os dados são persistidos corretamente no banco.
 
---
 
### #34 · `[FUNCIONALIDADE]` Painel Informativo do Terminal
 
**Labels:** `funcionalidade` `épico: painel`
**Milestone:** Sprint 4 (fecha quando a view estiver pronta)
**Depende de:** #10, #13, #22
 
---
 
> representa a funcionalidade completa do **Painel Informativo do Terminal** (Épico 3 · Funcionalidade 2). Fica aberta até todas as sub-issues estarem concluídas.
 
**Diagrama de Sequência:** Épico 3 · Funcionalidade 2 (ver README)
 
#### Fluxo principal
 
* `TerminalPainelView` executa um loop automático com `Thread.sleep()` simulando atualização periódica
* A cada ciclo chama `PainelBO.buscarProximasPartidas()`
* `RotaDAO.listarRotasAtivas()` executa `SELECT ... WHERE status = 'EM_CURSO'`
* Banco retorna os dados → DAO popula `List<RotaVO>` → BO retorna para a view
* View limpa a tela e exibe tabela formatada com `nomeLinha`, `status` e `horarioEstimado`
* Passageiro visualiza os horários atualizados no terminal
#### Sub-issues que compõem esta funcionalidade
 
* `#10` · Implementar RotaDAO
* `#13` · Implementar PainelBO
* `#22` · Implementar TerminalPainelView
#### Critério de aceite
 
O painel exibe em loop as rotas com status `EM_CURSO`, com `nomeLinha`, `status` e `horarioEstimado` formatados em tabela. A tela é limpa e atualizada a cada ciclo sem travamento.

