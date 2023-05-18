package APILayer.Requests;

import DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule.CompositePurchaseTermAnd;
import DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule.CompositePurchaseTermOr;
import DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule.PurchaseTerm;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CompositePolicyRequest extends Request{
    private Boolean and;
    private ConcurrentLinkedQueue<SimplePolicyRequest> policies;


    public CompositePolicyRequest(UUID clientCredentials, Boolean and, ConcurrentLinkedQueue<SimplePolicyRequest> policys) {
        super(clientCredentials);
        this.and = and;
        this.policies = policys;
    }

    public Boolean getAnd() {
        return and;
    }

    public ConcurrentLinkedQueue<SimplePolicyRequest> getPolicies() {
        return policies;
    }

    public PurchaseTerm getPurchaseTerm(){
        ConcurrentLinkedQueue<PurchaseTerm> purchaseTerms = new ConcurrentLinkedQueue<>();
        for (SimplePolicyRequest policy:getPolicies()) {
            purchaseTerms.add(policy.getPurchaseTerm());
        }
        if(getAnd()){return new CompositePurchaseTermAnd(purchaseTerms.poll().getPurchaseRule(),purchaseTerms);
        }
        else return new CompositePurchaseTermOr(purchaseTerms.poll().getPurchaseRule(),purchaseTerms);
    }
}
