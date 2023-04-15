package DomainLayer.Market.Stores.Discounts;

public class HiddenDiscount extends Discount{
    public HiddenDiscount(double discount) {
        super(discount);
    }

    @Override
    public double calculatePrice(double basePrice) {
        return basePrice;
    }
}
