package DomainLayer.Supply;

import DomainLayer.Market.Users.ShoppingCart;

public interface SupplyBridge {
    public void setReal();
    Boolean validateOrder(String address);
    Integer sendOrder();//supply(name , address, city, country, zip)
    //int cancel_supply(UUID transaction_id)// - the id of the transaction id of the transaction to be canceled.)
    //SERVER= "https://php-server-try.000webhostapp.com/"
    //In order to specify which service to request from
}
