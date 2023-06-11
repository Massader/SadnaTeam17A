import { Box, Button, Flex, HStack, IconButton, Text } from "@chakra-ui/react";
import SearchInput from "./SearchInput";
import { AiOutlineShoppingCart } from "react-icons/ai";
import { RxEnvelopeClosed } from "react-icons/rx";
import axios from "axios";
import { ClientCredentialsContext } from "../App";
import { useContext, useState } from "react";

interface Props {
  newClientCredentials: () => {};
  onSignOut: () => void;
  isLogged: boolean;
  setKeyword: React.Dispatch<React.SetStateAction<string>>;
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
  setLeftPage: React.Dispatch<React.SetStateAction<string>>;
  leftPages: string[];
  source: EventSource | null;
}

const SearchBar = ({
  newClientCredentials,
  isLogged,
  onSignOut,
  setKeyword,
  setPage,
  pages,
  setLeftPage,
  leftPages,
  source,
}: Props) => {
  const { clientCredentials, username, setUsername, setAdmin, setStoreManage } =
    useContext(ClientCredentialsContext);

  const handleSignOut = async () => {
    const response = await axios.post(
      "http://localhost:8080/api/v1/users/logout",
      {
        clientCredentials,
      }
    );
    if (!response.data.error) {
      source?.close();
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
      <Box w="40%">
        <SearchInput setKeyword={setKeyword} />
      </Box>
      <Flex>
        <Box marginRight={3}>
          {!isLogged && (
            <Button
              onClick={() => {
                setPage(pages[1]);
                setLeftPage(leftPages[0]);
              }}
            >
              Sign In
            </Button>
          )}
          {isLogged && (
            <Flex>
              <IconButton
                marginRight={4}
                onClick={() => {
                  setStoreManage("");
                  setPage(pages[15]);
                  setLeftPage(leftPages[0]);
                }}
                size="xl"
                colorScheme="#000000"
                aria-label="Messages"
                icon={<RxEnvelopeClosed size={30} />}
              />
              <Button marginRight={2} onClick={handleSignOut}>
                Sign Out
              </Button>
              <Text marginTop={2} color="white">
                Hello {username}!
              </Text>
            </Flex>
          )}
        </Box>
        <IconButton
          onClick={() => {
            setStoreManage("");
            setPage(pages[7]);
            setLeftPage(leftPages[0]);
          }}
          size="xl"
          colorScheme="#000000"
          aria-label="Cart"
          icon={<AiOutlineShoppingCart size={40} />}
        />
      </Flex>
    </HStack>
  );
};

export default SearchBar;
