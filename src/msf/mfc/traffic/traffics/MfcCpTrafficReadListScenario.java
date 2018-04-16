
package msf.mfc.traffic.traffics;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.data.MfcL3Cp;
import msf.mfc.common.data.MfcL3Slice;
import msf.mfc.db.dao.slices.MfcL3CpDao;
import msf.mfc.db.dao.slices.MfcL3SliceDao;
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
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.traffic.traffics.data.CpTrafficReadListResponseBody;
import msf.mfcfc.traffic.traffics.data.CpTrafficRequest;

/**
 * Implementation class for CP traffic information list acquisition.
 *
 * @author NTT
 *
 */
public class MfcCpTrafficReadListScenario extends MfcAbstractCpsTrafficScenarioBase<CpTrafficRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcCpTrafficReadListScenario.class);

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
  public MfcCpTrafficReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
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
        String logMsg = MessageFormat.format("param is undefined.param={0}, value={1}", "slice_type",
            request.getSliceType());
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
      ParameterCheckUtil.checkNotNull(request.getSliceId());

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
        List<MfcL3Cp> mfcL3CpList = mfcL3CpDao.readListBySliceId(session, request.getSliceId());

        if (mfcL3CpList.isEmpty()) {
          MfcL3SliceDao mfcL3SliceDao = new MfcL3SliceDao();

          MfcL3Slice mfcL3Slice = mfcL3SliceDao.read(session, request.getSliceId());

          if (mfcL3Slice == null) {

            String logMsg = MessageFormat.format("related resource not found. parameters={0}, sliceId={1}", "sliceId",
                request.getSliceId());
            throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, logMsg);
          }

          return responseTrafficInfoData(new CpTrafficReadListResponseBody());
        }

        return sendTrafficReadList(getSendClusterIdForL3CpList(mfcL3CpList));

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

  private Set<Integer> getSendClusterIdForL3CpList(List<MfcL3Cp> mfcL3CpList) throws MsfException {

    Set<Integer> result = new HashSet<Integer>();

    for (MfcL3Cp temp : mfcL3CpList) {
      result.add(temp.getSwCluster().getSwClusterId());
    }

    return result;

  }

  private RestResponseBase responseTrafficInfoData(CpTrafficReadListResponseBody body) {
    RestResponseBase restResponseBase = new RestResponseBase();

    restResponseBase.setResponseBody(JsonUtil.toJson(body));

    restResponseBase.setHttpStatusCode(HttpStatus.OK_200);

    return restResponseBase;
  }

}
