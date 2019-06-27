
package msf.fc.node.interfaces.ifmaintenance;

import msf.fc.node.interfaces.FcAbstractInterfaceScenarioBase;
import msf.mfcfc.core.scenario.RestRequestBase;

/**
 * Abstract class to implement the common process of the interface shutdown
 * processing in the configuration management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherited the RestRequestBase class
 */
public abstract class FcAbstractInterfaceMaintenanceScenarioBase<T extends RestRequestBase>
    extends FcAbstractInterfaceScenarioBase<T> {
}
