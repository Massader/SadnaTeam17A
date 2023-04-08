package DomainLayer.Market.Stores;

import DomainLayer.Market.Users.Roles.StoreOwner;

public class PolicyRules {
    private int minAmount;
    private int maxAmount;

//constructor:

    public PolicyRules(int minAmount, int maxAmount, StoreOwner creator) {
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.creator = creator;
    }

    //getters & setters :

    public int getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public StoreOwner getCreator() {
        return creator;
    }

    public void setCreator(StoreOwner creator) {
        this.creator = creator;
    }

    private StoreOwner creator;


    //methods:


}
