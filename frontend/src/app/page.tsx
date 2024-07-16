"use client";
import { Box, VStack, Text } from "@chakra-ui/react";
import Navbar from "../components/Navbar";
import DownloadButton from "../components/DownloadButton";

export default function Home() {
  return (
    <Box bg="white" minHeight="100vh" color="black">
      <Navbar />
      <VStack
        spacing={8}
        align="stretch"
        p={8}
        maxWidth="800px"
        margin="0 auto"
      >
        <Text fontSize="3xl" fontWeight="bold" lineHeight="1.2">
          ARTISTS SHOULD
          <br />
          DETERMINE THEIR FUTURE
          <br />
          AND THEIR VALUE, ON
          <br />
          THEIR OWN TERMS.
        </Text>
        <DownloadButton />
      </VStack>
    </Box>
  );
}
