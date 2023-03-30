package net.schrage.soapclient;

import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
import java.util.Arrays;

public class UTPasswordCallback implements CallbackHandler {
  @Override
  public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
    Arrays.stream(callbacks)
        .map(callback -> (WSPasswordCallback) callback)
        .filter(allServerCredentials -> allServerCredentials.getIdentifier().equals("Michael"))
        .forEach(serverCredential -> serverCredential.setPassword("geheim"));
  }
}
