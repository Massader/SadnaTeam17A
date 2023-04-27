package DomainLayer.Payment;

public interface PaymentBridge {
    public void setReal();
    int pay();
    Boolean validatePaymentDetails();
    int requestPayment();
}
