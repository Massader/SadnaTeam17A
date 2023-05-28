import {
  Button,
  Box,
  Select,
  Stack,
  Flex,
  Text,
  Checkbox,
  Heading,
} from "@chakra-ui/react";
import React, { useContext, useEffect, useState } from "react";
import { ClientCredentialsContext } from "../../App";
import axios from "axios";
import { User } from "../../types";

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
  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");

  const formatRoles = (roles: string[]) => {
    return roles
      .map((role) => {
        const words = role.split("_");
        const formattedWords = words.map(
          (word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase()
        );
        return formattedWords.join(" ");
      })
      .join(", ");
  };

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
        <Flex padding={10} justifyContent="center" alignItems="center">
          <Stack spacing={4} w="80%" px={4}>
            {users.map((user) =>
              Object.entries(user.roles).map(([id, roles]) => (
                <>
                  {id === storeId ? (
                    <>
                      <Text fontWeight="bold" fontSize="xl">
                        {user.username + ": "}
                      </Text>
                      <div key={id}>{formatRoles(roles)}</div>
                    </>
                  ) : null}
                </>
              ))
            )}
          </Stack>
        </Flex>
      </Box>
    </>
  );
};

export default PositionsInfo;
