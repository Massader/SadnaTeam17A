import { Box, Button, Flex, HStack, Text } from "@chakra-ui/react";
import SearchInput from "./SearchInput";
import { AiOutlineShoppingCart } from "react-icons/ai";
import axios from "axios";
import { ClientCredentialsContext } from "../App";
import { useContext } from "react";

interface Props {
  clientCredentials: string;
  newClientCredentials: () => {};
  onSearch: (searchText: string) => void;
  onSignIn: () => void;
  onSignOut: () => void;
  isLogged: boolean;
  username?: string;
}

const SearchBar = ({
  clientCredentials,
  newClientCredentials,
  onSearch,
  onSignIn,
  isLogged,
  onSignOut,
}: Props) => {
  const { username, setUsername, setAdmin } = useContext(
    ClientCredentialsContext
  );

  const handleSignOut = async () => {
    const response = await axios.post(
      "http://localhost:8080/api/v1/users/logout",
      {
        clientCredentials,
      }
    );
    if (!response.data.error) {
      newClientCredentials();
      setUsername("");
      setAdmin(false);
      onSignOut();
    } else {
      console.log(response.data.message);
    }
  };
  return (
    <HStack padding="15px" justifyContent="space-between">
      <SearchInput onSearch={onSearch} />
      <Flex>
        <Box marginRight={3}>
          {!isLogged && <Button onClick={onSignIn}>Sign In</Button>}
          {isLogged && (
            <Flex>
              <Button marginRight={2} onClick={handleSignOut}>
                Sign Out
              </Button>
              <Text marginTop={2} color="white">
                Hello {username}!
              </Text>
            </Flex>
          )}
        </Box>
        <AiOutlineShoppingCart size={40} color="white" />
      </Flex>
    </HStack>
  );
};

export default SearchBar;
