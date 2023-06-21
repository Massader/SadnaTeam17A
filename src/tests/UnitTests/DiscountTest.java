package UnitTests;

import DomainLayer.Market.Stores.Discounts.CalculateDiscount;
import DomainLayer.Market.Stores.Discounts.Discount;
import DomainLayer.Market.Stores.Discounts.ItemCalculateDiscount;
import DomainLayer.Market.Stores.Discounts.ShoppingBasketCalculateDiscount;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.PurchaseRule.AtLeastPurchaseTerm;
import DomainLayer.Market.Stores.PurchaseRule.AtMostPurchaseTerm;
import DomainLayer.Market.Stores.PurchaseRule.PurchaseTerm;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.CartItem;
import DomainLayer.Market.Users.ShoppingBasket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DiscountTest {

    @Test
    public void testCalculateDiscountWithNoPurchaseTerm() {
        // Create a discount with no purchase term
        CalculateDiscount calculateDiscount = new ItemCalculateDiscount();
        double discountPercentage = 0.2;
        Discount discount = null;
        try {
            discount = new Discount(calculateDiscount, discountPercentage, null);
        } catch (Exception e) {
            Assertions.fail("Exception occurred: " + e.getMessage());
        }

        // Create a shopping basket and store
        ShoppingBasket shoppingBasket = new ShoppingBasket();
        Store store = new Store();

        // Calculate the discount
        double calculatedDiscount = discount.calculateDiscount(shoppingBasket, store);

        // Ensure the calculated discount is correct
        double expectedDiscount = 0.0; // No discount applied
        Assertions.assertEquals(expectedDiscount, calculatedDiscount);
    }

    @Test
    public void testCalculateDiscountWithPurchaseTerm() {
        // Create a discount with purchase term
        CalculateDiscount calculateDiscount = new ShoppingBasketCalculateDiscount();
        double discountPercentage = 0.1;
        PurchaseTerm purchaseTerm = new AtLeastPurchaseTerm( ); // At least 3 items in the basket
        Discount discount = null;
        try {
            discount = new Discount(calculateDiscount, discountPercentage, purchaseTerm);
        } catch (Exception e) {
            Assertions.fail("Exception occurred: " + e.getMessage());
        }

        // Create a shopping basket and store
        ShoppingBasket shoppingBasket = new ShoppingBasket();
        Store store = new Store();

        // Add items to the shopping basket
        try {
            shoppingBasket.addItem(new CartItem(), 2);
            shoppingBasket.addItem(new CartItem(), 2);
            shoppingBasket.addItem(new CartItem(), 2);
        }catch(Exception e){}

        // Calculate the discount
        double calculatedDiscount = discount.calculateDiscount(shoppingBasket, store);

        // Ensure the calculated discount is correct
        double expectedDiscount = 0.0; // Discount not met (3 items required)
        Assertions.assertEquals(expectedDiscount, calculatedDiscount);
    }

    @Test
    public void testInvalidDiscountCreation() {
        // Attempt to create a discount with invalid parameters
        CalculateDiscount calculateDiscount = null; // Null calculate discount
        double discountPercentage = 1.5; // Invalid discount percentage
        PurchaseTerm purchaseTerm = new AtMostPurchaseTerm(); // At most 5 items allowed

        // Ensure the discount creation throws an exception
        Assertions.assertThrows(Exception.class, () -> new Discount(calculateDiscount, discountPercentage, purchaseTerm));
    }
}
