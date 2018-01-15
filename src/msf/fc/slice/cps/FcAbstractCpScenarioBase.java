
package msf.fc.slice.cps;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcNode;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.rest.ec.node.interfaces.vlan.data.VlanIfReadEcResponseBody;
import msf.fc.rest.ec.node.interfaces.vlan.data.VlanIfReadListEcResponseBody;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.slice.cps.AbstractCpScenarioBase;

/**
 * Abstract class to implement common process of CP-related processing in slice
 * management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class FcAbstractCpScenarioBase<T extends RestRequestBase> extends AbstractCpScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractCpScenarioBase.class);

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
}
