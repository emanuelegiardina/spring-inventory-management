Spring Inventory Management
📌 Panoramica

Spring Inventory Management è un progetto backend basato su microservizi, sviluppato con Spring Boot, che simula una parte fondamentale di un sistema e-commerce: la gestione dei prodotti e la verifica della disponibilità di stock durante il processo di ordine (cart).
L’obiettivo principale è dimostrare l’uso di Spring ecosystem, security enterprise, comunicazione tra microservizi e resilience patterns.

Componenti principali

---API Gateway 

Entry point unico per il sistema

Validazione JWT

Routing verso i microservizi

Circuit Breaker e fallback con Resilience4j verso order e product service

---Product Service

Crea un prodotto

Visualizza tutti prodotti

Controllo stock e prodotti

Conferma il pre-ordine decremnetando le stock

Endpoint protetti con Spring Security e @PreAuthorize

---Order Service

Verifica la disponibilità dei prodotti

Crea un ordine 

Conferma ordine

Comunicazione reattiva tramite WebClient

Circuit Breaker, Retry e TimeLimiter

---common.dto 

libreria per gestire scambio dati tra FE e BE 

🔐 Sicurezza

La sicurezza è implementata con Spring Security e OAuth2 Resource Server.

Keycloak come Identity Provider

Autenticazione basata su JWT

Autorizzazione tramite:
Realm roles (es. admin, user)
Client roles (es. product_confirm, product_check-stock)

Propagazione del token JWT nelle chiamate server-to-server
(Gateway → Order Service → Product Service)

🗄️ Persistenza dei dati
Database MySQL sia per product e order service
Avvio tramite Docker

🛠️ Stack Tecnologico
Java 17
Spring Boot 3
Spring Security 6
Spring Cloud Gateway
Spring WebFlux (WebClient)
OAuth2 / JWT
Keycloak
Resilience4j (Circuit Breaker, Retry, TimeLimiter)
MySQL
Docker

🚀 Avvio del progetto
Prerequisiti
Java 17+
Maven
Docker 
Keycloak (con realm già configurato)
MySQL (tramite Docker)

1️⃣ Avvio infrastruttura (Docker)

Avvia MySQL e Keycloak:

2️⃣ Configurazione Keycloak

Keycloak deve essere configurato:

è disponibile un file JSON di esportazione del realm.

3️⃣ Avvio dei servizi

Avvia i servizi nel seguente ordine:

Product Service
Order Service
API Gateway

Ogni servizio può essere avviato con:
mvn spring-boot:run

📡 Flusso di esempio

Il client invia una richiesta al Gateway

Il Gateway valida il JWT

La richiesta viene inoltrata all’Order Service

L’Order Service chiama il Product Service per verificare lo stock

Il Product Service risponde con la disponibilità

La risposta ritorna al client

In caso di indisponibilità di un servizio:

Il Circuit Breaker si attiva

Viene restituita una risposta di fallback

🎯 Obiettivi del progetto

Applicare un’architettura backend di livello enterprise

Comprendere la comunicazione tra microservizi

Utilizzare Spring Security con OAuth2 e JWT

Implementare pattern di resilienza

Lavorare con programmazione reattiva

📈 Miglioramenti futuri

Pattern Saga per gestire meglio la transizione della conferma ordine da parte del client.
Aggiungere un nuovo microservices: payment


👨‍💻 Autore

Progetto sviluppato a scopo formativo e professionale, per approfondire lo sviluppo backend in Java e Spring Boot.
