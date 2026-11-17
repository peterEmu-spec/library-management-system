
package dao;

import database.DBConnection;
import model.Loan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    // 1. ISSUE BOOK: Borrow a book
    public boolean issueBook(int bookId, int memberId, LocalDate dueDate) {

        String memberSql =
                "SELECT status FROM members WHERE member_id = ? FOR UPDATE";

        String bookSql =
                "SELECT available_copies FROM books WHERE book_id = ? FOR UPDATE";

        String loanSql =
                "INSERT INTO loans (book_id, member_id, issue_date, due_date, loan_status) " +
                "VALUES (?, ?, ?, ?, 'BORROWED')";

        String updateBookSql =
                "UPDATE books SET available_copies = available_copies - 1 " +
                "WHERE book_id = ? AND available_copies > 0";

        try (Connection connection = DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                // Check whether the member exists and is active
                try (PreparedStatement memberStatement =
                             connection.prepareStatement(memberSql)) {

                    memberStatement.setInt(1, memberId);

                    try (ResultSet memberResult = memberStatement.executeQuery()) {

                        if (!memberResult.next()) {
                            throw new SQLException("Member does not exist.");
                        }

                        if (!"ACTIVE".equals(memberResult.getString("status"))) {
                            throw new SQLException("Member is not active.");
                        }
                    }
                }

                // Check whether the book exists and has available copies
                try (PreparedStatement bookStatement =
                             connection.prepareStatement(bookSql)) {

                    bookStatement.setInt(1, bookId);

                    try (ResultSet bookResult = bookStatement.executeQuery()) {

                        if (!bookResult.next()) {
                            throw new SQLException("Book does not exist.");
                        }

                        if (bookResult.getInt("available_copies") <= 0) {
                            throw new SQLException("No copies of this book are available.");
                        }
                    }
                }

                // Validate the due date
                LocalDate issueDate = LocalDate.now();

                if (dueDate == null || dueDate.isBefore(issueDate)) {
                    throw new SQLException("Due date cannot be before the issue date.");
                }

                // Create the loan record
                try (PreparedStatement loanStatement =
                             connection.prepareStatement(loanSql)) {

                    loanStatement.setInt(1, bookId);
                    loanStatement.setInt(2, memberId);
                    loanStatement.setDate(3, Date.valueOf(issueDate));
                    loanStatement.setDate(4, Date.valueOf(dueDate));

                    loanStatement.executeUpdate();
                }

                // Reduce the available book quantity
                try (PreparedStatement updateStatement =
                             connection.prepareStatement(updateBookSql)) {

                    updateStatement.setInt(1, bookId);

                    int rowsUpdated = updateStatement.executeUpdate();

                    if (rowsUpdated == 0) {
                        throw new SQLException("Could not update book quantity.");
                    }
                }

                // Save all changes
                connection.commit();

                return true;

            } catch (SQLException e) {

                // Undo all changes if something fails
                connection.rollback();

                System.out.println("Error issuing book: " + e.getMessage());

                return false;

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {

            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }

    // 2. RETURN BOOK
    public boolean returnBook(int loanId) {

        String loanSql =
                "SELECT book_id, loan_status FROM loans WHERE loan_id = ? FOR UPDATE";

        String updateLoanSql =
                "UPDATE loans SET return_date = ?, loan_status = 'RETURNED' " +
                "WHERE loan_id = ? AND loan_status = 'BORROWED'";

        String updateBookSql =
                "UPDATE books SET available_copies = available_copies + 1 " +
                "WHERE book_id = ? AND available_copies < total_copies";

        try (Connection connection = DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                int bookId;

                // Find the loan and check its status
                try (PreparedStatement loanStatement =
                             connection.prepareStatement(loanSql)) {

                    loanStatement.setInt(1, loanId);

                    try (ResultSet loanResult = loanStatement.executeQuery()) {

                        if (!loanResult.next()) {
                            throw new SQLException("Loan record does not exist.");
                        }

                        if (!"BORROWED".equals(loanResult.getString("loan_status"))) {
                            throw new SQLException("This book has already been returned.");
                        }

                        bookId = loanResult.getInt("book_id");
                    }
                }

                // Update the loan record
                try (PreparedStatement updateLoanStatement =
                             connection.prepareStatement(updateLoanSql)) {

                    updateLoanStatement.setDate(1, Date.valueOf(LocalDate.now()));
                    updateLoanStatement.setInt(2, loanId);

                    int rowsUpdated = updateLoanStatement.executeUpdate();

                    if (rowsUpdated == 0) {
                        throw new SQLException("Could not update loan record.");
                    }
                }

                // Increase available book quantity
                try (PreparedStatement updateBookStatement =
                             connection.prepareStatement(updateBookSql)) {

                    updateBookStatement.setInt(1, bookId);

                    int rowsUpdated = updateBookStatement.executeUpdate();

                    if (rowsUpdated == 0) {
                        throw new SQLException("Could not update book quantity.");
                    }
                }

                connection.commit();

                return true;

            } catch (SQLException e) {

                connection.rollback();

                System.out.println("Error returning book: " + e.getMessage());

                return false;

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {

            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }

    // 3. READ: Retrieve all loan records
    public List<Loan> getAllLoans() {

        List<Loan> loans = new ArrayList<>();

        String sql = "SELECT * FROM loans";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Date returnDate = result.getDate("return_date");

                Loan loan = new Loan(
                        result.getInt("loan_id"),
                        result.getInt("book_id"),
                        result.getInt("member_id"),
                        result.getDate("issue_date").toLocalDate(),
                        result.getDate("due_date").toLocalDate(),
                        returnDate == null ? null : returnDate.toLocalDate(),
                        result.getString("loan_status")
                );

                loans.add(loan);
            }

        } catch (SQLException e) {

            System.out.println("Error retrieving loans: " + e.getMessage());
        }

        return loans;
    }
}
