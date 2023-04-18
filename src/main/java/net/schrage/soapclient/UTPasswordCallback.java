package net.schrage.soapclient;

import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UTPasswordCallback implements CallbackHandler {

  Map<String, String> serverStoredCredentials = new HashMap<>();

  public UTPasswordCallback() {
    serverStoredCredentials.put("Michael", "geheim");
    //serverStoredCredentials.put("myclientkey", "cspass");
    serverStoredCredentials.put("myservicekey", "cspass");
  }
  @Override
  public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
    Arrays.stream(callbacks)
        .map(callback -> (WSPasswordCallback) callback)
        .filter(transmittedCredential -> serverStoredCredentials.containsKey(transmittedCredential.getIdentifier()))
        .forEach(transmittedCredential -> transmittedCredential.setPassword(serverStoredCredentials.get(transmittedCredential.getIdentifier())));
  }

    /*WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
    if (pc.getIdentifier().equals("myclientkey")) {
      pc.setPassword("cspass");
    }
  }*/

}
