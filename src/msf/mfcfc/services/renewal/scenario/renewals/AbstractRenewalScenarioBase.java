
package msf.mfcfc.services.renewal.scenario.renewals;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.AbstractScenario;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.services.renewal.common.constant.RenewalControllerType;
import msf.mfcfc.services.renewal.common.constant.SwitchoverControllerType;

/**
 * Abstract class to implement the common process of controller renewal-related
 * processing in the controller file renewal function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherited the RestRequestBase class
 *
 */
public abstract class AbstractRenewalScenarioBase<T extends RestRequestBase> extends AbstractScenario<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractRenewalScenarioBase.class);

  /**
   * Method for checking the format of the target controller of the CTL renewal.
   *
   * @param controllerType
   *          Controller parameter
   * @throws MsfException
   *           If the parameter check is NG
   */
  public static void checkRenewalController(String controllerType) throws MsfException {
    try {
      logger.methodStart(new String[] { "controller" }, new Object[] { controllerType });

      List<String> controllers = Arrays.asList(controllerType.split("\\+", 0));
      for (String controller : controllers) {
        RenewalControllerType controllerEnum = RenewalControllerType.getEnumFromMessage(controller);
        if (controllerEnum == null) {
          String logMsg = MessageFormat.format("param is undefined. param = {0}, value = {1}", "controller",
              controllerType);
          throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
        }
      }

      if (!controllers.contains(RenewalControllerType.FC.getMessage())) {
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "fc is not contained in controller parameter.");
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Method for checking the format of the target controller of the CTL
   * switching over.
   *
   * @param controllerType
   *          Controller parameter
   * @throws MsfException
   *           If the parameter check is NG
   */
  public static void checkSwitchoverController(String controllerType) throws MsfException {
    try {
      logger.methodStart(new String[] { "controller" }, new Object[] { controllerType });

      List<String> controllers = Arrays.asList(controllerType.split("\\+", 0));
      for (String controller : controllers) {
        SwitchoverControllerType controllerEnum = SwitchoverControllerType.getEnumFromMessage(controller);
        if (controllerEnum == null) {
          String logMsg = MessageFormat.format("param is undefined. param = {0}, value = {1}", "controller",
              controllerType);
          throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected List<SwitchoverControllerType> getSwitchoverControllers(String controller) {
    List<SwitchoverControllerType> controllers = new ArrayList<>();
    if (controller == null) {

      controllers.addAll(Arrays.asList(SwitchoverControllerType.values()));
      return controllers;
    }

    List<String> controllerStrings = Arrays.asList(controller.split("\\+", 0));
    for (String controllerString : controllerStrings) {
      controllers.add(SwitchoverControllerType.getEnumFromMessage(controllerString));
    }
    return controllers;
  }

}
