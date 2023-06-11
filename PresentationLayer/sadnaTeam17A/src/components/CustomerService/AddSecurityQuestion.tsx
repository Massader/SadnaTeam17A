import { Button, Flex, Heading, Input, Text } from "@chakra-ui/react";
import React, { useContext, useState } from "react";
import { ClientCredentialsContext } from "../../App";
import axios from "axios";

interface Props {
  setPage: React.Dispatch<React.SetStateAction<string>>;
  pages: string[];
}

const AddSecurityQuestion = ({ setPage, pages }: Props) => {
  const { clientCredentials } = useContext(ClientCredentialsContext);

  const [question, setQuestion] = useState("");
  const [answer, setAnswer] = useState("");
  const [errorMsg, setErrorMsg] = useState(false);
  const [message, setMessage] = useState("");

  const handleChangePassword = async () => {
    const response = await axios.post(
      "http://localhost:8080/api/v1/users/security/add-question",
      {
        clientCredentials: clientCredentials,
        question: question,
        answer: answer,
      }
    );
    if (!response.data.error) {
      setErrorMsg(false);
      setMessage("Security question saved!");
    } else {
      setErrorMsg(true);
      setMessage(response.data.message);
    }
  };

  return (
    <>
      <Heading padding={5} textAlign="center">
        Add Security Question
      </Heading>
      <Input
        bg="white"
        placeholder="Question"
        value={question}
        onChange={(question) => setQuestion(question.target.value)}
      />
      <Input
        bg="white"
        placeholder="Answer"
        value={answer}
        onChange={(answer) => setAnswer(answer.target.value)}
      />
      <Button colorScheme="blue" size="lg" onClick={handleChangePassword}>
        Set security question
      </Button>
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

export default AddSecurityQuestion;
