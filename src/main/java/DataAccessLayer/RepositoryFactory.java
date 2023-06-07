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

    @Autowired
    public RepositoryFactory(ClientRepository clientRepository,
                             UserRepository userRepository,
                             PasswordRepository passwordRepository,
                             SecurityQuestionRepository securityQuestionRepository )
    {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
        this.securityQuestionRepository=securityQuestionRepository;

    }

}
