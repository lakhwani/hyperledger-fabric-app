`use client`;
import { Box, Button, Flex, Image, Text, Link } from "@chakra-ui/react";
import { useState, useEffect } from "react";

interface Asset {
  pdfID: string;
  uuid: number;
  link: string;
}

export default function Header() {
  const [asset, setAsset] = useState<Asset | null>(null);

  useEffect(() => {
    fetchAsset();
  }, []);

  const fetchAsset = async () => {
    try {
      const response = await fetch(
        "http://localhost:3000/query?channelid=mychannel&chaincodeid=basic&function=GetAllAssets"
      );
      const data = await response.json();
      if (data && data.length > 0) {
        setAsset(data[0]);
      }
    } catch (error) {
      console.error("Error fetching asset:", error);
    }
  };

  return (
    <Flex
      as="header"
      align="center"
      justify="space-between"
      wrap="wrap"
      padding="1.5rem"
      bg="black"
      color="white"
    >
      <Flex align="center" mr={5}>
        <Image src="/path-to-your-logo.png" alt="Onlock Logo" height="40px" />
      </Flex>

      <Box>
        {asset && (
          <Link href={asset.link} isExternal>
            <Button bg="white" color="black" _hover={{ bg: "gray.200" }}>
              Download Active License Agreement
            </Button>
          </Link>
        )}
        <Text fontSize="xs" mt={2}>
          By clicking the download button, a .PDF copy of my license agreement
          will download to my computer.
        </Text>
      </Box>
    </Flex>
  );
}
