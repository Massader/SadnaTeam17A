import {
  Button,
  Card,
  CardBody,
  CardFooter,
  Divider,
  Flex,
  Heading,
  Image,
  Stack,
  Text,
} from "@chakra-ui/react";
import storeIcon from "../assets/store.png";

interface Props {
  name: string;
  storeId: string;
  description: string;
  rating: number;
  onShop: () => void;
}

const StoreCard = ({ name, storeId, rating, description, onShop }: Props) => {
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
        <Button
          onClick={onShop}
          variant="solid"
          colorScheme="blue"
          width="100%"
        >
          Shop
        </Button>
      </CardFooter>
    </Card>
  );
};

export default StoreCard;
