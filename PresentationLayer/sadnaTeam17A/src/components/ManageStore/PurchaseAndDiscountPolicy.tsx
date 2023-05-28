import { Button, Flex, Heading, Stack } from "@chakra-ui/react";
import React, { useState } from "react";
import PurchaseTerm from "./PurchaseTerm";
import AndOrPurchaseRules from "./AndOrPurchaseRules";
import AddPurchaseRules from "./AddPurchaseRules";
import AddDiscount from "./AddDiscount";

interface Props {
  storeId: string;
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
}

const PurchaseAndDiscountPolicy = ({ storeId, setPage, pages }: Props) => {
  const purchaseAndDiscountPages = ["home", "addPurchaseRule", "addDiscount"];
  const [purchaseAndDiscountPage, setPurchaseAndDiscountPage] = useState(
    purchaseAndDiscountPages[0]
  );
  const [purchaseType, setPurchaseType] = useState("");
  const [discountType, setDiscountType] = useState("");

  const [addPurchaseRuleOn, setAddPurchaseRuleOn] = useState(false);
  const [addDiscountOn, setAddDiscountOn] = useState(false);

  return (
    <Flex padding={10} justifyContent="center" alignItems="center">
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
                  Add Simple Purchase Rule
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
                    setPurchaseType("conditioning");
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
                    setDiscountType("simple");
                    setPurchaseAndDiscountPage(purchaseAndDiscountPages[2]);
                  }}
                  padding={4}
                  colorScheme="gray"
                >
                  Add Simple Discount
                </Button>
                <Button
                  onClick={() => {
                    setDiscountType("conditioning");
                    setPurchaseAndDiscountPage(purchaseAndDiscountPages[2]);
                  }}
                  padding={4}
                  colorScheme="gray"
                >
                  Add Conditional Discount
                </Button>
              </>
            )}
            <Button colorScheme="blackAlpha">
              Show Store's Purchase Rules
            </Button>
            <Button colorScheme="blackAlpha">
              Remove a Store Purchase Rule
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
        {purchaseAndDiscountPage === "addDiscount" && (
          <AddDiscount
            storeId={storeId}
            setPurchaseAndDiscountPage={setPurchaseAndDiscountPage}
            purchaseAndDiscountPages={purchaseAndDiscountPages}
            discountType={discountType}
          ></AddDiscount>
        )}
      </Stack>
    </Flex>
  );
};

export default PurchaseAndDiscountPolicy;
