package com.kawakawaryuryu.samplespringclientcert.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Base64;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean
  public PrivateKey privateKey() throws IOException {
    byte[] key = Base64.getDecoder().decode("");
    String keyContent = new String(key);
    try (PEMParser pemParser = new PEMParser(new StringReader(keyContent))) {
      JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
      KeyPair keyPair = converter.getKeyPair((PEMKeyPair) pemParser.readObject());
      return keyPair.getPrivate();
    }
  }

  @Bean
  public Certificate[] certificates() throws CertificateException {
    String cert = "";
    Certificate[] chain = new Certificate[1];
    chain[0] = CertificateFactory.getInstance("X.509")
        .generateCertificate(new ByteArrayInputStream(Base64.getDecoder().decode(cert)));
    return chain;
  }
}
