import { Button, Input, Select, Stack, Text } from "@chakra-ui/react";
import React, { useContext, useEffect, useState } from "react";
import { ClientCredentialsContext } from "../../App";
import axios from "axios";
import { Item } from "../../types";

interface Props {
  storeId: string;
  purchaseType: string;
}

const PurchaseRule = ({ purchaseType, storeId }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const [selectedOption, setSelectedOption] = useState("");
  const [selectedOptionAtMostLeast, setSelectedOptionAtMostLeast] =
    useState("");
  const [items, setItems] = useState<Item[]>([]);
  const [selectedItem, setSelectedItem] = useState("");
  const [howMany, setHowMany] = useState("");

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
          setHowMany("");
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
          placeholder="Busket value?"
          type="number"
          value={howMany}
          onChange={(howMany) => setHowMany(howMany.target.value)}
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
            placeholder="How many?"
            type="number"
            value={howMany}
            onChange={(howMany) => setHowMany(howMany.target.value)}
          />
        </>
      )}
      {(selectedOption === "Shopping busket" || selectedOption === "Item") && (
        <>
          <Select
            bg="white"
            colorScheme="white"
            placeholder="Select option"
            value={selectedOptionAtMostLeast}
            onChange={(event) => {
              setSelectedOptionAtMostLeast(event.target.value);
            }}
          >
            <option value="AtMost">At most</option>
            <option value="AtLeast">At least</option>
          </Select>
        </>
      )}
      {purchaseType === "simple" &&
        selectedOption === "Item" &&
        howMany !== "" &&
        selectedItem !== "" &&
        selectedOptionAtMostLeast !== "" && (
          <Button colorScheme="blue">Submit</Button>
        )}
      {purchaseType === "simple" &&
        selectedOption === "Shopping busket" &&
        howMany !== "" &&
        selectedOptionAtMostLeast !== "" && (
          <Button colorScheme="blue">Submit</Button>
        )}
    </Stack>
  );
};

export default PurchaseRule;
