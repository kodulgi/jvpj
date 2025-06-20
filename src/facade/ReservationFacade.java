
package facade;

import domain.Member;
import domain.Reservation;
import service.movie.MovieService;
import service.movie.MovieServiceImp;
import service.movie.MovieServiceProxy;
import service.reservation.ReservationService;
import service.reservation.ReservationServiceImp;
import service.price.PriceStrategy;
import service.price.PriceStrategyFactory;

public class ReservationFacade {
    private final ReservationService reservationService = new ReservationServiceImp();

    public void reserveSeat(Member member, String seatType, int seatNumber, String movieTitle, String time) {
        MovieService movieService = new MovieServiceProxy(new MovieServiceImp(), member);

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

        System.out.println("예매 완료! 금액: " + price);
    }

    public void showMyReservations(Member member) {
        reservationService.showMyPage(member);
    }
}
