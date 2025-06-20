package dao;

import domain.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/java-projectDB?serverTimezone=Asia/Seoul";
    private static final String USER = "root";
    private static final String PW = "fpdlswj365";

    // 예매 내역 저장
    public boolean saveReservation(Reservation reservation) {
        String sql = "INSERT INTO reservation (member_id, movie_title, movie_time, seat_type, price) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PW);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reservation.getMemberId());
            ps.setString(2, reservation.getMovieTitle());
            ps.setString(3, reservation.getMovieTime());
            ps.setString(4, reservation.getSeatType());
            ps.setInt(5, reservation.getPrice());

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("예매 저장 실패: " + e.getMessage());
            return false;
        }
    }

    // 특정 회원의 예매 내역 모두 조회
    public List<Reservation> getReservationsForMember(int memberId) {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservation WHERE member_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PW);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String movieTitle = rs.getString("movie_title");
                    String movieTime = rs.getString("movie_time");
                    String seatType = rs.getString("seat_type");
                    int price = rs.getInt("price");

                    Reservation reservation = new Reservation(memberId, movieTitle, movieTime, seatType, price);
                    list.add(reservation);
                }
            }
        } catch (SQLException e) {
            System.out.println("예매 내역 조회 실패: " + e.getMessage());
        }
        return list;
    }

    // (옵션) 전체 예매 내역 조회, 예매 취소 등도 추가 구현 가능
}