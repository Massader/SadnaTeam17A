package DomainLayer.Supply;

import DomainLayer.Market.Users.ShoppingCart;

public class SupplyReal implements SupplyBridge {

    @Override
    public void setReal() {}
    

    @Override
    public Boolean validateOrder(String address) {
        return true;
    }

    @Override
    public Integer sendOrder() {
        return 1;
    }
}
