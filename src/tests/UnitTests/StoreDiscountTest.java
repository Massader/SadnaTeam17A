package UnitTests;

import DomainLayer.Market.Stores.Category;
import DomainLayer.Market.Stores.Discounts.condition.CategoryCalculateDiscount;
import DomainLayer.Market.Stores.Discounts.condition.Discount;
import DomainLayer.Market.Stores.Discounts.condition.ItemCalculateDiscount;
import DomainLayer.Market.Stores.Discounts.condition.ShopingBasketCalculateDiscount;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.PurchaseRule.AtLeastPurchaseTerm;
import DomainLayer.Market.Stores.PurchaseRule.ShoppingBasketPurchaseRule;
import DomainLayer.Market.Stores.PurchaseTypes.*;
import DomainLayer.Market.Stores.PurchaseRule.*;
import DomainLayer.Market.Stores.PurchaseRule.PurchaseTerm;
//import DomainLayer.Market.Stores.PurchaseRule.ShopingBasketPurchaseRule;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

public class StoreDiscountTest {
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

    @Test
    //add item  Discount without term
    void addItemDiscount() throws Exception {
        ItemCalculateDiscount itemDiscount = new ItemCalculateDiscount(tomato.getId());
        Discount discount = new Discount(itemDiscount,0.5,null);
        store.addDiscountByStoreOwner(discount);
        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato,10);
        Double price =store.calculatePriceOfBasketWithPolicyAndDiscount(shoppingBasket1);
        assertEquals(price,25);
    }
    @Test
    void addCategoryDiscount() throws Exception {
        CategoryCalculateDiscount calculateDiscount= new CategoryCalculateDiscount(vegetable);
        Discount discount = new Discount(calculateDiscount,0.5,null);
        store.addDiscountByStoreOwner(discount);
        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato,10);
        Double price =store.calculatePriceOfBasketWithPolicyAndDiscount(shoppingBasket1);
        assertEquals(25,price);
    }

    @Test
    void addShoppingBasketDiscount() throws Exception {
        ShopingBasketCalculateDiscount shoppingBasketDiscount = new ShopingBasketCalculateDiscount();
        Discount discount = new Discount(shoppingBasketDiscount,0.5,null);
        store.addDiscountByStoreOwner(discount);
        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato,10);
        Double price =store.calculatePriceOfBasketWithPolicyAndDiscount(shoppingBasket1);
        assertEquals(price,25);
    }

      @Test
        //add item  Discount without term
    void addItemDiscountWithUnValidDiscount() throws Exception {
        ItemCalculateDiscount itemDiscount = new ItemCalculateDiscount(tomato.getId());
        try{
        Discount discount = new Discount(itemDiscount,2.0,null);}
        catch (Exception exception){
            assertEquals("discount Percentage have to be between 0 to 1",exception.getMessage());
        }
        try{
            Discount discount = new Discount(itemDiscount,-1.0,null);}
        catch (Exception exception){
            assertEquals("discount Percentage have to be between 0 to 1",exception.getMessage());
        }
        try{
            Discount discount = new Discount(itemDiscount,1.0,null);}
        catch (Exception exception){
            assertEquals("discount Percentage have to be between 0 to 1",exception.getMessage());
        }
        try{
            Discount discount = new Discount(itemDiscount,0.0,null);}
        catch (Exception exception){
            assertEquals("discount Percentage have to be between 0 to 1",exception.getMessage());
        }

    }
@Test
    void addItemDiscountWithCondition() throws Exception {
    ItemCalculateDiscount itemDiscount = new ItemCalculateDiscount(tomato.getId());
    ShoppingBasketPurchaseRule shopingBasketPurchaseRule = new ShoppingBasketPurchaseRule();
    AtLeastPurchaseTerm BasketAtLeastPurchaseRule = new AtLeastPurchaseTerm(shopingBasketPurchaseRule, 49);
    Discount discount = new Discount(itemDiscount, 0.5, BasketAtLeastPurchaseRule);
    store.addDiscountByStoreOwner(discount);
    ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
    shoppingBasket1.addItem(tomato, 10);
    Double price = store.calculatePriceOfBasketWithPolicyAndDiscount(shoppingBasket1);
    assertEquals(25, price);
}
@Test
        void addItemDiscountWithFailCondition() throws Exception {
        ItemCalculateDiscount itemDiscount = new ItemCalculateDiscount(tomato.getId());
        ItemPurchaseRule tomatoPurchaseRule = new ItemPurchaseRule(tomato.getId());
    AtLeastPurchaseTerm tomatoAtLeastPurchaseRule= new AtLeastPurchaseTerm(tomatoPurchaseRule,20);
        Discount discount = new Discount(itemDiscount,0.5,tomatoAtLeastPurchaseRule);
       store.addDiscountByStoreOwner(discount);
       ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
       shoppingBasket1.addItem(tomato, 10);
       Double price = store.calculatePriceOfBasketWithPolicyAndDiscount(shoppingBasket1);
        assertEquals(50, price);
    }

    @Test
 void  MaxDiscounts() throws Exception {
        ItemCalculateDiscount itemDiscount = new ItemCalculateDiscount(tomato.getId());
        ShoppingBasketPurchaseRule shopingBasketPurchaseRule = new ShoppingBasketPurchaseRule();
        AtLeastPurchaseTerm BasketAtLeastPurchaseRule = new AtLeastPurchaseTerm(shopingBasketPurchaseRule, 49);
        Discount discount = new Discount(itemDiscount, 0.5, BasketAtLeastPurchaseRule);
        store.addDiscountByStoreOwner(discount);
        CategoryCalculateDiscount calculateDiscount= new CategoryCalculateDiscount(vegetable);
        Discount discount2 = new Discount(calculateDiscount,0.7,null);
        store.addDiscountByStoreOwner(discount2);
        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato, 10);
        Double price = store.calculatePriceOfBasketWithPolicyAndDiscount(shoppingBasket1);
        assertEquals(15, price);
    }

    @Test
    void CombiningDiscounts() throws Exception {
        store.getDiscounts().changeNumericalAssemblyOfDiscount();
        ItemCalculateDiscount itemDiscount = new ItemCalculateDiscount(tomato.getId());
        ShoppingBasketPurchaseRule shopingBasketPurchaseRule = new ShoppingBasketPurchaseRule();
        AtLeastPurchaseTerm BasketAtLeastPurchaseRule = new AtLeastPurchaseTerm(shopingBasketPurchaseRule, 49);
        Discount discount = new Discount(itemDiscount, 0.15, BasketAtLeastPurchaseRule);
        store.addDiscountByStoreOwner(discount);
        CategoryCalculateDiscount calculateDiscount= new CategoryCalculateDiscount(vegetable);
        Discount discount2 = new Discount(calculateDiscount,0.2,null);
        store.addDiscountByStoreOwner(discount2);
        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato, 10);
        Double price = store.calculatePriceOfBasketWithPolicyAndDiscount(shoppingBasket1);
        assertEquals(32.5, price);
    }

    @Test
    void CombiningDiscountsWithBigDiscount() throws Exception {
        store.getDiscounts().changeNumericalAssemblyOfDiscount();
        ItemCalculateDiscount itemDiscount = new ItemCalculateDiscount(tomato.getId());
        ShoppingBasketPurchaseRule shopingBasketPurchaseRule = new ShoppingBasketPurchaseRule();
        AtLeastPurchaseTerm BasketAtLeastPurchaseRule = new AtLeastPurchaseTerm(shopingBasketPurchaseRule, 49);
        Discount discount = new Discount(itemDiscount, 0.5, BasketAtLeastPurchaseRule);
        store.addDiscountByStoreOwner(discount);
        CategoryCalculateDiscount calculateDiscount= new CategoryCalculateDiscount(vegetable);
        Discount discount2 = new Discount(calculateDiscount,0.6,null);
        store.addDiscountByStoreOwner(discount2);
        ShoppingBasket shoppingBasket1 = new ShoppingBasket(store.getStoreId());
        shoppingBasket1.addItem(tomato, 10);
        Double price = store.calculatePriceOfBasketWithPolicyAndDiscount(shoppingBasket1);
        assertEquals(0.0, price);
    }


    @Test
    void addTheSameDiscount() throws Exception {
        store.getDiscounts().changeNumericalAssemblyOfDiscount();
        ItemCalculateDiscount itemDiscount = new ItemCalculateDiscount(tomato.getId());
        ShoppingBasketPurchaseRule shopingBasketPurchaseRule = new ShoppingBasketPurchaseRule();
        AtLeastPurchaseTerm BasketAtLeastPurchaseRule = new AtLeastPurchaseTerm(shopingBasketPurchaseRule, 49);
        Discount discount = new Discount(itemDiscount, 0.5, BasketAtLeastPurchaseRule);
        store.addDiscountByStoreOwner(discount);
        try{
            Discount discount2 = new Discount(itemDiscount, 0.6, null);
            store.addDiscountByStoreOwner(discount2);
        }
        catch (Exception exception){
            assertEquals("the discount is already exist, please put valid discount",exception.getMessage());
        }
    }

    @Test
    void UnValidDiscount() throws Exception {
        try {
            Discount discount = new Discount(null, 0.5, null);
            store.addDiscountByStoreOwner(discount);
        }catch (Exception exception){
            assertEquals("the discount is null, please put valid discount",exception.getMessage());
        }

        ItemCalculateDiscount itemDiscount = new ItemCalculateDiscount(tomato.getId());
        ShoppingBasketPurchaseRule shopingBasketPurchaseRule = new ShoppingBasketPurchaseRule();
        AtLeastPurchaseTerm BasketAtLeastPurchaseRule = new AtLeastPurchaseTerm(shopingBasketPurchaseRule, 49);
        Discount discount = new Discount(itemDiscount, 0.5, BasketAtLeastPurchaseRule);
        store.addDiscountByStoreOwner(discount);
        try{store.addDiscountByStoreOwner(discount);}
        catch (Exception exception){
            assertEquals("the discount is already exist, please put valid discount",exception.getMessage());

        }


    }


    }










