package DomainLayer.Market.Stores;

import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "Stores_Category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    @Column(unique = true)
    private String categoryName;

    public Category(String name){
        this.categoryName = name;
    }

    public Category(){}

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

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
