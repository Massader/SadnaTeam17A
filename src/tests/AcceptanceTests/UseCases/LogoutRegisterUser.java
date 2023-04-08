package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class LogoutRegisterUser extends ProjectTest {

    public LogoutRegisterUser(Bridge real) {
        super(real);
    }

    public boolean logoutRegisterUser() {
        return proxy.logoutRegisterUser();
    }
}
