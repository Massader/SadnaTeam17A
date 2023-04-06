package DomainLayer.Market.Stores;

import java.util.HashMap;
import java.util.UUID;

public class Store {
    private String name;
    private UUID storeID;
    private double rating;
    private boolean close;
    private boolean shutDown;
    private int ratingCounter;

    public Store(String name) {
        this.name = name;
        this.storeID= UUID.randomUUID();
        this.rating=-1;
        this.close=false;
        this.shutDown=false;
        this.ratingCounter=0;

    }


    public boolean addRating(int rating){
        double oldRating = this.rating*this.ratingCounter;
        this.ratingCounter+=1;
        this.rating= (oldRating+rating)/this.ratingCounter;
        return  true;

}
public  boolean closeStore(){
    if(this.close){
        throw new IllegalArgumentException("the Store :" + this.getName()+" is already close" );}
    else{
    this.close=true;
    return true;}
}

public  boolean reopenStore(){
        if(this.shutDown){
            throw new IllegalArgumentException("the Store : " + this.getName()+" is hutDown" );}
        else if (!this.close){
            throw new IllegalArgumentException("the Store : " + this.getName()+" is already open" );}
        else{
            this.close=false;
            return  true;
        }

    }




    //get +set function
    public boolean isClose() {
        return close;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public boolean isShutDown() {
        return shutDown;
    }

    public double getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }


    public int getRatingCounter() {
        return ratingCounter;
    }
}
