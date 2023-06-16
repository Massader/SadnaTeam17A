import {
  Button,
  Input,
  InputGroup,
  InputLeftElement,
  Select,
  Stack,
  Text,
} from "@chakra-ui/react";
import React, { useContext, useEffect, useState } from "react";
import { ClientCredentialsContext } from "../../App";
import axios from "axios";
import { DiscountType, Item } from "../../types";

interface Props {
  storeId: string;
  discountTerm: DiscountType;
  onUpdateDiscountTerm: (updatedDiscountTerm: DiscountType) => void;
}

const Discount = ({ discountTerm, onUpdateDiscountTerm, storeId }: Props) => {
  const [type, setType] = useState(discountTerm.type);
  const [itemIdOrCategory, setItemIdOrCategory] = useState(
    discountTerm.itemIdOrCategoryOrNull
  );
  const [discountPercentage, setDiscountPercentage] = useState(
    discountTerm.discountPercentage
  );

  const [items, setItems] = useState<Item[]>([]);

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

  const handleUpdateDiscountTerm = (eventType: string, event: string) => {
    const updatedDiscountTerm: DiscountType = {
      id: discountTerm.id,
      type: eventType === "selectType" ? event : type,
      itemIdOrCategoryOrNull:
        eventType === "selectType"
          ? ""
          : eventType === "itemIdOrCategory"
          ? event
          : itemIdOrCategory,
      discountPercentage:
        eventType === "selectType"
          ? 0
          : eventType === "discountPercentage"
          ? parseInt(event)

          : discountPercentage,
    };

    onUpdateDiscountTerm(updatedDiscountTerm);
  };

  return (
    <Stack>
      <Select
        bg="white"
        colorScheme="white"
        placeholder="Select option"
        value={type}
        onChange={(event) => {
          setDiscountPercentage(0);
          setType(event.target.value);
          setItemIdOrCategory("");
          handleUpdateDiscountTerm("selectType", event.target.value);
        }}
      >
        <option value="BASKET">Shopping basket</option>
        <option value="ITEM">Item</option>
        <option value="CATEGORY">Category</option>
      </Select>
      {type === "CATEGORY" && (
        <>
          <Input
            bg="white"
            placeholder="Category name"
            onChange={(event) => {
              setItemIdOrCategory(event.target.value);
              handleUpdateDiscountTerm("itemIdOrCategory", event.target.value);
            }}
          />
        </>
      )}
      {type === "ITEM" && (
        <>
          <Select
            bg="white"
            colorScheme="white"
            placeholder="Select an item"
            value={itemIdOrCategory}
            onChange={(event) => {
              setItemIdOrCategory(event.target.value);
              handleUpdateDiscountTerm("itemIdOrCategory", event.target.value);
            }}
          >
            {items.map((item) => (
              <option key={item.id} value={item.id}>
                {item.name}
              </option>
            ))}
          </Select>
        </>
      )}
      {(type === "BASKET" || type === "ITEM" || type === "CATEGORY") && (
        <InputGroup>
          <InputLeftElement
            pointerEvents="none"
            color="gray"
            fontSize="1.2em"
            children="%"
          />
          <Input
            bg="white"
            placeholder="Discount Percentage?"
            type="number"
            onChange={(event) => {
              setDiscountPercentage(parseInt(event.target.value));
              handleUpdateDiscountTerm(
                "discountPercentage",
                event.target.value
              );
            }}
          />
        </InputGroup>
      )}
    </Stack>
  );
};

export default Discount;
