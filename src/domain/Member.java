package domain;

import java.util.ArrayList;
import java.util.List;

public class Member {

    private int memberId;

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    private String id;

    private String password;

    private int age;

    public Member(String id, String password, int age, String role) {
        this.id = id;
        this.password = password;
        this.age = age;
        this.role = role;
    }

    private String role;

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public int getAge() {
        return age;
    }

    private List<Reservation> reservationList = new ArrayList<>();


    // 예매 내역 추가 메서드
    public void addReservation(Reservation reservation) {
        reservationList.add(reservation);
    }

    // 예매 내역 조회 메서드
    public List<Reservation> getReservationList() {
        return reservationList;
    }

}
