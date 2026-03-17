#  Thearckay Portfolio - Backend API

Backend desenvolvido para gerenciar os projetos do meu portfólio pessoal. A aplicação é uma API RESTful completa com autenticação, upload de imagens e persistência em banco de dados na nuvem.

##  Tecnologias Utilizadas

* **Java 25**
* **Spring Boot**
* **Spring Security & JWT** (Autenticação e Autorização)
* **PostgreSQL** (Banco de dados em produção via Aiven)
* **Spring Data JPA / Hibernate**
* **Cloudinary API** (Armazenamento e gerenciamento de imagens)
* **Docker** (Containerização para deploy)
* **Render** (Deploy da API)

## Funcionalidades

* **Autenticação:** Login seguro via token JWT para acesso ao painel de administrador.
* **CRUD de Projetos:** Endpoints para criação, listagem, atualização e exclusão de projetos do portfólio.
* **Upload de Mídia:** Integração com o Cloudinary para upload e exclusão otimizada das capas dos projetos (suporte a FormData).
* **Segurança e CORS:** Configurado para receber requisições de forma segura do frontend hospedado na Vercel.

