<h1>
  <img src="resources/images/logo.png" width="40" style="vertical-align: middle;">
  Sistema di Gestione Prenotazioni per Studi Medici
</h1>

Applicazione desktop per la gestione di piccoli studi medici e ambulatori, progettata per offrire una soluzione semplice, affidabile e non basata su cloud.  
Il sistema offre funzionalità per la gestione di pazienti e medici, prenotazione delle visite e pagamenti.

## 📑 Indice
- [Funzionalità principali](#funzionalità-principali)
- [Tecnologie utilizzate](#tecnologie-utilizzate)
- [Architettura del progetto](#architettura-del-sistema)
- [Prerequisiti](#prerequisiti)
- [Installazione](#installazione)
- [Configurazione Database](#configurazione-database)
- [Avvio dell’applicazione](#avvio-dell-applicazione)


## Funzionalità principali
1️⃣ Gestione degli utenti
L’applicazione permette agli utenti di identificarsi come:
-Medico
-Segretario

Ogni utente può:
-Registrare un account personale
-Accedere all’applicazione con le proprie credenziali

L’accesso alle funzionalità dell’applicazione deve essere controllato mediante autenticazione e autorizzazione, in conformità con il ruolo dell’utente (Medico o Segretario).

2️⃣ Funzionalità per i Medici
1. Inserire le proprie fasce orarie di disponibilità.
2. Ricercare i dati di un paziente tramite codice fiscale.
3. Inserire prescrizioni per un paziente associato a una visita registrata.
4. Modificare le note cliniche relative al paziente.

3️⃣ Funzionalità per i Segretari
1. Ricercare i dati di un paziente tramite codice fiscale.
2. Modificare i dati anagrafici di un paziente.
3. Visualizzare l’elenco delle visite di un paziente.
4. Modificare l’importo e lo stato di pagamento di ogni visita.
5. Visualizzare un calendario con le prenotazioni di un paziente.
6. Creare nuove prenotazioni sulla base delle fasce orarie di disponibilità dei medici, con suggerimento automatico delle fasce disponibili.
7. Scaricare report mensili sul totale delle visite e dei pagamenti.

## Tecnologie utilizzate
- Java, linguaggio di programmazione utilizzato per lo sviluppo dell’applicazione  
- JavaFX, framework per la realizzazione dell’interfaccia grafica  
- JDBC, API per l’accesso e la gestione del database  
- MySQL, sistema di gestione di database relazionale

## Architettura del progetto
Il progetto segue il pattern architetturale MVC (Model-View-Controller), con separazione tra logica applicativa, interfaccia grafica e accesso ai dati, al fine di migliorare manutenibilità e chiarezza del codice.

## Prerequisiti
Per eseguire l’applicazione è necessario disporre di:
- Java JDK 
- Un database relazionale installato (MySQL)
- Un IDE Java (IntelliJ IDEA)

## Installazione
1. Clonare o scaricare il repository
2. Importare il progetto all’interno di un IDE Java
3. Configurare la connessione al database

# Configurazione database
L’applicazione utilizza un database relazionale per la memorizzazione dei dati.
Prima dell’avvio è necessario configurare i parametri di connessione al database (nome del database, username e password) nel file di configurazione previsto dal progetto.

## Avvio dell'applicazione
L’applicazione può essere avviata direttamente dall’IDE.





