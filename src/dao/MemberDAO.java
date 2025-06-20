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
                        rs.getInt("member_id"),
                        rs.getString("id"),
                        rs.getString("password"),
                        rs.getInt("age"),
                        rs.getInt("balance"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.out.println("로그인 오류: " + e.getMessage());
        }
        return null;
    }

    public boolean chargeMoney(int memberId, int amount) {
        String sql = "UPDATE member SET balance = balance + ? WHERE member_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PW);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, amount);
            ps.setInt(2, memberId);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            System.out.println("충전 실패: " + e.getMessage());
            return false;
        }
    }

    public boolean deductBalance(int memberId, int amount) {
        String sql = "UPDATE member SET balance = balance - ? WHERE member_id = ? AND balance >= ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PW);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, amount);
            ps.setInt(2, memberId);
            ps.setInt(3, amount);
            int affected = ps.executeUpdate();
            return affected > 0; // true면 차감 성공
        } catch (SQLException e) {
            System.out.println("잔액 차감 실패: " + e.getMessage());
            return false;
        }
    }

    public boolean refundBalance(int memberId, int amount) {
        String sql = "UPDATE member SET balance = balance + ? WHERE member_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PW);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, amount);
            ps.setInt(2, memberId);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            System.out.println("환불 실패: " + e.getMessage());
            return false;
        }
    }
    public int getBalance(int memberId) {
        String sql = "SELECT balance FROM member WHERE member_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PW);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("balance");
                }
            }
        } catch (SQLException e) {
            System.out.println("잔액 조회 실패: " + e.getMessage());
        }
        return 0; // 실패 시 0 반환
    }


}
