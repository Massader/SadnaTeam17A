export interface Store {
  name: string;
  storeId: string;
  rating: number;
  description: string;
  //   closed: boolean;
  //   shutdown: boolean;
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

export interface Category {
  categoryName: string;
}

export interface User {
  username: string;
  id: string;
  roles: Role[];
  // purchases: Purchase[];
  isAdmin: boolean;
}

export abstract class Role {
  storeId: number | undefined;
}

export class StoreOwner extends Role {}

export class StoreFounder extends StoreOwner {}
