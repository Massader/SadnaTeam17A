package APILayer.Requests;

import java.util.UUID;

public class PurchaseCartRequest extends Request {
    private double expectedPrice;
    private String address;
    private int credit;

    public PurchaseCartRequest(UUID clientCredentials, double expectedPrice, String address, int credit) {
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

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
