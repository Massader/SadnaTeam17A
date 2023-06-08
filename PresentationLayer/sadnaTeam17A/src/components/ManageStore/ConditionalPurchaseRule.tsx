import { Button, Stack, Text } from "@chakra-ui/react";
import React, { useState } from "react";
import { ConditionalPurchaseTermType, PurchaseTermType } from "../../types";
import PurchaseTerm from "./PurchaseTerm";

interface Props {
  storeId: string;
  onSubmit: (conditionalTerm: ConditionalPurchaseTermType) => void;
}

const ConditionalPurchaseRule = ({ storeId, onSubmit }: Props) => {
  const [purchaseTerm1, setPurchaseTerm1] = useState<PurchaseTermType>({
    rule: {
      type: "",
      itemIdOrCategoryOrNull: "",
    },
    atLeast: true,
    quantity: 0,
  });

  const onUpdatePurchaseTerm1 = (updatedPurchaseTerm: PurchaseTermType) => {
    setPurchaseTerm1(updatedPurchaseTerm);
  };

  const [purchaseTerm2, setPurchaseTerm2] = useState<PurchaseTermType>({
    rule: {
      type: "",
      itemIdOrCategoryOrNull: "",
    },
    atLeast: true,
    quantity: 0,
  });

  const onUpdatePurchaseTerm2 = (updatedPurchaseTerm: PurchaseTermType) => {
    setPurchaseTerm2(updatedPurchaseTerm);
  };

  return (
    <>
      <Stack>
        <Text>if:</Text>
        <PurchaseTerm
          storeId={storeId}
          purchaseTerm={purchaseTerm1}
          onUpdatePurchaseTerm={onUpdatePurchaseTerm1}
        />
        <Text>then:</Text>
        <PurchaseTerm
          storeId={storeId}
          purchaseTerm={purchaseTerm2}
          onUpdatePurchaseTerm={onUpdatePurchaseTerm2}
        />
        {((purchaseTerm1.rule.type === "ITEM" &&
          purchaseTerm1.rule.itemIdOrCategoryOrNull !== "" &&
          purchaseTerm1.quantity !== 0) ||
          (purchaseTerm1.rule.type === "CATEGORY" &&
            purchaseTerm1.rule.itemIdOrCategoryOrNull !== "" &&
            purchaseTerm1.quantity !== 0) ||
          (purchaseTerm1.rule.type === "BASKET" &&
            purchaseTerm1.quantity !== 0)) &&
          ((purchaseTerm2.rule.type === "ITEM" &&
            purchaseTerm2.rule.itemIdOrCategoryOrNull !== "" &&
            purchaseTerm2.quantity !== 0) ||
            (purchaseTerm2.rule.type === "CATEGORY" &&
              purchaseTerm2.rule.itemIdOrCategoryOrNull !== "" &&
              purchaseTerm2.quantity !== 0) ||
            (purchaseTerm2.rule.type === "BASKET" &&
              purchaseTerm2.quantity !== 0)) && (
            <Button
              onClick={() =>
                onSubmit({
                  ifPurchaseTerm: purchaseTerm1,
                  thenPurchaseTerm: purchaseTerm2,
                })
              }
              colorScheme="blue"
            >
              Submit
            </Button>
          )}
      </Stack>
    </>
  );
};

export default ConditionalPurchaseRule;
