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
  // purchaseType
}

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
  items: Map<string, number>;
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
