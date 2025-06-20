package dao;

import domain.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/java-projectDB";
    private static final String USER = "root";
    private static final String PW = "fpdlswj365";
    public boolean createMovie(Movie movie) {
        String sql = "INSERT INTO movie (title, time, senior_seat_count, pregnant_seat_count, general_seat_count, premium_seat_count ) VALUES (?, ?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PW);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, movie.getTitle());
            ps.setString(2, movie.getTime());
            ps.setInt(3, movie.getSeatCounts().get("노약좌석"));
            ps.setInt(4, movie.getSeatCounts().get("임산부석"));
            ps.setInt(5, movie.getSeatCounts().get("일반석"));
            ps.setInt(6, movie.getSeatCounts().get("프리미엄석"));

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("영화 등록 실패: " + e.getMessage());
            return false;
        }
    }

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movie";
        try (Connection conn = DriverManager.getConnection(URL, USER, PW);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String time = rs.getString("time");

                Map<String, Integer> seatCounts = new HashMap<>();
                seatCounts.put("노약좌석", rs.getInt("senior_seat_count"));
                seatCounts.put("임산부석", rs.getInt("pregnant_seat_count"));
                seatCounts.put("일반석", rs.getInt("general_seat_count"));
                seatCounts.put("프리미엄석", rs.getInt("premium_seat_count"));

                Movie movie = new Movie(id, title, time, seatCounts);
                movies.add(movie);
            }
        } catch (SQLException e) {
            System.out.println("영화 목록 조회 실패: " + e.getMessage());
        }
        return movies;
    }
}

