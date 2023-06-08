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
} from "@chakra-ui/react";
import { AllPurchaseTermType } from "../../types";
import ItemNameFromId from "../ItemNameFromId";
import { useContext } from "react";
import { ClientCredentialsContext } from "../../App";
import axios from "axios";

interface Props {
  storeId: string;
  purchaseTerm: AllPurchaseTermType;
  refreshCards: () => {};
}

const PurchaseTermCard = ({ storeId, purchaseTerm, refreshCards }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const deleteCard = async () => {
    const response = await axios.delete(
      "http://localhost:8080/api/v1/stores/remove-policy-term",
      {
        data: {
          clientCredentials: clientCredentials,
          storeId: storeId,
          termId: purchaseTerm.termId,
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
        <Stack mt="6" spacing="3">
          <Heading size="md">{purchaseTerm.termType}</Heading>
          {purchaseTerm.termType === "SIMPLE" && (
            <>
              <Text>Type: {purchaseTerm.rule?.type}</Text>
              {purchaseTerm.rule?.type === "ITEM" && (
                <Flex alignItems="center">
                  <Text mr={2}>Item name:</Text>
                  <ItemNameFromId
                    storeId={storeId}
                    itemId={purchaseTerm.rule.itemIdOrCategoryOrNull}
                  />
                </Flex>
              )}
              {purchaseTerm.rule?.type === "CATEGORY" && (
                <Text>
                  Category name: {purchaseTerm.rule.itemIdOrCategoryOrNull}
                </Text>
              )}
              <Text>Quantity: {purchaseTerm.quantity}</Text>
              <Text>{purchaseTerm.atLeast ? "At least" : "At most"}</Text>
            </>
          )}
          {purchaseTerm.termType === "COMPOSITE" && (
            <>
              {purchaseTerm.purchaseTerms?.map((term, index) => {
                return (
                  <Stack key={index}>
                    <Text>Type: {term.rule?.type}</Text>
                    {term.rule?.type === "ITEM" && (
                      <Flex alignItems="center">
                        <Text mr={2}>Item name:</Text>
                        <ItemNameFromId
                          storeId={storeId}
                          itemId={term.rule.itemIdOrCategoryOrNull}
                        />
                      </Flex>
                    )}
                    {term.rule?.type === "CATEGORY" && (
                      <Text>
                        Category name: {term.rule.itemIdOrCategoryOrNull}
                      </Text>
                    )}
                    <Text>Quantity: {term.quantity}</Text>
                    <Text>{term.atLeast ? "At least" : "At most"}</Text>
                    {purchaseTerm.purchaseTerms?.length &&
                      index !== purchaseTerm.purchaseTerms?.length - 1 && (
                        <Text fontWeight="bold">{purchaseTerm.type}</Text>
                      )}
                  </Stack>
                );
              })}
            </>
          )}
          {purchaseTerm.termType === "CONDITIONAL" && (
            <>
              <Text>Type: {purchaseTerm.ifPurchaseTerm?.rule?.type}</Text>
              {purchaseTerm.ifPurchaseTerm?.rule?.type === "ITEM" && (
                <Text>
                  Item name:{" "}
                  <ItemNameFromId
                    storeId={storeId}
                    itemId={
                      purchaseTerm.ifPurchaseTerm.rule?.itemIdOrCategoryOrNull
                    }
                  />
                </Text>
              )}
              {purchaseTerm.ifPurchaseTerm?.rule?.type === "CATEGORY" && (
                <Text>
                  Category name:
                  {purchaseTerm.ifPurchaseTerm?.rule.itemIdOrCategoryOrNull}
                </Text>
              )}
              <Text>Quantity: {purchaseTerm.ifPurchaseTerm?.quantity}</Text>
              <Text>
                {purchaseTerm.ifPurchaseTerm?.atLeast ? "At least" : "At most"}
              </Text>
              <Text fontWeight="bold">only if:</Text>
              <Text>Type: {purchaseTerm.thenPurchaseTerm?.rule?.type}</Text>
              {purchaseTerm.thenPurchaseTerm?.rule?.type === "ITEM" && (
                <Text>
                  Item name:
                  <ItemNameFromId
                    storeId={storeId}
                    itemId={
                      purchaseTerm.thenPurchaseTerm.rule?.itemIdOrCategoryOrNull
                    }
                  />
                </Text>
              )}
              {purchaseTerm.thenPurchaseTerm?.rule?.type === "CATEGORY" && (
                <Text>
                  Category name:{" "}
                  {purchaseTerm.thenPurchaseTerm?.rule.itemIdOrCategoryOrNull}
                </Text>
              )}
              <Text>Quantity: {purchaseTerm.thenPurchaseTerm?.quantity}</Text>
              <Text>
                {purchaseTerm.thenPurchaseTerm?.atLeast
                  ? "At least"
                  : "At most"}
              </Text>
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

export default PurchaseTermCard;
