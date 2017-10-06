package msf.fc.rest.common;

import msf.fc.common.exception.MsfException;

public interface RestRequestValidator {

  public void validate() throws MsfException;

}
