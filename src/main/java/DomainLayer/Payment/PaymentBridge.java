package DomainLayer.Payment;

public interface PaymentBridge {
    public void setReal();
    Boolean pay(double price, String credit);
    Boolean validatePaymentDetails();


    Boolean cancelPay(double nowPrice, String credit);
    Integer requestPayment();
}
