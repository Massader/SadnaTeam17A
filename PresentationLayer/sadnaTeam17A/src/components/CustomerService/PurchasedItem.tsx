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
import { PurchasedItemType } from "../../types";
import axios from "axios";
import { ClientCredentialsContext } from "../../App";
import { AiFillStar, AiOutlineStar } from "react-icons/ai";

interface Props {
  purchasedItem: PurchasedItemType;
}

const PurchasedItem = ({ purchasedItem }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const getItemName = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/item-info/storeId=${purchasedItem.storeId}&itemId=${purchasedItem.itemId}`
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
      `http://localhost:8080/api/v1/stores/store-info/id=${clientCredentials}&storeId=${purchasedItem.storeId}`
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

  const handleRate = async () => {
    const response = await axios.post(
      "http://localhost:8080/api/v1/stores/rate-item",
      {
        clientCredentials: clientCredentials,
        itemId: purchasedItem.itemId,
        storeId: purchasedItem.storeId,
        rating: rating,
      }
    );
    if (!response.data.error) {
      setErrorMsg(false);
      setRated(true);
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  useEffect(() => {
    if (purchasedItem.rated) setRated(true);
  }, []);

  const handleMouseEnterItem = (index: number) => {
    setRating(index + 1);
  };

  const handleMouseLeaveItem = () => {
    submittedItem !== 0 ? setRating(submittedItem) : setRating(0);
  };

  const handleClickitemRating = (index: number) => {
    if (submittedItem === index + 1) {
      setSubmittedItem(0);
    } else {
      setSubmittedItem(index + 1);
    }
  };

  const [itemName, setItemName] = useState("");
  const [storeName, setStoreName] = useState("");
  const [rating, setRating] = useState(0);
  const [submittedItem, setSubmittedItem] = useState(0);
  const [rated, setRated] = useState(false);
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
          <Text>{storeName}</Text>
        </Stack>
      </CardBody>
      <Divider />
      <CardFooter>
        <Stack width="100%" justifyContent="center" alignItems="center">
          {!purchasedItem.rated && !rated && (
            <Stack justifyContent="center" alignItems="center">
              <Flex justifyContent="center" alignItems="center">
                {[...Array(5)].map((_, index) => (
                  <span
                    key={index}
                    onMouseEnter={() => handleMouseEnterItem(index)}
                    onMouseLeave={() => handleMouseLeaveItem()}
                    onClick={() => handleClickitemRating(index)}
                  >
                    {index < rating ? (
                      <AiFillStar color="ffb703" size={30} />
                    ) : (
                      <AiOutlineStar size={30} />
                    )}
                  </span>
                ))}
              </Flex>
              <Button
                onClick={() => handleRate()}
                variant="solid"
                colorScheme="blue"
                width="100%"
              >
                Rate item
              </Button>
            </Stack>
          )}
          {(purchasedItem.rated || rated) && (
            <Flex justifyContent="space-between" alignItems="center">
              <Text color="blue.600" fontSize="2xl">
                Rated
              </Text>
            </Flex>
          )}
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

export default PurchasedItem;
