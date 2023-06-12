package DataAccessLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "singleton")
public class RepositoryFactory {
    public final ClientRepository clientRepository;
    public final UserRepository userRepository;
    public final PasswordRepository passwordRepository;
    public final SecurityQuestionRepository securityQuestionRepository;
    public final ItemRepository itemRepository;
    public final StoreRepository storeRepository;
    public final RoleRepository roleRepository;
    public final ItemReviewRepository itemReviewRepository;

    @Autowired
    public RepositoryFactory(ClientRepository clientRepository,
                             UserRepository userRepository,
                             PasswordRepository passwordRepository,
                             SecurityQuestionRepository securityQuestionRepository,
                             ItemRepository itemRepository,
                             StoreRepository storeRepository,
                             RoleRepository roleRepository,
                             ItemReviewRepository itemReviewRepository)
    {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
        this.securityQuestionRepository=securityQuestionRepository;
        this.storeRepository = storeRepository;
        this.itemRepository = itemRepository;
        this.roleRepository = roleRepository;
        this.itemReviewRepository = itemReviewRepository;
    }


}
