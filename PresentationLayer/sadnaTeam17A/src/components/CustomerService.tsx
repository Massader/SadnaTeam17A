import { Button, Flex, Stack } from "@chakra-ui/react";

export const CustomerService = () => {
  return (
    <Flex padding={10} justifyContent="center" alignItems="center">
      <Stack spacing={4} w="100%" maxW="400px" px={4}>
        <Button colorScheme="blackAlpha">Purchase history</Button>
        <Button colorScheme="blackAlpha">Change Password</Button>
        <Button colorScheme="blackAlpha">Add security question</Button>
      </Stack>
    </Flex>
  );
};

export default CustomerService;
