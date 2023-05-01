package DomainLayer.Security;

import DomainLayer.Market.StoreController;
import ServiceLayer.Response;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SecurityController {
    private static SecurityController singleton = null;
    private final ConcurrentHashMap<UUID, String> passwords;
    protected ConcurrentHashMap<UUID, SecurityQuestion> securityQuestions;
    private final int PASS_MIN_LEN = 4;
    private final int PASS_MAX_LEN = 20;
    private final PasswordEncryptor encryptor;

    private  SecurityController(){
        passwords = new ConcurrentHashMap<>();
        securityQuestions = new ConcurrentHashMap<>();
        encryptor = new PasswordEncryptor();
    }

    public static synchronized SecurityController getInstance()
    {
        if (singleton == null)
            singleton = new SecurityController();
        return singleton;
    }

    public Response<Boolean> validatePassword(UUID id, String password) {
        try {
            if (!passwords.containsKey(id))
                return Response.getFailResponse("User does not have a registered password.");
            if(passwords.get(id).equals(encryptor.encrypt(password)))
                return Response.getSuccessResponse(true);
            else return Response.getFailResponse("Incorrect password");
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public boolean isLegalPassword(String password)
    {
        boolean isLegal = true;

        if (password.length() >= PASS_MIN_LEN && password.length() <= PASS_MAX_LEN)
        {
            boolean hasUpper = false;
            boolean hasLower = false;
            boolean hasNumber = false;
            for (int i = 0; i<password.length(); i++)
            {
                char c = password.charAt(i);
                if (c >= 'A' && c <= 'Z') hasUpper = true;
                if (c >= 'a' && c <= 'z') hasLower = true;
                if (c >= '0' && c <= '9') hasNumber = true;
            }
            if (!hasUpper || !hasLower || !hasNumber) isLegal = false;

        }
        else isLegal = false;

        return isLegal;
    }

    public Response<Boolean> changePassword(UUID id, String oldPass, String newPass){
        try {
            if (validatePassword(id, oldPass).isError())
                return Response.getFailResponse("the old password is incorrect!");
            return encryptAndSavePassword(id, newPass);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public void removePassword(UUID id){
        securityQuestions.remove(id);
        passwords.remove(id);
    }

    public Response<Boolean> encryptAndSavePassword(UUID id, String newPass){
        try {
            if (!isLegalPassword(newPass))
                return Response.getFailResponse("Illegal Password");
            passwords.put(id, encryptor.encrypt(newPass));
            return Response.getSuccessResponse(true);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> ValidateSecurityQuestion(UUID id, String answer){
        try{
            if(securityQuestions.get(id) != null){
        boolean valid = securityQuestions.get(id).validateAnswer(answer);
        return Response.getSuccessResponse(valid);}
        else return Response.getSuccessResponse(null);}
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> addSecurityQuestion(UUID id, String question, String answer) {
        try {
            SecurityQuestion securityQuestion = new SecurityQuestion(id, question, answer);
            if (securityQuestions.get(id) != null && securityQuestions.get(id).getQuestion().equals(question)) {
                return Response.getFailResponse("The same question already exists for the user");
            }
            securityQuestions.put(id, securityQuestion);
            return Response.getSuccessResponse(true);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<String> getSecurityQuestion (UUID id){
        try {
            if (securityQuestions.get(id) == null)
                return Response.getFailResponse("There is no security question for this user");
            else return Response.getSuccessResponse(securityQuestions.get(id).getQuestion());
        }
        catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

        //added for tests
    public void addPassword(UUID id, String pass) throws Exception {
        passwords.put(id, encryptor.encrypt(pass));
    }

    public void resetController() {
        singleton = new SecurityController();
    }

}

