import React, { useContext, useEffect, useState } from "react";
import { ClientCredentialsContext } from "../../App";
import axios from "axios";
import { PurchasedItemType } from "../../types";
import { Button, Flex, Heading, SimpleGrid, Stack } from "@chakra-ui/react";
import PurchasedItem from "./PurchasedItem";

interface Props {
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
}

const PurchaseHistory = ({ setPage, pages }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const [items, setItems] = useState<PurchasedItemType[]>([]);

  const getPurchaseHistory = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/purchase-history/id=${clientCredentials}`
    );
    if (!response.data.error) {
      setItems(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  useEffect(() => {
    getPurchaseHistory();
  }, []);

  const [itemsSet, setItemsSet] = useState<string[]>([]);

  return (
    <>
      <Heading padding={2} textAlign="center">
        Purchase history
      </Heading>
      <SimpleGrid
        columns={{ sm: 1, md: 2, lg: 3, xl: 3, "2xl": 4 }}
        padding="10px"
        spacing={6}
      >
        {items.map(
          (item) =>
            !itemsSet.includes(item.itemId) &&
            itemsSet.push(item.itemId) && (
              <PurchasedItem key={item.id} purchasedItem={item} />
            )
        )}
      </SimpleGrid>
    </>
  );
};

export default PurchaseHistory;
