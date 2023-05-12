import {
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
import storeIcon from "../assets/store.png";
import React, { useContext, useState } from "react";
import { ClientCredentialsContext } from "../App";
import axios from "axios";

interface Props {
  name: string;
  storeId: string;
  description: string;
  rating: number;
  onShop: () => void;
}

const StoreCard = ({ name, storeId, rating, description, onShop }: Props) => {
  const { clientCredentials, isAdmin } = useContext(ClientCredentialsContext);

  const handleShutdown = async () => {
    const response = await axios.put(
      "http://localhost:8080/api/v1/stores/admin/shutdown-store",
      {
        clientCredentials: clientCredentials,
        targetId: storeId,
      }
    );
    if (!response.data.error) {
      setErrorMsg(false);
      setMessage("Store Shutdowned!");
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");

  return (
    <Card maxW="sm">
      <CardBody>
        <Flex justifyContent="center">
          <Image src={storeIcon} borderRadius="lg" />
        </Flex>
        <Stack mt="6" spacing="3">
          <Heading size="md">{name}</Heading>
          <Text>{description}</Text>
          <Text color="blue.600" fontSize="2xl">
            Rating: {rating}
          </Text>
        </Stack>
      </CardBody>
      <Divider />
      <CardFooter>
        <Stack w="100%">
          <Button
            onClick={onShop}
            variant="solid"
            colorScheme="blue"
            width="100%"
          >
            Shop
          </Button>
          {isAdmin && (
            <>
              <Button
                onClick={handleShutdown}
                variant="solid"
                colorScheme="blackAlpha"
                width="100%"
              >
                Shutdown store
              </Button>
              <Flex justifyContent="center">
                {errorMsg ? (
                  <Text color="red">{message}</Text>
                ) : (
                  <Text>{message}</Text>
                )}
              </Flex>
            </>
          )}
        </Stack>
      </CardFooter>
    </Card>
  );
};

export default StoreCard;
