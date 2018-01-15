package msf.fc.node.nodes.rrs;

import msf.fc.node.nodes.FcAbstractNodeScenarioBase;
import msf.mfcfc.core.scenario.RestRequestBase;

/**
 * Abstract class to implement common process of node management (RR)-related processing in configuration management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class FcAbstractRrNodeScenarioBase<T extends RestRequestBase> extends FcAbstractNodeScenarioBase<T> {

}
