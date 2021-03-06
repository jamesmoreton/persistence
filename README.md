# Persistence

Simple Java database persistence with [PostgreSQL](https://www.postgresql.org/) and [MongoDB](https://www.mongodb.com/).

- Compiles & runs under Java 15
- Built with Maven
- Google Guice as dependency injection framework
- Postgres 12 & MongoDB

## Postgres

An open source object-relational database system.

### Setup

> N.B. Postgres 12 must be pre-installed

To get the database created ready for incoming connections and the example `employee` schema created, from the root directory `persistence`, run:

```
./scripts/postgres.sh create
```

This will create a local Postgres cluster running on port `15432`, with user `test` and password `test`.

> If this fails with error `Permission denied`, first run `chmod +rx ./scripts/postgres.sh` to make the file executable and then retry the above command.

To test the Postgres persistence, execute:

```
mvn clean install
mvn exec:java -Dexec.mainClass=com.jamesmoreton.postgres.PostgresMainline
```

...or run from your favourite IDE.

To connect to the database via the terminal, execute (with password `test`):

```
psql -p 15432 -U test -d postgres -h 127.0.0.1
```

You can start/stop the Postgres cluster, or reset it entirely including all its data, using the script `./scripts/postgres.sh`.

## MongoDB

A NoSQL document-oriented database which stores data in JSON-like documents. 

### Setup

> N.B. MongoDB must be pre-installed

To get the MongoDB daemon started, from the root directory `persistence`, run:

```
./scripts/mongo.sh start
```

> If this fails with error `Permission denied`, first run `chmod +rx ./scripts/mongo.sh` to make the file executable and then retry the above command.

To test the MongoDB persistence, execute:

```
mvn clean install
mvn exec:java -Dexec.mainClass=com.jamesmoreton.mongo.MongoMainline
```

...or run from your favourite IDE.

To connect to the database via the terminal, execute:

```
mongo --port 26543
```

You can shut down the MongoDB daemon using the script `./scripts/mongo.sh shutdown`.

## Future improvements

- Implement more extensive database interactions, e.g. update, bulk insert etc. 
- Create SQL schemas using flyway
- Build out database transactions
- Database testing