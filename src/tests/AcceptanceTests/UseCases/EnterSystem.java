package AcceptanceTests.UseCases;
import AcceptanceTests.*;

import java.util.UUID;

import ServiceLayer.Response;
import org.junit.*;

import org.junit.Test;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnterSystem extends ProjectTest {

    @BeforeClass
    public static void beforeClass() {

    }

    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void tearDown() {

    }

    @AfterAll
    public void afterClass() {
        bridge.resetService();
    }


    @Test
    //checks if a client can enter the system by creating a new client credentials and ensuring that the client credentials is not null.
    public void enterSystemSuccess() {
        Response<Integer> clients0 = bridge.numOfClients();
        Response<UUID> client = bridge.createClient();
        Response<Integer> clients1 = bridge.numOfClients();

        Assert.assertFalse("bridge.numOfClients() failed", clients0.isError());
        Assert.assertFalse("bridge.createClient() failed", client.isError());
        Assert.assertFalse("bridge.numOfClients() failed", clients1.isError());

        Assert.assertNotNull("bridge.createClient() failed", client.getValue());
        Assert.assertEquals("number of clients did not increased by 1", 1, clients1.getValue() - clients0.getValue());
    }

    @Test
    //check if the system can accommodate 1000 clients concurrently.
    public void enterSystemConcurrently(){
        Response<Integer> clients0 = bridge.numOfClients();

        Response<UUID>[] enters = new Response[1000];
        Thread[] threads = new Thread[1000];
        try {
            for (int i = 0; i < 1000; i++) {
                final int index = i;
                threads[i] = new Thread(() -> enters[index] = bridge.createClient());
                threads[i].start();
            }
            for (Thread t : threads) {
                t.join();
            }
        }
        catch (Exception ignore) {}

        Response<Integer> clients1 = bridge.numOfClients();

        Assert.assertFalse("bridge.numOfClients() failed", clients0.isError());
        Assert.assertFalse("bridge.numOfClients() failed", clients1.isError());
        for (Response<UUID> e : enters) {
            Assert.assertFalse("one of the bridge.createClient() calls failed", e.isError());
            Assert.assertNotNull("one of the bridge.createClient() calls failed", e.getValue());
        }
        Assert.assertEquals("number of clients did not increased by 1000", 1000, clients1.getValue() - clients0.getValue());
    }
}