package com.quaider.nanoservice.blockchain.core.sdk.affiliation;

import org.hyperledger.fabric.sdk.Enrollment;

import java.io.Serializable;
import java.security.PrivateKey;

/**
 * 包含公钥和私钥的登记(是fabric sdk和fabric-ca建立联系的桥梁)
 */
public class FabricEnrollment implements Enrollment, Serializable {

    private static final long serialVersionUID = -2784835212445309006L;
    private final PrivateKey privateKey;
    private final String certificate;

    FabricEnrollment(PrivateKey privateKey, String certificate) {

        this.certificate = certificate;

        this.privateKey = privateKey;
    }

    @Override
    public PrivateKey getKey() {
        return privateKey;
    }

    @Override
    public String getCert() {
        return certificate;
    }
}
