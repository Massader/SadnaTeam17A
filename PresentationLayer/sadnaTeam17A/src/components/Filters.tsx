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
import { useState } from "react";
import { AiOutlineStar, AiFillStar } from "react-icons/ai";

const Filters = () => {
  const [ratingItem, setRatingItem] = useState(0);
  const [submittedItem, setSubmittedItem] = useState(0);
  const [ratingStore, setRatingStore] = useState(0);
  const [submittedStore, setSubmittedStore] = useState(0);

  const handleMouseEnterItem = (index: number) => {
    setRatingItem(index + 1);
  };

  const handleMouseLeaveItem = () => {
    submittedItem !== 0 ? setRatingItem(submittedItem) : setRatingItem(0);
  };

  const handleClickRatingItem = (index: number) => {
    setSubmittedItem(index + 1);
  };

  const handleMouseEnterStore = (index: number) => {
    setRatingStore(index + 1);
  };

  const handleMouseLeaveStore = () => {
    submittedStore !== 0 ? setRatingStore(submittedStore) : setRatingStore(0);
  };

  const handleClickRatingStore = (index: number) => {
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
            <Input placeholder="min" marginRight={2} />
          </InputGroup>
          <InputGroup>
            <InputLeftElement children="$" />
            <Input placeholder="max" marginRight={2} />
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
              onClick={() => handleClickRatingItem(index)}
            >
              {index < ratingItem ? (
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
              onClick={() => handleClickRatingStore(index)}
            >
              {index < ratingStore ? (
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
