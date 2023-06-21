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
  vus: 1000,
  duration: '3m',
};


export default function () {

  const create_client = http.post('http://localhost:8080/api/v1/users/create-client');

  total_search.add(1);
  const search_item = http.get('http://localhost:8080/api/v1/stores/search-item/keyword=&category=&minPrice=&maxPrice=&itemRating=&storeRating=&storeId=&number=&page=');
  if (JSON.parse(search_item.body).successful) {
    successful_search.add(1);
  }

  sleep(1);
}
