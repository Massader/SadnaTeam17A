package UnitTests;

import DomainLayer.Market.Stores.Category;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule.*;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;
import org.junit.jupiter.api.AfterAll;
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
    }

    


    //the case of SimplePolicy {ShoppingBasketPurchaseRule,ItemPurchaseRule,CategoryPurchaseRule}*{AtMost, AtLeast}
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
    void addPurchaseTermConditioning() throws Exception {
        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato,10);
        CategoryPurchaseRule vegetableCategoryPurchaseRule= new CategoryPurchaseRule(vegetable);
        AtLeastPurchaseRule vegetableAtLeastPurchaseRule= new AtLeastPurchaseRule(vegetableCategoryPurchaseRule,2);

        ItemPurchaseRule tomatoPurchaseRule = new ItemPurchaseRule(tomato.getId());
        AtLeastPurchaseRule tomatoAtLeastPurchaseRule= new AtLeastPurchaseRule(tomatoPurchaseRule,2);

        ConcurrentLinkedQueue<PurchaseTerm> purchaseTermThen = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<PurchaseTerm> purchaseTermIf = new ConcurrentLinkedQueue<>();
        purchaseTermIf.add(vegetableAtLeastPurchaseRule);
        purchaseTermThen.add(tomatoAtLeastPurchaseRule);

        PurchaseTermConditioning purchaseTermConditioning= new PurchaseTermConditioning(tomatoPurchaseRule,purchaseTermThen,purchaseTermIf);
        store.addPolicyTermByStoreOwner(purchaseTermConditioning);
        assertTrue(purchaseTermConditioning.purchaseRuleOccurs(shoppingBasket1,store));
            }

    @Test
    // addPurchaseTermConditioning
    void addPurchaseTermConditioningFail() throws Exception {
        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato,10);
        CategoryPurchaseRule vegetableCategoryPurchaseRule= new CategoryPurchaseRule(vegetable);
        AtLeastPurchaseRule vegetableAtLeastPurchaseRule= new AtLeastPurchaseRule(vegetableCategoryPurchaseRule,2);

        ItemPurchaseRule tomatoPurchaseRule = new ItemPurchaseRule(tomato.getId());
        AtMostPurchaseRule tomatoAtMostPurchaseRule= new AtMostPurchaseRule(tomatoPurchaseRule,2);

        ConcurrentLinkedQueue<PurchaseTerm> purchaseTermThen = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<PurchaseTerm> purchaseTermIf = new ConcurrentLinkedQueue<>();
        purchaseTermIf.add(vegetableAtLeastPurchaseRule);
        purchaseTermThen.add(tomatoAtMostPurchaseRule);

        PurchaseTermConditioning purchaseTermConditioning= new PurchaseTermConditioning(tomatoPurchaseRule,purchaseTermThen,purchaseTermIf);
        store.addPolicyTermByStoreOwner(purchaseTermConditioning);
        assertTrue(purchaseTermConditioning.purchaseRuleOccurs(shoppingBasket1,store));
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
    void addCompositePolicyOrFail() throws Exception {
        //False
        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato,10);
        ShopingBasketPurchaseRule shopingBasketPurchaseRule = new ShopingBasketPurchaseRule();
        AtLeastPurchaseRule BasketAtLeastPurchaseRule= new AtLeastPurchaseRule(shopingBasketPurchaseRule,60);

        //false
        CategoryPurchaseRule vegetableCategoryPurchaseRule= new CategoryPurchaseRule(vegetable);
        AtMostPurchaseRule vegetableAtMostPurchaseRule= new AtMostPurchaseRule(vegetableCategoryPurchaseRule,2);

        ConcurrentLinkedQueue<PurchaseTerm> purchaseTerm = new ConcurrentLinkedQueue<>();
        purchaseTerm.add(BasketAtLeastPurchaseRule);
        purchaseTerm.add(vegetableAtMostPurchaseRule);

        CompositePurchaseTermOr purchaseTermOr = new CompositePurchaseTermOr(shopingBasketPurchaseRule,purchaseTerm);
        store.addPolicyTermByStoreOwner(purchaseTermOr);
        assertFalse(purchaseTermOr.purchaseRuleOccurs(shoppingBasket1,store));

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


    @Test
    void addStorePurchasePolicy() throws Exception {
        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato,10);

        ShopingBasketPurchaseRule shopingBasketPurchaseRule = new ShopingBasketPurchaseRule();
        AtLeastPurchaseRule BasketAtLeastPurchaseRule= new AtLeastPurchaseRule(shopingBasketPurchaseRule,49);
        store.addPolicyTermByStoreOwner(BasketAtLeastPurchaseRule);

        ItemPurchaseRule tomatoPurchaseRule = new ItemPurchaseRule(tomato.getId());
        AtLeastPurchaseRule tomatoAtLeastPurchaseRule= new AtLeastPurchaseRule(tomatoPurchaseRule,2);
        store.addPolicyTermByStoreOwner(tomatoAtLeastPurchaseRule);

        assertTrue(store.purchaseRuleOccurs(shoppingBasket1));

        CategoryPurchaseRule vegetableCategoryPurchaseRule= new CategoryPurchaseRule(vegetable);
        AtMostPurchaseRule vegetableAtMostPurchaseRule= new AtMostPurchaseRule(vegetableCategoryPurchaseRule,2);
        store.addPolicyTermByStoreOwner(vegetableAtMostPurchaseRule);
        assertFalse(store.purchaseRuleOccurs(shoppingBasket1));

        store.removePolicyTermByStoreOwner(vegetableAtMostPurchaseRule);
        assertTrue(store.purchaseRuleOccurs(shoppingBasket1));

    }


    @Test
    void addStorePurchasePolicyWithDuplicate() throws Exception {
        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato,10);
        ShopingBasketPurchaseRule shopingBasketPurchaseRule = new ShopingBasketPurchaseRule();
        AtLeastPurchaseRule BasketAtLeastPurchaseRule= new AtLeastPurchaseRule(shopingBasketPurchaseRule,49);

        ItemPurchaseRule tomatoPurchaseRule = new ItemPurchaseRule(tomato.getId());
        AtLeastPurchaseRule tomatoAtLeastPurchaseRule= new AtLeastPurchaseRule(tomatoPurchaseRule,2);

        ConcurrentLinkedQueue<PurchaseTerm> purchaseTerm = new ConcurrentLinkedQueue<>();
        purchaseTerm.add(BasketAtLeastPurchaseRule);
        purchaseTerm.add(tomatoAtLeastPurchaseRule);
        try {
            store.addPolicyTermByStoreOwner(null); }
        catch (Exception exception){
            assertEquals("the purchase Term is null, please put valid purchaseTerm",exception.getMessage());
        }

        CompositePurchaseTermAnd purchaseTermAnd = new CompositePurchaseTermAnd(shopingBasketPurchaseRule,purchaseTerm);
        store.addPolicyTermByStoreOwner(tomatoAtLeastPurchaseRule);
        try{
            store.addPolicyTermByStoreOwner(tomatoAtLeastPurchaseRule);
        }
        catch (Exception exception){
            assertEquals("the purchase Term is already exist, please put valid purchaseTerm",exception.getMessage());
        }
       store.addPolicyTermByStoreOwner(purchaseTermAnd);
        for (PurchaseTerm policy : store.getPolicy().getPurchasePolicies()) {
            System.out.println(policy);
        }
        assertFalse(store.getPolicy().getPurchasePolicies().contains(tomatoAtLeastPurchaseRule));

    }

















}
