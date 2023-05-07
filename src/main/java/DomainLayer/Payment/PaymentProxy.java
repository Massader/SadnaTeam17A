package DomainLayer.Payment;

public class PaymentProxy implements PaymentBridge{
    PaymentBridge real = null;
    public void setReal() {
        if(real==null){
            real = new PaymentReal();}
    }
    @Override
    public Boolean pay(double price, String credit) {
        return real == null ? false : real.pay(price, credit);
    }

    @Override
    public Boolean validatePaymentDetails() {
        return real == null ? null : real.validatePaymentDetails();}



    @Override
    public Boolean cancelPay(double nowPrice, String credit) {
        return real == null ? false : real.cancelPay(nowPrice,credit);
    }

    @Override
    public Integer requestPayment() {
        return real == null ? 0 : real.requestPayment();
    }
}
