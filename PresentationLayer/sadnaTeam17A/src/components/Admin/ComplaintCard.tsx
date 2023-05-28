import {
  Button,
  Card,
  CardBody,
  CardFooter,
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
import complaintIcon from "../../assets/complaint.png";
import { ComplaintType, Role, Store } from "../../types";
import { useContext, useEffect, useState } from "react";
import { ClientCredentialsContext } from "../../App";
import axios from "axios";
import SendMessage from "../SendMessage";

interface Props {
  complaint: ComplaintType;
  fetchComplaints: () => {};
}

const ComplaintCard = ({ fetchComplaints, complaint }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const toast = useToast();

  const sendMessage = async () => {
    const response = await axios.post(
      `http://localhost:8080/api/v1/users/messages/send-message`,
      {
        clientCredentials: clientCredentials,
        sender: clientCredentials,
        recipient: complaint.sender,
        body: body,
      }
    );
    if (!response.data.error) {
      toast({
        title: "Message Sent.",
        colorScheme: "blue",
        status: "success",
        duration: 3000,
        isClosable: true,
      });
      setBody("");
      fetchComplaints();
    } else {
      toast({
        title: `${response.data.message}`,
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    }
  };

  const assignAdminToComplaint = async () => {
    const response = await axios.put(
      "http://localhost:8080/api/v1/stores/admin/assign-admin-to-complaint",
      {
        clientCredentials: clientCredentials,
        targetId: complaint.id,
      }
    );
    if (!response.data.error) {
      console.log(response.data.value);
      fetchComplaints();
    } else {
      console.log(response.data.message);
    }
  };

  const closeComplaint = async () => {
    const response = await axios.put(
      "http://localhost:8080/api/v1/stores/admin/close-complaint",
      {
        clientCredentials: clientCredentials,
        targetId: complaint.id,
      }
    );
    if (!response.data.error) {
      console.log(response.data.value);
      fetchComplaints();
    } else {
      console.log(response.data.message);
    }
  };

  const reopenComplaint = async () => {
    const response = await axios.put(
      "http://localhost:8080/api/v1/stores/admin/reopen-complaint",
      {
        clientCredentials: clientCredentials,
        targetId: complaint.id,
      }
    );
    if (!response.data.error) {
      console.log(response.data.value);
      fetchComplaints();
    } else {
      console.log(response.data.message);
    }
  };

  const getSenderInfo = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/info/id=${complaint.sender}`
    );
    if (!response.data.error) {
      setSender(response.data.value.username);
    } else {
      console.log(response.data.message);
    }
  };

  const getAdminInfo = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/info/id=${complaint.assignedAdmin}`
    );
    if (!response.data.error) {
      setAssigned(response.data.value.username);
    } else {
      setAssigned("");
    }
  };

  useEffect(() => {
    getSenderInfo();
    getAdminInfo();
  }, []);

  const [sender, setSender] = useState("");
  const [assigned, setAssigned] = useState("");
  const [body, setBody] = useState("");

  const { isOpen, onOpen, onClose } = useDisclosure();

  return (
    <Card direction="row" overflow="hidden" variant="outline">
      <Flex alignItems="center">
        <Image
          objectFit="contain"
          maxH="130px"
          maxW="130px"
          src={complaintIcon}
        />
      </Flex>

      <Stack width="100%" flexWrap="nowrap">
        <CardBody>
          <Heading size="lg">Complaint from {sender}</Heading>
          {complaint.assignedAdmin !== "" && (
            <Text>Assigned to: {assigned}</Text>
          )}
          <span></span>
          <Text marginTop={2}>{complaint.body}</Text>
        </CardBody>
        <CardFooter justifyContent="space-between">
          <Flex align="center">
            <Text mr={2}>
              Complaint is {complaint.open ? "open" : "closed"}
            </Text>
            {complaint.assignedAdmin === clientCredentials &&
              complaint.open && (
                <Button
                  onClick={closeComplaint}
                  colorScheme="blackAlpha"
                  variant="outline"
                >
                  Close
                </Button>
              )}
            {complaint.assignedAdmin === clientCredentials &&
              !complaint.open && (
                <Button
                  onClick={reopenComplaint}
                  colorScheme="blackAlpha"
                  variant="outline"
                >
                  Reopen
                </Button>
              )}
          </Flex>
          {complaint.assignedAdmin !== clientCredentials && (
            <Button onClick={assignAdminToComplaint} colorScheme="blackAlpha">
              Assign
            </Button>
          )}
          {complaint.assignedAdmin === clientCredentials && (
            <>
              <Button
                onClick={() => {
                  onOpen();
                  setBody("");
                }}
                colorScheme="blue"
              >
                Message
              </Button>
              <Modal isOpen={isOpen} onClose={onClose}>
                <ModalOverlay />
                <ModalContent>
                  <ModalHeader>Send Message</ModalHeader>
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
                        sendMessage();
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
        </CardFooter>
      </Stack>
    </Card>
  );
};

export default ComplaintCard;
