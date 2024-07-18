Here is an updated and detailed README file for your repository, incorporating the structure and instructions provided:

---

# Hyperledger Fabric, Document (License Agreement) Distribution

## Overview

This repository contains a full-stack Hyperledger Fabric application designed to distribute license agreements. Each PDF document is represented as an asset on the blockchain and can be managed through the provided API endpoints and frontend interface.

## Repository Structure

```
.
├── backend
│   ├── app
│   │   ├── application-gateway-java
│   │   │   └── App.java
│   │   └── chaincode-java
│   │       ├── Asset.java
│   │       ├── AssetTransfer.java
│   │       └── build.gradle
│   ├── rest-api-go
│   │   ├── go.mod
│   │   ├── main.go
│   │   └── web
│   │       ├── app.go
│   │       ├── initialize.go
│   │       ├── invoke.go
│   │       └── query.go
│   ├── .gitignore
│   └── README.md
├── frontend
│   ├── .next
│   ├── node_modules
│   ├── public
│   ├── src
│   ├── .eslintrc.json
│   ├── .gitignore
│   ├── next-env.d.ts
│   ├── next.config.mjs
│   ├── package-lock.json
│   ├── package.json
│   ├── README.md
│   └── tsconfig.json
├── .github
├── bin
├── builders
├── ci
├── config
├── test-network
│   └── network.sh
├── .editorconfig
├── .gitignore
├── CHANGELOG.md
├── CODE_OF_CONDUCT.md
├── CODEOWNERS
├── CONTRIBUTING.md
├── install-fabric.sh
├── LICENSE
├── MAINTAINERS.md
├── README.md
└── SECURITY.md
```

## Components

### 1. Chaincode (Smart Contracts)

- **Asset.java**: Represents a PDF document as an asset on the blockchain.
- **AssetTransfer.java**: Manages CRUD operations and asset transfers on the blockchain.
- **build.gradle**: Build configuration for the Java chaincode.

### 2. Java Application Gateway

- **App.java**: Manages network connections, authentication, and interactions with the chaincode using gRPC.

### 3. Go REST API Server

- **go.mod**: Module definition and dependencies.
- **main.go**: Initializes the setup for the organization and starts the web server.
- **web**: Contains the logic for interacting with the Hyperledger Fabric network and handling API requests.

### 4. Frontend

- Contains the Next.js and TypeScript-based frontend application that interacts with the Go REST API server.

### 5. Network

- **network.sh**: Script for setting up the Hyperledger Fabric network, including creating channels, deploying chaincode, and managing network components.

## Running the Sample

The Fabric test network is used to deploy and run this sample. Follow these steps in order:

### 1. Create the Test Network and a Channel

Navigate to the `test-network` directory and run the following command:

```bash
cd test-network
./network.sh up createChannel -c mychannel -ca
```

### 2. Deploy the Chaincode

To deploy the **Java** chaincode implementation, run the following command from the `test-network` directory:

```bash
./network.sh deployCC -ccn basic -ccp ../backend/app/chaincode-java/ -ccl java
```

### 3. Run the Java Application Gateway

Navigate to the `application-gateway-java` directory and run the application:

```bash
cd backend/app/application-gateway-java
./gradlew run
```

### 4. Run the Go REST API Server

Navigate to the `rest-api-go` directory, download dependencies, and run the server:

```bash
cd backend/rest-api-go
go mod download
go run main.go
```

### 5. Run the Frontend Application

Navigate to the `frontend` directory, install dependencies, and start the application:

```bash
cd frontend
npm install
npm run dev
```

The frontend should now be accessible at `http://localhost:3001`.

## Usage

### API Endpoints

The Go REST API server exposes the following endpoints:

- **POST /invoke**: Invoke a transaction on the blockchain.
- **GET /query**: Query the blockchain.

### Example Requests

**Invoke a Transaction**:

```bash
curl --request POST \
  --url http://localhost:3000/invoke \
  --header 'content-type: application/x-www-form-urlencoded' \
  --data channelid=mychannel \
  --data chaincodeid=basic \
  --data function=createAsset \
  --data args=Asset123 \
  --data args=yellow \
  --data args=54 \
  --data args=Tom \
  --data args=13005
```

**Query the Blockchain**:

```bash
curl --request GET \
  --url 'http://localhost:3000/query?channelid=mychannel&chaincodeid=basic&function=ReadAsset&args=Asset123'
```

## Architectural Considerations

### Permissioned Blockchain

Hyperledger Fabric is used for its permissioned network capabilities, ensuring only authorized participants can join and transact.

### API Gateway

The Java Application Gateway provides a unified entry point, centralizes security, and enables efficient communication with the Hyperledger Fabric network.

### gRPC vs REST

- **gRPC**: Used for internal communications due to its efficiency, performance benefits with HTTP/2, and strong typing with Protobuf.
- **REST**: Used for external APIs for its flexibility, simplicity, and widespread adoption.

## Contributing

Contributions are welcome! Please submit a pull request or open an issue to discuss potential changes.

## License

This project is licensed under the Apache License 2.0.

---

Feel free to adjust the instructions and details based on the specific needs and structure of your project.
