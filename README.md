🍽️ Sistema Delivery – UNASP

Sistema de Delivery desenvolvido como projeto acadêmico para a disciplina de Engenharia de Software / Desenvolvimento Fullstack, no UNASP.
O sistema simula o fluxo completo de um restaurante digital — do cardápio ao preparo na cozinha — com integrações entre múltiplos módulos front-end e back-end.

🎯 Objetivo do Projeto

Criar um sistema completo de Delivery, permitindo:

que o cliente navegue no menu, selecione pratos e finalize pedidos;

que o setor de cozinha receba e gerencie os pedidos em tempo real;

que o delivery acompanhe e atualize o status da entrega;

que o usuário visualize o histórico completo de pedidos;

que tudo funcione de forma integrada entre front-end e back-end.

O foco do projeto é praticar arquitetura em camadas, integração entre sistemas, consumo de APIs REST, versionamento com GitHub e aplicação de boas práticas de desenvolvimento.

🧩 Módulos do Sistema
Backend Cozinha

Recebimento dos pedidos enviados pelo front

Atualização de status (em preparo, pronto, finalizado)

Comunicação com histórico

Backend Delivery

Gestão do fluxo de entrega

Atualização de status do pedido (a caminho, entregue)

Integração com módulo histórico

Backend Histórico

Registra todos os pedidos, status e timestamps

Permite consulta de pedidos concluídos

Frontend Menu

Exibição do cardápio

Listagem de pratos

Botões para adicionar ao carrinho

Tela de detalhes do prato

Frontend Carrinho

Montagem do pedido

Cálculo de totais

Envio do pedido para o backend

Frontend Cozinha

Painel para visualizar pedidos em tempo real

Ações de preparo e finalização

Frontend Histórico

Consulta de todos os pedidos concluídos

Exibição detalhada dos registros

🧰 Tecnologias Utilizadas
Backend

Java 17

Spring Boot

Spring Web

Spring Data JPA

Lombok

Maven

Frontend

Angular

HTML / CSS

TypeScript

Banco de Dados

H2 / MySQL (dependendo do ambiente configurado)

Ferramentas

Git e GitHub

Postman / Swagger

VS Code / IntelliJ

🚀 Como Rodar o Projeto
📌 Backend

Clonar o repositório:

git clone https://github.com/[SEU_USUARIO]/[SEU_REPOSITORIO].git


Acessar o diretório:

cd [NOME_DO_PROJETO]


Rodar o backend:

mvn spring-boot:run


A API subirá em:
http://localhost:8080

📌 Frontend

Acessar a pasta do front:

cd frontend


Instalar dependências:

npm install


Rodar o projeto:

npm start


A aplicação abrirá em:
http://localhost:4200