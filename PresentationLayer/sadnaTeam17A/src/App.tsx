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
import MyCart from "./components/MyCart";
import AdminPage from "./components/AdminPage";
import ForgotPassword from "./components/ForgotPassword";
import SaleHistory from "./components/ManageStore/SaleHistory";
import InventoryManagement from "./components/ManageStore/InventoryManagement";
import Notifications from "./components/Notifications";
import SetManagerPermissions from "./components/ManageStore/SetManagerPermissions";
import PositionsInfo from "./components/ManageStore/PositionsInfo";
import PurchaseAndDiscountPolicy from "./components/ManageStore/PurchaseAndDiscountPolicy";
import AddPurchaseRules from "./components/ManageStore/AddPurchaseRules";
import Messages from "./components/Messages";
import ViewBids from "./components/ManageStore/ViewBids";

type ClientCredentialsContextValue = {
  clientCredentials: string;
  setClientCredentials: React.Dispatch<React.SetStateAction<string>>;
  username: string;
  setUsername: React.Dispatch<React.SetStateAction<string>>;
  isAdmin: boolean;
  setAdmin: React.Dispatch<React.SetStateAction<boolean>>;
  roles: Role[];
  setRoles: React.Dispatch<React.SetStateAction<Role[]>>;
  isLogged: boolean;
  setStoreManage: React.Dispatch<React.SetStateAction<string>>;
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
    isLogged: false,
    setStoreManage: () => {},
  });

function App() {
  const pages = [
    "home", //0
    "signIn", //1
    "myStores", //2
    "register", //3
    "customerService", //4
    "stores", //5
    "createStore", //6
    "myCart", //7
    "adminPage", //8
    "forgot", //9
    "saleHistory", //10
    "inventoryManagement", //11
    "setManagerPermissions", //12
    "positionInfo", //13
    "purchaseAndDiscountPolicy", //14
    "messages", //15
    "viewBids", //16
  ];

  const leftPages = ["empty", "filters", "manageStore"];

  const rightPages = ["notifications"];

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
  }, [clientCredentials]);

  const [source, setSource] = useState<EventSource | null>(null);

  const [username, setUsername] = useState("");

  const [isAdmin, setAdmin] = useState(false);

  const [roles, setRoles] = useState<Role[]>([]);

  const [storeManage, setStoreManage] = useState("");

  const [page, setPage] = useState(pages[0]);

  const [leftPage, setLeftPage] = useState(leftPages[1]);

  const [rightPage, setRightPage] = useState(rightPages[0]);

  const [logged, setLogged] = useState(false);

  const [keyword, setKeyword] = useState("");
  const [itemRating, setItemRating] = useState(0);
  const [storeRating, setStoreRating] = useState(0);
  const [minPrice, setMinPrice] = useState(0);
  const [maxPrice, setMaxPrice] = useState(1000000);
  const [category, setCategory] = useState("");

  const onHome = () => {
    setPage(pages[0]);
    setLeftPage(leftPages[1]);
    setRightPage(rightPages[0]);
    setStoreManage("");
  };

  const onSpecStore = (storeId: string) => {
    setStoreManage(storeId);
    setLeftPage(leftPages[1]);
    setPage(pages[0]);
  };

  const onMyStores = () => {
    setLeftPage(leftPages[0]);
    setPage(pages[2]);
  };

  const onSignOut = () => {
    setLogged(false);
    setStoreManage("");
    setLeftPage(leftPages[1]);
    setPage(pages[0]);
    setNotifications([]);
  };

  const onLogin = (id: string) => {
    setLeftPage(leftPages[1]);
    setPage(pages[0]);
    setLogged(true);
    getNotifier(id);
  };

  const getNotifier = async (id: string) => {
    const source = new EventSource(
      `http://localhost:8080/api/v1/alerts/get-notifier/id=${id}`
    );
    console.log(source);
    source.addEventListener("message", (event) => {
      console.log(event.data);
    });
    source.onerror = (event) => {
      console.error("SSE error:", event);
    };
    source.onmessage = (event) => {
      const eventData = JSON.parse(event.data);
      const message = eventData.message;
      notifications.push(message);
      setNotificationAlert(true);
    };
    setSource(source);
    return source;
  };

  const [notifications, setNotifications] = useState<string[]>([]);
  const [notificationAlert, setNotificationAlert] = useState(false);

  const onManageStore = (id: string) => {
    setLeftPage(leftPages[2]);
    setStoreManage(id);
  };

  const onCreateStore = () => {
    setPage(pages[6]);
    setLeftPage(leftPages[0]);
  };

  const onShop = (storeId: string) => {
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
        isLogged: logged,
        setStoreManage: setStoreManage,
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
            isLogged={logged}
            onSignOut={onSignOut}
            setKeyword={setKeyword}
            setPage={setPage}
            pages={pages}
            setLeftPage={setLeftPage}
            leftPages={leftPages}
            source={source || null}
          />
        </GridItem>
        <GridItem height="70px" area="searchBar3" bg="blackAlpha.900" />
        <GridItem height="50px" area="nav1" bg="blackAlpha.700" />
        <GridItem height="50px" area="nav2" bg="blackAlpha.700">
          <NavBar
            onHome={onHome}
            onMyStores={onMyStores}
            isLogged={logged}
            setPage={setPage}
            pages={pages}
            setLeftPage={setLeftPage}
            leftPages={leftPages}
          ></NavBar>
        </GridItem>
        <GridItem height="50px" area="nav3" bg="blackAlpha.700" />

        <GridItem area="left" bg="white">
          {/* left */}
          {leftPage === "manageStore" && (
            <ManageStore
              storeId={storeManage}
              pages={pages}
              setPage={setPage}
            />
          )}
          {leftPage === "filters" && (
            <Filters
              setItemRating={setItemRating}
              setMinPrice={setMinPrice}
              setMaxPrice={setMaxPrice}
              setCategory={setCategory}
            />
          )}
        </GridItem>

        <GridItem area="middle" bg="gray.200" minH="100vh">
          {page === "home" && (
            <ItemGrid
              keyword={keyword}
              category={category}
              minPrice={minPrice}
              maxPrice={maxPrice}
              itemRating={itemRating}
              storeId={storeManage}
            />
          )}
          {page === "stores" && (
            <StoresGrid
              onShop={(storeId: string) => onShop(storeId)}
            ></StoresGrid>
          )}
          {page === "signIn" && (
            <SignIn
              setPage={setPage}
              pages={pages}
              newClientCredentials={(newClientCredentials) =>
                setClientCredentials(newClientCredentials)
              }
              onLogin={onLogin}
            ></SignIn>
          )}
          {page === "forgot" && (
            <ForgotPassword
              setPage={setPage}
              pages={pages}
              newClientCredentials={(newClientCredentials) =>
                setClientCredentials(newClientCredentials)
              }
              onLogin={onLogin}
            />
          )}
          {page === "register" && <Register setPage={setPage} pages={pages} />}
          {page === "createStore" && <CreateStore />}
          {page === "customerService" && <CustomerService />}
          {page === "myStores" && (
            <MyStores
              onCreateStore={onCreateStore}
              onManageStore={(id: string) => onManageStore(id)}
            />
          )}
          {page === "saleHistory" && (
            <SaleHistory
              storeId={storeManage}
              setPage={setPage}
              pages={pages}
            />
          )}
          {page === "inventoryManagement" && (
            <InventoryManagement
              storeId={storeManage}
              setPage={setPage}
              pages={pages}
            />
          )}
          {page === "viewBids" && (
            <ViewBids storeId={storeManage} setPage={setPage} pages={pages} />
          )}
          {page === "setManagerPermissions" && (
            <SetManagerPermissions
              storeId={storeManage}
              setPage={setPage}
              pages={pages}
            />
          )}
          {page === "positionInfo" && (
            <PositionsInfo
              storeId={storeManage}
              setPage={setPage}
              pages={pages}
            />
          )}
          {page === "purchaseAndDiscountPolicy" && (
            <PurchaseAndDiscountPolicy
              storeId={storeManage}
              setPage={setPage}
              pages={pages}
            />
          )}
          {page === "myCart" && <MyCart />}
          {page === "adminPage" && <AdminPage />}
          {page === "messages" && <Messages storeId={storeManage} />}
        </GridItem>

        <GridItem area="right" bg="white">
          {/* right */}
          {rightPage === "notifications" && logged && (
            <Notifications
              notifications={notifications}
              notificationAlert={notificationAlert}
              setNotificationAlert={setNotificationAlert}
              isLogged={logged}
            />
          )}
        </GridItem>
      </Grid>
    </ClientCredentialsContext.Provider>
  );
}

export default App;
