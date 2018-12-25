import com.google.gson.Gson;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.Formatter;

@Service
public class SmsService {
    private final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private final String HOST="https://www.smsup.es/api/sms/";
    private final String SECRET_KEY="your_key";
    private final String CLIENT_ID="your_id";

    /*
    Send sms. Can return any type
     */
    public HttpStatus sendSms(SmsEntity entity) throws Exception{
        String now=LocalDateTime.now().withNano(0)+"+00:00";//GMT (London, Lisbon) used here
        //Google Json serializer
        Gson gson=new Gson();
        String query="POST/api/sms/"+now+gson.toJson(entity);
        
        //Add headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Firma", CLIENT_ID+":"+generateHMACSignature(query, SECRET_KEY));
        headers.set("Sms-Date", now);
        
        //Add body and headers to request
        HttpEntity<SmsEntity> request = new HttpEntity<>(entity, headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(HOST, HttpMethod.POST, request, String.class).getStatusCode();
    }

    private String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    
    /*
    Generates HMAC signature to attach to headers of every request
     */
    private String generateHMACSignature(String data, String key)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException
    {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        return toHexString(mac.doFinal(data.getBytes()));
    }

}
