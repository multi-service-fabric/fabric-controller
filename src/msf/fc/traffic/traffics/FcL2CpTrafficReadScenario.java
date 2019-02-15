
package msf.fc.traffic.traffics;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcL2Cp;
import msf.fc.common.data.FcL2CpPK;
import msf.fc.common.data.FcNode;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.slices.FcL2CpDao;
import msf.fc.rest.ec.traffic.data.TrafficInfoCollectTrafficEcResponseBody;
import msf.fc.rest.ec.traffic.data.entity.TrafficInfoTrafficValueEcEntity;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.traffic.traffics.data.L2CpTrafficReadResponseBody;
import msf.mfcfc.traffic.traffics.data.L2CpTrafficRequest;
import msf.mfcfc.traffic.traffics.data.entity.L2CpTrafficEntity;

/**
 * Implementation class for the L2CP traffic information acquisition.
 *
 * @author NTT
 *
 */
public class FcL2CpTrafficReadScenario extends FcAbstractCpsTrafficScenarioBase<L2CpTrafficRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL2CpTrafficReadScenario.class);

  private L2CpTrafficRequest request;

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
  public FcL2CpTrafficReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(L2CpTrafficRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

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
        FcL2CpDao fcL2CpDao = new FcL2CpDao();
        FcL2CpPK l2CpPk = new FcL2CpPK();
        l2CpPk.setSliceId(request.getSliceId());
        l2CpPk.setCpId(request.getCpId());
        FcL2Cp fcL2Cp = fcL2CpDao.read(session, l2CpPk);

        if (fcL2Cp == null) {

          String logMsg = MessageFormat.format("target resource is not found. parameters={0}, L2CpPk={1}", "L2CP",
              ToStringBuilder.reflectionToString(l2CpPk));
          throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, logMsg);
        }

        FcNodeDao fcNodeDao = new FcNodeDao();

        FcNode fcNode = fcNodeDao.read(session, fcL2Cp.getVlanIf().getId().getNodeInfoId().longValue());

        if (fcNode == null) {

          String logMsg = MessageFormat.format("target resource is not found. parameters={0}, nodeInfoId={1}", "node",
              fcL2Cp.getVlanIf().getId().getNodeInfoId());
          throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, logMsg);
        }

        TrafficInfoCollectTrafficEcResponseBody responseBody = sendTrafficRead(String.valueOf(fcNode.getEcNodeId()));

        L2CpTrafficEntity ifTrafficEntity = getCpTraffic(fcL2Cp.getVlanIf().getId().getVlanIfId(), responseBody);
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

  private L2CpTrafficEntity getCpTraffic(Integer ifId, TrafficInfoCollectTrafficEcResponseBody responseBody)
      throws MsfException {

    L2CpTrafficEntity cpTraffic = null;

    List<TrafficInfoTrafficValueEcEntity> trafficValues = responseBody.getSwitchTraffic().getTrafficValueList();
    if (trafficValues == null) {

      String logMsg = MessageFormat.format("target resource is not found. parameters={0}", "TrafficValues");
      throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, logMsg);
    }

    for (TrafficInfoTrafficValueEcEntity trafficInfo : trafficValues) {
      if (!InterfaceType.VLAN_IF.getMessage().equals(trafficInfo.getIfType())) {
        continue;
      }
      if (!String.valueOf(ifId).equals(trafficInfo.getIfId())) {
        continue;
      }

      cpTraffic = getL2CpTrafficEntity(request.getSliceId(), request.getCpId(), trafficInfo);
      break;
    }

    if (cpTraffic == null) {

      String logMsg = MessageFormat.format(
          "target resource is not found. parameters={0}, sliceId={1}, cpId={2}, ifId={3}", "CpTraffic",
          request.getSliceId(), request.getCpId(), ifId);
      throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, logMsg);
    }
    return cpTraffic;
  }

  private RestResponseBase responseTrafficInfoData(L2CpTrafficEntity cpTraffic) {

    L2CpTrafficReadResponseBody body = new L2CpTrafficReadResponseBody();
    body.setCpTraffic(cpTraffic);
    return createRestResponse(body, HttpStatus.OK_200);
  }

}
