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
import itemIcon from "../assets/item.png";
import { AiOutlineShoppingCart } from "react-icons/ai";
import { useContext, useEffect, useState } from "react";
import axios from "axios";
import { Category } from "../types";
import { ClientCredentialsContext } from "../App";

interface Props {
  name: string;
  id: string;
  price: number;
  storeId: string;
  rating: number;
  quantity: number;
  description: string;
  categories: Category[];
}

const ItemCard = ({
  name,
  id,
  price,
  storeId,
  rating,
  quantity,
  description,
  categories,
}: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const getStoreName = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/store-info/id=${clientCredentials}&storeId=${storeId}`
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

  const handleAddToCart = async () => {
    const response = await axios.post(
      "http://localhost:8080/api/v1/users/add-to-cart",
      {
        clientCredentials: clientCredentials,
        itemId: id,
        quantity: 1,
        storeId: storeId,
      }
    );
    if (!response.data.error) {
      setErrorMsg(false);
      setMessage("Item added to cart!");
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  const [storeName, setStoreName] = useState("");

  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");
  return (
    <Card maxW="sm">
      <CardBody>
        <Flex justifyContent="center">
          <Image src={itemIcon} borderRadius="lg" />
        </Flex>
        <Stack mt="6" spacing="3">
          <Heading size="md">{name}</Heading>
          <Text>{storeName}</Text>
          <Text>{description}</Text>
          <Flex justifyContent="space-between" alignItems="center">
            <Text color="blue.600" fontSize="2xl">
              ${price}
            </Text>
            <Text color="blue.600" fontSize={22}>
              Rating: {rating.toFixed(1)}
            </Text>
          </Flex>
        </Stack>
      </CardBody>
      <Divider />
      <CardFooter>
        <Stack width="100%">
          <Button
            onClick={() => handleAddToCart()}
            variant="solid"
            colorScheme="blue"
            width="100%"
          >
            <Box marginRight={2}>
              <AiOutlineShoppingCart />
            </Box>
            Add to cart
          </Button>
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

export default ItemCard;
