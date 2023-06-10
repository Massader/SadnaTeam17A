import React, { useContext, useEffect, useState } from "react";
import { ClientCredentialsContext } from "../../App";
import axios from "axios";
import { DiscountType } from "../../types";
import { Box, Button, SimpleGrid } from "@chakra-ui/react";
import DiscountTermCard from "./DiscountTermCard";

interface Props {
  storeId: string;
  setPurchaseAndDiscountPage: React.Dispatch<React.SetStateAction<string>>;
  purchaseAndDiscountPages: string[];
}

const ShowDiscounts = ({
  storeId,
  setPurchaseAndDiscountPage,
  purchaseAndDiscountPages,
}: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const getDiscounts = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/get-discounts/id=${clientCredentials}&storeId=${storeId}`
    );
    if (!response.data.error) {
      console.log(response.data.value);
      setDiscounts(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  useEffect(() => {
    getDiscounts();
  }, []);

  const [discounts, setDiscounts] = useState<DiscountType[]>([]);

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
        {discounts.map((discount, index) => (
          <Box overflow="hidden" key={index}>
            <DiscountTermCard
              key={index}
              storeId={storeId}
              discountTerm={discount}
              refreshCards={getDiscounts}
            />
          </Box>
        ))}
      </SimpleGrid>
    </>
  );
};

export default ShowDiscounts;
