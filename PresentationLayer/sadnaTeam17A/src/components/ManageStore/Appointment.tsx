import { Button, Flex, Input, Stack, Text } from "@chakra-ui/react";
import { useContext, useEffect, useState } from "react";
import axios from "axios";
import { ClientCredentialsContext } from "../../App";

interface Props {
  storeId: string;
  role: string;
  onBack: () => void;
}

const Appointment = ({ storeId, onBack, role }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const getUserId = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/get-user-by-username/username=${username}`
    );
    if (!response.data.error) {
      setAppointee(response.data.value.id);
    } else {
      setAppointee("");
    }
  };

  const addManager = async () => {
    if (appointee === "") {
      setErrorMsg(true);
      setMessage("User not found");
    } else {
      const response = await axios.post(
        `http://localhost:8080/api/v1/stores/role/appoint-manager`,
        {
          clientCredentials: clientCredentials,
          appointee: appointee,
          storeId: storeId,
        }
      );
      if (!response.data.error) {
        setErrorMsg(false);
        setMessage(`${username} is now a manager!`);

      } else {
        setErrorMsg(true);
        setMessage(response.data.message);
      }
    }
  };

  const addOwner = async () => {
    if (appointee === "") {
      setErrorMsg(true);
      setMessage("User not found");
    } else {
      const response = await axios.post(
        `http://localhost:8080/api/v1/stores/role/appoint-owner`,
        {
          clientCredentials: clientCredentials,
          appointee: appointee,
          storeId: storeId,
        }
      );
      if (!response.data.error) {
        setErrorMsg(false);
        setMessage(
          username +
            " is now waiting for all the owners to approve him as owner"
        );
      } else {
        setErrorMsg(true);
        setMessage(response.data.message);
      }
    }
  };

  const removeRole = async () => {
    const response = await axios.delete(
      `http://localhost:8080/api/v1/stores/role/remove`,
      {
        data: {
          clientCredentials: clientCredentials,
          appointee: appointee,
          storeId: storeId,
        },
      }
    );
    if (!response.data.error) {
      setErrorMsg(false);
      setMessage(username + " role removed.");
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  const [username, setUsername] = useState("");
  const [appointee, setAppointee] = useState("");

  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");

  useEffect(() => {
    getUserId();
  }, [username]);

  return (
    <>
      <Stack marginTop={3} paddingLeft={2} paddingRight={2}>
        <Input
          bg="white"
          placeholder="Username"
          value={username}
          onChange={(username) => setUsername(username.target.value)}
        />
        {role === "manager" && (
          <Button
            colorScheme="blue"
            onClick={() => addManager()}
            whiteSpace="normal"
          >
            Appoint new manager
          </Button>
        )}
        {role === "owner" && (
          <Button
            colorScheme="blue"
            onClick={() => addOwner()}
            whiteSpace="normal"
          >
            Appoint new owner
          </Button>
        )}
        {role === "remove" && (
          <Button
            colorScheme="blue"
            onClick={() => removeRole()}
            whiteSpace="normal"
          >
            Remove user role
          </Button>
        )}
        <Flex justifyContent="center">
          {errorMsg ? (
            <Text color="red">{message}</Text>
          ) : (
            <Text>{message}</Text>
          )}
        </Flex>
        <Button onClick={onBack} whiteSpace="normal">
          Back
        </Button>
      </Stack>
    </>
  );
};

export default Appointment;
