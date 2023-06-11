import {
  Button,
  Card,
  CardBody,
  CardFooter,
  Flex,
  Heading,
  Image,
  Stack,
  Text,
  Textarea,
} from "@chakra-ui/react";
import messageIcon from "../assets/message.png";
import { MessageType, Role, Store } from "../types";
import { useContext, useEffect, useState } from "react";
import { ClientCredentialsContext } from "../App";
import axios from "axios";

interface Props {
  message: MessageType;
  storeId: string;
  fetchMessages: () => {};
}

const MessageCard = ({ fetchMessages, message, storeId }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const sendMessage = async () => {
    const response = await axios.post(
      `http://localhost:8080/api/v1/${
        storeId === "" ? "users" : "stores"
      }/messages/send-message`,
      {
        clientCredentials: clientCredentials,
        sender: storeId === "" ? clientCredentials : storeId,
        recipient: message.sender,
        body: body,
      }
    );
    if (!response.data.error) {
      setErrorMsg(false);
      setSuccessSent(true);
      setMessageShow("Message sent!");
      console.log(response.data.message);
      fetchMessages();
    } else {
      setErrorMsg(true);
      setMessageShow(response.data.message);
    }
  };

  const [successSent, setSuccessSent] = useState(false);
  const [errorMsg, setErrorMsg] = useState(false);
  const [messageShow, setMessageShow] = useState("");

  const getSenderInfo = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/info/id=${message.sender}`
    );
    if (!response.data.error) {
      setSender(response.data.value.username);
    } else {
      getStoreName();
    }
  };

  const getStoreName = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/store-info/id=${clientCredentials}&storeId=${message.sender}`
    );
    if (!response.data.error) {
      setSender(response.data.value.name);
    } else {
      console.log(response.data.error);
    }
  };

  useEffect(() => {
    getSenderInfo();
  }, []);

  const [sender, setSender] = useState("");

  const [replyOn, setReplyOn] = useState(false);
  const [body, setBody] = useState("");

  return (
    <Card direction="row" overflow="hidden" variant="outline">
      <Flex alignItems="center">
        <Image
          objectFit="contain"
          maxH="130px"
          maxW="130px"
          src={messageIcon}
        />
      </Flex>

      <Stack width="100%" flexWrap="nowrap">
        <CardBody>
          <Heading size="lg">Message from {sender}</Heading>
          <Text>{message.body}</Text>
        </CardBody>
        <CardFooter justifyContent="space-between">
          {!replyOn && (
            <Button
              onClick={() => {
                setReplyOn(!replyOn);
              }}
              variant="solid"
              colorScheme="blue"
            >
              Reply
            </Button>
          )}
          {replyOn && !successSent && (
            <>
              <Textarea
                bg="white"
                value={body}
                onChange={(body) => setBody(body.target.value)}
                placeholder="Message Body"
                size="sm"
              />
              <Stack>
                <Button
                  onClick={() => {
                    sendMessage();
                  }}
                  colorScheme="blue"
                >
                  Send
                </Button>
                <Button
                  onClick={() => {
                    setReplyOn(false);
                    setBody("");
                  }}
                  colorScheme="blackAlpha"
                >
                  Cancel
                </Button>
              </Stack>
            </>
          )}
          {errorMsg ? (
            <Text color="red">{messageShow}</Text>
          ) : (
            <Text>{messageShow}</Text>
          )}
        </CardFooter>
      </Stack>
    </Card>
  );
};

export default MessageCard;
