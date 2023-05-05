import { Grid, GridItem } from "@chakra-ui/react";
import SearchBar from "./components/SearchBar";
import NavBar from "./components/NavBar";
import ItemGrid from "./components/ItemGrid";
import SignIn from "./components/SignIn";
import { useEffect, useState } from "react";
import MyStores from "./components/MyStores";
import ManageStore from "./components/ManageStore";
import Filters from "./components/Filters";
import Register from "./components/Register";
import CustomerService from "./components/CustomerService";
import StoresGrid from "./components/StoresGrid";
import axios from "axios";
import CreateStore from "./components/CreateStore";
import React from "react";
import { Role } from "./types";

type ClientCredentialsContextValue = {
  clientCredentials: string;
  setClientCredentials: React.Dispatch<React.SetStateAction<string>>;
  username: string;
  setUsername: React.Dispatch<React.SetStateAction<string>>;
  isAdmin: boolean;
  setAdmin: React.Dispatch<React.SetStateAction<boolean>>;
  roles: Role[];
  setRoles: React.Dispatch<React.SetStateAction<Role[]>>;
};

export const ClientCredentialsContext =
  React.createContext<ClientCredentialsContextValue>({
    clientCredentials: "",
    setClientCredentials: () => {},
    username: "",
    setUsername: () => {},
    isAdmin: false,
    setAdmin: () => {},
    roles: [],
    setRoles: () => {},
  });

function App() {
  const pages = [
    "home",
    "signIn",
    "myStores",
    "register",
    "customerService",
    "stores",
    "createStore",
  ];

  const createClient = async () => {
    try {
      const response = await axios.post(
        "http://localhost:8080/api/v1/users/create-client"
      );
      setClientCredentials(response.data.value);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    createClient();
  }, []);

  const [clientCredentials, setClientCredentials] = useState("");
  useEffect(() => {
    console.log(clientCredentials);
    console.log(username);
    console.log(isAdmin);
    console.log(roles);
  }, [clientCredentials]);

  const [username, setUsername] = useState("");

  const [isAdmin, setAdmin] = useState(false);

  const [roles, setRoles] = useState<Role[]>([]);

  const [searchText, setSearchText] = useState("");

  const [storeManage, setStoreManage] = useState("");

  const [manageStoreOn, setmanageStoreOn] = useState(false);

  const [page, setPage] = useState(pages[0]);

  const [logged, setLogged] = useState(false);

  const [keyword, setKeyword] = useState("");
  const [category, setCategory] = useState("");

  const onHome = () => {
    setPage(pages[0]);
    setSearchText("");
    setStoreManage("");
  };

  const onCustomerService = () => setPage(pages[4]);

  const onStores = () => setPage(pages[5]);

  const onSpecStore = (storeId: string) => {
    setStoreManage(storeId);
    setPage(pages[0]);
  };

  const onMyStores = () => {
    setmanageStoreOn(false);
    setPage(pages[2]);
  };

  const onSignIn = () => setPage(pages[1]);

  const onSignOut = () => {
    setLogged(false);
    setPage(pages[0]);
  };

  const onLogin = () => {
    setPage(pages[0]);
    setLogged(true);
  };

  const onManageStore = (id: string) => {
    setmanageStoreOn(true);
    setStoreManage(id);
  };

  const onCreateStore = () => setPage(pages[6]);

  const onRegister = () => setPage(pages[3]);

  const onShop = (storeId: string) => {
    console.log(storeId);
    onSpecStore(storeId);
  };

  //when page refresh
  const handleSignOutOnRefresh = async () => {
    const response = await axios.post(
      "http://localhost:8080/api/v1/users/logout",
      {
        clientCredentials,
      }
    );
    if (response.data.error) console.log(response.data.message);
  };

  useEffect(() => {
    const handleBeforeUnload = (event: BeforeUnloadEvent) => {
      event.preventDefault();
      handleSignOutOnRefresh();
    };

    window.addEventListener("beforeunload", handleBeforeUnload);
    return () => {
      window.removeEventListener("beforeunload", handleBeforeUnload);
    };
  }, []);

  return (
    <ClientCredentialsContext.Provider
      value={{
        clientCredentials: clientCredentials,
        setClientCredentials: setClientCredentials,
        username: username,
        setUsername: setUsername,
        isAdmin: isAdmin,
        setAdmin: setAdmin,
        roles: roles,
        setRoles: setRoles,
      }}
    >
      <Grid
        templateAreas={`"searchBar1 searchBar2 searchBar3" "nav1 nav2 nav3" "left middle right"`}
        templateColumns="20% 60% 20%"
      >
        <GridItem height="70px" area="searchBar1" bg="blackAlpha.900" />
        <GridItem height="70px" area="searchBar2" bg="blackAlpha.900">
          <SearchBar
            newClientCredentials={() => createClient()}
            clientCredentials={clientCredentials}
            username={username}
            isLogged={logged}
            onSignIn={onSignIn}
            onSignOut={onSignOut}
            onSearch={(searchText) => {
              setPage(pages[0]);
              setSearchText(searchText);
            }}
          />
        </GridItem>
        <GridItem height="70px" area="searchBar3" bg="blackAlpha.900" />
        <GridItem height="50px" area="nav1" bg="blackAlpha.700" />
        <GridItem height="50px" area="nav2" bg="blackAlpha.700">
          <NavBar
            onCustomerService={onCustomerService}
            onHome={onHome}
            onStores={onStores}
            onMyStores={onMyStores}
            isLogged={logged}
          ></NavBar>
        </GridItem>
        <GridItem height="50px" area="nav3" bg="blackAlpha.700" />

        <GridItem area="left" bg="white">
          {/* left */}
          {page === "myStores" && manageStoreOn && (
            <ManageStore storeId={storeManage} />
          )}
          {page === "home" && <Filters />}
        </GridItem>

        <GridItem area="middle" bg="gray.200" minH="100vh">
          {page === "home" && (
            <ItemGrid keyword="" category="" storeId={storeManage} />
          )}
          {page === "stores" && (
            <StoresGrid
              onShop={(storeId: string) => onShop(storeId)}
            ></StoresGrid>
          )}
          {page === "signIn" && (
            <SignIn
              newClientCredentials={(newClientCredentials) =>
                setClientCredentials(newClientCredentials)
              }
              onRegister={onRegister}
              onLogin={onLogin}
            ></SignIn>
          )}
          {page === "register" && <Register />}
          {page === "createStore" && <CreateStore />}
          {page === "customerService" && <CustomerService />}
          {page === "myStores" && (
            <MyStores
              onCreateStore={onCreateStore}
              onManageStore={(id: string) => onManageStore(id)}
            />
          )}
        </GridItem>

        <GridItem area="right" bg="white">
          {/* Right */}
        </GridItem>
      </Grid>
    </ClientCredentialsContext.Provider>
  );
}

export default App;
