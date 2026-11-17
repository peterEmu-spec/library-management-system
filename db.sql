CREATE database library_management;
use library_management;
CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE
);
CREATE TABLE books (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    title VARCHAR(150) NOT NULL,
    author VARCHAR(100) NOT NULL,
    category_id INT NOT NULL,
    publisher VARCHAR(100),
    publication_year INT,
    total_copies INT NOT NULL DEFAULT 1,
    available_copies INT NOT NULL DEFAULT 1,

    FOREIGN KEY (category_id)
    REFERENCES categories(category_id),

    CHECK (total_copies >= 0),
    CHECK (available_copies >= 0),
    CHECK (available_copies <= total_copies)
);
CREATE TABLE members (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    registration_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',

    CHECK (status IN ('ACTIVE', 'INACTIVE'))
);
CREATE TABLE loans (
    loan_id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT NOT NULL,
    member_id INT NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    loan_status VARCHAR(20) DEFAULT 'BORROWED',

    FOREIGN KEY (book_id)
    REFERENCES books(book_id),

    FOREIGN KEY (member_id)
    REFERENCES members(member_id),

    CHECK (due_date >= issue_date),
    CHECK (return_date IS NULL OR return_date >= issue_date),
    CHECK (loan_status IN ('BORROWED', 'RETURNED'))
);
show TABLES;
INSERT INTO categories (category_name)
VALUES
('Computer Science'),
('Mathematics'),
('Networking'),
('Business'),
('Cybersecurity');

INSERT INTO categories (category_name)
VALUES ('fiction');

INSERT INTO books
(isbn, title, author, category_id, publisher,
 publication_year, total_copies, available_copies)
VALUES
('9780131103627', 'The C Programming Language',
 'Brian Kernighan', 1, 'Prentice Hall', 1988, 5, 5),

('9780132350884', 'Clean Code',
 'Robert Martin', 1, 'Prentice Hall', 2008, 4, 4),

('9780133594140', 'Computer Networking',
 'James Kurose', 3, 'Pearson', 2016, 6, 6),

('9780134685991', 'Effective Java',
 'Joshua Bloch', 1, 'Addison-Wesley', 2018, 3, 3);

INSERT INTO members
(full_name, email, phone, registration_date, status)
VALUES
('Peter Kimani', 'peter@example.com', '0712345678', CURDATE(), 'ACTIVE'),
('John Mwangi', 'john@example.com', '0723456789', CURDATE(), 'ACTIVE'),
('Mary Wanjiku', 'mary@example.com', '0734567890', CURDATE(), 'ACTIVE');
SELECT * FROM categories;

SELECT * FROM books;

SELECT * FROM members;

SELECT * FROM loans;
SELECT member_id, full_name, status
FROM members;
SELECT book_id, title, available_copies
FROM books;

START TRANSACTION;
INSERT INTO loans
(book_id, member_id, issue_date, due_date, loan_status)
VALUES
(2, 1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY), 'BORROWED');

UPDATE books
SET available_copies = available_copies - 1
WHERE book_id = 2
AND available_copies > 0;

SELECT * FROM loans;
SELECT book_id, title, available_copies
FROM books
WHERE book_id = 2;
ROLLBACK;