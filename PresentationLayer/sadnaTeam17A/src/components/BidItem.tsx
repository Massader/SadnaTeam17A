import {
  Button,
  Input,
  InputGroup,
  InputLeftElement,
  Modal,
  ModalBody,
  ModalCloseButton,
  ModalContent,
  ModalFooter,
  ModalHeader,
  ModalOverlay,
  Flex,
  Text,
  useToast,
} from "@chakra-ui/react";
import React, { useContext, useState } from "react";
import { Item } from "../types";
import { ClientCredentialsContext } from "../App";
import axios from "axios";

interface Props {
  item: Item;
  isOpen: boolean;
  onClose: () => void;
}

const BidItem = ({ isOpen, onClose, item }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const [quantity, setQuantity] = useState("0");
  const [bid, setBid] = useState("0");
  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");

  const toast = useToast();

  const addBid = async () => {
    const response = await axios.post(
      "http://localhost:8080/api/v1/stores/add-bid-to-item",
      {
        clientCredentials: clientCredentials,
        storeId: item.storeId,
        itemId: item.id,
        bidPrice: bid,
        quantity: quantity,
      }
    );
    if (!response.data.error) {
      setErrorMsg(false);
      toast({
        title: `Bid sent!`,
        colorScheme: "blue",
        status: "success",
        duration: 3000,
        isClosable: true,
      });
      onClose();
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose}>
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>Add bid to {item.name}</ModalHeader>
        <ModalCloseButton />
        <ModalBody>
          <Text>Quantity:</Text>
          <Input
            bg="white"
            placeholder="0"
            type="number"
            onChange={(quantity) => setQuantity(quantity.target.value)}
          />
          <Text>Bid:</Text>
          <Flex>
            <InputGroup>
              <InputLeftElement
                pointerEvents="none"
                color="gray.300"
                fontSize="1.2em"
                children="$"
              />
              <Input
                bg="white"
                placeholder="0"
                type="number"
                onChange={(bid) => setBid(bid.target.value)}
              />
            </InputGroup>
          </Flex>
          <Flex marginTop={2} justifyContent="center">
            {errorMsg ? (
              <Text color="red">{message}</Text>
            ) : (
              <Text>{message}</Text>
            )}
          </Flex>
        </ModalBody>

        <ModalFooter justifyContent="space-between">
          <Button colorScheme="blackAlpha" mr={3} onClick={onClose}>
            Close
          </Button>
          <Button
            onClick={() => {
              addBid();
            }}
            colorScheme="blue"
          >
            Submit
          </Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
};

export default BidItem;
