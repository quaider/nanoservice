package com.quaider.nanoservice.blockchain.config;

import com.quaider.nanoservice.blockchain.core.sdk.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TestConfigProvider {

    static TestConfigProvider instance;

    Set<FabricAffiliation> affiliations;
    Set<FabricOrg> orgs;
    FabricOrderer orderer;
    FabricCA ca;

    private TestConfigProvider() {
        affiliations = new HashSet<>();
        orgs = new HashSet<>();
        loadConfig();
    }

    static {
        instance = new TestConfigProvider();
    }

    public static TestConfigProvider getInstance() {
        return instance;
    }

    public Set<FabricOrg> getOrgs() {
        return orgs;
    }

    public FabricOrderer getOrderer() {
        return orderer;
    }

    public FabricCA getCa() {
        return ca;
    }

    public void loadConfig() {
        FabricAffiliation affiliation = new FabricAffiliation();
        affiliation.setName("test-affiliation");
        affiliation.setRootDomain("cnabs.com");

        FabricOrg caOrg = new FabricOrg("ca", "");
        caOrg.setAffiliation(affiliation);
        // root.ca.cnabs.com
        ca = new FabricCA();
        ca.setName("root");
        ca.setRootCA(true);
        ca.setOrganization(caOrg);
        ca.setLocation("https://" + ca.getFullDomainName() + ":7054");
        FabricUser caAdmin = new FabricUser("admin", caOrg);
        caAdmin.setEnrollmentSecret("adminpw");
        ca.setBootstrap(caAdmin);

        FabricOrg ordererOrg = new FabricOrg("orderer", "OrderMSP");
        ordererOrg.setAffiliation(affiliation);
        // root.orderer.cnabs.com
        orderer = new FabricOrderer();
        orderer.setName("root");
        orderer.setOrganization(ordererOrg);
        orderer.setLocation("grpcs://" + orderer.getFullDomainName() + ":7050");

        Map<String, String> orgNames = new HashMap<>();
        orgNames.put("org1", "Org1MSP");
        orgNames.put("org2", "Org2MSP");

        String[] peerNames = new String[] {"peer0", "peer1"};

        for(Map.Entry<String, String> orgName: orgNames.entrySet()) {
            FabricOrg org = new FabricOrg(orgName.getKey(), orgName.getValue());
            org.setAffiliation(affiliation);

            for (String peerName: peerNames) {
                FabricPeer peer = new FabricPeer();
                peer.setName(peerName);
                peer.setOrganization(org);
                peer.setLocation("grpcs://" + peer.getFullDomainName() + ":7051");
                org.addPeer(peer);
            }

            FabricUser orgAdmin = new FabricUser("Admin", org);
            orgAdmin.setEnrollmentSecret("passwd");
            org.setAdmin(orgAdmin);
            orgs.add(org);
        }

        affiliations.add(affiliation);

    }
}
