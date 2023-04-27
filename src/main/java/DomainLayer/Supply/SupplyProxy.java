package DomainLayer.Supply;

import DomainLayer.Payment.PaymentReal;

public class SupplyProxy implements SupplyBridge{
    SupplyBridge real = null;
    public void setReal() {
        real = new SupplyReal();
    }


    @Override
    public Boolean validateOrder() {
        return real == null ? null : real.validateOrder();
    }

    @Override
    public int sendOrder() {
        return real == null ? null : real.sendOrder();
    }
}
