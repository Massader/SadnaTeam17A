import React, { useContext, useEffect, useState } from "react";
import { ClientCredentialsContext } from "../../App";
import axios from "axios";
import { AllPurchaseTermType } from "../../types";
import { Box, Button, SimpleGrid } from "@chakra-ui/react";
import PurchaseTermCard from "./PurchaseTermCard";

interface Props {
  storeId: string;
  setPurchaseAndDiscountPage: React.Dispatch<React.SetStateAction<string>>;
  purchaseAndDiscountPages: string[];
}

const ShowPurchaseRules = ({
  storeId,
  setPurchaseAndDiscountPage,
  purchaseAndDiscountPages,
}: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const getPurchaseTerms = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/get-store-purchase-terms/id=${clientCredentials}&storeId=${storeId}`
    );
    if (!response.data.error) {
      console.log(response.data.value);
      setTerms(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  useEffect(() => {
    getPurchaseTerms();
  }, []);

  const [terms, setTerms] = useState<AllPurchaseTermType[]>([]);

  return (
    <>
      <Button
        onClick={() => {
          setPurchaseAndDiscountPage(purchaseAndDiscountPages[0]);
        }}
        colorScheme="blackAlpha"
      >
        Back
      </Button>
      <SimpleGrid
        columns={{ sm: 1, md: 2, lg: 3, xl: 3, "2xl": 4 }}
        padding="10px"
        spacing={6}
      >
        {terms.map((term, index) => (
          <Box overflow="hidden" key={index}>
            <PurchaseTermCard
              key={index}
              storeId={storeId}
              purchaseTerm={term}
              refreshCards={getPurchaseTerms}
            />
          </Box>
        ))}
      </SimpleGrid>
    </>
  );
};

export default ShowPurchaseRules;
