import {
  Box,
  Button,
  Card,
  CardBody,
  CardFooter,
  Divider,
  Flex,
  Heading,
  Image,
  Stack,
  Text,
} from "@chakra-ui/react";
import React, { useContext, useEffect, useState } from "react";
import itemIcon from "../assets/item.png";
import axios from "axios";
import { ClientCredentialsContext } from "../App";
import { AiOutlineShoppingCart } from "react-icons/ai";
import { CartItemType } from "../types";

interface Props {
  itemId: string;
  cartItem: CartItemType;
  storeId: string;
  getCart: () => {};
  getCartPrice: () => {};
}

const ItemInBasket = ({
  itemId,
  cartItem,
  storeId,
  getCart,
  getCartPrice,
}: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const handleRemoveFromCart = async (quantityToRemove: number) => {
    const response = await axios.delete(
      "http://localhost:8080/api/v1/users/remove-from-cart",
      {
        data: {
          clientCredentials: clientCredentials,
          itemId: itemId,
          quantity: quantityToRemove,
          storeId: storeId,
        },
      }
    );
    if (!response.data.error) {
      setErrorMsg(false);
      setMessage("Item removed from cart!");
      getCart();
      getCartPrice();
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  const handleAddToCart = async () => {
    const response = await axios.post(
      "http://localhost:8080/api/v1/users/add-to-cart",
      {
        clientCredentials: clientCredentials,
        itemId: itemId,
        quantity: 1,
        storeId: storeId,
      }
    );
    if (!response.data.error) {
      setErrorMsg(false);
      setMessage("Item added to cart!");
      getCart();
      getCartPrice();
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  const getitemDiscount = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/get-item-discount/id=${clientCredentials}&storeId=${storeId}&itemId=${itemId}`
    );
    if (!response.data.error) {
      console.log(response.data.value);
      setItemDiscount(response.data.value);
    } else {
      setItemDiscount(0);
    }
  };

  const [itemDiscount, setItemDiscount] = useState(0);

  useEffect(() => {
    getitemDiscount();
  }, []);

  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");

  return (
    <Card maxW="sm">
      <CardBody>
        <Flex justifyContent="center">
          <Image src={itemIcon} borderRadius="lg" />
        </Flex>
        <Stack mt="6" spacing="3">
          <Heading size="md">{cartItem.item.name}</Heading>
          <Flex alignItems="center">
            <Text>quantity: {cartItem.quantity}</Text>
            {cartItem.item.purchaseType !== "BID" && (
              <>
                <Button
                  onClick={() => handleAddToCart()}
                  colorScheme="blackAlpha"
                  marginLeft={2}
                >
                  +
                </Button>
                <Button
                  onClick={() => handleRemoveFromCart(1)}
                  colorScheme="blackAlpha"
                  marginLeft={2}
                >
                  -
                </Button>
              </>
            )}
          </Flex>
          <Flex alignItems="center">
            {itemDiscount === 0 && (
              <Text color="blue.600" fontSize="2xl">
                ${cartItem.price * cartItem.quantity}
              </Text>
            )}
            {itemDiscount !== 0 && (
              <>
                <Text as="del" color="gray" fontSize="2xl">
                  ${cartItem.price * cartItem.quantity}
                </Text>
                <Text color="blue.600" fontSize="2xl" ml={3}>
                  ${cartItem.price * cartItem.quantity * (itemDiscount / 100)}
                </Text>
              </>
            )}
          </Flex>
        </Stack>
      </CardBody>
      <Divider />
      <CardFooter>
        <Stack width="100%">
          <Button
            onClick={() => handleRemoveFromCart(cartItem.quantity)}
            variant="solid"
            colorScheme="blue"
            width="100%"
          >
            <Box marginRight={2}>
              <AiOutlineShoppingCart />
            </Box>
            Remove from cart
          </Button>
          {/* <Flex justifyContent="center">
            {errorMsg ? (
              <Text color="red">{message}</Text>
            ) : (
              <Text>{message}</Text>
            )}
          </Flex> */}
        </Stack>
      </CardFooter>
    </Card>
  );
};

export default ItemInBasket;
