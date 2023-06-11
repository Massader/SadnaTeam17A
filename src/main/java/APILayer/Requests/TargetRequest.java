package APILayer.Requests;

import java.util.UUID;

public class TargetRequest extends Request {

    UUID targetId;
    public TargetRequest(UUID clientCredentials, UUID targetUser) {
        super(clientCredentials);
        this.targetId = targetUser;
    }

    public UUID getTargetId() {
        return targetId;
    }

    public void setTargetId(UUID targetUser) {
        this.targetId = targetUser;
    }
}
