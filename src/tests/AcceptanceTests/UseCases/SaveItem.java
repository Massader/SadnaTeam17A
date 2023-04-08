package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class SaveItem extends ProjectTest {

    public SaveItem(Bridge real) {
        super(real);
    }

    public boolean saveItemInShoppingCart() {
        return proxy.saveItemInShoppingCart();
    }
}
