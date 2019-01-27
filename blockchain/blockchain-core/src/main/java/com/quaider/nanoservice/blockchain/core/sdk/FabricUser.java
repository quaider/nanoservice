package com.quaider.nanoservice.blockchain.core.sdk;

import lombok.Data;
import org.hyperledger.fabric.sdk.Enrollment;

import java.io.Serializable;
import java.util.Set;

/**
 * 一个用户代表了fabric-ca中的一个身份
 */
@Data
public class FabricUser implements org.hyperledger.fabric.sdk.User, Comparable<FabricUser>, Serializable {

    private static final long serialVersionUID = 8077132186383604355L;

    private String simpleName;
    private Set<String> roles;
    private String account;
    private String affiliation;
    private FabricOrg organization;
    private Enrollment enrollment;
    private String enrollmentSecret;

    public FabricUser(String simpleName, FabricOrg org) {
        this.simpleName = simpleName;
        this.organization = org;
    }

    @Override
    public String getName() {
        return String.format("%s@%s", simpleName, organization.getFullDomainName());
    }

    /**
     * Get the Membership Service Provider Identifier provided by the user's organization.
     *
     * @return MSP Id.
     */
    @Override
    public String getMspId() {
        return organization.getMspid();
    }

    @Override
    public int compareTo(FabricUser o) {
        return o.getName().compareTo(o.getName());
    }
}
