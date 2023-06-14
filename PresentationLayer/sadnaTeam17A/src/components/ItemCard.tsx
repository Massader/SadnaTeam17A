import {
  Box,
  Button,
  Card,
  CardBody,
  CardFooter,
  Divider,
  Drawer,
  DrawerBody,
  DrawerContent,
  DrawerHeader,
  DrawerOverlay,
  Flex,
  Heading,
  Image,
  Radio,
  RadioGroup,
  Stack,
  Text,
  useDisclosure,
  useToast,
} from "@chakra-ui/react";
import itemIcon from "../assets/item.png";
import { AiOutlineShoppingCart } from "react-icons/ai";
import { useContext, useEffect, useState } from "react";
import axios from "axios";
import { Category, Item, ReviewType } from "../types";
import { ClientCredentialsContext } from "../App";
import Review from "./Review";
import BidItem from "./BidItem";

interface Props {
  item: Item;
}

const ItemCard = ({ item }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const getStoreName = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/store-info/id=${clientCredentials}&storeId=${item.storeId}`
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

  const toast = useToast();

  const handleAddToCart = async () => {
    const response = await axios.post(
      "http://localhost:8080/api/v1/users/add-to-cart",
      {
        clientCredentials: clientCredentials,
        itemId: item.id,
        quantity: 1,
        storeId: item.storeId,
      }
    );
    if (!response.data.error) {
      toast({
        title: `${item.name} added to your cart!`,
        colorScheme: "blue",
        status: "success",
        duration: 3000,
        isClosable: true,
      });
    } else {
      toast({
        title: `${response.data.message}`,
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    }
  };

  const getReviews = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/get-item-reviews/storeId=${item.storeId}&itemId=${item.id}`
    );
    if (!response.data.error) {
      setReviews(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  const getStoreReviews = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/get-store-reviews/storeId=${item.storeId}`
    );
    if (!response.data.error) {
      setReviewsStore(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  const [reviews, setReviews] = useState<ReviewType[]>([]);
  const [reviewsStore, setReviewsStore] = useState<ReviewType[]>([]);

  const [reviewType, setReviewType] = useState("item");

  const [storeName, setStoreName] = useState("");

  const { isOpen, onOpen, onClose } = useDisclosure();

  const {
    isOpen: isOpenBid,
    onOpen: onOpenBid,
    onClose: onCloseBid,
  } = useDisclosure();

  return (
    <Card maxW="sm">
      <CardBody>
        <Flex justifyContent="center">
          <Image src={itemIcon} borderRadius="lg" />
        </Flex>
        <Stack mt="6" spacing="3">
          <Heading size="md">{item.name}</Heading>
          <Text>{storeName}</Text>
          <Text>{item.description}</Text>
          <Flex justifyContent="space-between" alignItems="center">
            <Text color="blue.600" fontSize="2xl">
              ${item.price}
            </Text>
            <Text color="blue.600" fontSize={22}>
              Rating: {item.rating.toFixed(1)}
            </Text>
          </Flex>
        </Stack>
      </CardBody>
      <Divider />
      <CardFooter>
        <Stack width="100%">
          {item.purchaseType === "DIRECT" && (

            <Button
              onClick={() => handleAddToCart()}
              variant="solid"
              colorScheme="blue"
              width="100%"
            >
              <Box marginRight={2}>
                <AiOutlineShoppingCart />
              </Box>
              Add to Cart
            </Button>
          )}
          {item.purchaseType === "BID" && (
            <Button
              onClick={onOpenBid}
              variant="solid"
              colorScheme="blue"
              width="100%"
            >
              <Box marginRight={2}>
                <AiOutlineShoppingCart />
              </Box>
              Add Bid
            </Button>
          )}
          {isOpenBid && (
            <BidItem item={item} isOpen={isOpenBid} onClose={onCloseBid} />
          )}
          <Button
            variant="outline"
            colorScheme="blue"
            onClick={() => {
              onOpen();
              getReviews();
              getStoreReviews();
            }}
          >
            Reviews
          </Button>
          <Drawer
            placement="left"
            onClose={() => {
              onClose();
              setReviewType("item");
            }}
            isOpen={isOpen}
          >
            <DrawerOverlay />
            <DrawerContent>
              <RadioGroup
                padding={3}
                onChange={(value) => {
                  setReviewType(value);
                }}
                value={reviewType}
              >
                <Stack direction="row">
                  <Radio value="item">Item</Radio>
                  <Radio value="store">Store</Radio>
                </Stack>
              </RadioGroup>
              <DrawerHeader borderBottomWidth="1px">
                {reviewType === "item" ? item.name : storeName} Reviews:
              </DrawerHeader>
              <DrawerBody>
                {reviewType === "item" &&
                  reviews.map((review) => (
                    <Review key={review.id} review={review} />
                  ))}
                {reviewType === "store" &&
                  reviewsStore.map((review) => (
                    <Review key={review.id} review={review} />
                  ))}
              </DrawerBody>
            </DrawerContent>
          </Drawer>
        </Stack>
      </CardFooter>
    </Card>
  );
};

export default ItemCard;
