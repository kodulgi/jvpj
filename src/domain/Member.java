package domain;

import domain.Reservation;
import java.util.ArrayList;
import java.util.List;

public class Member {
    private int memberId;
    private String id;
    private String password;
    private int age;
    private int balance;
    private int point; // ✅ 포인트 필드 추가
    private String role;
    private List<Reservation> reservations;

    public Member(String id, String password, int age, String role) {
        this.id = id;
        this.password = password;
        this.age = age;
        this.role = role;
        this.reservations = new ArrayList<>();
    }

    public Member(int memberId, String id, String password, int age, int balance, String role) {
        this.memberId = memberId;
        this.id = id;
        this.password = password;
        this.age = age;
        this.balance = balance;
        this.role = role;
        this.reservations = new ArrayList<>();
    }

    public int getMemberId() {
        return memberId;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }

    public int getBalance() {
        return balance;
    }

    public String getRole() {
        return role;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void chargeMoney(int amount) {
        this.balance += amount;
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
    }

    public void removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
    }

    // ✅ 포인트 관련 메서드 추가
    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
