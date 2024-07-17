/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public final class AssetTransferTest {

    private static final class MockKeyValue implements KeyValue {

        private final String key;
        private final String value;

        MockKeyValue(final String key, final String value) {
            super();
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public String getStringValue() {
            return this.value;
        }

        @Override
        public byte[] getValue() {
            return this.value.getBytes();
        }

    }

    private static final class MockAssetResultsIterator implements QueryResultsIterator<KeyValue> {

        private final List<KeyValue> assetList;

        MockAssetResultsIterator() {
            super();

            assetList = new ArrayList<KeyValue>();

            assetList.add(new MockKeyValue("doc123",
                    "{ \"documentID\": \"doc123\", \"documentLink\": \"https://images-template-net.webpkgcache.com/doc/-/s/images.template.net/wp-content/uploads/2022/06/Menu-PDF.pdf\", \"owner\": \"Alice\", \"serialNumber\": 101 }"));
            assetList.add(new MockKeyValue("doc124",
                    "{ \"documentID\": \"doc124\", \"documentLink\": \"https://images-template-net.webpkgcache.com/doc/-/s/images.template.net/wp-content/uploads/2022/06/Menu-PDF.pdf\", \"owner\": \"Bob\", \"serialNumber\": 102 }"));
            assetList.add(new MockKeyValue("doc125",
                    "{ \"documentID\": \"doc125\", \"documentLink\": \"https://images-template-net.webpkgcache.com/doc/-/s/images.template.net/wp-content/uploads/2022/06/Menu-PDF.pdf\", \"owner\": \"Charlie\", \"serialNumber\": 103 }"));
        }

        @Override
        public Iterator<KeyValue> iterator() {
            return assetList.iterator();
        }

        @Override
        public void close() throws Exception {
            // do nothing
        }

    }

    @Test
    public void invokeUnknownTransaction() {
        AssetTransfer contract = new AssetTransfer();
        Context ctx = mock(Context.class);

        Throwable thrown = catchThrowable(() -> {
            contract.unknownTransaction(ctx);
        });

        assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                .hasMessage("Undefined contract method called");
        assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo(null);

        verifyNoInteractions(ctx);
    }

    @Nested
    class InvokeReadAssetTransaction {

        @Test
        public void whenAssetExists() {
            AssetTransfer contract = new AssetTransfer();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("doc123"))
                    .thenReturn(
                            "{ \"documentID\": \"doc123\", \"documentLink\": \"https://images-template-net.webpkgcache.com/doc/-/s/images.template.net/wp-content/uploads/2022/06/Menu-PDF.pdf\", \"owner\": \"Alice\", \"serialNumber\": 101 }");

            Asset asset = contract.ReadAsset(ctx, "doc123");

            assertThat(asset).isEqualTo(new Asset("doc123",
                    "https://images-template-net.webpkgcache.com/doc/-/s/images.template.net/wp-content/uploads/2022/06/Menu-PDF.pdf",
                    "Alice", 101));
        }

        @Test
        public void whenAssetDoesNotExist() {
            AssetTransfer contract = new AssetTransfer();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("doc123")).thenReturn("");

            Throwable thrown = catchThrowable(() -> {
                contract.ReadAsset(ctx, "doc123");
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Asset doc123 does not exist");
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("ASSET_NOT_FOUND".getBytes());
        }
    }

    @Test
    void invokeInitLedgerTransaction() {
        AssetTransfer contract = new AssetTransfer();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);

        contract.InitLedger(ctx);

        InOrder inOrder = inOrder(stub);
        inOrder.verify(stub).putStringState("doc123",
                "{\"documentID\":\"doc123\",\"documentLink\":\"https://images-template-net.webpkgcache.com/doc/-/s/images.template.net/wp-content/uploads/2022/06/Menu-PDF.pdf\",\"owner\":\"Alice\",\"serialNumber\":101}");
        inOrder.verify(stub).putStringState("doc124",
                "{\"documentID\":\"doc124\",\"documentLink\":\"https://images-template-net.webpkgcache.com/doc/-/s/images.template.net/wp-content/uploads/2022/06/Menu-PDF.pdf\",\"owner\":\"Bob\",\"serialNumber\":102}");
        inOrder.verify(stub).putStringState("doc125",
                "{\"documentID\":\"doc125\",\"documentLink\":\"https://images-template-net.webpkgcache.com/doc/-/s/images.template.net/wp-content/uploads/2022/06/Menu-PDF.pdf\",\"owner\":\"Charlie\",\"serialNumber\":103}");
    }

    @Nested
    class InvokeCreateAssetTransaction {

        @Test
        public void whenAssetExists() {
            AssetTransfer contract = new AssetTransfer();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("doc123"))
                    .thenReturn(
                            "{ \"documentID\": \"doc123\", \"documentLink\": \"https://images-template-net.webpkgcache.com/doc/-/s/images.template.net/wp-content/uploads/2022/06/Menu-PDF.pdf\", \"owner\": \"Alice\", \"serialNumber\": 101 }");

            Throwable thrown = catchThrowable(() -> {
                contract.CreateAsset(ctx, "doc123",
                        "https://images-template-net.webpkgcache.com/doc/-/s/images.template.net/wp-content/uploads/2022/06/Menu-PDF.pdf",
                        "Alice", 101);
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Asset doc123 already exists");
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("ASSET_ALREADY_EXISTS".getBytes());
        }

        @Test
        public void whenAssetDoesNotExist() {
            AssetTransfer contract = new AssetTransfer();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("doc123")).thenReturn("");

            Asset asset = contract.CreateAsset(ctx, "doc123",
                    "https://images-template-net.webpkgcache.com/doc/-/s/images.template.net/wp-content/uploads/2022/06/Menu-PDF.pdf",
                    "Alice", 101);

            assertThat(asset).isEqualTo(new Asset("doc123",
                    "https://images-template-net.webpkgcache.com/doc/-/s/images.template.net/wp-content/uploads/2022/06/Menu-PDF.pdf",
                    "Alice", 101));
        }
    }

    @Test
    void invokeGetAllAssetsTransaction() {
        AssetTransfer contract = new AssetTransfer();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);
        when(stub.getStateByRange("", "")).thenReturn(new MockAssetResultsIterator());

        String assets = contract.GetAllAssets(ctx);

        assertThat(assets).isEqualTo(
                "[{\"documentID\":\"doc123\",\"documentLink\":\"https://images-template-net.webpkgcache.com/doc/-/s/images.template.net/wp-content/uploads/2022/06/Menu-PDF.pdf\",\"owner\":\"Alice\",\"serialNumber\":101},"
                        + "{\"documentID\":\"doc124\",\"documentLink\":\"https://images-template-net.webpkgcache.com/doc/-/s/images.template.net/wp-content/uploads/2022/06/Menu-PDF.pdf\",\"owner\":\"Bob\",\"serialNumber\":102},"
                        + "{\"documentID\":\"doc125\",\"documentLink\":\"https://images-template-net.webpkgcache.com/doc/-/s/images.template.net/wp-content/uploads/2022/06/Menu-PDF.pdf\",\"owner\":\"Charlie\",\"serialNumber\":103}]");

    }

    @Nested
    class TransferAssetTransaction {

        @Test
        public void whenAssetExists() {
            AssetTransfer contract = new AssetTransfer();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("doc123"))
                    .thenReturn(
                            "{ \"documentID\": \"doc123\", \"documentLink\": \"https://images-template-net.webpkgcache.com/doc/-/s/images.template.net/wp-content/uploads/2022/06/Menu-PDF.pdf\", \"owner\": \"Alice\", \"serialNumber\": 101 }");

            String oldOwner = contract.TransferAsset(ctx, "doc123", "Bob");

            assertThat(oldOwner).isEqualTo("Alice");
        }

        @Test
        public void whenAssetDoesNotExist() {
            AssetTransfer contract = new AssetTransfer();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("doc123")).thenReturn("");

            Throwable thrown = catchThrowable(() -> {
                contract.TransferAsset(ctx, "doc123", "Bob");
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Asset doc123 does not exist");
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("ASSET_NOT_FOUND".getBytes());
        }
    }

    @Nested
    class UpdateAssetTransaction {

        @Test
        public void whenAssetExists() {
            AssetTransfer contract = new AssetTransfer();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("doc123"))
                    .thenReturn(
                            "{ \"documentID\": \"doc123\", \"documentLink\": \"https://images-template-net.webpkgcache.com/doc/-/s/images.template.net/wp-content/uploads/2022/06/Menu-PDF.pdf\", \"owner\": \"Alice\", \"serialNumber\": 101 }");

            Asset asset = contract.UpdateAsset(ctx, "doc123",
                    "https://images-template-net.webpkgcache.com/doc/-/s/images.template.net/wp-content/uploads/2022/06/Menu-PDF-updated.pdf",
                    "Alice", 101);

            assertThat(asset).isEqualTo(new Asset("doc123",
                    "https://images-template-net.webpkgcache.com/doc/-/s/images.template.net/wp-content/uploads/2022/06/Menu-PDF-updated.pdf",
                    "Alice", 101));
        }

        @Test
        public void whenAssetDoesNotExist() {
            AssetTransfer contract = new AssetTransfer();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("doc123")).thenReturn("");

            Throwable thrown = catchThrowable(() -> {
                contract.UpdateAsset(ctx, "doc123",
                        "https://images-template-net.webpkgcache.com/doc/-/s/images.template.net/wp-content/uploads/2022/06/Menu-PDF-updated.pdf",
                        "Alice", 101);
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Asset doc123 does not exist");
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("ASSET_NOT_FOUND".getBytes());
        }
    }

    @Nested
    class DeleteAssetTransaction {

        @Test
        public void whenAssetDoesNotExist() {
            AssetTransfer contract = new AssetTransfer();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("doc123")).thenReturn("");

            Throwable thrown = catchThrowable(() -> {
                contract.DeleteAsset(ctx, "doc123");
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Asset doc123 does not exist");
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("ASSET_NOT_FOUND".getBytes());
        }
    }
}
