# Autobots - Parte IV 🤖
✅ Concluído

Aplicação web para gestão de manutenção veicular e vendas de autopeças.

## 🔧 Tecnologias utilizadas
- Java 17
- Maven 3.9+
- Spring Boot + Spring Security (JWT)

## 📋 Guia de Instalação

### Pré-requisitos
- Java 17 instalado
- Maven instalado
- MySQL 8 rodando em `localhost`

### 1) Clone este repositório
```bash
git clone https://github.com/EmmanuelJYokoyama/ATV4_SPRING.git
cd automanager
```

### 2) Configure `application.properties`
Arquivo: `src/main/resources/application.properties`
```
spring.datasource.url=jdbc:mysql://localhost:3306/ativ4
spring.datasource.username=<user>
spring.datasource.password=<password>
# Em desenvolvimento, recria as tabelas
spring.jpa.hibernate.ddl-auto=create-drop
jwt.secret=<secret-key>
jwt.expiration=3600000
```

### 3) Compile e execute
```bash
mvn clean install
mvn spring-boot:run
```
A aplicação sobe em `http://localhost:8080`.

## ✔️ Usuários seed
- admin: `nomeUsuario=admin`, `senha=admin123` (ADMIN)
- funcionário: `nomeUsuario=dompedrofuncionario`, `senha=123456` (VENDEDOR)
- fornecedor: `nomeUsuario=fornecedor`, `senha=123456` (GERENTE)
- cliente: `nomeUsuario=dompedrocliente`, `senha=123456` (CLIENTE)

## 🔐 Autenticação (JWT)
Existem duas formas:

1) Filtro padrão
- Rota: `POST /login`
- Body:
```json
{ "nomeUsuario": "admin", "senha": "admin123" }
```
- Token no header: `Authorization: Bearer <token>`

2) Endpoint dedicado
- Rota: `POST /auth/login`
- Body:
```json
{ "nomeUsuario": "admin", "senha": "admin123" }
```
- Resposta JSON: `{ "token": "<jwt>", "type": "Bearer", ... }`

## 🚪 Rotas protegidas (exemplos)
Inclua `Authorization: Bearer <token>` em cada requisição.

- Usuários: `GET /usuario/buscar`
- Serviços: `GET /servico/buscar`
- Mercadorias: `GET /mercadoria/buscar`
- Vendas: `GET /venda/buscar`
- Veículos: `GET /veiculo/buscar`

## 📖 Swagger
`http://localhost:8080/swagger-ui.html` → clique em "Authorize" → informe `Bearer <seu_token>` para chamar endpoints protegidos.

## 🧩 Dicas
- Se o banco não conectar, ajuste `spring.datasource.*`.
- Com `ddl-auto=create-drop`, o seed é recriado a cada execução em desenvolvimento.
