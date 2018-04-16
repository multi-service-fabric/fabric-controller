
package msf.fc.node.interfaces.internalifs;

import msf.fc.node.interfaces.FcAbstractInterfaceScenarioBase;
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
public abstract class FcAbstractInternalInterfaceScenarioBase<T extends RestRequestBase>
    extends FcAbstractInterfaceScenarioBase<T> {

}
