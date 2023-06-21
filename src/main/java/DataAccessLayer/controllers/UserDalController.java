package DataAccessLayer.controllers;

import DataAccessLayer.*;
import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.User;
import DomainLayer.Security.Password;
import DomainLayer.Security.SecurityQuestion;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserDalController {
    RepositoryFactory repositoryFactory;
    UserRepository userRepository;
    PasswordRepository passwordRepository;
    SecurityQuestionRepository securityQuestionRepository;
    RoleRepository roleRepository;
    private static UserDalController singleton = null;
    private Map<UUID, User> userCache;

    private UserDalController(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
        this.userRepository = repositoryFactory.userRepository; //high frequency use
        this.passwordRepository = repositoryFactory.passwordRepository;
        this.securityQuestionRepository = repositoryFactory.securityQuestionRepository;
        this.roleRepository = repositoryFactory.roleRepository;
        userCache = new ConcurrentHashMap<>();
    }

    public static synchronized UserDalController getInstance(RepositoryFactory repositoryFactory) {
        if (singleton == null) {
            singleton = new UserDalController(repositoryFactory);
        }
        return singleton;
    }

    public User getUser(UUID uuid){
        if (userCache.containsKey(uuid))
            return userCache.get(uuid);
        List<User> usersFromDb = userRepository.findByClientCredentials(uuid);
        if(usersFromDb.isEmpty())
            return null;
        User user = usersFromDb.get(0);

        userCache.put(uuid, user);
        return user;
    }

    public User getUser(String username){
        for(User user : userCache.values()){
            if(user.getUsername().equals(username))
                return user;
        }
        List<User> usersFromDb = userRepository.findByUsername(username);
        if(usersFromDb.isEmpty())
            return null;
        User user = usersFromDb.get(0);
        userCache.put(user.getId(), user);
        return user;

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
            userCache.put(user.getId(), user);
            return user.getId();
        }
        catch(Exception e){
            return null;
        }
    }


    public void deleteUser(User user){
        userRepository.delete(user);
        userCache.remove(user.getId());
    }
    public void deleteAll(int pass){ //so that no one will use it by accident
        if (pass == 1234) {
            userRepository.deleteAll();
            userCache = new ConcurrentHashMap<>();
        }

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


//    public List<Role> getRoles(User user) {
//        List<Role> roles = null;
//        if (loadedUsers.containsKey(user.getId()))
//            roles.get()
//        roles = roleRepository.findByUser(user);
//        return roles;
//    }
//    public boolean

    public void saveRole(Role role ){
        roleRepository.save(role);
    }

    public void resetCache() {
        userCache.clear();
    }
}
