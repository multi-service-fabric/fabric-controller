
package msf.fc.services.filter.scenario.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcLagIfFilterInfo;
import msf.fc.common.data.FcNode;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.filters.FcLagIfFilterInfoDao;
import msf.fc.services.filter.rest.ec.filters.data.LagIfFilterReadDetailListEcResponseBody;
import msf.fc.services.filter.rest.ec.filters.data.entity.LagIfFilterDetailEcEntity;
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
import msf.mfcfc.services.filter.scenario.filters.data.LagIfFilterReadDetailListResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.LagIfFilterReadListResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.LagIfFilterRequest;
import msf.mfcfc.services.filter.scenario.filters.data.entity.FilterTermEntity;
import msf.mfcfc.services.filter.scenario.filters.data.entity.LagIfFilterDetailEntity;
import msf.mfcfc.services.filter.scenario.filters.data.entity.LagIfFilterListEntity;

/**
 * Implementation class for the LagIF filter information list acquisition.
 *
 * @author NTT
 *
 */
public class FcLagIfFilterReadListScenario extends FcAbstractFilterScenarioBase<LagIfFilterRequest> {

  private LagIfFilterRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcLagIfFilterReadListScenario.class);

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
  public FcLagIfFilterReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(LagIfFilterRequest request) throws MsfException {

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
        FcLagIfFilterInfoDao fcLagIfFilterInfoDao = new FcLagIfFilterInfoDao();

        List<FcLagIfFilterInfo> lagIfFilters = fcLagIfFilterInfoDao.readList(sessionWrapper,
            request.getFabricTypeEnum().getCode(), Integer.parseInt(request.getNodeId()));

        responseBase = responseLagIfFilterReadListData(lagIfFilters, sessionWrapper, request.getFormat());

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

  private RestResponseBase responseLagIfFilterReadListData(List<FcLagIfFilterInfo> lagIfFilters,
      SessionWrapper sessionWrapper, String format) throws MsfException {
    try {
      logger.methodStart();
      if (!lagIfFilters.isEmpty()) {

        TreeMap<String, List<FcLagIfFilterInfo>> lagIfFilterMap = createLagIfFilterMapForLagIfId(lagIfFilters);

        FcNode fcNode = lagIfFilters.get(0).getLagIf().getNode();

        if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
          LagIfFilterReadDetailListResponseBody body = new LagIfFilterReadDetailListResponseBody();
          List<LagIfFilterDetailEntity> fcLagIfFilterDetailEntitis = new ArrayList<>();

          LagIfFilterReadDetailListEcResponseBody ecResponseBody = sendLagInterfaceFilterReadList(fcNode);

          if (ecResponseBody.getLagIfFilters() != null) {
            for (FcLagIf fcLagIf : fcNode.getLagIfs()) {
              LagIfFilterDetailEntity lagIfFilter = new LagIfFilterDetailEntity();
              lagIfFilter.setClusterId(request.getClusterId());
              lagIfFilter.setFabricType(fcNode.getNodeTypeEnum().getPluralMessage());
              lagIfFilter.setNodeId(String.valueOf(fcNode.getNodeId()));
              String fcLagIfId = String.valueOf(fcLagIf.getLagIfId());
              lagIfFilter.setlagIfId(fcLagIfId);

              List<FilterTermEntity> terms = new ArrayList<>();
              if (lagIfFilterMap.containsKey(fcLagIfId)) {
                terms = getLagIfFilterTermEntities(lagIfFilterMap.get(fcLagIfId),
                    getLagIfFilterEcData(ecResponseBody, fcLagIfId));
              }
              if (CollectionUtils.isNotEmpty(terms)) {
                lagIfFilter.setTerms(terms);
              } else {

                lagIfFilter.setTerms(null);
              }
              fcLagIfFilterDetailEntitis.add(lagIfFilter);
            }
          } else {

            throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
                "There is no appropriate data in the EC system.");
          }

          body.setLagIfFilters(fcLagIfFilterDetailEntitis);
          return createRestResponse(body, HttpStatus.OK_200);
        } else {
          LagIfFilterReadListResponseBody body = new LagIfFilterReadListResponseBody();
          List<LagIfFilterListEntity> fcLagIfFilterListEntities = new ArrayList<>();
          for (FcLagIf fcLagIf : fcNode.getLagIfs()) {
            LagIfFilterListEntity lagIfFilterListEntity = new LagIfFilterListEntity();
            String fcLagIfId = String.valueOf(fcLagIf.getLagIfId());
            lagIfFilterListEntity.setLagIfId(fcLagIfId);
            List<String> termIds = new ArrayList<>();
            if (lagIfFilterMap.containsKey(fcLagIfId)) {

              for (FcLagIfFilterInfo fcLagIfFilterInfo : lagIfFilterMap.get(fcLagIfId)) {
                termIds.add(fcLagIfFilterInfo.getId().getTermId());
              }
            }
            lagIfFilterListEntity.setTermIds(termIds);
            fcLagIfFilterListEntities.add(lagIfFilterListEntity);
          }
          body.setLagIfFilter(fcLagIfFilterListEntities);
          return createRestResponse(body, HttpStatus.OK_200);
        }
      } else {

        FcNode fcNode = getNode(sessionWrapper, new FcNodeDao(), request.getFabricTypeEnum().getCode(),
            Integer.parseInt(request.getNodeId()));
        if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
          LagIfFilterReadDetailListResponseBody body = new LagIfFilterReadDetailListResponseBody();
          List<LagIfFilterDetailEntity> fcLagIfFilterDetailEntitis = new ArrayList<>();

          for (FcLagIf fcLagIf : fcNode.getLagIfs()) {
            LagIfFilterDetailEntity lagIfFilter = new LagIfFilterDetailEntity();
            lagIfFilter.setClusterId(request.getClusterId());
            lagIfFilter.setFabricType(fcNode.getNodeTypeEnum().getPluralMessage());
            lagIfFilter.setNodeId(String.valueOf(fcNode.getNodeId()));
            lagIfFilter.setlagIfId(String.valueOf(fcLagIf.getLagIfId()));

            lagIfFilter.setTerms(null);
            fcLagIfFilterDetailEntitis.add(lagIfFilter);
          }
          body.setLagIfFilters(fcLagIfFilterDetailEntitis);
          return createRestResponse(body, HttpStatus.OK_200);
        } else {
          LagIfFilterReadListResponseBody body = new LagIfFilterReadListResponseBody();
          List<LagIfFilterListEntity> fcLagIfFilterListEntities = new ArrayList<>();

          for (FcLagIf fcLagIf : fcNode.getLagIfs()) {
            LagIfFilterListEntity lagIfFilterListEntity = new LagIfFilterListEntity();
            lagIfFilterListEntity.setLagIfId(String.valueOf(fcLagIf.getLagIfId()));

            lagIfFilterListEntity.setTermIds(new ArrayList<>());
            fcLagIfFilterListEntities.add(lagIfFilterListEntity);
          }
          body.setLagIfFilter(fcLagIfFilterListEntities);
          return createRestResponse(body, HttpStatus.OK_200);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private TreeMap<String, List<FcLagIfFilterInfo>> createLagIfFilterMapForLagIfId(List<FcLagIfFilterInfo> lagIfFilters)
      throws MsfException {
    try {
      logger.methodStart();
      TreeMap<String, List<FcLagIfFilterInfo>> lagIfFilterMap = new TreeMap<>();
      for (FcLagIfFilterInfo fcLagIfFilterInfo : lagIfFilters) {
        String lagIfId = String.valueOf(fcLagIfFilterInfo.getLagIf().getLagIfId());

        List<FcLagIfFilterInfo> fcLagIfFilterInfos;
        if (lagIfFilterMap.containsKey(lagIfId)) {
          fcLagIfFilterInfos = lagIfFilterMap.get(lagIfId);
        } else {
          fcLagIfFilterInfos = new ArrayList<>();
        }
        fcLagIfFilterInfos.add(fcLagIfFilterInfo);

        lagIfFilterMap.put(lagIfId, fcLagIfFilterInfos);
      }
      return lagIfFilterMap;
    } finally {
      logger.methodEnd();
    }
  }

  private LagIfFilterReadDetailListEcResponseBody sendLagInterfaceFilterReadList(FcNode node) throws MsfException {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();

      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.LAG_IF_FILTER_READ_LIST.getHttpMethod(),
          EcRequestUri.LAG_IF_FILTER_READ_LIST.getUri(String.valueOf(node.getEcNodeId())), null, ecControlIpAddress,
          ecControlPort);

      LagIfFilterReadDetailListEcResponseBody lagIfFilterReadDetailListEcResponseBody = JsonUtil.fromJson(
          restResponseBase.getResponseBody(), LagIfFilterReadDetailListEcResponseBody.class,
          ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          lagIfFilterReadDetailListEcResponseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return lagIfFilterReadDetailListEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private LagIfFilterDetailEcEntity getLagIfFilterEcData(LagIfFilterReadDetailListEcResponseBody ecResponseBody,
      String fcLagIfId) throws MsfException {
    try {
      logger.methodStart();
      for (LagIfFilterDetailEcEntity lagIfFilterDetailEcEntity : ecResponseBody.getLagIfFilters()) {
        if (fcLagIfId.equals(lagIfFilterDetailEcEntity.getLagIfId())) {
          return lagIfFilterDetailEcEntity;
        }
      }

      throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, "There is no appropriate data in the EC system.");
    } finally {
      logger.methodEnd();
    }
  }

}
