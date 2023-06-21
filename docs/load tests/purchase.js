import http from 'k6/http';
import { sleep } from 'k6';
import { Counter, Rate, Trend } from 'k6/metrics';

//const users_logedIn = new Array(1000).fill(false);
//const users_ids = new Array(1000).fill(null);

var total_search = new Counter("total search");
var total_add_to_cart = new Counter("total add to cart");
var total_purchase = new Counter("total purchase");
var successful_search = new Counter("successful search");
var successful_add_to_cart = new Counter("successful add to cart");
var successful_purchase = new Counter("successful purchase");
var bad_search = new Counter("fail search");
var bad_add_to_cart = new Counter("fail add to cart");
var bad_purchase = new Counter("fail purchase");
var counter = new Counter("counter");

const params = {
  headers: {
    'Content-Type': 'application/json'
  }
};


export const options = {
  vus: 20,
  duration: '3m',
};


export default function () {

  const create_client = http.post('http://localhost:8080/api/v1/users/create-client');
/*
  total_search.add(1);
  const search_item = http.get('http://localhost:8080/api/v1/stores/search-item/keyword=item00&category=&minPrice=&maxPrice=&itemRating=&storeRating=&storeId=&number=&page=');

  const item_id = JSON.parse(search_item.body).value[0].id;
  const store_id = JSON.parse(search_item.body).value[0].storeId;
*/
  const item_id = '05d12241-094d-43fd-aad9-fd0dda78fea4';
  const store_id = 'a041fddd-3888-466d-bde8-e3a1a41f3cc7';
  const add_item_to_cart_body = JSON.stringify({
    'clientCredentials': JSON.parse(create_client.body).value,
    'itemId': item_id,
    'quantity': 1,
    'storeId': store_id
  });
  
  const add_item_to_cart = http.post('http://localhost:8080/api/v1/users/add-to-cart', add_item_to_cart_body, params);

  const purchase_body = JSON.stringify({
    'clientCredentials': JSON.parse(create_client.body).value,
    'expectedPrice': 1,
    'address': 'Bar Giora, 21',
    'city': 'Beer-Sheva',
    'country': 'Israel',
    'zip': '5363109',
    'cardNumber': '5326010102029945',
    'month': '12',
    'year': '2027',
    'holder': 'Yala Kvar',
    'cvv': '123',
    'idCard': '234234234'
  });

  total_purchase.add(1);
  const purchase_cart = http.put('http://localhost:8080/api/v1/stores/purchase-cart', purchase_body, params);
  if (JSON.parse(purchase_cart.body).successful) {
    successful_purchase.add(1);
  }

  sleep(1);
}
