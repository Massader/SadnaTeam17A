import axios from "axios";
import { useContext, useEffect, useState } from "react";
import { ClientCredentialsContext } from "../App";
import { Basket, Item } from "../types";
import StoreBasket from "./StoreBasket";
import {
  Button,
  Flex,
  Heading,
  Input,
  Select,
  Stack,
  Text,
} from "@chakra-ui/react";
import { countries } from "../countries";

const MyCart = () => {
  const pages = ["myCart", "buyCart"];
  const [page, setPage] = useState(pages[0]);

  const [cartPrice, setCartPrice] = useState(0);
  const [cart, setCart] = useState<Basket[]>([]);
  const { clientCredentials } = useContext(ClientCredentialsContext);
  const [cartPurchased, setCartPurchased] = useState(false);

  const [address, setAddress] = useState("");
  const [city, setCity] = useState("");
  const [country, setCountry] = useState("");
  const [zip, setZip] = useState("");
  const [cardNumber, setCardNumber] = useState("");
  const [month, setMonth] = useState("");
  const [year, setYear] = useState("");
  const [holder, setHolder] = useState("");
  const [cvv, setCvv] = useState("");
  const [idCard, setIdCard] = useState("");

  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");
  const [errorMsgPrice, setErrorMsgPrice] = useState(false);
  const [messagePrice, setMessagePrice] = useState("");

  const getCart = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/get-cart/id=${clientCredentials}`
    );
    if (!response.data.error) {
      console.log(response.data.value);
      setCart(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  const getCartPrice = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/get-cart-price/id=${clientCredentials}`
    );
    if (!response.data.error) {
      setCartPrice(response.data.value);
      setErrorMsgPrice(false);
      setMessagePrice("");
    } else {
      setCartPrice(0);
      setErrorMsgPrice(true);
      setMessagePrice(response.data.message);
    }
  };

  const purchaseCart = async () => {
    const response = await axios.put(
      "http://localhost:8080/api/v1/stores/purchase-cart",
      {
        clientCredentials: clientCredentials,
        expectedPrice: cartPrice,
        address: address,
        cardNumber: cardNumber,
        month: month,
        year: year,
        cvv: cvv,
        holder: holder,
        zip: zip,
        idCard: idCard,
        city: city,
        country: country,
      }
    );
    if (!response.data.error) {
      console.log(response.data.value);
      setCartPurchased(true);
      setErrorMsg(false);
      setMessage("Cart bought!");
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  useEffect(() => {
    getCart();
  }, []);

  useEffect(() => {
    getCartPrice();
  }, []);

  return (
    <>
      {page === "myCart" && (
        <Stack ml={3}>
          {cart.map((basket) => (
            <StoreBasket
              getCart={getCart}
              getCartPrice={getCartPrice}
              key={basket.storeId}
              basket={basket}
            />
          ))}
          <Flex padding={10}>
            <Stack w="100%">
              <Text color="blue.600" fontSize="2xl">
                Total price: ${cartPrice}
              </Text>
              {cartPrice > 0 && (
                <Button
                  onClick={() => setPage(pages[1])}
                  colorScheme="blue"
                  width="100%"
                >
                  Buy Cart
                </Button>
              )}
              <Flex justifyContent="center">
                {errorMsgPrice ? (
                  <Text color="red">{messagePrice}</Text>
                ) : (
                  <Text>{messagePrice}</Text>
                )}
              </Flex>
            </Stack>
          </Flex>
        </Stack>
      )}

      {page === "buyCart" && (
        <Flex padding={10} justifyContent="center" alignItems="center">
          <Stack spacing={4} w="100%" maxW="400px" px={4}>
            <Heading padding={5} textAlign="center">
              Buy Cart
            </Heading>
            {!cartPurchased && (
              <>
                <Input
                  bg="white"
                  placeholder="Address"
                  value={address}
                  onChange={(address) => setAddress(address.target.value)}
                />
                <Input
                  bg="white"
                  placeholder="ZIP"
                  value={zip}
                  type="number"
                  onChange={(zip) => setZip(zip.target.value)}
                />
                <Input
                  bg="white"
                  placeholder="City"
                  value={city}
                  onChange={(city) => setCity(city.target.value)}
                />
                <Select
                  bg="white"
                  placeholder="Select Country"
                  value={country}
                  onChange={(event) => setCountry(event.target.value)}
                >
                  {countries.map((countryName) => (
                    <option key={countryName} value={countryName}>
                      {countryName}
                    </option>
                  ))}
                </Select>
                <Input
                  bg="white"
                  placeholder="Card Number"
                  value={cardNumber}
                  type="number"
                  onChange={(cardNumber) =>
                    setCardNumber(cardNumber.target.value)
                  }
                />
                <Flex>
                  <Input
                    bg="white"
                    placeholder="Select Date and Time"
                    type="month"
                    onChange={(event) => {
                      const { value } = event.target;
                      const [year, month] = value.split("-");
                      setYear(year);
                      setMonth(month);
                    }}
                  />
                  <Input
                    bg="white"
                    placeholder="CVV"
                    value={cvv}
                    type="number"
                    onChange={(cvv) => setCvv(cvv.target.value)}
                  />
                </Flex>
                <Input
                  bg="white"
                  placeholder="Holder Name"
                  value={holder}
                  onChange={(holder) => setHolder(holder.target.value)}
                />
                <Input
                  bg="white"
                  placeholder="ID"
                  value={idCard}
                  type="number"
                  onChange={(idCard) => setIdCard(idCard.target.value)}
                />
                <Button colorScheme="blue" size="lg" onClick={purchaseCart}>
                  Buy Cart
                </Button>
              </>
            )}
            <Flex justifyContent="center">
              {errorMsg ? (
                <Text color="red">{message}</Text>
              ) : (
                <Text>{message}</Text>
              )}
            </Flex>
            <Button
              colorScheme="blackAlpha"
              size="lg"
              onClick={() => {
                setPage(pages[0]);
                getCart();
                getCartPrice();
              }}
            >
              Back
            </Button>
          </Stack>
        </Flex>
      )}
    </>
  );
};

export default MyCart;
