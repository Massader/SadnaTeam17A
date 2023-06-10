import {
  Card,
  CardBody,
  CardFooter,
  Divider,
  Flex,
  Heading,
  Image,
  Stack,
  Text,
} from "@chakra-ui/react";
import founderIcon from "../../assets/founder.png";
import managerIcon from "../../assets/manager.png";
import ownerIcon from "../../assets/owner.png";

interface Props {
  username: string;
  roles: string[];
}

const PositionInfoCard = ({ username, roles }: Props) => {
  const formatRole = (role: string) => {
    const word = role.replace(/_/g, " ");
    return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase();
  };

  return (
    <Card maxW="sm">
      <CardBody>
        <Flex justifyContent="center">
          {roles.includes("STORE_FOUNDER") ? (
            <Image src={founderIcon} borderRadius="lg" />
          ) : roles.includes("STORE_OWNER") ? (
            <Image src={ownerIcon} borderRadius="lg" />
          ) : (
            <Image src={managerIcon} borderRadius="lg" />
          )}
        </Flex>
        <Stack mt="6" spacing="3">
          <Heading size="md">{username}</Heading>
        </Stack>
      </CardBody>
      <Divider />
      <CardFooter>
        <Stack px={4}>
          <ul>
            {roles.map((role, index) => (
              <li key={index}>{formatRole(role)}</li>
            ))}
          </ul>
        </Stack>
      </CardFooter>
    </Card>
  );
};

export default PositionInfoCard;
