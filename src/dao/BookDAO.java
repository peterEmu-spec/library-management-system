
package dao;

import database.DBConnection;
import model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    // 1. CREATE: Add a new book
    public boolean addBook(Book book) {

        String sql = "INSERT INTO books " +
                "(isbn, title, author, category_id, publisher, " +
                "publication_year, total_copies, available_copies) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setInt(4, book.getCategoryId());
            statement.setString(5, book.getPublisher());
            statement.setInt(6, book.getPublicationYear());
            statement.setInt(7, book.getTotalCopies());
            statement.setInt(8, book.getAvailableCopies());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding book: " + e.getMessage());
            return false;
        }
    }

    // 2. READ: Retrieve all books
    public List<Book> getAllBooks() {

        List<Book> books = new ArrayList<>();

        String sql = "SELECT * FROM books";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Book book = new Book(
                        result.getInt("book_id"),
                        result.getString("isbn"),
                        result.getString("title"),
                        result.getString("author"),
                        result.getInt("category_id"),
                        result.getString("publisher"),
                        result.getInt("publication_year"),
                        result.getInt("total_copies"),
                        result.getInt("available_copies")
                );

                books.add(book);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving books: " + e.getMessage());
        }

        return books;
    }

    // 3. UPDATE: Modify an existing book
    public boolean updateBook(Book book) {

        String sql = "UPDATE books SET isbn = ?, title = ?, " +
                "author = ?, category_id = ?, publisher = ?, " +
                "publication_year = ?, total_copies = ?, " +
                "available_copies = ? WHERE book_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setInt(4, book.getCategoryId());
            statement.setString(5, book.getPublisher());
            statement.setInt(6, book.getPublicationYear());
            statement.setInt(7, book.getTotalCopies());
            statement.setInt(8, book.getAvailableCopies());
            statement.setInt(9, book.getBookId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating book: " + e.getMessage());
            return false;
        }
    }

    // 4. DELETE: Remove a book
    public boolean deleteBook(int bookId) {

        String sql = "DELETE FROM books WHERE book_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bookId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting book: " + e.getMessage());
            return false;
        }
    }
}
