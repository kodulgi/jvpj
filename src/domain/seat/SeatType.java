package domain.seat;

public enum SeatType {
    SENIOR("노약좌석"),
    PREGNANT("임산부석"),
    GENERAL("일반석"),
    PREMIUM("프리미엄석");

    private final String displayName;
    SeatType(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}
