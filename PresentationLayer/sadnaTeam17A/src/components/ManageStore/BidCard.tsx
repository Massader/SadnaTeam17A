import {
  Button,
  Card,
  CardBody,
  CardFooter,
  Divider,
  Stack,
  Text,
  Heading,
  Flex,
  Tag,
  Image,
} from "@chakra-ui/react";
import { Bid } from "../../types";
import ItemNameFromId from "../ItemNameFromId";
import { useContext } from "react";
import { ClientCredentialsContext } from "../../App";
import axios from "axios";
import UsernameFromId from "../UsernameFromId";
import bidIcon from "../../assets/BID.png";

interface Props {
  storeId: string;
  bid: Bid;
  refreshBids: () => {};
}

const BidCard = ({ storeId, bid, refreshBids }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const acceptBid = async () => {
    const response = await axios.post(
      "http://localhost:8080/api/v1/stores/accept-item-bid",
      {
        clientCredentials: clientCredentials,
        storeId: storeId,
        itemId: bid.itemId,
        bidderId: bid.bidderId,
        bidPrice: bid.price,
      }
    );
    if (!response.data.error) {
      refreshBids();
    } else {
    }
  };

  console.log(bid);
  return (
    <Card maxW="sm">
      <CardBody>
        <Flex justifyContent="center">
          <Image src={bidIcon} borderRadius="lg" />
        </Flex>
        <Stack mt="6" spacing="3">
          <Flex alignItems="center">
            <Heading size="md" display="flex" alignItems="center">
              Bid on:
              <ItemNameFromId ml={1} storeId={storeId} itemId={bid.itemId} />
            </Heading>
          </Flex>
          <>
            <Flex alignItems="center">
              <Text mr={1}>From:</Text>
              <UsernameFromId userId={bid.bidderId} />
            </Flex>
            <Text>Quantity: {bid.quantity}</Text>
            <Text>Price: ${bid.price}</Text>
          </>
        </Stack>
      </CardBody>
      <Divider />
      <CardFooter>
        <Stack w="100%">
          {!bid.ownersAccepted.includes(clientCredentials) && (
            <Button
              onClick={acceptBid}
              variant="solid"
              colorScheme="blue"
              width="100%"
            >
              Accept
            </Button>
          )}
          {bid.ownersAccepted.includes(clientCredentials) && (
            <Flex justifyContent="center">
              <Tag size="lg" variant="outline" colorScheme="blue">
                Accepted
              </Tag>
            </Flex>
          )}
        </Stack>
      </CardFooter>
    </Card>
  );
};

export default BidCard;
