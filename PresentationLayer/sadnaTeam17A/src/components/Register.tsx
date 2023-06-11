import { Button, Flex, Heading, Input, Stack, Text } from "@chakra-ui/react";
import axios from "axios";
import { useState } from "react";

interface Props {
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
}

const Register = ({ pages, setPage }: Props) => {
  const handleRegister = async () => {
    const response = await axios.post(
      "http://localhost:8080/api/v1/users/register",
      {
        username,
        password,
      }
    );
    if (!response.data.error) {
      setRegistered(true);
      setErrorMsg(false);
      setMessage(username + " registered successfully!");
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const [registered, setRegistered] = useState(false);

  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");

  return (
    <Flex padding={10} justifyContent="center" alignItems="center">
      <Stack spacing={4} w="100%" maxW="400px" px={4}>
        <Heading padding={5} textAlign="center">
          Register
        </Heading>
        {!registered && (
          <>
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
            <Button colorScheme="blue" size="lg" onClick={handleRegister}>
              Register
            </Button>
          </>
        )}
        {registered && (
          <Button
            colorScheme="blackAlpha"
            size="lg"
            onClick={() => setPage(pages[1])}
          >
            Back
          </Button>
        )}
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

export default Register;
