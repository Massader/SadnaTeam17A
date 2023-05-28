import { Button, Text } from "@chakra-ui/react";
import React, { useState } from "react";
import AddPurchaseRules from "./AddPurchaseRules";

interface Props {
  storeId: string;
}

const ConditioningPurchaseRule = ({ storeId }: Props) => {
  const [firstConditionOn, setFirstConditionOn] = useState(false);
  const [secondConditionOn, setSecondConditionOn] = useState(false);
  const [firstType, setFirstType] = useState("");
  const [secondType, setSecondType] = useState("");
  return (
    <>
      {!firstConditionOn && (
        <>
          <Button
            onClick={() => {
              setFirstType("simple");
              setFirstConditionOn(true);
            }}
            padding={4}
            colorScheme="gray"
          >
            Add simple purchase rule
          </Button>
          <Button
            onClick={() => {
              setFirstType("and");
              setFirstConditionOn(true);
            }}
            padding={4}
            colorScheme="gray"
          >
            Add "And" purchase rule
          </Button>
          <Button
            onClick={() => {
              setFirstType("or");
              setFirstConditionOn(true);
            }}
            padding={4}
            colorScheme="gray"
          >
            Add "Or" purchase rule
          </Button>
        </>
      )}
      {firstConditionOn && (
        <AddPurchaseRules
          storeId={storeId}
          purchaseType={firstType}
          setPurchaseAndDiscountPage={() => {}}
          purchaseAndDiscountPages={["0"]}
        />
      )}
      <Text>Only if:</Text>
      {!secondConditionOn && (
        <>
          <Button
            onClick={() => {
              setSecondType("simple");
              setSecondConditionOn(true);
            }}
            padding={4}
            colorScheme="gray"
          >
            Add simple purchase rule
          </Button>
          <Button
            onClick={() => {
              setSecondType("and");
              setSecondConditionOn(true);
            }}
            padding={4}
            colorScheme="gray"
          >
            Add "And" purchase rule
          </Button>
          <Button
            onClick={() => {
              setSecondType("or");
              setSecondConditionOn(true);
            }}
            padding={4}
            colorScheme="gray"
          >
            Add "Or" purchase rule
          </Button>
        </>
      )}
      {secondConditionOn && (
        <AddPurchaseRules
          storeId={storeId}
          purchaseType={secondType}
          setPurchaseAndDiscountPage={() => {}}
          purchaseAndDiscountPages={["0", "1"]}
        />
      )}
    </>
  );
};

export default ConditioningPurchaseRule;
