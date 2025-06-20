package dao;

import domain.Member;

import java.sql.*;

public class MemberDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/java-projectDB";
    private static final String USER = "root";
    private static final String PW = "fpdlswj365";

    public boolean createMember(Member member) {
        String sql = "INSERT INTO member (id, password, age, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PW);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, member.getId());
            ps.setString(2, member.getPassword());
            ps.setInt(3, member.getAge());
            ps.setString(4, member.getRole());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("회원가입 실패: " + e.getMessage());
            return false;
        }
    }

    public Member loginMember(String id, String password) {
        String sql = "SELECT * FROM member WHERE id=? AND password=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PW);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Member(
                        rs.getString("id"),
                        rs.getString("password"),
                        rs.getInt("age"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.out.println("로그인 오류: " + e.getMessage());
        }
        return null;
    }
}
