package net.schrage.soapclient;

import net.schrage.soapserver.ws.SumRequest;
import net.schrage.soapserver.ws.SumResponse;
import net.schrage.soapserver.ws.SumWs;
import net.schrage.soapserver.ws.SumWsImplService;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.springframework.context.annotation.Bean;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SumServiceClient {

  public static void main(String[] args) {
    try {
      SumWsImplService service = new SumWsImplService(new URL("http://localhost:8080/sum/sumservice?wsdl"));
      SumWs sumWs = service.getSumWsImplPort();

      /* Adding Security*/
      Client client = ClientProxy.getClient(sumWs);
      Endpoint endpoint = client.getEndpoint();
      Map<String, Object> props =new HashMap<>();
      props.put(WSHandlerConstants.ACTION, "Signature");
      props.put(WSHandlerConstants.USER, "myclientkey");
      props.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
      props.put(WSHandlerConstants.PW_CALLBACK_CLASS, UTPasswordCallback.class.getName());
      /*End of WSS4J Security*/

      /*Adding Encryption*/
      //props.put(WSHandlerConstants.ENCRYPTION_USER, "myclientkey");
      props.put(WSHandlerConstants.SIG_PROP_FILE, "etc/clientKeystore.properties");
      /*End Encryption*/

      /*WSS4JOutInterceptor wss4jOutInterceptor = new WSS4JOutInterceptor();
      wss4jOutInterceptor.setProperty("action", "Encrypt");
      wss4jOutInterceptor.setProperty("user", "yourUsername");
      wss4jOutInterceptor.setProperty("encryptionUser", "yourUsername");
      wss4jOutInterceptor.setProperty("passwordCallbackClass", "com.yourpackage.PasswordCallback");
      wss4jOutInterceptor.setProperty("encryptionPropFile", "encryption.properties");
      wss4jOutInterceptor.setProperty("encryptionKeyIdentifier", "IssuerSerial");
      wss4jOutInterceptor.setProperty("keyIdentifierType", "IssuerSerial");
      wss4jOutInterceptor.setProperty("secureParts",
          "{Element}{http://schemas.xmlsoap.org/soap/envelope/}
*/

     /* @Bean
      public WSS4JOutInterceptor wss4jOutInterceptor() {
        Map<String, Object> outProps = new HashMap<>();
        outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.ENCRYPT);
        outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, ClientKeystorePasswordCallback.class.getName());
        outProps.put(WSHandlerConstants.USER, "client");
        outProps.put(WSHandlerConstants.ENCRYPTION_USER, "server");
        outProps.put(WSHandlerConstants.ENCRYPTION_PARTS, "{Content}{http://schemas.xmlsoap.org/soap/envelope/}Body");
        outProps.put(WSHandlerConstants.ENC_PROP_FILE, "clientKeystore.properties");
        outProps.put(WSHandlerConstants.SIG_PROP_FILE, "clientKeystore.properties");
        outProps.put(WSHandlerConstants.USE_REQ_SIG_CERT, "true");
        outProps.put(WSHandlerConstants.SIG_KEY_ID, "DirectReference");
        return new WSS4JOutInterceptor(outProps);
      }*/


      WSS4JOutInterceptor wssout = new WSS4JOutInterceptor(props);
      endpoint.getOutInterceptors().add(wssout);

      SumRequest sumRequest = new SumRequest();
      sumRequest.setNum1(5);
      sumRequest.setNum2(8);
      SumResponse sumResponse = sumWs.calculateSum(sumRequest);
      System.out.println("Das Ergebnis von " + sumRequest.getNum1() + " + " + sumRequest.getNum2() + " = " + sumResponse.getSum());
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }
}
