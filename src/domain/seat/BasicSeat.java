package domain.seat;

import service.price.PriceStrategy;

public class BasicSeat implements Seat {
    private final String type;
    private final PriceStrategy priceStrategy;

    public BasicSeat(String type, PriceStrategy priceStrategy) {
        this.type = type;
        this.priceStrategy = priceStrategy;
    }

    @Override
    public int getPrice() { return priceStrategy.calculatePrice(); }
    @Override
    public String getType() { return type; }
}
