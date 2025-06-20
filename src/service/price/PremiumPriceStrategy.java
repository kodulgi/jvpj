package service.price;

public class PremiumPriceStrategy implements PriceStrategy{
    @Override
    public int calculatePrice() {
        return 16000;
    }
}
