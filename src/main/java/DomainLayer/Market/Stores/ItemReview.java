package DomainLayer.Market.Stores;

import jakarta.persistence.*;

import java.util.Date;
import java.time.Instant;
import java.util.UUID;
@Entity
@Table(name = "Stores_ItemReview")
public class ItemReview {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ItemReviewId", nullable = false, unique = true)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @Column
    private String text;
    @Column
    private UUID reviewer;
    @Column
    private Date timestamp;
    @Column
    private int rating;

    public ItemReview(Item item, String text, UUID reviewer, int rating) {
        this.id = UUID.randomUUID();
        this.item=item;
        this.text = text;
        this.reviewer = reviewer;
        this.timestamp = Date.from(Instant.now());
        this.rating = rating;
    }
    public ItemReview() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UUID getReviewer() {
        return reviewer;
    }

    public void setReviewer(UUID reviewer) {
        this.reviewer = reviewer;
    }
    
    public Item getItem() {
        return item;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public int getRating() {
        return rating;
    }
    
    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
