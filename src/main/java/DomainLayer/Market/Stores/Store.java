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
    public boolean closeStore(){
        checkNotShutDown();
        checkNotClose();
        this.close=true;
        return true;}


    public  boolean reopenStore(){
        checkNotShutDown();
        if (!this.close){
            throw new IllegalArgumentException("the Store :" + this.getName()+" is already open" );}
        else{
            this.close=false;
            return  true;
        }

    }

    public Boolean ShutDown() {
         this.shutDown = true;
        return  true;
    }

    private void checkNotShutDown(){
        if(isShutDown()){
            throw new IllegalArgumentException("the Store :" + this.getName()+" is already shutDown" );}
    }
    private void checkNotClose(){
        if(isClose()){
            throw new IllegalArgumentException("the Store :" + this.getName()+" is already close" );}
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
