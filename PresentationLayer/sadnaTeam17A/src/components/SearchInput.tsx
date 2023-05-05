import { Input, InputGroup, InputLeftElement } from "@chakra-ui/react";
import axios from "axios";
import { useRef } from "react";
import { BsSearch } from "react-icons/bs";

interface Props {
  onSearch: (searchText: string) => void;
}

const SearchInput = ({ onSearch }: Props) => {
  const ref = useRef<HTMLInputElement>(null);
  return (
    <form
      onSubmit={(event) => {
        event.preventDefault();
        if (ref.current) onSearch(ref.current.value);
      }}
    >
      <InputGroup>
        <InputLeftElement children={<BsSearch color="white" />} />
        <Input
          bg="gray.700"
          ref={ref}
          borderRadius={20}
          placeholder="Search Item..."
          variant="filled"
          textColor="white"
        />
      </InputGroup>
    </form>
  );
};

export default SearchInput;
