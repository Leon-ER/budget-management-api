# Budget-Management-API

This is a **Spring Boot REST API** for managing budgets, transactions, and financial reports.  
It supports **user authentication, budgeting, transaction tracking, and report generation**.

## Objective

Create a fully functional Budget Management API using. The project demonstrates our ability to design,
develop, and secure APIs while implementing internationalization and dynamic localization.

## Requirements

* IDE of your choice
* JDK 21 (java --version should return valid version)
* Maven  (mvn --version should return valid version)

## How to run

Clone the repo
```
git clone https://github.com/Leon-ER/budget-management-api.git
```
Navigate to the repo 

```
cd budget-management-api
```

### Setup database SQL Server

```
USE [budget_management]
GO


CREATE TABLE [dbo].[Users] (
    [user_id]    INT IDENTITY(1,1) PRIMARY KEY,
    [username]   NVARCHAR(50)  NOT NULL UNIQUE,
    [password]   NVARCHAR(MAX) NOT NULL,
    [email]      NVARCHAR(100) NOT NULL UNIQUE,
    [role]       NVARCHAR(20)  NOT NULL,
    [created_at] DATETIME      NOT NULL DEFAULT GETDATE()
);
GO


CREATE TABLE [dbo].[Budgets] (
    [budget_id]      INT IDENTITY(1,1) PRIMARY KEY,
    [user_id]        INT NOT NULL,
    [category_name]  NVARCHAR(50) NOT NULL,
    [total_budget]   DECIMAL(10, 2) NOT NULL,
    [created_at]     DATETIME NOT NULL DEFAULT GETDATE(),

    -- 🔗 Foreign Key Constraint
    CONSTRAINT FK_Budgets_Users FOREIGN KEY ([user_id]) REFERENCES [dbo].[Users]([user_id]) ON DELETE CASCADE
);
GO

CREATE TABLE [dbo].[Transactions] (
    [transaction_id]   INT IDENTITY(1,1) PRIMARY KEY,
    [user_id]         INT NOT NULL,
    [budget_id]       INT NOT NULL,
    [transaction_type] VARCHAR(255) NOT NULL,
    [amount]         NUMERIC(10, 2) NOT NULL,
    [transaction_date] DATETIME2(6) NOT NULL,
    [description]     VARCHAR(255) NULL,

    CONSTRAINT FK_Transactions_Budgets FOREIGN KEY ([budget_id]) REFERENCES [dbo].[Budgets]([budget_id]),
    CONSTRAINT FK_Transactions_Users FOREIGN KEY ([user_id]) REFERENCES [dbo].[Users]([user_id])
);
GO
```
### Replace connection strings with yours inside src/main/resources/application.properties

Build the projects
```
mvn clean install
```
Run the application
```
mvn spring-boot:run
```

## Features
- Budget tracking system 
- User authentication & roles 
- Expense & income management 
- Report generation 
- REST API with Spring Boot
