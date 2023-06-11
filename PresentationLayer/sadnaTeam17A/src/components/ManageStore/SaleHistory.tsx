import React, { useContext, useEffect, useState } from "react";
import { ClientCredentialsContext } from "../../App";
import axios from "axios";
import { Box, Button, Heading, SimpleGrid } from "@chakra-ui/react";
import { SoldItemType } from "../../types";
import SoldItem from "./SoldItem";

interface Props {
  storeId: string;
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
}

const SaleHistory = ({ storeId, setPage, pages }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const [items, setItems] = useState<SoldItemType[]>([]);

  const getSaleHistory = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/sale-history/id=${clientCredentials}&storeId=${storeId}`
    );
    if (!response.data.error) {
      setItems(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };
  useEffect(() => {
    getSaleHistory();
  }, []);
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
      <Heading padding={5} textAlign="center">
        Sale History
      </Heading>
      <SimpleGrid
        columns={{ sm: 1, md: 2, lg: 3, xl: 3, "2xl": 4 }}
        padding="10px"
        spacing={6}
      >
        {items.map((item) => (
          <SoldItem key={item.id} soldItem={item} />
        ))}
      </SimpleGrid>
    </>
  );
};

export default SaleHistory;
