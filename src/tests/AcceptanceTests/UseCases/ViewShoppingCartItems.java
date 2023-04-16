package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.ServiceShoppingBasket;
import org.junit.*;

import java.util.List;
import java.util.UUID;

public class ViewShoppingCartItems extends ProjectTest {
    UUID client;

    @BeforeClass
    public void setUp() {
        bridge.setReal();
        bridge.register("founder", "pass");
        client = bridge.enterSystem();
    }

    @Before
    public void beforeEach()  {
        client = bridge.enterSystem();
    }

    @After
    public void tearDown() {
        bridge.exitSystem(client);


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
        UUID newClient = bridge.enterSystem();
        UUID user = bridge.login(newClient, "newUser", "pass");
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
