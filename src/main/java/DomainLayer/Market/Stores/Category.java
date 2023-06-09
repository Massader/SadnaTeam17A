package DomainLayer.Market.Stores;

import jakarta.persistence.*;

@Entity
@Table(name = "Categorys")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column
    private String categoryName;

    public Category(String name){
        this.categoryName = name;
    }
    public Category(){}
    public String getCategoryName(){return categoryName;}
}
