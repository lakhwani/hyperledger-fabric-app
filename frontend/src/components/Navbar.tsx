import { Flex, Box, Text } from "@chakra-ui/react";
import Logo from "./Logo";

export default function Navbar() {
  return (
    <Flex
      as="nav"
      align="center"
      justify="space-between"
      wrap="wrap"
      padding="1rem"
      borderBottom="1px solid"
      borderColor="gray.200"
    >
      <Logo />
      <Box>
        <Text>ARTISTS</Text>
      </Box>
    </Flex>
  );
}
