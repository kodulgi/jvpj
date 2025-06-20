package service.movie;

import dao.MovieDAO;
import domain.Movie;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MovieServiceImp implements MovieService {
    private final MovieDAO movieDAO;
    private final Scanner sc;

    public MovieServiceImp(MovieDAO movieDAO, Scanner sc) {
        this.movieDAO = movieDAO;
        this.sc = sc;
    }

    @Override
    public boolean registerMovie() {
        System.out.print("영화 제목: ");
        String title = sc.nextLine();
        System.out.print("상영 시간(예: 18:30): ");
        String time = sc.nextLine();
        String[] seatTypes = {"노약좌석", "임산부석", "일반석", "프리미엄석"};
        Map<String, Integer> seatCounts = new HashMap<>();

        for (String seatType : seatTypes) {
            System.out.print(seatType + " 개수: ");
            int count = sc.nextInt();
            sc.nextLine();
            seatCounts.put(seatType, count);
        }

        return movieDAO.createMovie(new Movie(title, time, seatCounts));
    }

}
