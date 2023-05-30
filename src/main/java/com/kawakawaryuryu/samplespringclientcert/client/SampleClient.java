package com.kawakawaryuryu.samplespringclientcert.client;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class SampleClient {

  private final PrivateKey privateKey;
  private final Certificate[] certificates;

  public void get() {
    try {
      RestTemplate restTemplate = clientCertRestTemplate(privateKey, certificates);
    } catch (Exception e) {
      log.error("error", e);
    }
  }

  private RestTemplate clientCertRestTemplate(PrivateKey privateKey, Certificate[] chain)
      throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException, KeyManagementException {
    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    keyStore.load(null, null);
    keyStore.setKeyEntry("1", privateKey, null, chain);
    SSLContext sslContext = SSLContextBuilder
        .create()
        .loadKeyMaterial(keyStore, null)
        .build();
    HttpClient httpClient = HttpClients.custom()
        .setSSLContext(sslContext)
        .build();
    return new RestTemplateBuilder()
        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(httpClient))
        .build();
  }
}
