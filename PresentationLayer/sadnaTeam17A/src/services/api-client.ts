import axios, { CanceledError } from "axios";

export default axios.create({
  baseURL: "localhost:8080/api/v1",
});

export { CanceledError };
