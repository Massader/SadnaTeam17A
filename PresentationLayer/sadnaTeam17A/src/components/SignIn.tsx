import { Button, Flex, Heading, Input, Stack, Text } from "@chakra-ui/react";
import { useState, useContext } from "react";
import axios from "axios";
import { ClientCredentialsContext } from "../App";

interface Props {
  onLogin: () => void;
  newClientCredentials: (id: string) => void;
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
}

const SignIn = ({ onLogin, newClientCredentials, setPage, pages }: Props) => {
  const { clientCredentials, username, setUsername, setAdmin, setRoles } =
    useContext(ClientCredentialsContext);

  const handleSignIn = async () => {
    const response = await axios.post(
      "http://localhost:8080/api/v1/users/login",
      {
        clientCredentials,
        username,
        password,
      }
    );
    if (!response.data.error) {
      console.log(response.data.value);
      newClientCredentials(response.data.value.id);
      setUsername(response.data.value.username);
      setAdmin(response.data.value.isAdmin);
      setRoles(response.data.value.roles);
      setErrorMsg(false);
      setMessage(username + " logged in successfully!");
      onLogin();
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  const [password, setPassword] = useState("");
  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");

  return (
    <Flex padding={10} justifyContent="center" alignItems="center">
      <Stack spacing={4} w="100%" maxW="400px" px={4}>
        <Heading padding={5} textAlign="center">
          LOG IN
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
          type="password"
          value={password}
          onChange={(password) => setPassword(password.target.value)}
        />
        <Button colorScheme="blue" size="lg" onClick={handleSignIn}>
          Sign In
        </Button>
        <Flex justifyContent="space-between" alignItems="center">
          <Button
            onClick={() => setPage(pages[3])}
            fontSize="sm"
            fontWeight="bold"
          >
            REGISTER
          </Button>
          <Button
            onClick={() => setPage(pages[9])}
            fontSize="sm"
            fontWeight="bold"
          >
            FORGOT?
          </Button>
        </Flex>
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

export default SignIn;
