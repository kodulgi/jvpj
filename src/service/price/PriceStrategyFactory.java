
package service.price;

import domain.Member;

public class PriceStrategyFactory {
    public static PriceStrategy getStrategy(Member member, String seatType) {
        switch (seatType) {
            case "일반석":
                return new GeneralPriceStrategy();
            case "프리미엄석":
                return new PremiumPriceStrategy();
            case "임산부석":
                return new PregnantPriceStrategy();
            case "노약좌석":
                return new SeniorPriceStrategy();
            default:
                return new GeneralPriceStrategy();
        }
    }
}
