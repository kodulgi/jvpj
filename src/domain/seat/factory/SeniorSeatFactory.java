package domain.seat.factory;

import domain.seat.BasicSeat;
import domain.seat.Seat;
import service.price.SeniorPriceStrategy;

public class SeniorSeatFactory extends SeatFactory {
    @Override
    public Seat createSeat() {
        return new BasicSeat("노약좌석", new SeniorPriceStrategy());
    }
}
