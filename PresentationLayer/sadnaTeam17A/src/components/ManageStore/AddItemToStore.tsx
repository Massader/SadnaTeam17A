import { Button, Flex, Input, Stack, Text } from "@chakra-ui/react";
import { useContext, useState } from "react";
import axios from "axios";
import { ClientCredentialsContext } from "../../App";

interface Props {
  storeId: string;
  storeName?: string;
  onBack: () => void;
}

const AddItemToStore = ({ storeId, onBack }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const addItem = async () => {
    const response = await axios.post(
      `http://localhost:8080/api/v1/stores/add-item-to-store`,
      {
        clientCredentials: clientCredentials,
        name: itemName,
        price: Number(itemPrice),
        storeId: storeId,
        quantity: Number(itemQuantity),
        description: itemDescription,
      }
    );
    if (!response.data.error) {
      setErrorMsg(false);
      setMessage(itemName + " added successfully!");
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };
  const [itemName, setItemName] = useState("");
  const [itemPrice, setItemPrice] = useState("");
  const [itemQuantity, setItemQuantity] = useState("");
  const [itemDescription, setItemDescription] = useState("");

  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");

  return (
    <>
      <Stack marginTop={3} paddingLeft={2} paddingRight={2}>
        <Input
          bg="white"
          placeholder="Item name"
          value={itemName}
          onChange={(itemName) => setItemName(itemName.target.value)}
        />
        <Input
          bg="white"
          placeholder="Item description"
          value={itemDescription}
          onChange={(itemDescription) =>
            setItemDescription(itemDescription.target.value)
          }
        />
        <Input
          bg="white"
          placeholder="Item price"
          value={itemPrice}
          onChange={(itemPrice) => setItemPrice(itemPrice.target.value)}
        />
        <Input
          bg="white"
          placeholder="Item quantity"
          value={itemQuantity}
          onChange={(itemQuantity) =>
            setItemQuantity(itemQuantity.target.value)
          }
        />
        <Button
          colorScheme="blue"
          onClick={() => addItem()}
          whiteSpace="normal"
        >
          Send
        </Button>
        <Flex justifyContent="center">
          {errorMsg ? (
            <Text color="red">{message}</Text>
          ) : (
            <Text>{message}</Text>
          )}
        </Flex>
        <Button onClick={onBack} whiteSpace="normal">
          Back
        </Button>
      </Stack>
    </>
  );
};

export default AddItemToStore;
