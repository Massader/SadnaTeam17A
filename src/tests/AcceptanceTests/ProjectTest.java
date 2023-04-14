package AcceptanceTests;

public abstract class ProjectTest {

    public Bridge bridge;

    public ProjectTest() {
        bridge = new ProxyBridge();
    }
}
