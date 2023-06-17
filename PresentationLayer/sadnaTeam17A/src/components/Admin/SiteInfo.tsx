import axios from "axios";
import React, { useContext, useEffect, useState } from "react";
import { ClientCredentialsContext } from "../../App";
import { Button, Flex, Heading, Input, Stack, Text } from "@chakra-ui/react";
import { User } from "../../types";
interface Props {
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
}

const SiteInfo = ({ setPage, pages }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const [connectedUsersNum, setConnectedUsersNum] = useState(0);
  const [connectedUsers, setConnectedUsers] = useState<User[]>([]);
  const [connectedClients, setConnectedClients] = useState(0);
  const [registeredUsers, setRegisteredUsers] = useState(0);

  const getLoggedInUsers = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/admin/get-logged-in-users/id=${clientCredentials}`
    );
    if (!response.data.error) {
      console.log(response.data.value);
      setConnectedUsersNum(response.data.value.length);
      setConnectedUsers(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  const getNumberOfConnectedClients = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/admin/get-number-of-connected-clients/id=${clientCredentials}`
    );
    if (!response.data.error) {
      console.log(response.data.value);
      setConnectedClients(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  const getNumberOfRegistered = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/admin/get-number-of-registered-users/id=${clientCredentials}`
    );
    if (!response.data.error) {
      console.log(response.data.value);
      setRegisteredUsers(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  useEffect(() => {
    getLoggedInUsers();
    getNumberOfConnectedClients();
    getNumberOfRegistered();
  }, []);

  return (
    <>
      <Stack w="50%" maxW="400px">
        <Heading padding={5} textAlign="center">
          Site Information
        </Heading>
        <Text>Number of Connected Users: {connectedUsersNum}</Text>
        <Text>Number of Connected Clients: {connectedClients}</Text>
        <Text>Number Of Registered Users: {registeredUsers}</Text>
        <Text>Connected Users:</Text>
        {connectedUsers.map((user) => (
          <Text>{user.username}</Text>
        ))}
        <Button
          colorScheme="blackAlpha"
          size="lg"
          onClick={() => setPage(pages[0])}
        >
          Back
        </Button>
      </Stack>
    </>
  );
};

export default SiteInfo;
