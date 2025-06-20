package service.price;

public class SeniorPriceStrategy implements PriceStrategy{
    @Override
    public int calculatePrice() {
        return 8000;
    }
}
