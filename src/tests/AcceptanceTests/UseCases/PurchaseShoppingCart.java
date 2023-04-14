package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class PurchaseShoppingCart extends ProjectTest {

    public PurchaseShoppingCart(Bridge real) {
        super(real);
    }

    public boolean purchaseShoppingCartPayment() {
        return bridge.purchaseShoppingCartPayment();
    }

    public boolean purchaseShoppingCartSupply() {
        return bridge.purchaseShoppingCartSupply();
    }
}
