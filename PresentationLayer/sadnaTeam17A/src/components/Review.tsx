import React, { useEffect, useState } from "react";
import { ReviewType } from "../types";
import { Divider, Flex, Stack, Text } from "@chakra-ui/react";
import axios from "axios";
import { AiFillStar, AiOutlineStar } from "react-icons/ai";

interface Props {
  review: ReviewType;
}

const Review = ({ review }: Props) => {
  const getUserInfo = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/info/id=${review.reviewer}`
    );
    if (!response.data.error) {
      setUsername(response.data.value.username);
    } else {
      console.log(response.data.error);
    }
  };

  const [username, setUsername] = useState("");

  useEffect(() => {
    getUserInfo();
  }, []);

  const date = new Date(review.timestamp);
  const formattedDate = `${date.getDate()}/${date.getMonth() + 1}/${date
    .getFullYear()
    .toString()
    .substr(-2)}`;

  return (
    <>
      <Stack spacing={1}>
        <Text as="b" fontSize="md">
          {username}
        </Text>
        <Text>{review.text}</Text>
        <Flex>
          {[...Array(5)].map((_, index) => (
            <span key={index}>
              {index < review.rating ? (
                <AiFillStar color="ffb703" size={15} />
              ) : (
                <AiOutlineStar size={15} />
              )}
            </span>
          ))}
        </Flex>
        <Text>Date: {formattedDate}</Text>
        <Divider />
      </Stack>
    </>
  );
};

export default Review;
