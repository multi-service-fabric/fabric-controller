
package msf.fc.slice.cps;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcEdgePoint;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcVlanIf;
import msf.fc.db.dao.clusters.FcEdgePointDao;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.slices.FcVlanIfDao;
import msf.fc.rest.ec.core.operation.data.OperationResponseBody;
import msf.fc.rest.ec.node.interfaces.vlan.data.VlanIfReadEcResponseBody;
import msf.fc.rest.ec.node.interfaces.vlan.data.VlanIfReadListEcResponseBody;
import msf.fc.rest.ec.node.nodes.data.NodeReadEcResponseBody;
import msf.fc.rest.ec.node.nodes.data.NodeReadListEcResponseBody;
import msf.mfcfc.common.constant.EcEmControlStatus;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.EcControlStatusUtil;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.slice.cps.AbstractCpRunnerBase;

/**
 * Abstract class to implement the common process of the asynchronous runner
 * processing in the CP management.
 *
 * @author NTT
 *
 */
public abstract class FcAbstractCpRunnerBase extends AbstractCpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractCpRunnerBase.class);

  protected static final int MULTIPLY_VALUE_FOR_RD_CALCULATION_ = 1000;

  protected VlanIfReadEcResponseBody getVlanIf(SessionWrapper sessionWrapper, Long nodeInfoId, Integer vlanIfId)
      throws MsfException {
    try {
      logger.methodStart();
      FcNodeDao nodeDao = new FcNodeDao();
      FcNode node = nodeDao.read(sessionWrapper, nodeInfoId);

      String ecNodeId = String.valueOf(node.getEcNodeId());
      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.VLAN_IF_READ.getHttpMethod(),
          EcRequestUri.VLAN_IF_READ.getUri(ecNodeId, String.valueOf(vlanIfId)), null,
          FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlAddress(),
          FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort());
      VlanIfReadEcResponseBody body = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          VlanIfReadEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200, body.getErrorCode(),
          ErrorCode.EC_CONTROL_ERROR);
      return body;
    } finally {
      logger.methodEnd();
    }
  }

  protected VlanIfReadListEcResponseBody getVlanIfList(SessionWrapper sessionWrapper, Long nodeInfoId)
      throws MsfException {
    try {
      logger.methodStart();
      FcNodeDao nodeDao = new FcNodeDao();
      FcNode node = nodeDao.read(sessionWrapper, nodeInfoId);

      String ecNodeId = String.valueOf(node.getEcNodeId());
      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.VLAN_IF_READ_LIST.getHttpMethod(),
          EcRequestUri.VLAN_IF_READ_LIST.getUri(ecNodeId), null,
          FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlAddress(),
          FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort());
      VlanIfReadListEcResponseBody body = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          VlanIfReadListEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200, body.getErrorCode(),
          ErrorCode.EC_CONTROL_ERROR);
      return body;
    } finally {
      logger.methodEnd();
    }
  }

  protected FcEdgePoint getEdgePointFromDb(SessionWrapper sessionWrapper, int edgePointId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "edgePointId" },
          new Object[] { sessionWrapper, edgePointId });
      FcEdgePointDao edgePointDao = new FcEdgePointDao();
      return edgePointDao.read(sessionWrapper, edgePointId);
    } finally {
      logger.methodEnd();
    }
  }

  protected InterfaceType getIfType(FcEdgePoint edgePoint) throws MsfException {
    try {
      logger.methodStart(new String[] { "edgePoint" }, new Object[] { edgePoint });
      if (edgePoint.getPhysicalIf() != null) {
        return InterfaceType.PHYSICAL_IF;
      } else if (edgePoint.getLagIf() != null) {
        return InterfaceType.LAG_IF;
      } else {
        return InterfaceType.BREAKOUT_IF;
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected String getIfId(FcEdgePoint edgePoint) throws MsfException {
    try {
      logger.methodStart(new String[] { "edgePoint" }, new Object[] { edgePoint });
      switch (getIfType(edgePoint)) {
        case PHYSICAL_IF:
          return edgePoint.getPhysicalIf().getPhysicalIfId();
        case LAG_IF:
          return String.valueOf(edgePoint.getLagIf().getLagIfId());
        case BREAKOUT_IF:
        default:
          return edgePoint.getBreakoutIf().getBreakoutIfId();
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void commitTransaction(SessionWrapper sessionWrapper, RestResponseBase restResponse,
      int expectHttpStatusCode, boolean isCommit) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "restResponse" },
          new Object[] { sessionWrapper, restResponse });

      if (restResponse.getHttpStatusCode() == expectHttpStatusCode) {
        if (isCommit) {
          sessionWrapper.commit(EcEmControlStatus.SUCCESS);
        } else {
          sessionWrapper.rollback();
        }
      } else {

        OperationResponseBody responseBody = JsonUtil.fromJson(restResponse.getResponseBody(),
            OperationResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

        EcEmControlStatus ecEmControlStatus = EcControlStatusUtil.getStatusFromEcErrorCode(responseBody.getErrorCode());
        switch (ecEmControlStatus) {
          case EM_SUCCESS_BUT_EC_FAILED:
            if (isCommit) {

              sessionWrapper.commit(ecEmControlStatus);
            } else {
              sessionWrapper.rollback();
            }
            String logMsg1 = MessageFormat.format("ecErrorCode = {0}", responseBody.getErrorCode());
            logger.error(logMsg1);
            throw new MsfException(ErrorCode.EC_CONTROL_ERROR_EM_CONTROL_COMPLETED, logMsg1);
          case FAILED:

            String logMsg2 = MessageFormat.format("ecErrorCode = {0}", responseBody.getErrorCode());
            logger.error(logMsg2);
            throw new MsfException(ErrorCode.EC_CONTROL_ERROR, logMsg2);
          default:
            String logMsg3 = MessageFormat.format("unexpected ec status. status = {0}.", ecEmControlStatus.name());
            logger.error(logMsg3);
            throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg3);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void commitTransaction(SessionWrapper sessionWrapper, RestResponseBase restResponse,
      int expectHttpStatusCode) throws MsfException {
    commitTransaction(sessionWrapper, restResponse, expectHttpStatusCode, true);
  }

  protected RestResponseBase sendRequestToEc(String requestJson, EcRequestUri ecReuqestUri, String... uriParams)
      throws MsfException {
    try {
      logger.methodStart();
      RestRequestBase requestBase = new RestRequestBase();
      requestBase.setRequestBody(requestJson);
      RestResponseBase restResponse = RestClient.sendRequest(ecReuqestUri.getHttpMethod(),
          ecReuqestUri.getUri(uriParams), requestBase,
          FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlAddress(),
          FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort());
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

  protected FcNode getNodeAndCheck(SessionWrapper sessionWrapper, int edgePointId) throws MsfException {
    try {
      logger.methodStart();
      FcNodeDao nodeDao = new FcNodeDao();
      FcNode node = nodeDao.read(sessionWrapper, edgePointId);

      checkNodePresence(node, edgePointId);
      return node;
    } finally {
      logger.methodEnd();
    }
  }

  protected Set<String> createVlanIfIdSet(SessionWrapper sessionWrapper, Long nodeInfoId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "nodeInfoId" }, new Object[] { sessionWrapper, nodeInfoId });
      FcVlanIfDao vlanIfDao = new FcVlanIfDao();
      List<FcVlanIf> vlanIfList = vlanIfDao.readList(sessionWrapper, nodeInfoId);
      Set<String> vlanIfIdSet = new TreeSet<>();
      for (FcVlanIf vlanIf : vlanIfList) {
        vlanIfIdSet.add(String.valueOf(vlanIf.getId().getVlanIfId()));
      }
      return vlanIfIdSet;
    } finally {
      logger.methodEnd();
    }
  }

  protected NodeReadEcResponseBody getNodeFromEc(SessionWrapper sessionWrapper, Long nodeInfoId) throws MsfException {
    try {
      logger.methodStart();
      FcNodeDao nodeDao = new FcNodeDao();
      FcNode node = nodeDao.read(sessionWrapper, nodeInfoId);

      String ecNodeId = String.valueOf(node.getEcNodeId());
      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.NODE_READ.getHttpMethod(),
          EcRequestUri.NODE_READ.getUri(ecNodeId), null,
          FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlAddress(),
          FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort());
      NodeReadEcResponseBody body = JsonUtil.fromJson(restResponseBase.getResponseBody(), NodeReadEcResponseBody.class,
          ErrorCode.EC_CONTROL_ERROR);
      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200, body.getErrorCode(),
          ErrorCode.EC_CONTROL_ERROR);
      return body;
    } finally {
      logger.methodEnd();
    }
  }

  protected NodeReadListEcResponseBody getNodeListFromEc() throws MsfException {
    try {
      logger.methodStart();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.NODE_READ_LIST.getHttpMethod(),
          EcRequestUri.NODE_READ_LIST.getUri(), null,
          FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlAddress(),
          FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort());
      NodeReadListEcResponseBody body = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          NodeReadListEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200, body.getErrorCode(),
          ErrorCode.EC_CONTROL_ERROR);
      return body;
    } finally {
      logger.methodEnd();
    }
  }

}
