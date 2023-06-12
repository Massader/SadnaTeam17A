package UnitTests;

import DomainLayer.Payment.PaymentController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ServiceLayer.Response;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentControllerTest {

    private PaymentController paymentController;

    @BeforeEach
    public void setup() {
        paymentController = PaymentController.getInstance();
        paymentController.init();
    }


    @Test
    public void testRequestPayment() {
        double price = 100.0;
        String cardNumber = "12345";
        String month = "12";
        String year = "2027";
        String holder = "Lior Levy";
        String ccv = "123";
        String id = "123456789";

        // Call the method under test
        Response<Integer> response = paymentController.pay(price, cardNumber, month, year, holder, ccv, id);
        assertTrue(response.isSuccessful());
        int messageValue = response.getValue();
        assertNotEquals(-1, messageValue);
        assertTrue(messageValue >= 10000 && messageValue <= 100000);

    }

    @Test
    public void testRequestPaymentFail() {
        double price = 100.0;
        String cardNumber = "12345";
        String month = "12";
        String year = "2027";
        String holder = "Lior Levy";
        String ccv = "986";
        String id = "123456789";

        // Call the method under test
        Response<Integer> response = paymentController.pay(price, cardNumber, month, year, holder, ccv, id);
        assertTrue(response.isError());
        assertEquals("Transaction payment has failed.",response.getMessage());
    }

    @Test
    public void testRequestPaymentFailTimeOut() {
        double price = 100.0;
        String cardNumber = "12345";
        String month = "12";
        String year = "2027";
        String holder = "Lior Levy";
        String ccv = "984";
        String id = "123456789";

        // Call the method under test

        Response<Integer> response = paymentController.pay(price, cardNumber, month, year, holder, ccv, id);
        assertTrue(response.isError());
        assertEquals("Transaction payment has failed.",response.getMessage());
    }



    @Test
    public void testCancelPaySuccess() {

        double price = 100.0;
        String cardNumber = "12345";
        String month = "12";
        String year = "2027";
        String holder = "Lior Levy";
        String ccv = "123";
        String id = "123456789";
        //pay
        Response<Integer> responsePay = paymentController.pay(price, cardNumber, month, year, holder, ccv, id);
        int transaction = responsePay.getValue();
        //cancelPay
        Response<Boolean> response = paymentController.cancelPay(transaction);
        assertTrue(response.isSuccessful());
       assertTrue(response.getValue());
    }

    @Test
    public void testHandshake() {
        // Call the method under test
        Response<Boolean> response = paymentController.handshake();
        assertTrue(response.isSuccessful());
        assertTrue(response.getValue()== true);
    }
}

