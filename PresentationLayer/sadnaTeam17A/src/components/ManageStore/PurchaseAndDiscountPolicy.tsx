import { Button, Flex, Heading, Stack } from "@chakra-ui/react";
import React, { useState } from "react";
import AddPurchaseRules from "./AddPurchaseRules";
import ShowPurchaseRules from "./ShowPurchaseRules";

interface Props {
  storeId: string;
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
}

const PurchaseAndDiscountPolicy = ({ storeId, setPage, pages }: Props) => {
  const purchaseAndDiscountPages = [
    "home",
    "addPurchaseRule",
    "addDiscount",
    "showTerms",
  ];
  const [purchaseAndDiscountPage, setPurchaseAndDiscountPage] = useState(
    purchaseAndDiscountPages[0]
  );
  const [purchaseType, setPurchaseType] = useState("");

  const [addPurchaseRuleOn, setAddPurchaseRuleOn] = useState(false);
  const [addDiscountOn, setAddDiscountOn] = useState(false);

  return (
    <Flex padding={5} justifyContent="center" alignItems="center">
      <Stack w="80%" justifyContent="center" alignItems="center">
        <Heading padding={5} textAlign="center">
          Purchase and Discount Policy
        </Heading>
        {purchaseAndDiscountPage === "home" && (
          <>
            <Button
              onClick={() => {
                setAddPurchaseRuleOn(!addPurchaseRuleOn);
              }}
              colorScheme="blackAlpha"
            >
              Add a Purchase Rule to the Store
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
                  Add "Simple" Purchase Rule
                </Button>
                <Button
                  onClick={() => {
                    setPurchaseType("and");
                    setPurchaseAndDiscountPage(purchaseAndDiscountPages[1]);
                  }}
                  padding={4}
                  colorScheme="gray"
                >
                  Add "And" Purchase Rule
                </Button>
                <Button
                  onClick={() => {
                    setPurchaseType("or");
                    setPurchaseAndDiscountPage(purchaseAndDiscountPages[1]);
                  }}
                  padding={4}
                  colorScheme="gray"
                >
                  Add "Or" Purchase Rule
                </Button>
                <Button
                  onClick={() => {
                    setPurchaseType("conditional");
                    setPurchaseAndDiscountPage(purchaseAndDiscountPages[1]);
                  }}
                  padding={4}
                  colorScheme="gray"
                >
                  Add "Conditional" Purchase Rule
                </Button>
              </>
            )}
            <Button
              onClick={() => {
                setAddDiscountOn(!addDiscountOn);
              }}
              colorScheme="blackAlpha"
            >
              Add Discount to the Store
            </Button>
            {addDiscountOn && (
              <>
                <Button
                  onClick={() => {
                    setPurchaseType("simpleDiscount");
                    setPurchaseAndDiscountPage(purchaseAndDiscountPages[1]);
                  }}
                  padding={4}
                  colorScheme="gray"
                >
                  Add "Simple" Discount
                </Button>
                <Button
                  onClick={() => {
                    setPurchaseType("conditionalDiscount");
                    setPurchaseAndDiscountPage(purchaseAndDiscountPages[1]);
                  }}
                  padding={4}
                  colorScheme="gray"
                >
                  Add "Conditional" Discount
                </Button>
              </>
            )}
            <Button
              colorScheme="blackAlpha"
              onClick={() =>
                setPurchaseAndDiscountPage(purchaseAndDiscountPages[3])
              }
            >
              Show Store's Purchase Rules
            </Button>
            <Button
              colorScheme="blackAlpha"
              onClick={() =>
                setPurchaseAndDiscountPage(purchaseAndDiscountPages[3])
              }
            >
              Show Store's Discounts
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
        {purchaseAndDiscountPage === "showTerms" && (
          <ShowPurchaseRules
            storeId={storeId}
            setPurchaseAndDiscountPage={setPurchaseAndDiscountPage}
            purchaseAndDiscountPages={purchaseAndDiscountPages}
          ></ShowPurchaseRules>
        )}
      </Stack>
    </Flex>
  );
};

export default PurchaseAndDiscountPolicy;
