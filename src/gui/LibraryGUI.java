
package gui;

import dao.BookDAO;
import dao.MemberDAO;
import dao.LoanDAO;

import model.Book;
import model.Member;
import model.Loan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class LibraryGUI extends JFrame {

    // DATABASE OPERATIONS
    private BookDAO bookDAO = new BookDAO();
    private MemberDAO memberDAO = new MemberDAO();
    private LoanDAO loanDAO = new LoanDAO();

    // BOOK INPUT FIELDS
    private JTextField txtIsbn;
    private JTextField txtTitle;
    private JTextField txtAuthor;
    private JTextField txtCategoryId;
    private JTextField txtPublisher;
    private JTextField txtYear;
    private JTextField txtTotalCopies;
    private JTextField txtAvailableCopies;

    private JTable bookTable;
    private DefaultTableModel tableModel;
    private int selectedBookId = -1;

    // MEMBER INPUT FIELDS
    private JTextField txtFullName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtRegistrationDate;
    private JComboBox<String> cmbMemberStatus;

    private JTable memberTable;
    private DefaultTableModel memberTableModel;
    private int selectedMemberId = -1;

    // LOAN INPUT FIELDS
    private JComboBox<String> cmbLoanBooks;
    private JComboBox<String> cmbLoanMembers;
    private JTextField txtDueDate;

    private JTable loanTable;
    private DefaultTableModel loanTableModel;
    private int selectedLoanId = -1;

    // CONSTRUCTOR
    public LibraryGUI() {

        setTitle("Library Management System");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Books", createBookPanel());
        tabs.addTab("Members", createMemberPanel());
        tabs.addTab("Loans", createLoanPanel());

        add(tabs);

        loadBooks();
    }

    // ==========================================
    // BOOK MANAGEMENT
    // ==========================================

    private JPanel createBookPanel() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JLabel heading = new JLabel("BOOK MANAGEMENT");
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        mainPanel.add(heading, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 8, 8));

        txtIsbn = new JTextField();
        txtTitle = new JTextField();
        txtAuthor = new JTextField();
        txtCategoryId = new JTextField();
        txtPublisher = new JTextField();
        txtYear = new JTextField();
        txtTotalCopies = new JTextField();
        txtAvailableCopies = new JTextField();

        formPanel.add(new JLabel("ISBN:"));
        formPanel.add(txtIsbn);

        formPanel.add(new JLabel("Title:"));
        formPanel.add(txtTitle);

        formPanel.add(new JLabel("Author:"));
        formPanel.add(txtAuthor);

        formPanel.add(new JLabel("Category ID:"));
        formPanel.add(txtCategoryId);

        formPanel.add(new JLabel("Publisher:"));
        formPanel.add(txtPublisher);

        formPanel.add(new JLabel("Publication Year:"));
        formPanel.add(txtYear);

        formPanel.add(new JLabel("Total Copies:"));
        formPanel.add(txtTotalCopies);

        formPanel.add(new JLabel("Available Copies:"));
        formPanel.add(txtAvailableCopies);

        JButton btnAdd = new JButton("Add Book");
        JButton btnUpdate = new JButton("Update Book");
        JButton btnDelete = new JButton("Delete Book");
        JButton btnClear = new JButton("Clear Fields");
        JButton btnRefresh = new JButton("Refresh");

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnRefresh);

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.CENTER);

        String[] columns = {
            "ID", "ISBN", "Title", "Author",
            "Category ID", "Publisher", "Year",
            "Total Copies", "Available Copies"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookTable = new JTable(tableModel);

        mainPanel.add(new JScrollPane(bookTable), BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addBook());
        btnUpdate.addActionListener(e -> updateBook());
        btnDelete.addActionListener(e -> deleteBook());
        btnClear.addActionListener(e -> clearFields());
        btnRefresh.addActionListener(e -> loadBooks());

        bookTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectBook();
            }
        });

        return mainPanel;
    }

    private void addBook() {

        if (!validateInputs()) {
            return;
        }

        try {
            Book book = getBookFromFields();

            boolean success = bookDAO.addBook(book);

            if (success) {
                JOptionPane.showMessageDialog(this, "Book added successfully!");
                clearFields();
                loadBooks();
                loadLoanOptions();
            } else {
                JOptionPane.showMessageDialog(this, "Could not add book. Check the details.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateBook() {

        if (selectedBookId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book first.");
            return;
        }

        if (!validateInputs()) {
            return;
        }

        try {
            Book book = getBookFromFields();
            book.setBookId(selectedBookId);

            boolean success = bookDAO.updateBook(book);

            if (success) {
                JOptionPane.showMessageDialog(this, "Book updated successfully!");
                clearFields();
                loadBooks();
                loadLoanOptions();
            } else {
                JOptionPane.showMessageDialog(this, "Could not update book.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteBook() {

        if (selectedBookId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this book?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            boolean success = bookDAO.deleteBook(selectedBookId);

            if (success) {
                JOptionPane.showMessageDialog(this, "Book deleted successfully!");
                clearFields();
                loadBooks();
                loadLoanOptions();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Could not delete book. It may have loan records."
                );
            }
        }
    }

    private void loadBooks() {

        tableModel.setRowCount(0);

        List<Book> books = bookDAO.getAllBooks();

        for (Book book : books) {

            Object[] row = {
                book.getBookId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategoryId(),
                book.getPublisher(),
                book.getPublicationYear(),
                book.getTotalCopies(),
                book.getAvailableCopies()
            };

            tableModel.addRow(row);
        }
    }

    private void selectBook() {

        int row = bookTable.getSelectedRow();

        if (row >= 0) {

            selectedBookId = Integer.parseInt(
                tableModel.getValueAt(row, 0).toString()
            );

            txtIsbn.setText(tableModel.getValueAt(row, 1).toString());
            txtTitle.setText(tableModel.getValueAt(row, 2).toString());
            txtAuthor.setText(tableModel.getValueAt(row, 3).toString());
            txtCategoryId.setText(tableModel.getValueAt(row, 4).toString());

            txtPublisher.setText(
                tableModel.getValueAt(row, 5) == null
                    ? ""
                    : tableModel.getValueAt(row, 5).toString()
            );

            txtYear.setText(tableModel.getValueAt(row, 6).toString());
            txtTotalCopies.setText(tableModel.getValueAt(row, 7).toString());
            txtAvailableCopies.setText(tableModel.getValueAt(row, 8).toString());
        }
    }

    private boolean validateInputs() {

        if (txtIsbn.getText().trim().isEmpty()
                || txtTitle.getText().trim().isEmpty()
                || txtAuthor.getText().trim().isEmpty()
                || txtCategoryId.getText().trim().isEmpty()
                || txtYear.getText().trim().isEmpty()
                || txtTotalCopies.getText().trim().isEmpty()
                || txtAvailableCopies.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
            return false;
        }

        try {

            int categoryId = Integer.parseInt(txtCategoryId.getText().trim());
            int year = Integer.parseInt(txtYear.getText().trim());
            int total = Integer.parseInt(txtTotalCopies.getText().trim());
            int available = Integer.parseInt(txtAvailableCopies.getText().trim());

            if (categoryId <= 0 || year <= 0 || total < 0 || available < 0) {
                JOptionPane.showMessageDialog(this, "Enter valid positive IDs and year, and non-negative copy quantities.");
                return false;
            }

            if (available > total) {
                JOptionPane.showMessageDialog(this, "Available copies cannot exceed total copies.");
                return false;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Numeric fields must contain whole numbers.");
            return false;
        }

        return true;
    }

    private Book getBookFromFields() {

        return new Book(
            0,
            txtIsbn.getText().trim(),
            txtTitle.getText().trim(),
            txtAuthor.getText().trim(),
            Integer.parseInt(txtCategoryId.getText().trim()),
            txtPublisher.getText().trim(),
            Integer.parseInt(txtYear.getText().trim()),
            Integer.parseInt(txtTotalCopies.getText().trim()),
            Integer.parseInt(txtAvailableCopies.getText().trim())
        );
    }

    private void clearFields() {

        txtIsbn.setText("");
        txtTitle.setText("");
        txtAuthor.setText("");
        txtCategoryId.setText("");
        txtPublisher.setText("");
        txtYear.setText("");
        txtTotalCopies.setText("");
        txtAvailableCopies.setText("");

        selectedBookId = -1;
        bookTable.clearSelection();
    }

    // ==========================================
    // MEMBER MANAGEMENT
    // ==========================================

    private JPanel createMemberPanel() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JLabel heading = new JLabel("MEMBER MANAGEMENT");
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        mainPanel.add(heading, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 8, 8));

        txtFullName = new JTextField();
        txtEmail = new JTextField();
        txtPhone = new JTextField();
        txtRegistrationDate = new JTextField(LocalDate.now().toString());

        cmbMemberStatus = new JComboBox<>(
            new String[]{"ACTIVE", "INACTIVE"}
        );

        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(txtFullName);

        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);

        formPanel.add(new JLabel("Phone:"));
        formPanel.add(txtPhone);

        formPanel.add(new JLabel("Registration Date (YYYY-MM-DD):"));
        formPanel.add(txtRegistrationDate);

        formPanel.add(new JLabel("Member Status:"));
        formPanel.add(cmbMemberStatus);

        JButton btnAdd = new JButton("Register Member");
        JButton btnUpdate = new JButton("Update Member");
        JButton btnDelete = new JButton("Delete Member");
        JButton btnClear = new JButton("Clear Fields");
        JButton btnRefresh = new JButton("Refresh");

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnRefresh);

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.CENTER);

        String[] columns = {
            "ID", "Full Name", "Email",
            "Phone", "Registration Date", "Status"
        };

        memberTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        memberTable = new JTable(memberTableModel);

        mainPanel.add(new JScrollPane(memberTable), BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addMember());
        btnUpdate.addActionListener(e -> updateMember());
        btnDelete.addActionListener(e -> deleteMember());
        btnClear.addActionListener(e -> clearMemberFields());
        btnRefresh.addActionListener(e -> loadMembers());

        memberTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectMember();
            }
        });

        loadMembers();

        return mainPanel;
    }

    private void addMember() {

        if (!validateMemberInputs()) {
            return;
        }

        try {

            Member member = getMemberFromFields();

            boolean success = memberDAO.addMember(member);

            if (success) {
                JOptionPane.showMessageDialog(this, "Member registered successfully!");
                clearMemberFields();
                loadMembers();
                loadLoanOptions();
            } else {
                JOptionPane.showMessageDialog(this, "Could not register member. Check the details.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void loadMembers() {

        memberTableModel.setRowCount(0);

        List<Member> members = memberDAO.getAllMembers();

        for (Member member : members) {

            Object[] row = {
                member.getMemberId(),
                member.getFullName(),
                member.getEmail(),
                member.getPhone(),
                member.getRegistrationDate(),
                member.getStatus()
            };

            memberTableModel.addRow(row);
        }
    }

    private void selectMember() {

        int row = memberTable.getSelectedRow();

        if (row >= 0) {

            selectedMemberId = Integer.parseInt(
                memberTableModel.getValueAt(row, 0).toString()
            );

            txtFullName.setText(memberTableModel.getValueAt(row, 1).toString());
            txtEmail.setText(memberTableModel.getValueAt(row, 2).toString());
            txtPhone.setText(memberTableModel.getValueAt(row, 3).toString());
            txtRegistrationDate.setText(memberTableModel.getValueAt(row, 4).toString());

            cmbMemberStatus.setSelectedItem(
                memberTableModel.getValueAt(row, 5).toString()
            );
        }
    }

    private void updateMember() {

        if (selectedMemberId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member first.");
            return;
        }

        if (!validateMemberInputs()) {
            return;
        }

        try {

            Member member = getMemberFromFields();
            member.setMemberId(selectedMemberId);

            boolean success = memberDAO.updateMember(member);

            if (success) {
                JOptionPane.showMessageDialog(this, "Member updated successfully!");
                clearMemberFields();
                loadMembers();
                loadLoanOptions();
            } else {
                JOptionPane.showMessageDialog(this, "Could not update member.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteMember() {

        if (selectedMemberId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member to delete.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this member?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            boolean success = memberDAO.deleteMember(selectedMemberId);

            if (success) {
                JOptionPane.showMessageDialog(this, "Member deleted successfully!");
                clearMemberFields();
                loadMembers();
                loadLoanOptions();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Could not delete member. They may have loan records."
                );
            }
        }
    }

    private boolean validateMemberInputs() {

        if (txtFullName.getText().trim().isEmpty()
                || txtEmail.getText().trim().isEmpty()
                || txtPhone.getText().trim().isEmpty()
                || txtRegistrationDate.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
            return false;
        }

        if (!txtEmail.getText().trim().matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
            return false;
        }

        try {
            LocalDate.parse(txtRegistrationDate.getText().trim());

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Enter the date in YYYY-MM-DD format.");
            return false;
        }

        return true;
    }

    private Member getMemberFromFields() {

        return new Member(
            0,
            txtFullName.getText().trim(),
            txtEmail.getText().trim(),
            txtPhone.getText().trim(),
            LocalDate.parse(txtRegistrationDate.getText().trim()),
            cmbMemberStatus.getSelectedItem().toString()
        );
    }

    private void clearMemberFields() {

        txtFullName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtRegistrationDate.setText(LocalDate.now().toString());

        cmbMemberStatus.setSelectedItem("ACTIVE");

        selectedMemberId = -1;
        memberTable.clearSelection();
    }

    // ==========================================
    // LOAN MANAGEMENT
    // ==========================================

    private JPanel createLoanPanel() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JLabel heading = new JLabel("BOOK BORROWING AND RETURNS");
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        mainPanel.add(heading, BorderLayout.NORTH);

        // LOAN FORM
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 8, 8));

        cmbLoanBooks = new JComboBox<>();
        cmbLoanMembers = new JComboBox<>();
        txtDueDate = new JTextField(LocalDate.now().plusDays(14).toString());

        formPanel.add(new JLabel("Select Book:"));
        formPanel.add(cmbLoanBooks);

        formPanel.add(new JLabel("Select Member:"));
        formPanel.add(cmbLoanMembers);

        formPanel.add(new JLabel("Due Date (YYYY-MM-DD):"));
        formPanel.add(txtDueDate);

        // BUTTONS
        JButton btnIssue = new JButton("Issue Book");
        JButton btnReturn = new JButton("Return Selected Book");
        JButton btnRefresh = new JButton("Refresh");

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(btnIssue);
        buttonPanel.add(btnReturn);
        buttonPanel.add(btnRefresh);

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));

        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.CENTER);

        // LOAN TABLE
        String[] columns = {
            "Loan ID",
            "Book ID",
            "Member ID",
            "Issue Date",
            "Due Date",
            "Return Date",
            "Status"
        };

        loanTableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        loanTable = new JTable(loanTableModel);

        mainPanel.add(new JScrollPane(loanTable), BorderLayout.SOUTH);

        // BUTTON ACTIONS
        btnIssue.addActionListener(e -> issueBook());
        btnReturn.addActionListener(e -> returnBook());
        btnRefresh.addActionListener(e -> refreshLoans());

        // SELECT LOAN FROM TABLE
        loanTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int row = loanTable.getSelectedRow();

                if (row >= 0) {
                    selectedLoanId = Integer.parseInt(
                        loanTableModel.getValueAt(row, 0).toString()
                    );
                }
            }
        });

        loadLoanOptions();
        loadLoans();

        return mainPanel;
    }

    // LOAD BOOKS AND ACTIVE MEMBERS INTO DROPDOWNS
    private void loadLoanOptions() {

        cmbLoanBooks.removeAllItems();
        cmbLoanMembers.removeAllItems();

        // Load books that have available copies
        List<Book> books = bookDAO.getAllBooks();

        for (Book book : books) {

            if (book.getAvailableCopies() > 0) {

                cmbLoanBooks.addItem(
                    book.getBookId() + " - " + book.getTitle()
                );
            }
        }

        // Load active members only
        List<Member> members = memberDAO.getAllMembers();

        for (Member member : members) {

            if ("ACTIVE".equalsIgnoreCase(member.getStatus())) {

                cmbLoanMembers.addItem(
                    member.getMemberId() + " - " + member.getFullName()
                );
            }
        }
    }

    // ISSUE A BOOK
    private void issueBook() {

        if (cmbLoanBooks.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "No available books to issue.");
            return;
        }

        if (cmbLoanMembers.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "No active members available.");
            return;
        }

        LocalDate dueDate;

        try {

            dueDate = LocalDate.parse(txtDueDate.getText().trim());

            if (dueDate.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Due date cannot be in the past.");
                return;
            }

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                this,
                "Enter the due date in YYYY-MM-DD format."
            );

            return;
        }

        try {

            // Extract book ID from dropdown
            String selectedBook = cmbLoanBooks.getSelectedItem().toString();

            int bookId = Integer.parseInt(
                selectedBook.split(" - ")[0]
            );

            // Extract member ID from dropdown
            String selectedMember = cmbLoanMembers.getSelectedItem().toString();

            int memberId = Integer.parseInt(
                selectedMember.split(" - ")[0]
            );

            // Call LoanDAO to issue the book
            boolean success = loanDAO.issueBook(
                bookId,
                memberId,
                dueDate
            );

            if (success) {

                JOptionPane.showMessageDialog(
                    this,
                    "Book issued successfully!"
                );

                refreshLoans();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Could not issue the book. Check book availability and member status."
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Error issuing book: " + e.getMessage()
            );
        }
    }

    // LOAD LOANS INTO TABLE
    private void loadLoans() {

        loanTableModel.setRowCount(0);

        List<Loan> loans = loanDAO.getAllLoans();

        for (Loan loan : loans) {

            Object[] row = {
                loan.getLoanId(),
                loan.getBookId(),
                loan.getMemberId(),
                loan.getIssueDate(),
                loan.getDueDate(),
                loan.getReturnDate(),
                loan.getLoanStatus()
            };

            loanTableModel.addRow(row);
        }
    }

    // RETURN A SELECTED BOOK
    private void returnBook() {

        if (selectedLoanId == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Please select a loan record from the table first."
            );

            return;
        }

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to return this book?",
            "Confirm Book Return",
            JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            boolean success = loanDAO.returnBook(selectedLoanId);

            if (success) {

                JOptionPane.showMessageDialog(
                    this,
                    "Book returned successfully!"
                );

                refreshLoans();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Could not return book. It may already have been returned."
                );
            }
        }
    }

    // REFRESH LOANS, BOOKS AND MEMBERS
    private void refreshLoans() {

        selectedLoanId = -1;

        loadLoans();
        loadLoanOptions();
        loadBooks();
    }
}
