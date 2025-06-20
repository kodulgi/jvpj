package service.reservation;

import domain.Member;

public interface ReservationService {
    void reserve(Reservation reservation);

    void showMyPage(Member member);

}
