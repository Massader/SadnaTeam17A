package DataAccessLayer.controllers;

import DataAccessLayer.RepositoryFactory;
import DataAccessLayer.ShoppingBasketRepository;
import DataAccessLayer.ShoppingCartRepository;
import DomainLayer.Market.Users.Client;
import DomainLayer.Market.Users.ShoppingCart;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PurchaseDalController {

    RepositoryFactory repositoryFactory;
    ShoppingBasketRepository shoppingBasketRepository;
    ShoppingCartRepository shoppingCartRepository;
    private static PurchaseDalController singleton = null;


    private PurchaseDalController(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
        this.shoppingBasketRepository = repositoryFactory.shoppingBasketRepository;
        this.shoppingCartRepository = repositoryFactory.shoppingCartRepository;
    }
    public static synchronized PurchaseDalController getInstance(RepositoryFactory repositoryFactory) {
        if (singleton == null) {
            singleton = new PurchaseDalController(repositoryFactory);
        }
        return singleton;
    }

    public Long saveCart(ShoppingCart shoppingCart){
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart.getId();
    }

    public ShoppingCart getCart(Client client){
        List<ShoppingCart> cart= shoppingCartRepository.findByClient(client);
        if(cart.isEmpty())
            return null;
        return cart.get(0);
    }
}
