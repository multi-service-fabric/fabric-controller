
package msf.fc.services.filter.scenario.filters;

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.common.data.FcPhysicalIfFilterInfo;
import msf.fc.db.dao.clusters.FcPhysicalIfDao;
import msf.fc.db.dao.filters.FcPhysicalIfFilterInfoDao;
import msf.fc.services.filter.rest.ec.filters.data.PhysicalIfFilterReadDetailEcResponseBody;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeType;
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
import msf.mfcfc.services.filter.scenario.filters.data.PhysicalIfFilterReadResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.PhysicalIfFilterRequest;
import msf.mfcfc.services.filter.scenario.filters.data.entity.PhysicalIfFilterDetailEntity;

/**
 * Implementation class for the physical IF filter information acquisition.
 *
 * @author NTT
 *
 */
public class FcPhysicalIfFilterReadScenario extends FcAbstractFilterScenarioBase<PhysicalIfFilterRequest> {

  private PhysicalIfFilterRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcPhysicalIfFilterReadScenario.class);

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
  public FcPhysicalIfFilterReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(PhysicalIfFilterRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      if (!NodeType.LEAF.equals(NodeType.getEnumFromPluralMessage(request.getFabricType()))) {
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "fabricType = " + request.getFabricType());
      }
      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);
      ParameterCheckUtil.checkIdSpecifiedByUri(request.getIfId());

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
        FcPhysicalIfFilterInfoDao fcPhysicalIfFilterInfoDao = new FcPhysicalIfFilterInfoDao();

        List<FcPhysicalIfFilterInfo> physicalIfFilters = fcPhysicalIfFilterInfoDao.readList(sessionWrapper,
            request.getFabricTypeEnum().getCode(), Integer.parseInt(request.getNodeId()), request.getIfId());

        responseBase = responsePhysicalIfFilterReadData(physicalIfFilters, sessionWrapper);

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

  private RestResponseBase responsePhysicalIfFilterReadData(List<FcPhysicalIfFilterInfo> physicalIfFilters,
      SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();
      PhysicalIfFilterReadResponseBody body = new PhysicalIfFilterReadResponseBody();
      if (!physicalIfFilters.isEmpty()) {

        FcPhysicalIf physicalIf = physicalIfFilters.get(0).getPhysicalIf();
        PhysicalIfFilterReadDetailEcResponseBody ecResponseBody = sendPhysicalInterfaceFilterRead(physicalIf.getNode(),
            physicalIf.getPhysicalIfId());

        PhysicalIfFilterDetailEntity physicalIfFilter = new PhysicalIfFilterDetailEntity();
        physicalIfFilter.setClusterId(request.getClusterId());
        physicalIfFilter.setFabricType(physicalIf.getNode().getNodeTypeEnum().getPluralMessage());
        physicalIfFilter.setNodeId(String.valueOf(physicalIf.getNode().getNodeId()));
        physicalIfFilter.setPhysicalIfId(physicalIf.getPhysicalIfId());
        physicalIfFilter
            .setTerms(getPhysicalIfFilterTermEntities(physicalIfFilters, ecResponseBody.getPhysicalIfFilter()));
        body.setPhysicalIfFilter(physicalIfFilter);
      } else {

        FcPhysicalIf physicalIf = getPhysicalInterface(sessionWrapper, new FcPhysicalIfDao(),
            request.getFabricTypeEnum().getCode(), Integer.parseInt(request.getNodeId()), request.getIfId());

        PhysicalIfFilterDetailEntity physicalIfFilter = new PhysicalIfFilterDetailEntity();
        physicalIfFilter.setClusterId(request.getClusterId());
        physicalIfFilter.setFabricType(physicalIf.getNode().getNodeTypeEnum().getPluralMessage());
        physicalIfFilter.setNodeId(String.valueOf(physicalIf.getNode().getNodeId()));
        physicalIfFilter.setPhysicalIfId(physicalIf.getPhysicalIfId());

        physicalIfFilter.setTerms(null);
        body.setPhysicalIfFilter(physicalIfFilter);
      }

      return createRestResponse(body, HttpStatus.OK_200);
    } finally {
      logger.methodEnd();
    }
  }

  private PhysicalIfFilterReadDetailEcResponseBody sendPhysicalInterfaceFilterRead(FcNode node, String ifId)
      throws MsfException {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();

      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.PHYSICAL_IF_FILTER_READ.getHttpMethod(),
          EcRequestUri.PHYSICAL_IF_FILTER_READ.getUri(String.valueOf(node.getEcNodeId()), ifId), null,
          ecControlIpAddress, ecControlPort);

      PhysicalIfFilterReadDetailEcResponseBody ifFilterReadDetailEcResponseBody = JsonUtil.fromJson(
          restResponseBase.getResponseBody(), PhysicalIfFilterReadDetailEcResponseBody.class,
          ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          ifFilterReadDetailEcResponseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return ifFilterReadDetailEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  protected FcPhysicalIf getPhysicalInterface(SessionWrapper sessionWrapper, FcPhysicalIfDao fcPhysicalIfDao,
      Integer nodeType, Integer nodeId, String physicalIfId) throws MsfException {
    try {
      logger.methodStart();
      FcPhysicalIf fcPhysicalIf = fcPhysicalIfDao.read(sessionWrapper, nodeType, nodeId, physicalIfId);
      if (fcPhysicalIf == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            MessageFormat.format("target resource is not found. nodeType = {0}, nodeId = {1}, physicalIfId = {2}.",
                nodeType, nodeId, physicalIfId));
      }
      return fcPhysicalIf;
    } finally {
      logger.methodEnd();
    }
  }
}
