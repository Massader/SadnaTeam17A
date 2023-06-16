package DomainLayer.Security;

import DataAccessLayer.RepositoryFactory;
import DataAccessLayer.controllers.UserDalController;
import DomainLayer.Market.StoreController;
import DomainLayer.Market.UserController;
import DomainLayer.Market.Users.User;
import ServiceLayer.Response;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SecurityController {
    private static SecurityController singleton = null;
//    private final ConcurrentHashMap<UUID, String> passwords;
//    protected ConcurrentHashMap<UUID, SecurityQuestion> securityQuestions;
    private final int PASS_MIN_LEN = 4;
    private final int PASS_MAX_LEN = 20;
    private final PasswordEncryptor encryptor;
    private RepositoryFactory repositoryFactory;
    private UserDalController userDalController;

    private SecurityController(){
//        passwords = new ConcurrentHashMap<>();
//        securityQuestions = new ConcurrentHashMap<>();
        encryptor = new PasswordEncryptor();
    }

    public void init(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
        this.userDalController = UserDalController.getInstance(repositoryFactory);
    }

    public static synchronized SecurityController getInstance()
    {
        if (singleton == null)
            singleton = new SecurityController();
        return singleton;
    }

    public Response<Boolean> validatePassword(UUID id, String password) {
        try {
            Password passwordObj = userDalController.getPassword(id);
            if (passwordObj == null)
                return Response.getFailResponse("User does not have a registered password.");
            if(passwordObj.getEncryptedPassword().equals(encryptor.encrypt(password)))
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
//        securityQuestions.remove(id);
        userDalController.deleteSecurityQuestion(id);
        userDalController.deletePassword(id);
    }

    public Response<Boolean> encryptAndSavePassword(UUID id, String newPass){
        try {
            if (!isLegalPassword(newPass))
                return Response.getFailResponse("Illegal Password");
            Password password = new Password(id, encryptor.encrypt(newPass));
//            repositoryFactory.passwordRepository.save(password);
            userDalController.addPassword(password);
            return Response.getSuccessResponse(true);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> validateSecurityQuestion(UUID id, String answer){
        try{
            SecurityQuestion securityQuestion = userDalController.getSecurityQuestion(id);
            if(securityQuestion != null){
                boolean valid = securityQuestion.validateAnswer(answer);
                if (valid) {
                    UserController.getInstance().loginFromSecurityQuestion(id);
                    return Response.getSuccessResponse(true);
                }
                return Response.getFailResponse("Security question verification failed.");
            }
            else return Response.getFailResponse("User does not have a security question.");
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> addSecurityQuestion(UUID id, String question, String answer) {
        try {
            SecurityQuestion securityQuestion = new SecurityQuestion(id, question, answer);
            SecurityQuestion prevSecurityQuestion = userDalController.getSecurityQuestion(id);
            if (prevSecurityQuestion != null && prevSecurityQuestion.getQuestion().equals(question)) {
                return Response.getFailResponse("The same question already exists for the user");
            }
            userDalController.addSecurityQuestion(securityQuestion);
            return Response.getSuccessResponse(true);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<String> getSecurityQuestion (UUID id){
        try {
            SecurityQuestion SecurityQuestion = userDalController.getSecurityQuestion(id);
            if (SecurityQuestion == null)
                return Response.getFailResponse("There is no security question for this user");
            else return Response.getSuccessResponse(SecurityQuestion.getQuestion());
        }
        catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

        //added for tests
    public void addPassword(UUID id, String pass) throws Exception {
//        passwords.put(id, encryptor.encrypt(pass));
        userDalController.addPassword(new Password(id, encryptor.encrypt(pass)));
    }

    public void resetController() {
        singleton = new SecurityController();
    }

}

