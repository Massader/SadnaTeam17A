package APILayer.Requests;

import java.util.UUID;

public class TargetUserRequest extends Request {

    UUID targetUser;
    public TargetUserRequest(UUID clientCredentials, UUID targetUser) {
        super(clientCredentials);
        this.targetUser = targetUser;
    }

    public UUID getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(UUID targetUser) {
        this.targetUser = targetUser;
    }
}
