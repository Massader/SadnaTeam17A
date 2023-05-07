package APILayer.Requests;

import java.util.UUID;

public class PurchaseCartRequest extends Request {
    private double expectedPrice;
    private String address;
    private String credit;

    public PurchaseCartRequest(UUID clientCredentials, double expectedPrice, String address, String credit) {
        super(clientCredentials);
        this.expectedPrice = expectedPrice;
        this.address = address;
        this.credit = credit;
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

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }
}
