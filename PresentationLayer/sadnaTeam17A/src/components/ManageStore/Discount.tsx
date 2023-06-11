import { Button, Input, Select, Stack, Text } from "@chakra-ui/react";
import React, { useContext, useEffect, useState } from "react";
import { ClientCredentialsContext } from "../../App";
import axios from "axios";
import { Item } from "../../types";

interface Props {
  storeId: string;
}

const Discount = ({ storeId }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const [selectedOption, setSelectedOption] = useState("");
  const [items, setItems] = useState<Item[]>([]);
  const [selectedItem, setSelectedItem] = useState("");
  const [percent, setPercent] = useState("");

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

  useEffect(() => {
    fetchItems();
  }, []);

  return (
    <Stack>
      <Select
        bg="white"
        colorScheme="white"
        placeholder="Select option"
        value={selectedOption}
        onChange={(event) => {
          setPercent("");
          setSelectedOption(event.target.value);
        }}
      >
        <option value="Shopping busket">Shopping busket</option>
        <option value="Item">Item</option>
        <option value="Category">Category</option>
      </Select>
      {selectedOption === "Shopping busket" && (
        <Input
          bg="white"
          placeholder="how many percent?"
          type="number"
          value={percent}
          onChange={(percent) => setPercent(percent.target.value)}
        />
      )}
      {selectedOption === "Item" && (
        <>
          <Select
            bg="white"
            colorScheme="white"
            placeholder="Select an item"
            value={selectedItem}
            onChange={(event) => {
              setSelectedItem(event.target.value);
            }}
          >
            {items.map((item) => (
              <option key={item.id} value={item.id}>
                {item.name}
              </option>
            ))}
          </Select>
          <Input
            bg="white"
            placeholder="how many percent?"
            type="number"
            value={percent}
            onChange={(percent) => setPercent(percent.target.value)}
          />
        </>
      )}
      {selectedOption === "Item" && percent !== "" && selectedItem !== "" && (
        <Button colorScheme="blue">Submit</Button>
      )}
      {selectedOption === "Shopping busket" && percent !== "" && (
        <Button colorScheme="blue">Submit</Button>
      )}
    </Stack>
  );
};

export default Discount;
