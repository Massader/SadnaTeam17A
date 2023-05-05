import {
  Button,
  Grid,
  GridItem,
  SimpleGrid,
  useBreakpointValue,
} from "@chakra-ui/react";
import ItemCardContainer from "./ItemCardContaincer";
import { useEffect, useState } from "react";
import StoreCard from "./StoreCard";
import axios from "axios";
import { Store } from "../types";

interface Props {
  onShop: (storeId: string) => void;
}

const StoresGrid = ({ onShop }: Props) => {
  const [page, setPage] = useState(1);
  const [pagesNum, setPagesNum] = useState(0);
  const [stores, setStores] = useState<Store[]>([]);

  const number =
    useBreakpointValue({
      base: 2,
      sm: 2,
      md: 4,
      lg: 6,
      xl: 6,
      "2xl": 8,
    }) || 2;

  const fetchStores = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/get-stores-page/number=${number}&page=${page}`
    );
    if (!response.data.error) {
      setStores(response.data.value);
    } else {
      setPage(page - 1);
    }
  };

  const getStoresNumber = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/num-of-stores`
    );
    if (!response.data.error) {
      setPagesNum(response.data.value / number);
    } else {
      console.log(response.data.error);
    }
  };

  useEffect(() => {
    fetchStores();
  }, [page, number]);

  useEffect(() => {
    getStoresNumber();
  }, [number]);

  return (
    <>
      <SimpleGrid
        columns={{ sm: 1, md: 2, lg: 3, xl: 3, "2xl": 4 }}
        padding="10px"
        spacing={6}
      >
        {stores.map((store) => (
          <ItemCardContainer key={store.storeId}>
            <StoreCard
              key={store.storeId}
              name={store.name}
              storeId={store.storeId}
              description={store.description}
              rating={store.rating}
              onShop={() => onShop(store.storeId)}
            />
          </ItemCardContainer>
        ))}
      </SimpleGrid>
      <Grid
        mt={6}
        margin={5}
        templateAreas={`"next previous"`}
        templateColumns="50% 50%"
      >
        <GridItem area="next">
          {page !== 1 && (
            <Button
              w="95%"
              colorScheme="blackAlpha"
              marginRight={3}
              onClick={() => {
                setPage(page - 1);
                fetchStores();
              }}
            >
              Previous page
            </Button>
          )}
        </GridItem>
        <GridItem area="previous">
          {page < pagesNum && (
            <Button
              w="95%"
              colorScheme="blackAlpha"
              onClick={() => {
                setPage(page + 1);
              }}
            >
              Next page
            </Button>
          )}
        </GridItem>
      </Grid>
    </>
  );
};

export default StoresGrid;
