package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class Register extends ProjectTest {

    public Register(Bridge real) {
        super(real);
    }

    public boolean register() {
        return proxy.register();
    }
}
