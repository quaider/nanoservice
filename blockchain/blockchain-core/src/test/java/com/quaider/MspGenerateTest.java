package com.quaider;

import com.quaider.nanoservice.blockchain.core.sdk.FabricOrg;
import com.quaider.nanoservice.blockchain.core.sdk.FabricUser;
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
    private static CryptoSuite cryptoSuite;

    private final String CRYPTO_ROOT = "D:/crypto/";
    private static FabricOrg ordererOrg;
    private List<FabricOrg> peerOrgs = Arrays.asList();
    private static String DOMAIN = "cnabs.com";

    private HFCAInfo caInfo;

    // 随环境变化
    private String orgMspId = "";
    private String orgHome = "";
    private String orgAdminHome = "";
    private String peerHome = "";
    private String filename = "";
    private String orgHost = "";
    private String peerHost = "";
    private FabricUser orgAdmin = null;


    @BeforeClass
    public static void init() throws Exception {
        cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
        ordererOrg = new FabricOrg("orderer", "OrdererMSP");
    }

    @Before
    public void initCAClient() throws Exception {
        FabricOrg org1 = new FabricOrg("org1", "Org1MSP");
        FabricOrg org2 = new FabricOrg("org2", "Org2MSP");

        org1.addPeer(new FabricUser("peer0.org1.cnabs.com", org1).setEnrollmentSecret("passwd"));
        org1.addPeer(new FabricUser("peer1.org1.cnabs.com", org1).setEnrollmentSecret("passwd"));

        org2.addPeer(new FabricUser("peer0.org2.cnabs.com", org2).setEnrollmentSecret("passwd"));
        org2.addPeer(new FabricUser("peer1.org2.cnabs.com", org2).setEnrollmentSecret("passwd"));

        peerOrgs = Arrays.asList(org1, org2);

        String caLocation = "https://fabric-ca-server:7054";
        String path = "src/main/resources/ca-cert-comp.pem";

        // Properties 用于携带 tls pem 证书
        Properties properties = new Properties();
        properties.put("pemFile", path);
        properties.put("allowAllHostNames", false);

        client = HFCAClient.createNewInstance(caLocation, properties);
        client.setCryptoSuite(cryptoSuite);

        caInfo = client.info();
    }


    @Test
    public void genOrderer() throws Exception {
        initOrdererVars();
        genOrdererCA();
        genOrdererMsp();
        genOrdererAdmin();
    }

    private void initOrdererVars() throws Exception {
        orgMspId = ordererOrg.getMspid();
        orgHome = CRYPTO_ROOT + "ordererOrganizations/cnabs.com/";
        peerHome = orgHome + "orderers/orderer.cnabs.com/";
        orgAdminHome = orgHome + "/users/Admin@cnabs.com/";
    }

    private void genOrdererCA() throws Exception {
        // cnabs.com组织的 cacerts
        filename = orgHome + "msp/cacerts/fabric-ca-server-7054.pem";
        saveFile(filename, caInfoToPEM(caInfo));

        // cnabs.com组织的 tlscerts(同cacerts)
        filename = orgHome + "msp/tlscacerts/fabric-ca-server-7054.pem";
        saveFile(filename, caInfoToPEM(caInfo));
    }

    private void genOrdererMsp() throws Exception {
        FabricUser orderer = new FabricUser("orderer.cnabs.com", ordererOrg);
        orderer.setEnrollmentSecret("passwd");

        EnrollmentRequest request = new EnrollmentRequest();
        request.setProfile("tls");

        Enrollment enrollment = client.enroll(orderer.getName(), orderer.getEnrollmentSecret(), request);
        filename = peerHome + "/tls/server.key";
        saveFile(filename, privateKeyToPEM(enrollment.getKey()));

        filename = peerHome + "/tls/server.crt";
        saveFile(filename, enrollment.getCert());

        enrollment = client.enroll(orderer.getName(), orderer.getEnrollmentSecret());

        filename = peerHome + "msp/signcerts/cert.pem";
        saveFile(filename, enrollment.getCert());

        filename = peerHome + String.format("msp/keystore/%s_sk", orderer.getName());
        saveFile(filename, privateKeyToPEM(enrollment.getKey()));

        filename = peerHome + "msp/cacerts/fabric-ca-server-7054.pem";
        saveFile(filename, caInfoToPEM(caInfo));

        filename = peerHome + "msp/tlscacerts/fabric-ca-server-7054.pem";
        saveFile(filename, caInfoToPEM(caInfo));
    }

    private void genOrdererAdmin() throws Exception {
        FabricUser ordererAdmin = new FabricUser("Admin@cnabs.com", ordererOrg);
        ordererAdmin.setEnrollmentSecret("passwd");
        Enrollment enrollment = client.enroll(ordererAdmin.getName(), ordererAdmin.getEnrollmentSecret());

        // cacert
        filename = orgAdminHome + "msp/cacerts/fabric-ca-server-7054.pem";
        saveFile(filename, caInfoToPEM(caInfo));

        // keystore
        filename = orgAdminHome + "msp/keystore/orderer_admin_sk";
        saveFile(filename, privateKeyToPEM(enrollment.getKey()));

        // signcert
        filename = orgAdminHome + "msp/signcerts/cert.pem";
        saveFile(filename, enrollment.getCert());

        // signcert to org admincerts
        filename = orgHome + "msp/admincerts/cert.pem";
        saveFile(filename, enrollment.getCert());

        // admin's signcerts to admincerts
        filename = orgAdminHome + "msp/admincerts/cert.pem";
        saveFile(filename, enrollment.getCert());

        filename = peerHome + "msp/admincerts/cert.pem";
        saveFile(filename, enrollment.getCert());
    }

    @Test
    public void genPeers() throws Exception {
        for (FabricOrg org : peerOrgs) {
            initOrgVars(org);
            genOrgCA(org);
            genOrgAdmin(org);

            for (FabricUser peer : org.getPeers()) {
                initPeerVars(peer);
                genOrgPeerMSP(peer);
            }
        }
    }

    private void initOrgVars(FabricOrg org) {
        orgMspId = org.getMSPID();
        // org1.cnabs.com
        orgHost = org.getName() + "." + DOMAIN;
        orgAdmin = new FabricUser("Admin@" + orgHost, org).setEnrollmentSecret("passwd");
        // crypto/peerOrganizations/org1.cnabs.com/
        orgHome = CRYPTO_ROOT + "peerOrganizations/" + orgHost + "/";
        orgAdminHome = orgHome + "users/" + orgAdmin.getName() + "/";
    }

    private void initPeerVars(FabricUser peer) {
        peerHost = peer.getName();
        peerHome = orgHome + "peers/" + peerHost + "/";
    }

    private void genOrgCA(FabricOrg org) throws Exception {
        filename = orgHome + "msp/cacerts/fabric-ca-server-7054.pem";
        saveFile(filename, caInfoToPEM(caInfo));

        filename = orgHome + "msp/tlscacerts/fabric-ca-server-7054.pem";
        saveFile(filename, caInfoToPEM(caInfo));
    }

    private void genOrgAdmin(FabricOrg org) throws Exception {
        Enrollment enrollment = client.enroll(orgAdmin.getName(), orgAdmin.getEnrollmentSecret());

        filename = orgAdminHome + "msp/cacerts/cert.pem";
        saveFile(filename, caInfoToPEM(caInfo));

        filename = orgAdminHome + String.format("msp/keystore/%s_admin_sk", org.getName());
        saveFile(filename, privateKeyToPEM(enrollment.getKey()));

        filename = orgAdminHome + "msp/signcerts/cert.pem";
        saveFile(filename, enrollment.getCert());

        filename = orgHome + "msp/admincerts/cert.pem";
        saveFile(filename, enrollment.getCert());

        filename = orgAdminHome + "msp/admincerts/cert.pem";
        saveFile(filename, enrollment.getCert());
    }

    private EnrollmentRequest getTlsRequest() {
        EnrollmentRequest request = new EnrollmentRequest();
        request.addHost(peerHost);
        request.setProfile("tls");
        return request;
    }

    private void genOrgPeerMSP(FabricUser peer) throws Exception {

        EnrollmentRequest request = getTlsRequest();

        Enrollment enrollment = client.enroll(peer.getName(), peer.getEnrollmentSecret(), request);

        filename = peerHome + "tls/server.key";
        saveFile(filename, privateKeyToPEM(enrollment.getKey()));

        filename = peerHome + "tls/server.crt";
        saveFile(filename, enrollment.getCert());

        request = getTlsRequest();
        enrollment = client.enroll(peer.getName(), peer.getEnrollmentSecret(), request);

        filename = peerHome + "tls/" + getKeyName(peer.getName(), "client.key");
        saveFile(filename, privateKeyToPEM(enrollment.getKey()));

        filename = peerHome + "tls/" + getKeyName(peer.getName(), "client.crt");
        saveFile(filename, enrollment.getCert());

        request = getTlsRequest();
        enrollment = client.enroll(peer.getName(), peer.getEnrollmentSecret(), request);

        filename = peerHome + "tls/" + getKeyName(peer.getName(), "cli-client.key");
        saveFile(filename, privateKeyToPEM(enrollment.getKey()));

        filename = peerHome + "tls/" + getKeyName(peer.getName(), "cli-client.crt");
        saveFile(filename, enrollment.getCert());


        enrollment = client.enroll(peer.getName(), peer.getEnrollmentSecret());

        filename = peerHome + String.format("msp/keystore/%s_sk", peer.getName());
        saveFile(filename, privateKeyToPEM(enrollment.getKey()));

        filename = peerHome + "msp/signcerts/cert.pem";
        saveFile(filename, enrollment.getCert());

        filename = peerHome + "msp/cacerts/fabric-ca-server-7054.pem";
        saveFile(filename, caInfoToPEM(caInfo));

        filename = peerHome + "msp/tlscacerts/fabric-ca-server-7054.pem";
        saveFile(filename, caInfoToPEM(caInfo));

        // 复制组织的admincerts
        filename = peerHome + "msp/admincerts/cert.pem";
        String orgAdminCert = orgHome + "msp/admincerts/cert.pem";
        saveFile(filename, readAllText(orgAdminCert));
    }

    private String getKeyName(String peerName, String suffix) {
        String name = peerName.replace("." + DOMAIN, "").replace(".", "-");
        return name + "-" + suffix;
    }

    @Test
    public void test() {

    }

    private String readAllText(String path) {
        File file = new File(path);
        try (FileReader fileReader = new FileReader(file);
             BufferedReader reader = new BufferedReader(fileReader);
             StringWriter sw = new StringWriter()) {

            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                if (i > 0)
                    sw.write("\n");
                sw.write(line);
                i++;
            }

            return sw.toString();
        } catch (Exception ex) {
            throw new RuntimeException(path + "读取失败");
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

        return bytesToPEM("PRIVATE KEY", privateKey.getEncoded());
    }

    private String certToPEM(String cert) throws Exception {
        return bytesToPEM("CERTIFICATE", cert.getBytes());
    }

    private String bytesToPEM(String plainText, byte[] bytes) throws Exception {
        // 这样没有 BEGIN PRIVATE KEY 包裹
        // String privateKeyStr = Base64.encodeBase64String(enrollment.getKey().getEncoded());

        PemObject pemObject = new PemObject(plainText, bytes);
        StringWriter stringWriter = new StringWriter();
        PemWriter pemWriter = new PemWriter(stringWriter);
        pemWriter.writeObject(pemObject);
        pemWriter.close();

        return stringWriter.toString();
    }

    private void saveFile(String fileName, String content) {

        File file = new File(fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (FileWriter fileWriter = new FileWriter(file);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {

            writer.write(content);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    private String caInfoToPEM(HFCAInfo info) {
        String originalStr = info.getCACertificateChain();
        // 自带了 BEGIN CERTIFICATE END CERTIFICATE
        String pemStr = new String(Base64.getDecoder().decode(originalStr));
        return pemStr;
    }

    // 打印联盟层级结构
    private void printAffiliation(HFCAAffiliation affiliation) throws AffiliationException {
        if (affiliation == null) return;
        System.out.println(affiliation.getName());
        Collection<HFCAAffiliation> affiliations = affiliation.getChildren();

        if (affiliations == null || affiliations.size() <= 0)  {
            return;
        }

        for (HFCAAffiliation aff : affiliations) {
            printAffiliation(aff);
        }
    }
}
