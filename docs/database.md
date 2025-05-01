# Fines Management System - Database Documentation

## Overview

This document provides detailed information about the database structure for the Fines Management System. The database is designed to store information about police officers, drivers, traffic offenses, and issued fines.

## Database Schema

### Entity Relationship Diagram (ERD)

```
                +----------------+
                | police_officers|
                +----------------+
                | id             |
                | service_id     |
                | first_name     |
                | last_name      |
                | badge_number   |
                | department     |
                | email          |
                | phone          |
                | created_at     |
                | updated_at     |
                +----------------+
                        |
                        | 1
                        |
                        v
                      n |
+----------------+    +--------------------+          +-------------------+
| drivers        |    | fines              |          | offenses          |
+----------------+    +--------------------+          +-------------------+
| id             |    | id                 |          | id                |
| pesel          |    | fine_number        |          | code              |
| license_number |    | police_officer_id  |          | description       |
| first_name     | 1  | driver_id          | n        | penalty_points    |
| last_name      |<---| issue_date         |          | min_fine_amount   |
| date_of_birth  |    | location           |          | max_fine_amount   |
| address        |    | total_amount       |          | repeatable_offense|
| email          |    | total_points       |          | created_at        |
| phone          |    | status             |          | updated_at        |
| license_issue_date| | acceptance_date    |          +-------------------+
| license_expiry_date|| cancellation_date  |                 |
| created_at     |    | cancellation_reason|                 |
| updated_at     |    | created_at         |                 |
+----------------+    | updated_at         |                 |
                      +--------------------+                 |
                              |                              |
                              | 1                            | n
                              v                              |
                      +----------------+                     |
                      | fine_offenses  |                     |
                      +----------------+                     |
                      | id             |                     |
                      | fine_id        |---------------------+
                      | offense_id     |
                      | amount         |
                      | points         |
                      | is_repeat      |
                      | created_at     |
                      +----------------+
```

## Tables Description

### 1. police_officers

Stores information about police officers who issue fines.

| Column Name    | Data Type     | Description                         |
|----------------|---------------|-------------------------------------|
| id             | SERIAL        | Primary key                         |
| service_id     | VARCHAR(20)   | Unique service identification number|
| first_name     | VARCHAR(50)   | First name of the officer           |
| last_name      | VARCHAR(50)   | Last name of the officer            |
| badge_number   | VARCHAR(20)   | Badge number of the officer         |
| department     | VARCHAR(100)  | Department the officer belongs to   |
| email          | VARCHAR(100)  | Email address of the officer        |
| phone          | VARCHAR(20)   | Phone number of the officer         |
| created_at     | TIMESTAMP     | Record creation timestamp           |
| updated_at     | TIMESTAMP     | Record update timestamp             |

### 2. drivers

Stores information about drivers who may receive fines.

| Column Name       | Data Type     | Description                      |
|-------------------|---------------|----------------------------------|
| id                | SERIAL        | Primary key                      |
| pesel             | VARCHAR(11)   | Polish national identification number |
| license_number    | VARCHAR(20)   | Driver's license number          |
| first_name        | VARCHAR(50)   | First name of the driver         |
| last_name         | VARCHAR(50)   | Last name of the driver          |
| date_of_birth     | DATE          | Date of birth                    |
| address           | VARCHAR(255)  | Residential address              |
| email             | VARCHAR(100)  | Email address                    |
| phone             | VARCHAR(20)   | Phone number                     |
| license_issue_date| DATE          | Date when license was issued     |
| license_expiry_date| DATE         | Date when license expires        |
| created_at        | TIMESTAMP     | Record creation timestamp        |
| updated_at        | TIMESTAMP     | Record update timestamp          |

### 3. offenses

Catalog of traffic offenses with associated penalties.

| Column Name       | Data Type     | Description                     |
|-------------------|---------------|---------------------------------|
| id                | SERIAL        | Primary key                     |
| code              | VARCHAR(20)   | Unique code for the offense     |
| description       | TEXT          | Description of the offense      |
| penalty_points    | INT           | Number of penalty points        |
| min_fine_amount   | DECIMAL(10,2) | Minimum fine amount             |
| max_fine_amount   | DECIMAL(10,2) | Maximum fine amount             |
| repeatable_offense| BOOLEAN       | If true, repeat offenses are valued double |
| created_at        | TIMESTAMP     | Record creation timestamp       |
| updated_at        | TIMESTAMP     | Record update timestamp         |

### 4. fines

Records of fines issued to drivers by police officers.

| Column Name         | Data Type     | Description                    |
|---------------------|---------------|--------------------------------|
| id                  | SERIAL        | Primary key                    |
| fine_number         | VARCHAR(20)   | Unique number for the fine     |
| police_officer_id   | INT           | References police_officers.id  |
| driver_id           | INT           | References drivers.id          |
| issue_date          | TIMESTAMP     | Date and time when fine was issued |
| location            | VARCHAR(255)  | Location where fine was issued |
| total_amount        | DECIMAL(10,2) | Total amount of the fine       |
| total_points        | INT           | Total penalty points           |
| status              | VARCHAR(20)   | Status: ISSUED, ACCEPTED, CANCELED |
| acceptance_date     | TIMESTAMP     | Date when fine was accepted    |
| cancellation_date   | TIMESTAMP     | Date when fine was canceled    |
| cancellation_reason | TEXT          | Reason for cancellation        |
| created_at          | TIMESTAMP     | Record creation timestamp      |
| updated_at          | TIMESTAMP     | Record update timestamp        |

### 5. fine_offenses

Junction table linking fines to offenses (many-to-many relationship).

| Column Name | Data Type     | Description                       |
|-------------|---------------|-----------------------------------|
| id          | SERIAL        | Primary key                       |
| fine_id     | INT           | References fines.id               |
| offense_id  | INT           | References offenses.id            |
| amount      | DECIMAL(10,2) | Fine amount for this offense      |
| points      | INT           | Points for this offense           |
| is_repeat   | BOOLEAN       | If true, this is a repeat offense |
| created_at  | TIMESTAMP     | Record creation timestamp         |

## Views

### 1. police_officer_fines

View for police officers to see details of fines they have issued.

### 2. driver_fines

View for drivers to see their fines.

### 3. driver_total_points

View to calculate the total penalty points for each driver.

## Indexes

The following indexes are created to optimize common queries:

- `idx_fines_driver_id` - For quick lookup of fines by driver
- `idx_fines_police_officer_id` - For quick lookup of fines by police officer
- `idx_fines_status` - For filtering fines by status
- `idx_drivers_pesel` - For quick lookup of drivers by PESEL
- `idx_police_officers_service_id` - For quick lookup of officers by service ID

## Triggers

The database uses triggers to automatically update the `updated_at` timestamp whenever a record is modified:

- `update_police_officers_timestamp`
- `update_drivers_timestamp`
- `update_offenses_timestamp`
- `update_fines_timestamp`

## Relationships

1. One police officer can issue many fines (One-to-Many)
2. One driver can receive many fines (One-to-Many)
3. One fine can include many offenses (Many-to-Many through fine_offenses)
4. One offense can be included in many fines (Many-to-Many through fine_offenses)

## Data Constraints

- PESEL and license_number must be unique for drivers
- service_id must be unique for police officers
- code must be unique for offenses
- fine_number must be unique for fines
- fine status must be one of: ISSUED, ACCEPTED, CANCELED