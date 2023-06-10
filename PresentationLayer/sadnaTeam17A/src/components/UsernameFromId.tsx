import { Text } from "@chakra-ui/react";
import axios from "axios";
import React, { useEffect, useState } from "react";

interface Props {
  userId: string;
}

const UsernameFromId = ({ userId }: Props) => {
  const [username, setUsername] = useState("");

  const getUserInfo = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/info/id=${userId}`
    );
    if (!response.data.error) {
      setUsername(response.data.value.username);
    } else {
      console.log(response.data.error);
    }
  };

  useEffect(() => {
    getUserInfo();
  }, [userId]);
  return <Text>{username}</Text>;
};

export default UsernameFromId;
