
package msf.mfc.slice.slices.l3slice;

import msf.mfc.common.data.MfcL3Slice;
import msf.mfc.db.dao.slices.MfcL3SliceDao;
import msf.mfc.slice.slices.MfcAbstractSliceRunnerBase;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceRequest;

/**
 * Abstract class to implement the common process of L3 slice related
 * asynchronous runner process in slice management function.
 *
 * @author NTT
 *
 */
public abstract class MfcAbstractL3SliceRunnerBase extends MfcAbstractSliceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcAbstractL3SliceRunnerBase.class);

  protected L3SliceRequest request;

  protected void checkL3SlicePresence(MfcL3Slice l3Slice, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Slice", "sliceId" }, new Object[] { l3Slice, sliceId });
      ParameterCheckUtil.checkNotNullTargetResource(l3Slice, new String[] { "sliceId" }, new Object[] { sliceId });
    } finally {
      logger.methodEnd();
    }
  }

  protected MfcL3Slice getL3SliceAndCheck(SessionWrapper sessionWrapper, String sliceId) throws MsfException {
    try {
      logger.methodStart();
      MfcL3SliceDao l3SliceDao = new MfcL3SliceDao();
      MfcL3Slice l3Slice = l3SliceDao.read(sessionWrapper, sliceId);

      checkSlicePresence(l3Slice, sliceId);
      return l3Slice;
    } finally {
      logger.methodEnd();
    }
  }
}
