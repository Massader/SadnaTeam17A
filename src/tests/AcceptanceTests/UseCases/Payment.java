package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class Payment extends ProjectTest {

    public Payment(Bridge real) {
        super(real);
    }

    public boolean payForShoppingCart() {
        return bridge.payForShoppingCart();
    }
}
