package domain.seat.decorator;

import domain.seat.Seat;

public abstract class SeatDecorator implements Seat {
    protected final Seat decoratedSeat;

    public SeatDecorator(Seat seat) {
        this.decoratedSeat = seat;
    }

    @Override
    public int getPrice() {
        return decoratedSeat.getPrice();
    }

    @Override
    public String getType() {
        return decoratedSeat.getType();
    }
}