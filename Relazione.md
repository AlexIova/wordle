# wordle

Lorem Ipsum questo è un progetto di Lab III


# Server

Lorem ipsum

## Concorrenza

### Client threads
Per i client ho optato l'utilizzo di ThreadPools in particolare la versione `FixedThreadPool`. Questo si adatta bene nell'utilizzo efficiente delle risorse per la gestione di vari client, con la possibilità della gestione contemporanea fino ad un massimo di _n_ client contemporanei. Nel caso in cui vi siano più di _n_ client in contemporanea il client _n+1_ dovrà aspettare e non sarà in grado di proseguire.
Avrei anche potuto usare un `CachedThreadPool` per poter riuscire a servire ancora più client oltre al limite inposto ma un `CachedThreadPool` potrebbe portare al dover gestire più client di quanti la macchina del server sarebbe effettivamente in grado di poter gestire.
Assumo che l'ordine di grandezza dei client possa essere stimato a priori e quindi ho optato per un `FixedThreadPool`.

### WordPicker Thread
Viene creato un thread che leggerà dalla lista delle parole e le caricherà in memoria in una lista per poi restituire ad altri thread che lo richiedono la parola del momento.

## Strutture dati

# Client



