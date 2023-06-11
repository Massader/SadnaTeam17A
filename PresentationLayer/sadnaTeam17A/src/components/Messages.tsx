import React, { useContext, useEffect, useState } from "react";
import { ClientCredentialsContext } from "../App";
import axios from "axios";
import { Button, Flex, Stack } from "@chakra-ui/react";
import SendMessage from "./SendMessage";
import { MessageType } from "../types";
import MessageCard from "./MessageCard";

interface Props {
  storeId: string;
}

const Messages = ({ storeId }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const messagesPages = ["home", "sendMessageToUser"];
  const [messagesPage, setMessagesPage] = useState(messagesPages[0]);

  const fetchMessages = async () => {
    const response = await axios.get(
      storeId === ""
        ? `http://localhost:8080/api/v1/users/messages/get-messages/id=${clientCredentials}`
        : `http://localhost:8080/api/v1/stores/messages/get-messages/id=${clientCredentials}&storeId=${storeId}`
    );
    if (!response.data.error) {
      setMessages(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  const [messages, setMessages] = useState<MessageType[]>([]);

  useEffect(() => {
    fetchMessages();
  }, [storeId]);

  return (
    <>
      <Flex marginTop={4} justifyContent="center">
        <Stack w="70%">
          {messagesPage === "home" && (
            <>
              <Button
                colorScheme="blue"
                onClick={() => {
                  setMessagesPage(messagesPages[1]);
                }}
              >
                Send message to user
              </Button>
              {messages.reverse().map((message) => (
                <MessageCard
                  fetchMessages={fetchMessages}
                  message={message}
                  key={message.id}
                  storeId={storeId}
                />
              ))}
            </>
          )}
        </Stack>
      </Flex>
      <Flex marginTop={4} justifyContent="center">
        <Stack w="70%" maxW="400px">
          {messagesPage === "sendMessageToUser" && (
            <SendMessage
              fetchMessages={fetchMessages}
              storeId={storeId}
              setMessagesPage={setMessagesPage}
            />
          )}
        </Stack>
      </Flex>
    </>
  );
};

export default Messages;
