import { Button, Input, Stack, Text } from "@chakra-ui/react";
import { Store } from "../types";
import { useContext, useEffect, useState } from "react";
import axios from "axios";
import { ClientCredentialsContext } from "../App";
import AddItemToStore from "./AddItemToStore";

interface Props {
  storeId: string;
}

const ManageStore = ({ storeId }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const onBack = () => setPage(pages[0]);

  const pages = ["manage", "addItem"];
  const [page, setPage] = useState(pages[0]);

  useEffect(() => {
    setPage(pages[0]);
  }, [storeId]);

  const fetchStore = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/store-info/storeId=${storeId}`
    );
    if (!response.data.error) {
      console.log(response.data.value);
      setStore(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  const [store, setStore] = useState<Store>();

  useEffect(() => {
    fetchStore();
  }, [storeId]);

  return (
    <>
      <Stack alignItems="center" marginTop={3}>
        <Text as="b" fontSize="2xl">
          {store?.name}
        </Text>
      </Stack>
      <Stack marginTop={3} paddingLeft={2} paddingRight={2}>
        {page === "manage" && (
          <>
            <Button onClick={() => setPage(pages[1])} whiteSpace="normal">
              Add item
            </Button>
            <Button whiteSpace="normal">Inventory management</Button>
            <Button whiteSpace="normal">Purchase and discount policy</Button>
            <Button whiteSpace="normal">Traceability constraints</Button>
            <Button whiteSpace="normal">Appointment of store owner</Button>
            <Button whiteSpace="normal">
              Removing a store owner appointment
            </Button>
            <Button whiteSpace="normal">Change store manager privileges</Button>
            <Button whiteSpace="normal">
              Removal of appointment of store manager
            </Button>
            <Button whiteSpace="normal">Store closing</Button>
            <Button whiteSpace="normal">Information on store positions</Button>
            <Button whiteSpace="normal">
              Receiving information and response
            </Button>
            <Button whiteSpace="normal">Purchase history in store</Button>
          </>
        )}
        {page === "addItem" && (
          <AddItemToStore storeId={storeId} onBack={onBack} />
        )}
      </Stack>
    </>
  );
};

export default ManageStore;
