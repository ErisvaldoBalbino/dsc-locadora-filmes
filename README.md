# 🎬 Sistema de Gerenciamento para Locadora de Filmes

API REST desenvolvida em Spring Boot para gerenciar o catálogo de filmes de uma locadora, controlar o estoque de cópias disponíveis e administrar o processo de locações e devoluções.

## 🛠️ Tecnologias

- **Java**
- **Spring Boot**


## 📋 Funcionalidades

### 🎬 Catálogo de Filmes
- Gerenciamento de filmes (CRUD)
- Controle de gêneros (CRUD)
- Controle de estoque

### 👥 Clientes
- Cadastro e gerenciamento de clientes

### 📦 Locações
- Registro de locações
- Registro de devoluções
- Consultas de disponibilidade e histórico

## 🔌 Endpoints da API

### 🎬 Filmes
- `GET /api/filmes` - Listar todos os filmes
- `GET /api/filmes/{id}` - Buscar filme por ID
- `GET /api/filmes/disponiveis` - Listar filmes disponíveis (com cópias em estoque)
- `POST /api/filmes` - Criar novo filme
- `PUT /api/filmes/{id}` - Atualizar filme
- `DELETE /api/filmes/{id}` - Deletar filme

### 🏷️ Gêneros
- `GET /api/generos` - Listar todos os gêneros
- `GET /api/generos/{id}` - Buscar gênero por ID
- `POST /api/generos` - Criar novo gênero
- `PUT /api/generos/{id}` - Atualizar gênero
- `DELETE /api/generos/{id}` - Deletar gênero

### 👤 Clientes
- `GET /api/clientes` - Listar todos os clientes
- `GET /api/clientes/{id}` - Buscar cliente por ID
- `GET /api/clientes/{id}/locacoes` - Histórico de locações do cliente
- `POST /api/clientes` - Criar novo cliente
- `PUT /api/clientes/{id}` - Atualizar cliente
- `DELETE /api/clientes/{id}` - Deletar cliente

### 📦 Locações
- `GET /api/locacoes` - Listar todas as locações
- `GET /api/locacoes/{id}` - Buscar locação por ID
- `GET /api/locacoes/atrasadas` - Listar locações atrasadas
- `POST /api/locacoes` - Criar nova locação
- `PATCH /api/locacoes/{id}/devolver` - Registrar devolução de uma locação