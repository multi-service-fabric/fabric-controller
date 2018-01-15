package msf.fc.node.interfaces.clusterlinkifs;

import msf.fc.node.interfaces.FcAbstractInterfaceScenarioBase;
import msf.mfcfc.core.scenario.RestRequestBase;

/**
 * Abstract class to implement common process of intercluster link interface-related processing in configuration management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherited the RestRequestBase class
 */
public abstract class FcAbstractClusterLinkInterfaceScenarioBase<T extends RestRequestBase>
    extends FcAbstractInterfaceScenarioBase<T> {

}
