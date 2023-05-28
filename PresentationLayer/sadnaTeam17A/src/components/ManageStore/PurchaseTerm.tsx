import {
  Button,
  Input,
  NumberDecrementStepper,
  NumberIncrementStepper,
  NumberInput,
  NumberInputField,
  NumberInputStepper,
  Radio,
  RadioGroup,
  Select,
  Stack,
  Text,
  useToast,
} from "@chakra-ui/react";
import React, { useContext, useEffect, useState } from "react";
import { ClientCredentialsContext } from "../../App";
import axios from "axios";
import { Item, PurchaseRuleType, PurchaseTermType } from "../../types";

interface Props {
  storeId: string;
  purchaseTerm: PurchaseTermType;
  onUpdatePurchaseTerm: (updatedPurchaseTerm: PurchaseTermType) => void;
}

const PurchaseTerm = ({
  purchaseTerm,
  onUpdatePurchaseTerm,
  storeId,
}: Props) => {
  const [ruleType, setRuleType] = useState(purchaseTerm.rule.type);
  const [atLeast, setAtLeast] = useState(purchaseTerm.atLeast);
  const [itemIdOrCategory, setItemIdOrCategory] = useState(
    purchaseTerm.rule.itemIdOrCategoryOrNull
  );
  const [quantity, setQuantity] = useState(purchaseTerm.quantity);

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

  const handleUpdatePurchaseTerm = (eventType: string, event: string) => {
    const updatedPurchaseTerm: PurchaseTermType = {
      rule: {
        type: eventType === "selectType" ? event : ruleType,
        itemIdOrCategoryOrNull:
          eventType === "itemIdOrCategory" ? event : itemIdOrCategory,
      },
      atLeast: eventType === "atLeast" ? event === "true" : atLeast,
      quantity:
        eventType === "selectType"
          ? 0
          : eventType === "quantity"
          ? parseInt(event)
          : quantity,
    };

    onUpdatePurchaseTerm(updatedPurchaseTerm);
  };

  return (
    <Stack>
      <Select
        bg="white"
        colorScheme="white"
        placeholder="Select option"
        value={ruleType}
        onChange={(event) => {
          setQuantity(0);
          setRuleType(event.target.value);
          handleUpdatePurchaseTerm("selectType", event.target.value);
        }}
      >
        <option value="BUSKET">Shopping busket</option>
        <option value="ITEM">Item</option>
        <option value="CATEGORY">Category</option>
      </Select>
      {ruleType === "BUSKET" && (
        <Input
          bg="white"
          placeholder="Busket value?"
          type="number"
          onChange={(event) => {
            setQuantity(parseInt(event.target.value));
            handleUpdatePurchaseTerm("quantity", event.target.value);
          }}
        />
      )}
      {ruleType === "CATEGORY" && (
        <>
          <Input
            bg="white"
            placeholder="Category name"
            onChange={(event) => {
              setItemIdOrCategory(event.target.value);
              handleUpdatePurchaseTerm("itemIdOrCategory", event.target.value);
            }}
          />
        </>
      )}
      {ruleType === "ITEM" && (
        <Select
          bg="white"
          colorScheme="white"
          placeholder="Select an item"
          value={itemIdOrCategory}
          onChange={(event) => {
            setItemIdOrCategory(event.target.value);
            handleUpdatePurchaseTerm("itemIdOrCategory", event.target.value);
          }}
        >
          {items.map((item) => (
            <option key={item.id} value={item.id}>
              {item.name}
            </option>
          ))}
        </Select>
      )}
      {(ruleType === "ITEM" || ruleType === "CATEGORY") && (
        <Input
          bg="white"
          placeholder="How many?"
          type="number"
          onChange={(event) => {
            setQuantity(parseInt(event.target.value));
            handleUpdatePurchaseTerm("quantity", event.target.value);
          }}
        />
      )}
      {(ruleType === "BUSKET" ||
        ruleType === "ITEM" ||
        ruleType === "CATEGORY") && (
        <>
          <Select
            bg="white"
            colorScheme="white"
            placeholder="Select option"
            value={atLeast.toString()}
            onChange={(event) => {
              setAtLeast(event.target.value === "true");
              handleUpdatePurchaseTerm("atLeast", event.target.value);
            }}
          >
            <option value="true">At least</option>
            <option value="false">At most</option>
          </Select>
        </>
      )}
    </Stack>
  );
};

export default PurchaseTerm;
