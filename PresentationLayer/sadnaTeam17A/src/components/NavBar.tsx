import { Button, HStack } from "@chakra-ui/react";
import { ClientCredentialsContext } from "../App";
import { useContext } from "react";

interface Props {
  isLogged: boolean;
  onHome: () => void;
  onMyStores: () => void;
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
  setLeftPage: React.Dispatch<React.SetStateAction<string>>;
  leftPages: string[];
}

const NavBar = ({
  onHome,
  isLogged,
  onMyStores,
  pages,
  setPage,
  setLeftPage,
  leftPages,
}: Props) => {
  const { isAdmin } = useContext(ClientCredentialsContext);
  return (
    <HStack padding={1}>
      <Button
        onClick={onHome}
        variant="ghost"
        color="white"
        _hover={{ borderColor: "white", color: "white" }}
        borderWidth="1px"
        borderColor="transparent"
      >
        Home
      </Button>
      <Button
        onClick={() => {
          setPage(pages[5]);
          setLeftPage(leftPages[0]);
        }}
        variant="ghost"
        color="white"
        _hover={{ borderColor: "white", color: "white" }}
        borderWidth="1px"
        borderColor="transparent"
      >
        Stores
      </Button>
      {/* <Button
        variant="ghost"
        color="white"
        _hover={{ borderColor: "white", color: "white" }}
        borderWidth="1px"
        borderColor="transparent"
      >
        Categories
      </Button> */}
      {isLogged && (
        <Button
          onClick={() => {
            setPage(pages[4]);
            setLeftPage(leftPages[0]);
          }}
          variant="ghost"
          color="white"
          _hover={{ borderColor: "white", color: "white" }}
          borderWidth="1px"
          borderColor="transparent"
        >
          Customer Service
        </Button>
      )}
      {isLogged && (
        <Button
          onClick={onMyStores}
          variant="ghost"
          color="white"
          _hover={{ borderColor: "white", color: "white" }}
          borderWidth="1px"
          borderColor="transparent"
        >
          My Stores
        </Button>
      )}
      {isAdmin && (
        <Button
          onClick={() => {
            setPage(pages[8]);
            setLeftPage(leftPages[0]);
          }}
          variant="ghost"
          color="white"
          _hover={{ borderColor: "white", color: "white" }}
          borderWidth="1px"
          borderColor="transparent"
        >
          Admin
        </Button>
      )}
    </HStack>
  );
};

export default NavBar;
