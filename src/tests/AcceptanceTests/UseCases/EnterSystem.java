package AcceptanceTests.UseCases;
import AcceptanceTests.*;

import java.util.UUID;

import ServiceLayer.Response;
import org.junit.*;

import org.junit.Test;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnterSystem extends ProjectTest {

    @AfterAll
    public void afterAll() {
        bridge.resetService();
    }

    @Test
    //checks if a client can enter the system by creating a new client credentials and ensuring that the client credentials is not null.
    public void enterSystemSuccess() {
        Response<Integer> clients0 = bridge.numOfClients();
        Response<UUID> client = bridge.createClient();
        Response<Integer> clients1 = bridge.numOfClients();

        Assert.assertFalse(clients0.isError());
        Assert.assertFalse(client.isError());
        Assert.assertFalse(clients1.isError());

        Assert.assertNotNull(client.getValue());
        Assert.assertEquals(1, clients1.getValue() - clients0.getValue());
    }

    @Test
    //check if the system can accommodate 1000 clients concurrently.
    public void enterSystemConcurrently(){
        Response<Integer> clients0 = bridge.numOfClients();

        Response<UUID>[] responses = new Response[1000];
        Thread[] threads = new Thread[1000];
        try {
            for (int i = 0; i < 1000; i++) {
                final int index = i;
                threads[i] = new Thread(() -> responses[index] = bridge.createClient());
                threads[i].start();
            }
            for (Thread t : threads) {
                t.join();
            }
        }
        catch (Exception ignore) {}

        Response<Integer> clients1 = bridge.numOfClients();

        Assert.assertFalse(clients0.isError());
        Assert.assertFalse(clients1.isError());
        for (Response<UUID> r : responses) {
            Assert.assertFalse(r.isError());
            Assert.assertNotNull(r.getValue());
        }
        Assert.assertEquals(1000, clients1.getValue() - clients0.getValue());
    }
}
