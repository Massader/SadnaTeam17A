import React, { useContext, useState } from "react";
import PurchaseTerm from "./PurchaseTerm";
import { PurchaseRuleType, PurchaseTermType } from "../../types";
import { Button, Stack, useToast } from "@chakra-ui/react";
import { ClientCredentialsContext } from "../../App";
import axios from "axios";

interface Props {
  storeId: string;
}

const AddSimplePurcahseRule = ({ storeId }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const [purchaseTerm, setPurchaseTerm] = useState<PurchaseTermType>({
    rule: {
      type: "",
      itemIdOrCategoryOrNull: "",
    },
    atLeast: true,
    quantity: 0,
  });

  const onUpdatePurchaseTerm = (updatedPurchaseTerm: PurchaseTermType) => {
    console.log(updatedPurchaseTerm);
    setPurchaseTerm(updatedPurchaseTerm);
  };

  const toast = useToast();

  const onSumbit = () => {
    console.log(purchaseTerm);
    if (purchaseTerm.rule.type === "BUSKET") addBusketPolictyTerm();
    if (purchaseTerm.rule.type === "ITEM") addItemCategoryPolictyTerm();
    if (purchaseTerm.rule.type === "CATEGORY") addItemCategoryPolictyTerm();
  };

  const addBusketPolictyTerm = async () => {
    const response = await axios.post(
      "http://localhost:8080/api/v1/stores/add-basket-policy-term",
      {
        clientCredentials,
        storeId: storeId,
        quantity: purchaseTerm.quantity,
        atLeast: purchaseTerm.atLeast,
      }
    );
    if (!response.data.error) {
      toast({
        title: "Policy added.",
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

  const addItemCategoryPolictyTerm = async () => {
    const response = await axios.post(
      `http://localhost:8080/api/v1/stores/add-${
        purchaseTerm.rule.type === "ITEM" ? "item" : "category"
      }-policy-term`,
      {
        clientCredentials,
        storeId: storeId,
        itemId: purchaseTerm.rule.itemIdOrCategoryOrNull,
        quantity: purchaseTerm.quantity,
        atLeast: purchaseTerm.atLeast,
      }
    );
    if (!response.data.error) {
      toast({
        title: "Policy added.",
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

  return (
    <Stack>
      <PurchaseTerm
        storeId={storeId}
        purchaseTerm={purchaseTerm}
        onUpdatePurchaseTerm={onUpdatePurchaseTerm}
      />
      {((purchaseTerm.rule.type === "ITEM" &&
        purchaseTerm.rule.itemIdOrCategoryOrNull !== "" &&
        purchaseTerm.quantity !== 0) ||
        (purchaseTerm.rule.type === "CATEGORY" &&
          purchaseTerm.rule.itemIdOrCategoryOrNull !== "" &&
          purchaseTerm.quantity !== 0) ||
        (purchaseTerm.rule.type === "BUSKET" &&
          purchaseTerm.quantity !== 0)) && (
        <Button onClick={onSumbit} colorScheme="blue">
          Submit
        </Button>
      )}
    </Stack>
  );
};

export default AddSimplePurcahseRule;
