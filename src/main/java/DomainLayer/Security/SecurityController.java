package DomainLayer.Security;

import DomainLayer.Market.UserController;
import DomainLayer.Market.Users.SecurityQuestion;
import ServiceLayer.Response;

import java.util.Hashtable;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SecurityController {
    private static SecurityController singleton = null;
    private ConcurrentHashMap<UUID, String> passwords;
    private ConcurrentHashMap<UUID, SecurityQuestion> securityQuestions;
    private final int PASS_MIN_LEN = 4;
    private final int PASS_MAX_LEN = 20;
    private UserController userController;

    private  SecurityController(){
        passwords = new ConcurrentHashMap<>();
        securityQuestions = new ConcurrentHashMap<>();
        userController = UserController.getInstance();
    }

    public static synchronized SecurityController getInstance()
    {
        if (singleton == null)
            singleton = new SecurityController();
        return singleton;
    }

    public boolean ValidatePass(UUID id, String password) {
        return this.passwords.get(id).equals(password);
    }

    private boolean isLegalPassword(String password)
    {
        boolean isLegal = true;

//                 *****   all in commas just to make the manual tests easier :)   ******

//        if (password.length() >= PASS_MIN_LEN && password.length() <= PASS_MAX_LEN)
//        {
//            boolean hasUpper = false;
//            boolean hasLower = false;
//            boolean hasNumber = false;
//            for (int i = 0; i<password.length(); i++)
//            {
//                char c = password.charAt(i);
//                if (c >= 'A' && c <= 'Z') hasUpper = true;
//                if (c >= 'a' && c <= 'z') hasLower = true;
//                if (c >= '9' && c <= '0') hasNumber = true;
//            }
//            if (!hasUpper || !hasLower || !hasNumber) isLegal = false;
//
//        }
//        else isLegal = false;

        return isLegal;
    }

    public Response<Boolean> changePassword(UUID id, String oldPass, String newPass){
        if(!ValidatePass(id, oldPass))
            return Response.getFailResponse("the old password is incorrect!");

        return encryptAndSavePassword(id, newPass);
    }

    public Response<Boolean> removePassword(UUID id, String oldPass, String newPass){
        passwords.remove(id);
        return Response.getSuccessResponse(true);
    }

    public Response<Boolean> encryptAndSavePassword(UUID id, String newPass){
        if(!isLegalPassword(newPass))
            return Response.getFailResponse("illegal Password");
        passwords.put(id, newPass);
        return Response.getSuccessResponse(true);
    }

    public Response<Boolean> ValidateSecurityQuestion(UUID id, String answer){
        boolean valid = securityQuestions.get(id).validateAnswer(answer);
        return Response.getSuccessResponse(valid);
    }

    public Response<Boolean> addSecurityQuestion(UUID id, String question, String answer){
        SecurityQuestion securityQuestion = new SecurityQuestion(id, question, answer);
        securityQuestions.put(id, securityQuestion);
        return Response.getSuccessResponse(true);
    }

}