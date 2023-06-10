import { Text } from "@chakra-ui/react";
import axios from "axios";
import React, { useEffect, useState } from "react";

interface Props {
  storeId: string;
  userId: string;
  mr?: number;
  ml?: number;
}

const StoreNameFromId = ({ ml, mr, storeId, userId }: Props) => {
  const [storeName, setStoreName] = useState("");

  const getStoreName = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/store-info/id=${userId}&storeId=${storeId}`
    );
    if (!response.data.error) {
      setStoreName(response.data.value.name);
    } else {
      console.log(response.data.error);
    }
  };

  useEffect(() => {
    getStoreName();
  }, [storeId]);
  return (
    <Text ml={ml} mr={mr}>
      {storeName}
    </Text>
  );
};

export default StoreNameFromId;
