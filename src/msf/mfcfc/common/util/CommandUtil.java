
package msf.mfcfc.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;

/**
 * Utility class for OS command execution.
 *
 * @author NTT
 *
 */
public class CommandUtil {

  private static final MsfLogger logger = MsfLogger.getInstance(CommandUtil.class);

  public static final Integer GET_FAILED = -1;

  private static final int DEFAULT_FAILED_COMMAND_RESULT_LINE = 8;

  /**
   * Execute a command and return the execution result.
   *
   * @param command
   *          Command to execute
   * @param failedResultLineNum
   *          Number of rows of failure data to be returned when execution
   *          result acquisition fails.
   * @param charsetName
   *          Charactor code
   * @return A list containing all rows of the command execution result.
   * @throws MsfException
   *           If the OS command execution fails.
   */
  public static List<String> getCommand(ProcessBuilder command, int failedResultLineNum, String charsetName)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "command", "failedResultLineNum" },
          new Object[] { command.command(), failedResultLineNum });

      List<String> result = new ArrayList<>();
      try (InputStream inputStream = command.start().getInputStream();
          BufferedReader bufferedReader = new BufferedReader((charsetName == null) ? new InputStreamReader(inputStream)
              : new InputStreamReader(inputStream, charsetName));) {
        for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
          result.add(line);
        }
        if (result.isEmpty()) {
          logger.warn(MessageFormat.format("Command failed. command={0}", command.command()));

          for (int i = 0; i < failedResultLineNum; i++) {
            result.add(String.valueOf(GET_FAILED));
          }
        }
        logger.debug("result = " + result);
        return result;
      } catch (IOException exp) {
        logger.warn(MessageFormat.format("IOException occurred. command={0}", command.command()), exp);

        for (int i = 0; i < failedResultLineNum; i++) {
          result.add(String.valueOf(GET_FAILED));
        }
        return result;
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Execute a command and return the execution result.
   *
   * @param command
   *          Command to execute
   * @return A list containing all rows of the command execution result.
   * @throws MsfException
   *           If the OS command execution fails.
   */
  public static List<String> getCommand(ProcessBuilder command) throws MsfException {
    return getCommand(command, DEFAULT_FAILED_COMMAND_RESULT_LINE, null);
  }

  /**
   * Execute a command and return the execution result.
   *
   * @param command
   *          Command to execute
   * @param charsetName
   *          Charactor code
   * @return A list containing all rows of the command execution result.
   * @throws MsfException
   *           If the OS command execution fails.
   */
  public static List<String> getCommand(ProcessBuilder command, String charsetName) throws MsfException {
    return getCommand(command, DEFAULT_FAILED_COMMAND_RESULT_LINE, charsetName);
  }

  /**
   * Execute a command.
   *
   * @param command
   *          Command to execute
   * @throws MsfException
   *           If the OS command execution fails.
   */
  public static void execCommand(ProcessBuilder command) throws MsfException {
    try {
      logger.methodStart(new String[] { "command" }, new Object[] { command.command() });
      Process process = command.start();
      int result = process.waitFor();

      if (result != 0) {
        String logMsg = MessageFormat.format("failed command execution, command = {0}, return code = {1}",
            command.command(), result);
        logger.warn(logMsg);
        throw new MsfException(ErrorCode.EXECUTE_FILE_ERROR, logMsg);
      }
    } catch (IOException | InterruptedException exp) {
      String logMsg = MessageFormat.format("failed command execution, command = {0}", command.command(), exp);
      logger.warn(logMsg, exp);
      throw new MsfException(ErrorCode.EXECUTE_FILE_ERROR, logMsg);
    } finally {
      logger.methodEnd();
    }
  }
}
