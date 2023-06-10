import axios from "axios";
import React, { useEffect, useState, useContext } from "react";
import { Bid } from "../../types";
import { Box, Button, Heading, SimpleGrid } from "@chakra-ui/react";
import { ClientCredentialsContext } from "../../App";
import BidCard from "./BidCard";

interface Props {
  storeId: string;
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
}

const ViewBids = ({ storeId, setPage, pages }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const fetchBids = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/get-store-bids/id=${clientCredentials}&storeId=${storeId}`
    );
    if (!response.data.error) {
      console.log(response.data.value);
      setBids(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  const [bids, setBids] = useState<Bid[]>([]);

  useEffect(() => {
    fetchBids();
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
        Bids
      </Heading>
      <SimpleGrid
        columns={{ sm: 1, md: 2, lg: 3, xl: 3, "2xl": 4 }}
        padding="10px"
        spacing={6}
      >
        {bids.map((bid, index) => (
          <BidCard
            key={index}
            storeId={storeId}
            bid={bid}
            refreshBids={fetchBids}
          />
        ))}
      </SimpleGrid>
    </>
  );
};

export default ViewBids;
