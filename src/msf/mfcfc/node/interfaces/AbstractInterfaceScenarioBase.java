
package msf.mfcfc.node.interfaces;

import msf.mfcfc.core.scenario.AbstractScenario;
import msf.mfcfc.core.scenario.RestRequestBase;

/**
 * Abstract class to implement the common process of the interface-related
 * processing in the configuration management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class AbstractInterfaceScenarioBase<T extends RestRequestBase> extends AbstractScenario<T> {

}
