package DomainLayer.Market.Stores;

import java.util.Objects;

public class Category {


    private String categoryName;

    public Category(String name){
        this.categoryName = name;
    }
    public String getCategoryName() {
        return categoryName;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(categoryName, category.categoryName);
    }
}
