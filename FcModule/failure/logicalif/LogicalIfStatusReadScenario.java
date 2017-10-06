package msf.fc.failure.logicalif;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ActiveStatus;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.InternalLinkIf;
import msf.fc.common.data.L2Cp;
import msf.fc.common.data.L3Cp;
import msf.fc.common.data.Node;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.InternalLinkIfDao;
import msf.fc.db.dao.clusters.SwClusterDao;
import msf.fc.db.dao.slices.L2CpDao;
import msf.fc.db.dao.slices.L3CpDao;
import msf.fc.failure.logicalif.data.LogicalIfStatusReadRequestBody;
import msf.fc.failure.logicalif.data.LogicalIfStatusReadResponseBody;
import msf.fc.failure.logicalif.data.LogicalIfStatusRequest;
import msf.fc.failure.logicalif.data.entity.LogicalIfStatusCpBaseData;
import msf.fc.failure.logicalif.data.entity.LogicalIfStatusData;
import msf.fc.failure.logicalif.data.entity.LogicalIfStatusInternalLinkIfData;
import msf.fc.failure.logicalif.data.entity.LogicalIfStatusNodeData;
import msf.fc.failure.logicalif.data.entity.LogicalIfStatusReadRequestData;
import msf.fc.failure.logicalif.data.entity.LogicalIfStatusSliceData;
import msf.fc.failure.logicalif.data.entity.LogicalIfStatusVpnBaseData;
import msf.fc.rest.common.JsonUtil;

public class LogicalIfStatusReadScenario extends AbstractLogicalIfStatusScenarioBase<LogicalIfStatusRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(LogicalIfStatusReadScenario.class);
  private LogicalIfStatusReadRequestBody requestBody;

  public LogicalIfStatusReadScenario(OperationType operationType, SystemInterfaceType systemIfType) {
    this.syncType = SynchronousType.SYNC;

    this.operationType = operationType;
    this.systemIfType = systemIfType;
  }

  @Override
  protected void checkParameter(LogicalIfStatusRequest request) throws MsfException {
    logger.methodStart();

    try {
      this.requestBody = JsonUtil.fromJson(request.getRequestBody(), LogicalIfStatusReadRequestBody.class);

      this.requestBody.validate();

    } catch (MsfException me) {
      logger.error("[ope_id={0}]: check parameter error.", me, getOperationId());
      throw me;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    logger.methodStart();

    SessionWrapper session = new SessionWrapper();

    try {
      session.openSession();
      LogicalIfStatusReadRequestData readStatusBody = this.requestBody.getReadOptionData();
      String clusterId = readStatusBody.getClusterId();
      LogicalIfStatusData responseReadBody = new LogicalIfStatusData();

      if (new SwClusterDao().read(session, clusterId) == null) {
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            logger.warn("[ope_id={0}]: cluster_id={1}. cluster_id not Exists.", getOperationId(), clusterId));
      }
      responseReadBody.setClusterId(clusterId);

      List<InternalLinkIf> dbInternalLinkIfList = new InternalLinkIfDao().readlListBySwCluster(session, clusterId);
      if (!dbInternalLinkIfList.isEmpty()) {
        responseReadBody.setNodes(createNodesData(dbInternalLinkIfList));
      }

      List<L2Cp> dbL2CpList = new L2CpDao().readListBySwClusterAndCpStatus(session, clusterId,
          ActiveStatus.ACTIVE.getCode());
      List<L3Cp> dbL3CpList = new L3CpDao().readListBySwClusterAndCpStatus(session, clusterId,
          ActiveStatus.ACTIVE.getCode());
      if (!dbL2CpList.isEmpty() || !dbL3CpList.isEmpty()) {
        LogicalIfStatusSliceData slices = new LogicalIfStatusSliceData();
        if (!dbL2CpList.isEmpty()) {
          slices.setL2Vpn(createL2VpnData(dbL2CpList));
        }
        if (!dbL3CpList.isEmpty()) {
          slices.setL3Vpn(createL3VpnData(dbL3CpList));
        }
        responseReadBody.setSlices(slices);
      }

      LogicalIfStatusReadResponseBody responseBody = new LogicalIfStatusReadResponseBody();
      responseBody.setReadResponse(responseReadBody);
      return new RestResponseBase(HttpStatus.OK_200, responseBody);

    } catch (MsfException me) {
      logger.error("[ope_id={0}]: {1}", me, getOperationId(), me.getMessage());
      throw me;
    } finally {
      session.closeSession();
      logger.methodEnd();
    }
  }

  private List<LogicalIfStatusNodeData> createNodesData(List<InternalLinkIf> dbInternalLinkIfList) throws MsfException {
    logger.methodStart();

    try {
      HashMap<String, HashMap<String, LogicalIfStatusNodeData>> internalLinkIfDataMap = new HashMap<>();
      for (InternalLinkIf dbInternalLinkIf : dbInternalLinkIfList) {
        LogicalIfStatusInternalLinkIfData internalLinkIfData = new LogicalIfStatusInternalLinkIfData();
        internalLinkIfData.setInternalLinkIfId(dbInternalLinkIf.getInternalLinkIfId().toString());
        internalLinkIfData.setStatusEnum(dbInternalLinkIf.getOperationStatusEnum());

        Node node = dbInternalLinkIf.getLagIf().getNode();
        String nodeType = node.getNodeTypeEnum().getSingularMessage();
        String nodeId = node.getNodeId().toString();

        HashMap<String, LogicalIfStatusNodeData> nodeTypeMap;
        nodeTypeMap = internalLinkIfDataMap.get(nodeType);
        if (nodeTypeMap == null) {
          nodeTypeMap = new HashMap<>();
          internalLinkIfDataMap.put(nodeType, nodeTypeMap);
        }

        LogicalIfStatusNodeData nodeData;
        nodeData = nodeTypeMap.get(nodeId);
        if (nodeData == null) {
          nodeData = new LogicalIfStatusNodeData(nodeType, nodeId, new ArrayList<LogicalIfStatusInternalLinkIfData>());
          nodeTypeMap.put(nodeId, nodeData);
        }
        nodeData.getInternalLinkIfs().add(internalLinkIfData);
      }

      List<LogicalIfStatusNodeData> nodes = new ArrayList<>();
      for (HashMap<String, LogicalIfStatusNodeData> internalIfData : internalLinkIfDataMap.values()) {
        nodes.addAll(internalIfData.values());
      }

      return nodes;
    } finally {
      logger.methodEnd();
    }
  }

  private List<LogicalIfStatusVpnBaseData> createL2VpnData(List<L2Cp> dbL2CpList) throws MsfException {
    logger.methodStart();

    try {
      HashMap<String, LogicalIfStatusVpnBaseData> l2VpnDataMap = new HashMap<>();

      for (L2Cp dbL2Cp : dbL2CpList) {
        LogicalIfStatusCpBaseData cpData = new LogicalIfStatusCpBaseData();
        cpData.setCpId(dbL2Cp.getId().getCpId());
        cpData.setStatusEnum(dbL2Cp.getOperationStatusEnum());
        String sliceId = dbL2Cp.getL2Slice().getSliceId();

        LogicalIfStatusVpnBaseData l2vpn = l2VpnDataMap.get(sliceId);
        if (l2vpn == null) {
          l2vpn = new LogicalIfStatusVpnBaseData(sliceId, new ArrayList<LogicalIfStatusCpBaseData>());
          l2VpnDataMap.put(sliceId, l2vpn);
        }
        l2vpn.getCps().add(cpData);
      }

      return new ArrayList<LogicalIfStatusVpnBaseData>(l2VpnDataMap.values());

    } finally {
      logger.methodEnd();
    }

  }

  private List<LogicalIfStatusVpnBaseData> createL3VpnData(List<L3Cp> dbL3CpList) throws MsfException {
    logger.methodStart();

    try {
      HashMap<String, LogicalIfStatusVpnBaseData> l3VpnDataMap = new HashMap<>();

      for (L3Cp dbL3Cp : dbL3CpList) {
        LogicalIfStatusCpBaseData cpData = new LogicalIfStatusCpBaseData();
        cpData.setCpId(dbL3Cp.getId().getCpId());
        cpData.setStatusEnum(dbL3Cp.getOperationStatusEnum());
        String sliceId = dbL3Cp.getL3Slice().getSliceId();

        LogicalIfStatusVpnBaseData l3vpn = l3VpnDataMap.get(sliceId);
        if (l3vpn == null) {
          l3vpn = new LogicalIfStatusVpnBaseData(sliceId, new ArrayList<LogicalIfStatusCpBaseData>());
          l3VpnDataMap.put(sliceId, l3vpn);
        }
        l3vpn.getCps().add(cpData);
      }

      return new ArrayList<LogicalIfStatusVpnBaseData>(l3VpnDataMap.values());

    } finally {
      logger.methodEnd();
    }

  }

}
