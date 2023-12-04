# Koupit Backend

## Integrantes

- [Kaik Wulck Bassanelli](https://github.com/kaikwb) RM: 96731
- [Leonardo de Barros Silva](https://github.com/negreiroleo) RM: 97582
- [Lucas Satoru Shiaku](https://github.com/LucasShiaku) RM: 97019
- [Pablo Lage Carral](https://github.com/Pablo-Lage-Carral) RM 97282
- [Rafael Vieira Pinto](https://github.com/Rafa2318) RM: 97117

## Nossa proposta

Nossa solução consiste em um sistema de gerenciamento de requisições de compra de produtos, onde o usuário pode criar 
uma requisição de compra, adicionar produtos a ela e enviar para aprovação e futuramente para cotação. O sistema
também permite que o usuário visualize o histórico de requisições de compra e a posição de cada uma delas.
Durante a criação da requisição, o usuário pode adicionar produtos de forma parametrizada, ou seja, ele pode adicionar
um produto que já existe no sistema, ou pode adicionar um produto novo, que não existe no sistema, e que será
buscado conforme os requisitos informados pelo usuário para o produto.
Pretendemos utilizar um sistema de AI por NLP para fazer essa busca dinâmica de produtos, onde o usuário informa os
requisitos do produto que ele deseja, e o sistema retorna os produtos que mais se encaixam nesses requisitos.
Por hora fora do escopo do projeto, mas que pretendemos implementar futuramente, é a possibilidade do sistema fazer
o acompanhamento do pedido após a entrega até o produto estar disponível no estoque ou ao solicitante.

## Tecnologias utilizadas

- [Java 21](https://www.java.com/pt-BR/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Keycloak](https://www.keycloak.org/)
- [PostgreSQL](https://www.postgresql.org/)
- [Oracle Database](https://www.oracle.com/br/database/)
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Postman](https://www.postman.com/)

## Como executar

### Pré-requisitos

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)

### Executando

1. Clone o repositório
2. Execute o comando abaixo para iniciar o sistema, pode levar alguns minutos dependendo da sua conexão com a internet
e do computador que está executando o projeto:
```shell
docker compose up
```
Obs: Caso queira executar o projeto em background, execute o comando abaixo:
```shell
docker compose up -d
```

## Endpoints

### Coleção do Postman

Para facilitar o uso do sistema, foi criada uma coleção do Postman com todos os endpoints, ela está disponível na pasta
[postman](/postman) do projeto.

[Coleção do Postman](/postman/postman_collection.json)

### Authenticação

Todos os endpoints abaixo necessitam de autenticação, para isso, é necessário obter um token de acesso, para isso,
utilize o endpoint abaixo:

#### POST /auth/login

##### Request
```json
{
  "username": "username",
  "password": "password"
}
```

##### Response HTTP 200 OK
```json
{
    "access_token": "eyJ...u-PgBA",
    "expires_in": 300,
    "refresh_token": "eyJ...QiNw",
    "refresh_expires_in": 1800,
    "token_type": "Bearer",
    "not-before-policy": 0,
    "session_state": "711...7d33",
    "scope": "email profile"
}
```

Obs: Um usuário padrão já foi criado, com as seguintes credenciais:
- username: fiap
- password: fiap

### Produtos

#### GET /api/products

Retorna todos os produtos cadastrados no sistema

##### Response HTTP 200 OK
```json
[
  {
    "id": 1,
    "name": "Test",
    "description": null,
    "brand": null,
    "types": [
      "Toys",
      "Electronics"
    ]
  },
  {
    "id": 2,
    "name": "Test",
    "description": null,
    "brand": null,
    "types": null
  },
  {
    "id": 3,
    "name": "Test",
    "description": null,
    "brand": null,
    "types": null
  },
  ...
]
```

#### GET /api/products/{id}

Retorna o produto com o id informado

##### Response HTTP 200 OK
```json
{
  "id": 1,
  "name": "Test",
  "description": null,
  "brand": null,
  "types": [
    "Toys",
    "Electronics"
  ]
}
```

#### POST /api/products

Cria um novo produto

##### Request
```json
{
  "name": "Test",
  "description": null,
  "brand": null,
  "types": [
    "Electronics",
    "Toys"
  ]
}
```

##### Response HTTP 201 CREATED
```json
{
  "id": 1,
  "name": "Test",
  "description": null,
  "brand": null,
  "types": [
    "Electronics",
    "Toys"
  ]
}
```

#### PUT /api/products/{id}

Atualiza o produto com o id informado

##### Request
```json
{
  "name": "Test",
  "description": null,
  "brand": null,
  "types": [
    "Electronics",
    "Toys"
  ]
}
```

##### Response HTTP 200 OK
```json
{
  "id": 1,
  "name": "Test",
  "description": null,
  "brand": null,
  "types": [
    "Electronics",
    "Toys"
  ]
}
```

#### DELETE /api/products/{id}

Deleta o produto com o id informado

##### Response HTTP 204 NO CONTENT 

#### GET /api/products/brands

Retorna todas as marcas cadastradas no sistema

##### Response HTTP 200 OK
```json
[
  "Acer",
  "Apple",
  "Asus",
  ...
]
```

### Requisições de compra

#### GET /api/purchase-requests

Retorna todas as requisições de compra cadastradas no sistema

##### Response HTTP 200 OK
```json
[
  {
    "id": 1,
    "requestDate": "2023-12-03T21:05:51.533884Z",
    "pending": true,
    "products": [
      {
        "id": 1,
        "name": "Test",
        "description": null,
        "brand": null,
        "types": [
          "Toys",
          "Electronics"
        ],
        "quantity": 2
      }
    ]
  }
]
```

#### GET /api/purchase-requests/{id}

Retorna a requisição de compra com o id informado

##### Response HTTP 200 OK
```json
{
  "id": 1,
  "requestDate": "2023-12-03T21:05:51.533884Z",
  "pending": true,
  "products": [
    {
      "id": 1,
      "name": "Test",
      "description": null,
      "brand": null,
      "types": [
        "Toys",
        "Electronics"
      ],
      "quantity": 2
    }
  ]
}
```

#### POST /api/purchase-requests

Cria uma nova requisição de compra

##### Request
```json
{
  "products": [
    {
      "id": 1,
      "quantity": 1
    }
  ]
}
```

##### Response HTTP 201 CREATED
```json
{
  "id": 1,
  "requestDate": "2023-12-03T21:05:51.533884Z",
  "pending": true,
  "products": [
    {
      "id": 1,
      "name": "Test",
      "description": null,
      "brand": null,
      "types": [
        "Toys",
        "Electronics"
      ],
      "quantity": 2
    }
  ]
}
```

#### PUT /api/purchase-requests/{id}/approve

Aprova a requisição de compra com o id informado

##### Response HTTP 200 OK
```json
{
  "id": 1,
  "requestDate": "2023-12-03T21:05:51.533884Z",
  "pending": false,
  "products": [
    {
      "id": 1,
      "name": "Test",
      "description": null,
      "brand": null,
      "types": [
        "Toys",
        "Electronics"
      ],
      "quantity": 2
    }
  ]
}
```

#### POST /api/purchase-requests/{id}/products

Adiciona um novo produto a requisição de compra com o id informado

##### Request
```json
{
  "id": 1,
  "quantity": 1
}
```

##### Response HTTP 200 OK
```json
{
  "id": 1,
  "requestDate": "2023-12-03T21:05:51.533884Z",
  "pending": true,
  "products": [
    {
      "id": 1,
      "name": "Test",
      "description": null,
      "brand": null,
      "types": [
        "Toys",
        "Electronics"
      ],
      "quantity": 2
    }
  ]
}
```

#### PUT /api/purchase-requests/{id}/products/{productId}

Atualiza o produto da requisição de compra com o id informado

##### Request
```json
{
  "quantity": 2
}
```

##### Response HTTP 200 OK
```json
{
  "id": 1,
  "requestDate": "2023-12-03T21:05:51.533884Z",
  "pending": true,
  "products": [
    {
      "id": 1,
      "name": "Test",
      "description": null,
      "brand": null,
      "types": [
        "Toys",
        "Electronics"
      ],
      "quantity": 2
    }
  ]
}
```

#### DELETE /api/purchase-requests/{id}/products/{productId}

Deleta o produto da requisição de compra com o id informado

##### Response HTTP 200 OK
```json
{
  "id": 1,
  "requestDate": "2023-12-03T21:05:51.533884Z",
  "pending": true,
  "products": [
    {
      "id": 1,
      "name": "Test",
      "description": null,
      "brand": null,
      "types": [
        "Toys",
        "Electronics"
      ],
      "quantity": 2
    }
  ]
}
```

## Arquiteura da solução

![Arquiteura da solução](/diagrams/arch.png)

## Modelo DER

![Modelo DER](/diagrams/der.svg)

## Diagrama de classes

![Diagrama de classes](/diagrams/project_diagram.svg)
