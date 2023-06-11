import {
  Button,
  Card,
  CardBody,
  CardFooter,
  Divider,
  Flex,
  Heading,
  Image,
  Input,
  Stack,
  Text,
} from "@chakra-ui/react";
import React, { useContext, useEffect, useState } from "react";
import itemIcon from "../../assets/item.png";
import { SoldItemType } from "../../types";
import axios from "axios";
import { ClientCredentialsContext } from "../../App";

interface Props {
  soldItem: SoldItemType;
}

const SoldItem = ({ soldItem }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const getItemName = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/item-info/storeId=${soldItem.storeId}&itemId=${soldItem.itemId}`
    );
    if (!response.data.error) {
      setItemName(response.data.value.name);
    } else {
      console.log(response.data.error);
    }
  };
  useEffect(() => {
    getItemName();
  }, []);

  const getStoreName = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/store-info/id=${clientCredentials}&storeId=${soldItem.storeId}`
    );
    if (!response.data.error) {
      setStoreName(response.data.value.name);
    } else {
      console.log(response.data.error);
    }
  };
  useEffect(() => {
    getStoreName();
  }, []);

  const [itemName, setItemName] = useState("");
  const [storeName, setStoreName] = useState("");
  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");

  const date = new Date(soldItem.date);
  const formattedDate = `${date.getDate()}/${date.getMonth() + 1}/${date
    .getFullYear()
    .toString()
    .substr(-2)}`;

  return (
    <Card maxW="sm">
      <CardBody>
        <Flex justifyContent="center">
          <Image src={itemIcon} borderRadius="lg" />
        </Flex>
        <Stack mt="6" spacing="3">
          <Heading size="md">{itemName}</Heading>
          <Text>{storeName}</Text>
          <Text>quantity: {soldItem.quantity}</Text>
          <Text>date: {formattedDate}</Text>
        </Stack>
      </CardBody>
      <Divider />
      <CardFooter>
        <Stack width="100%">
          <Flex justifyContent="center">
            {errorMsg ? (
              <Text color="red">{message}</Text>
            ) : (
              <Text>{message}</Text>
            )}
          </Flex>
        </Stack>
      </CardFooter>
    </Card>
  );
};

export default SoldItem;
