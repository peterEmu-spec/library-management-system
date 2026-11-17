
package model;

import java.time.LocalDate;

public class Loan {

    // 1. Private attributes
    private int loanId;
    private int bookId;
    private int memberId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String loanStatus;

    // 2. Default constructor
    public Loan() {
    }

    // 3. Parameterized constructor
    public Loan(int loanId, int bookId, int memberId,
                LocalDate issueDate, LocalDate dueDate,
                LocalDate returnDate, String loanStatus) {

        this.loanId = loanId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.loanStatus = loanStatus;
    }

    // 4. Getters and setters

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    // 5. Display loan information
    @Override
    public String toString() {
        return "Loan{" +
                "loanId=" + loanId +
                ", bookId=" + bookId +
                ", memberId=" + memberId +
                ", issueDate=" + issueDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", loanStatus='" + loanStatus + '\'' +
                '}';
    }
}
