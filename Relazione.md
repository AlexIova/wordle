# wordle

Relazione sul progetto di Lab III *wordle*.

# Struttura
- Organizzazione client/server con messaggi fatti come classi.
- Come sono fatti i messaggi.
- Properties file

# Server

## Strutture dati

# Database utenti

# Database giochi

# Lista parole


## Concorrenza

### Client threads
Per i client ho optato l'utilizzo di ThreadPools in particolare la versione `FixedThreadPool`. Questo si adatta bene nell'utilizzo efficiente delle risorse per la gestione di vari client, con la possibilità della gestione contemporanea fino ad un massimo di _n_ client contemporanei. Nel caso in cui vi siano più di _n_ client in contemporanea il client _n+1_ dovrà aspettare e non sarà in grado di proseguire.
Avrei anche potuto usare un `CachedThreadPool` per poter riuscire a servire ancora più client oltre al limite inposto ma un `CachedThreadPool` potrebbe portare al dover gestire più client di quanti la macchina del server sarebbe effettivamente in grado di poter gestire.
Assumo che l'ordine di grandezza dei client possa essere stimato a priori e quindi ho optato per un `FixedThreadPool`.

### ChangeThread Thread
Viene creato un thread che leggerà dalla lista delle parole e le caricherà in memoria in una lista per poi restituire ad altri thread che lo richiedono la parola del momento.
Nello specifico il thread adibito al chiedere le nuove parole da usare nel gioco è il thread `ChangeThread` che runna costantemente in background.

### ExitHandler Thread
Dato che prima che il server venga chiuso è opportuno salvare su file i cambiamenti effettuati a `gameDB` e `utentiDB` nel caso di un `SIGINT` si attiverà il `ShutdownHook` che creerà un nuovo thread che si occuperà dell'uscita pulita scrivendo in un file JSON i cambiamenti effettuati ai due database.

### Sincronizzazione


## Strutture dati

# Client

## Concorrenza

### Database notifiche

### McListenerThread Thread

## Strutture dati

### Database notifiche

# Compilazione ed esecuzione

## Compilazione

### Compilazione programma

### Creazione jar

## Esecuzione

### Esecuzione normale

### Esecuzione da jar

## Uso di script

# Dipendenze