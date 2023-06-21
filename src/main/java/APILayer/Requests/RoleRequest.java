package APILayer.Requests;

import java.util.UUID;

public class RoleRequest extends Request {

    private UUID storeId;
    private UUID appointee;

    public RoleRequest(UUID clientCredentials, UUID storeId, UUID manager) {
        super(clientCredentials);
        this.storeId = storeId;
        this.appointee = manager;
    }
    public RoleRequest(){}

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public UUID getAppointee() {
        return appointee;
    }

    public void setAppointee(UUID appointee) {
        this.appointee = appointee;
    }
}
