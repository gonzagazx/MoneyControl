# MoneyControl

Sistema de controle financeiro pessoal com dashboard, desenvolvido com Java, Spring Boot, MySQL, HTML, CSS e JavaScript.

> 🚧 Projeto em desenvolvimento.

## Sobre o projeto

O MoneyControl permite registrar receitas e despesas, organizá-las por categorias e acompanhar a saúde financeira em um dashboard com gráficos.

## Funcionalidades

- [x] Configuração inicial do projeto (Spring Boot + MySQL)
- [ ] Cadastro de categorias
- [ ] Cadastro de transações (receitas e despesas)
- [ ] Autenticação com JWT
- [ ] Dashboard com resumo (saldo, receitas e despesas)
- [ ] Gráficos por categoria
- [ ] Metas de economia
- [ ] Pagamentos recorrentes

## Tecnologias

**Back-end**

- Java 17
- Spring Boot
- Spring Data JPA / Hibernate
- Spring Security + JWT
- MySQL

**Front-end**

- HTML
- CSS
- JavaScript

**Ferramentas**

- Git e GitHub
- Maven

## Como rodar localmente

### Pré-requisitos

- Java 17
- MySQL
- Maven (ou use o `mvnw` que já vem no projeto)

### Passo a passo

1. Clone o repositório:

   ```bash
   git clone https://github.com/gonzagazx/moneycontrol.git
   cd moneycontrol
   ```

2. Crie o banco de dados no MySQL:

   ```sql
   CREATE DATABASE moneycontrol;
   ```

3. Crie o arquivo `src/main/resources/application-local.properties` com as suas credenciais do MySQL:

   ```properties
   spring.datasource.username=SEU_USUARIO
   spring.datasource.password=SUA_SENHA
   ```

   > Esse arquivo está no `.gitignore` e não é enviado para o GitHub.

4. Execute a aplicação:

   ```bash
   # Linux / macOS
   ./mvnw spring-boot:run

   #
