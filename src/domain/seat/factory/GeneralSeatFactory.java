package domain.seat.factory;


import domain.seat.BasicSeat;
import domain.seat.Seat;
import service.price.GeneralPriceStrategy;

public class GeneralSeatFactory extends SeatFactory {
    @Override
    public Seat createSeat() {
        return new BasicSeat("일반석", new GeneralPriceStrategy());
    }
}
