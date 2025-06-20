package domain.seat.factory;

import domain.seat.BasicSeat;
import domain.seat.Seat;
import service.price.PremiumPriceStrategy;

public class PremiumSeatFactory extends SeatFactory {
    @Override
    public Seat createSeat() {
        return new BasicSeat("프리미엄석", new PremiumPriceStrategy());
    }
}
