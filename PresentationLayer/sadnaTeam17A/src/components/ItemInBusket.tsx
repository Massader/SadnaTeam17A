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

interface Props {
  itemId: string;
  quantity: number;
  storeId: string;
  getCart: () => {};
  getCartPrice: () => {};
}

const ItemInBusket = ({
  itemId,
  quantity,
  storeId,
  getCart,
  getCartPrice,
}: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const getItemInfo = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/item-info/storeId=${storeId}&itemId=${itemId}`
    );
    if (!response.data.error) {
      setItemName(response.data.value.name);
      setItemPrice(response.data.value.price);
    } else {
      console.log(response.data.error);
    }
  };

  useEffect(() => {
    getItemInfo();
  }, [quantity]);

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

  const [itemName, setItemName] = useState("");
  const [itemPrice, setItemPrice] = useState(0);

  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");

  return (
    <Card maxW="sm">
      <CardBody>
        <Flex justifyContent="center">
          <Image src={itemIcon} borderRadius="lg" />
        </Flex>
        <Stack mt="6" spacing="3">
          <Heading size="md">{itemName}</Heading>
          <Flex alignItems="center">
            <Text>quantity: {quantity}</Text>
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
          </Flex>
          <Flex justifyContent="space-between" alignItems="center">
            <Text color="blue.600" fontSize="2xl">
              ${itemPrice * quantity}
            </Text>
          </Flex>
        </Stack>
      </CardBody>
      <Divider />
      <CardFooter>
        <Stack width="100%">
          <Button
            onClick={() => handleRemoveFromCart(quantity)}
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

export default ItemInBusket;
