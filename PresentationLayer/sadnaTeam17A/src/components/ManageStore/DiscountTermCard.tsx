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
  Image,
} from "@chakra-ui/react";
import simpleDiscountIcon from "../../assets/simpleDiscount.png";
import conditionalDiscountIcon from "../../assets/conditionalDiscount.png";
import { DiscountType } from "../../types";
import ItemNameFromId from "../ItemNameFromId";
import { useContext } from "react";
import { ClientCredentialsContext } from "../../App";
import axios from "axios";

interface Props {
  storeId: string;
  discountTerm: DiscountType;
  refreshCards: () => {};
}

const DiscountTermCard = ({ storeId, discountTerm, refreshCards }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const deleteCard = async () => {
    const response = await axios.delete(
      "http://localhost:8080/api/v1/stores/remove-discount",
      {
        data: {
          clientCredentials: clientCredentials,
          storeId: storeId,
          discountId: discountTerm.id,
        },
      }
    );
    if (!response.data.error) {
      refreshCards();
    } else {
    }
  };
  return (
    <Card maxW="sm">
      <CardBody>
        <Flex justifyContent="center">
          {discountTerm.purchaseTerm !== null ? (
            <Image src={conditionalDiscountIcon} borderRadius="lg" />
          ) : (
            <Image src={simpleDiscountIcon} borderRadius="lg" />
          )}
        </Flex>
        <Stack mt="6" spacing="3">
          {discountTerm.purchaseTerm === null && (
            <>
              <Text>Type: {discountTerm.type}</Text>
              {discountTerm.type === "ITEM" && (
                <Flex alignItems="center">
                  <Text mr={1}>Item name:</Text>
                  <ItemNameFromId
                    storeId={storeId}
                    itemId={discountTerm.itemIdOrCategoryOrNull}
                  />
                </Flex>
              )}
              {discountTerm.type === "CATEGORY" && (
                <Text>
                  Category name: {discountTerm.itemIdOrCategoryOrNull}
                </Text>
              )}
              <Text>Percentage: {discountTerm.discountPercentage}%</Text>
            </>
          )}
          {discountTerm.purchaseTerm !== null && (
            <>
              <Text fontWeight="bold">if:</Text>
              <Text>Type: {discountTerm.purchaseTerm?.rule?.type}</Text>
              {discountTerm.purchaseTerm?.rule?.type === "ITEM" && (
                <Text>
                  Item name:{" "}
                  <ItemNameFromId
                    storeId={storeId}
                    itemId={
                      discountTerm.purchaseTerm?.rule?.itemIdOrCategoryOrNull
                    }
                  />
                </Text>
              )}
              {discountTerm.purchaseTerm?.rule?.type === "CATEGORY" && (
                <Text>
                  Category name:
                  {discountTerm.purchaseTerm?.rule.itemIdOrCategoryOrNull}
                </Text>
              )}
              <Text>Quantity: {discountTerm.purchaseTerm?.quantity}</Text>
              <Text>
                {discountTerm.purchaseTerm?.atLeast ? "At least" : "At most"}
              </Text>
              <Text fontWeight="bold">then:</Text>
              <Text>Type: {discountTerm.type}</Text>
              {discountTerm.type === "ITEM" && (
                <Flex alignItems="center">
                  <Text mr={1}>Item name:</Text>
                  <ItemNameFromId
                    storeId={storeId}
                    itemId={discountTerm.itemIdOrCategoryOrNull}
                  />
                </Flex>
              )}
              {discountTerm.type === "CATEGORY" && (
                <Text>
                  Category name: {discountTerm.itemIdOrCategoryOrNull}
                </Text>
              )}
              <Text>Percentage: {discountTerm.discountPercentage}%</Text>
            </>
          )}
        </Stack>
      </CardBody>
      <Divider />
      <CardFooter>
        <Stack w="100%">
          <Button
            onClick={deleteCard}
            variant="solid"
            colorScheme="red"
            width="100%"
          >
            Delete
          </Button>
        </Stack>
      </CardFooter>
    </Card>
  );
};

export default DiscountTermCard;
