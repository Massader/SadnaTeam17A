package DomainLayer.Supply;

public abstract class ProjectSupply {
    protected SupplyBridge bridge;

    public ProjectSupply(){
        bridge= new SupplyProxy();
        bridge.setReal();;
    }
}
