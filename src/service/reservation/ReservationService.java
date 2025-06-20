package service.reservation;

import domain.Member;
import domain.Reservation;

public interface ReservationService {
    // ✅ 인터페이스에 오버로딩된 reserve 메서드 2개 모두 정의
    void reserve(Reservation reservation);
    void reserve(Member member);

    void showMyPage(Member member);
}
