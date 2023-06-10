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
import React, { useEffect } from "react";
import { useState } from "react";
import { AiOutlineStar, AiFillStar } from "react-icons/ai";

interface Props {
  setMinPrice: React.Dispatch<React.SetStateAction<number>>;
  setMaxPrice: React.Dispatch<React.SetStateAction<number>>;
  setItemRating: React.Dispatch<React.SetStateAction<number>>;
  setStoreRating: React.Dispatch<React.SetStateAction<number>>;
  setCategory: React.Dispatch<React.SetStateAction<string>>;
}

const Filters = ({
  setMinPrice,
  setMaxPrice,
  setItemRating,
  setStoreRating,
  setCategory,
}: Props) => {
  const [submittedItem, setSubmittedItem] = useState(0);
  const [itemRatingView, setItemRatingView] = useState(0);
  const [submittedStore, setSubmittedStore] = useState(0);
  const [storeRatingView, setStoreRatingView] = useState(0);

  useEffect(() => {
    setItemRating(0);
    setStoreRating(0);
  }, []);

  const handleMouseEnterItem = (index: number) => {
    setItemRatingView(index + 1);
  };

  const handleMouseLeaveItem = () => {
    submittedItem !== 0
      ? setItemRatingView(submittedItem)
      : setItemRatingView(0);
  };

  const handleClickitemRating = (index: number) => {
    if (submittedItem === index + 1) {
      setSubmittedItem(0);
      setItemRating(0);
    } else {
      setSubmittedItem(index + 1);
      setItemRating(index + 1);
    }
  };

  const handleMouseEnterStore = (index: number) => {
    setStoreRatingView(index + 1);
  };

  const handleMouseLeaveStore = () => {
    submittedStore !== 0
      ? setStoreRatingView(submittedStore)
      : setStoreRatingView(0);
  };

  const handleClickStoreRating = (index: number) => {
    if (submittedStore === index + 1) {
      setSubmittedStore(0);
      setStoreRating(0);
    } else {
      setSubmittedStore(index + 1);
      setStoreRating(index + 1);
    }
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
              {index < itemRatingView ? (
                <AiFillStar color="ffb703" size={30} />
              ) : (
                <AiOutlineStar size={30} />
              )}
            </span>
          ))}
        </Flex>
        <Text>Category:</Text>
        <Input
          bg="white"
          placeholder="Category"
          onChange={(category) => {
            setCategory(category.target.value);
          }}
        />
        <Text>Store rating:</Text>
        <Flex>
          {[...Array(5)].map((_, index) => (
            <span
              key={index}
              onMouseEnter={() => handleMouseEnterStore(index)}
              onMouseLeave={() => handleMouseLeaveStore()}
              onClick={() => handleClickStoreRating(index)}
            >
              {index < storeRatingView ? (
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
