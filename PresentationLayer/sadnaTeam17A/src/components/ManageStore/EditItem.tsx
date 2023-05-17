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
import { Item } from "../../types";
import axios from "axios";
import { ClientCredentialsContext } from "../../App";

interface Props {
  editItem: Item;
}

const EditItem = ({ editItem }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const handleEdit = () => {
    if (
      !errorMsgName &&
      !errorMsgPrice &&
      !errorMsgDescription &&
      !errorMsgQuantity
    ) {
      setErrorMsg(false);
      setInEdit(false);
      setMessage("Edit saved");
      setMessageName("");
      setMessageDescription("");
      setMessageQuantity("");
      setMessagePrice("");
    } else {
      setErrorMsg(true);
    }
  };

  const handleEditName = async () => {
    const response = await axios.put(
      "http://localhost:8080/api/v1/stores/item/name",
      {
        clientCredentials: clientCredentials,
        storeId: editItem.storeId,
        id: editItem.id,
        name: name,
      }
    );
    if (!response.data.error) {
      setErrorMsgName(false);
      setMessageName("Name saved");
    } else {
      setErrorMsgName(true);
      setMessageName(response.data.message);
    }
  };

  const handleEditQuantity = async () => {
    const response = await axios.put(
      "http://localhost:8080/api/v1/stores/item/quantity",
      {
        clientCredentials: clientCredentials,
        storeId: editItem.storeId,
        id: editItem.id,
        quantity: Number(quantity),
      }
    );
    if (!response.data.error) {
      setErrorMsgQuantity(false);
      setMessageQuantity("Quantity saved");
    } else {
      setErrorMsgQuantity(true);
      setMessageQuantity(response.data.message);
    }
  };

  const handleEditPrice = async () => {
    const response = await axios.put(
      "http://localhost:8080/api/v1/stores/item/price",
      {
        clientCredentials: clientCredentials,
        storeId: editItem.storeId,
        id: editItem.id,
        price: Number(price),
      }
    );
    if (!response.data.error) {
      setErrorMsgPrice(false);
      setMessagePrice("Price saved");
    } else {
      setErrorMsgPrice(true);
      setMessagePrice(response.data.message);
    }
  };

  const handleEditDescription = async () => {
    const response = await axios.put(
      "http://localhost:8080/api/v1/stores/item/description",
      {
        clientCredentials: clientCredentials,
        storeId: editItem.storeId,
        id: editItem.id,
        description: description,
      }
    );
    if (!response.data.error) {
      setErrorMsgDescription(false);
      setMessageDescription("Description saved");
    } else {
      setErrorMsgDescription(true);
      setMessageDescription(response.data.message);
    }
  };

  const handleRemoveItem = async () => {
    const response = await axios.delete(
      "http://localhost:8080/api/v1/stores/remove-item-from-store",
      {
        data: {
          clientCredentials: clientCredentials,
          storeId: editItem.storeId,
          itemId: editItem.id,
        },
      }
    );
    if (!response.data.error) {
      setErrorMsg(false);
      setMessage("Item deleted!");
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  const [name, setName] = useState(editItem.name);
  const [description, setDescription] = useState(editItem.description);
  const [quantity, setQuantity] = useState("");
  const [price, setPrice] = useState("");

  const [inEdit, setInEdit] = useState(false);
  const [sureDelete, setSureDelete] = useState(false);
  const [errorMsg, setErrorMsg] = useState(false);
  const [errorMsgName, setErrorMsgName] = useState(false);
  const [messageName, setMessageName] = useState("");
  const [errorMsgDescription, setErrorMsgDescription] = useState(false);
  const [messageDescription, setMessageDescription] = useState("");
  const [errorMsgQuantity, setErrorMsgQuantity] = useState(false);
  const [messageQuantity, setMessageQuantity] = useState("");
  const [errorMsgPrice, setErrorMsgPrice] = useState(false);
  const [messagePrice, setMessagePrice] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    if (!errorMsgName) setMessageName("");
  }, [name]);
  useEffect(() => {
    if (!errorMsgDescription) setMessageDescription("");
  }, [description]);
  useEffect(() => {
    if (!errorMsgQuantity) setMessageQuantity("");
  }, [quantity]);
  useEffect(() => {
    if (!errorMsgPrice) setMessagePrice("");
  }, [price]);

  return (
    <Card maxW="sm">
      <CardBody>
        <Flex justifyContent="center">
          <Image src={itemIcon} borderRadius="lg" />
        </Flex>
        <Stack mt="6" spacing="3">
          {!inEdit && (
            <>
              <Text>name: {editItem.name}</Text>
              <Text>description: {editItem.description}</Text>
              <Text>quantity: {editItem.quantity}</Text>
              <Text>price: ${editItem.price}</Text>
            </>
          )}
          {inEdit && (
            <>
              <Text>name:</Text>
              <Flex>
                <Input
                  w="90%"
                  bg="white"
                  defaultValue={editItem.name}
                  onChange={(name) => setName(name.target.value)}
                />
                <Button onClick={handleEditName}>V</Button>
              </Flex>
              {errorMsgName ? (
                <Text color="red">{messageName}</Text>
              ) : (
                <Text>{messageName}</Text>
              )}
              <Text>description:</Text>
              <Flex>
                <Input
                  bg="white"
                  defaultValue={editItem.description}
                  onChange={(description) =>
                    setDescription(description.target.value)
                  }
                />
                <Button onClick={handleEditDescription}>V</Button>
              </Flex>
              {errorMsgDescription ? (
                <Text color="red">{messageDescription}</Text>
              ) : (
                <Text>{messageDescription}</Text>
              )}
              <Text>quantity:</Text>
              <Flex>
                <Input
                  bg="white"
                  defaultValue={editItem.quantity}
                  onChange={(quantity) => setQuantity(quantity.target.value)}
                />
                <Button
                  onClick={() => {
                    if (quantity !== "") handleEditQuantity();
                  }}
                >
                  V
                </Button>
              </Flex>
              {errorMsgQuantity ? (
                <Text color="red">{messageQuantity}</Text>
              ) : (
                <Text>{messageQuantity}</Text>
              )}
              <Text>price:</Text>
              <Flex>
                <Input
                  bg="white"
                  defaultValue={editItem.price}
                  onChange={(price) => setPrice(price.target.value)}
                />
                <Button
                  onClick={() => {
                    if (price !== "") handleEditPrice();
                  }}
                >
                  V
                </Button>
              </Flex>
              {errorMsgPrice ? (
                <Text color="red">{messagePrice}</Text>
              ) : (
                <Text>{messagePrice}</Text>
              )}
            </>
          )}
        </Stack>
      </CardBody>
      <Divider />
      <CardFooter>
        <Stack width="100%">
          <Flex justifyContent="center">
            {!inEdit && (
              <Button
                onClick={() => {
                  setInEdit(true);
                  setSureDelete(false);
                  setMessage("");
                }}
                variant="solid"
                colorScheme="blue"
                width="100%"
              >
                Edit
              </Button>
            )}
            {inEdit && (
              <Button
                onClick={() => handleEdit()}
                variant="solid"
                colorScheme="blue"
                width="100%"
              >
                Done
              </Button>
            )}
          </Flex>
          <Flex justifyContent="center">
            {!inEdit && !sureDelete && (
              <Button
                onClick={() => setSureDelete(true)}
                variant="solid"
                colorScheme="red"
                width="100%"
              >
                Delete Item
              </Button>
            )}
            {!inEdit && sureDelete && (
              <Button
                onClick={handleRemoveItem}
                variant="solid"
                colorScheme="red"
                width="100%"
              >
                Sure?
              </Button>
            )}
          </Flex>
          {errorMsg ? (
            <Text color="red">{message}</Text>
          ) : (
            <Text>{message}</Text>
          )}
        </Stack>
      </CardFooter>
    </Card>
  );
};

export default EditItem;
