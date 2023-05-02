package APILayer.Requests;

import java.util.UUID;

public class GetPartOfStoresRequest extends Request {
    private int number;
    private int page;


    public GetPartOfStoresRequest(UUID clientCredentials, int number, int page) {
        super(clientCredentials);
        this.number = number;
        this.page = page;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
