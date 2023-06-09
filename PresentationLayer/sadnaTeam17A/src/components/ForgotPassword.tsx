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
  const { setUsername, setAdmin, setRoles } = useContext(
    ClientCredentialsContext
  );

  const [forgotUsername, setForgotUsername] = useState("");

  const getUserId = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/get-user-by-username/username=${forgotUsername}`
    );
    if (!response.data.error) {
      setUserId(response.data.value.id);
      getQuestion(response.data.value.id);
      setErrorMsg(false);
      setMessage("");
    } else {
      setUserId("");
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  const getUserInfo = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/info/id=${userId}`
    );
    if (!response.data.error) {
      setUsername(response.data.value.username);
      setAdmin(response.data.value.isAdmin);
      setRoles(response.data.value.roles);
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  const getQuestion = async (userId: string) => {
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
        onLogin(userId);
        setErrorMsg(false);
        setMessage(forgotUsername + " logged in successfully!");
      } else {
        setErrorMsg(true);
        setMessage("Wrong answer.");
      }
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  const [userId, setUserId] = useState("");
  const [answer, setAnswer] = useState("");
  const [question, setQuestion] = useState("");
  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");

  return (
    <Flex padding={10} justifyContent="center" alignItems="center">
      <Stack spacing={4} w="100%" maxW="400px" px={4}>
        <Heading padding={5} textAlign="center">
          Forgot Password
        </Heading>
        <Input
          bg="white"
          placeholder="Username"
          value={forgotUsername}
          onChange={(forgotUsername) => {
            setForgotUsername(forgotUsername.target.value);
            setMessage("");
          }}
        />

        <Button colorScheme="blue" size="lg" onClick={getUserId}>
          Get question
        </Button>
        {userId !== "" && (
          <>
            <Text>{question}</Text>
            <Input
              bg="white"
              placeholder="Answer"
              value={answer}
              onChange={(answer) => {
                setAnswer(answer.target.value);
                setMessage("");
              }}
            />
            <Button colorScheme="blue" size="lg" onClick={validateAnswer}>
              Sign In
            </Button>
          </>
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

export default ForgotPassword;
