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

interface Props {
  keyword: string;
  storeId: string;
  category?: string;
  minPrice: number;
  maxPrice: number;
  itemRating?: number;
  storeRating?: number;
}

const ItemGrid = ({ storeId, minPrice, maxPrice, keyword }: Props) => {
  const [page, setPage] = useState(1);
  const [pagesNum, setPagesNum] = useState(0);
  const [items, setItems] = useState<Item[]>([]);

  const number =
    useBreakpointValue({
      base: 2,
      sm: 2,
      md: 4,
      lg: 6,
      xl: 6,
      "2xl": 8,
    }) || 2;

  const fetchItems = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/search-item/keyword=${keyword}&category=&minPrice=${minPrice}&maxPrice=${maxPrice}&itemRating=&storeRating=&storeId=${storeId}&number=${number}&page=${page}`
    );
    if (!response.data.error) {
      setItems(response.data.value);
    } else {
      setPage(page - 1);
    }
  };

  const getItemsNumber = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/search-item-num/keyword=${keyword}&category=&minPrice=${minPrice}&maxPrice=${maxPrice}&itemRating=&storeRating=&storeId=${storeId}&number=&page=`
    );
    if (!response.data.error) {
      setPagesNum(response.data.value / number);
    } else {
      console.log(response.data.error);
    }
  };

  useEffect(() => {
    setPage(1);
  }, [keyword, minPrice, maxPrice]);

  useEffect(() => {
    fetchItems();
  }, [page, number, storeId, keyword, minPrice, maxPrice]);

  useEffect(() => {
    getItemsNumber();
  }, [page, number, storeId, keyword, minPrice, maxPrice]);

  return (
    <>
      <SimpleGrid
        columns={{ sm: 1, md: 2, lg: 3, xl: 3, "2xl": 4 }}
        padding="10px"
        spacing={6}
      >
        {items.map((item) => (
          <ItemCardContainer key={item.id}>
            <ItemCard
              key={item.id}
              id={item.id}
              name={item.name}
              price={item.price}
              storeId={item.storeId}
              rating={item.rating}
              quantity={item.quantity}
              description={item.description}
              categories={item.categories}
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
