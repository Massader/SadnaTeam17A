package DataAccessLayer;

import DomainLayer.Market.StoreController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "singleton")
public class RepositoryFactory {
    private final ClientRepository clientRepository;

    @Autowired
    public RepositoryFactory(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    public ClientRepository getClientRepository(){
        return clientRepository;
    }
}
