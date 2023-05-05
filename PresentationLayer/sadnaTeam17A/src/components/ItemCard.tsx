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
import { useEffect, useState } from "react";
import axios from "axios";
import { Category } from "../types";

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
  const getStoreName = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/store-info/storeId=${storeId}`
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

  const [storeName, setStoreName] = useState("");
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
              Rating: {rating}
            </Text>
          </Flex>
        </Stack>
      </CardBody>
      <Divider />
      <CardFooter>
        <Button variant="solid" colorScheme="blue" width="100%">
          <Box marginRight={2}>
            <AiOutlineShoppingCart />
          </Box>
          Add to cart
        </Button>
      </CardFooter>
    </Card>
  );
};

export default ItemCard;
