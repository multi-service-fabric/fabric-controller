
package msf.fc.node.interfaces.clusterlinkifs;

import msf.fc.node.interfaces.FcAbstractInterfaceScenarioBase;
import msf.mfcfc.core.scenario.RestRequestBase;

/**
 * Abstract class to implement the common process of the inter-cluster link
 * interface-related processing in the configuration management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class FcAbstractClusterLinkInterfaceScenarioBase<T extends RestRequestBase>
    extends FcAbstractInterfaceScenarioBase<T> {

}
