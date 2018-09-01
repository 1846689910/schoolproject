package utils.common;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class URLParser {
    @Test
    public void urlParseTest(){
        try {
            URL url = new URL("https://redorbit-api-dev.geo.apple.com/api/0.1/pipelineruns/d4d679c836c74a17a944e498a50ef4f2?auth_token=eyJhbGciOiJIUzI1NiJ9.Y3Byb2QuZDFjM2RkYjAtNTdiZC0xMWU4LTllNGMtM2Q3MDQ2YmQ3ZDRi.L7Qwy2m6-vL-jQMUQqxW7kt_6mfmHmHdj1Xx9y1IjpI&redorbit_session_id=d4d679c836c74a17a944e498a50ef4f2&redorbit_user_name=skutter_bot");
            System.out.println(url.getQuery());  // 问号后的: auth_token=eyJhbGciOiJIUzI1NiJ9.Y3Byb2QuZDFjM2RkYjAtNTdiZC0xMWU4LTllNGMtM2Q3MDQ2YmQ3ZDRi.L7Qwy2m6-vL-jQMUQqxW7kt_6mfmHmHdj1Xx9y1IjpI&redorbit_session_id=d4d679c836c74a17a944e498a50ef4f2&redorbit_user_name=skutter_bot
            System.out.println(url.getPath());  // 域名后，问号前: /api/0.1/pipelineruns/d4d679c836c74a17a944e498a50ef4f2
            System.out.println(url.getHost());  // 域名, 无协议: redorbit-api-dev.geo.apple.com
            System.out.println(url.getProtocol());  // 协议: https
            // 完整url是由上述组成的
            System.out.println(url.getProtocol() + "://" + url.getHost() + url.getPath() + "?" + url.getQuery());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
