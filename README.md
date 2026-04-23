# Loan Eligibility & EMI Calculator System

## Overview

This project implements a structured, console-based loan evaluation system designed to simulate real-world banking decision logic.

It combines eligibility assessment, EMI computation, and basic data persistence into a single modular application. The system evaluates applicants based on financial and demographic constraints, while also providing detailed repayment insights.

---

## Core Features

- Loan eligibility assessment using:
  - Credit score thresholds
  - Income validation
  - Debt-to-income ratio
  - Employment stability
  - Age constraints

- EMI calculation with:
  - Accurate amortization formula
  - Total payment and interest breakdown
  - Interest percentage analysis

- Loan plan comparison:
  - Multiple tenure options
  - EMI vs total interest trade-off

- Data persistence:
  - User data stored in CSV format
  - Structured for Excel compatibility
  - Sequential record tracking (S.No)

- File handling:
  - Save user records
  - Read and display saved data

---

## System Design

The application follows a modular structure:

- `LoanCalculatorSystem.java` → Main controller & menu handling  
- `User.java` → Data model & file operations  

This separation enables cleaner logic management and improves maintainability.

---

## Technical Implementation

- Language: Java  
- Paradigm: Object-Oriented Programming (OOP)  
- Data Storage: CSV (file-based persistence)  
- Interface: Console-based interactive system  

---

## Functional Highlights

- Realistic eligibility constraints inspired by banking systems  
- EMI computation using standard financial formula  
- Structured output with detailed breakdowns  
- Persistent storage enabling multi-run data retention  

---

## How to Run

1. Compile the program:
   javac LoanCalculatorSystem.java User.java

2. Run:
   java LoanCalculatorSystem


3. Use the menu to:
- Check eligibility  
- Calculate EMI  
- Compare loan plans  
- View saved users  

---

## Output

- Console-based structured output  
- CSV file (`users.csv`) storing user records  
- Compatible with spreadsheet tools like Excel  

---

## Future Enhancements

- Database integration (MySQL / PostgreSQL)  
- GUI or web-based interface  
- Advanced risk scoring models  
- API-based interest rate integration  

---

## Note

This project is developed as part of early-stage system design and financial logic exploration, focusing on structured programming, data handling, and modular architecture.

---

## Author

**Mahdiya Rifqua M**  
B.Tech Computer Science & Medical Engineering  
(Specialization: Artificial Intelligence and Data Analysis)
