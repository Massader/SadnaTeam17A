import { Button, Box, Heading, SimpleGrid } from "@chakra-ui/react";
import React, { useContext, useEffect, useState } from "react";
import { ClientCredentialsContext } from "../../App";
import axios from "axios";
import { User } from "../../types";
import PositionInfoCard from "./PositionInfoCard";

interface Props {
  storeId: string;
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
}

const PositionsInfo = ({ storeId, setPage, pages }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const getStoreStaff = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/store-staff/id=${clientCredentials}&storeId=${storeId}`
    );
    if (!response.data.error) {
      setUsers(response.data.value);
      console.log(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  useEffect(() => {
    getStoreStaff();
  }, []);

  const [users, setUsers] = useState<User[]>([]);

  return (
    <>
      <Box padding="30px">
        <Button
          w="100%"
          colorScheme="blackAlpha"
          size="lg"
          onClick={() => setPage(pages[2])}
        >
          Back
        </Button>
        <Heading padding={5} textAlign="center">
          Store's Positions Information
        </Heading>
        <SimpleGrid
          columns={{ sm: 1, md: 2, lg: 3, xl: 3, "2xl": 4 }}
          padding="10px"
          spacing={6}
        >
          {users.map((user) =>
            Object.entries(user.roles).map(([id, roles]) => (
              <>
                {id === storeId ? (
                  <>
                    <Box borderRadius={10} overflow="hidden">
                      <PositionInfoCard
                        key={id}
                        roles={roles}
                        username={user.username}
                      />
                    </Box>
                  </>
                ) : null}
              </>
            ))
          )}
        </SimpleGrid>
      </Box>
    </>
  );
};

export default PositionsInfo;
