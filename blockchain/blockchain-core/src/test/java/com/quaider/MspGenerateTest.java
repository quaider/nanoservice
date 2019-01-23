package com.quaider;

import com.google.gson.Gson;
import com.quaider.nanoservice.blockchain.core.sdk.affiliation.FabricOrg;
import com.quaider.nanoservice.blockchain.core.sdk.affiliation.FabricUser;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.*;
import org.hyperledger.fabric_ca.sdk.exception.AffiliationException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.security.PrivateKey;
import java.util.*;

public class MspGenerateTest {

    private HFCAClient client;
    private static CryptoSuite crypto;

    @BeforeClass
    public static void init() throws Exception {
        crypto = CryptoSuite.Factory.getCryptoSuite();
    }

    @Before
    public void initCAClient() throws Exception {
        String caLocation = "https://fabric-ca-server:7054";


        String path = "blockchain/blockchain-core/src/main/resources/ca-cert.pem";
//        String tlsPem = readAllText(path);

        // Properties 用于携带 tls pem 证书
        Properties properties = new Properties();
        properties.put("pemFile", path);
        properties.put("allowAllHostNames", false);

        client = HFCAClient.createNewInstance(caLocation, properties);
        client.setCryptoSuite(crypto);

        Enrollment enrollment = client.enroll("admin", "adminpw");
        FabricOrg org = new FabricOrg("org3.cnabs.com", "Org3MSP");
        FabricUser admin = new FabricUser("admin", null);
        admin.setEnrollment(enrollment);

        HFCAAffiliation resp = client.getHFCAAffiliations(admin);
        printAffiliation(resp);

        HFCACertificateRequest hfcaCertificateRequest = client.newHFCACertificateRequest();
//        hfcaCertificateRequest.setEnrollmentID("admin");
        hfcaCertificateRequest.setRevoked(false);
        hfcaCertificateRequest.setExpired(false);

        //CN=86ac0ba8a848,OU=Fabric,O=Hyperledger,ST=North Carolina,C=US
        HFCACertificateResponse hfcaCertificateResponse = client.getHFCACertificates(admin, hfcaCertificateRequest);
        Collection<HFCACredential> credentials = hfcaCertificateResponse.getCerts();
        for (HFCACredential credential : credentials) {
            HFCAX509Certificate x509Certificate = (HFCAX509Certificate) credential;
            if (x509Certificate == null) continue;
            String name = x509Certificate.getX509().getSubjectX500Principal().getName();
            if(name.indexOf("OU=Fabric,O=Hyperledger") > -1) {
                System.out.println(x509Certificate.getPEM());
                break;
            }
        }

        // 注册联盟(联盟名称是全名)
//        HFCAAffiliation affiliation = client.newHFCAAffiliation("com.cnabs.org3");
//        HFCAAffiliation.HFCAAffiliationResp resp = affiliation.create(admin);
//        System.out.println(resp.getStatusCode());
//        System.out.println(affiliation.getName());

        // 注册 user/peer/orderer
//        RegistrationRequest registrationRequest = new RegistrationRequest("org3.cnabs.com", "com.cnabs.org2");
//        registrationRequest.setType("peer");
//        registrationRequest.setSecret("passwd");
//        String r = client.register(registrationRequest, admin);
//        System.out.println(r);
    }

    @Test
    public void test() {

    }

    private String readAllText(String path) {
        File file = new File(path);
        FileReader fileReader = null;
        BufferedReader reader = null;
        try (InputStream inputStream = new FileInputStream(file)) {
            fileReader = new FileReader(file);
            reader = new BufferedReader(fileReader);
            String line;
            String text = "";
            while ((line = reader.readLine()) != null) {
                text += line;
            }

            return text;
        } catch (Exception ex) {
            throw new RuntimeException(path + "读取失败");
        } finally {
            try {
                if (fileReader != null) fileReader.close();
                if (reader != null) fileReader.close();
            } catch (Exception ex) {

            }
        }
    }

    /**
     * 将私钥转换为PEM格式(fabric msp证书基本都是pem格式的文件)
     *
     * @param privateKey 待转换的私钥
     * @return
     * @throws Exception
     */
    private String privateKeyToPEM(PrivateKey privateKey) throws Exception {
//        这样没有 BEGIN PRIVATE KEY 包裹
//        String privateKeyStr = Base64.encodeBase64String(enrollment.getKey().getEncoded());

        PemObject pemObject = new PemObject("PRIVATE KEY", privateKey.getEncoded());
        StringWriter stringWriter = new StringWriter();
        PemWriter pemWriter = new PemWriter(stringWriter);
        pemWriter.writeObject(pemObject);
        pemWriter.close();

        return stringWriter.toString();
    }

    // 打印联盟层级结构
    private void printAffiliation(HFCAAffiliation affiliation) throws AffiliationException {
        if (affiliation == null) return;
        System.out.println(affiliation.getName());
        Collection<HFCAAffiliation> affiliations = affiliation.getChildren();

        if (affiliations == null || affiliations.size() <= 0) {
            return;
        }

        for (HFCAAffiliation aff : affiliations) {
            printAffiliation(aff);
        }
    }
}
