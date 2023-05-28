import React, { useContext, useEffect, useState } from "react";
import { ClientCredentialsContext } from "../../App";
import axios from "axios";
import { Button, Flex, Heading, Input, Stack, Text } from "@chakra-ui/react";

interface Props {
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
}

const DeleteUser = ({ setPage, pages }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const getUserId = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/get-user-by-username/username=${username}`
    );
    if (!response.data.error) {
      setTargetId(response.data.value.id);
    } else {
      setTargetId("");
      console.log(response.data.error);
    }
  };

  const handleDeleteUser = async () => {
    if (targetId === "") {
      setErrorMsg(true);
      setMessage("User not found");
    } else {
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
      <Stack w="50%" maxW="400px">
        <Heading padding={5} textAlign="center">
          Delete User
        </Heading>
        <Input
          bg="white"
          placeholder="Username"
          value={username}
          onChange={(username) => {
            setUsername(username.target.value);
            setErrorMsg(false);
            setMessage("");
          }}
        />
        <Button colorScheme="blue" size="lg" onClick={handleDeleteUser}>
          Delete User
        </Button>
        <Button
          colorScheme="blackAlpha"
          size="lg"
          onClick={() => setPage(pages[0])}
        >
          Back
        </Button>
        <Flex justifyContent="center">
          {errorMsg ? (
            <Text color="red">{message}</Text>
          ) : (
            <Text>{message}</Text>
          )}
        </Flex>
      </Stack>
    </>
  );
};

export default DeleteUser;
