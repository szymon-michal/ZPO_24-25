# Docker Setup for Fines Management System

This document provides instructions for setting up and running the Fines Management System using Docker.



## Docker Components

The system uses the following Docker components:

1. PostgreSQL 16 database container
2. Java 17 (Eclipse Temurin) for building the application **TODO** later 




### 1. Connect to the Database

You can connect to the PostgreSQL database using any PostgreSQL client with the following credentials:

- Host: localhost
- Port: 5432
- Database: fines_db
- Username: finesadmin
- Password: finespassword

For example, using `psql` client:

```bash
psql -h localhost -p 5432 -U finesadmin -d fines_db
```
### 2. Seting up DataBase schema
yousimply have to run:
```bash
docker exec -it fines-db psql -U finesadmin -d fines_db -f /temp/SQL/DataBaseStructure.sql
```
in your terminal too create structures
next you have to reload accual data with:
```bash
docker exec -it fines-db psql -U finesadmin -d fines_db -f /temp/SQL/DummyData.sql
```
now you are ready for testing and developing
## Database Management

### Viewing Database Schema

Connect to the database and run:

```sql
\dt
```

To view details of a specific table:

```sql
\d+ table_name
```

### Running Queries

You can run SQL queries directly against the database:

```sql
SELECT * FROM drivers LIMIT 5;
SELECT * FROM offenses WHERE penalty_points > 5;
```

### Resetting the Database

If you need to delete all structures and tables

```bash
 docker exec -it fines-db psql -U finesadmin -d fines_db -f /temp/SQL/DropStructures.sql
```

This will:
1. Drop all tables and views
2. extinct all data 

but you can also wipe all data from all structures without damaging the table structures simply by doing:
```bash
  docker exec -it fines-db psql -U finesadmin -d fines_db -f /temp/SQL/CleanDataBase.sql 
```

To reload the dummy data after resetting:

```bash
docker exec -it fines-db psql -U finesadmin -d fines_db -f /temp/SQL/DummyData.sql
```



## Database Backup and Restore

### Creating a Backup

```bash
docker exec -t $(docker ps -q -f name=postgres) pg_dump -U finesadmin -d fines_db > backup.sql
```

### Restoring from Backup

```bash
cat backup.sql | docker exec -i $(docker ps -q -f name=postgres) psql -U finesadmin -d fines_db
```

## Troubleshooting

### Container Not Starting

Check the logs:

```bash
docker-compose logs
```


### Healthcheck Status

View the container's health status:

```bash
docker inspect --format='{{.State.Health.Status}}' $(docker ps -q -f name=postgres)
```