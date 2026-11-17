
package dao;

import database.DBConnection;
import model.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    // 1. CREATE: Register a new member
    public boolean addMember(Member member) {

        String sql = "INSERT INTO members " +
                "(full_name, email, phone, registration_date, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, member.getFullName());
            statement.setString(2, member.getEmail());
            statement.setString(3, member.getPhone());
            statement.setDate(4,
                    java.sql.Date.valueOf(member.getRegistrationDate()));
            statement.setString(5, member.getStatus());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error registering member: " + e.getMessage());
            return false;
        }
    }

    // 2. READ: Retrieve all members
    public List<Member> getAllMembers() {

        List<Member> members = new ArrayList<>();

        String sql = "SELECT * FROM members";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Member member = new Member(
                        result.getInt("member_id"),
                        result.getString("full_name"),
                        result.getString("email"),
                        result.getString("phone"),
                        result.getDate("registration_date").toLocalDate(),
                        result.getString("status")
                );

                members.add(member);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving members: " + e.getMessage());
        }

        return members;
    }

    // 3. UPDATE: Modify member information
    public boolean updateMember(Member member) {

        String sql = "UPDATE members SET full_name = ?, email = ?, " +
                "phone = ?, registration_date = ?, status = ? " +
                "WHERE member_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, member.getFullName());
            statement.setString(2, member.getEmail());
            statement.setString(3, member.getPhone());
            statement.setDate(4,
                    java.sql.Date.valueOf(member.getRegistrationDate()));
            statement.setString(5, member.getStatus());
            statement.setInt(6, member.getMemberId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating member: " + e.getMessage());
            return false;
        }
    }

    // 4. DELETE: Remove a member
    public boolean deleteMember(int memberId) {

        String sql = "DELETE FROM members WHERE member_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, memberId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting member: " + e.getMessage());
            return false;
        }
    }
}
