package com.quaider.nanoservice.blockchain.core.sdk;

/**
 * fabric存储聚合接口，包含一些通用操作
 */
public interface FabricStore {
    /**
     * 根据组织和用户名获取fabric成员(在fabric-ca内部，使用name.org的方式组成enrollmentId)
     *
     * @param name 用户名
     * @param org  组织名
     * @return
     */
    FabricUser getMember(String name, String org);

    /**
     * 判断fabric-ca中是否存在该成员(在fabric-ca内部，使用name.org的方式组成enrollmentId)
     *
     * @param name 用户名
     * @param org  组织名
     * @return
     */
    boolean hasMember(String name, String org);
}
