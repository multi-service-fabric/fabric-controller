
package msf.fc.core.operation.scenario;

import msf.mfcfc.core.operation.scenario.AbstractOperationScenarioBase;
import msf.mfcfc.core.scenario.RestRequestBase;

/**
 * Abstract class to implement the common process of operation-related
 * processing in the system basic function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class FcAbstractOperationScenarioBase<T extends RestRequestBase>
    extends AbstractOperationScenarioBase<T> {

}
