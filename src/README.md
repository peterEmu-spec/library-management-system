# Library Management System

## 1. Project Overview

The Library Management System is a Java-based desktop application developed to simplify library operations through computerisation.

The system is designed to help librarians manage books, library members, and borrowing and returning activities using a MySQL database.

It aims to reduce manual record-keeping, improve information retrieval, and make library operations more efficient.

## 2. Problem Statement

Traditional library management methods often rely on manual records to track books, members, and borrowing activities.

This can result in:
- Loss or misplacement of records.
- Difficulty tracking borrowed books.
- Slow retrieval of book information.
- Errors when updating book availability.
- Challenges managing library members.

This project provides a computerised solution to these challenges.

## 3. Technologies Used

| Technology | Purpose |
|---|---|
| Java | Main programming language |
| Java Swing | Graphical User Interface |
| MySQL | Database management |
| JDBC | Connecting Java to MySQL |
| VS Code | Development environment |
| Git | Version control |

## 4. System Features

### Book Management
- Add new books.
- View all available book records.
- Update book information.
- Delete book records.
- Track total and available copies.

### Member Management
- Register library members.
- Store member details.
- Track member status.

### Borrowing and Returning
- Issue books to registered members.
- Check member eligibility.
- Check book availability.
- Record borrowing dates and due dates.
- Update book quantities when books are borrowed or returned.

*Note: The book management GUI is currently implemented. Member and loan management interfaces are planned for subsequent development.*

## 5. Object-Oriented Programming Concepts

The project applies the following OOP principles:

- **Encapsulation:** Model attributes are declared private and accessed through getters and setters.
- **Classes and Objects:** Book, Member and Loan are represented as Java classes.
- **Constructors:** Constructors initialise objects with appropriate values.
- **Inheritance and Polymorphism:** To be incorporated where appropriate as the application develops.

## 6. Database Design

The project uses a MySQL database named:

`library_management`

It contains four main tables:

### Categories
Stores book categories.

- category_id (Primary Key)
- category_name

### Books
Stores book information.

- book_id (Primary Key)
- isbn
- title
- author
- category_id (Foreign Key)
- publisher
- publication_year
- total_copies
- available_copies

### Members
Stores registered library members.

- member_id (Primary Key)
- full_name
- email
- phone
- registration_date
- status

### Loans
Stores borrowing and returning transactions.

- loan_id (Primary Key)
- book_id (Foreign Key)
- member_id (Foreign Key)
- issue_date
- due_date
- return_date
- loan_status

## 7. Project Structure

```text
lib-management/
│
├── src/
│   ├── database/
│   │   └── DBConnection.java
│   │
│   ├── model/
│   │   ├── Book.java
│   │   ├── Member.java
│   │   └── Loan.java
│   │
│   ├── dao/
│   │   ├── BookDAO.java
│   │   ├── MemberDAO.java
│   │   └── LoanDAO.java
│   │
│   ├── gui/
│   │   └── LibraryGUI.java
│   │
│   └── Main.java
│
├── out/
│
└── README.md
```

## 8. Installation and Setup

### Requirements

Before running the application, ensure you have:

1. Java Development Kit (JDK 17 or later).
2. MySQL Server.
3. MySQL Connector/J.
4. Visual Studio Code or another Java IDE.

### Step 1: Clone the Repository

```bash
git clone <repository-url>
cd lib-management
```

### Step 2: Create the Database

Create the MySQL database:

```sql
CREATE DATABASE library_management;
```

Create the four database tables using the SQL definitions provided with the project.

### Step 3: Configure Database Connection

The application connects to MySQL through `DBConnection.java`.

Configure the database credentials using an environment variable:

```bash
export LIBRARY_DB_PASSWORD='your_database_password'
```

The application uses the following database account:

- Database: library_management
- Username: library_user
- Host: localhost
- Port: 3306

Do not commit database passwords or other sensitive credentials to GitHub.

### Step 4: Configure MySQL Connector/J

Ensure the MySQL Connector/J library is installed.

The current development environment uses:

```text
/usr/share/java/mysql-connector-j-26.10.0.jar
```

### Step 5: Compile the Application

From the project root directory, execute:

```bash
mkdir -p out
```

Then compile:

```bash
javac -cp ".:/usr/share/java/mysql-connector-j-26.10.0.jar" -d out $(find src -name "*.java")
```

### Step 6: Run the Application

```bash
java -cp "out:/usr/share/java/mysql-connector-j-26.10.0.jar" Main
```

The Library Management System graphical interface should launch.

## 9. Validation and Error Handling

The application incorporates:
- Input validation to prevent incomplete records.
- Numeric validation for fields such as category ID and book quantities.
- Prepared statements for database operations.
- Try-catch blocks for handling database errors.
- Confirmation dialogs before deleting books.
- JDBC transactions for borrowing and returning operations.

## 10. Future Improvements

Planned improvements include:

- Complete member management interface.
- Complete borrowing and returning interface.
- Category selection dropdown.
- Book search functionality.
- Overdue book tracking.
- Improved graphical interface and navigation.
- Better user feedback and reporting.

## 11. Project Purpose

This project is developed as part of an Object-Oriented Programming coursework project to demonstrate the practical application of Java, OOP principles, database connectivity, CRUD operations and graphical user interface development.

---

**Project:** Library Management System  
**Programming Language:** Java  
**Database:** MySQL  
**Interface:** Java Swing