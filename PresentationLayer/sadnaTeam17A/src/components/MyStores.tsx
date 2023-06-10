import MyStoreCard from "./MyStoreCard";
import { Button, SimpleGrid } from "@chakra-ui/react";
import axios from "axios";
import { useContext, useEffect, useState } from "react";
import { Role, Store } from "../types";
import { ClientCredentialsContext } from "../App";

interface Props {
  onCreateStore: () => void;
  onManageStore: (storeId: string, permissions: string[]) => void;
}

const MyStores = ({ onManageStore, onCreateStore }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const [roles, setRoles] = useState<Role[]>([]);

  const fetchRoles = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/get-user-roles/id=${clientCredentials}`
    );
    if (!response.data.error) {
      console.log(response.data.value);
      setRoles(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  useEffect(() => {
    fetchRoles();
  }, []);

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
        {roles.map((role) => (
          <MyStoreCard
            key={role.storeId}
            role={role}
            onManageStore={() => onManageStore(role.storeId, role.permissions)}
          />
        ))}
      </SimpleGrid>
    </>
  );
};

export default MyStores;
