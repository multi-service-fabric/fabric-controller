
package msf.mfc.node.interfaces.internalifs;

import msf.mfc.node.interfaces.MfcAbstractInterfaceScenarioBase;
import msf.mfcfc.core.scenario.RestRequestBase;

/**
 * Abstract class to implement the common process of intra-cluster link
 * interface-related processing in configuration management function.
 *
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class MfcAbstractInternalInterfaceScenarioBase<T extends RestRequestBase>
    extends MfcAbstractInterfaceScenarioBase<T> {
}
