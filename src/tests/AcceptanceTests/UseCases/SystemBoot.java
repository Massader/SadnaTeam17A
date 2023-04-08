package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class SystemBoot extends ProjectTest {

    public SystemBoot(Bridge real) {
        super(real);
    }

    public boolean systemBoot() {
        return proxy.systemBoot();
    }

    public boolean integrityTest() {
        return proxy.integrityTest();
    }
}
