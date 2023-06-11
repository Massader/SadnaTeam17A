package APILayer.Requests;

import DomainLayer.Market.Users.Roles.StorePermissions;

import java.util.List;
import java.util.UUID;

public class SetManagerPermissionsRequest extends Request {
    private UUID managerId;
    private UUID storeId;
    private List<Integer> permissions;

    public SetManagerPermissionsRequest(UUID clientCredentials, UUID managerId, UUID storeId,
                                        List<Integer> permissions) {
        super(clientCredentials);
        this.managerId = managerId;
        this.storeId = storeId;
        this.permissions = permissions;
    }

    public UUID getManagerId() {
        return managerId;
    }

    public void setManagerId(UUID managerId) {
        this.managerId = managerId;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public List<Integer> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Integer> permissions) {
        this.permissions = permissions;
    }
}
