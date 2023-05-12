import axios from "axios";
import React, { useContext, useState } from "react";
import { ClientCredentialsContext } from "../../App";
import { Button, Flex, Heading, Input, Text } from "@chakra-ui/react";

interface Props {
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
}

const RegisterAdmin = ({ setPage, pages }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const handleRegisterAdmin = async () => {
    const response = await axios.post(
      "http://localhost:8080/api/v1/users/admin/register",
      {
        clientCredentials: clientCredentials,
        username: username,
        password: password,
      }
    );
    if (!response.data.error) {
      setErrorMsg(false);
      setMessage("New admin registered!");
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");

  return (
    <>
      <Heading padding={5} textAlign="center">
        Register Admin
      </Heading>
      <Input
        bg="white"
        placeholder="Username"
        value={username}
        onChange={(username) => setUsername(username.target.value)}
      />
      <Input
        bg="white"
        placeholder="Password"
        value={password}
        type="password"
        onChange={(password) => setPassword(password.target.value)}
      />
      <Button colorScheme="blue" size="lg" onClick={handleRegisterAdmin}>
        Register admin
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

export default RegisterAdmin;
