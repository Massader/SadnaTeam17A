package DomainLayer.Market.Users.Roles;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Admin extends Role{
    private ConcurrentLinkedQueue<UUID> appointmentOFStoreOwner;
    private ConcurrentLinkedQueue<UUID> appointmentOFStoreManager;
}
