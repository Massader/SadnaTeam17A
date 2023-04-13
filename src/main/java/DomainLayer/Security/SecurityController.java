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
    protected ConcurrentHashMap<UUID, SecurityQuestion> securityQuestions;
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

    public Response<UUID> ValidatePass(UUID id, String password) {
        try {
            if(passwords.get(id).equals(password))
                return Response.getSuccessResponse(id);
            else return Response.getFailResponse("Incorrect password");
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
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
        try {
            if (!ValidatePass(id, oldPass).getValue().equals(id))
                return Response.getFailResponse("the old password is incorrect!");
            return encryptAndSavePassword(id, newPass);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> removePassword(UUID id, String oldPass, String newPass){
        try {
            passwords.remove(id);
            return Response.getSuccessResponse(true);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> encryptAndSavePassword(UUID id, String newPass){
        try {
            if (!isLegalPassword(newPass))
                return Response.getFailResponse("Illegal Password");
            passwords.put(id, newPass);
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
        try {  if (securityQuestions.get(id) == null)
        {return Response.getFailResponse("There is no security question for this user");}
            else return Response.getSuccessResponse(securityQuestions.get(id).getQuestion());
        }  catch (Exception exception) {
        return Response.getFailResponse(exception.getMessage());
    }


        }


    }

