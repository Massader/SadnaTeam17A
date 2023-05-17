import React, { useState } from "react";
import PurchaseRule from "./PurchaseRule";
import { Button, Stack } from "@chakra-ui/react";

interface Props {
  storeId: string;
  purchaseType: string;
}

const AndOrPurchaseRules = ({ purchaseType, storeId }: Props) => {
  const [purchaseRules, setPurchaseRules] = useState<
    { id: number; component: React.ReactNode }[]
  >([]);
  const [ruleIdCounter, setRuleIdCounter] = useState(0);

  const handleAddRule = () => {
    const newRule = {
      id: ruleIdCounter,
      component: <PurchaseRule purchaseType={purchaseType} storeId={storeId} />,
    };

    setPurchaseRules([...purchaseRules, newRule]);
    setRuleIdCounter((prevCounter) => prevCounter + 1);
  };

  const handleDeleteRule = (id: number) => {
    setPurchaseRules((prevRules) => prevRules.filter((rule) => rule.id !== id));
  };

  return (
    <Stack spacing={4}>
      <Button padding={4} colorScheme="blackAlpha" onClick={handleAddRule}>
        Add rule
      </Button>

      {purchaseRules.map((rule) => (
        <div key={rule.id}>
          {rule.component}
          <Button
            padding={2}
            colorScheme="red"
            onClick={() => handleDeleteRule(rule.id)}
            w="100%"
          >
            Delete
          </Button>
        </div>
      ))}
    </Stack>
  );
};

export default AndOrPurchaseRules;
