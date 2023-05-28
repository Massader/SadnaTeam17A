package DomainLayer.Payment;

public interface PaymentBridge {
    public void setReal();

    Boolean pay(double price, String credit);// card_number, month, year, holder, ccv, id
    Boolean validatePaymentDetails();


    Boolean cancelPay(double nowPrice, String credit);//: transaction_id - the id of the transaction id of the    transaction to be canceled and return int
    Integer requestPayment(); //   Boolean handshake();
}