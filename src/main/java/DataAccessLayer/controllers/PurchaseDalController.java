package DataAccessLayer.controllers;

import DataAccessLayer.CartItemRepository;
import DataAccessLayer.RepositoryFactory;
import DataAccessLayer.ShoppingBasketRepository;
import DataAccessLayer.ShoppingCartRepository;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Users.CartItem;
import DomainLayer.Market.Users.Client;
import DomainLayer.Market.Users.ShoppingBasket;
import DomainLayer.Market.Users.ShoppingCart;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PurchaseDalController {

    RepositoryFactory repositoryFactory;
    ShoppingBasketRepository shoppingBasketRepository;
    ShoppingCartRepository shoppingCartRepository;
    CartItemRepository cartItemRepository;
    private static PurchaseDalController singleton = null;


    private PurchaseDalController(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
        this.shoppingBasketRepository = repositoryFactory.shoppingBasketRepository;
        this.shoppingCartRepository = repositoryFactory.shoppingCartRepository;
        this.cartItemRepository = repositoryFactory.cartItemRepository;
    }
    public static synchronized PurchaseDalController getInstance(RepositoryFactory repositoryFactory) {
        if (singleton == null) {
            singleton = new PurchaseDalController(repositoryFactory);
        }
        return singleton;
    }

    public UUID saveCart(ShoppingCart shoppingCart){
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart.getId();
    }

    public ShoppingCart getCart(Client client){
        List<ShoppingCart> cart= shoppingCartRepository.findByClient(client);
        if(cart.isEmpty())
            return null;
        return cart.get(0);
    }

    public UUID saveBasket(ShoppingBasket shoppingBasket){
        shoppingBasketRepository.save(shoppingBasket);
        return shoppingBasket.getId();
    }

//    public ShoppingBasket getbasket(){
//        List<ShoppingCart> cart= shoppingCartRepository.findByClient(client);
//        if(cart.isEmpty())
//            return null;
//        return cart.get(0);
//    }

    public UUID saveCartItem(CartItem cartItem){
        cartItemRepository.save(cartItem);
        return cartItem.getItem().getId();
    }

    public List<CartItem> getCartItems(Item item){
        List<CartItem> cartItems= cartItemRepository.findByItem(item);
        return cartItems;
    }
}
