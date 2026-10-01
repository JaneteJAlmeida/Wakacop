# Sistema de Votação Cooperativa (Wakacop)

## Sobre o Projeto
Este sistema é um back-end desenvolvido em Java Spring Boot para gerenciar sessões de votação em cooperativas. Ele permite o cadastramento de pautas, abertura de sessões de votação, recebimento e contabilização de votos dos associados, e fornece os resultados das votações. O sistema opera na nuvem e é acessível via API REST.

---

## Architecture Haiku

### Objetivos do Negócio
* Facilitar a gestão democrática em cooperativas.
* Assegurar a integridade e confiabilidade dos votos.
* Prover resultados rápidos e precisos das votações.

### Restrições
* Operação na nuvem.
* Persistência de dados.
* Foco no back-end.

### Atributos de Qualidade
Segurança > Disponibilidade > Escalabilidade

### Decisões de Design
* Java Spring Boot, Maven, Docker Compose.
* API RESTful.
* Integração com sistema externo para validação de CPF.
* Mensageria para divulgação de resultados.
* Estratégia de versionamento da API.

---

## Pré-Requisitos
Antes de começar, você vai precisar ter instalado em sua máquina as seguintes ferramentas:
* **Java 17**
* **Maven**
* **Docker e Docker Compose**
* **Git**

---
## Tecnologias e Infraestrutura
Além do ecossistema Spring Boot, o projeto utiliza os seguintes componentes estruturais:
* **Banco de Dados Relacional (H2 / PostgreSQL):** Utilizado via JPA/Hibernate para garantir a persistência e a consistência das pautas, sessões e votos.
* **Mensageria (Apache Kafka / RabbitMQ):** Sistema de mensageria assíncrona para publicação automática do resultado da sessão de votação assim que ela é encerrada.
* **Feign Client:** Integrado para realizar chamadas REST externas ao serviço de validação de elegibilidade do associado (validação de CPF).

---
## Endpoints da API

A URL base da aplicação local é: `http://localhost:8080/wakacop`

### 📌 Pautas
* `POST /pauta` - Cadastra uma nova pauta para votação.
  * **URL Completa:** `http://localhost:8080/wakacop/pauta`

### 📌 Sessão de Votação
* `POST /sessao/abertura` - Abre uma nova sessão de votação para uma pauta.
  * **URL Completa:** `http://localhost:8080/wakacop/sessao/abertura`
* `POST /sessao/{idSessao}/voto` - Registra o voto de um associado (SIM/NÃO).
  * **URL Completa:** `http://localhost:8080/wakacop/sessao/{idSessao}/voto`
* `GET /sessao/{idSessao}/resultado` - Obtém o resultado e a contabilização dos votos.
  * **URL Completa:** `http://localhost:8080/wakacop/sessao/{idSessao}/resultado`


## Como Executar o Projeto

Siga os passos abaixo para clonar e rodar a aplicação localmente:

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/JaneteJAlmeida/Wakacop.git
   ```

2. **Navegue até a pasta do projeto:**
   ```bash
   cd Wakacop
   ```

3. **Construa o projeto com Maven:**
   ```bash
   mvn clean install
   ```

4. **Inicie os serviços usando Docker Compose:**
   ```bash
   docker-compose up
   ```

5. **O sistema estará rodando e acessível.**

---

---

## Testando a Aplicação

Para facilitar os testes das APIs do projeto, foi disponibilizada uma collection do Postman. Você pode importá-la e executá-la diretamente clicando no botão abaixo:

[![Run in Postman](https://pstmn.io)](https://postman.com)
---

## Exemplos de Payload para Testes (JSON)

Ao testar a aplicação utilizando o Postman ou outra ferramenta, utilize as estruturas de corpo (Body) abaixo:

### 1. Criar Nova Pauta (`POST /pauta`)
**Body (JSON):**
```json
{
  "titulo": "Implementação de Nova Tecnologia",
  "descricao": "Votação para decidir a adoção do ecossistema Spring Boot na cooperativa."
}
```

### 2. Abrir Sessão de Votação (`POST /sessao/abertura`)
*O campo `tempoDuracao` é opcional (padrão é 1 minuto caso não seja enviado).*
**Body (JSON):**
```json
{
  "idPauta": "COLE_O_ID_DA_PAUTA_AQUI",
  "tempoDuracao": 5
}
```

### 3. Enviar Voto (`POST /sessao/{idSessao}/voto`)
*A opção aceita apenas os valores textuais representativos do voto.*
**Body (JSON):**
```json
{
  "cpfAssociado": "11122233344",
  "opcao": "SIM",
  "dataNascimento": "1995-01-01"
}
```


