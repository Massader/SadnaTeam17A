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

const SetManagerPermissions = ({ storeId, setPage, pages }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const getStoreManagers = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/get-store-managers/id=${clientCredentials}&storeId=${storeId}`
    );
    if (!response.data.error) {
      setUsers(response.data.value);
      console.log(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  useEffect(() => {
    getStoreManagers();
  }, []);

  const setPermissions = async () => {
    const response = await axios.put(
      "http://localhost:8080/api/v1/stores/role/set-manager-permissions",
      {
        clientCredentials: clientCredentials,
        managerId: selectedUser?.id,
        storeId: storeId,
        permissions: selectedPermissions,
      }
    );
    if (!response.data.error) {
      setErrorMsg(false);
      setMessage("Permissions set!");
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  const [users, setUsers] = useState<User[]>([]);
  const [selectedUser, setSelectedUser] = useState<User | null>(null);
  const [selectedPermissions, setSelectedPermissions] = useState<number[]>([]);
  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");

  const handlePermissionChange = (permissionNumber: number) => {
    setMessage("");
    setSelectedPermissions((prevPermissions) => {
      if (prevPermissions.includes(permissionNumber)) {
        return prevPermissions.filter((num) => num !== permissionNumber);
      } else {
        return [...prevPermissions, permissionNumber];
      }
    });
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
          Set Manager Permissions
        </Heading>
        <Flex padding={10} justifyContent="center" alignItems="center">
          <Stack spacing={4} w="50%" maxW="320px" px={4}>
            <Select
              bg="white"
              colorScheme="white"
              placeholder="Select a user"
              value={selectedUser?.id || ""}
              onChange={(event) => {
                setMessage("");
                const selectedId = event.target.value;
                const user = users.find((u) => u.id === selectedId);
                setSelectedUser(user || null);
              }}
            >
              {users.map((user) => (
                <option key={user.id} value={user.id}>
                  {user.username}
                </option>
              ))}
            </Select>
            {selectedUser !== null && (
              <>
                <Flex>
                  <Checkbox
                    borderColor="black"
                    defaultChecked={selectedPermissions.includes(0)}
                    onChange={() => handlePermissionChange(0)}
                  />
                  <Text marginLeft="6px">Store communicaion</Text>
                </Flex>
                <Flex>
                  <Checkbox
                    borderColor="black"
                    defaultChecked={selectedPermissions.includes(1)}
                    onChange={() => handlePermissionChange(1)}
                  />
                  <Text marginLeft="6px">Store sale history</Text>
                </Flex>
                <Flex>
                  <Checkbox
                    borderColor="black"
                    defaultChecked={selectedPermissions.includes(2)}
                    onChange={() => handlePermissionChange(2)}
                  />
                  <Text marginLeft="6px">Store stock management</Text>
                </Flex>
                <Flex>
                  <Checkbox
                    borderColor="black"
                    defaultChecked={selectedPermissions.includes(3)}
                    onChange={() => handlePermissionChange(3)}
                  />
                  <Text marginLeft="6px">Store item management</Text>
                </Flex>
                <Flex>
                  <Checkbox
                    borderColor="black"
                    defaultChecked={selectedPermissions.includes(4)}
                    onChange={() => handlePermissionChange(4)}
                  />
                  <Text marginLeft="6px">Store policy management</Text>
                </Flex>
                <Flex>
                  <Checkbox
                    borderColor="black"
                    defaultChecked={selectedPermissions.includes(5)}
                    onChange={() => handlePermissionChange(5)}
                  />
                  <Text marginLeft="6px">Store discount management</Text>
                </Flex>
                <Flex>
                  <Checkbox
                    borderColor="black"
                    defaultChecked={selectedPermissions.includes(6)}
                    onChange={() => handlePermissionChange(6)}
                  />
                  <Text marginLeft="6px">Store management information</Text>
                </Flex>
                <Button
                  w="100%"
                  colorScheme="blue"
                  size="lg"
                  onClick={setPermissions}
                >
                  Submit
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
        </Flex>
      </Box>
    </>
  );
};

export default SetManagerPermissions;
