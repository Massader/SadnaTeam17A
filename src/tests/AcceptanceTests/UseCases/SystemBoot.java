package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class SystemBoot extends ProjectTest {

    public SystemBoot(Bridge real) {
        super(real);
    }

    public boolean systemBoot() {
        return bridge.systemBoot();
    }

    public boolean integrityTest() {
        return bridge.integrityTest();
    }
}
