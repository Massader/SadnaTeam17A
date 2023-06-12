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

        // Assert the expected result
        assertTrue(response.isSuccessful());


        try {
            int messageValue = Integer.parseInt(response.getMessage());

            assertTrue(messageValue >= 10000 && messageValue <= 100000);
        } catch (NumberFormatException e) {
            fail("Invalid response message format: " + response.getMessage());
        }
        assertTrue(response.getMessage() >= 10000 && response.getMessage() <= 100000);
    }

    }

    @Test
    public void testPay() {
        // Call the method under test
        Response<Integer> response = paymentController.pay(/* provide necessary arguments */);

        // Assert the expected result
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    public void testCancelPay() {
        // Call the method under test
        Response<Boolean> response = paymentController.cancelPay(/* provide necessary arguments */);

        // Assert the expected result
        assertTrue(response.isSuccess());
        assertTrue(response.getData());
    }

    @Test
    public void testHandshake() {
        // Call the method under test
        Response<Boolean> response = paymentController.handshake();

        // Assert the expected result
        assertTrue(response.isSuccess());
        assertTrue(response.getData());
    }
}
