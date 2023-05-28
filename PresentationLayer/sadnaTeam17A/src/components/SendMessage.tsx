import {
  Button,
  Flex,
  Input,
  Textarea,
  Text,
  useToast,
} from "@chakra-ui/react";
import React, { useContext, useEffect, useState } from "react";
import { ClientCredentialsContext } from "../App";
import axios from "axios";

interface Props {
  setMessagesPage: React.Dispatch<React.SetStateAction<string>>;
  storeId: string;
  fetchMessages: () => {};
}

const SendMessage = ({ storeId, setMessagesPage, fetchMessages }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const getUserId = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/get-user-by-username/username=${username}`
    );
    if (!response.data.error) {
      setRecipient(response.data.value.id);
    } else {
      setRecipient("");
      console.log(response.data.error);
    }
  };

  const [username, setUsername] = useState("");
  const [body, setBody] = useState("");
  const [recipient, setRecipient] = useState("");

  useEffect(() => {
    getUserId();
  }, [username]);

  const toast = useToast();

  const sendMessage = async () => {
    if (recipient === "") {
      setErrorMsg(true);
      setMessage("User not found");
    } else {
      const response = await axios.post(
        `http://localhost:8080/api/v1/${
          storeId === "" ? "users" : "stores"
        }/messages/send-message`,
        {
          clientCredentials: clientCredentials,
          sender: storeId === "" ? clientCredentials : storeId,
          recipient: recipient,
          body: body,
        }
      );
      if (!response.data.error) {
        setErrorMsg(false);
        setMessage("Message sent!");
        toast({
          title: "Message sent!",
          description: `The message sent to ${username}`,
          position: "top",
          colorScheme: "blue",
          status: "success",
          duration: 4000,
          isClosable: true,
        });
        setMessagesPage("home");
        fetchMessages();
      } else {
        setErrorMsg(true);
        setMessage(response.data.message);
      }
    }
  };

  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");

  return (
    <>
      <>
        <Input
          bg="white"
          placeholder="Username"
          value={username}
          onChange={(username) => {
            setErrorMsg(false);
            setMessage("");
            setUsername(username.target.value);
          }}
        />
        <Textarea
          bg="white"
          value={body}
          onChange={(body) => setBody(body.target.value)}
          placeholder="Message Body"
          size="sm"
        />
        <Flex justifyContent="space-between" alignItems="center">
          <Button
            colorScheme="blackAlpha"
            onClick={() => {
              setMessagesPage("home");
            }}
          >
            Back
          </Button>
          <Button
            colorScheme="blue"
            onClick={() => {
              sendMessage();
            }}
          >
            Send
          </Button>
        </Flex>
      </>
      <Flex justifyContent="center">
        {errorMsg ? <Text color="red">{message}</Text> : <Text>{message}</Text>}
      </Flex>
    </>
  );
};

export default SendMessage;
