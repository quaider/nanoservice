package com.quaider.nanoservice.blockchain.core.sdk;

import lombok.Data;
import lombok.Getter;
import org.hyperledger.fabric_ca.sdk.HFCAClient;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

@Data
public class FabricOrg implements FabricDomain, Comparable<FabricOrg> {
    private String name;
    private String mspid;
    private FabricAffiliation affiliation;
    private Set<FabricPeer> peers;
    private FabricUser admin;
    private FabricCA ca;
    private Set<FabricUser> users;
    private Set<FabricOrderer> orderers;
    private Properties caProperties;
    private HFCAClient caClient;

    public FabricOrg(String name, String mspid) {
        this.name = name;
        this.mspid = mspid;
        peers = new HashSet<>();
    }

    @Override
    public String getFullDomainName() {
        return String.format("%s.%s", name, affiliation.getFullDomainName());
    }

    @Override
    public int compareTo(FabricOrg o) {
        return name.compareTo(o.name);
    }

    public void addPeer(FabricPeer peer) {
        peers.add(peer);
    }
}
