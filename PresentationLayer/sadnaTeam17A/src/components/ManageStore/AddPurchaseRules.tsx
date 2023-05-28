import React from "react";
import PurchaseTerm from "./PurchaseTerm";
import AndOrPurchaseRules from "./AndOrPurchaseRules";
import { Button } from "@chakra-ui/react";
import ConditioningPurchaseRule from "./ConditioningPurchaseRule";
import { Item, PurchaseRuleType, PurchaseTermType } from "../../types";
import AddSimplePurcahseRule from "./AddSimplePurcahseRule";

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
      {purchaseAndDiscountPages[0] !== "0" && (
        <Button
          onClick={() => {
            setPurchaseAndDiscountPage(purchaseAndDiscountPages[0]);
          }}
          colorScheme="blackAlpha"
        >
          Back
        </Button>
      )}
      {purchaseType === "simple" && <AddSimplePurcahseRule storeId={storeId} />}
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
