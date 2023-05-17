import { Button, Flex, Heading, Stack } from "@chakra-ui/react";
import React, { useState } from "react";
import PurchaseRule from "./PurchaseRule";
import AndOrPurchaseRules from "./AndOrPurchaseRules";
import AddPurchaseRules from "./AddPurchaseRules";

interface Props {
  storeId: string;
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
}

const PurchaseAndDiscountPolicy = ({ storeId, setPage, pages }: Props) => {
  const purchaseAndDiscountPages = ["home", "addPurchaseRule"];
  const [purchaseAndDiscountPage, setPurchaseAndDiscountPage] = useState(
    purchaseAndDiscountPages[0]
  );
  const [purchaseType, setPurchaseType] = useState("");

  const [addPurchaseRuleOn, setAddPurchaseRuleOn] = useState(false);

  return (
    <Flex padding={10} justifyContent="center" alignItems="center">
      <Stack w="80%" justifyContent="center" alignItems="center">
        <Heading padding={5} textAlign="center">
          Purchase and discount policy
        </Heading>
        {purchaseAndDiscountPage === "home" && (
          <>
            <Button
              onClick={() => {
                setAddPurchaseRuleOn(!addPurchaseRuleOn);
              }}
              colorScheme="blackAlpha"
            >
              Add a purchase rule to the store
            </Button>
            {addPurchaseRuleOn && (
              <>
                <Button
                  onClick={() => {
                    setPurchaseType("simple");
                    setPurchaseAndDiscountPage(purchaseAndDiscountPages[1]);
                  }}
                  padding={4}
                  colorScheme="gray"
                >
                  Add simple purchase rule
                </Button>
                <Button
                  onClick={() => {
                    setPurchaseType("and");
                    setPurchaseAndDiscountPage(purchaseAndDiscountPages[1]);
                  }}
                  padding={4}
                  colorScheme="gray"
                >
                  Add "And" purchase rule
                </Button>
                <Button
                  onClick={() => {
                    setPurchaseType("or");
                    setPurchaseAndDiscountPage(purchaseAndDiscountPages[1]);
                  }}
                  padding={4}
                  colorScheme="gray"
                >
                  Add "Or" purchase rule
                </Button>
                <Button
                  onClick={() => {
                    setPurchaseType("conditioning");
                    setPurchaseAndDiscountPage(purchaseAndDiscountPages[1]);
                  }}
                  padding={4}
                  colorScheme="gray"
                >
                  Add "Conditioning" purchase rule
                </Button>
              </>
            )}
            <Button colorScheme="blackAlpha">
              Show store's purchase rules
            </Button>
            <Button colorScheme="blackAlpha">
              Remove a store purchase rule
            </Button>
          </>
        )}
        {purchaseAndDiscountPage === "addPurchaseRule" && (
          <AddPurchaseRules
            storeId={storeId}
            setPurchaseAndDiscountPage={setPurchaseAndDiscountPage}
            purchaseAndDiscountPages={purchaseAndDiscountPages}
            purchaseType={purchaseType}
          ></AddPurchaseRules>
        )}
      </Stack>
    </Flex>
  );
};

export default PurchaseAndDiscountPolicy;
