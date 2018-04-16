
package msf.mfcfc.node.clusters;

import msf.mfcfc.core.scenario.AbstractScenario;
import msf.mfcfc.core.scenario.RestRequestBase;

/**
 * Abstract class to implement the common process of SW cluster-related
 * processing in configuration management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 *
 */
public abstract class AbstractClusterScenarioBase<T extends RestRequestBase> extends AbstractScenario<T> {

}
