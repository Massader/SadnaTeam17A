import { Button, Flex, Heading, Input, Stack, Text } from "@chakra-ui/react";
import { useState, useContext, useEffect } from "react";
import axios from "axios";
import { ClientCredentialsContext } from "../App";

interface Props {
  onLogin: (clientCredentials: string) => void;
  newClientCredentials: (id: string) => void;
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
}

const ForgotPassword = ({
  onLogin,
  newClientCredentials,
  setPage,
  pages,
}: Props) => {
  const { clientCredentials, username, setUsername, setAdmin, setRoles } =
    useContext(ClientCredentialsContext);

  const getUserId = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/-by-username/username=${username}`
    );
    if (!response.data.error) {
      setUserId(response.data.value[0].id);
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  const getUserInfo = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/info/id=${userId}`
    );
    if (!response.data.error) {
      setUsername(username);
      setAdmin(response.data.value.isAdmin);
      setRoles(response.data.value.roles);
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  useEffect(() => {
    getUserId();
  }, [username]);

  useEffect(() => {
    setQuestion("");
  }, [username]);

  const getQuestion = async () => {
    setMessage("");
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/security/get-question/id=${userId}`
    );
    if (!response.data.error) {
      setQuestion(response.data.value);
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  const validateAnswer = async () => {
    setMessage("");
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/security/validate-question/id=${userId}&answer=${answer}`
    );
    if (!response.data.error) {
      if (response.data.value) {
        newClientCredentials(userId);
        getUserInfo();
        setErrorMsg2(false);
        setMessage2(username + " logged in successfully!");
        onLogin(userId);
      } else {
        setErrorMsg2(true);
        setMessage2("Wrong answer.");
      }
    } else {
      setErrorMsg2(true);
      setMessage2(response.data.message);
    }
  };

  const [userId, setUserId] = useState("");
  const [answer, setAnswer] = useState("");
  const [question, setQuestion] = useState("");
  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");
  const [errorMsg2, setErrorMsg2] = useState(false);
  const [message2, setMessage2] = useState("");

  return (
    <Flex padding={10} justifyContent="center" alignItems="center">
      <Stack spacing={4} w="100%" maxW="400px" px={4}>
        <Heading padding={5} textAlign="center">
          Forgot Password
        </Heading>
        <Input
          bg="white"
          placeholder="Username"
          value={username}
          onChange={(username) => setUsername(username.target.value)}
        />
        <Button colorScheme="blue" size="lg" onClick={getQuestion}>
          Get question
        </Button>
        <Text>{question}</Text>
        <Flex justifyContent="center">
          {errorMsg ? (
            <Text color="red">{message}</Text>
          ) : (
            <Text>{message}</Text>
          )}
        </Flex>
        <Input
          bg="white"
          placeholder="Answer"
          value={answer}
          onChange={(answer) => setAnswer(answer.target.value)}
        />
        <Button colorScheme="blue" size="lg" onClick={validateAnswer}>
          Sign In
        </Button>
        <Flex justifyContent="center">
          {errorMsg2 ? (
            <Text color="red">{message2}</Text>
          ) : (
            <Text>{message2}</Text>
          )}
        </Flex>
      </Stack>
    </Flex>
  );
};

export default ForgotPassword;
