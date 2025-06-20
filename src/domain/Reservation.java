package domain;

public class Reservation {

    private int ReservationId;

    public int getReservationId() {
        return ReservationId;
    }

    public void setReservationId(int reservationId) {
        ReservationId = reservationId;
    }

    private int memberId;

    private String movieTitle;

    private String movieTime;

    private String seatType;

    private int price;

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieTime() {
        return movieTime;
    }

    public void setMovieTime(String movieTime) {
        this.movieTime = movieTime;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Reservation(int memberId, String movieTitle, String movieTime, String seatType, int price) {
        this.memberId = memberId;
        this.movieTitle = movieTitle;
        this.movieTime = movieTime;
        this.seatType = seatType;
        this.price = price;
    }
}
