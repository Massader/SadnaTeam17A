import React, { useContext } from "react";
import AndOrPurchaseRules from "./AndOrPurchaseRules";
import { Button, useToast } from "@chakra-ui/react";
import ConditionalPurchaseRule from "./ConditionalPurchaseRule";
import {
  CompositePurchaseTermType,
  ConditionalPurchaseTermType,
  DiscountType,
  PurchaseTermType,
} from "../../types";
import SimplePurcahseRule from "./SimplePurcahseRule";
import { ClientCredentialsContext } from "../../App";
import axios from "axios";
import SimpleDiscount from "./SimpleDiscount";
import ConditionalDiscount from "./ConditionalDiscount";

interface Props {
  storeId: string;
  setPurchaseAndDiscountPage: React.Dispatch<React.SetStateAction<string>>;
  purchaseAndDiscountPages: string[];
  purchaseType: string;
}

const AddPurchaseRules = ({
  storeId,
  setPurchaseAndDiscountPage,
  purchaseAndDiscountPages,
  purchaseType,
}: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const toast = useToast();

  const onSumbitSimple = (purchaseTerm: PurchaseTermType) => {
    if (purchaseTerm.rule.type === "BASKET") addBasketPolictyTerm(purchaseTerm);
    if (purchaseTerm.rule.type === "ITEM")
      addItemCategoryPolictyTerm(purchaseTerm);
    if (purchaseTerm.rule.type === "CATEGORY")
      addItemCategoryPolictyTerm(purchaseTerm);
  };

  const addBasketPolictyTerm = async (purchaseTerm: PurchaseTermType) => {
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

  const addItemCategoryPolictyTerm = async (purchaseTerm: PurchaseTermType) => {
    const response = await axios.post(
      `http://localhost:8080/api/v1/stores/add-${
        purchaseTerm.rule.type === "ITEM" ? "item" : "category"
      }-policy-term`,
      {
        clientCredentials,
        storeId: storeId,
        itemId: purchaseTerm.rule.itemIdOrCategoryOrNull,
        category: purchaseTerm.rule.itemIdOrCategoryOrNull,
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

  const addCompositePolictyTerm = async (
    purchaseTerms: CompositePurchaseTermType
  ) => {
    const response = await axios.post(
      "http://localhost:8080/api/v1/stores/add-composite-policy-term",
      {
        clientCredentials,
        storeId: storeId,
        term: purchaseTerms,
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

  const addConditionalPolictyTerm = async (
    conditionalPurchaseTerm: ConditionalPurchaseTermType
  ) => {
    const response = await axios.post(
      "http://localhost:8080/api/v1/stores/add-conditional-policy-term",
      {
        clientCredentials,
        storeId: storeId,
        term: conditionalPurchaseTerm,
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

  const addDiscount = async (discountTerm: DiscountType) => {
    const response = await axios.post(
      "http://localhost:8080/api/v1/stores/add-discount",
      {
        clientCredentials,
        storeId: storeId,
        discount: discountTerm,
      }
    );
    if (!response.data.error) {
      console.log(discountTerm.purchaseTerm);
      toast({
        title: "Discount added.",
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
    <>
      <Button
        onClick={() => {
          setPurchaseAndDiscountPage(purchaseAndDiscountPages[0]);
        }}
        colorScheme="blackAlpha"
      >
        Back
      </Button>
      {purchaseType === "simple" && (
        <SimplePurcahseRule onSubmit={onSumbitSimple} storeId={storeId} />
      )}
      {purchaseType === "and" && (
        <AndOrPurchaseRules
          onSubmit={addCompositePolictyTerm}
          purchaseType={purchaseType}
          storeId={storeId}
        />
      )}
      {purchaseType === "or" && (
        <AndOrPurchaseRules
          onSubmit={addCompositePolictyTerm}
          purchaseType={purchaseType}
          storeId={storeId}
        />
      )}
      {purchaseType === "conditional" && (
        <ConditionalPurchaseRule
          storeId={storeId}
          onSubmit={addConditionalPolictyTerm}
        />
      )}
      {purchaseType === "simpleDiscount" && (
        <SimpleDiscount onSubmit={addDiscount} storeId={storeId} />
      )}
      {purchaseType === "conditionalDiscount" && (
        <ConditionalDiscount onSubmit={addDiscount} storeId={storeId} />
      )}
    </>
  );
};

export default AddPurchaseRules;
