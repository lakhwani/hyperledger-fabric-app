import { Button, Text, VStack } from "@chakra-ui/react";
import { useState, useEffect } from "react";
import axios from "axios";

interface Asset {
  appraisedValue: number;
  assetID: string;
  color: string;
  owner: string;
  size: number;
}

export default function DownloadButton() {
  const [asset, setAsset] = useState<Asset | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchAsset();
  }, []);

  const fetchAsset = async () => {
    try {
      setIsLoading(true);
      const response = await axios.get<Asset[]>(
        "http://localhost:3000/query?channelid=mychannel&chaincodeid=basic&function=GetAllAssets"
      );
      if (response.data && response.data.length > 0) {
        console.log(response.data);
        setAsset(response.data[0]);
      } else {
        throw new Error("No assets found");
      }
    } catch (error) {
      console.error("Error fetching asset:", error);
      setError(axios.isAxiosError(error) ? error.message : "An error occurred");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <VStack align="flex-start" spacing={4}>
      <Text fontSize="sm">
        CLICK THE DOWNLOAD BUTTON BELOW TO INSTALL
        <br />A COPY OF YOUR LICENSE AGREEMENT.
      </Text>
      {isLoading ? (
        <Button
          isLoading
          loadingText="Loading Agreement"
          bg="black"
          color="white"
          _hover={{ bg: "gray.800" }}
        >
          Loading Agreement
        </Button>
      ) : error ? (
        <Button
          bg="red.500"
          color="white"
          _hover={{ bg: "red.600" }}
          onClick={() => fetchAsset()}
        >
          Error: Click to retry
        </Button>
      ) : asset ? (
        <Button
          as="a"
          href="#" // You may want to add a proper link here
          download
          bg="black"
          color="white"
          _hover={{ bg: "gray.800" }}
        >
          {asset.owner}
        </Button>
      ) : (
        <Button
          bg="gray.300"
          color="gray.800"
          _hover={{ bg: "gray.400" }}
          isDisabled
        >
          No agreement available
        </Button>
      )}
    </VStack>
  );
}
