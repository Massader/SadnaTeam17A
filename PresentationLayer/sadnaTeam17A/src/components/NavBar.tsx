import { Button, HStack } from "@chakra-ui/react";
import { ClientCredentialsContext } from "../App";
import { useContext } from "react";

interface Props {
  isLogged: boolean;
  onHome: () => void;
  onCustomerService: () => void;
  onMyStores: () => void;
  onStores: () => void;
}

const NavBar = ({
  onHome,
  onStores,
  onCustomerService,
  isLogged,
  onMyStores,
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
        onClick={onStores}
        variant="ghost"
        color="white"
        _hover={{ borderColor: "white", color: "white" }}
        borderWidth="1px"
        borderColor="transparent"
      >
        Stores
      </Button>
      <Button
        variant="ghost"
        color="white"
        _hover={{ borderColor: "white", color: "white" }}
        borderWidth="1px"
        borderColor="transparent"
      >
        Categories
      </Button>
      {isLogged && (
        <Button
          onClick={onCustomerService}
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
