import React, { useContext, useEffect, useState } from "react";
import { ClientCredentialsContext } from "../../App";
import axios from "axios";
import { Button, Flex, Heading, Input, Text } from "@chakra-ui/react";

interface Props {
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
}

const DeleteUser = ({ setPage, pages }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const getUserId = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/search-user/username=${username}`
    );
    if (!response.data.error) {
      setTargetId(response.data.value[0].id);
    } else {
      console.log(response.data.error);
    }
  };

  const handleDeleteUser = async () => {
    const response = await axios.delete(
      "http://localhost:8080/api/v1/users/admin/delete-user",
      {
        data: {
          clientCredentials: clientCredentials,
          targetId: targetId,
        },
      }
    );
    if (!response.data.error) {
      setErrorMsg(false);
      setMessage("User deleted!");
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  const [username, setUsername] = useState("");
  const [targetId, setTargetId] = useState("");
  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");

  useEffect(() => {
    getUserId();
  }, [username]);

  return (
    <>
      <Heading padding={5} textAlign="center">
        Delete User
      </Heading>
      <Input
        bg="white"
        placeholder="Username"
        value={username}
        onChange={(username) => setUsername(username.target.value)}
      />
      <Button colorScheme="blue" size="lg" onClick={handleDeleteUser}>
        Delete user
      </Button>
      <Button
        colorScheme="blackAlpha"
        size="lg"
        onClick={() => setPage(pages[0])}
      >
        Back
      </Button>
      <Flex justifyContent="center">
        {errorMsg ? <Text color="red">{message}</Text> : <Text>{message}</Text>}
      </Flex>
    </>
  );
};

export default DeleteUser;
