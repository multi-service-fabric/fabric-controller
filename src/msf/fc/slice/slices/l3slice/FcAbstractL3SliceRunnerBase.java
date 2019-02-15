
package msf.fc.slice.slices.l3slice;

import msf.fc.common.data.FcL3Slice;
import msf.fc.slice.slices.FcAbstractSliceRunnerBase;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceRequest;

/**
 * Abstract class to implement the common process of the L3 slice related
 * asynchronous runner process in the slice management function.
 *
 * @author NTT
 *
 */
public abstract class FcAbstractL3SliceRunnerBase extends FcAbstractSliceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractL3SliceRunnerBase.class);

  protected L3SliceRequest request;

  protected void checkL3SlicePresence(FcL3Slice l3Slice, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Slice", "sliceId" }, new Object[] { l3Slice, sliceId });
      ParameterCheckUtil.checkNotNullTargetResource(l3Slice, new String[] { "sliceId" }, new Object[] { sliceId });
    } finally {
      logger.methodEnd();
    }
  }
}
