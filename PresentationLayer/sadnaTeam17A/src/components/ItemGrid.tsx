import {
  Button,
  Grid,
  GridItem,
  SimpleGrid,
  useBreakpointValue,
} from "@chakra-ui/react";
import ItemCardContainer from "./ItemCardContaincer";
import { useContext, useEffect, useState } from "react";
import axios from "axios";
import ItemCard from "./ItemCard";
import { Item } from "../types";
import { ClientCredentialsContext } from "../App";

interface Props {
  keyword: string;
  storeId: string;
  category: string;
  minPrice: number;
  maxPrice: number;
  itemRating: number;
  storeRating: number;
}

const ItemGrid = ({
  storeId,
  minPrice,
  maxPrice,
  keyword,
  itemRating,
  category,
  storeRating,
}: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const [page, setPage] = useState(1);
  const [pagesNum, setPagesNum] = useState(0);
  const [items, setItems] = useState<Item[]>([]);

  const number =
    useBreakpointValue({
      // base: 2,
      sm: 2,
      md: 4,
      lg: 6,
      xl: 6,
      "2xl": 8,
    }) || 6;

  const fetchItems = async () => {
    if (clientCredentials !== "") {
      const response = await axios.get(
        `http://localhost:8080/api/v1/stores/search-item/keyword=${keyword}&category=${category}&minPrice=${minPrice}&maxPrice=${maxPrice}&itemRating=${itemRating}&storeRating=${storeRating}&storeId=${storeId}&number=${number}&page=${page}`
      );
      if (!response.data.error) {
        console.log(response.data.value);
        setItems(response.data.value);
      } else {
        setPage(page - 1);
      }
    }
  };

  const getItemsNumber = async () => {
    if (clientCredentials !== "") {
      const response = await axios.get(
        `http://localhost:8080/api/v1/stores/search-item-num/keyword=${keyword}&category=${category}&minPrice=${minPrice}&maxPrice=${maxPrice}&itemRating=${itemRating}&storeRating=${storeRating}&storeId=${storeId}&number=&page=`
      );
      if (!response.data.error) {
        setPagesNum(response.data.value / number);
      } else {
        console.log(response.data.error);
      }
    }
  };

  useEffect(() => {
    setPage(1);
  }, [clientCredentials, keyword, minPrice, maxPrice, itemRating, category]);

  useEffect(() => {
    fetchItems();
  }, [
    clientCredentials,
    page,
    number,
    storeId,
    keyword,
    minPrice,
    maxPrice,
    itemRating,
    category,
  ]);

  useEffect(() => {
    getItemsNumber();
  }, [
    clientCredentials,
    page,
    number,
    storeId,
    keyword,
    minPrice,
    maxPrice,
    itemRating,
    category,
  ]);

  return (
    <>
      <SimpleGrid
        columns={{ sm: 1, md: 2, lg: 3, xl: 3, "2xl": 4 }}
        padding="10px"
        spacing={6}
      >
        {items.map((item) => (
          <ItemCardContainer key={item.id}>
            <ItemCard item={item} key={item.id} />
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
                fetchItems();
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

export default ItemGrid;
