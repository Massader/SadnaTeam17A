import { Button, Flex, Heading, Stack } from "@chakra-ui/react";
import { ClientCredentialsContext } from "../../App";
import React, { useContext, useEffect, useState } from "react";
import axios from "axios";
import { ComplaintType } from "../../types";
import ComplaintCard from "./ComplaintCard";

interface Props {
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
}

const Complaints = ({ setPage, pages }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const fetchComplaints = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/stores/admin/get-complaints/id=${clientCredentials}`
    );
    if (!response.data.error) {
      setComplaints(response.data.value);
    } else {
      console.log(response.data.error);
    }
  };

  const [complaints, setComplaints] = useState<ComplaintType[]>([]);

  useEffect(() => {
    fetchComplaints();
  }, []);

  return (
    <>
      <Stack w="70%">
        <Heading padding={5} textAlign="center">
          Complaints
        </Heading>
        <Button
          colorScheme="blackAlpha"
          size="lg"
          onClick={() => setPage(pages[0])}
        >
          Back
        </Button>
        <Stack>
          {complaints.reverse().map((complaint) => (
            <ComplaintCard
              fetchComplaints={fetchComplaints}
              complaint={complaint}
              key={complaint.id}
            />
          ))}
        </Stack>
      </Stack>
    </>
  );
};

export default Complaints;
