package UnitTests;

import DomainLayer.Market.Stores.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class CategoryTest {

    @Test
    public void testGetCategoryName() {
        // Create a category
        String categoryName = "Electronics";
        Category category = new Category(categoryName);

        // Ensure the category name is retrieved correctly
        Assertions.assertEquals(categoryName, category.getCategoryName());
    }

    @Test
    public void testEquals() {
        // Create categories with the same name
        String categoryName = "Electronics";
        Category category1 = new Category(categoryName);
        Category category2 = new Category(categoryName);

        // Ensure the categories are considered equal
        Assertions.assertEquals(category1, category2);
    }

    @Test
    public void testEquals_SameInstance() {
        // Create a category
        String categoryName = "Electronics";
        Category category = new Category(categoryName);

        // Ensure the category is considered equal to itself
        Assertions.assertEquals(category, category);
    }

    @Test
    public void testEquals_NullObject() {
        // Create a category
        String categoryName = "Electronics";
        Category category = new Category(categoryName);

        // Ensure the category is not equal to null
        Assertions.assertNotEquals(category, null);
    }

    @Test
    public void testEquals_DifferentClass() {
        // Create a category
        String categoryName = "Electronics";
        Category category = new Category(categoryName);

        // Ensure the category is not equal to an object of a different class
        Assertions.assertNotEquals(category, "Electronics");
    }

    @Test
    public void testSetCategoryName() {
        // Create a category
        String categoryName = "Electronics";
        Category category = new Category(categoryName);

        // Update the category name
        String newCategoryName = "Appliances";
        category.setCategoryName(newCategoryName);

        // Ensure the category name is updated correctly
        Assertions.assertEquals(newCategoryName, category.getCategoryName());
    }

    @Test
    public void testSetId() {
        // Create a category
        String categoryName = "Electronics";
        Category category = new Category(categoryName);

        // Update the ID
        UUID newId = UUID.randomUUID();
        category.setId(newId);
    }
}
        // Ensure the ID is updated
