
## Justificativa Arquitetural

Para o desenvolvimento deste serviço, optei por utilizar a **Arquitetura Hexagonal (Ports and Adapters)**.

Embora seja um serviço de propósito único (consumir e salvar), a escolha dessa arquitetura garante que a **regra de negócio** (a triagem de severidade dos erros baseada na quantidade de itens) fique totalmente isolada das tecnologias de infraestrutura (AWS e Banco de Dados H2/Postgres).

Se no futuro o hospital decidir trocar a AWS SQS por RabbitMQ, ou o banco H2 por PostgreSQL, a regra de negócio central não precisará sofrer nenhuma alteração, pois o domínio não conhece as ferramentas externas.

### Organização dos Pacotes e Responsabilidades

O projeto foi organizado em três camadas principais para garantir baixo acoplamento e alta coesão:

#### 1. `core` (O Coração da Aplicação)
- **O que é:** Onde vivem os objetos de domínio (`OcorrenciaErroBO`, `ItemPedidoBO`) e a regra de negócio pura.
- **Por que:** É aqui que a lógica de "soma de itens para definir severidade HIGH/MEDIUM/LOW" foi implementada. Esta camada é feita apenas de Java puro, sem nenhuma dependência do Spring, da AWS ou de anotações de Banco de Dados. Isso facilita testes unitários isolados.

#### 2. `application` (Os Casos de Uso e Contratos)
- **O que é:** Contém os *Ports* (interfaces) e os *Services*.
- **Por que:** O `TriagemIncidentesService` atua como o orquestrador. Ele não sabe *como* a mensagem chegou e nem *como* ela será salva. Ele apenas implementa o Port de Entrada (`ProcessarTriagemUseCase`) e chama o Port de Saída (`SalvarAuditoriaPort`). Isso estabelece um contrato claro do que o sistema faz.

#### 3. `infrastructure` (Os Adaptadores Tecnológicos)
- **O que é:** Onde o sistema se comunica com o mundo exterior.
- **Por que:** Dividido entre `in` (entrada) e `out` (saída):
    - **`adapters.in.sqs`:** Contém o Listener do Spring Cloud AWS e os DTOs. O `OuvinteFilaDlqAdapter` recebe o JSON bruto da fila, converte para o objeto de Domínio (BO) e passa para a camada de Aplicação.
    - **`adapters.out.persistence`:** Contém as Entities do JPA, o Repository do Spring Data e a implementação do banco H2 (`SalvarAuditoriaH2Adapter`). Ele pega o objeto de Domínio já processado e o traduz para uma linha no banco de dados, gerando o UUID e gravando o *payload* bruto.

### Benefícios Alcançados
- **Testabilidade:** A regra de definição de severidade pode ser testada sem subir o contexto do Spring ou conectar na nuvem.
- **Segurança da Informação:** A captura do JSON na sua forma bruta (String) logo na entrada do adaptador SQS garante que nenhum detalhe do `payload` seja perdido durante a conversão para objetos Java, cumprindo o requisito de auditoria fidedigna.
