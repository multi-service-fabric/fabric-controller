package msf.fc.traffic.tm;

import java.io.File;

import msf.fc.common.exception.MsfException;


public abstract class AbstractTrafficFileController {

  protected boolean checkWriteFile(File file) {
    if (file.exists()) {
      if (file.isFile() && file.canWrite()) {
        return true;
      }
    }
    return false;
  }

  protected boolean checkReadFile(File file) {
    if (file.exists()) {
      if (file.isFile() && file.canRead()) {
        return true;
      }
    }
    return false;
  }

  abstract void createFile() throws MsfException, InterruptedException;

}
