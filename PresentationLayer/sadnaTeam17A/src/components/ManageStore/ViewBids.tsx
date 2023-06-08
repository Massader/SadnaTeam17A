import axios from "axios";
import React, { useEffect, useState, useContext } from "react";
import { Item } from "../../types";
import { Box, Button, Heading, SimpleGrid } from "@chakra-ui/react";
import EditItem from "./EditItem";
import { ClientCredentialsContext } from "../../App";

interface Props {
  storeId: string;
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
}

const ViewBids = ({ storeId, setPage, pages }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const fetchItems = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/search-item/keyword=&category=&minPrice=&maxPrice=&itemRating=&storeRating=&storeId=${storeId}&number=&page=`
    );
    if (!response.data.error) {
      setItems(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  const fetchBids = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/search-item/keyword=&category=&minPrice=&maxPrice=&itemRating=&storeRating=&storeId=${storeId}&number=&page=`
    );
    if (!response.data.error) {
      setItems(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  //   const getItemBids = async () => {
  //     const response = await axios.get(
  //       `http://localhost:8080/api/v1/stores/get-item-bids/id=${clientCredentials}&storeId=${storeId}&itemId=${itemId}`
  //     );
  //     if (!response.data.error) {
  //       setItems(response.data.value);
  //     } else {
  //       console.log(response.data.error);
  //     }
  //   };

  const [items, setItems] = useState<Item[]>([]);

  useEffect(() => {
    fetchItems();
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
        Inventory Management
      </Heading>
      <SimpleGrid
        columns={{ sm: 1, md: 2, lg: 3, xl: 3, "2xl": 4 }}
        padding="10px"
        spacing={6}
      >
        {items.map((item) => (
          <EditItem key={item.id} editItem={item} refreshItems={fetchItems} />
        ))}
      </SimpleGrid>
    </>
  );
};

export default ViewBids;
