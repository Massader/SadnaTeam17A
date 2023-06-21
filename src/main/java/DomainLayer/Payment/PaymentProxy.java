package DomainLayer.Payment;

public class PaymentProxy implements PaymentBridge{
    PaymentBridge real = null;
    public void setReal() {
        if(real==null){
            real = new PaymentReal();
            real.setReal();}
    }

    @Override
    public String handshake() {
        return real == null ? "false" : real.handshake();
    }

    @Override
    public Integer pay(String card_number, String month, String year, String holder, String ccv, String id) {
        return real == null ? -1 : real.pay( card_number, month, year, holder, ccv, id);
    }

    @Override
    public Integer cancel_Pay(int transactionId) {
        return real == null ? -1 : real.cancel_Pay(transactionId);
    }



}
