package DomainLayer.Market.Users;

import java.util.Date;
import java.util.UUID;

public class Purchase {
    UUID purchaseId;
    Date date;


    public Purchase() {
        this.purchaseId = UUID.randomUUID();
        this.date= new Date();
    }
}
