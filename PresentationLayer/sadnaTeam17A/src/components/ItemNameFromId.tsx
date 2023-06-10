import { Text } from "@chakra-ui/react";
import axios from "axios";
import React, { useEffect, useState } from "react";

interface Props {
  storeId: string;
  itemId?: string;
  mr?: number;
  ml?: number;
}

const ItemNameFromId = ({ ml, mr, storeId, itemId }: Props) => {
  const [itemName, setItemName] = useState("");

  const getItemInfo = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/item-info/storeId=${storeId}&itemId=${itemId}`
    );
    if (!response.data.error) {
      setItemName(response.data.value.name);
    } else {
      return response.data.error;
    }
  };

  useEffect(() => {
    getItemInfo();
  }, [itemId]);
  return (
    <Text ml={ml} mr={mr}>
      {itemName}
    </Text>
  );
};

export default ItemNameFromId;
