
package msf.fc.services.filter.scenario.filters;

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcCpFilterInfo;
import msf.fc.common.data.FcL2Cp;
import msf.fc.common.data.FcL2CpPK;
import msf.fc.common.data.FcNode;
import msf.fc.db.dao.filters.FcCpFilterInfoDao;
import msf.fc.db.dao.slices.FcL2CpDao;
import msf.fc.services.filter.rest.ec.filters.data.VlanIfFilterReadDetailEcResponseBody;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.services.filter.common.constant.EcRequestUri;
import msf.mfcfc.services.filter.scenario.filters.data.L2CpFilterReadResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.L2CpFilterRequest;
import msf.mfcfc.services.filter.scenario.filters.data.entity.L2CpFilterDetailEntity;

/**
 * Implementation class for the L2CP filter information acquisition.
 *
 * @author NTT
 *
 */
public class FcL2CpFilterReadScenario extends FcAbstractFilterScenarioBase<L2CpFilterRequest> {

  private L2CpFilterRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcL2CpFilterReadScenario.class);

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
  public FcL2CpFilterReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(L2CpFilterRequest request) throws MsfException {

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

      RestResponseBase responseBase = null;
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        sessionWrapper.openSession();
        FcCpFilterInfoDao fcCpFilterInfoDao = new FcCpFilterInfoDao();

        List<FcCpFilterInfo> cpFilters = fcCpFilterInfoDao.readList(sessionWrapper, request.getSliceId(),
            request.getCpId());

        responseBase = responseL2CpFilterReadData(cpFilters, sessionWrapper);

      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseL2CpFilterReadData(List<FcCpFilterInfo> cpFilters, SessionWrapper sessionWrapper)
      throws MsfException {
    try {
      logger.methodStart();
      L2CpFilterReadResponseBody body = new L2CpFilterReadResponseBody();
      if (!cpFilters.isEmpty()) {

        FcL2Cp l2Cp = cpFilters.get(0).getL2Cp();

        FcNode fcNode = null;
        if (l2Cp.getEdgePoint().getLagIf() != null) {
          fcNode = l2Cp.getEdgePoint().getLagIf().getNode();
        } else if (l2Cp.getEdgePoint().getPhysicalIf() != null) {
          fcNode = l2Cp.getEdgePoint().getPhysicalIf().getNode();
        } else {
          fcNode = l2Cp.getEdgePoint().getBreakoutIf().getNode();
        }
        VlanIfFilterReadDetailEcResponseBody ecResponseBody = sendVlanInterfaceFilterRead(fcNode,
            String.valueOf(l2Cp.getVlanIf().getId().getVlanIfId()));

        L2CpFilterDetailEntity l2CpFilterDetailEntity = new L2CpFilterDetailEntity();
        l2CpFilterDetailEntity.setSliceId(l2Cp.getL2Slice().getSliceId());
        l2CpFilterDetailEntity.setCpId(l2Cp.getId().getCpId());
        l2CpFilterDetailEntity.setTerms(getL2CpFilterTermEntities(cpFilters, ecResponseBody.getVlanIfFilter()));
        body.setCpFilter(l2CpFilterDetailEntity);
      } else {

        FcL2Cp l2Cp = getL2Cp(sessionWrapper, new FcL2CpDao(), request.getSliceId(), request.getCpId());

        L2CpFilterDetailEntity l2CpFilterDetailEntity = new L2CpFilterDetailEntity();
        l2CpFilterDetailEntity.setSliceId(l2Cp.getL2Slice().getSliceId());
        l2CpFilterDetailEntity.setCpId(l2Cp.getId().getCpId());

        l2CpFilterDetailEntity.setTerms(null);
        body.setCpFilter(l2CpFilterDetailEntity);
      }

      return createRestResponse(body, HttpStatus.OK_200);
    } finally {
      logger.methodEnd();
    }
  }

  private VlanIfFilterReadDetailEcResponseBody sendVlanInterfaceFilterRead(FcNode node, String vlanIfId)
      throws MsfException {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();

      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.VLAN_IF_FILTER_READ.getHttpMethod(),
          EcRequestUri.VLAN_IF_FILTER_READ.getUri(String.valueOf(node.getEcNodeId()), vlanIfId), null,
          ecControlIpAddress, ecControlPort);

      VlanIfFilterReadDetailEcResponseBody vlanIfFilterReadDetailEcResponseBody = JsonUtil.fromJson(
          restResponseBase.getResponseBody(), VlanIfFilterReadDetailEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          vlanIfFilterReadDetailEcResponseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return vlanIfFilterReadDetailEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private FcL2Cp getL2Cp(SessionWrapper sessionWrapper, FcL2CpDao fcL2CpDao, String sliceId, String cpId)
      throws MsfException {
    try {
      logger.methodStart();
      FcL2CpPK pk = new FcL2CpPK();
      pk.setSliceId(sliceId);
      pk.setCpId(cpId);
      FcL2Cp fcL2Cp = fcL2CpDao.read(sessionWrapper, pk);
      if (fcL2Cp == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            MessageFormat.format("target resource is not found. sliceId = {0}, cpId = {1}.", sliceId, cpId));
      }
      return fcL2Cp;
    } finally {
      logger.methodEnd();
    }
  }
}
