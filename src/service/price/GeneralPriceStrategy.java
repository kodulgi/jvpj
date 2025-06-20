package service.price;

public class GeneralPriceStrategy implements PriceStrategy{
    @Override
    public int calculatePrice() {
        return 12000;
    }
}
