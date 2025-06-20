package service.reservation;

import domain.Member;

public interface ReservationService {
    void reserve(Member member);

    void showMyPage(Member member);

}
