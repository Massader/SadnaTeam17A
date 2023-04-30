package DomainLayer.Payment;

public class PaymentProxy implements  PaymentBridge{
    PaymentBridge real = null;
    public void setReal() {
        if(real==null){
            real = new PaymentReal();}
    }
    @Override
    public Boolean pay(double price,int credit) {
        return real == null ? false : real.pay(price, credit);
    }

    @Override
    public Boolean validatePaymentDetails() {
        return real == null ? null : real.validatePaymentDetails();}



    @Override
    public Boolean cancelPay(double nowPrice, int credit) {
        return real == null ? false : real.cancelPay(nowPrice,credit);
    }
}
