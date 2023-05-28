import {
  Button,
  Card,
  CardBody,
  CardFooter,
  Divider,
  Flex,
  Heading,
  Image,
  Modal,
  ModalBody,
  ModalCloseButton,
  ModalContent,
  ModalFooter,
  ModalHeader,
  ModalOverlay,
  Stack,
  Text,
  Textarea,
  useDisclosure,
  useToast,
} from "@chakra-ui/react";
import storeIcon from "../assets/store.png";
import React, { useContext, useState } from "react";
import { ClientCredentialsContext } from "../App";
import axios from "axios";

interface Props {
  name: string;
  storeId: string;
  description: string;
  rating: number;
  onShop: () => void;
}

const StoreCard = ({ name, storeId, rating, description, onShop }: Props) => {
  const { clientCredentials, isAdmin, isLogged } = useContext(
    ClientCredentialsContext
  );

  const handleShutdown = async () => {
    const response = await axios.put(
      "http://localhost:8080/api/v1/stores/admin/shutdown-store",
      {
        clientCredentials: clientCredentials,
        targetId: storeId,
      }
    );
    if (!response.data.error) {
      setErrorMsg(false);
      setMessage("Store Shutdowned!");
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  const toast = useToast();

  const sendMessageToStore = async () => {
    const response = await axios.post(
      "http://localhost:8080/api/v1/stores/messages/send-message",
      {
        clientCredentials: clientCredentials,
        sender: clientCredentials,
        recipient: storeId,
        body: body,
      }
    );
    if (!response.data.error) {
      toast({
        title: "Message sent!",
        description: `The message sent to ${name}`,
        position: "top",
        colorScheme: "blue",
        status: "success",
        duration: 4000,
        isClosable: true,
      });
    } else {
      toast({
        title: "Message sent!",
        description: `${response.data.value.message}`,
        position: "top",
        status: "error",
        duration: 4000,
        isClosable: true,
      });
    }
  };

  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");

  const [body, setBody] = useState("");
  const { isOpen, onOpen, onClose } = useDisclosure();

  return (
    <Card maxW="sm">
      <CardBody>
        <Flex justifyContent="center">
          <Image src={storeIcon} borderRadius="lg" />
        </Flex>
        <Stack mt="6" spacing="3">
          <Heading size="md">{name}</Heading>
          <Text>{description}</Text>
          <Text color="blue.600" fontSize="2xl">
            Rating: {rating}
          </Text>
        </Stack>
      </CardBody>
      <Divider />
      <CardFooter>
        <Stack w="100%">
          <Button
            onClick={onShop}
            variant="solid"
            colorScheme="blue"
            width="100%"
          >
            Shop
          </Button>
          {isLogged && (
            <>
              {" "}
              <Button
                onClick={onOpen}
                variant="outline"
                colorScheme="blue"
                width="100%"
              >
                Send Message
              </Button>
              <Modal isOpen={isOpen} onClose={onClose}>
                <ModalOverlay />
                <ModalContent>
                  <ModalHeader>Send message to {name}</ModalHeader>
                  <ModalCloseButton />
                  <ModalBody>
                    <Textarea
                      bg="white"
                      value={body}
                      onChange={(body) => setBody(body.target.value)}
                      placeholder="Message Body"
                      size="sm"
                    />
                  </ModalBody>

                  <ModalFooter justifyContent="space-between">
                    <Button colorScheme="blackAlpha" mr={3} onClick={onClose}>
                      Close
                    </Button>
                    <Button
                      onClick={() => {
                        sendMessageToStore();
                        onClose();
                      }}
                      colorScheme="blue"
                    >
                      Send
                    </Button>
                  </ModalFooter>
                </ModalContent>
              </Modal>
            </>
          )}
          {isAdmin && (
            <>
              <Button
                onClick={handleShutdown}
                variant="solid"
                colorScheme="blackAlpha"
                width="100%"
              >
                Shutdown store
              </Button>
              <Flex justifyContent="center">
                {errorMsg ? (
                  <Text color="red">{message}</Text>
                ) : (
                  <Text>{message}</Text>
                )}
              </Flex>
            </>
          )}
        </Stack>
      </CardFooter>
    </Card>
  );
};

export default StoreCard;
