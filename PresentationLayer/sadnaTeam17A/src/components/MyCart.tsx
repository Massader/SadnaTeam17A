import axios from "axios";
import { useContext, useEffect, useState } from "react";
import { ClientCredentialsContext } from "../App";
import { Basket, Item } from "../types";
import StoreBasket from "./StoreBasket";
import { Button, Flex, Stack, Text } from "@chakra-ui/react";

const MyCart = () => {
  const [cartPrice, setCartPrice] = useState(0);
  const [cart, setCart] = useState<Basket[]>([]);
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const getCart = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/get-cart/id=${clientCredentials}`
    );
    if (!response.data.error) {
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
    } else {
      console.log(response.data.error);
    }
  };

  useEffect(() => {
    getCart();
  }, [cart]);

  useEffect(() => {
    getCartPrice();
  }, [cart]);

  return (
    <>
      <Stack>
        {cart.map((basket) => (
          <StoreBasket key={basket.storeId} basket={basket} />
        ))}
        <Flex padding={10}>
          <Stack w="100%">
            <Text color="blue.600" fontSize="2xl">
              Total price: ${cartPrice}
            </Text>
            <Button colorScheme="blue" width="100%">
              Buy Cart
            </Button>
          </Stack>
        </Flex>
      </Stack>
    </>
  );
};

export default MyCart;
