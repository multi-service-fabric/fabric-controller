
package msf.mfc.slice.slices.l2slice;

import msf.mfc.common.data.MfcL2Slice;
import msf.mfc.db.dao.slices.MfcL2SliceDao;
import msf.mfc.slice.slices.MfcAbstractSliceRunnerBase;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.slices.l2slice.data.L2SliceRequest;

/**
 * Abstract class to implement the common process of L2 slice related
 * asynchronous runner process in slice management function.
 *
 * @author NTT
 *
 */
public abstract class MfcAbstractL2SliceRunnerBase extends MfcAbstractSliceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcAbstractL2SliceRunnerBase.class);

  protected L2SliceRequest request;

  protected void checkL2SlicePresence(MfcL2Slice l2Slice, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice", "sliceId" }, new Object[] { l2Slice, sliceId });
      ParameterCheckUtil.checkNotNullTargetResource(l2Slice, new String[] { "sliceId" }, new Object[] { sliceId });
    } finally {
      logger.methodEnd();
    }
  }

  protected MfcL2Slice getL2SliceAndCheck(SessionWrapper sessionWrapper, String sliceId) throws MsfException {
    try {
      logger.methodStart();
      MfcL2SliceDao l2SliceDao = new MfcL2SliceDao();
      MfcL2Slice l2Slice = l2SliceDao.read(sessionWrapper, sliceId);

      checkSlicePresence(l2Slice, sliceId);
      return l2Slice;
    } finally {
      logger.methodEnd();
    }
  }
}
