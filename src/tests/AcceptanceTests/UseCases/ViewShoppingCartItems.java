package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.*;

import java.util.List;
import java.util.UUID;
import org.junit.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ViewShoppingCartItems extends ProjectTest {

    UUID client;

    @BeforeAll
    public void setUp() {
        bridge.setReal();
        bridge.register("founder", "Pass1");
        client = bridge.enterSystem();
    }

    @BeforeEach
    public void beforeEach()  {
        client = bridge.enterSystem();
    }

    @AfterEach
    public void tearDown() {
        bridge.exitSystem(client);
    }

    @Test
    public void ViewShoppingCartItemsSuccessClient(){
        bridge.register("founder", "Pass1");
        client = bridge.enterSystem();
        List<ServiceShoppingBasket> shoppingCartView=  bridge.viewShoppingCartItems(client);
        Assert.assertNotNull(shoppingCartView);
        Assert.assertTrue(shoppingCartView.isEmpty());
    }

    @Test
    public void ViewShoppingCartItemsSuccessUser() {
        bridge.register("newUser", "Pass1");
        UUID newClient = bridge.enterSystem();
        UUID user = bridge.login(newClient, "newUser", "Pass1");
        List<ServiceShoppingBasket> shoppingCartView=  bridge.viewShoppingCartItems(user);
        Assert.assertNotNull(shoppingCartView);
        Assert.assertTrue(shoppingCartView.isEmpty());
        bridge.logout(user);
        bridge.exitSystem(newClient);
    }

    @Test
    public void ViewShoppingCartItemsFail() {
        UUID notUserOrClient = UUID.randomUUID();
        List<ServiceShoppingBasket> shoppingCartView=  bridge.viewShoppingCartItems(notUserOrClient);
        Assert.assertNull(shoppingCartView);
    }
}
