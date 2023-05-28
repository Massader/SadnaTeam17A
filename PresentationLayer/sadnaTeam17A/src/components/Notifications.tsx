import { Button, Flex, Heading, Stack, Text } from "@chakra-ui/react";
import axios from "axios";
import React, { useContext, useEffect, useState } from "react";
import { ClientCredentialsContext } from "../App";
import { NotificationType } from "../types";

interface Props {
  notifications: string[];
  notificationAlert: boolean;
  setNotificationAlert: React.Dispatch<React.SetStateAction<boolean>>;
  isLogged: boolean;
}

const Notifications = ({
  notifications,
  notificationAlert,
  setNotificationAlert,
  isLogged,
}: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const getNotifications = async () => {
    if (isLogged) {
      const response = await axios.get(
        `http://localhost:8080/api/v1/users/get-notifications/id=${clientCredentials}`
      );
      if (!response.data.error) {
        setLoggedOutNotifications(response.data.value);
        if (response.data.value.length !== 0) {
          setNotificationAlert(true);
        } else {
          setNotificationAlert(false);
        }
      } else {
        setNotificationAlert(false);
        console.log(response.data.error);
      }
    }
  };

  useEffect(() => {
    getNotifications();
  }, [isLogged]);

  const [loggedOutNotifications, setLoggedOutNotifications] = useState<
    NotificationType[]
  >([]);
  const [notificationOpen, setNotificationOpen] = useState(false);

  return (
    <>
      <Flex padding={2} justifyContent="center" alignItems="center">
        <Stack w="90%" spacing={4} px={3}>
          <Button
            textAlign="center"
            style={{ fontSize: 24, fontWeight: "bold" }}
            onClick={() => {
              setNotificationOpen(!notificationOpen);
              setNotificationAlert(false);
            }}
            textColor={notificationAlert ? "red.500" : "black"}
          >
            Notifications
          </Button>
          {notificationOpen && (
            <ul>
              {notifications.reverse().map((notification, index) => (
                <li key={index}>{notification}</li>
              ))}
            </ul>
          )}
          {notificationOpen && (
            <ul>
              {loggedOutNotifications.reverse().map((notification, index) => (
                <li key={index}>{notification.message}</li>
              ))}
            </ul>
          )}
        </Stack>
      </Flex>
    </>
  );
};

export default Notifications;
