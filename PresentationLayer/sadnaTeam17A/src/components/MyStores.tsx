import MyStoreCard from "./MyStoreCard";
import { Button, SimpleGrid } from "@chakra-ui/react";
import axios from "axios";
import { useContext, useEffect, useState } from "react";
import { Role, Store } from "../types";
import { ClientCredentialsContext } from "../App";

interface Props {
  onCreateStore: () => void;
  onManageStore: (storeId: string) => void;
}

const MyStores = ({ onManageStore, onCreateStore }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const [stores, setStores] = useState<Store[]>([]);
  const [roles, setRoles] = useState<Role[]>([]);

  const fetchRoles = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/get-user-roles/id=${clientCredentials}`
    );
    if (!response.data.error) {
      setRoles(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  const fetchStores = async () => {
    const tempStores = [];
    for (const role of roles) {
      const response = await axios.get(
        `http://localhost:8080/api/v1/stores/store-info/id=${clientCredentials}&storeId=${role.storeId}`
      );
      if (!response.data.error) {
        tempStores.push(response.data.value);
      } else {
        console.log(response.data.error);
      }
    }
    setStores(tempStores);
  };

  useEffect(() => {
    fetchRoles();
  }, []);

  useEffect(() => {
    fetchStores();
  }, [roles]);

  return (
    <>
      <SimpleGrid
        padding="30px"
        spacing={6}
        // columns={{ sm: 1, md: 2, lg: 3, xl: 3, "2xl": 2 }}
      >
        <Button onClick={onCreateStore} colorScheme="blackAlpha">
          Open new store
        </Button>
        {stores.map((store) => (
          <MyStoreCard
            key={store.storeId}
            name={store.name}
            id={store.storeId}
            rating={store.rating}
            onManageStore={() => onManageStore(store.storeId)}
          />
        ))}
      </SimpleGrid>
    </>
  );
};

export default MyStores;
