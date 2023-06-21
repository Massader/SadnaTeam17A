package AcceptanceTests;

import DataAccessLayer.ItemRepository;
import DataAccessLayer.RepositoryFactory;
import DomainLayer.Market.UserController;
import DomainLayer.Market.Users.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

public abstract class ProjectTest {
    ;
    public RepositoryFactory repositoryFactory;
    protected Bridge bridge;

    public ProjectTest() {
        bridge = new ProxyBridge();
        bridge.setReal();
    }

    public void deleteDB() {
        try {
            this.repositoryFactory = UserController.repositoryFactory;
            repositoryFactory.roleRepository.deleteAll();
            repositoryFactory.itemRepository.deleteAll();
            repositoryFactory.passwordRepository.deleteAll();
            repositoryFactory.securityQuestionRepository.deleteAll();
            repositoryFactory.userRepository.deleteAll();
            repositoryFactory.storeRepository.deleteAll();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
