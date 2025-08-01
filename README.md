# Sistema de Chat TCP – Multiusuário 

## Descrição
Sistema de bate-papo via linha de comando usando sockets TCP, com suporte a múltiplos usuários em tempo real. 

## Funcionalidades
- Comunicação bidirecional (servidor ↔ clientes)
- Vários usuários simultâneos
- Notificações de entrada/saída
- Identificação por nome
- Comandos `/sair` ou `/quit`
- Gerenciamento com Thread Pool
- Interface do servidor com log e contador

## Arquitetura
- `ChatServer.java`: aceita conexões, gerencia clientes, envia mensagens administrativas
- `ChatClient.java`: conecta ao servidor, envia e recebe mensagens
- `ClientHandler`: gerencia cada cliente
- `ServerConsoleHandler`: permite mensagens do servidor via console

## Execução

1. **Iniciar o Servidor** (em um terminal):
bash
java labs/lab-sockets/src/ChatServer


2. **Conectar Clientes** (em terminais separados):
bash
java labs/lab-sockets/src/ChatClient 
# 

### Compilação
    
    javac labs/lab-sockets/src/ChatServer.java 
    labs/lab-sockets/src/ChatClient.java
