/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public final class AssetTest {

    @Nested
    class Equality {

        @Test
        public void isReflexive() {
            Asset asset = new Asset("doc123", "http://example.com/doc123", "Alice", 101);

            assertThat(asset).isEqualTo(asset);
        }

        @Test
        public void isSymmetric() {
            Asset assetA = new Asset("doc123", "http://example.com/doc123", "Alice", 101);
            Asset assetB = new Asset("doc123", "http://example.com/doc123", "Alice", 101);

            assertThat(assetA).isEqualTo(assetB);
            assertThat(assetB).isEqualTo(assetA);
        }

        @Test
        public void isTransitive() {
            Asset assetA = new Asset("doc123", "http://example.com/doc123", "Alice", 101);
            Asset assetB = new Asset("doc123", "http://example.com/doc123", "Alice", 101);
            Asset assetC = new Asset("doc123", "http://example.com/doc123", "Alice", 101);

            assertThat(assetA).isEqualTo(assetB);
            assertThat(assetB).isEqualTo(assetC);
            assertThat(assetA).isEqualTo(assetC);
        }

        @Test
        public void handlesInequality() {
            Asset assetA = new Asset("doc123", "http://example.com/doc123", "Alice", 101);
            Asset assetB = new Asset("doc124", "http://example.com/doc124", "Bob", 102);

            assertThat(assetA).isNotEqualTo(assetB);
        }

        @Test
        public void handlesOtherObjects() {
            Asset assetA = new Asset("doc123", "http://example.com/doc123", "Alice", 101);
            String assetB = "not an asset";

            assertThat(assetA).isNotEqualTo(assetB);
        }

        @Test
        public void handlesNull() {
            Asset asset = new Asset("doc123", "http://example.com/doc123", "Alice", 101);

            assertThat(asset).isNotEqualTo(null);
        }
    }

    // @Test
    // public void toStringIdentifiesAsset() {
    // Asset asset = new Asset("doc123", "http://example.com/doc123", "Alice", 101);

    // assertThat(asset.toString()).isEqualTo("Asset@e04f6c53 [documentID=doc123,
    // documentLink=http://example.com/doc123, owner=Alice, serialNumber=101]");
    // }
}
