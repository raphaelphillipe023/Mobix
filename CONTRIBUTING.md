C# Guia de Contribuição — Mobix

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
- Criar pacotes: `view`, `bo`, `dao`, `model.usuario`, `model.cartao`, `interfaces`, `infra`, `exception`
- Configurar `pom.xml`: JUnit 5, Mockito, PostgreSQL JDBC Driver
- Adicionar `.gitignore` para `/target` e `/.idea`

---

**#2 · `[MODEL]` Criar classe abstrata Cartao e subclasses polimórficas**
`camada: model` `épico: bilhetagem`
- `Cartao` (abstract): `id`, `saldo`, `titular` | `adicionarSaldo()`, `debitar()`, `calcularTarifa()` abstract
- `CartaoComum`: sem desconto
- `CartaoEstudante`: 50% de desconto
- `CartaoIdoso`: isenção/gratuidade

---

**#3 · `[MODEL]` Criar VeiculoVO com atributos placa, capacidade e tipo**
`camada: model` `épico: frota`
- Atributos: `placa`, `capacidade`, `tipo`
- Construtor completo + getters
- Sem lógica de negócio

---

**#4 · `[MODEL]` Criar RotaVO com List\<String\> para paradas**
`camada: model` `épico: frota`
- Atributos: `id`, `nomeLinha`, `status`, `paradas (List<String>)`, `horarioEstimado`
- Construtor: `RotaVO(int id, String nomeLinha)`
- Métodos: `adicionarParada()`, `getParadas()`, `getStatus()`, `setStatus()`, `getHorarioEstimado()`, `setHorarioEstimado()`

---

**#5 · `[MODEL]` Criar hierarquia de usuários e interface Autenticavel**
`camada: model` `infra`
- Interface `Autenticavel`: `autenticar(String login, String senha) : boolean`
- `Usuario`: `login`, `senha`
- `Administrador extends Usuario implements Autenticavel`
- `Motorista extends Usuario implements Autenticavel` | atributo `cnh`

---

### 🟨 Sprint 2 — Persistência JDBC

**#6 · `[INFRA]` Criar script SQL das tabelas**
`infra` `camada: dao`
```sql
CREATE TABLE tb_veiculo (placa VARCHAR PK, capacidade INT, tipo VARCHAR);
CREATE TABLE tb_cartao (id INT PK, saldo DOUBLE, titular VARCHAR, tipo VARCHAR);
CREATE TABLE tb_rota (id INT PK, nome_linha VARCHAR, status VARCHAR, horario_estimado VARCHAR);
CREATE TABLE tb_monitoramento (id INT PK, viagem_id INT FK, status VARCHAR, horario VARCHAR);
```

---

**#7 · `[INFRA]` Implementar ConnectionFactory**
`infra`
- Método estático `getConnection() : Connection`
- Tratamento de `SQLException`
- Critério de aceite: conexão abre e fecha sem erro com o banco local

---

**#8 · `[DAO]` Implementar VeiculoDAO**
`camada: dao` `épico: frota`
- `salvar(VeiculoVO) : boolean` → `INSERT INTO tb_veiculo`
- `buscarPorPlaca(String) : VeiculoVO` → `SELECT * FROM tb_veiculo WHERE placa = ?`
- Usar `PreparedStatement` e `ConnectionFactory`

---

**#9 · `[DAO]` Implementar CartaoDAO**
`camada: dao` `épico: bilhetagem`
- `buscarPorId(int) : Cartao` → instancia subclasse correta pelo campo `tipo` (fábrica)
- `atualizarSaldo(Cartao) : boolean` → `UPDATE tb_cartao SET saldo = ? WHERE id = ?`

---

**#10 · `[DAO]` Implementar RotaDAO**
`camada: dao` `épico: frota` `épico: painel`
- `buscarRotaPorId(int) : RotaVO`
- `atualizarStatusRota(int, String) : boolean`
- `atualizarHorarioEstimado(int, String) : boolean`
- `listarRotasAtivas() : List<RotaVO>` → `SELECT * FROM rotas INNER JOIN viagens ... WHERE status = 'EM_CURSO'`

---

### 🟥 Sprint 3 — Regras de Negócio e Testes

**#11 · `[BO]` Implementar FrotaBO**
`camada: bo` `épico: frota` · Depende de #8 #10
- `cadastrarVeiculo(VeiculoVO)`: chama `buscarPorPlaca()` antes — rejeita duplicatas
- `ativarViagem(int idRota, String placa)`: busca rota, atualiza status para `EM_CURSO`

---

**#12 · `[BO]` Implementar BilhetagemBO**
`camada: bo` `épico: bilhetagem` · Depende de #9 #14
- `recarregarCartao(int, double)`: busca cartão, `adicionarSaldo()`, persiste
- `processarEmbarque(int, double)`: `calcularTarifa()`, `debitar()` → lança `SaldoInsuficienteException` se saldo insuficiente

---

**#13 · `[BO]` Implementar PainelBO**
`camada: bo` `épico: painel` · Depende de #10
- `atualizarStatusVeiculo(int, String)`
- `atualizarHorarioEstimado(int, String)`
- `buscarProximasPartidas() : List<RotaVO>`

---

**#14 · `[EXCEPTION]` Criar SaldoInsuficienteException**
`camada: bo` `épico: bilhetagem`
- `<<exception>>` extends `Exception`
- Construtor: `SaldoInsuficienteException(String mensagem)`

---

**#15 · `[TESTE]` Testes JUnit — polimorfismo de tarifa**
`testes` `épico: bilhetagem`
- `CartaoComumTest`, `CartaoEstudanteTest`, `CartaoIdosoTest`
- Verificar `calcularTarifa()`, `debitar()` e `adicionarSaldo()` para cada tipo

---

**#16 · `[TESTE]` Testes JUnit — BilhetagemBO com Mockito**
`testes` `épico: bilhetagem` · Depende de #12 #14
- Mock de `CartaoDAO`
- `processarEmbarque()` → saldo ok → débito realizado
- `processarEmbarque()` → saldo insuficiente → exceção lançada
- `recarregarCartao()` → saldo atualizado

---

**#17 · `[TESTE]` Testes JUnit — FrotaBO com Mockito**
`testes` `épico: frota` · Depende de #11
- Mock de `VeiculoDAO` e `RotaDAO`
- `cadastrarVeiculo()` → placa nova → salvo
- `cadastrarVeiculo()` → placa duplicada → não salva
- `ativarViagem()` → status `EM_CURSO`

---

### 🟩 Sprint 4 — Views e Fluxo Final

**#18 · `[VIEW]` Implementar CadastroVeiculoView**
`camada: view` `épico: frota` · Depende de #11
- Captura `placa`, `capacidade`, `tipo` via `Scanner`
- Chama `FrotaBO.cadastrarVeiculo()`
- Exibe confirmação ou mensagem de duplicidade

---

**#19 · `[VIEW]` Implementar PainelBordoView**
`camada: view` `épico: frota` · Depende de #11
- Captura `idRota` e `placaVeiculo` via `Scanner`
- Chama `FrotaBO.ativarViagem()`
- Exibe `"Viagem em curso"`

---

**#20 · `[VIEW]` Implementar GerenciaBilheteriaView**
`camada: view` `épico: bilhetagem` · Depende de #12
- Menu: `1) Emitir cartão  2) Recarregar cartão`
- Captura `idCartao` e `valorRecarga` via `Scanner`
- Exibe `"Recarga efetuada com sucesso!"`

---

**#21 · `[VIEW]` Implementar ValidadorView**
`camada: view` `épico: bilhetagem` · Depende de #12 #14
- Simula leitura do ID do cartão via `Scanner`
- Chama `BilhetagemBO.processarEmbarque()`
- Exibe `"Catraca Liberada"` ou trata `SaldoInsuficienteException` com mensagem amigável

---

**#22 · `[VIEW]` Implementar TerminalPainelView**
`camada: view` `épico: painel` · Depende de #13
- Loop com `Thread.sleep()` simulando atualização periódica
- Chama `PainelBO.buscarProximasPartidas()`
- Exibe tabela formatada: `nomeLinha`, `status`, `horarioEstimado`

---

**#23 · `[VIEW]` Implementar DispositivoGPSView**
`camada: view` `épico: painel` · Depende de #13
- Simula envio de status via `Scanner`
- Chama `PainelBO.atualizarStatusVeiculo()` e `atualizarHorarioEstimado()`
- Exibe confirmação de atualização

---

**#24 · `[DOC]` Auditoria final e atualização do README**
`documentacao`
- Verificar commits de todos os integrantes
- Atualizar README com diagramas finais
- Fechar issues pendentes das sprints anteriores
