import React, { useContext, useState } from "react";
import PurchaseTerm from "./PurchaseTerm";
import { Button, Flex, Stack, Text, useToast } from "@chakra-ui/react";
import { CompositePurchaseTermType, PurchaseTermType } from "../../types";
import axios from "axios";
import { ClientCredentialsContext } from "../../App";

interface Props {
  storeId: string;
  purchaseType: string;
  onSubmit: (compositePurchaseTerm: CompositePurchaseTermType) => void;
}

const AndOrPurchaseRules = ({ storeId, purchaseType, onSubmit }: Props) => {
  const [purchaseTerms, setPurchaseTerms] = useState<PurchaseTermType[]>([]);
  const [termIndex, setTermIndex] = useState(0);

  const addPurchaseTerm = () => {
    const newTermId = termIndex + 1;
    const newTerm = {
      id: newTermId,
      rule: {
        type: "",
        itemIdOrCategoryOrNull: "",
      },
      atLeast: true,
      quantity: 0,
    };
    setPurchaseTerms([...purchaseTerms, newTerm]);
    setTermIndex(newTermId);
  };

  const deletePurchaseTerm = (termId: number) => {
    setPurchaseTerms((prevTerms) =>
      prevTerms.filter((term) => term.id !== termId)
    );
  };

  const updatePurchaseTerm = (updatedTerm: PurchaseTermType, index: number) => {
    setPurchaseTerms((prevTerms) => {
      const updatedTerms = [...prevTerms];
      updatedTerms[index] = updatedTerm;
      return updatedTerms;
    });
  };

  const checkSubmitConditions = () => {
    for (const term of purchaseTerms) {
      if (
        !(
          (term.rule.type === "ITEM" &&
            term.rule.itemIdOrCategoryOrNull !== "" &&
            term.quantity !== 0) ||
          (term.rule.type === "CATEGORY" &&
            term.rule.itemIdOrCategoryOrNull !== "" &&
            term.quantity !== 0) ||
          (term.rule.type === "BASKET" && term.quantity !== 0)
        )
      ) {
        return false;
      }
    }
    return true;
  };

  return (
    <Stack spacing={4}>
      <Button colorScheme="blackAlpha" onClick={addPurchaseTerm}>
        Add Purchase Term
      </Button>
      {purchaseTerms.map((term, index) => (
        <div key={term.id}>
          <PurchaseTerm
            storeId={storeId}
            purchaseTerm={term}
            onUpdatePurchaseTerm={(updatedTerm) =>
              updatePurchaseTerm(updatedTerm, index)
            }
          />
          <Button
            w="100%"
            colorScheme="red"
            onClick={() => term.id && deletePurchaseTerm(term.id)}
          >
            Delete
          </Button>
          <Flex justifyContent="center" paddingTop={2}>
            {index !== purchaseTerms.length - 1 && (
              <Text fontWeight="bold">{purchaseType.toUpperCase()}</Text>
            )}
          </Flex>
        </div>
      ))}
      {purchaseTerms.length > 0 && checkSubmitConditions() && (
        <Button
          padding={4}
          colorScheme="blue"
          onClick={() =>
            onSubmit({
              purchaseTerms: purchaseTerms.map((purchaseTerm)=> {
                return {rule: purchaseTerm.rule, atLeast:purchaseTerm.atLeast, quantity:purchaseTerm.quantity}
              }),
              type: purchaseType.toUpperCase(),
            })
          }
        >
          Submit
        </Button>
      )}
    </Stack>
  );
};

export default AndOrPurchaseRules;
