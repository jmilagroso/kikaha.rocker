package kikaha.app.services;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jay <j.milagroso@gmail.com>
 */
public class S3 {

    /**
     * AwsS3Request
     */
    public static class AwsS3Request {
        private String url;
        private Map<String, String> headers;
        private String requestBody;

        public AwsS3Request(String url, Map<String, String> headers, String requestBody) {
            this.url = url;
            this.headers = headers;
            this.requestBody = requestBody;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Map<String, String> getHeaders() {
            return this.headers;
        }

        public void setHeaders(Map<String, String> headers) {
            this.headers = headers;
        }

        public String getRequestBody() {
            return this.getRequestBody();
        }

        public void setRequestBody(String requestBody) {
            this.requestBody = requestBody;
        }
    }

    //TODO implement typesafe config for these values.
    private final static String utf8 = "UTF-8";
    private final static String hmacsha1 = "HMACSHA1";
    private final static String s3Uri = "http://localhost:43908";
    private final static String s3bucket = "jay";
    private final static String s3Key = "LOCALMINIO";
    private final static String s3Secret = "cloud4wi/minerva";
    private static String path;

    private final static SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
    {
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static String getS3bucket() {
        return s3bucket;
    }

    public static String getPath() {
        return path;
    }

    public static String getRequestURL() {
        return S3.getS3Uri() + "/"+S3.getS3bucket()+"/"+S3.getPath();
    }

    public static String getS3Uri() {
        return s3Uri;
    }
    /**
     * Auth
     * @param str2sign
     * @return
     */
    public static String auth(String str2sign) {

        String result = "";

        try {
            Mac mac = Mac.getInstance(hmacsha1);
            mac.init(getSigningKey());

            byte[] dataBytes = str2sign.getBytes(utf8);
            byte[] signature = mac.doFinal(dataBytes);
            String sign = Base64.encodeBase64String(signature);

            result = "AWS "+s3Key+":"+sign;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * AwsS3Request get
     * @return
     */
    public static AwsS3Request get(String path) {

        S3.path = path;

        String date = sdf.format(new Date());
        String authorization = auth("GET\n\n\n"+date+"\n/"+s3bucket+"/"+path);

        Map <String, String> headers = new HashMap<String, String>();
        headers.put("Date", date);
        headers.put("Authorization", authorization);

        return new AwsS3Request("$s3Uri/$path", headers, "");
    }

    /**
     * AwsS3Request get
     * @return
     */
    public static AwsS3Request put(String path, String content) {

        String date = sdf.format(new Date());
        String authorization = auth("PUT\n\ntext/html\n$date\n/$s3bucket/$path");

        StringBuilder sb = new StringBuilder();
        sb.append(content.length());
        String contentLength = sb.toString();

        Map <String, String> headers = new HashMap<String, String>();
        headers.put("Date", date);
        headers.put("Authorization", authorization);
        headers.put("Content-Type", "text/html");
        headers.put("Content-Length", contentLength);

        return new AwsS3Request("$s3Uri/$path", headers, content);
    }

    /**
     * getSigningKey
     * @return
     */
    private static SecretKeySpec getSigningKey() {

        SecretKeySpec secretKeySpec = null;

        try {
            secretKeySpec = new SecretKeySpec(s3Secret.getBytes(utf8), hmacsha1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return secretKeySpec;
    }
}
