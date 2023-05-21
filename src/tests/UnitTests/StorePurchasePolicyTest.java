package UnitTests;

import DomainLayer.Market.Stores.Category;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule.*;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StorePurchasePolicyTest {
    private Store store;
    private Item tomato;
    private Item cucumber;
    private Category vegetable;
   // ConcurrentHashMap<UUID,Integer> items;


    @BeforeEach
    public void setUp() {
        store = new Store("Test Store", "A test store for unit testing");
        tomato = new Item(UUID.randomUUID(), "tomato", 5.0, store.getStoreId(), 2.0, 30, "red vegetable");
        cucumber = new Item(UUID.randomUUID(), "cucumber", 5.0, store.getStoreId(),  5.0, 30, "green vegetable");
        vegetable =new Category("vegetable");
        tomato.addCategory(vegetable);
        try {
            store.addItem(tomato);
            store.addItem(cucumber);
        } catch (Exception ignored) {}
        //items = new ConcurrentHashMap<>();
        // items.put(tomato.getId(), 2);
        //items.put(cucumber.getId(), 1);
    }


    //the case of SimplePolicy {ShoppingBasketPurchaseRule,ItemPurchaseRule,CategoryPurchaseRule}*{Atmost, AtLeast}
    @Test
    //SimplePolicy, ShoppingBasketPurchaseRule, AtLeast
    void addSimplePolicyAtLeastShoppingBasketPurchaseRule() throws Exception {
        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato,10);

        ShopingBasketPurchaseRule shopingBasketPurchaseRule = new ShopingBasketPurchaseRule();
        AtLeastPurchaseRule BasketAtLeastPurchaseRule= new AtLeastPurchaseRule(shopingBasketPurchaseRule,49);
        store.addPolicyTermByStoreOwner(BasketAtLeastPurchaseRule);

        assertTrue(BasketAtLeastPurchaseRule.purchaseRuleOccurs(shoppingBasket1,store));

        ShoppingBasket shoppingBasket2= new ShoppingBasket(store.getStoreId());
        shoppingBasket2.addItem(tomato,1);
        assertFalse(BasketAtLeastPurchaseRule.purchaseRuleOccurs(shoppingBasket2,store));
    }

    @Test
        //SimplePolicy, ShoppingBasketPurchaseRule, AtMost
    void addSimplePolicyAtMostShoppingBasketPurchaseRule() throws Exception {
        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato,10);

        ShopingBasketPurchaseRule shopingBasketPurchaseRule = new ShopingBasketPurchaseRule();
        AtMostPurchaseRule BasketAtLeastPurchaseRule= new AtMostPurchaseRule(shopingBasketPurchaseRule,49);
        store.addPolicyTermByStoreOwner(BasketAtLeastPurchaseRule);

        assertFalse(BasketAtLeastPurchaseRule.purchaseRuleOccurs(shoppingBasket1,store));

        ShoppingBasket shoppingBasket2= new ShoppingBasket(store.getStoreId());
        shoppingBasket2.addItem(tomato,1);
        assertTrue(BasketAtLeastPurchaseRule.purchaseRuleOccurs(shoppingBasket2,store));
    }

    @Test
        //SimplePolicy, ItemPurchaseRule, AtLeast
    void addSimplePolicyAtLeastItemPurchaseRule() throws Exception {
        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato,10);
        ItemPurchaseRule tomatoPurchaseRule = new ItemPurchaseRule(tomato.getId());
        AtLeastPurchaseRule tomatoAtLeastPurchaseRule= new AtLeastPurchaseRule(tomatoPurchaseRule,2);
        store.addPolicyTermByStoreOwner(tomatoAtLeastPurchaseRule);
        assertTrue(tomatoAtLeastPurchaseRule.purchaseRuleOccurs(shoppingBasket1,store));

        ShoppingBasket shoppingBasket2= new ShoppingBasket(store.getStoreId());
        shoppingBasket2.addItem(tomato,1);
        assertFalse(tomatoAtLeastPurchaseRule.purchaseRuleOccurs(shoppingBasket2,store));
    }

    @Test
        //SimplePolicy, ItemPurchaseRule, AtMost
    void addSimplePolicyAtMostItemPurchaseRule() throws Exception {
        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato,10);
        ItemPurchaseRule tomatoPurchaseRule = new ItemPurchaseRule(tomato.getId());
        AtMostPurchaseRule tomatoAtLeastPurchaseRule= new AtMostPurchaseRule(tomatoPurchaseRule,2);
        store.addPolicyTermByStoreOwner(tomatoAtLeastPurchaseRule);
        assertFalse(tomatoAtLeastPurchaseRule.purchaseRuleOccurs(shoppingBasket1,store));
        ShoppingBasket shoppingBasket2= new ShoppingBasket(store.getStoreId());
        shoppingBasket2.addItem(tomato,1);
        assertTrue(tomatoAtLeastPurchaseRule.purchaseRuleOccurs(shoppingBasket2,store));

    }

    @Test
        //SimplePolicy, CategoryPurchaseRule, AtLeast
    void addSimplePolicyAtLeastCategoryPurchaseRule() throws Exception {

        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato,10);
        CategoryPurchaseRule vegetableCategoryPurchaseRule= new CategoryPurchaseRule(vegetable);
        AtLeastPurchaseRule vegetableAtLeastPurchaseRule= new AtLeastPurchaseRule(vegetableCategoryPurchaseRule,2);
        store.addPolicyTermByStoreOwner(vegetableAtLeastPurchaseRule);
        assertTrue(vegetableAtLeastPurchaseRule.purchaseRuleOccurs(shoppingBasket1,store));

        ShoppingBasket shoppingBasket2= new ShoppingBasket(store.getStoreId());
        shoppingBasket2.addItem(tomato,1);
        assertFalse(vegetableAtLeastPurchaseRule.purchaseRuleOccurs(shoppingBasket2,store));
    }

    @Test
        //SimplePolicy, CategoryPurchaseRule, AtMost
    void addSimplePolicyAtMostCategoryPurchaseRule() throws Exception {

        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato,10);
        CategoryPurchaseRule vegetableCategoryPurchaseRule= new CategoryPurchaseRule(vegetable);
        AtMostPurchaseRule vegetableAtMostPurchaseRule= new AtMostPurchaseRule(vegetableCategoryPurchaseRule,2);
        store.addPolicyTermByStoreOwner(vegetableAtMostPurchaseRule);
        assertFalse(vegetableAtMostPurchaseRule.purchaseRuleOccurs(shoppingBasket1,store));

        ShoppingBasket shoppingBasket2= new ShoppingBasket(store.getStoreId());
        shoppingBasket2.addItem(tomato,1);
        assertTrue(vegetableAtMostPurchaseRule.purchaseRuleOccurs(shoppingBasket2,store));
    }

    @Test
        //CompositePurchaseTermOr,
    void addCompositePolicyOr() throws Exception {
        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato,10);
        ShopingBasketPurchaseRule shopingBasketPurchaseRule = new ShopingBasketPurchaseRule();
        AtLeastPurchaseRule BasketAtLeastPurchaseRule= new AtLeastPurchaseRule(shopingBasketPurchaseRule,49);

        ItemPurchaseRule tomatoPurchaseRule = new ItemPurchaseRule(tomato.getId());
        AtLeastPurchaseRule tomatoAtLeastPurchaseRule= new AtLeastPurchaseRule(tomatoPurchaseRule,2);

        //false
        CategoryPurchaseRule vegetableCategoryPurchaseRule= new CategoryPurchaseRule(vegetable);
        AtMostPurchaseRule vegetableAtMostPurchaseRule= new AtMostPurchaseRule(vegetableCategoryPurchaseRule,2);

        ConcurrentLinkedQueue<PurchaseTerm> purchaseTerm = new ConcurrentLinkedQueue<>();
        purchaseTerm.add(BasketAtLeastPurchaseRule);
        purchaseTerm.add(tomatoAtLeastPurchaseRule);
        purchaseTerm.add(vegetableAtMostPurchaseRule);

        CompositePurchaseTermOr purchaseTermOr = new CompositePurchaseTermOr(shopingBasketPurchaseRule,purchaseTerm);
        store.addPolicyTermByStoreOwner(purchaseTermOr);
        assertTrue(purchaseTermOr.purchaseRuleOccurs(shoppingBasket1,store));

    }
@Test
    void addCompositePolicyAnd() throws Exception {
        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato,10);
        ShopingBasketPurchaseRule shopingBasketPurchaseRule = new ShopingBasketPurchaseRule();
        AtLeastPurchaseRule BasketAtLeastPurchaseRule= new AtLeastPurchaseRule(shopingBasketPurchaseRule,49);

        ItemPurchaseRule tomatoPurchaseRule = new ItemPurchaseRule(tomato.getId());
        AtLeastPurchaseRule tomatoAtLeastPurchaseRule= new AtLeastPurchaseRule(tomatoPurchaseRule,2);

        ConcurrentLinkedQueue<PurchaseTerm> purchaseTerm = new ConcurrentLinkedQueue<>();
        purchaseTerm.add(BasketAtLeastPurchaseRule);
        purchaseTerm.add(tomatoAtLeastPurchaseRule);

        CompositePurchaseTermAnd purchaseTermAnd = new CompositePurchaseTermAnd(shopingBasketPurchaseRule,purchaseTerm);
        store.addPolicyTermByStoreOwner(purchaseTermAnd);
        assertTrue(purchaseTermAnd.purchaseRuleOccurs(shoppingBasket1,store));


       CategoryPurchaseRule vegetableCategoryPurchaseRule= new CategoryPurchaseRule(vegetable);
       AtMostPurchaseRule vegetableAtMostPurchaseRule= new AtMostPurchaseRule(vegetableCategoryPurchaseRule,2);
       purchaseTerm.add(vegetableAtMostPurchaseRule);
       CompositePurchaseTermAnd purchaseTermAnd1 = new CompositePurchaseTermAnd(shopingBasketPurchaseRule,purchaseTerm);
       assertFalse(purchaseTermAnd1.purchaseRuleOccurs(shoppingBasket1,store));

    }










}
