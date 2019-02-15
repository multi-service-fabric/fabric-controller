
package msf.fc.services.filter.scenario.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.common.data.FcPhysicalIfFilterInfo;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.filters.FcPhysicalIfFilterInfoDao;
import msf.fc.services.filter.rest.ec.filters.data.PhysicalIfFilterReadDetailListEcResponseBody;
import msf.fc.services.filter.rest.ec.filters.data.entity.PhysicalIfFilterDetailEcEntity;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestFormatOption;
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
import msf.mfcfc.services.filter.scenario.filters.data.PhysicalIfFilterReadDetailListResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.PhysicalIfFilterReadListResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.PhysicalIfFilterRequest;
import msf.mfcfc.services.filter.scenario.filters.data.entity.FilterTermEntity;
import msf.mfcfc.services.filter.scenario.filters.data.entity.PhysicalIfFilterDetailEntity;
import msf.mfcfc.services.filter.scenario.filters.data.entity.PhysicalIfFilterListEntity;

/**
 * Implementation class for the physical IF filter information list acquisition.
 *
 * @author NTT
 *
 */
public class FcPhysicalIfFilterReadListScenario extends FcAbstractFilterScenarioBase<PhysicalIfFilterRequest> {

  private PhysicalIfFilterRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcPhysicalIfFilterReadListScenario.class);

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
  public FcPhysicalIfFilterReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
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

      if (request.getFormat() != null) {
        ParameterCheckUtil.checkNotNull(request.getFormatEnum());
      }

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
            request.getFabricTypeEnum().getCode(), Integer.parseInt(request.getNodeId()));

        responseBase = responsePhysicalIfFilterReadListData(physicalIfFilters, sessionWrapper, request.getFormat());

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

  private RestResponseBase responsePhysicalIfFilterReadListData(List<FcPhysicalIfFilterInfo> physicalIfFilters,
      SessionWrapper sessionWrapper, String format) throws MsfException {
    try {
      logger.methodStart();
      if (!physicalIfFilters.isEmpty()) {

        TreeMap<String, List<FcPhysicalIfFilterInfo>> physicalIfFilterMap = createPhysicalIfFilterMapForPhysicalIfId(
            physicalIfFilters);

        FcNode fcNode = physicalIfFilters.get(0).getPhysicalIf().getNode();

        if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
          PhysicalIfFilterReadDetailListResponseBody body = new PhysicalIfFilterReadDetailListResponseBody();
          List<PhysicalIfFilterDetailEntity> fcPhysicalIfFilterDetailEntitis = new ArrayList<>();
          PhysicalIfFilterReadDetailListEcResponseBody ecResponseBody = sendPhysicalInterfaceFilterReadList(fcNode);

          if (ecResponseBody.getPhysicalIfFilters() != null) {
            for (FcPhysicalIf fcPhysicalIf : fcNode.getPhysicalIfs()) {
              PhysicalIfFilterDetailEntity physicalIfFilter = new PhysicalIfFilterDetailEntity();
              physicalIfFilter.setClusterId(request.getClusterId());
              physicalIfFilter.setFabricType(fcNode.getNodeTypeEnum().getPluralMessage());
              physicalIfFilter.setNodeId(String.valueOf(fcNode.getNodeId()));
              String fcPhysicalIfId = fcPhysicalIf.getPhysicalIfId();
              physicalIfFilter.setPhysicalIfId(fcPhysicalIfId);

              List<FilterTermEntity> terms = new ArrayList<>();
              if (physicalIfFilterMap.containsKey(fcPhysicalIfId)) {
                terms = getPhysicalIfFilterTermEntities(physicalIfFilterMap.get(fcPhysicalIfId),
                    getPhysicalIfFilterEcData(ecResponseBody, fcPhysicalIfId));
              }
              if (CollectionUtils.isNotEmpty(terms)) {
                physicalIfFilter.setTerms(terms);
              } else {

                physicalIfFilter.setTerms(null);
              }
              fcPhysicalIfFilterDetailEntitis.add(physicalIfFilter);
            }
          } else {

            throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
                "There is no appropriate data in the EC system.");
          }

          body.setPhysicalIfFilters(fcPhysicalIfFilterDetailEntitis);
          return createRestResponse(body, HttpStatus.OK_200);
        } else {
          PhysicalIfFilterReadListResponseBody body = new PhysicalIfFilterReadListResponseBody();
          List<PhysicalIfFilterListEntity> fcPhysicalIfFilterListEntities = new ArrayList<>();
          for (FcPhysicalIf fcPhysicalIf : fcNode.getPhysicalIfs()) {
            PhysicalIfFilterListEntity physicalIfFilterListEntity = new PhysicalIfFilterListEntity();
            String fcPhysicalIfId = fcPhysicalIf.getPhysicalIfId();
            physicalIfFilterListEntity.setPhysicalIfId(fcPhysicalIfId);
            List<String> termIds = new ArrayList<>();
            if (physicalIfFilterMap.containsKey(fcPhysicalIfId)) {

              for (FcPhysicalIfFilterInfo fcPhysicalIfFilterInfo : physicalIfFilterMap.get(fcPhysicalIfId)) {
                termIds.add(fcPhysicalIfFilterInfo.getId().getTermId());
              }
            }
            physicalIfFilterListEntity.setTermIds(termIds);
            fcPhysicalIfFilterListEntities.add(physicalIfFilterListEntity);
          }
          body.setPhysicalIfFilters(fcPhysicalIfFilterListEntities);
          return createRestResponse(body, HttpStatus.OK_200);
        }
      } else {

        FcNode fcNode = getNode(sessionWrapper, new FcNodeDao(), request.getFabricTypeEnum().getCode(),
            Integer.parseInt(request.getNodeId()));
        if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
          PhysicalIfFilterReadDetailListResponseBody body = new PhysicalIfFilterReadDetailListResponseBody();
          List<PhysicalIfFilterDetailEntity> fcPhysicalIfFilterDetailEntitis = new ArrayList<>();

          for (FcPhysicalIf fcPhysicalIf : fcNode.getPhysicalIfs()) {
            PhysicalIfFilterDetailEntity physicalIfFilter = new PhysicalIfFilterDetailEntity();
            physicalIfFilter.setClusterId(request.getClusterId());
            physicalIfFilter.setFabricType(fcNode.getNodeTypeEnum().getPluralMessage());
            physicalIfFilter.setNodeId(String.valueOf(fcNode.getNodeId()));
            physicalIfFilter.setPhysicalIfId(fcPhysicalIf.getPhysicalIfId());

            physicalIfFilter.setTerms(null);
            fcPhysicalIfFilterDetailEntitis.add(physicalIfFilter);
          }
          body.setPhysicalIfFilters(fcPhysicalIfFilterDetailEntitis);
          return createRestResponse(body, HttpStatus.OK_200);
        } else {
          PhysicalIfFilterReadListResponseBody body = new PhysicalIfFilterReadListResponseBody();
          List<PhysicalIfFilterListEntity> fcPhysicalIfFilterListEntities = new ArrayList<>();

          for (FcPhysicalIf fcPhysicalIf : fcNode.getPhysicalIfs()) {
            PhysicalIfFilterListEntity physicalIfFilterListEntity = new PhysicalIfFilterListEntity();
            physicalIfFilterListEntity.setPhysicalIfId(fcPhysicalIf.getPhysicalIfId());

            physicalIfFilterListEntity.setTermIds(new ArrayList<>());
            fcPhysicalIfFilterListEntities.add(physicalIfFilterListEntity);
          }
          body.setPhysicalIfFilters(fcPhysicalIfFilterListEntities);
          return createRestResponse(body, HttpStatus.OK_200);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private TreeMap<String, List<FcPhysicalIfFilterInfo>> createPhysicalIfFilterMapForPhysicalIfId(
      List<FcPhysicalIfFilterInfo> physicalIfFilters) throws MsfException {
    try {
      logger.methodStart();
      TreeMap<String, List<FcPhysicalIfFilterInfo>> physicalIfFilterMap = new TreeMap<>();
      for (FcPhysicalIfFilterInfo fcPhysicalIfFilterInfo : physicalIfFilters) {
        String physicalIfId = fcPhysicalIfFilterInfo.getPhysicalIf().getPhysicalIfId();

        List<FcPhysicalIfFilterInfo> fcPhysicalIfFilterInfos;
        if (physicalIfFilterMap.containsKey(physicalIfId)) {
          fcPhysicalIfFilterInfos = physicalIfFilterMap.get(physicalIfId);
        } else {
          fcPhysicalIfFilterInfos = new ArrayList<>();
        }
        fcPhysicalIfFilterInfos.add(fcPhysicalIfFilterInfo);

        physicalIfFilterMap.put(physicalIfId, fcPhysicalIfFilterInfos);
      }
      return physicalIfFilterMap;
    } finally {
      logger.methodEnd();
    }
  }

  private PhysicalIfFilterReadDetailListEcResponseBody sendPhysicalInterfaceFilterReadList(FcNode node)
      throws MsfException {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();

      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(
          EcRequestUri.PHYSICAL_IF_FILTER_READ_LIST.getHttpMethod(),
          EcRequestUri.PHYSICAL_IF_FILTER_READ_LIST.getUri(String.valueOf(node.getEcNodeId())), null,
          ecControlIpAddress, ecControlPort);

      PhysicalIfFilterReadDetailListEcResponseBody ifFilterReadDetailListEcResponseBody = JsonUtil.fromJson(
          restResponseBase.getResponseBody(), PhysicalIfFilterReadDetailListEcResponseBody.class,
          ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          ifFilterReadDetailListEcResponseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return ifFilterReadDetailListEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private PhysicalIfFilterDetailEcEntity getPhysicalIfFilterEcData(
      PhysicalIfFilterReadDetailListEcResponseBody ecResponseBody, String fcPhysicalIfId) throws MsfException {
    try {
      logger.methodStart();
      for (PhysicalIfFilterDetailEcEntity physicalIfFilterDetailEcEntity : ecResponseBody.getPhysicalIfFilters()) {
        if (fcPhysicalIfId.equals(physicalIfFilterDetailEcEntity.getPhysicalIfId())) {
          return physicalIfFilterDetailEcEntity;
        }
      }

      throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, "There is no appropriate data in the EC system.");
    } finally {
      logger.methodEnd();
    }
  }

}
