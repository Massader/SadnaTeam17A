import { Button, Flex, Heading, Input, Stack, Text } from "@chakra-ui/react";
import axios from "axios";
import React, { useState, useContext } from "react";
import { ClientCredentialsContext } from "../App";

const CreateStore = () => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const handleCreateStore = async () => {
    const response = await axios.post(
      "http://localhost:8080/api/v1/stores/create-store",
      {
        clientCredentials,
        name: storeName,
        description,
      }
    );
    if (!response.data.error) {
      setErrorMsg(false);
      setMessage(storeName + " created successfully!");
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  const [storeName, setStoreName] = useState("");
  const [description, setDescription] = useState("");
  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");

  return (
    <Flex padding={10} justifyContent="center" alignItems="center">
      <Stack spacing={4} w="100%" maxW="400px" px={4}>
        <Heading padding={5} textAlign="center">
          New Store
        </Heading>
        <Input
          bg="white"
          placeholder="Store name"
          value={storeName}
          onChange={(storeName) => setStoreName(storeName.target.value)}
        />
        <Input
          bg="white"
          placeholder="Store description"
          value={description}
          onChange={(description) => setDescription(description.target.value)}
        />
        <Button colorScheme="blue" size="lg" onClick={handleCreateStore}>
          Create Store
        </Button>
        <Flex justifyContent="center">
          {errorMsg ? (
            <Text color="red">{message}</Text>
          ) : (
            <Text>{message}</Text>
          )}
        </Flex>
      </Stack>
    </Flex>
  );
};

export default CreateStore;
