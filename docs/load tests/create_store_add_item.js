import http from 'k6/http';
import { sleep } from 'k6';
import { Counter, Rate, Trend } from 'k6/metrics';
import { SharedArray } from 'k6/data';

//const users_logedIn = new Array(1000).fill(false);
//const users_idss = new SharedArray("userIDs", );

var total_open = new Counter("total open");
var total_add_item = new Counter("total add item");
var successful_open = new Counter("successful open");
var successful_add_item = new Counter("successful add item");


const params = {
  headers: {
    'Content-Type': 'application/json'
  }
};


export const options = {
  vus: 1000,
  duration: '3m',
};

export function setup() {
  for (let i = 1; i < 1001; i++) {
    const body = JSON.stringify({
      'username': 'vu_' + i,
      'password': 'Aa1234'
    });
    const register = http.post('http://localhost:8080/api/v1/users/register', body, params);
  }
}


export default function () {

  const create_client = http.post('http://localhost:8080/api/v1/users/create-client');

  const login_body = JSON.stringify({
    'clientCredentials': JSON.parse(create_client.body).value,
    'username': 'vu_' + __VU,
    'password': 'Aa1234'
  });

  const login = http.post('http://localhost:8080/api/v1/users/login', login_body, params);
  const founderId = JSON.parse(login.body).value.id;

  const store_number = Math.floor(Math.random() * 2**31);
  const create_store_body = JSON.stringify({
    'clientCredentials': founderId,
    'name': "store_" + store_number,
    'description': "desc_" + store_number
  });
  total_open.add(1);
  const create_store = http.post('http://localhost:8080/api/v1/stores/create-store', create_store_body, params);
  if (JSON.parse(create_store.body).successful) {
    successful_open.add(1);
  }

  const store_id = JSON.parse(create_store.body).value.storeId;
  const add_item_body = JSON.stringify({
    'id': founderId,
    'name': 'item__' + store_number,
    'price': 2,
    'storeId': store_id,
    'rating': 3,
    'quantity': 10,
    'description': 'desc'
  });
  total_add_item.add(1);
  const add_item = http.post('http://localhost:8080/api/v1/stores/add-item-to-store', add_item_body, params);
  if (JSON.parse(add_item.body).successful) {
    successful_add_item.add(1);
  }
  
  const logout_body = JSON.stringify({
    'clientCredentials': founderId
  });

  http.post('http://localhost:8080/api/v1/users/logout', logout_body, params);
  
  sleep(1);
}
