//package DomainLayer.Market.Stores.Discounts.condition;
//
//import DomainLayer.Market.Stores.Store;
//import DomainLayer.Market.Users.ShoppingBasket;
//
//import java.util.concurrent.ConcurrentLinkedQueue;
//
//public class CompositeCalculateDiscountAnd extends CompositeCalculateDiscount {
//    public CompositeCalculateDiscountAnd(CalculateDiscount calculateDiscount) {
//        super(calculateDiscount);
//
//
//    }
//
//    @Override
//    public Double CalculateDiscount(ShoppingBasket shoppingBasket, Store store, Double discountPercentage) {
//
//    }
//}
//
////    private ConcurrentLinkedQueue<CalculateDiscount> calculateDiscounts;
////    @Override
////    public Boolean conditionOccurs(ShoppingBasket shoppingBasket, Store store) {
////        for (CalculateDiscount con: calculateDiscounts) {
////            if(!con.conditionOccurs(shoppingBasket,store)){
////                return false;}
////        }
////        return  true;
////        }
////    }
//
