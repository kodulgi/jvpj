package main;

import dao.MovieDAO;
import domain.Member;
import service.movie.MovieService;
import service.movie.MovieServiceImp;
import service.movie.MovieServiceProxy;
import service.reservation.ReservationService;
import service.reservation.ReservationServiceImp;
import util.InputUtil;
import util.PrintUtil;

import java.util.Scanner;

public class MainMenu {
    private final Scanner sc;
    private final Member member;

    private final MovieService movieService;

    private final ReservationService reservationService;

    public MainMenu(Scanner sc, Member member) {
        this.sc = sc;
        this.member = member;
        MovieDAO movieDAO = new MovieDAO();
        MovieService movieServiceImp = new MovieServiceImp(movieDAO, sc);
        this.movieService = new MovieServiceProxy(movieServiceImp, member);;
        this.reservationService = new ReservationServiceImp();
    }

    public void showMenu() {
        while (true) {
            PrintUtil.printMainMenu();
            int choice = InputUtil.getIntInput(sc);

            if (choice == 1) {
                reservationService.reserve(member);   // ★ 예매 서비스 실행
            }
            if (choice == 2) {
                if (movieService.registerMovie()) {
                    System.out.println("영화 등록 완료!");
                }
            }
            if (choice == 4) {
                System.out.println("이용해주셔서 감사합니다.");
                break;
            }
            // 각 기능은 나중에 연결!
        }
    }
}
