package com.quaider.nanoservice.blockchain.core.sdk.affiliation;

import java.util.Collection;
import java.util.HashSet;

public class FabricOrg {

    final String name;
    final String mspid;

    private Collection<FabricUser> peers;

    public FabricOrg(String name, String mspid) {
        this.name = name;
        this.mspid = mspid;
        peers = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public String getMSPID() {
        return mspid;
    }

    public void addPeers(FabricUser user) {
        peers.add(user);
    }

    public Collection<FabricUser> getPeers() {
        return peers;
    }
}
