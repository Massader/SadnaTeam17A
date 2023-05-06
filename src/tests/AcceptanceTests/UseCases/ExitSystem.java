package AcceptanceTests.UseCases;
import AcceptanceTests.*;

import java.util.UUID;

import ServiceLayer.Response;
import org.junit.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExitSystem extends ProjectTest {

    @BeforeAll
    public void setUp() {
        bridge.resetService();
    }

    @Test
    //checks if a client can exit the system.
    public void exitSystemSuccess() {
        UUID client = bridge.createClient().getValue();
        Response<Integer> clients0 = bridge.numOfClients();
        Response<Boolean> exit = bridge.closeClient(client);
        Response<Integer> clients1 = bridge.numOfClients();

        Assert.assertFalse(clients0.isError());
        Assert.assertFalse(exit.isError());
        Assert.assertFalse(clients1.isError());

        Assert.assertTrue(exit.getValue());
        Assert.assertEquals(1, clients0.getValue() - clients1.getValue());
    }

    @Test
    //check if 1000 clients can exit the system concurrently.
    public void enterSystemConcurrently(){
        UUID[] clients = new UUID[1000];
        for (int i = 0; i < 1000; i++) {
            clients[i] = bridge.createClient().getValue();
        }

        Response<Integer> clients0 = bridge.numOfClients();
        Response<Boolean>[] responses = new Response[1000];
        Thread[] threads = new Thread[1000];
        try {
            for (int i = 0; i < 1000; i++) {
                final int index = i;
                threads[i] = new Thread(() -> responses[index] = bridge.closeClient(clients[index]));
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
        for (Response<Boolean> r : responses) {
            Assert.assertFalse(r.isError());
            Assert.assertTrue(r.getValue());
        }
        Assert.assertEquals(1000, clients0.getValue() - clients1.getValue());
    }
}