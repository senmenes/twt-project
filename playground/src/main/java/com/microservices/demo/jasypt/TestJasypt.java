package com.microservices.demo.jasypt;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;

public class TestJasypt {

    public static void main(String[] args) {

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("Demo_SecPass!");
        encryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        encryptor.setIvGenerator(new RandomIvGenerator());

        String result = encryptor.encrypt("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");
        System.out.println(result);
        System.out.println(encryptor.decrypt(result));
    }
}
