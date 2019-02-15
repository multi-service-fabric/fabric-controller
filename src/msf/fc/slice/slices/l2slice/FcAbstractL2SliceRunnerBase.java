
package msf.fc.slice.slices.l2slice;

import msf.fc.common.data.FcL2Slice;
import msf.fc.slice.slices.FcAbstractSliceRunnerBase;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.slice.slices.l2slice.data.L2SliceRequest;

/**
 * Abstract class to implement the common process of the L2 slice related
 * asynchronous runner process in the slice management function.
 *
 * @author NTT
 *
 */
public abstract class FcAbstractL2SliceRunnerBase extends FcAbstractSliceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractL2SliceRunnerBase.class);

  protected L2SliceRequest request;

  protected void checkL2SlicePresence(FcL2Slice l2Slice, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice", "sliceId" }, new Object[] { l2Slice, sliceId });
      ParameterCheckUtil.checkNotNullTargetResource(l2Slice, new String[] { "sliceId" }, new Object[] { sliceId });
    } finally {
      logger.methodEnd();
    }
  }
}
