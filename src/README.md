<div align="center">

```
███╗   ███╗ ██████╗ ██████╗ ██╗██╗  ██╗
████╗ ████║██╔═══██╗██╔══██╗██║╚██╗██╔╝
██╔████╔██║██║   ██║██████╔╝██║ ╚███╔╝ 
██║╚██╔╝██║██║   ██║██╔══██╗██║ ██╔██╗ 
██║ ╚═╝ ██║╚██████╔╝██████╔╝██║██╔╝ ██╗
╚═╝     ╚═╝ ╚═════╝ ╚═════╝ ╚═╝╚═╝  ╚═╝
```

**Sistema de Gestão de Transporte Público Urbano**

*Frota · Bilhetagem Eletrônica · Monitoramento em Tempo Real*

---

![Status](https://img.shields.io/badge/status-em%20desenvolvimento-f59e0b?style=flat-square)
![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791?style=flat-square&logo=postgresql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=flat-square&logo=apachemaven&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-5-25A162?style=flat-square&logo=junit5&logoColor=white)
![License](https://img.shields.io/badge/license-MIT-3b82f6?style=flat-square)

</div>

---

## 📋 Visão Geral

O **Mobix** é um sistema de gestão de transporte público urbano que unifica três grandes domínios em uma única plataforma:

| Domínio | O que faz |
|---|---|
| 🚌 **Gestão Operacional de Frota e Rotas** | Cadastro, validação e ativação de veículos e rotas com controle de duplicidade via `buscarPorPlaca()` |
| 🎫 **Bilhetagem Eletrônica Inteligente** | Emissão, recarga e validação de cartões com tarifa polimórfica por perfil (`CartaoComum`, `CartaoEstudante`, `CartaoIdoso`) |
| 📡 **Módulo de Informação Dinâmica** | Rastreamento GPS em tempo real e painel estilo aeroporto com loop automático de próximas partidas nos terminais |

O sistema automatiza o ciclo completo da operação — do cadastro de veículos à liberação da catraca. Como diferencial, o **Módulo de Informação Dinâmica** integra painéis automáticos nos terminais de ônibus, exibindo horários e atualizações em tempo real de forma semelhante aos sistemas informativos utilizados em aeroportos. A arquitetura segue camadas rígidas (VIEW → BO → DAO) com persistência via JDBC e Collections (`List<String>`) para organização das paradas de rota.


---

## 👥 Equipe

- **Andrey Joshua**
- **Bruno Gabriel**
- **Henrique Cavalcanti**
- **Maryane Santos**
- **Raphael Phillipe**


Projeto acadêmico desenvolvido na disciplina de **Programação Orientada a Objetos**

---

## 🎯 Objetivos

**Geral:** Desenvolver o sistema Mobix para centralizar o gerenciamento da operação de transporte público urbano, utilizando a linguagem Java e os pilares da Programação Orientada a Objetos, integrando o controle de frota, rotas e bilhetagem a um painel informativo dinâmico.

**Específicos:**

- Cadastrar e validar veículos da frota (`placa`, `capacidade`, `tipo`) evitando duplicidades via `buscarPorPlaca()`.
- Organizar itinerários e paradas de rota utilizando **Collections** (`List<String>`) para estruturar os dados em memória.
- Ativar e monitorar viagens em tempo real, atualizando o status operacional das rotas para `EM_CURSO`.
- Emitir e recarregar cartões de transporte com persistência de saldo no banco de dados.
- Calcular tarifas **polimorficamente** com base no tipo real do cartão em tempo de execução.
- Lançar `SaldoInsuficienteException` quando o saldo do cartão for insuficiente para o embarque.
- Garantir persistência de todas as informações via **DAO** com comandos SQL e `ConnectionFactory` dedicada.
- Atualizar localização e status logístico dos veículos via Módulo de Bordo (GPS).
- Exibir painel informativo com loop automático de próximas partidas nos terminais de embarque.
- Aplicar **Testes Unitários (JUnit)** para validar as regras de cobrança e horários.


---

## 🎭 Atores

| Ator | Papel no sistema |
|---|---|
| 👨‍💼 **Administrador** | Cadastra veículos na frota; gerencia emissão e recarga de cartões de transporte. |
| 🚌 **Motorista** | Faz login no painel de bordo, informa a rota e inicia a viagem (`EM_CURSO`). |
| 📡 **Módulo de Bordo (GPS)** | Envia periodicamente coordenadas e status logístico do veículo ao sistema. |
| 🧍 **Passageiro** | Aproxima o cartão no validador para embarcar; visualiza horários no painel do terminal. |


---

## 🗂️ Épicos

### Épico 1 — Gestão Operacional de Frota e Rotas
Ciclo operacional dos veículos e rotas, do cadastro à ativação de viagem. Cobre validação de placas, verificação de duplicidade, ativação de viagens com status `EM_CURSO` e organização de itinerários com `List<String>` para as paradas da rota.

> **Funcionalidades:** Cadastro de Veículos · Ativação de Rota

### Épico 2 — Bilhetagem Eletrônica Inteligente
Gestão de cartões de transporte e processamento de embarques. Inclui emissão, recarga de saldo e validação polimórfica de tarifa por perfil do passageiro. Lança `SaldoInsuficienteException` em caso de saldo insuficiente e aciona a liberação da catraca após débito confirmado.

> **Funcionalidades:** Emissão e Recarga de Cartão · Validação de Embarque 

### Épico 3 — Módulo de Informação Dinâmica
Atualização em tempo real do status logístico dos veículos via GPS de bordo e exibição de painel estilo aeroporto nos terminais. Inclui loop automático de atualização com listagem de próximas partidas, reduzindo a incerteza do passageiro e melhorando a previsibilidade das viagens.

> **Funcionalidades:** Monitoramento de Status · Painel Informativo do Terminal


---

## 🛠️ Stack Tecnológica

| Camada | Tecnologia | Justificativa |
|---|---|---|
| **Linguagem** | Java 17 | Tipagem forte; suporte robusto a herança e polimorfismo dinâmico. |
| **Framework** | Spring Boot 3 | Configuração mínima, injeção de dependência e suporte nativo a REST. |
| **Persistência** | PostgreSQL 15 + JPA + Hibernate | ORM maduro com suporte a herança de entidades. |
| **Build** | Maven 3.9 | Gerenciamento de dependências e ciclo de build padronizado. |
| **Testes** | JUnit 5 + Mockito | TDD com mocks para BO/DAO sem banco real nos testes unitários. |
| **Validação** | Jakarta Bean Validation | Anotações `@NotNull`, `@Pattern` etc. nas entidades e VOs. |

---

## 🏗️ Arquitetura em Camadas

O sistema adota um padrão arquitetural rígido em quatro camadas com responsabilidades bem definidas. Modificações na interface ou no banco não afetam as regras de negócio.

```
┌─────────────────────┐     VOs transitam      ┌─────────────────────┐     VOs transitam     ┌─────────────────────┐
│        VIEW         │ ──────────────────────►│         BO          │ ─────────────────────►│        DAO          │
│   (Apresentação)    │     Ex: VeiculoVO      │  (Regras de Negócio)│     Ex: CartaoVO      │  (Acesso a Dados)   │
│                     │ ◄──────────────────────│                     │ ◄─────────────────────│                     │
└─────────────────────┘      RotaVO            └─────────────────────┘      RotaVO           └──────────┬──────────┘
                                                                                                        │   ▲
                                                                                               Query SQL│   │ ResultSet
                                                                                                        ▼   │
                                                                                             ┌─────────────────────┐
                                                                                             │    BANCO DE DADOS   │
                                                                                             │     (Relacional)    │
                                                                                             │  TB_VEICULO         │
                                                                                             │  TB_CARTAO          │
                                                                                             │  TB_ROTA            │
                                                                                             │  TB_MONITORAMENTO   │
                                                                                             └─────────────────────┘
```

| Camada | Responsabilidade |
|---|---|
| **VIEW** | Interface com o ator. Captura entradas e exibe resultados — sem cálculos ou acesso ao banco. |
| **BO** | Cérebro do sistema. Centraliza validações, cálculo polimórfico de tarifa, verificação de saldo e orquestração da persistência. |
| **DAO** | Comunicação exclusiva com o banco. Traduz operações em SQL (CRUD) e instancia VOs polimorficamente com base nos dados retornados. |
| **VO** | Transportadores de dados entre camadas. `CartaoVO` possui subclasses polimórficas com `calcularTarifa()` sobrescrito. |

---

## Diagrama de Classes

```mermaid
classDiagram

%% ─── Usuário / Autenticação ───────────────────────────────────────────────

    class Usuario {
        -login: String
        -senha: String
        +Usuario(String login, String senha)
    }

    class Autenticavel {
        <<Interface>>
        +autenticar(String login, String senha) boolean
    }

    class Administrador {
        +autenticar(String login, String senha) boolean
    }

    class Motorista {
        -cnh: String
        +autenticar(String login, String senha) boolean
    }

    Usuario <|-- Administrador : Herança
    Usuario <|-- Motorista     : Herança
    Autenticavel <|.. Administrador : Implementa
    Autenticavel <|.. Motorista     : Implementa

%% ─── Bilhetagem ───────────────────────────────────────────────────────────

    class GerenciaBilheteiraView {
        -bilhetagemBO: BilhetagemBO
        +renderizarMenu() void
    }

    class ValidadorView {
        -bilhetagemBO: BilhetagemBO
        +lerCartaoFisico() void
    }

    class SaldoInsuficienteException {
        <<exception>>
        +SaldoInsuficienteException(String mensagem) void
    }

    class BilhetagemBO {
        -cartaoDAO: CartaoDAO
        +recarregarCartao(int id, double valor) void
        +processarEmbarque(int id, double tarifaBase) boolean
    }

    class CartaoDAO {
        +buscarPorId(int id) Cartao
        +atualizarSaldo(Cartao cartao) boolean
    }

    class Cartao {
        <<abstract>>
        -id: int
        -saldo: double
        -titular: String
        +Cartao(int id, double saldo, String titular)
        +adicionarSaldo(double valor) void
        +debitar(double valor) void
        +calcularTarifa(double tarifaBase) double
        +getId() int
        +getSaldo() double
    }

    class CartaoComum {
        +CartaoComum(int id, double saldo, String titular)
        +calcularTarifa(double tarifaBase) double
    }

    class CartaoEstudante {
        +CartaoEstudante(int id, double saldo, String titular)
        +calcularTarifa(double tarifaBase) double
    }

    class CartaoIdoso {
        +CartaoIdoso(int id, double saldo, String titular)
        +calcularTarifa(double tarifaBase) double
    }

    GerenciaBilheteiraView ..> BilhetagemBO : Depende
    ValidadorView          ..> BilhetagemBO : Depende
    BilhetagemBO           ..> CartaoDAO    : Depende
    BilhetagemBO ..> SaldoInsuficienteException : Lança
    CartaoDAO               o-- Cartao      : Manipula (Polimorfismo)
    Cartao <|-- CartaoComum    : Herança
    Cartao <|-- CartaoEstudante: Herança
    Cartao <|-- CartaoIdoso    : Herança

%% ─── Frota / Veículo ─────────────────────────────────────────────────────

    class CadastroVeiculoView {
        -frotaBO: FrotaBO
        +exibirFormulario() void
    }

    class PainelBordoView  {
        -frotaBO: FrotaBO
        + informarRota(int idRota, String placaVeiculo) : void
        + exibirStatusViagem(String status) : void
    }

    class FrotaBO {
        -veiculoDAO: VeiculoDAO
        -rotaDAO: RotaDAO
        +cadastrarVeiculo(VeiculoVO veiculo) void
        +ativarViagem(int idRota, String placa) void
    }

    class VeiculoDAO {
        +salvar(VeiculoVO veiculo) boolean
        +buscarPorPlaca(String placa) VeiculoVO
    }

    class VeiculoVO {
        -placa: String
        -capacidade: int
        -tipo: String
        +VeiculoVO(String placa, int capacity, String tipo)
        +getPlaca() String
        +getCapacidade() int
    }

    CadastroVeiculoView ..> FrotaBO   : Depende
    FrotaBO             ..> VeiculoDAO: Depende
    FrotaBO             ..> RotaDAO   : Depende
    PainelBordoView ..> FrotaBO : Depende
    VeiculoDAO           o-- VeiculoVO: Manipula
    VeiculoVO <|-- CartaoIdoso         : Herança

%% ─── Rota / Painel ───────────────────────────────────────────────────────


    class DispositivoGPSView {
        -painelBO: PainelBO
        +enviarStatus(int id, String status)
    }

    class TerminalPainelView {
        -painelBO: PainelBO
        +atualizarPainelAutomatico() void
    }

    class PainelBO {
        -rotaDAO: RotaDAO
        +atualizarStatusVeiculo(int idViagem, String status) void
        +buscarProximasPartidas() List~RotaVO~
    }

    class RotaDAO {
        +buscarRotaPorId(int id) RotaVO
        +atualizarStatusRota(int id, String status) boolean
        +atualizarHorarioEstimado(int idViagem, String horario) : boolean
        +listarRotasAtivas() : List<RotaVO>
    }

    class RotaVO {
        -id: int
        -nomeLinha: String
        -status: String
        -paradas: List~String~
        +RotaVO(int id, String nomeLinha)
        +adicionarParada(String parada) void
        +getParadas() List~String~
        +getStatus() String
        +setStatus(String status) void
        +setHorarioEstimado(String horario) : void
        +getHorarioEstimado() : String
    }

    class ListString["List~String~"]

    TerminalPainelView ..> PainelBO : Depende
    DispositivoGPSView ..> PainelBO : Depende
    PainelBO           ..> RotaDAO  : Depende
    RotaDAO             o-- RotaVO  : Manipula (Collections)
    RotaVO             --> ListString : Usa Collection (List)
```

---

## 🔄 Diagramas de Sequência

### Épico 1 · Funcionalidade 1 — Cadastro de Veículo

```mermaid
sequenceDiagram
    actor ADM as Administrador
    participant CVV as CadastroVeiculoView
    participant FBO as FrotaBO
    participant VDA as VeiculoDAO
    participant BD as Banco de Dados

    ADM ->> CVV: Inserir dados do veículo (placa, capacidade)
    CVV ->> FBO: cadastrarVeiculo(veiculoVO)
    FBO ->> VDA: buscarPorPlaca(placa)
    VDA ->> BD: SELECT * FROM veiculo WHERE placa = ?
    BD -->> VDA: Resultado (Não existe)
    VDA -->> FBO: null
    FBO ->> VDA: salvar(veiculoVO)
    VDA ->> BD: INSERT INTO veiculo ...
    BD -->> VDA: Confirmar inserção
    VDA -->> FBO: Sucesso
    FBO -->> CVV: "Veículo cadastrado com sucesso!"
    CVV -->> ADM: Exibir mensagem na tela
```


### Épico 1 · Funcionalidade 2 — Ativação de Rota

```mermaid
sequenceDiagram
    actor MOT as Motorista
    participant TPV as PainelBordoView
    participant FBO as FrotaBO
    participant RDA as RotaDAO
    participant BD as Banco de Dados

    MOT ->> TPV: Informar ID da Rota e Placa
    TPV ->> FBO: ativarViagem(idRota, placaVeiculo)
    FBO ->> RDA: buscarRotaPorId(idRota)
    RDA ->> BD: SELECT * FROM rota WHERE id = ?
    BD -->> RDA: Dados da rota
    RDA -->> FBO: RotaVO
    FBO ->> RDA: atualizarStatusRota(idRota, "EM_CURSO")
    RDA ->> BD: UPDATE rota SET status = 'EM_CURSO' WHERE id = ?
    BD -->> RDA: Confirmar alteração
    RDA -->> FBO: Sucesso
    FBO -->> TPV: Rota Ativada
    TPV -->> MOT: Exibir "Viagem em curso"
```


### Épico 2 · Funcionalidade 1 — Emissão e Recarga de Cartão

```mermaid
sequenceDiagram
    actor ADM as Administrador
    participant GBV as GerenciaBilheteriaView
    participant BBO as BilhetagemBO
    participant CDA as CartaoDAO
    participant CAR as Cartao
    participant BD as Banco de Dados

    ADM ->> GBV: Informar ID do Cartão e Valor da Recarga
    GBV ->> BBO: recarregarCartao(idCartao, valorRecarga)
    BBO ->> CDA: buscarPorId(idCartao)
    CDA ->> BD: SELECT * FROM cartao WHERE id = ?
    BD -->> CDA: Dados do Cartão
    CDA -->> BBO: Instância de Cartao
    BBO ->> CAR: adicionarSaldo(valorRecarga)
    BBO ->> CDA: atualizarSaldo(cartao)
    CDA ->> BD: UPDATE cartao SET saldo = ? WHERE id = ?
    BD -->> CDA: Confirmar alteração
    CDA -->> BBO: Sucesso
    BBO -->> GBV: "Recarga efetuada com sucesso!"
    GBV -->> ADM: Exibir mensagem na tela
```


### Épico 2 · Funcionalidade 2 — Validação de Embarque


```mermaid
sequenceDiagram
    actor MOD as Módulo de Bordo
    participant VV as ValidadorView
    participant BBO as BilhetagemBO
    participant CDA as CartaoDAO
    participant CAR as Cartao (Polimórfico)
    participant BD as Banco de Dados

    MOD ->> VV: Aproximar Cartão (Leitura do ID)
    VV ->> BBO: processarEmbarque(idCartao, tarifaBase)
    BBO ->> CDA: buscarPorId(idCartao)
    CDA ->> BD: SELECT * FROM cartao WHERE id = ?
    BD -->> CDA: Linha da tabela
    CDA -->> BBO: Retorna instância correspondente (Cartao)
    BBO ->> CAR: calcularTarifa(tarifaBase)
    CAR -->> BBO: Valor com desconto aplicado

    alt [Saldo Insuficiente]
        BBO -->> VV: lança SaldoInsuficienteException
        VV -->> MOD: Alerta Sonoro / Erro na tela
    else [Saldo Ok]
        BBO ->> CAR: debitar(valorCalculado)
        BBO ->> CDA: atualizarSaldo(cartao)
        CDA ->> BD: UPDATE cartao SET saldo = ? WHERE id = ?
        BD -->> CDA: Confirmar alteração
        CDA -->> BBO: Sucesso
        BBO -->> VV: "Catraca Liberada"
        VV -->> MOD: Sinal Verde / Liberar Catraca
    end
```

### Épico 3 · Funcionalidade 1 — Monitoramento de Status

```mermaid
sequenceDiagram
    actor GPS as Módulo de Bordo (GPS)
    participant TPV as DispositivoGPSView
    participant PBO as PainelBO
    participant RDA as RotaDAO
    participant BD as Banco de Dados
 
    GPS ->> TPV: Enviar Coordenadas e Status ("Atrasado")
    TPV ->> PBO: atualizarStatusVeiculo(idViagem, "ATRASADO")
    PBO ->> RDA: atualizarStatusRota(idViagem, "ATRASADO")
    
    RDA ->> BD: UPDATE monitoramento SET status = 'ATRASADO' WHERE viagem_id = ?
    BD -->> RDA: Confirmar alteração

    PBO ->> RDA: atualizarHorarioEstimado(idViagem, novoHorario)
    
    RDA -->> PBO: Sucesso
    PBO -->> TPV: Sucesso
    TPV -->> GPS: Sucesso
```

### Épico 3 · Funcionalidade 2 — Painel Informativo

```mermaid
sequenceDiagram
    actor PAS as Passageiro
    participant TPV as TerminalPainelView
    participant PBO as PainelBO
    participant RDA as RotaDAO
    participant BD as Banco de Dados
 
    note over TPV: Rotina de atualização (Loop automático)
 
    TPV ->> PBO: buscarProximasPartidas()
    PBO ->> RDA: listarRotasAtivas()
    RDA ->> BD: SELECT * FROM rotas INNER JOIN... WHERE status = 'EM_CURSO'
    BD -->> RDA: Linhas de dados das viagens
    RDA -->> PBO: Retorna Collection de RotaVO
    note over PBO: Preenche a Collection (ArrayList)
    PBO -->> TPV: Retorna Collection de RotaVO
    TPV -->> TPV: Limpar e Atualizar Tela
    TPV -->> PAS: Visualiza horários atualizados na TV do Terminal
```



---


