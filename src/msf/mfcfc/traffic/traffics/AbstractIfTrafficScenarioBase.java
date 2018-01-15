
package msf.mfcfc.traffic.traffics;

import msf.mfcfc.core.scenario.AbstractScenario;
import msf.mfcfc.core.scenario.RestRequestBase;

/**
 * Abstract class to implement common process in the IF traffic information
 * management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 *
 */
public abstract class AbstractIfTrafficScenarioBase<T extends RestRequestBase> extends AbstractScenario<T> {

}
