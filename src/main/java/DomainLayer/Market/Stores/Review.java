package DomainLayer.Market.Stores;

import java.util.UUID;

public class Review {
    private UUID id;
    private String text;
    private UUID reviewer;

    public Review(UUID id, String text, UUID reviewer) {
        this.id = id;
        this.text = text;
        this.reviewer = reviewer;
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
}
