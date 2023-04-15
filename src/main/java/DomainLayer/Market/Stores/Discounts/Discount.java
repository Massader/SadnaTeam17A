package DomainLayer.Market.Stores.Discounts;

public abstract class Discount {
    private double discount;

    public abstract double calculatePrice(double basePrice);

    public Discount(double discount) {
        this.discount = discount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
