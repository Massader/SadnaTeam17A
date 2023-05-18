package APILayer.Requests;

import DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule.CompositePurchaseTermAnd;
import DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule.CompositePurchaseTermOr;
import DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule.PurchaseTerm;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CompositePolicy extends Request{
    private Boolean and;
    private ConcurrentLinkedQueue<SimplePolicy> policies;


    public CompositePolicy(UUID clientCredentials, Boolean and, ConcurrentLinkedQueue<SimplePolicy> policys) {
        super(clientCredentials);
        this.and = and;
        this.policies = policys;
    }

    public Boolean getAnd() {
        return and;
    }

    public ConcurrentLinkedQueue<SimplePolicy> getPolicies() {
        return policies;
    }

    public PurchaseTerm getPurchaseTerm(){
        ConcurrentLinkedQueue<PurchaseTerm> purchaseTerms = new ConcurrentLinkedQueue<>();
        for (SimplePolicy policy:getPolicies()) {
            purchaseTerms.add(policy.getPurchaseTerm());
        }
        if(getAnd()){return new CompositePurchaseTermAnd(purchaseTerms.poll().getPurchaseRule(),purchaseTerms);
        }
        else return new CompositePurchaseTermOr(purchaseTerms.poll().getPurchaseRule(),purchaseTerms);
    }
}
