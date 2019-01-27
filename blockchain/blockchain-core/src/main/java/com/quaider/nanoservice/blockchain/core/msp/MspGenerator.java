package com.quaider.nanoservice.blockchain.core.msp;

import com.quaider.nanoservice.blockchain.core.bccsp.X509Helper;
import com.quaider.nanoservice.blockchain.core.io.PlainFileManager;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;

public abstract class MspGenerator {

    final String cryptoRoot;

    public MspGenerator(String cryptoRoot) {
        this.cryptoRoot = cryptoRoot;
    }

    protected abstract String getOrgHome();

    protected String getTlsKeyName() {
        return "server";
    }

    protected void generateTls(HFCAClient client, String name, String secret) throws Exception {
        EnrollmentRequest request = new EnrollmentRequest();
        request.setProfile("tls");
        Enrollment enrollment = client.enroll(name, secret, request);

        String filename = String.format("%s/tls/%s.key",getOrgHome(), getTlsKeyName());
        PlainFileManager.saveFile(filename, X509Helper.privateKeyToPEM(enrollment.getKey()));

        filename = String.format("%s/tls/%s.crt",getOrgHome(), getTlsKeyName());
        PlainFileManager.saveFile(filename, enrollment.getCert());
    }
}
