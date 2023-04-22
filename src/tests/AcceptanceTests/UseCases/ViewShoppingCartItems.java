package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.*;

import java.util.List;
import java.util.UUID;
import org.junit.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ViewShoppingCartItems extends ProjectTest {

    UUID client;

    @BeforeAll
    public void setUp() {
        bridge.setReal();
        bridge.register("founder", "pass");
        client = bridge.createClient();
    }

    @BeforeEach
    public void beforeEach()  {
        client = bridge.createClient();
    }

    @AfterEach
    public void tearDown() {
        bridge.closeClient(client);
    }

    @Test
    public void ViewShoppingCartItemsSuccessClient(){
        List<ServiceShoppingBasket> shoppingCartView=  bridge.viewShoppingCartItems(client);
        Assert.assertNotNull(shoppingCartView);
        Assert.assertTrue(shoppingCartView.isEmpty());
    }

    @Test
    public void ViewShoppingCartItemsSuccessUser() {
        bridge.register("newUser", "pass");
        UUID newClient = bridge.createClient();
        UUID user = bridge.login(newClient, "newUser", "pass");
        List<ServiceShoppingBasket> shoppingCartView=  bridge.viewShoppingCartItems(user);
        Assert.assertNotNull(shoppingCartView);
        Assert.assertTrue(shoppingCartView.isEmpty());
        bridge.logout(user);
        bridge.closeClient(newClient);
    }

    @Test
    public void ViewShoppingCartItemsFail() {
        UUID notUserOrClient = UUID.randomUUID();
        List<ServiceShoppingBasket> shoppingCartView=  bridge.viewShoppingCartItems(notUserOrClient);
        Assert.assertNull(shoppingCartView);
    }
}
