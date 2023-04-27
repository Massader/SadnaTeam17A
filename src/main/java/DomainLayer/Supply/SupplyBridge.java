package DomainLayer.Supply;

public interface SupplyBridge {
    public void setReal();
    Boolean validateOrder();
    int sendOrder();
}
