package DomainLayer.Market.Stores.Discounts;

import java.util.ArrayList;
import java.util.List;

public class ConditionalDiscount extends Discount {

    List<DiscountCondition> conditions;

    public ConditionalDiscount(double discount) {
        super(discount);
        conditions = new ArrayList<>();
    }

    @Override
    public double calculatePrice(double basePrice) {
        // Check conditions apply
        return basePrice;
    }
}
