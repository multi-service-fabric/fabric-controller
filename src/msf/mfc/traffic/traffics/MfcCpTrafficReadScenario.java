
package msf.mfc.traffic.traffics;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfc.common.data.MfcL3Cp;
import msf.mfc.common.data.MfcL3CpPK;
import msf.mfc.db.dao.slices.MfcL3CpDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.traffic.traffics.data.CpTrafficRequest;

/**
 * Implementation class for CP traffic information acquisition.
 *
 * @author NTT
 *
 */
public class MfcCpTrafficReadScenario extends MfcAbstractCpsTrafficScenarioBase<CpTrafficRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcCpTrafficReadScenario.class);

  /**
   * Constructor.
   *
   * <p>
   * Set the "operation type" and "system interface type" as arguments
   * </p>
   *
   * @param operationType
   *          Operation type
   * @param systemInterfaceType
   *          System interface type
   *
   */
  public MfcCpTrafficReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(CpTrafficRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNotNull(request.getSliceType());
      if (SliceType.L3_SLICE != request.getSliceTypeEnum()) {
        String logMsg = MessageFormat.format("parameter is undefined. parameter={0}, value={1}", "slice_type",
            request.getSliceType());
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
      ParameterCheckUtil.checkNotNull(request.getSliceId());
      ParameterCheckUtil.checkNotNull(request.getCpId());

      this.request = request;

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      SessionWrapper session = new SessionWrapper();
      try {
        session.openSession();
        MfcL3CpDao mfcL3CpDao = new MfcL3CpDao();
        MfcL3CpPK l3CpPk = new MfcL3CpPK();
        l3CpPk.setSliceId(request.getSliceId());
        l3CpPk.setCpId(request.getCpId());
        MfcL3Cp mfcL3Cp = mfcL3CpDao.read(session, l3CpPk);

        if (mfcL3Cp == null) {

          String logMsg = MessageFormat.format("target resource not found. parameters={0}, L3CpPk={1}", "L3CP",
              ToStringBuilder.reflectionToString(l3CpPk));
          throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, logMsg);
        }

        RestResponseBase responseBase = sendTrafficRead(mfcL3Cp.getSwCluster().getSwClusterId());

        return responseBase;

      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      } finally {
        session.closeSession();
      }
    } finally {
      logger.methodEnd();
    }
  }
}
