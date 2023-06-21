package AcceptanceTests.UseCases;
import AcceptanceTests.*;

import java.util.UUID;
import ServiceLayer.Response;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnterSystem extends ProjectTest {

    @BeforeAll
    public static void beforeClass() {

    }

    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void tearDown() {
        deleteDB();
        bridge.resetService();
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

        assertFalse(clients0.isError(), "bridge.numOfClients() failed");
        assertFalse(client.isError(), "bridge.createClient() failed");
        assertFalse(clients1.isError(), "bridge.numOfClients() failed");

        assertNotNull(client.getValue(), "bridge.createClient() failed");
        assertEquals(1, clients1.getValue() - clients0.getValue(), "number of clients did not increased by 1");
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

        assertFalse(clients0.isError(), "bridge.numOfClients() failed");
        assertFalse(clients1.isError(), "bridge.numOfClients() failed");
        for (Response<UUID> e : enters) {
            assertFalse(e.isError(), "one of the bridge.createClient() calls failed");
            assertNotNull(e.getValue(), "one of the bridge.createClient() calls failed");
        }
        assertEquals(1000, clients1.getValue() - clients0.getValue(), "number of clients did not increased by 1000");
    }
}