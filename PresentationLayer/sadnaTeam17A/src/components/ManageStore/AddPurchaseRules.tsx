import React from "react";
import PurchaseRule from "./PurchaseRule";
import AndOrPurchaseRules from "./AndOrPurchaseRules";
import { Button } from "@chakra-ui/react";
import ConditioningPurchaseRule from "./ConditioningPurchaseRule";

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
        <PurchaseRule purchaseType={purchaseType} storeId={storeId} />
      )}
      {purchaseType === "and" && (
        <AndOrPurchaseRules purchaseType={purchaseType} storeId={storeId} />
      )}
      {purchaseType === "or" && (
        <AndOrPurchaseRules purchaseType={purchaseType} storeId={storeId} />
      )}
      {purchaseType === "conditioning" && (
        <ConditioningPurchaseRule storeId={storeId} />
      )}
    </>
  );
};

export default AddPurchaseRules;
