import React, { useContext, useState } from "react";
import PurchaseTerm from "./PurchaseTerm";
import { Button, Stack, useToast } from "@chakra-ui/react";
import { PurchaseTermType } from "../../types";
import axios from "axios";
import { ClientCredentialsContext } from "../../App";

interface Props {
  storeId: string;
  purchaseType: string;
}

const AndOrPurchaseRules = ({ purchaseType, storeId }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const [purchaseRules, setPurchaseRules] = useState<
    { id: number; component: React.ReactNode; purchaseTerm: PurchaseTermType }[]
  >([]);
  const [ruleIdCounter, setRuleIdCounter] = useState(0);

  const toast = useToast();

  const addCompositeTerm = async () => {
    const response = await axios.post(
      "http://localhost:8080/api/v1/stores/add-basket-policy-term",
      {
        clientCredentials,
        storeId: storeId,
        term: purchaseRules.map((rule) => rule.purchaseTerm),
      }
    );
    if (!response.data.error) {
      console.log(response.data.value);
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

  const handleAddRule = () => {
    const newRule = {
      id: ruleIdCounter,
      // component: <PurchaseTerm purchaseType={purchaseType} storeId={storeId} />,
    };

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
      {purchaseRules.length > 1 && (
        <Button padding={4} colorScheme="blue" onClick={addCompositeTerm}>
          Submit
        </Button>
      )}
    </Stack>
  );
};

export default AndOrPurchaseRules;
