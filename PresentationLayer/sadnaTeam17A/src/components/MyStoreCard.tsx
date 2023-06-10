import {
  Button,
  Card,
  CardBody,
  CardFooter,
  Flex,
  Heading,
  Image,
  Stack,
  Text,
} from "@chakra-ui/react";
import storeIcon from "../assets/store.png";
import { Role, Store } from "../types";
import { useContext, useEffect, useState } from "react";
import { ClientCredentialsContext } from "../App";
import axios from "axios";

interface Props {
  role: Role;
  onManageStore: () => void;
}

const MyStoreCard = ({ role, onManageStore }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const [store, setStore] = useState<Store>();

  const fetchStore = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/store-info/id=${clientCredentials}&storeId=${role.storeId}`
    );
    if (!response.data.error) {
      setStore(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  useEffect(() => {
    fetchStore();
  }, []);

  return (
    <Card direction="row" overflow="hidden" variant="outline">
      <Flex alignItems="center" ml={3}>
        <Image objectFit="contain" maxH="200px" maxW="200px" src={storeIcon} />
      </Flex>

      <Stack width="100%" flexWrap="nowrap">
        <CardBody>
          <Heading size="lg">{store?.name}</Heading>
          {role.permissions.map((permission) => (
            <Text key={permission} py="2">
              {permission
                .replaceAll("_", " ")
                .split(" ")
                .map(
                  (word) =>
                    word.charAt(0).toUpperCase() + word.slice(1).toLowerCase()
                )
                .join(" ")}
            </Text>
          ))}
        </CardBody>
        <CardFooter justifyContent="space-between">
          <Text>Rating: {store?.rating}</Text>
          <Button onClick={onManageStore} variant="solid" colorScheme="blue">
            Manage store
          </Button>
        </CardFooter>
      </Stack>
    </Card>
  );
};

export default MyStoreCard;
