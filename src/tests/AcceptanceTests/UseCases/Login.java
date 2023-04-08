package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class Login extends ProjectTest {

    public Login(Bridge real) {
        super(real);
    }

    public boolean login() {
        return proxy.login();
    }
}
