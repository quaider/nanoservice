package com.quaider.nanoservice.blockchain.core.sdk;

import lombok.Data;

/**
 * 代表一个联盟
 */
@Data
public class FabricAffiliation implements FabricDomain, Comparable<FabricAffiliation> {
    private String name;
    private String rootDomain;

    @Override
    public String getFullDomainName() {
        return rootDomain;
    }

    @Override
    public int compareTo(FabricAffiliation o) {
        return name.compareTo(o.name);
    }
}
