package AcceptanceTests;

public abstract class ProjectTest {

    public Bridge proxy;

    public ProjectTest(Bridge real) {
        proxy = new ProxyBridge(real);
    }
}
