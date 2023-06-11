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
import React, { useContext, useEffect, useState } from "react";
import itemIcon from "../../assets/item.png";
import { PurchasedItemType } from "../../types";
import axios from "axios";
import { ClientCredentialsContext } from "../../App";
import { AiFillStar, AiOutlineStar } from "react-icons/ai";
import { Radio, RadioGroup } from "@chakra-ui/react";

interface Props {
  purchasedItem: PurchasedItemType;
  getPurchaseHistory: () => {};
}

const PurchasedItem = ({ purchasedItem, getPurchaseHistory }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const getItemName = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/item-info/storeId=${purchasedItem.storeId}&itemId=${purchasedItem.itemId}`
    );
    if (!response.data.error) {
      setItemName(response.data.value.name);
    } else {
      console.log(response.data.error);
    }
  };
  useEffect(() => {
    getItemName();
  }, []);

  const getStoreName = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/store-info/id=${clientCredentials}&storeId=${purchasedItem.storeId}`
    );
    if (!response.data.error) {
      setStoreName(response.data.value.name);
    } else {
      console.log(response.data.error);
    }
  };
  useEffect(() => {
    getStoreName();
  }, []);

  const isReviewedItemByUser = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/item-is-reviewable-by-user/id=${clientCredentials}&storeId=${purchasedItem.storeId}&itemId=${purchasedItem.itemId}`
    );
    if (!response.data.error) {
      setReviewedItem(!response.data.value);
      if (!response.data.value) setReviewType("store");
    } else {
      console.log(response.data.error);
    }
  };

  const isReviewedStoreByUser = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/store-is-reviewable-by-user/id=${clientCredentials}&storeId=${purchasedItem.storeId}`
    );
    if (!response.data.error) {
      setReviewedStore(!response.data.value);
      if (!response.data.value) setReviewType("item");
    } else {
      console.log(response.data.error);
    }
  };

  useEffect(() => {
    isReviewedItemByUser();
    isReviewedStoreByUser();
  }, []);

  const postReview = async () => {
    const response = await axios.post(
      `http://localhost:8080/api/v1/stores/post-${
        reviewType === "item" ? "item" : "store"
      }-review`,
      {
        clientCredentials: clientCredentials,
        targetId:
          reviewType === "item" ? purchasedItem.itemId : purchasedItem.storeId,
        body: body,
        rating: rating,
      }
    );
    if (!response.data.error) {
      setErrorMsg(false);
      reviewType === "item" ? setReviewedItem(true) : setReviewedStore(true);
      reviewType === "item" ? setReviewType("store") : setReviewType("item");
      setBody("");
      setRating(0);
      getPurchaseHistory();
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  const toast = useToast();

  const sendComplaint = async () => {
    const response = await axios.post(
      "http://localhost:8080/api/v1/stores/send-complaint",
      {
        clientCredentials: clientCredentials,
        purchaseId: purchasedItem.id,
        storeId: purchasedItem.storeId,
        itemId: purchasedItem.itemId,
        body: body,
      }
    );
    if (!response.data.error) {
      toast({
        title: "Complaint Sent.",
        colorScheme: "blue",
        status: "success",
        duration: 3000,
        isClosable: true,
      });
      setBody("");
    } else {
      toast({
        title: `${response.data.message}`,
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    }
  };

  const [body, setBody] = useState("");

  const handleMouseEnterItem = (index: number) => {
    setRating(index + 1);
  };

  const handleMouseLeaveItem = () => {
    submittedItem !== 0 ? setRating(submittedItem) : setRating(0);
  };

  const handleClickitemRating = (index: number) => {
    if (submittedItem === index + 1) {
      setSubmittedItem(0);
    } else {
      setSubmittedItem(index + 1);
    }
  };

  const [itemName, setItemName] = useState("");
  const [storeName, setStoreName] = useState("");
  const [rating, setRating] = useState(0);
  const [submittedItem, setSubmittedItem] = useState(0);
  const [reviewedItem, setReviewedItem] = useState(false);
  const [reviewedStore, setReviewedStore] = useState(false);
  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");

  const [reviewType, setReviewType] = useState("item");

  const {
    isOpen: reviewModalOpen,
    onOpen: openReviewModal,
    onClose: closeReviewModal,
  } = useDisclosure();
  const {
    isOpen: complaintModalOpen,
    onOpen: openComplaintModal,
    onClose: closeComplaintModal,
  } = useDisclosure();

  return (
    <Card maxW="sm">
      <CardBody>
        <Flex justifyContent="center">
          <Image src={itemIcon} borderRadius="lg" />
        </Flex>
        <Stack mt="6" spacing="3">
          <Heading size="md">{itemName}</Heading>
          <Text>{storeName}</Text>
        </Stack>
      </CardBody>
      <Divider />
      <CardFooter>
        <Stack width="100%" justifyContent="center" alignItems="center">
          {(!reviewedItem || !reviewedStore) && (
            <>
              <Button
                onClick={() => {
                  openReviewModal();
                  setBody("");
                  isReviewedItemByUser();
                  isReviewedStoreByUser();
                }}
                variant="solid"
                colorScheme="blue"
                width="100%"
              >
                Post review
              </Button>
              <Modal isOpen={reviewModalOpen} onClose={closeReviewModal}>
                <ModalOverlay />
                <ModalContent>
                  <ModalHeader>Review {itemName}</ModalHeader>
                  <ModalCloseButton />
                  <ModalBody>
                    <RadioGroup
                      onChange={(value) => {
                        setReviewType(value);
                        setBody("");
                        setRating(0);
                      }}
                      value={reviewType}
                      // defaultValue={reviewType}
                    >
                      <Stack direction="row">
                        <Radio value="item" isDisabled={reviewedItem}>
                          Item
                        </Radio>
                        <Radio value="store" isDisabled={reviewedStore}>
                          Store
                        </Radio>
                      </Stack>
                    </RadioGroup>
                    <Textarea
                      marginTop={2}
                      bg="white"
                      value={body}
                      onChange={(body) => setBody(body.target.value)}
                      placeholder="Message Body"
                      size="sm"
                    />
                    <Flex
                      marginTop={3}
                      justifyContent="center"
                      alignItems="center"
                    >
                      {[...Array(5)].map((_, index) => (
                        <span
                          key={index}
                          onMouseEnter={() => handleMouseEnterItem(index)}
                          onMouseLeave={() => handleMouseLeaveItem()}
                          onClick={() => handleClickitemRating(index)}
                        >
                          {index < rating ? (
                            <AiFillStar color="ffb703" size={30} />
                          ) : (
                            <AiOutlineStar size={30} />
                          )}
                        </span>
                      ))}
                    </Flex>
                  </ModalBody>

                  <ModalFooter justifyContent="space-between">
                    <Button
                      colorScheme="blackAlpha"
                      mr={3}
                      onClick={() => {
                        closeReviewModal();
                        setBody("");
                      }}
                    >
                      Close
                    </Button>
                    <Button
                      onClick={() => {
                        postReview();
                        closeReviewModal();
                      }}
                      colorScheme="blue"
                    >
                      Post
                    </Button>
                  </ModalFooter>
                </ModalContent>
              </Modal>
            </>
          )}
          {reviewedItem && reviewedStore && (
            <Flex justifyContent="space-between" alignItems="center">
              <Text color="blue.600" fontSize="2xl">
                Reviewed
              </Text>
            </Flex>
          )}
          <Button
            onClick={() => {
              openComplaintModal();
              setBody("");
            }}
            variant="outline"
            colorScheme="red"
            width="100%"
          >
            Send Complaint
          </Button>
          <Modal isOpen={complaintModalOpen} onClose={closeComplaintModal}>
            <ModalOverlay />
            <ModalContent>
              <ModalHeader>Send complaint</ModalHeader>
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
                <Button
                  colorScheme="blackAlpha"
                  mr={3}
                  onClick={() => {
                    closeComplaintModal();
                    setBody("");
                  }}
                >
                  Close
                </Button>
                <Button
                  onClick={() => {
                    sendComplaint();
                    closeComplaintModal();
                  }}
                  colorScheme="blue"
                >
                  Send
                </Button>
              </ModalFooter>
            </ModalContent>
          </Modal>
          <Flex justifyContent="center">
            {errorMsg ? (
              <Text color="red">{message}</Text>
            ) : (
              <Text>{message}</Text>
            )}
          </Flex>
        </Stack>
      </CardFooter>
    </Card>
  );
};

export default PurchasedItem;
