package DomainLayer.Market.Stores.Discounts;

import java.util.ArrayList;
import java.util.List;

public class ConditionalDiscountType extends DiscountType {

    List<DiscountCondition> conditions;

    public ConditionalDiscountType(double discount) {
        super(discount);
        conditions = new ArrayList<>();
    }

    @Override
    public double calculatePrice(double basePrice) {
        // Check conditions apply
        return basePrice;
    }
}
