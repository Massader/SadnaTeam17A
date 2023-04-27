package DomainLayer.Payment;

public class PaymentReal implements PaymentBridge{
    @Override
    public void setReal() {}

    @Override
    public int pay() {
        return 0;
    }

    @Override
    public Boolean validatePaymentDetails() {
        return null;
    }

    @Override
    public int requestPayment() {
        return 0;
    }
}
