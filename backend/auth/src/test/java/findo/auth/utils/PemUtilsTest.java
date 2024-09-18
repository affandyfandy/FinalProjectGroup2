package findo.auth.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PemUtilsTest {

    private String validPrivateKeyPEM;
    private String validPublicKeyPEM;

    @BeforeEach
    void setUp() {
        validPrivateKeyPEM = """
                -----BEGIN PRIVATE KEY-----
                MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC73jvL3rbf6IHQ
                +d8RgERupPB6lJPlkuWMHpBhpffCNQjXnic5ljhudQsmqYCdOfhUH5nekBZS0I3e
                RkDdR503tOYHu+ZaQt3poyGoqgWKpVJds25zXVUmo5XC5lQ3GZ5v65dtHvDAwJNw
                Z+b+IiI/NNXopmswXSh4Cl4tMV9U2WXFHDKucd+yN08VJ3ETe+eG03aBip4npPbi
                kbBDq26nRU9UOd/3DGVaoHjDivuyTGYUF1LjIRQzXHNoJ+G/XBbeR+ObaNqloJz7
                bcdqe4N/s6CVimasmukXSx2SDwnQIpNKJrcOnbUeYJCncNRmNj7H5HtZn1RU2jgy
                FVFLfLFPAgMBAAECggEAM+5mkSBrZNGcsOuhYfEW+Y7Wesv/ZgKJOYZpWtTJ/AE1
                BIVPSnff+YiZIILSbqPLpVVm70LdKN9FmB/LZl+ZcgCrZY/nSSQau67AayM0jPLO
                JeCns3QlSZ0GlO2ecey/AykYnq1i16VkHDpce/trhBVS3BDEqIXX3fbfuZZgR8+m
                8pdvRG3jWL2q+LmrzWK/bxKhEj5PTse28aeyOtUswHrp+PHK5cN0GXK+3oVMdhZC
                PysOaDEtsXI04WybpNkzufHeW/Z/fqRzHbzgVVJ0xfwXl48fNZwGCsSUsqSSLsOj
                15CZTfDEdL66QCxTQocb+Eg7QabDOlPrtW3q8MdyQQKBgQDqciUCX0yE9m0wXQ4E
                Ahmn7PpJW9oMgu/vf0plvtN/2fGKAFP9pIMctLLgI1IuIfqrUPkc1ZloGzzUJZl9
                VZWPTDF1OUT85j7dMa640fDwgwCF1ibZXC5loKDtcmZ6hWCOnQ2NoyKuQhJKnjRp
                w4aAOtJSACRW3yMQR+JaVvugpwKBgQDNI9jPCD9qWo2HmFLKhDStkniS4RPcWoEO
                8VgUnX8jdamKX+JwNJn9HleE0wj/XuDYXGT1LFd8k5MgJC50chKahLmrhGHEsOmN
                kDRQ+zGhnum903kpbMDo4sJjWI+QbpGYqzoL/bysrrOxZFE5wgYxFBr8YsdJL/nh
                +Y0Azw0XGQKBgDBkz27l7Q2lMHTDQJljNZiMOj3aT/A1NwHg7KKD+XRysIEkADJj
                Ow0535KUt+n96ZhjZoilLr1moe/Isx5EBFNIDw17fNoXTPKrxr0v2ORMplb6FZrY
                hFtGBPH+o1i3H29Qy/NosUf3K39rmLYcPi9J9RAoJ1gnRz2dGQQ84EQRAoGAdpRB
                EDOpTJX5d6byPIQ9WxPuxKB8dYSPU47opkoAIwQxs34Hi2qslKcEWhAPBGbYu9h0
                t1AhZi57/jlL7R2/aThNhzqntSW6a40u4JzDLLSKUqM6R1rJDBV/3iNkIU34tXJM
                xH+wn3UBBAqD9UoVgCsqCjgjEEg+PoSdIBmPbIkCgYADcv7wmEr20Os12sj0RVKQ
                PV+NSwTFAJOlmF37TDLcHdlotLO0KKNgkn5rBn0qqJPDbHjhzmZa4IZYRCoIEUvo
                5IlpiXdGDRubyLTC3gncDz+EHR9IWCZmKY5B59Trj+t60nkj+KkKBv/TgfRiVCjX
                fO2MmJxHy7I4KNtT+KStjg==
                -----END PRIVATE KEY-----
                """;

        validPublicKeyPEM = """
                -----BEGIN PUBLIC KEY-----
                MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu947y9623+iB0PnfEYBE
                bqTwepST5ZLljB6QYaX3wjUI154nOZY4bnULJqmAnTn4VB+Z3pAWUtCN3kZA3Ued
                N7TmB7vmWkLd6aMhqKoFiqVSXbNuc11VJqOVwuZUNxmeb+uXbR7wwMCTcGfm/iIi
                PzTV6KZrMF0oeApeLTFfVNllxRwyrnHfsjdPFSdxE3vnhtN2gYqeJ6T24pGwQ6tu
                p0VPVDnf9wxlWqB4w4r7skxmFBdS4yEUM1xzaCfhv1wW3kfjm2japaCc+23HanuD
                f7OglYpmrJrpF0sdkg8J0CKTSia3Dp21HmCQp3DUZjY+x+R7WZ9UVNo4MhVRS3yx
                TwIDAQAB
                -----END PUBLIC KEY-----
                """;
    }

    @Test
    void testReadPrivateKey_success() throws Exception {
        InputStream privateKeyStream = new ByteArrayInputStream(validPrivateKeyPEM.getBytes());

        PrivateKey privateKey = PemUtils.readPrivateKey(privateKeyStream);

        assertNotNull(privateKey);
        assertEquals("RSA", privateKey.getAlgorithm());
    }

    @Test
    void testReadPublicKey_success() throws Exception {
        InputStream publicKeyStream = new ByteArrayInputStream(validPublicKeyPEM.getBytes());

        RSAPublicKey publicKey = PemUtils.readPublicKey(publicKeyStream);

        assertNotNull(publicKey);
        assertEquals("RSA", publicKey.getAlgorithm());
    }

    @Test
    void testReadPrivateKey_invalidKey_throwsException() {
        String invalidPrivateKeyPEM = "invalid private key";
        InputStream privateKeyStream = new ByteArrayInputStream(invalidPrivateKeyPEM.getBytes());

        Exception exception = assertThrows(Exception.class, () -> {
            PemUtils.readPrivateKey(privateKeyStream);
        });

        assertTrue(exception instanceof InvalidKeySpecException || 
                   exception instanceof IllegalArgumentException);
    }

    @Test
    void testReadPublicKey_invalidKey_throwsException() {
        String invalidPublicKeyPEM = "invalid public key";
        InputStream publicKeyStream = new ByteArrayInputStream(invalidPublicKeyPEM.getBytes());

        Exception exception = assertThrows(Exception.class, () -> {
            PemUtils.readPublicKey(publicKeyStream);
        });

        assertTrue(exception instanceof InvalidKeySpecException || 
                   exception instanceof IllegalArgumentException);
    }
}
