
package msf.mfcfc.traffic.traffics;

import msf.mfcfc.core.scenario.AbstractScenario;
import msf.mfcfc.core.scenario.RestRequestBase;

/**
 * Abstract class to implement the common process in the CP traffic information
 * management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 *
 */
public abstract class AbstractCpsTrafficScenarioBase<T extends RestRequestBase> extends AbstractScenario<T> {

}
