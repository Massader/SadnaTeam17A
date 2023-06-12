package DomainLayer.Payment;

public interface PaymentBridge {
    public void setReal();

    String handshake();

    Integer pay(String card_number,String  month, String year,String holder, String ccv, String id);

    Integer cancel_Pay(int transactionId);
}