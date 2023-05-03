package DomainLayer.Supply;

import DomainLayer.Market.Users.ShoppingCart;

public interface SupplyBridge {
    public void setReal();
    Boolean validateOrder(String address);
    Integer sendOrder();
}
