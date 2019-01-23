package com.quaider.nanoservice.blockchain.core.sdk.affiliation;

public class FabricOrg {
    final String name;
    final String mspid;

    public FabricOrg(String name, String mspid) {
        this.name = name;
        this.mspid = mspid;
    }

    public String getName() {
        return name;
    }

    public String getMSPID() {
        return mspid;
    }
}
