package com.quaider.nanoservice.blockchain.core.sdk.affiliation;

import org.hyperledger.fabric.sdk.Enrollment;

import java.io.Serializable;
import java.util.Set;

/**
 * 一个用户代表了fabric-ca中的一个身份
 */
public class FabricUser implements org.hyperledger.fabric.sdk.User, Serializable {

    private static final long serialVersionUID = 8077132186383604355L;

    private String name;
    private Set<String> roles;
    private String account;
    private String affiliation;
    private FabricOrg organization;
    private Enrollment enrollment;

    public FabricUser(String name, FabricOrg org) {
        this.name = name;
        this.organization = org;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    @Override
    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    @Override
    public String getMspId() {
        return this.organization.getMSPID();
    }

    public void setOrganization(FabricOrg organization) {
        this.organization = organization;
    }
}
