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

  const handleEdit = async () => {
    handleEditName();
    handleEditDescription();
    if (quantity !== "") handleEditQuantity();
    if (price !== "") handleEditPrice();
    if (!errorMsg) {
      setInEdit(false);
      setMessage("Edit saved");
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
      return true;
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
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
      return true;
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
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
      return true;
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
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
      return true;
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
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
  const [message, setMessage] = useState("");

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
              <Input
                bg="white"
                defaultValue={editItem.name}
                onChange={(name) => setName(name.target.value)}
              />
              <Text>description:</Text>
              <Input
                bg="white"
                defaultValue={editItem.description}
                onChange={(description) =>
                  setDescription(description.target.value)
                }
              />
              <Text>quantity:</Text>
              <Input
                bg="white"
                defaultValue={editItem.quantity}
                onChange={(quantity) => setQuantity(quantity.target.value)}
              />
              <Text>price:</Text>
              <Input
                bg="white"
                defaultValue={editItem.price}
                onChange={(price) => setPrice(price.target.value)}
              />
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
                Save
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
