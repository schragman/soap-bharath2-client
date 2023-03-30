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
      props.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
      props.put(WSHandlerConstants.USER, "Michael");
      props.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
      props.put(WSHandlerConstants.PW_CALLBACK_CLASS, UTPasswordCallback.class.getName());
      WSS4JOutInterceptor wssout = new WSS4JOutInterceptor(props);
      endpoint.getOutInterceptors().add(wssout);
      /*End of WSS4J Security*/

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
