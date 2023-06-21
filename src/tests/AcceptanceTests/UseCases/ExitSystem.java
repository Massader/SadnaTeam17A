package AcceptanceTests.UseCases;
import AcceptanceTests.*;

import java.util.UUID;
import ServiceLayer.Response;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExitSystem extends ProjectTest {

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
    //checks if a client can exit the system.
    public void exitSystemSuccess() {
        UUID client = bridge.createClient().getValue();
        Response<Integer> clients0 = bridge.numOfClients();
        Response<Boolean> exit = bridge.closeClient(client);
        Response<Integer> clients1 = bridge.numOfClients();

        assertFalse(clients0.isError(), "bridge.numOfClients() failed");
        assertFalse(exit.isError(), "bridge.closeClient(client) failed");
        assertFalse(clients1.isError(), "bridge.numOfClients() failed");

        assertTrue(exit.getValue(), "bridge.closeClient(client) failed");
        assertEquals(1, clients0.getValue() - clients1.getValue(), "number of clients did not decreased by 1");
    }

    @Test
    //check if 1000 clients can exit the system concurrently.
    public void enterSystemConcurrently(){
        UUID[] clients = new UUID[1000];
        for (int i = 0; i < 1000; i++) {
            clients[i] = bridge.createClient().getValue();
        }

        Response<Integer> clients0 = bridge.numOfClients();
        Response<Boolean>[] exits = new Response[1000];
        Thread[] threads = new Thread[1000];
        try {
            for (int i = 0; i < 1000; i++) {
                final int index = i;
                threads[i] = new Thread(() -> exits[index] = bridge.closeClient(clients[index]));
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
        for (Response<Boolean> e : exits) {
            assertFalse(e.isError(), "one of the bridge.closeClient(clients[index]) calls failed");
            assertTrue(e.getValue(), "one of the bridge.closeClient(clients[index])) calls failed");
        }
        assertEquals(1000, clients0.getValue() - clients1.getValue(), "number of clients did not decreased by 1000");
    }
}