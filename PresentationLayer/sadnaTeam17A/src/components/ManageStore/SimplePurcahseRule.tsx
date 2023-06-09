import React, { useState } from "react";
import PurchaseTerm from "./PurchaseTerm";
import { PurchaseTermType } from "../../types";
import { Button, Stack } from "@chakra-ui/react";

interface Props {
  storeId: string;
  onSubmit: (purchaseTerm: PurchaseTermType) => void;
}

const SimplePurcahseRule = ({ storeId, onSubmit }: Props) => {
  const [purchaseTerm, setPurchaseTerm] = useState<PurchaseTermType>({
    rule: {
      type: "",
      itemIdOrCategoryOrNull: "",
    },
    atLeast: true,
    quantity: 0,
  });

  const onUpdatePurchaseTerm = (updatedPurchaseTerm: PurchaseTermType) => {
    setPurchaseTerm(updatedPurchaseTerm);
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
        (purchaseTerm.rule.type === "BASKET" &&
          purchaseTerm.quantity !== 0)) && (
        <Button onClick={() => onSubmit(purchaseTerm)} colorScheme="blue">
          Submit
        </Button>
      )}
    </Stack>
  );
};

export default SimplePurcahseRule;
