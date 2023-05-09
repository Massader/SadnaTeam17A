package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.*;

import java.util.List;
import java.util.UUID;
import org.junit.*;

import org.junit.Test;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ViewShoppingCartItems extends ProjectTest {

    UUID client;

    @BeforeAll
    public void beforeClass() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
    }

    @BeforeEach
    public void setUp()  {
        client = bridge.createClient().getValue();
    }

    @AfterEach
    public void tearDown() {
        bridge.closeClient(client);
    }

    @AfterAll
    public void afterClass() {
        
    }

    @Test
    //Tests whether a newly created client has an empty shopping cart.
    public void ViewShoppingCartItemsSuccessClient(){
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        List<ServiceShoppingBasket> shoppingCartView=  bridge.getCart(client).getValue();
        Assert.assertNotNull(shoppingCartView);
        Assert.assertTrue(shoppingCartView.isEmpty());
    }

    @Test
    // Tests whether a newly registered user has an empty shopping cart.
    public void ViewShoppingCartItemsSuccessUser() {
        bridge.register("newUser", "Pass1");
        UUID newClient = bridge.createClient().getValue();
        UUID user = bridge.login(newClient, "newUser", "Pass1").getValue().getId();
        List<ServiceShoppingBasket> shoppingCartView=  bridge.getCart(user).getValue();
        Assert.assertNotNull(shoppingCartView);
        Assert.assertTrue(shoppingCartView.isEmpty());
        bridge.logout(user);
        bridge.closeClient(newClient);
    }

    @Test
    //Tests whether an attempt to view a shopping cart by an invalid UUID fails.
    public void ViewShoppingCartItemsFail() {
        UUID notUserOrClient = UUID.randomUUID();
        List<ServiceShoppingBasket> shoppingCartView=  bridge.getCart(notUserOrClient).getValue();
        Assert.assertNull(shoppingCartView);
    }
}
