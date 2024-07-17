/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class Asset {

    @Property()
    private final String documentID;

    @Property()
    private final String documentLink;

    @Property()
    private final String owner;

    @Property()
    private final int serialNumber;

    public String getDocumentID() {
        return documentID;
    }

    public String getDocumentLink() {
        return documentLink;
    }

    public String getOwner() {
        return owner;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public Asset(@JsonProperty("documentID") final String documentID,
            @JsonProperty("documentLink") final String documentLink,
            @JsonProperty("owner") final String owner, @JsonProperty("serialNumber") final int serialNumber) {
        this.documentID = documentID;
        this.documentLink = documentLink;
        this.owner = owner;
        this.serialNumber = serialNumber;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        Asset other = (Asset) obj;

        return Objects.deepEquals(
                new String[] {
                        getDocumentID(), getDocumentLink(), getOwner()
                },
                new String[] {
                        other.getDocumentID(), other.getDocumentLink(), other.getOwner()
                })
                &&
                Objects.deepEquals(
                        new int[] {
                                getSerialNumber()
                        },
                        new int[] {
                                other.getSerialNumber()
                        });
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDocumentID(), getDocumentLink(), getOwner(), getSerialNumber());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [documentID=" + documentID
                + ", documentLink="
                + documentLink + ", owner=" + owner + ", serialNumber=" + serialNumber + "]";
    }
}
