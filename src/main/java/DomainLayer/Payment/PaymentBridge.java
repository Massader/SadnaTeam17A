package DomainLayer.Payment;

public interface PaymentBridge {
    public void setReal();
    Boolean pay(double price,int credit);
    Boolean validatePaymentDetails();


    Boolean cancelPay(double nowPrice,int credit);
    Integer requestPayment();
}
