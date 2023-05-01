package APILayer.Requests;

import ServiceLayer.ServiceObjects.ServiceItem;

import java.util.UUID;

public class ItemRequest extends Request {

    private ServiceItem item;

    public ItemRequest(UUID clientCredentials, ServiceItem item) {
        super(clientCredentials);
        this.item = item;
    }

    public ServiceItem getItem() {
        return item;
    }

    public void setItem(ServiceItem item) {
        this.item = item;
    }
}
