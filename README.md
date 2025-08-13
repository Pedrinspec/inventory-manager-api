# Projeto de Otimização de Inventário Distribuído 📦

## 1. Objetivo 🎯
Este projeto visa projetar e prototipar uma solução de backend para um sistema de gerenciamento de inventário distribuído, substituindo um sistema legado baseado em sincronização em lote.  
Os objetivos principais são:

- Resolver problemas de consistência e latência na atualização de estoque.
- Aumentar a resiliência e a tolerância a falhas do sistema.
- Projetar uma arquitetura escalável e manutenível seguindo as melhores práticas de mercado.

---

## 2. Decisões de Arquitetura Fundamentais 🏛️

### a. Arquitetura Orientada a Eventos com Kafka ⛓️
**Justificativa:** O sistema legado falhava devido à sua sincronização em lote a cada 15 minutos, criando uma janela de inconsistência inaceitável. Adotamos uma Arquitetura Orientada a Eventos para eliminar essa falha.

**Fluxo:**  
Em vez de o sistema central *puxar* dados, as lojas (produtores) *empurram* eventos (`EstoqueVendido`, `EstoqueAjustado`) em tempo real para um tópico no **Apache Kafka**.  
Nosso serviço (consumidor) reage a esses eventos de forma assíncrona.

**Benefícios:**
- **Desacoplamento:** A loja não precisa saber que o serviço central existe, aumentando a resiliência.  
  Se o nosso serviço estiver offline, as lojas continuam a operar e os eventos são enfileirados no Kafka.
- **Consistência Eventual:** Reduzimos a janela de inconsistência de minutos para segundos (ou milissegundos), garantindo que a visão online do estoque seja um reflexo quase em tempo real da realidade.
- **Escalabilidade:** O Kafka atua como um buffer, absorvendo picos de vendas sem sobrecarregar diretamente o nosso serviço.

---

### b. Clean Architecture & Domain-Driven Design (DDD) 🧼
**Justificativa:** Para garantir a manutenibilidade e testabilidade a longo prazo, o código foi estruturado seguindo os princípios da **Clean Architecture** e do **DDD**.

**Organização do projeto:**
- **`domain`** → O coração do sistema. Contém as entidades de negócio puras (`InventoryItem`).
- **`application`** → Camada de orquestração. Contém os Casos de Uso (`UpdateStockUseCase`) e as Portas (interfaces *in* e *out*) que definem os contratos da aplicação.
- **`infra`** → Detalhes de implementação. Contém os Adaptadores que conectam ao mundo exterior (Controllers para Web, Consumers para Kafka, Repositórios para JPA).

Essa separação garante que a lógica de negócio seja **independente de tecnologia**.

---

### c. Tolerância a Falhas e Resiliência 🛡️
**Justificativa:** Um sistema distribuído deve ser projetado para falhar.

- **Idempotência:** Cada evento possui um `eventId` único. Uma entidade (`ProcessedEventEntity`) rastreia eventos já processados, evitando duplicidade e corrupção de dados.
- **Retentativas e DLQ:** Com `@RetryableTopic` do Spring Kafka, o sistema reprocessa mensagens em caso de falhas transitórias (ex.: banco offline por 1s).  
  Mensagens “envenenadas” vão para a **DLQ** para análise, sem interromper eventos válidos.

---

## 3. Stack de Tecnologia 🚀
- **Linguagem e Framework:** Java 21 + Spring Boot 3
- **Build Tool:** Gradle com Kotlin DSL
- **Persistência:** Spring Data JPA com H2
- **Mensageria:** Spring Kafka
- **Cache:** Spring Cache
- **Geração de Código:** MapStruct e Lombok
- **Testes:** JUnit 5, Mockito, MockMvc, Testcontainers
- **Qualidade:** JaCoCo
- **Ambiente:** Docker e Docker Compose

---

## 4. Design da API (Principais Endpoints) 📡
A API REST é a porta de entrada síncrona para consultas de inventário.

### `POST /api/v1/inventory/products`
- **Propósito:** Criar novo produto e definir estoque inicial.
- **Request Body:** `CreateProductRequest` DTO.

### `GET /api/v1/inventory/products/{productId}`
- **Propósito:** Consultar inventário consolidado para um SKU.
- **Resposta:** `ProductInventoryResponse` DTO.

### `POST /api/v1/inventory/simulate/event`
- **Propósito:** Simular evento de atualização de estoque para o Kafka.
- **Request Body:** `StockEventDTO`.

---

## 5. Instruções de Configuração ⚙️
**Pré-requisitos:** Java 21, Docker, Docker Compose.

- **Iniciar Infraestrutura:**
  ```bash
  docker-compose up -d
  ```

- **Executar Aplicação:**
  ```bash
  ./gradlew bootRun
  ```

- **Executar Testes:**
  ```bash
  ./gradlew test
  ```

- **Relatório HTML em:**
  ```bash
  build/reports/jacoco/test/html/index.html
  ```

---

## 6. Integração com IA Generativa e Ferramentas Modernas 🤖⚡
O uso de IA generativa e ferramentas de desenvolvimento modernas agiliza tarefas como aceleração de testes unitários,
auxiliam em questões e dúvidas arquiteturais, além de ajuda para fazer documentações de projeto.
Essas tecnologias não substituem o conhecimento técnico, mas potencializam a produtividade e a qualidade do código.