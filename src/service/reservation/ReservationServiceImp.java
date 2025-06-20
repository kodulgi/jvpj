package service.reservation;

import dao.MovieDAO;
import dao.ReservationDAO;
import domain.Member;
import domain.Movie;
import domain.Reservation;
import domain.seat.Seat;
import domain.seat.decorator.PopcornDecorator;
import domain.seat.factory.*;
import observer.AdminObserver;
import observer.ReservationSubject;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ReservationServiceImp implements ReservationService {
    private final MovieDAO movieDAO;
    private final ReservationDAO reservationDAO;
    private final Scanner sc;

    public ReservationServiceImp() {
        this.movieDAO = new MovieDAO();
        this.sc = new Scanner(System.in);
        this.reservationDAO = new ReservationDAO();
    }

    @Override
    public void reserve(Member member) {
        List<Movie> movies = movieDAO.getAllMovies();
        if (movies.isEmpty()) {
            System.out.println("등록된 영화가 없습니다.");
            return;
        }

        System.out.println("==== 영화 목록 ====");
        for (int i = 0; i < movies.size(); i++) {
            System.out.printf("%d. %s (%s)\n", i + 1, movies.get(i).getTitle(), movies.get(i).getTime());
        }

        System.out.print("영화 선택(번호): ");
        int movieIdx = sc.nextInt() - 1;
        sc.nextLine();
        if (movieIdx < 0 || movieIdx >= movies.size()) {
            System.out.println("잘못된 선택입니다.");
            return;
        }

        Movie movie = movies.get(movieIdx);
        Map<String, Integer> seatCounts = movie.getSeatCounts();
        String[] seatTypes = {"노약좌석", "임산부석", "일반석", "프리미엄석"};

        System.out.println("좌석 종류:");
        for (int i = 0; i < seatTypes.length; i++) {
            int count = seatCounts.getOrDefault(seatTypes[i], 0);
            System.out.printf("%d. %s (남은 좌석: %d)\n", i + 1, seatTypes[i], count);
        }

        System.out.print("좌석 선택(번호): ");
        int seatTypeIdx = sc.nextInt() - 1;
        sc.nextLine();
        if (seatTypeIdx < 0 || seatTypeIdx >= seatTypes.length) {
            System.out.println("잘못된 선택입니다.");
            return;
        }

        String seatType = seatTypes[seatTypeIdx];
        if (seatCounts.get(seatType) == null || seatCounts.get(seatType) <= 0) {
            System.out.println("죄송합니다. 해당 좌석이 매진입니다.");
            return;
        }

        SeatFactory seatFactory = getSeatFactory(seatType);
        Seat seat = seatFactory.createSeat();

        System.out.print("팝콘을 추가하시겠습니까? (yes/no): ");
        String popcorn = sc.nextLine();
        if (popcorn.equalsIgnoreCase("yes")) {
            seat = new PopcornDecorator(seat);
        }

        System.out.println("최종 예매 가격: " + seat.getPrice() + "원");
        System.out.print("예매하시겠습니까? (yes/no): ");
        String ok = sc.nextLine();
        if (!ok.equalsIgnoreCase("yes")) {
            System.out.println("예매가 취소되었습니다.");
            return;
        }

        seatCounts.put(seatType, seatCounts.get(seatType) - 1);

        Reservation reservation = new Reservation(
                member.getMemberId(),
                movie.getTitle(),
                movie.getTime(),
                seat.getType(),
                seat.getPrice()
        );

        if (reservationDAO.saveReservation(reservation)) {
            member.addReservation(reservation);
            System.out.println("예매가 완료되었습니다!");

            // ✅ 옵저버 패턴: 예매 후 관리자에게 알림
            ReservationSubject subject = new ReservationSubject();
            subject.registerObserver(new AdminObserver("관리자1"));
            subject.notifyObservers("[" + member.getId() + "] 님이 영화 '" + movie.getTitle() + "'을 예매했습니다.");
        } else {
            System.out.println("예약에 실패했습니다. 다시 시도해주세요.");
        }
    }

    @Override
    public void showMyPage(Member member) {
        System.out.println("==== 나의 예매 내역 ====");
        List<Reservation> reservations = reservationDAO.getReservationsForMember(member.getMemberId());
        if (reservations.isEmpty()) {
            System.out.println("예매 내역이 없습니다.");
            return;
        }
        for (Reservation r : reservations) {
            System.out.printf("영화: %s / 시간: %s / 좌석: %s / 가격: %d원\n",
                    r.getMovieTitle(), r.getMovieTime(), r.getSeatType(), r.getPrice());
        }
    }

    private SeatFactory getSeatFactory(String seatType) {
        switch (seatType) {
            case "노약좌석":
                return new SeniorSeatFactory();
            case "임산부석":
                return new PregnantSeatFactory();
            case "일반석":
                return new GeneralSeatFactory();
            case "프리미엄석":
                return new PremiumSeatFactory();
            default:
                throw new IllegalArgumentException("존재하지 않는 좌석 타입: " + seatType);
        }
    }
}
