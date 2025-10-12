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
![Connector](images/0_connector.png)
- **Make sure you have the mysql server online**
![Xampp](images/1_xampp.png)
- **Make sure the mysql server have the finances database**
![Database](images/2_database.png)
- **Make sure the DataBase class have the correct address to conect in factory method**
---

## Project Structure
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
![Credentials](images/3_credentials.png)
![Newuser 1](images/4_newuser1.png)
![Newuser 2](images/5_newuser2.png)
![Newuser 3](images/6_newuser3.png)
![Newuser 4](images/7_newuser4.png)
![Homepage Admin](images/8_homepageAdmin.png)
![Homepage Client](images/9_homepageClient.png)
![Settings](images/10_settings.png)
![Deposit Operation](images/11_deposit.png)
![Withdraw Operation](images/12_withdraw.png)
![Transfer Operation](images/13_transfer.png)
![Loan Operation](images/14_loan.png)
![Current Chart](images/15_current_chart.png)
![Loan Chart](images/16_loan_chart.png)
