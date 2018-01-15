
package msf.fc.traffic.traffics;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcL3Cp;
import msf.fc.common.data.FcL3CpPK;
import msf.fc.common.data.FcNode;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.slices.FcL3CpDao;
import msf.fc.rest.ec.traffic.data.TrafficInfoCollectTrafficEcResponseBody;
import msf.fc.rest.ec.traffic.data.entity.TrafficInfoTrafficValueEcEntity;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.traffic.traffics.data.CpTrafficReadResponseBody;
import msf.mfcfc.traffic.traffics.data.CpTrafficRequest;
import msf.mfcfc.traffic.traffics.data.entity.CpTrafficEntity;

/**
 * Implementation class for CP traffic information acquisition.
 *
 * @author NTT
 *
 */
public class FcCpTrafficReadScenario extends FcAbstractCpsTrafficScenarioBase<CpTrafficRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcCpTrafficReadScenario.class);

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
  public FcCpTrafficReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
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
        FcL3CpDao fcL3CpDao = new FcL3CpDao();
        FcL3CpPK l3CpPk = new FcL3CpPK();
        l3CpPk.setSliceId(request.getSliceId());
        l3CpPk.setCpId(request.getCpId());
        FcL3Cp fcL3Cp = fcL3CpDao.read(session, l3CpPk);

        if (fcL3Cp == null) {

          String logMsg = MessageFormat.format("target resource not found. parameters={0}, L3CpPk={1}", "L3CP",
              ToStringBuilder.reflectionToString(l3CpPk));
          throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, logMsg);
        }

        FcNodeDao fcNodeDao = new FcNodeDao();

        FcNode fcNode = fcNodeDao.read(session, fcL3Cp.getVlanIf().getId().getNodeInfoId().longValue());

        if (fcNode == null) {

          String logMsg = MessageFormat.format("target resource not found. parameters={0}, nodeInfoId={1}", "node",
              fcL3Cp.getVlanIf().getId().getNodeInfoId());
          throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, logMsg);
        }

        TrafficInfoCollectTrafficEcResponseBody responseBody = sendTrafficRead(String.valueOf(fcNode.getEcNodeId()));

        CpTrafficEntity ifTrafficEntity = getCpTraffic(fcL3Cp.getVlanIf().getId().getVlanIfId(), responseBody);
        RestResponseBase responseBase = responseTrafficInfoData(ifTrafficEntity);
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

  private CpTrafficEntity getCpTraffic(Integer ifId, TrafficInfoCollectTrafficEcResponseBody responseBody)
      throws MsfException {

    CpTrafficEntity cpTraffic = null;

    List<TrafficInfoTrafficValueEcEntity> trafficValues = responseBody.getSwitchTraffic().getTrafficValueList();
    if (trafficValues == null) {

      String logMsg = MessageFormat.format("target resource not found. parameters={0}", "TrafficValues");
      throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, logMsg);
    }

    for (TrafficInfoTrafficValueEcEntity trafficInfo : trafficValues) {
      if (!InterfaceType.VLAN_IF.getMessage().equals(trafficInfo.getIfType())) {
        continue;
      }
      if (!String.valueOf(ifId).equals(trafficInfo.getIfId())) {
        continue;
      }

      cpTraffic = getCpTrafficEntity(request.getSliceId(), request.getCpId(), trafficInfo);
      break;
    }

    if (cpTraffic == null) {
      String logMsg = MessageFormat.format("target resource not found. parameters={0}, sliceId={1}, cpId={2}, ifId={3}",
          "CpTraffic", request.getSliceId(), request.getCpId(), ifId);
      throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, logMsg);
    }
    return cpTraffic;
  }

  private RestResponseBase responseTrafficInfoData(CpTrafficEntity cpTraffic) {

    CpTrafficReadResponseBody body = new CpTrafficReadResponseBody();
    body.setCpTraffic(cpTraffic);
    return createRestResponse(body, HttpStatus.OK_200);
  }

}