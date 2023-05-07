import { Button, Flex, Stack } from "@chakra-ui/react";
import React from "react";

const AdminPage = () => {
  return (
    <Flex padding={10} justifyContent="center" alignItems="center">
      <Stack spacing={4} w="100%" maxW="400px" px={4}>
        <Button colorScheme="blackAlpha">Delete user</Button>
      </Stack>
    </Flex>
  );
};

export default AdminPage;
