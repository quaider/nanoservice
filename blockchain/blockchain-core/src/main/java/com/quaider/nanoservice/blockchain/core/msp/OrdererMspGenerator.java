package com.quaider.nanoservice.blockchain.core.msp;

import com.quaider.nanoservice.blockchain.core.bccsp.X509Helper;
import com.quaider.nanoservice.blockchain.core.io.PlainFileManager;
import com.quaider.nanoservice.blockchain.core.sdk.*;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;

import java.net.MalformedURLException;


public final class OrdererMspGenerator extends MspGenerator {

    final FabricOrderer orderer;
    final String orderOrgHome;
    final String ordererHome;
    final String ordererAdminHome;
    final FabricCA ca;
    HFCAClient caClient;

    public OrdererMspGenerator(String cryptoRoot, FabricCA ca, FabricOrderer orderer) {
        super(cryptoRoot);

        if (orderer == null) throw new IllegalArgumentException("orderer 参数不能为空");
        if (orderer.getOrganization() == null) throw new IllegalArgumentException("orderer.organization字段不能为空");
        if (orderer.getOrganization().getCa() == null) throw new IllegalArgumentException("orderer.organization.ca字段不能为空");

        this.orderer = orderer;
        this.ca = orderer.getOrganization().getCa();
        orderOrgHome = String.format("%s/ordererOrganizations/%s", cryptoRoot, orderer.getOrganization().getFullDomainName());
        ordererHome = String.format("%s/orderers/%s", orderOrgHome,orderer.getFullDomainName());
        ordererAdminHome = String.format("%s/users/%s", orderOrgHome, orderer.getOrganization().getAdmin().getName());
    }

    public void generateOrderer() throws Exception {
        initCaClient();
    }

    private void generateOrderMsp() throws Exception {
        generateTls(caClient, orderer.getFullDomainName(), orderer.getEnrollmentSecret());

        Enrollment enrollment = caClient.enroll(orderer.getName(), orderer.getEnrollmentSecret());
        String filename = ordererHome + "msp/signcerts/cert.pem";
        PlainFileManager.saveFile(filename, enrollment.getCert());

        filename = ordererHome + String.format("msp/keystore/%s_sk", orderer.getName());
        PlainFileManager.saveFile(filename, X509Helper.privateKeyToPEM(enrollment.getKey()));

        filename = ordererHome + "msp/cacerts/fabric-ca-server-7054.pem";
        PlainFileManager.saveFile(filename, X509Helper.caInfoToPEM(caClient.info()));

        filename = ordererHome + "msp/tlscacerts/fabric-ca-server-7054.pem";
        PlainFileManager.saveFile(filename, X509Helper.caInfoToPEM(caClient.info()));
    }

    private void initCaClient() throws InvalidArgumentException, MalformedURLException {
        caClient = orderer.getOrganization().getCaClient();
        if (caClient == null) {
            caClient = HFCAClient.createNewInstance(ca.getName(), ca.getLocation(), orderer.getOrganization().getCaProperties());
        }
    }

    public static void main(String[] args) {
        FabricAffiliation affiliation = new FabricAffiliation();
        affiliation.setName("test-affiliation");
        affiliation.setRootDomain("cnabs.com");

        FabricOrg caOrg = new FabricOrg("ca", "");
        caOrg.setAffiliation(affiliation);

        FabricOrg ordererOrg = new FabricOrg("orderer", "OrderMSP");
        ordererOrg.setAffiliation(affiliation);

        FabricCA ca = new FabricCA();
        ca.setOrganization(caOrg);

        FabricUser user = new FabricUser("Admin", ordererOrg);
        ordererOrg.setAdmin(user);

        // root.orderer.cnabs.com
        FabricOrderer o = new FabricOrderer();
        o.setName("root");
        o.setOrganization(ordererOrg);
        o.setLocation("grpcs://" + o.getFullDomainName() + ":7050");

        OrdererMspGenerator g = new OrdererMspGenerator("D:/crypto",ca ,o);
    }
}
