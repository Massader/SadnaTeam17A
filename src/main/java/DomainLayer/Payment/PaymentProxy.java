package DomainLayer.Payment;

public class PaymentProxy implements  PaymentBridge{
    PaymentBridge real = null;
    public void setReal() {
        real = new PaymentReal();
    }
    @Override
    public int pay() {
        return real == null ? null : real.pay();
    }

    @Override
    public Boolean validatePaymentDetails() {
        return real == null ? null : real.validatePaymentDetails();}

    @Override
    public int requestPayment() {
        return real == null ? null : real.requestPayment();

    }
}
