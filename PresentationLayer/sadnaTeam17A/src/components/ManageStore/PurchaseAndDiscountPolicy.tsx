import { Box, Button, Flex, Heading, Stack } from "@chakra-ui/react";
import React, { useState } from "react";
import AddPurchaseRules from "./AddPurchaseRules";
import ShowPurchaseRules from "./ShowPurchaseRules";
import ShowDiscounts from "./ShowDiscounts";

interface Props {
  storeId: string;
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
  permissions: string[];
}

const PurchaseAndDiscountPolicy = ({
  storeId,
  setPage,
  pages,
  permissions,
}: Props) => {

  const purchaseAndDiscountPages = [
    "home",
    "addPurchaseRule",
    "addDiscount",
    "showTerms",
    "showDiscounts",

  ];
  const [purchaseAndDiscountPage, setPurchaseAndDiscountPage] = useState(
    purchaseAndDiscountPages[0]
  );
  const [purchaseType, setPurchaseType] = useState("");

  const [addPurchaseRuleOn, setAddPurchaseRuleOn] = useState(false);
  const [addDiscountOn, setAddDiscountOn] = useState(false);

  return (

    <>
      <Box padding="30px">
        <Button
          w="100%"
          colorScheme="blackAlpha"
          size="lg"
          onClick={() => setPage(pages[2])}
        >
          Back
        </Button>
      </Box>
      <Flex padding={5} justifyContent="center" alignItems="center">
        <Stack w="80%" justifyContent="center" alignItems="center">
          <Heading marginBottom={5} textAlign="center">
            Purchase and Discount Policy
          </Heading>
          {purchaseAndDiscountPage === "home" && (
            <>
              {(permissions.includes("STORE_FOUNDER") ||
                permissions.includes("STORE_OWNER") ||
                permissions.includes("STORE_POLICY_MANAGEMENT")) && (

                <Button
                  onClick={() => {
                    setAddPurchaseRuleOn(!addPurchaseRuleOn);
                  }}
                  colorScheme="blackAlpha"
                >
                  Add a Purchase Term to the Store

                </Button>
              )}
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
                    Add "Simple" Purchase Term
                  </Button>
                  <Button
                    onClick={() => {
                      setPurchaseType("and");
                      setPurchaseAndDiscountPage(purchaseAndDiscountPages[1]);
                    }}
                    padding={4}
                    colorScheme="gray"
                  >
                    Add "And" Purchase Term
                  </Button>
                  <Button
                    onClick={() => {
                      setPurchaseType("or");
                      setPurchaseAndDiscountPage(purchaseAndDiscountPages[1]);
                    }}
                    padding={4}
                    colorScheme="gray"
                  >
                    Add "Or" Purchase Term
                  </Button>
                  <Button
                    onClick={() => {
                      setPurchaseType("conditional");
                      setPurchaseAndDiscountPage(purchaseAndDiscountPages[1]);
                    }}
                    padding={4}
                    colorScheme="gray"
                  >
                    Add "Conditional" Purchase Term
                  </Button>
                </>
              )}
              {(permissions.includes("STORE_FOUNDER") ||
                permissions.includes("STORE_OWNER") ||
                permissions.includes("STORE_DISCOUNT_MANAGEMENT")) && (
                <Button
                  onClick={() => {
                    setAddDiscountOn(!addDiscountOn);
                  }}
                  colorScheme="blackAlpha"
                >
                  Add Discount to the Store
                </Button>
              )}
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
              {(permissions.includes("STORE_FOUNDER") ||
                permissions.includes("STORE_OWNER") ||
                permissions.includes("STORE_POLICY_MANAGEMENT")) && (
                <Button
                  colorScheme="blackAlpha"
                  onClick={() =>
                    setPurchaseAndDiscountPage(purchaseAndDiscountPages[3])
                  }
                >
                  Show Store's Purchase Terms
                </Button>
              )}
              {(permissions.includes("STORE_FOUNDER") ||
                permissions.includes("STORE_OWNER") ||
                permissions.includes("STORE_DISCOUNT_MANAGEMENT")) && (
                <Button
                  colorScheme="blackAlpha"
                  onClick={() =>
                    setPurchaseAndDiscountPage(purchaseAndDiscountPages[4])
                  }
                >
                  Show Store's Discounts
                </Button>

              )}
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
          {purchaseAndDiscountPage === "showDiscounts" && (
            <ShowDiscounts
              storeId={storeId}
              setPurchaseAndDiscountPage={setPurchaseAndDiscountPage}
              purchaseAndDiscountPages={purchaseAndDiscountPages}
            ></ShowDiscounts>
          )}
        </Stack>
      </Flex>
    </>
  );
};

export default PurchaseAndDiscountPolicy;
