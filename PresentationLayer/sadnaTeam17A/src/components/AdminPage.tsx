import { Button, Flex, Stack } from "@chakra-ui/react";
import React, { useState } from "react";
import DeleteUser from "./Admin/DeleteUser";
import RegisterAdmin from "./Admin/RegisterAdmin";

const AdminPage = () => {
  const pages = ["home", "deleteUser", "registerAdmin"];
  const [page, setPage] = useState(pages[0]);
  return (
    <Flex padding={10} justifyContent="center" alignItems="center">
      <Stack spacing={4} w="100%" maxW="400px" px={4}>
        {page === "home" && (
          <>
            <Button onClick={() => setPage(pages[1])} colorScheme="blackAlpha">
              Delete user
            </Button>
            <Button onClick={() => setPage(pages[2])} colorScheme="blackAlpha">
              Register admin
            </Button>
          </>
        )}
        {page === "deleteUser" && (
          <DeleteUser pages={pages} setPage={setPage} />
        )}
        {page === "registerAdmin" && (
          <RegisterAdmin pages={pages} setPage={setPage} />
        )}
      </Stack>
    </Flex>
  );
};

export default AdminPage;
