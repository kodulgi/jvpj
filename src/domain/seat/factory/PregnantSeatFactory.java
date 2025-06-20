package domain.seat.factory;

import domain.seat.BasicSeat;
import domain.seat.Seat;
import service.price.PregnantPriceStrategy;

public class PregnantSeatFactory extends SeatFactory {
    @Override
    public Seat createSeat() {
        return new BasicSeat("임산부석", new PregnantPriceStrategy());
    }
}
