import http from 'k6/http';
import { sleep } from 'k6';
import { Counter, Rate, Trend } from 'k6/metrics';

var total_registrations = new Counter("total registrations");
var successful_registrations = new Counter("successful registrations");
var total_logins = new Counter("total logins");
var successful_logins = new Counter("successful logins");

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
  const i = Math.floor(Math.random() * 2**31);
  const body = JSON.stringify({
    'username': 'vu' + i,
    'password': 'Aa1234'
  });
  total_registrations.add(1);
  const register = http.post('http://localhost:8080/api/v1/users/register', body, params);
  if (JSON.parse(register.body).successful) {
    successful_registrations.add(1);
  }

  const create_client = http.post('http://localhost:8080/api/v1/users/create-client');
  
  const login_body = JSON.stringify({
    'clientCredentials': JSON.parse(create_client.body).value,
    'username': 'vu' + i,
    'password': 'Aa1234'
  });
  

  total_logins.add(1);
  const login = http.post('http://localhost:8080/api/v1/users/login', login_body, params);
  if (JSON.parse(login.body).successful) {
    successful_logins.add(1);
  }

  sleep(1);
}

