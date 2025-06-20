package service.reservation;

import dao.MemberDAO;
import dao.MovieDAO;
import dao.ReservationDAO;
import domain.Member;
import domain.Movie;
import domain.Reservation;
import domain.seat.Seat;
import domain.seat.decorator.PopcornDecorator;
import domain.seat.factory.*;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ReservationServiceImp implements ReservationService {
    private final MovieDAO movieDAO;
    private final ReservationDAO reservationDAO;
    private final Scanner sc;
    private final MemberDAO memberDAO;

    public ReservationServiceImp() {
        this.movieDAO = new MovieDAO();
        this.sc = new Scanner(System.in);
        this.reservationDAO = new ReservationDAO();
        this.memberDAO = new MemberDAO();
    }

    @Override
    public void reserve(Reservation reservation) {
        reservationDAO.createReservation(reservation);
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
        System.out.println("현재 보유 금액: " + member.getBalance() + "원");

        if (member.getBalance() < seat.getPrice()) {
            System.out.println("잔액이 부족하여 예매할 수 없습니다. 충전 후 시도해주세요.");
            return;
        }
        System.out.print("예매하시겠습니까? (yes/no): ");
        String ok = sc.nextLine();
        if (!ok.equalsIgnoreCase("yes")) {
            System.out.println("예매가 취소되었습니다.");
            return;
        }

        if (!memberDAO.deductBalance(member.getMemberId(), seat.getPrice())) {
            System.out.println("잔액이 부족하여 결제에 실패했습니다.");
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

        if (reservationDAO.createReservation(reservation)) {
            if (!movieDAO.decrementSeatCount(movie.getId(), seatType)) {
                System.out.println("좌석 차감(DB)이 실패했습니다. 관리자에게 문의하세요.");
            }

            member.addReservation(reservation);
            member.setBalance(memberDAO.getBalance(member.getMemberId()));
            System.out.println("예매가 완료되었습니다!");
        } else {
            System.out.println("예약에 실패했습니다. 다시 시도해주세요.");
            memberDAO.refundBalance(member.getMemberId(), seat.getPrice());
        }
    }

    @Override
    public void showMyPage(Member member) {
        System.out.println("\n==== 나의 예매 내역 ====");

        List<Reservation> reservations = reservationDAO.getReservationsForMember(member.getMemberId());
        if (reservations.isEmpty()) {
            System.out.println("예매 내역이 없습니다.");
        } else {
            for (int i = 0; i < reservations.size(); i++) {
                Reservation r = reservations.get(i);
                System.out.printf("%d. 영화: %s / 시간: %s / 좌석: %s / 가격: %d원\n",
                        i + 1, r.getMovieTitle(), r.getMovieTime(), r.getSeatType(), r.getPrice());
            }
        }

        int dbBalance = memberDAO.getBalance(member.getMemberId());
        member.setBalance(dbBalance);
        System.out.println("현재 보유 금액: " + member.getBalance() + "원");

        System.out.println("1. 예매 취소하기");
        System.out.println("2. 돈 충전하기");
        System.out.println("3. 뒤로가기");
        System.out.print("선택: ");
        int sel = sc.nextInt();
        sc.nextLine();

        if (sel == 1 && !reservations.isEmpty()) {
            System.out.print("취소할 예매 번호 입력: ");
            int idx = sc.nextInt() - 1;
            sc.nextLine();
            if (idx < 0 || idx >= reservations.size()) {
                System.out.println("잘못된 입력입니다.");
                return;
            }
            Reservation toCancel = reservations.get(idx);
            int movieId = movieDAO.findMovieIdByTitleAndTime(toCancel.getMovieTitle(), toCancel.getMovieTime());

            if (reservationDAO.deleteReservation(
                    member.getMemberId(), toCancel.getMovieTitle(), toCancel.getMovieTime(), toCancel.getSeatType())) {

                if (memberDAO.refundBalance(member.getMemberId(), toCancel.getPrice())) {

                    if (!movieDAO.incrementSeatCount(movieId, toCancel.getSeatType())) {
                        System.out.println("좌석 복구(DB)가 실패했습니다. 관리자에게 문의하세요.");
                    }

                    member.removeReservation(toCancel);
                    member.setBalance(memberDAO.getBalance(member.getMemberId()));
                    System.out.println("예매가 취소되고 금액이 환불되었습니다.");
                } else {
                    System.out.println("환불 처리 실패!");
                }
            } else {
                System.out.println("예매 취소 실패!");
            }
        } else if (sel == 2) {
            System.out.print("충전할 금액 입력: ");
            int amount = sc.nextInt();
            sc.nextLine();
            if (memberDAO.chargeMoney(member.getMemberId(), amount)) {
                member.chargeMoney(amount);
                member.setBalance(memberDAO.getBalance(member.getMemberId()));
                System.out.println(amount + "원이 충전되었습니다. 현재 잔액: " + member.getBalance() + "원");
            } else {
                System.out.println("충전 실패!");
            }
        } else if (sel == 3) {
            return;
        } else {
            System.out.println("잘못된 선택입니다.");
        }
    }

    private SeatFactory getSeatFactory(String seatType) {
        switch (seatType) {
            case "노약좌석": return new SeniorSeatFactory();
            case "임산부석": return new PregnantSeatFactory();
            case "일반석": return new GeneralSeatFactory();
            case "프리미엄석": return new PremiumSeatFactory();
            default: throw new IllegalArgumentException("존재하지 않는 좌석 타입: " + seatType);
        }
    }
}