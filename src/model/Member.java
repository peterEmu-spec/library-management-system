
package model;

import java.time.LocalDate;

public class Member {

    // 1. Private attributes
    private int memberId;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate registrationDate;
    private String status;

    // 2. Default constructor
    public Member() {
    }

    // 3. Parameterized constructor
    public Member(int memberId, String fullName, String email,
                  String phone, LocalDate registrationDate,
                  String status) {

        this.memberId = memberId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.registrationDate = registrationDate;
        this.status = status;
    }

    // 4. Getters and setters

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // 5. Display member information
    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", registrationDate=" + registrationDate +
                ", status='" + status + '\'' +
                '}';
    }
}
