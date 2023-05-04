package DomainLayer.Supply;

import DomainLayer.Market.Users.ShoppingCart;
import DomainLayer.Payment.PaymentReal;

public class SupplyProxy implements SupplyBridge{
    SupplyBridge real = null;
    public void setReal() {
        if(real ==null){real = new SupplyReal();}
    }
    @Override
    public Boolean validateOrder(String address) {
        return real == null ? null : real.validateOrder(address);
    }

    @Override
    public Integer sendOrder() {
        return real == null ? null : real.sendOrder();
    }
}
