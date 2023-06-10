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
  Tag,
  Text,
  useToast,
} from "@chakra-ui/react";
import React, { useContext, useState } from "react";
import bidIcon from "../../assets/bid.png";
import { Bid } from "../../types";
import axios from "axios";
import { ClientCredentialsContext } from "../../App";
import { AiOutlineShoppingCart } from "react-icons/ai";
import ItemNameFromId from "../ItemNameFromId";

interface Props {
  bid: Bid;
  refreshBids: () => {};
}

const MyBidCard = ({ bid, refreshBids }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const itemName = <ItemNameFromId storeId={bid.storeId} itemId={bid.itemId} />;

  const toast = useToast();

  const handleAddToCart = async () => {
    const response = await axios.post(
      "http://localhost:8080/api/v1/users/add-to-cart",
      {
        clientCredentials: clientCredentials,
        itemId: bid.itemId,
        quantity: bid.quantity,
        storeId: bid.storeId,
      }
    );
    if (!response.data.error) {
      toast({
        title: `${itemName} added to your cart!`,
        colorScheme: "blue",
        status: "success",
        duration: 3000,
        isClosable: true,
      });
      refreshBids();
    } else {
      toast({
        title: `${response.data.message}`,
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    }
  };

  return (
    <Card maxW="sm">
      <CardBody>
        <Flex justifyContent="center">
          <Image src={bidIcon} borderRadius="lg" />
        </Flex>
        <Stack mt="6" spacing="3">
          <Flex alignItems="center">
            <Heading size="md" display="flex" alignItems="center">
              Bid on:
              <ItemNameFromId
                ml={1}
                storeId={bid.storeId}
                itemId={bid.itemId}
              />
            </Heading>
          </Flex>
          <>
            <Text>Quantity: {bid.quantity}</Text>
            <Text>Price: ${bid.price}</Text>
          </>
        </Stack>
      </CardBody>
      <Divider />
      <CardFooter>
        <Stack w="100%">
          {!bid.accepted && (
            <Flex justifyContent="center">
              <Tag size="lg" variant="outline" colorScheme="blue">
                Waiting
              </Tag>
            </Flex>
          )}
          {bid.accepted && (
            <Button
              onClick={handleAddToCart}
              variant="solid"
              colorScheme="blue"
              width="100%"
            >
              <Box marginRight={2}>
                <AiOutlineShoppingCart />
              </Box>
              Add to cart
            </Button>
          )}
        </Stack>
      </CardFooter>
    </Card>
  );
};

export default MyBidCard;
