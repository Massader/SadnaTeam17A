import {
  Button,
  Flex,
  Input,
  InputGroup,
  InputLeftElement,
  Select,
  Stack,
  Text,
} from "@chakra-ui/react";
import React from "react";
import { useState } from "react";
import { AiOutlineStar, AiFillStar } from "react-icons/ai";

interface Props {
  setMinPrice: React.Dispatch<React.SetStateAction<number>>;
  setMaxPrice: React.Dispatch<React.SetStateAction<number>>;
}

const Filters = ({ setMinPrice, setMaxPrice }: Props) => {
  const [itemRating, setItemRating] = useState(0);
  const [submittedItem, setSubmittedItem] = useState(0);
  const [storeRating, setStoreRating] = useState(0);
  const [submittedStore, setSubmittedStore] = useState(0);

  const handleMouseEnterItem = (index: number) => {
    setItemRating(index + 1);
  };

  const handleMouseLeaveItem = () => {
    submittedItem !== 0 ? setItemRating(submittedItem) : setItemRating(0);
  };

  const handleClickitemRating = (index: number) => {
    setSubmittedItem(index + 1);
  };

  const handleMouseEnterStore = (index: number) => {
    setStoreRating(index + 1);
  };

  const handleMouseLeaveStore = () => {
    submittedStore !== 0 ? setStoreRating(submittedStore) : setStoreRating(0);
  };

  const handleClickstoreRating = (index: number) => {
    setSubmittedStore(index + 1);
  };

  return (
    <>
      <Stack marginTop={3} padding={4}>
        <Stack alignItems="center">
          <Text as="b" fontSize="2xl">
            Filters
          </Text>
        </Stack>
        <Text>price range:</Text>
        <Flex>
          <InputGroup>
            <InputLeftElement children="$" />
            <Input
              onChange={(event) => {
                const newValue = parseFloat(event.target.value);
                if (isNaN(newValue)) {
                  setMinPrice(0);
                } else {
                  setMinPrice(newValue);
                }
              }}
              placeholder="min"
              marginRight={2}
            />
          </InputGroup>
          <InputGroup>
            <InputLeftElement children="$" />
            <Input
              onChange={(event) => {
                const newValue = parseFloat(event.target.value);
                if (isNaN(newValue)) {
                  setMaxPrice(1000000);
                } else {
                  setMaxPrice(newValue);
                }
              }}
              placeholder="max"
              marginRight={2}
            />
          </InputGroup>
          {/* <Button>V</Button> */}
        </Flex>
        <Text>Item rating:</Text>
        <Flex>
          {[...Array(5)].map((_, index) => (
            <span
              key={index}
              onMouseEnter={() => handleMouseEnterItem(index)}
              onMouseLeave={() => handleMouseLeaveItem()}
              onClick={() => handleClickitemRating(index)}
            >
              {index < itemRating ? (
                <AiFillStar color="ffb703" size={30} />
              ) : (
                <AiOutlineStar size={30} />
              )}
            </span>
          ))}
        </Flex>
        <Text>Category:</Text>
        <Select placeholder="Select Category">
          <option value="option1">Option 1</option>
          <option value="option2">Option 2</option>
          <option value="option3">Option 3</option>
        </Select>
        <Text>Store rating:</Text>
        <Flex>
          {[...Array(5)].map((_, index) => (
            <span
              key={index}
              onMouseEnter={() => handleMouseEnterStore(index)}
              onMouseLeave={() => handleMouseLeaveStore()}
              onClick={() => handleClickstoreRating(index)}
            >
              {index < storeRating ? (
                <AiFillStar color="ffb703" size={30} />
              ) : (
                <AiOutlineStar size={30} />
              )}
            </span>
          ))}
        </Flex>
      </Stack>
    </>
  );
};

export default Filters;
