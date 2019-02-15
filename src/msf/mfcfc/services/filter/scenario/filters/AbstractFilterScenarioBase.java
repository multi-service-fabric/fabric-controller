
package msf.mfcfc.services.filter.scenario.filters;

import msf.mfcfc.core.scenario.AbstractScenario;
import msf.mfcfc.core.scenario.RestRequestBase;

/**
 * Abstract class to implement the common process in the filter management
 * function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits RestRequestBase class
 *
 */
public abstract class AbstractFilterScenarioBase<T extends RestRequestBase> extends AbstractScenario<T> {

}
