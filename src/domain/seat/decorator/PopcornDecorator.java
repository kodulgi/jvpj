package domain.seat.decorator;

import domain.seat.Seat;

public class PopcornDecorator extends SeatDecorator {
    public PopcornDecorator(Seat seat) {
        super(seat);
    }

    @Override
    public int getPrice() {
        return decoratedSeat.getPrice() + 3000;
    }

    @Override
    public String getType() {
        return decoratedSeat.getType() + " + 팝콘세트";
    }
}
