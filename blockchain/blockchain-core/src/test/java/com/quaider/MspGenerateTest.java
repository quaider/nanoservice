package com.quaider;

import com.google.gson.Gson;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.util.Properties;

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


        String path = "src/main/resources/ca-cert.pem";
//        String tlsPem = readAllText(path);

        // Properties 用于携带 tls pem 证书
        Properties properties = new Properties();
        properties.put("pemFile", path);
        properties.put("allowAllHostNames", false);

        client = HFCAClient.createNewInstance(caLocation, properties);
        client.setCryptoSuite(crypto);

        Enrollment enrollment = client.enroll("admin", "adminpw");
        System.out.println(new Gson().toJson(enrollment));
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

}
