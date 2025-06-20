package facade;

import dao.MovieDAO;
import domain.Member;
import domain.Reservation;
import observer.AdminObserver;
import observer.ReservationSubject;
import service.movie.MovieService;
import service.movie.MovieServiceImp;
import service.movie.MovieServiceProxy;
import service.reservation.ReservationService;
import service.reservation.ReservationServiceImp;
import service.price.PriceStrategy;
import service.price.PriceStrategyFactory;

import java.util.Scanner;

public class ReservationFacade {
    private final ReservationService reservationService = new ReservationServiceImp();
    private final ReservationSubject subject;

    public ReservationFacade() {
        // 옵저버 등록
        this.subject = new ReservationSubject();
        subject.registerObserver(new AdminObserver("관리자1"));
        subject.registerObserver(new AdminObserver("관리자2")); // 필요시 추가
    }

    public void reserveSeat(Member member, String seatType, int seatNumber, String movieTitle, String time) {
        MovieService movieService = new MovieServiceProxy(
                new MovieServiceImp(new MovieDAO(), new Scanner(System.in)), member
        );

        if (!movieService.exists(movieTitle)) {
            System.out.println("해당 영화가 존재하지 않습니다.");
            return;
        }

        PriceStrategy strategy = PriceStrategyFactory.getStrategy(member, seatType);
        int price = strategy.calculatePrice();

        if (member.getPoint() < price) {
            System.out.println("포인트가 부족합니다.");
            return;
        }

        member.setPoint(member.getPoint() - price);
        Reservation reservation = new Reservation(member.getMemberId(), movieTitle, time, seatType, price);
        reservationService.reserve(reservation);

        // ✅ 관리자에게 알림 전송
        String message = "회원 " + member.getMemberId() + " 님이 " + movieTitle + " 예매 (좌석: " + seatType + ")";
        subject.notifyObservers(message);

        System.out.println("예매 완료! 금액: " + price);
    }

    public void showMyReservations(Member member) {
        reservationService.showMyPage(member);
    }
}
