package service.movie;

import domain.Member;
import java.util.Scanner;

public class MovieServiceProxy implements MovieService {
    private final MovieService movieService;
    private final Member member;

    public MovieServiceProxy(MovieService movieService, Member member) {
        this.movieService = movieService;
        this.member = member;
    }

    @Override
    public boolean registerMovie() {
        if (!member.getRole().equals("ADMIN")) {
            System.out.println("관리자만 영화 등록이 가능합니다.");
            return false;
        }
        return movieService.registerMovie();
    }

    @Override
    public boolean exists(String title) {
        return movieService.exists(title);
    }
}
    