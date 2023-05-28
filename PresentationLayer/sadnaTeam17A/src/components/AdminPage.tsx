import { Button, Flex, Stack } from "@chakra-ui/react";
import React, { useState } from "react";
import DeleteUser from "./Admin/DeleteUser";
import RegisterAdmin from "./Admin/RegisterAdmin";
import Complaints from "./Admin/Complaints";

const AdminPage = () => {
  const pages = ["home", "deleteUser", "registerAdmin", "complaints"];
  const [page, setPage] = useState(pages[0]);
  return (
    <Flex marginTop={4} justifyContent="center">
      {page === "home" && (
        <>
          <Stack padding={6} spacing={4} w="50%" maxW="400px">
            <Button onClick={() => setPage(pages[1])} colorScheme="blackAlpha">
              Delete user
            </Button>
            <Button onClick={() => setPage(pages[2])} colorScheme="blackAlpha">
              Register admin
            </Button>
            <Button onClick={() => setPage(pages[3])} colorScheme="blackAlpha">
              Complaints
            </Button>
          </Stack>
        </>
      )}
      {page === "deleteUser" && <DeleteUser pages={pages} setPage={setPage} />}
      {page === "registerAdmin" && (
        <RegisterAdmin pages={pages} setPage={setPage} />
      )}
      {page === "complaints" && <Complaints pages={pages} setPage={setPage} />}
    </Flex>
  );
};

export default AdminPage;
