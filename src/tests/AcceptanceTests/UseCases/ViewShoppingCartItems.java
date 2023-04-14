package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class ViewShoppingCartItems extends ProjectTest {

    public ViewShoppingCartItems(Bridge real) {
        super(real);
    }

    public boolean viewShoppingCartItems() {
        return bridge.viewShoppingCartItems();
    }
}
