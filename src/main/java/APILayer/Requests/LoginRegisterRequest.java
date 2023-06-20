package APILayer.Requests;

import java.util.UUID;

public class LoginRegisterRequest extends Request{

    private String username;
    private String password;
    public LoginRegisterRequest(UUID clientCredentials, String username, String password) {
        super(clientCredentials);
        this.username = username;
        this.password = password;
    }
    public LoginRegisterRequest(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
