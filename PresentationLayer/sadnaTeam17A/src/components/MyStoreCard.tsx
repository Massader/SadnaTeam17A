import {
  Button,
  Card,
  CardBody,
  CardFooter,
  Heading,
  Image,
  Stack,
  Text,
} from "@chakra-ui/react";
import storeIcon from "../assets/store.png";

interface Props {
  name: string;
  id: string;
  rating: number;
  onManageStore: () => void;
}

const StoreCard = ({ name, id, rating, onManageStore }: Props) => {
  return (
    <Card direction="row" overflow="hidden" variant="outline">
      <Image objectFit="cover" src={storeIcon} />

      <Stack width="100%" flexWrap="nowrap">
        <CardBody>
          <Heading size="lg">{name}</Heading>
          <Text py="2">Store Owner</Text>
        </CardBody>
        <CardFooter justifyContent="space-between">
          <Text>Rating: {rating}</Text>
          <Button onClick={onManageStore} variant="solid" colorScheme="blue">
            Manage store
          </Button>
        </CardFooter>
      </Stack>
    </Card>
  );
};

export default StoreCard;
