package net.schrage.soapclient;

import net.schrage.soapserver.ws.SumRequest;
import net.schrage.soapserver.ws.SumResponse;
import net.schrage.soapserver.ws.SumWs;
import net.schrage.soapserver.ws.SumWsImplService;

import java.net.MalformedURLException;
import java.net.URL;

public class SumServiceClient {

  public static void main(String[] args) {
    try {
      SumWsImplService service = new SumWsImplService(new URL("http://localhost:8080/sum/sumservice?wsdl"));
      SumWs sumWs = service.getSumWsImplPort();
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
