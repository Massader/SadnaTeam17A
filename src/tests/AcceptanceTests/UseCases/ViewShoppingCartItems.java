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
        bridge.register("founder", "Pass1");
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
    //Tests whether a newly created client has an empty shopping cart.
    public void ViewShoppingCartItemsSuccessClient(){
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        List<ServiceShoppingBasket> shoppingCartView=  bridge.getCart(client);
        Assert.assertNotNull(shoppingCartView);
        Assert.assertTrue(shoppingCartView.isEmpty());
    }

    @Test
    // Tests whether a newly registered user has an empty shopping cart.
    public void ViewShoppingCartItemsSuccessUser() {
        bridge.register("newUser", "Pass1");
        UUID newClient = bridge.createClient();
        UUID user = bridge.login(newClient, "newUser", "Pass1");
        List<ServiceShoppingBasket> shoppingCartView=  bridge.getCart(user);
        Assert.assertNotNull(shoppingCartView);
        Assert.assertTrue(shoppingCartView.isEmpty());
        bridge.logout(user);
        bridge.closeClient(newClient);
    }

    @Test
    //Tests whether an attempt to view a shopping cart by an invalid UUID fails.
    public void ViewShoppingCartItemsFail() {
        UUID notUserOrClient = UUID.randomUUID();
        List<ServiceShoppingBasket> shoppingCartView=  bridge.getCart(notUserOrClient);
        Assert.assertNull(shoppingCartView);
    }
}
