package DomainLayer.Supply;

public interface SupplyBridge {
    public void setReal();

    String handshake();

    Integer supply(String name, String address, String city, String country, int zip);

    Integer cancel_supply(int transaction_id);
}
