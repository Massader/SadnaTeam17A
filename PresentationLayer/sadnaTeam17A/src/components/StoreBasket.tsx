import { SimpleGrid, Text } from "@chakra-ui/react";
import axios from "axios";
import { useContext, useEffect, useState } from "react";
import { Basket } from "../types";
import ItemInBusket from "./ItemInBusket";
import { ClientCredentialsContext } from "../App";

interface Props {
  basket: Basket;
}

const StoreBasket = ({ basket }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);
  const [storeName, setStoreName] = useState("");

  const getStoreName = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/store-info/id=${clientCredentials}&storeId=${basket.storeId}`
    );
    if (!response.data.error) {
      setStoreName(response.data.value.name);
    } else {
      console.log(response.data.error);
    }
  };

  useEffect(() => {
    getStoreName();
  }, []);

  return (
    <>
      <Text fontWeight="bold" margin={3}>
        {storeName}:
      </Text>
      <SimpleGrid
        columns={{ sm: 1, md: 2, lg: 3, xl: 3, "2xl": 4 }}
        padding="10px"
        spacing={6}
      >
        {Object.entries(basket.items).map(([itemId, quantity]) => (
          <div key={itemId}>
            <ItemInBusket
              itemId={itemId}
              quantity={quantity}
              storeId={basket.storeId}
            />
          </div>
        ))}
      </SimpleGrid>
    </>
  );
};

export default StoreBasket;
