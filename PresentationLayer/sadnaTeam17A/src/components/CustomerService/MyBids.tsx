import React, { useContext, useEffect, useState } from "react";
import { ClientCredentialsContext } from "../../App";
import axios from "axios";
import { Bid } from "../../types";
import { Heading, SimpleGrid, Stack } from "@chakra-ui/react";
import MyBidCard from "./MyBidCard";

interface Props {
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
}

const MyBids = ({ setPage, pages }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const [bids, setBids] = useState<Bid[]>([]);

  const getBids = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/get-user-bids/id=${clientCredentials}`
    );
    if (!response.data.error) {
      setBids(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  useEffect(() => {
    getBids();
  }, []);

  return (
    <>
      <Heading padding={2} textAlign="center">
        My Bids
      </Heading>
      <SimpleGrid
        columns={{ sm: 1, md: 2, lg: 3, xl: 3, "2xl": 4 }}
        padding="10px"
        spacing={6}
      >
        {bids.map((bid, index) => (
          <MyBidCard key={index} bid={bid} refreshBids={getBids} />
        ))}
      </SimpleGrid>
    </>
  );
};

export default MyBids;
