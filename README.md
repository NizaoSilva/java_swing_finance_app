# Finances - An application that manages banking operations

## Overview
This is a desktop application built with **Java Swing** for managing finances.  
It connects to a **MySQL** database (via **XAMPP** or another MySQL server).  
All main operations — deposits, withdrawals, transfers, and loans — are handled directly through the graphical interface.

---

## Requirements
- **Java JDK 8+**
- **MySQL Server** (recommended: XAMPP)
- **MySQL Connector/J** file: `mysql-connector-j-9.4.0.jar`, Place the `.jar` file inside the `lib/` folder and make sure it’s included in your project’s classpath.).
Official download page: [https://dev.mysql.com/downloads/connector/j/](https://dev.mysql.com/downloads/connector/j/), choose Plataform Idependent
- (Optional) **NetBeans IDE** to run or edit the source code
---

## Install
- **Download and extract** the project into the folder C:\Users\User\Documents\NetBeansProjects\
- **Add connector library:**
![Connector](images/00_connector.png)
- **Make sure you have the mysql server online**
![Xampp](images/01_xampp.png)
- **Make sure the mysql server have the finances database**
![Database](images/02_database.png)
- **Make sure the DataBase class have the correct address to conect in factory method**
---

## Project Structure
All classes and relationships [Diagram ⛓](https://lucid.app/lucidchart/d719d178-a64a-4bc4-8f8f-a31e21a69fe4/edit?invitationId=inv_9bad4673-da67-4556-bb84-ad3c5793fb6b&page=0_0#).
### Classes
- **Address**(zip_code, ...)
- **Contact**(name, ..., Address)
- **Client**(username, password, ..., Contact)
- **Admin**(...) extend Client
- Abstract **Capital**(balance, ..., Client)
- **Current**(...) extend Capital
- **SafeZone**(super(), Double interest_rate) extend Current
- **Loan**(..., Double initial_fee) extend SafeZone
- Abstract **Operation**(value, datetime, ..., Capital)
- **Deposit**(...) extend Operation
- **Withdraw**(...) extend Operation
- **Transfer**(..., Capital destination) extend Operation
### Forms
All form screenshots are available in the [Images Folder 🖼️](images/).
