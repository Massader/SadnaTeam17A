import { Button, Flex, Heading, Input, Text } from "@chakra-ui/react";
import React, { useContext, useState } from "react";
import { ClientCredentialsContext } from "../../App";
import axios from "axios";

interface Props {
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
}

const ChangePassword = ({ setPage, pages }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [passwordChanged, setPasswordChanged] = useState(false);
  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");

  const handleChangePassword = async () => {
    const response = await axios.put(
      "http://localhost:8080/api/v1/users/security/change-password",
      {
        clientCredentials: clientCredentials,
        oldPassword: oldPassword,
        newPassword: newPassword,
      }
    );
    if (!response.data.error) {
      setPasswordChanged(true);
      setErrorMsg(false);
      setMessage("Password changed!");
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  return (
    <>
      <Heading padding={5} textAlign="center">
        Change Password
      </Heading>
      {!passwordChanged && (
        <>
          <Input
            bg="white"
            placeholder="Old password"
            value={oldPassword}
            type="password"
            onChange={(oldPassword) => setOldPassword(oldPassword.target.value)}
          />
          <Input
            bg="white"
            placeholder="New password"
            value={newPassword}
            type="password"
            onChange={(newPassword) => setNewPassword(newPassword.target.value)}
          />
          <Button colorScheme="blue" size="lg" onClick={handleChangePassword}>
            Set new Password
          </Button>
        </>
      )}
      <Button
        colorScheme="blackAlpha"
        size="lg"
        onClick={() => setPage(pages[0])}
      >
        Back
      </Button>
      <Flex justifyContent="center">
        {errorMsg ? <Text color="red">{message}</Text> : <Text>{message}</Text>}
      </Flex>
    </>
  );
};

export default ChangePassword;
