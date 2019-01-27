package com.quaider.nanoservice.blockchain.core.sdk;

import lombok.Data;

@Data
public abstract class FabricNode implements FabricDomain, Comparable<FabricNode> {
    private String name;
    /**
     * grpc location
     */
    private String location;
    private FabricOrg organization;

    @Override
    public String getFullDomainName() {
        return String.format("%s.%s", name, organization.getFullDomainName());
    }

    @Override
    public int compareTo(FabricNode o) {
        return name.compareTo(o.name);
    }
}
