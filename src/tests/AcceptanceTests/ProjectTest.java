package AcceptanceTests;

public abstract class ProjectTest {

    protected Bridge bridge;

    public ProjectTest() {
        bridge = new ProxyBridge();
        bridge.setReal();
    }
}
