import { Button, Flex, Input, Stack, Text } from "@chakra-ui/react";
import { Store } from "../types";
import { useContext, useEffect, useState } from "react";
import axios from "axios";
import { ClientCredentialsContext } from "../App";
import AddItemToStore from "./ManageStore/AddItemToStore";
import Appointment from "./ManageStore/Appointment";

interface Props {
  storeId: string;
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
}

const ManageStore = ({ storeId, setPage, pages }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const onBack = () => {
    setLeftPage(leftPages[0]);
    setCloseStoreSure(false);
  };

  const leftPages = [
    "manage",
    "addItem",
    "appointmentOwner",
    "appointmentManager",
    "removeAppointment",
  ];
  const [leftPage, setLeftPage] = useState(leftPages[0]);

  useEffect(() => {
    setLeftPage(leftPages[0]);
  }, [storeId]);

  const fetchStore = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/store-info/id=${clientCredentials}&storeId=${storeId}`
    );
    if (!response.data.error) {
      console.log(response.data.value);
      setStore(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  const handleCloseStore = async () => {
    const response = await axios.put(
      "http://localhost:8080/api/v1/stores/close-store",
      {
        clientCredentials: clientCredentials,
        targetId: storeId,
      }
    );
    if (!response.data.error) {
      setErrorMsg(false);
      setMessage("Store Closed!");
      setCloseStoreSure(false);
      setStoreCloseOpen(!storeCloseOpen);
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  const handleReopenStore = async () => {
    const response = await axios.put(
      "http://localhost:8080/api/v1/stores/reopen-store",
      {
        clientCredentials: clientCredentials,
        targetId: storeId,
      }
    );
    if (!response.data.error) {
      setErrorMsg(false);
      setMessage("Store Reopened!");
      setCloseStoreSure(false);
      setStoreCloseOpen(!storeCloseOpen);
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  const [store, setStore] = useState<Store>();

  const [closeStoreSure, setCloseStoreSure] = useState(false);

  const [storeCloseOpen, setStoreCloseOpen] = useState(false);

  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");

  useEffect(() => {
    fetchStore();
  }, [storeId, storeCloseOpen]);

  useEffect(() => {
    setCloseStoreSure(false);
  }, [storeId]);

  return (
    <>
      <Stack alignItems="center" marginTop={3}>
        <Text as="b" fontSize="2xl">
          {store?.name}
        </Text>
      </Stack>
      <Stack marginTop={3} paddingLeft={2} paddingRight={2}>
        {leftPage === "manage" && (
          <>
            <Button
              onClick={() => setLeftPage(leftPages[1])}
              whiteSpace="normal"
            >
              Add Item
            </Button>
            <Button onClick={() => setPage(pages[11])} whiteSpace="normal">
              Inventory Management
            </Button>
            <Button onClick={() => setPage(pages[14])} whiteSpace="normal">
              Purchase and Discount Policy
            </Button>
            <Button whiteSpace="normal">Traceability Constraints</Button>
            <Button
              onClick={() => setLeftPage(leftPages[2])}
              whiteSpace="normal"
            >
              Appointment of Store Owner
            </Button>
            <Button
              onClick={() => setLeftPage(leftPages[3])}
              whiteSpace="normal"
            >
              Appointment of Store Manager
            </Button>
            <Button
              onClick={() => setLeftPage(leftPages[4])}
              whiteSpace="normal"
            >
              Removing a Store Role Appointment
            </Button>
            <Button onClick={() => setPage(pages[12])} whiteSpace="normal">
              Set Store Manager Permissions
            </Button>
            {!store?.isClosed && (
              <Button
                onClick={() => setCloseStoreSure(!closeStoreSure)}
                whiteSpace="normal"
              >
                Close Store
              </Button>
            )}
            {store?.isClosed && (
              <Button onClick={handleReopenStore} whiteSpace="normal">
                Reopen Store
              </Button>
            )}
            {closeStoreSure && (
              <Flex alignItems="center" justifyContent="center" padding={3}>
                <Text>Sure? </Text>
                <Button
                  onClick={handleCloseStore}
                  marginLeft={3}
                  colorScheme="blackAlpha"
                  whiteSpace="normal"
                >
                  V
                </Button>
              </Flex>
            )}
            <Button onClick={() => setPage(pages[13])} whiteSpace="normal">
              Information on Store Positions
            </Button>
            <Button onClick={() => setPage(pages[15])} whiteSpace="normal">
              Store Messages
            </Button>
            <Button onClick={() => setPage(pages[10])} whiteSpace="normal">
              Purchase History in Store
            </Button>
          </>
        )}
        {leftPage === "addItem" && (
          <AddItemToStore storeId={storeId} onBack={onBack} />
        )}
        {leftPage === "appointmentOwner" && (
          <Appointment storeId={storeId} onBack={onBack} role={"owner"} />
        )}
        {leftPage === "appointmentManager" && (
          <Appointment storeId={storeId} onBack={onBack} role={"manager"} />
        )}
        {leftPage === "removeAppointment" && (
          <Appointment storeId={storeId} onBack={onBack} role={"remove"} />
        )}
      </Stack>
    </>
  );
};

export default ManageStore;
