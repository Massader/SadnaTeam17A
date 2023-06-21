package APILayer.Requests;

import java.util.UUID;

public class CreateStoreRequest extends Request {

    private String name;
    private String description;

    public CreateStoreRequest(UUID clientCredentials, String name, String description) {
        super(clientCredentials);
        this.name = name;
        this.description = description;
    }
    public CreateStoreRequest(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}