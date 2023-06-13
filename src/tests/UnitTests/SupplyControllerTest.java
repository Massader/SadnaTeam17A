package UnitTests;

import DomainLayer.Payment.PaymentController;
import DomainLayer.Supply.SupplyController;
import ServiceLayer.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SupplyControllerTest {


    private SupplyController supplyController;

    @BeforeEach
    public void setup() {
        supplyController = SupplyController.getInstance();
        supplyController.init();
    }


    @Test
    public void testRequestSupply() {
        String name = "lior levy";
        String address = "heshkolit";
        String city = "beer sheva";
        String country = "Israel";
        int zip = 6092000;

        // Call the method under test
        Response<Integer> response = supplyController.supply(name, address, city, country, zip);
        assertTrue(response.isSuccessful());
        int messageValue = response.getValue();
        assertNotEquals(-1, messageValue);
        assertTrue(messageValue >= 10000 && messageValue <= 100000);
    }
//        int messageValue = response.getValue();
//        assertEquals(1, messageValue);

    @Test
    public void testCancelSupplySuccess() {
        String name = "lior levy";
        String address = "heshkolit";
        String city = "beer sheva";
        String country = "Israel";
        int zip = 6092000;

        Response<Integer> responsePay = supplyController.supply(name, address, city, country, zip);
        int transaction = responsePay.getValue();
        //cancelPay
        Response<Boolean> response = supplyController.cancelSupply(transaction);
        assertTrue(response.isSuccessful());
        assertTrue(response.getValue());
    }


    @Test
    public void testHandshake() {
        // Call the method under test
        Response<Boolean> response = supplyController.handshake();
        assertTrue(response.isSuccessful());
        assertTrue(response.getValue());
    }
    }
