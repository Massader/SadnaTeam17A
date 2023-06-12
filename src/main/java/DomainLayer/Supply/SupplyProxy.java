package DomainLayer.Supply;

public class SupplyProxy implements SupplyBridge{
    SupplyBridge real = null;
    public void setReal() {
        if(real ==null){real = new SupplyReal();}
    }

    @Override
    public String handshake() {
        return null;
    }

    @Override
    public Integer supply(String name, String address, String city, String country, int zip) {
        return real == null ? null : real.supply( name , address, city, country, zip);
    }

    @Override
    public Integer cancel_supply(int transaction_id) {
        return real == null ? -1 : real.cancel_supply(transaction_id);
    }




}
