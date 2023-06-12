package APILayer.Requests;

import java.util.UUID;

public class PurchaseCartRequest extends Request {
    private double expectedPrice;
    private String address;
    private String city;
    private String country;
    private int zip;
    private String cardNumber;
    private String month;
    private String year;
    private String holder;
    private String ccv;
    private String idCard;


    public PurchaseCartRequest(UUID clientCredentials, double expectedPrice, String address, String city, String country, int zip, String cardNumber, String month, String year, String holder, String ccv, String idCard) {
        super(clientCredentials);
        this.expectedPrice = expectedPrice;
        this.address = address;
        this.city = city;
        this.country = country;
        this.zip = zip;
        this.cardNumber = cardNumber;
        this.month = month;
        this.year = year;
        this.holder = holder;
        this.ccv = ccv;
        this.idCard = idCard;
    }

    public double getExpectedPrice() {
        return expectedPrice;
    }

    public void setExpectedPrice(double expectedPrice) {
        this.expectedPrice = expectedPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public String getCcv() {
        return ccv;
    }

    public void setCcv(String ccv) {
        this.ccv = ccv;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
}
