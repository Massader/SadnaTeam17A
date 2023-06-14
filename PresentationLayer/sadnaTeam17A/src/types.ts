export interface Store {
  name: string;
  storeId: string;
  rating: number;
  description: string;
  isClosed: boolean;
  isShutdown: boolean;
  //   rolesMap: Map<number, Role>;
}

export interface Item {
  id: string;
  name: string;
  price: number;
  storeId: string;
  rating: number;
  quantity: number;
  description: string;
  categories: Category[];
  purchaseType: string;

}

export interface CartItemType {
  item: Item;
  quantity: number;
  price: number;

}

// export interface PurchaseType {
//   DIRECT_PURCHASE: string;
//   BID_PURCHASE: string;
// }

export interface PurchasedItemType {
  date: Date;
  id: string;
  itemId: string;
  quantity: number;
  rated: boolean;
  userId: string;
  storeId: string;
}

export interface SoldItemType {
  date: Date;
  id: string;
  itemId: string;
  quantity: number;
  userId: string;
  storeId: string;
}

export interface Category {
  categoryName: string;
}

export interface User {
  username: string;
  id: string;
  roles: Map<string, Role[]>;
  // purchases: Purchase[];
  isAdmin: boolean;
}

export interface Basket {
  // items: Map<string, number>;
  items: Map<string, CartItemType>;
  storeId: string;
}

export interface Role {
  permissions: string[];
  storeId: string;
}

export interface NotificationType {
  message: string;
  id: string;
  timestamp: Date;
}

export interface MessageType {
  body: string;
  id: string;
  recipient: string;
  sender: string;
}

export interface ReviewType {
  id: string;
  itemId: string;
  reviewer: string;
  text: string;
  rating: number;
  timestamp: Date;
}

export interface ComplaintType {
  assignedAdmin: string;
  body: string;
  id: string;
  itemId: string;
  open: boolean;
  purchaseId: string;
  sender: string;
  storeId: string;
}

export interface DiscountType {
  id?: string;
  type: string; //CATEGORY ITEM BASKET
  itemIdOrCategoryOrNull?: string;
  discountPercentage: number;
  purchaseTerm?: PurchaseTermType;
}

export interface PurchaseRuleType {
  type: string; //CATEGORY ITEM BASKET
  itemIdOrCategoryOrNull?: string; //
}

export interface PurchaseTermType {
  id?: number;
  rule: PurchaseRuleType;
  atLeast: boolean;
  quantity: number;
}

export interface CompositePurchaseTermType {
  purchaseTerms: PurchaseTermType[];
  type: string; //AND OR XOR
}

export interface ConditionalPurchaseTermType {
  ifPurchaseTerm: PurchaseTermType;
  thenPurchaseTerm: PurchaseTermType;
}

export interface AllPurchaseTermType {
  termId: string;
  termType: string;
  rule?: PurchaseRuleType;
  atLeast?: boolean;
  quantity?: number;
  purchaseTerms?: PurchaseTermType[];
  type?: string; //AND OR XOR
  ifPurchaseTerm?: PurchaseTermType;
  thenPurchaseTerm?: PurchaseTermType;
}

export interface OwnerPetitionsType {
  appointeeId: string;
  appointer: string;
  ownersList: string[];
  storeId: string;
}


export interface Bid {
  accepted: boolean;
  bidderId: string;
  itemId: string;
  ownersAccepted: string[];
  price: number;
  quantity: number;
  storeId: string;
}

