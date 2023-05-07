package DomainLayer.Payment;

public class PaymentReal implements PaymentBridge{
    @Override
    public void setReal() {}

    @Override
    public Boolean pay(double price, String credit) {
        return true;
    }

    @Override
    public Boolean validatePaymentDetails() {
        return true;
    }


    @Override
    public Boolean cancelPay(double price, String credit) {
        return true;
    }

    @Override
    public Integer requestPayment() {
        return 1;
    }
}
