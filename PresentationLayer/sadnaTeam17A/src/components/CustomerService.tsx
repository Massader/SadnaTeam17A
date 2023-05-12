import { Button, Flex, Stack } from "@chakra-ui/react";
import { useState } from "react";
import ChangePassword from "./CustomerService/ChangePassword";
import AddSecurityQuestion from "./CustomerService/AddSecurityQuestion";
import PurchaseHistory from "./CustomerService/PurchaseHistory";

export const CustomerService = () => {
  const pages = [
    "home",
    "purchaseHistory",
    "changePassword",
    "securityQuestion",
  ];
  const [page, setPage] = useState(pages[0]);

  return (
    <>
      <Flex padding={10} justifyContent="center" alignItems="center">
        <Stack spacing={4} w="100%" maxW="400px" px={4}>
          {page === "home" && (
            <>
              <Button
                onClick={() => setPage(pages[1])}
                colorScheme="blackAlpha"
              >
                Purchase history
              </Button>
              <Button
                onClick={() => setPage(pages[2])}
                colorScheme="blackAlpha"
              >
                Change Password
              </Button>
              <Button
                onClick={() => setPage(pages[3])}
                colorScheme="blackAlpha"
              >
                Add security question
              </Button>
            </>
          )}
          {page === "changePassword" && (
            <ChangePassword pages={pages} setPage={setPage} />
          )}
          {page === "securityQuestion" && (
            <AddSecurityQuestion pages={pages} setPage={setPage} />
          )}
          {page === "purchaseHistory" && (
            <Button
              colorScheme="blackAlpha"
              size="lg"
              onClick={() => setPage(pages[0])}
            >
              Back
            </Button>
          )}
        </Stack>
      </Flex>
      {page === "purchaseHistory" && (
        <PurchaseHistory pages={pages} setPage={setPage} />
      )}
    </>
  );
};

export default CustomerService;
