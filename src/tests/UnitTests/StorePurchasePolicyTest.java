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

        ShoppingBasketPurchaseRule shoppingBasketPurchaseRule = new ShoppingBasketPurchaseRule();
        AtLeastPurchaseTerm basketAtLeastPurchaseTerm = new AtLeastPurchaseTerm(shoppingBasketPurchaseRule,49);
        store.addPolicyTermByStoreOwner(basketAtLeastPurchaseTerm);

        assertTrue(basketAtLeastPurchaseTerm.purchaseRuleOccurs(shoppingBasket1,store));

        ShoppingBasket shoppingBasket2= new ShoppingBasket(store.getStoreId());
        shoppingBasket2.addItem(tomato,1);
        assertFalse(basketAtLeastPurchaseTerm.purchaseRuleOccurs(shoppingBasket2,store));
    }

    @Test
        //SimplePolicy, ShoppingBasketPurchaseRule, AtMost
    void addSimplePolicyAtMostShoppingBasketPurchaseRule() throws Exception {
        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato,10);

        ShoppingBasketPurchaseRule shoppingBasketPurchaseRule = new ShoppingBasketPurchaseRule();
        AtMostPurchaseTerm BasketAtLeastPurchaseRule= new AtMostPurchaseTerm(shoppingBasketPurchaseRule,49);
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
        AtLeastPurchaseTerm tomatoAtLeastPurchaseTerm = new AtLeastPurchaseTerm(tomatoPurchaseRule,2);
        store.addPolicyTermByStoreOwner(tomatoAtLeastPurchaseTerm);
        assertTrue(tomatoAtLeastPurchaseTerm.purchaseRuleOccurs(shoppingBasket1,store));

        ShoppingBasket shoppingBasket2= new ShoppingBasket(store.getStoreId());
        shoppingBasket2.addItem(tomato,1);
        assertFalse(tomatoAtLeastPurchaseTerm.purchaseRuleOccurs(shoppingBasket2,store));
    }

    @Test
        //SimplePolicy, ItemPurchaseRule, AtMost
    void addSimplePolicyAtMostItemPurchaseRule() throws Exception {
        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato,10);
        ItemPurchaseRule tomatoPurchaseRule = new ItemPurchaseRule(tomato.getId());
        AtMostPurchaseTerm tomatoAtLeastPurchaseRule= new AtMostPurchaseTerm(tomatoPurchaseRule,2);
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
        AtLeastPurchaseTerm vegetableAtLeastPurchaseTerm = new AtLeastPurchaseTerm(vegetableCategoryPurchaseRule,2);
        store.addPolicyTermByStoreOwner(vegetableAtLeastPurchaseTerm);
        assertTrue(vegetableAtLeastPurchaseTerm.purchaseRuleOccurs(shoppingBasket1,store));

        ShoppingBasket shoppingBasket2= new ShoppingBasket(store.getStoreId());
        shoppingBasket2.addItem(tomato,1);
        assertFalse(vegetableAtLeastPurchaseTerm.purchaseRuleOccurs(shoppingBasket2,store));
    }

    @Test
        //SimplePolicy, CategoryPurchaseRule, AtMost
    void addSimplePolicyAtMostCategoryPurchaseRule() throws Exception {

        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato,10);
        CategoryPurchaseRule vegetableCategoryPurchaseRule= new CategoryPurchaseRule(vegetable);
        AtMostPurchaseTerm vegetableAtMostPurchaseTerm = new AtMostPurchaseTerm(vegetableCategoryPurchaseRule,2);
        store.addPolicyTermByStoreOwner(vegetableAtMostPurchaseTerm);
        assertFalse(vegetableAtMostPurchaseTerm.purchaseRuleOccurs(shoppingBasket1,store));

        ShoppingBasket shoppingBasket2= new ShoppingBasket(store.getStoreId());
        shoppingBasket2.addItem(tomato,1);
        assertTrue(vegetableAtMostPurchaseTerm.purchaseRuleOccurs(shoppingBasket2,store));
    }

    @Test
        //CompositePurchaseTermOr,
    void addCompositePolicyOr() throws Exception {
        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato,10);
        ShoppingBasketPurchaseRule shoppingBasketPurchaseRule = new ShoppingBasketPurchaseRule();
        AtLeastPurchaseTerm basketAtLeastPurchaseTerm = new AtLeastPurchaseTerm(shoppingBasketPurchaseRule,49);

        ItemPurchaseRule tomatoPurchaseRule = new ItemPurchaseRule(tomato.getId());
        AtLeastPurchaseTerm tomatoAtLeastPurchaseTerm = new AtLeastPurchaseTerm(tomatoPurchaseRule,2);

        //false
        CategoryPurchaseRule vegetableCategoryPurchaseRule= new CategoryPurchaseRule(vegetable);
        AtMostPurchaseTerm vegetableAtMostPurchaseTerm = new AtMostPurchaseTerm(vegetableCategoryPurchaseRule,2);

        ConcurrentLinkedQueue<PurchaseTerm> purchaseTerm = new ConcurrentLinkedQueue<>();
        purchaseTerm.add(basketAtLeastPurchaseTerm);
        purchaseTerm.add(tomatoAtLeastPurchaseTerm);
        purchaseTerm.add(vegetableAtMostPurchaseTerm);

        CompositePurchaseTermOr purchaseTermOr = new CompositePurchaseTermOr(shoppingBasketPurchaseRule,purchaseTerm);
        store.addPolicyTermByStoreOwner(purchaseTermOr);
        assertTrue(purchaseTermOr.purchaseRuleOccurs(shoppingBasket1,store));

    }
@Test
    void addCompositePolicyAnd() throws Exception {
        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato,10);
        ShoppingBasketPurchaseRule shoppingBasketPurchaseRule = new ShoppingBasketPurchaseRule();
        AtLeastPurchaseTerm basketAtLeastPurchaseTerm = new AtLeastPurchaseTerm(shoppingBasketPurchaseRule,49);

        ItemPurchaseRule tomatoPurchaseRule = new ItemPurchaseRule(tomato.getId());
        AtLeastPurchaseTerm tomatoAtLeastPurchaseTerm = new AtLeastPurchaseTerm(tomatoPurchaseRule,2);

        ConcurrentLinkedQueue<PurchaseTerm> purchaseTerm = new ConcurrentLinkedQueue<>();
        purchaseTerm.add(basketAtLeastPurchaseTerm);
        purchaseTerm.add(tomatoAtLeastPurchaseTerm);

        CompositePurchaseTermAnd purchaseTermAnd = new CompositePurchaseTermAnd(shoppingBasketPurchaseRule,purchaseTerm);
        store.addPolicyTermByStoreOwner(purchaseTermAnd);
        assertTrue(purchaseTermAnd.purchaseRuleOccurs(shoppingBasket1,store));


       CategoryPurchaseRule vegetableCategoryPurchaseRule= new CategoryPurchaseRule(vegetable);
       AtMostPurchaseTerm vegetableAtMostPurchaseTerm = new AtMostPurchaseTerm(vegetableCategoryPurchaseRule,2);
       purchaseTerm.add(vegetableAtMostPurchaseTerm);
       CompositePurchaseTermAnd purchaseTermAnd1 = new CompositePurchaseTermAnd(shoppingBasketPurchaseRule,purchaseTerm);
       assertFalse(purchaseTermAnd1.purchaseRuleOccurs(shoppingBasket1,store));

    }










}
