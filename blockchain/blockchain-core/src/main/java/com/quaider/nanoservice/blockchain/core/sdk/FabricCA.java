package com.quaider.nanoservice.blockchain.core.sdk;

import lombok.Data;

@Data
public class FabricCA extends FabricNode {
    private FabricUser bootstrap;
    private boolean isRootCA;
    private FabricCA parent;
}
