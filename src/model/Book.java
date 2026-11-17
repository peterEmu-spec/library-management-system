
package model;

public class Book {

    // 1. Private attributes
    private int bookId;
    private String isbn;
    private String title;
    private String author;
    private int categoryId;
    private String publisher;
    private int publicationYear;
    private int totalCopies;
    private int availableCopies;

    // 2. Default constructor
    public Book() {
    }

    // 3. Parameterized constructor
    public Book(int bookId, String isbn, String title, String author,
                int categoryId, String publisher, int publicationYear,
                int totalCopies, int availableCopies) {

        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.categoryId = categoryId;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    // 4. Getters and setters

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    // 5. Display book information
    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", categoryId=" + categoryId +
                ", publisher='" + publisher + '\'' +
                ", publicationYear=" + publicationYear +
                ", totalCopies=" + totalCopies +
                ", availableCopies=" + availableCopies +
                '}';
    }
}
