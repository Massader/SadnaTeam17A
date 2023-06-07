package DataAccessLayer.controllers;

import DataAccessLayer.PasswordRepository;
import DataAccessLayer.RepositoryFactory;
import DataAccessLayer.SecurityQuestionRepository;
import DataAccessLayer.UserRepository;
import DomainLayer.Market.Users.User;
import DomainLayer.Security.Password;
import DomainLayer.Security.SecurityQuestion;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class UserDalController {
    RepositoryFactory repositoryFactory;
    UserRepository userRepository;
    PasswordRepository passwordRepository;
    SecurityQuestionRepository securityQuestionRepository;
    private static UserDalController singleton = null;


    private UserDalController(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
        this.userRepository = repositoryFactory.userRepository; //high frequency use
        this.passwordRepository = repositoryFactory.passwordRepository;
        securityQuestionRepository = repositoryFactory.securityQuestionRepository;
    }

    public static synchronized UserDalController getInstance(RepositoryFactory repositoryFactory) {
        if (singleton == null) {
            singleton = new UserDalController(repositoryFactory);
        }
        return singleton;
    }

    public User getUser(UUID uuid){
        List<User> usersFromDb = userRepository.findByClientCredentials(uuid);
        if(usersFromDb.isEmpty())
            return null;
        else return usersFromDb.get(0);
    }

    public User getUser(String username){
        List<User> usersFromDb = userRepository.findByUsername(username);
        if(usersFromDb.isEmpty())
            return null;
        else return usersFromDb.get(0);
    }

    public User getAdmin(){
        List<User> usersFromDb = userRepository.findByIsAdmin(true);
        if(usersFromDb.isEmpty())
            return null;
        else return usersFromDb.get(0);
    }


    public boolean userExists(UUID uuid){
        return userRepository.existsByClientCredentials(uuid);
    }

    public boolean userExists(String username){
        return userRepository.existsByUsername(username);
    }

    public UUID saveUser(User user){
        try {
            userRepository.save(user);
            return user.getId();
        }
        catch(Exception e){
            return null;
        }
    }


    public void deleteUser(User user){
        userRepository.delete(user);
    }
    public void deleteAll(int pass){ //so that no one will use it by accident
        if (pass == 1234)
            userRepository.deleteAll();
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public List<User> serachUser(String keyword){
        return userRepository.findByUsernameContaining(keyword);
    }


    public Password getPassword(UUID uuid){
        List<Password> passwords = passwordRepository.findAllById(Set.of(uuid));
        if(passwords.isEmpty())
            return null;
        return passwords.get(0);
    }

    public void addPassword(Password password){
        passwordRepository.save(password);
    }

    public void deletePassword(UUID id){
        passwordRepository.deleteAllById(Set.of(id));
    }

    public SecurityQuestion getSecurityQuestion(UUID uuid){
        List<SecurityQuestion> securityQuestion = securityQuestionRepository.findAllById(Set.of(uuid));
        if(securityQuestion.isEmpty())
            return null;
        return securityQuestion.get(0);
    }

    public void addSecurityQuestion(SecurityQuestion securityQuestion){
        securityQuestionRepository.save(securityQuestion);
    }

    public void deleteSecurityQuestion(UUID id){
        securityQuestionRepository.deleteAllById(Set.of(id));
    }


}
