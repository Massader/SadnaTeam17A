package APILayer.Requests;

import java.util.UUID;

public class ChangePasswordRequest extends Request {

    private String oldPassword;
    private String newPassword;

    public ChangePasswordRequest(UUID clientCredentials, String oldPassword, String newPassword) {
        super(clientCredentials);
        this.newPassword = newPassword;
        this.oldPassword = oldPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
