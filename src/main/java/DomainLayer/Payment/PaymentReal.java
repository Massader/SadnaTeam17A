package DomainLayer.Payment;

public class PaymentReal implements PaymentBridge{
    @Override
    public void setReal() {}

    @Override
    public Boolean pay(double price,int credit) {
        return true;
    }

    @Override
    public Boolean validatePaymentDetails() {
        return null;
    }


    @Override
    public Boolean cancelPay(double nowPrice, int credit) {
        return true;
    }
}
