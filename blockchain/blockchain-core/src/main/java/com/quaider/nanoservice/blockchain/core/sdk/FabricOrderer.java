package com.quaider.nanoservice.blockchain.core.sdk;

import lombok.Data;

@Data
public class FabricOrderer extends FabricNode {
    private String enrollmentSecret;
}
