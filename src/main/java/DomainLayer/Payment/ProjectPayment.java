package DomainLayer.Payment;

public abstract class ProjectPayment {
    protected PaymentBridge bridge;

    public ProjectPayment(){
        bridge= new PaymentProxy();
        bridge.setReal();
    }
}
