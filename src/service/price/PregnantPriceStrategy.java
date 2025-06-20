package service.price;

public class PregnantPriceStrategy implements PriceStrategy{
    @Override
    public int calculatePrice() {
        return 9000;
    }
}
