package com.quaider.nanoservice.blockchain.core.bccsp;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.hyperledger.fabric_ca.sdk.HFCAInfo;

import java.io.StringWriter;
import java.security.PrivateKey;
import java.util.Base64;

/**
 * 提供X509证书到字符串的的相互转换便捷工具
 */
public class X509Helper {
    /**
     * 将私钥转换为PEM格式(fabric msp证书基本都是pem格式的文件)
     *
     * @param privateKey 待转换的私钥
     * @return
     * @throws Exception
     */
    public static String privateKeyToPEM(PrivateKey privateKey) throws Exception {
        return bytesToPEM("PRIVATE KEY", privateKey.getEncoded());
    }

    public static String certToPEM(String cert) throws Exception {
        return bytesToPEM("CERTIFICATE", cert.getBytes());
    }

    public static String bytesToPEM(String plainText, byte[] bytes) throws Exception {
        // 这样没有 BEGIN PRIVATE KEY 包裹
        // String privateKeyStr = Base64.encodeBase64String(enrollment.getKey().getEncoded());

        PemObject pemObject = new PemObject(plainText, bytes);
        StringWriter stringWriter = new StringWriter();
        PemWriter pemWriter = new PemWriter(stringWriter);
        pemWriter.writeObject(pemObject);
        pemWriter.close();

        return stringWriter.toString();
    }

    public static String caInfoToPEM(HFCAInfo info) {
        String originalStr = info.getCACertificateChain();
        // 自带了 BEGIN CERTIFICATE END CERTIFICATE
        String pemStr = new String(Base64.getDecoder().decode(originalStr));
        return pemStr;
    }
}
