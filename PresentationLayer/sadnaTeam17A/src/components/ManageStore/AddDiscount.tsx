import { Button } from "@chakra-ui/react";
import React from "react";
import Discount from "./Discount";

interface Props {
  storeId: string;
  setPurchaseAndDiscountPage: React.Dispatch<React.SetStateAction<string>>;
  purchaseAndDiscountPages: string[];
  discountType: string;
}

const AddDiscount = ({
  storeId,
  setPurchaseAndDiscountPage,
  purchaseAndDiscountPages,
  discountType,
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
      {discountType === "simple" && <Discount storeId={storeId} />}
      {/* {discountType === "conditioning" && (
        <ConditioningDiscount storeId={storeId} />
      )} */}
    </>
  );
};

export default AddDiscount;
