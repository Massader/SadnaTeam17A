import React, { useState } from "react";
import Discount from "./Discount";
import { DiscountType, PurchaseTermType } from "../../types";
import { Button, Stack, Text } from "@chakra-ui/react";
import PurchaseTerm from "./PurchaseTerm";

interface Props {
  storeId: string;
  onSubmit: (discountTerm: DiscountType) => void;
}

const ConditionalDiscount = ({ storeId, onSubmit }: Props) => {
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

  const [discountTerm, setDiscountTerm] = useState<DiscountType>({
    type: "",
    itemIdOrCategoryOrNull: "",
    discountPercentage: 0,
    purchaseTerm: purchaseTerm,
  });

  const onUpdateDiscountTerm = (updatedDiscountTerm: DiscountType) => {
    setDiscountTerm(updatedDiscountTerm);
  };

  return (
    <Stack>
      <Text>if:</Text>
      <PurchaseTerm
        storeId={storeId}
        purchaseTerm={purchaseTerm}
        onUpdatePurchaseTerm={onUpdatePurchaseTerm}
      />
      <Text>then:</Text>
      <Discount
        storeId={storeId}
        discountTerm={discountTerm}
        onUpdateDiscountTerm={onUpdateDiscountTerm}
      />
      {((purchaseTerm.rule.type === "ITEM" &&
        purchaseTerm.rule.itemIdOrCategoryOrNull !== "" &&
        purchaseTerm.quantity !== 0) ||
        (purchaseTerm.rule.type === "CATEGORY" &&
          purchaseTerm.rule.itemIdOrCategoryOrNull !== "" &&
          purchaseTerm.quantity !== 0) ||
        (purchaseTerm.rule.type === "BASKET" && purchaseTerm.quantity !== 0)) &&
        ((discountTerm.type === "ITEM" &&
          discountTerm.itemIdOrCategoryOrNull !== "" &&
          discountTerm.discountPercentage !== 0) ||
          (discountTerm.type === "CATEGORY" &&
            discountTerm.itemIdOrCategoryOrNull !== "" &&
            discountTerm.discountPercentage !== 0) ||
          (discountTerm.type === "BASKET" &&
            discountTerm.discountPercentage !== 0)) && (
          <Button
            onClick={() =>
              onSubmit({
                type: discountTerm.type,
                itemIdOrCategoryOrNull: discountTerm.itemIdOrCategoryOrNull,
                discountPercentage: discountTerm.discountPercentage,
                purchaseTerm: purchaseTerm,
              })
            }
            colorScheme="blue"
          >
            Submit
          </Button>
        )}
    </Stack>
  );
};

export default ConditionalDiscount;
