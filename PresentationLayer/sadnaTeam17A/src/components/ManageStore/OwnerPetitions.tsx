import React from "react";
import { ClientCredentialsContext } from "../../App";
import { useContext, useEffect, useState } from "react";
import { Button, Flex, Input, Stack, Text } from "@chakra-ui/react";
import axios from "axios";
import { OwnerPetitionsType } from "../../types";
import UsernameFromId from "../UsernameFromId";

interface Props {
  storeId: string;
  onBack: () => void;
}

const OwnerPetitions = ({ storeId, onBack }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");

  const getOwnerPetitions = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/role/get-store-owner-appointments/id=${clientCredentials}&storeId=${storeId}`
    );
    if (!response.data.error) {
      console.log(response.data.value);
      setOwnerPetitions(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  useEffect(() => {
    getOwnerPetitions();
  }, []);

  const [ownerPetitions, setOwnerPetitions] = useState<OwnerPetitionsType[]>(
    []
  );

  const approveOwner = async (appointee: string) => {
    const response = await axios.post(
      `http://localhost:8080/api/v1/stores/role/appoint-owner`,
      {
        clientCredentials: clientCredentials,
        appointee: appointee,
        storeId: storeId,
      }
    );
    if (!response.data.error) {
      getOwnerPetitions();
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  const removeApproval = async (appointee: string) => {
    const response = await axios.delete(
      `http://localhost:8080/api/v1/stores/role/remove-store-owner-approval`,
      {
        data: {
          clientCredentials: clientCredentials,
          appointee: appointee,
          storeId: storeId,
        },
      }
    );
    if (!response.data.error) {
      getOwnerPetitions();
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  return (
    <>
      <Stack marginTop={3} paddingLeft={2} paddingRight={2}>
        <Button onClick={onBack} whiteSpace="normal">
          Back
        </Button>
        {ownerPetitions.map((ownerPetition, index) => {
          return (
            <Stack key={index}>
              <Flex alignItems="center">
                <Text mr={1}>Appointee username:</Text>
                <UsernameFromId userId={ownerPetition.appointeeId} />
              </Flex>
              {!ownerPetition.ownersList.includes(clientCredentials) && (
                <Button
                  onClick={() => {
                    approveOwner(ownerPetition.appointeeId);
                  }}
                  colorScheme="blue"
                >
                  Approve
                </Button>
              )}
              {ownerPetition.ownersList.includes(clientCredentials) && (
                <Button
                  onClick={() => {
                    removeApproval(ownerPetition.appointeeId);
                  }}
                  colorScheme="blackAlpha"
                >
                  Remove Approval
                </Button>
              )}
              <Flex justifyContent="center">
                {errorMsg ? (
                  <Text color="red">{message}</Text>
                ) : (
                  <Text>{message}</Text>
                )}
              </Flex>
            </Stack>
          );
        })}
      </Stack>
    </>
  );
};

export default OwnerPetitions;
