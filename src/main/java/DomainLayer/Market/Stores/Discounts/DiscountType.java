package DomainLayer.Market.Stores.Discounts;

public abstract class DiscountType {
    private double discount;

    public abstract double calculatePrice(double basePrice);

    public DiscountType(double discount) {
        this.discount = discount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
