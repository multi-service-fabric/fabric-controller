
package msf.fc.traffic.traffics;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcL3Cp;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcVlanIfPK;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.slices.FcL3CpDao;
import msf.fc.rest.ec.traffic.data.TrafficInfoCollectAllTrafficEcResponseBody;
import msf.fc.rest.ec.traffic.data.entity.TrafficInfoSwitchTrafficCollectAllEcEntity;
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
import msf.mfcfc.traffic.traffics.data.CpTrafficReadListResponseBody;
import msf.mfcfc.traffic.traffics.data.CpTrafficRequest;
import msf.mfcfc.traffic.traffics.data.entity.CpTrafficEntity;

/**
 * Implementation class for CP traffic information list acquisition.
 *
 * @author NTT
 *
 */
public class FcCpTrafficReadListScenario extends FcAbstractCpsTrafficScenarioBase<CpTrafficRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcCpTrafficReadListScenario.class);

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
  public FcCpTrafficReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
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

        TrafficInfoCollectAllTrafficEcResponseBody responseBody = sendTrafficReadList();
        Map<Integer, List<TrafficInfoTrafficValueEcEntity>> trafficInfoMap = getTrafficInfoMap(session,
            responseBody.getSwitchTrafficList());

        List<CpTrafficEntity> ifTraffics = getCpTraffics(session, trafficInfoMap);
        RestResponseBase responseBase = responseTrafficInfoData(ifTraffics);
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

  private Map<Integer, List<TrafficInfoTrafficValueEcEntity>> getTrafficInfoMap(SessionWrapper session,
      List<TrafficInfoSwitchTrafficCollectAllEcEntity> switchTrafficList) throws MsfException {

    Map<Integer, List<TrafficInfoTrafficValueEcEntity>> trafficInfoMap = new HashMap<>();
    if (switchTrafficList == null) {
      return trafficInfoMap;
    }

    FcNodeDao fcNodeDao = new FcNodeDao();

    for (TrafficInfoSwitchTrafficCollectAllEcEntity switchTraffic : switchTrafficList) {
      FcNode fcNode = fcNodeDao.readByEcNodeId(session, Integer.valueOf(switchTraffic.getNodeId()));
      if (fcNode == null) {
        logger.info("Traffic Notify Skipped. Target Node Not Found. TrafficInfoSwitchTrafficCollectAllEcEntity={0}.",
            switchTraffic);
        continue;
      }

      List<TrafficInfoTrafficValueEcEntity> trafficInfoList = switchTraffic.getTrafficValueList();
      if (trafficInfoList == null) {
        continue;
      }
      for (TrafficInfoTrafficValueEcEntity trafficInfo : trafficInfoList) {
        if (!InterfaceType.VLAN_IF.getMessage().equals(trafficInfo.getIfType())) {
          continue;
        }
        Integer nodeInfoId = new Integer(fcNode.getNodeInfoId().toString());
        if (!trafficInfoMap.containsKey(nodeInfoId)) {
          trafficInfoMap.put(nodeInfoId, new ArrayList<TrafficInfoTrafficValueEcEntity>());
        }
        trafficInfoMap.get(nodeInfoId).add(trafficInfo);
      }
    }

    return trafficInfoMap;
  }

  private List<CpTrafficEntity> getCpTraffics(SessionWrapper session,
      Map<Integer, List<TrafficInfoTrafficValueEcEntity>> trafficInfoMap) throws MsfException {

    List<CpTrafficEntity> cpTrafficList = new ArrayList<CpTrafficEntity>();

    String sliceId = request.getSliceId();
    FcL3CpDao fcL3CpDao = new FcL3CpDao();
    List<FcL3Cp> fcL3CpList = fcL3CpDao.readListBySliceId(session, sliceId);

    for (FcL3Cp fcL3Cp : fcL3CpList) {
      TrafficInfoTrafficValueEcEntity trafficInfo = getTrafficInfo(fcL3Cp.getVlanIf().getId(), trafficInfoMap);
      CpTrafficEntity cpTrafficEntity = getCpTrafficEntity(sliceId, fcL3Cp.getId().getCpId(), trafficInfo);
      cpTrafficList.add(cpTrafficEntity);
    }
    return cpTrafficList;
  }

  private TrafficInfoTrafficValueEcEntity getTrafficInfo(FcVlanIfPK fcVlanIfPk,
      Map<Integer, List<TrafficInfoTrafficValueEcEntity>> trafficInfoMap) throws MsfException {

    if (!trafficInfoMap.containsKey(fcVlanIfPk.getNodeInfoId())) {
      String logMsg = MessageFormat.format("There is only the data in the FC system. NodeInfoId={0}",
          fcVlanIfPk.getNodeInfoId());
      throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, logMsg);
    }

    for (TrafficInfoTrafficValueEcEntity trafficInfo : trafficInfoMap.get(fcVlanIfPk.getNodeInfoId())) {
      if (String.valueOf(fcVlanIfPk.getVlanIfId()).equals(trafficInfo.getIfId())) {
        return trafficInfo;
      }
    }
    String logMsg = MessageFormat.format("There is only the data in the FC system. NodeInfoId={0}, VlanIfId={1}",
        fcVlanIfPk.getNodeInfoId(), fcVlanIfPk.getVlanIfId());
    throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, logMsg);
  }

  private RestResponseBase responseTrafficInfoData(List<CpTrafficEntity> cpTraffics) {

    CpTrafficReadListResponseBody body = new CpTrafficReadListResponseBody();
    if (cpTraffics.size() > 0) {
      body.setCpTrafficList(cpTraffics);
    }
    return createRestResponse(body, HttpStatus.OK_200);
  }
}
