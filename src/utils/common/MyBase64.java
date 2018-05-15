package utils.common;

import org.junit.Test;

import java.util.Base64;

public class MyBase64 {
    public static String base64Encode2Str(byte[] buf){
        return Base64.getEncoder().encodeToString(buf);
    }
    public static byte[] base64Encode2Bytes(byte[] buf){
        return Base64.getEncoder().encode(buf);
    }
    public static byte[] base64Decode2Bytes(String encoded) {
        return Base64.getDecoder().decode(encoded);
    }
    public static String base64Decode2Str(String encoded) {
        return new String(base64Decode2Bytes(encoded));
    }
    @Test
    public void Base64Test(){
        String plain = "Skutter_bot990101";
        String encoded = base64Encode2Str(plain.getBytes());
        String decoded = base64Decode2Str(encoded);
        System.out.println(encoded);
        System.out.println(decoded);

        String encoded2 = "aHR0cHM6Ly9yZWRvcmJpdC1hcGktZGV2Lmdlby5hcHBsZS5jb20vYXBpLzAuMS9waXBlbGluZXJ1bnMvZDRkNjc5YzgzNmM3NGExN2E5NDRlNDk4YTUwZWY0ZjI/YXV0aF90b2tlbj1leUpoYkdjaU9pSklVekkxTmlKOS5ZM0J5YjJRdU1tUTBNV1V4TmpBdE5UTmhaQzB4TVdVNExXRXdNMlV0TlRFNU16TTNNV0l4WW1GaS5NaS1xMFREVEVlZEZWNG9MWTFZdlJ5VEpBLUMzZW9ZekVmUUpvLUZXT2tZJnJlZG9yYml0X3Nlc3Npb25faWQ9ZDRkNjc5YzgzNmM3NGExN2E5NDRlNDk4YTUwZWY0ZjImcmVkb3JiaXRfdXNlcl9uYW1lPWVyaWNfaGU=";
        System.out.println(base64Decode2Str(encoded2));

        String encoded3 = "aHR0cHM6Ly9sb2NhbGhvc3Q=";
        System.out.println(base64Decode2Str(encoded3));

    }
}
