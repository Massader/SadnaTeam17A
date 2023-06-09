import React, { useState } from "react";
import Discount from "./Discount";
import { DiscountType } from "../../types";
import { Button, Stack } from "@chakra-ui/react";

interface Props {
  storeId: string;
  onSubmit: (discountTerm: DiscountType) => void;
}

const SimpleDiscount = ({ storeId, onSubmit }: Props) => {
  const [discountTerm, setDiscountTerm] = useState<DiscountType>({
    type: "",
    itemIdOrCategoryOrNull: "",
    discountPercentage: 0,
  });

  const onUpdateDiscountTerm = (updatedDiscountTerm: DiscountType) => {
    setDiscountTerm(updatedDiscountTerm);
  };

  return (
    <Stack>
      <Discount
        storeId={storeId}
        discountTerm={discountTerm}
        onUpdateDiscountTerm={onUpdateDiscountTerm}
      />
      {((discountTerm.type === "ITEM" &&
        discountTerm.itemIdOrCategoryOrNull !== "" &&
        discountTerm.discountPercentage !== 0) ||
        (discountTerm.type === "CATEGORY" &&
          discountTerm.itemIdOrCategoryOrNull !== "" &&
          discountTerm.discountPercentage !== 0) ||
        (discountTerm.type === "BASKET" &&
          discountTerm.discountPercentage !== 0)) && (
        <Button onClick={() => onSubmit(discountTerm)} colorScheme="blue">
          Submit
        </Button>
      )}
    </Stack>
  );
};

export default SimpleDiscount;
