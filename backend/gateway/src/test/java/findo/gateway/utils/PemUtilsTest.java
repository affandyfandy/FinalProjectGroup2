package findo.gateway.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PemUtilsTest {

    private String validPublicKeyPEM;

    @BeforeEach
    void setUp() {

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
    void testReadPublicKey_success() throws Exception {
        InputStream publicKeyStream = new ByteArrayInputStream(validPublicKeyPEM.getBytes());

        RSAPublicKey publicKey = PemUtils.readPublicKey(publicKeyStream);

        assertNotNull(publicKey);
        assertEquals("RSA", publicKey.getAlgorithm());
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