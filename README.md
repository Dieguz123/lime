# Lime 🍋‍🟩

Lime is an app where you swipe through movies and say whether you like them or not.
You join a room with one or more people by sharing a link, and when everyone likes the same movie, it’s a match!
Thanks to Lime, I will never argue with my girlfriend again about which movie to watch. 🍿


> 🚧 Progetto in sviluppo. Al momento è disponibile il backend con
> un primo set di endpoint sui film.


## Tecnologie

- Java 25
- Spring Boot 4
- Gradle
- JUnit 5
## Come avviarlo

```bash
./gradlew bootRun
```

L'app parte su `http://localhost:8080`.

## Test

```bash
./gradlew test
```

## Endpoint

| Metodo | Percorso | Descrizione |
|---|---|---|
| GET | `/films` | Restituisce l'elenco di tutti i film |
| GET | `/api/films/{id}` | Restituisce il film con l'id indicato, oppure `404 Not Found` se non esiste |

## Struttura

Il codice è organizzato a strati:

- **controller**: riceve le richieste HTTP e decide la risposta (200, 404...)
- **service**: contiene la logica, come la ricerca dei film per id
- **model**: le classi che rappresentano i dati, come `Film`

Il controller riceve il service tramite dependency injection nel costruttore:
in questo modo i due strati restano separati, e la logica si può testare
senza avviare Spring.

## Prossimi passi

- Database PostgreSQL al posto della lista in memoria
- Stanze di coppia con codice di invito
- Swipe e logica di match
- Film reali tramite le API di TMDB
- Interfaccia web per smartphone