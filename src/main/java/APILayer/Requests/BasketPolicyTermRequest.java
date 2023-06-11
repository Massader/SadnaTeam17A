package APILayer.Requests;

import java.util.UUID;

public class BasketPolicyTermRequest extends PolicyTermRequest{
    public BasketPolicyTermRequest(UUID clientCredentials, UUID storeId, boolean atLeast, int quantity) {
        super(clientCredentials, storeId, atLeast, quantity);
    }
}
