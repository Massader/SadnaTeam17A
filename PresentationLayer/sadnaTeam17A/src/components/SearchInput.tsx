import { Input, InputGroup, InputLeftElement } from "@chakra-ui/react";
import axios from "axios";
import { useRef, useState } from "react";
import { BsSearch } from "react-icons/bs";

interface Props {
  setKeyword: React.Dispatch<React.SetStateAction<string>>;
}

const SearchInput = ({ setKeyword }: Props) => {
  const [searchValue, setSearchValue] = useState("");

  return (
    <InputGroup>
      <InputLeftElement children={<BsSearch color="white" />} />
      <Input
        bg="gray.700"
        borderRadius={20}
        placeholder="Search Item..."
        variant="filled"
        textColor="white"
        value={searchValue}
        onChange={(e) => {
          const { value } = e.target;
          setSearchValue(value);
          setKeyword(value);
        }}
      />
    </InputGroup>
  );
};

export default SearchInput;
