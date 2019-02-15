
package msf.fc.services.filter.scenario.filters;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcCpFilterInfo;
import msf.fc.common.data.FcL2Cp;
import msf.fc.common.data.FcL2Slice;
import msf.fc.db.dao.filters.FcCpFilterInfoDao;
import msf.fc.db.dao.slices.FcL2SliceDao;
import msf.fc.services.filter.rest.ec.filters.data.VlanIfFilterReadDetailListEcResponseBody;
import msf.fc.services.filter.rest.ec.filters.data.entity.VlanIfFilterDetailEcEntity;
import msf.mfcfc.common.constant.ErrorCode;
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
import msf.mfcfc.services.filter.scenario.filters.data.L2CpFilterReadDetailListResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.L2CpFilterReadListResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.L2CpFilterRequest;
import msf.mfcfc.services.filter.scenario.filters.data.entity.FilterTermEntity;
import msf.mfcfc.services.filter.scenario.filters.data.entity.L2CpFilterDetailEntity;
import msf.mfcfc.services.filter.scenario.filters.data.entity.L2CpFilterListEntity;

/**
 * Implementation class for the L2CP filter information list acquisition.
 *
 * @author NTT
 *
 */
public class FcL2CpFilterReadListScenario extends FcAbstractFilterScenarioBase<L2CpFilterRequest> {

  private L2CpFilterRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcL2CpFilterReadListScenario.class);

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
  public FcL2CpFilterReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(L2CpFilterRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNotNull(request.getSliceId());

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
        FcCpFilterInfoDao fcCpFilterInfoDao = new FcCpFilterInfoDao();

        List<FcCpFilterInfo> cpFilters = fcCpFilterInfoDao.readList(sessionWrapper, request.getSliceId());

        responseBase = responseL2CpFilterReadListData(cpFilters, sessionWrapper, request.getFormat());

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

  private RestResponseBase responseL2CpFilterReadListData(List<FcCpFilterInfo> cpFilters, SessionWrapper sessionWrapper,
      String format) throws MsfException {
    try {
      logger.methodStart();
      if (!cpFilters.isEmpty()) {

        TreeMap<String, List<FcCpFilterInfo>> l2CpFilterMap = createL2CpFilterMapForL2CpId(cpFilters);

        FcL2Slice fcL2Slice = cpFilters.get(0).getL2Cp().getL2Slice();

        if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
          L2CpFilterReadDetailListResponseBody body = new L2CpFilterReadDetailListResponseBody();
          List<L2CpFilterDetailEntity> fcL2CpFilterDetailEntitis = new ArrayList<>();

          TreeSet<Integer> ecNodeIds = getEcNodeIdsForL2Slice(fcL2Slice);

          List<VlanIfFilterDetailEcEntity> vlanIfFilterDetailEcEntities = new ArrayList<>();
          for (Integer ecNodeId : ecNodeIds) {
            VlanIfFilterReadDetailListEcResponseBody ecResponseBody = sendVlanInterfaceFilterReadList(ecNodeId);
            if (CollectionUtils.isNotEmpty(ecResponseBody.getVlanIfFilters())) {

              vlanIfFilterDetailEcEntities.addAll(ecResponseBody.getVlanIfFilters());
            }
          }

          for (FcL2Cp fcL2Cp : fcL2Slice.getL2Cps()) {
            L2CpFilterDetailEntity l2CpFilter = new L2CpFilterDetailEntity();
            l2CpFilter.setSliceId(fcL2Slice.getSliceId());
            String fcCpId = fcL2Cp.getId().getCpId();
            l2CpFilter.setCpId(fcCpId);

            List<FilterTermEntity> terms = new ArrayList<>();
            if (l2CpFilterMap.containsKey(fcCpId)) {
              terms = getL2CpFilterTermEntities(l2CpFilterMap.get(fcCpId),
                  getVlanIfFilterEcData(vlanIfFilterDetailEcEntities, fcL2Cp));
            }
            if (CollectionUtils.isNotEmpty(terms)) {
              l2CpFilter.setTerms(terms);
            } else {

              l2CpFilter.setTerms(null);
            }
            fcL2CpFilterDetailEntitis.add(l2CpFilter);
          }
          body.setCpFilters(fcL2CpFilterDetailEntitis);
          return createRestResponse(body, HttpStatus.OK_200);
        } else {
          L2CpFilterReadListResponseBody body = new L2CpFilterReadListResponseBody();
          List<L2CpFilterListEntity> fcL2CpFilterListEntities = new ArrayList<>();
          for (FcL2Cp fcL2Cp : fcL2Slice.getL2Cps()) {
            L2CpFilterListEntity l2CpFilterListEntity = new L2CpFilterListEntity();
            String fcCpId = fcL2Cp.getId().getCpId();
            l2CpFilterListEntity.setCpId(fcCpId);
            List<String> termIds = new ArrayList<>();
            if (l2CpFilterMap.containsKey(fcCpId)) {

              for (FcCpFilterInfo fcCpFilterInfo : l2CpFilterMap.get(fcCpId)) {
                termIds.add(fcCpFilterInfo.getId().getTermId());
              }
            }
            l2CpFilterListEntity.setTermIds(termIds);
            fcL2CpFilterListEntities.add(l2CpFilterListEntity);
          }
          body.setCpFilters(fcL2CpFilterListEntities);
          return createRestResponse(body, HttpStatus.OK_200);
        }
      } else {

        FcL2Slice fcL2Slice = getL2Slice(sessionWrapper, new FcL2SliceDao(), request.getSliceId());

        if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
          L2CpFilterReadDetailListResponseBody body = new L2CpFilterReadDetailListResponseBody();
          List<L2CpFilterDetailEntity> fcL2CpFilterDetailEntitis = new ArrayList<>();

          for (FcL2Cp fcL2Cp : fcL2Slice.getL2Cps()) {
            L2CpFilterDetailEntity l2CpFilter = new L2CpFilterDetailEntity();
            l2CpFilter.setSliceId(fcL2Slice.getSliceId());
            l2CpFilter.setCpId(fcL2Cp.getId().getCpId());

            l2CpFilter.setTerms(null);
            fcL2CpFilterDetailEntitis.add(l2CpFilter);
          }
          body.setCpFilters(fcL2CpFilterDetailEntitis);
          return createRestResponse(body, HttpStatus.OK_200);
        } else {
          L2CpFilterReadListResponseBody body = new L2CpFilterReadListResponseBody();
          List<L2CpFilterListEntity> fcL2CpFilterListEntities = new ArrayList<>();

          for (FcL2Cp fcL2Cp : fcL2Slice.getL2Cps()) {
            L2CpFilterListEntity l2CpFilterListEntity = new L2CpFilterListEntity();
            l2CpFilterListEntity.setCpId(fcL2Cp.getId().getCpId());

            l2CpFilterListEntity.setTermIds(new ArrayList<>());
            fcL2CpFilterListEntities.add(l2CpFilterListEntity);
          }
          body.setCpFilters(fcL2CpFilterListEntities);
          return createRestResponse(body, HttpStatus.OK_200);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private TreeMap<String, List<FcCpFilterInfo>> createL2CpFilterMapForL2CpId(List<FcCpFilterInfo> cpFilters)
      throws MsfException {
    try {
      logger.methodStart();
      TreeMap<String, List<FcCpFilterInfo>> l2CpFilterMap = new TreeMap<>();
      for (FcCpFilterInfo fcL2CpFilterInfo : cpFilters) {
        String l2CpId = fcL2CpFilterInfo.getL2Cp().getId().getCpId();

        List<FcCpFilterInfo> fcL2CpFilterInfos;
        if (l2CpFilterMap.containsKey(l2CpId)) {
          fcL2CpFilterInfos = l2CpFilterMap.get(l2CpId);
        } else {
          fcL2CpFilterInfos = new ArrayList<>();
        }
        fcL2CpFilterInfos.add(fcL2CpFilterInfo);

        l2CpFilterMap.put(l2CpId, fcL2CpFilterInfos);
      }
      return l2CpFilterMap;
    } finally {
      logger.methodEnd();
    }
  }

  private VlanIfFilterReadDetailListEcResponseBody sendVlanInterfaceFilterReadList(Integer ecNodeId)
      throws MsfException {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();

      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.VLAN_IF_FILTER_READ_LIST.getHttpMethod(),
          EcRequestUri.VLAN_IF_FILTER_READ_LIST.getUri(String.valueOf(ecNodeId)), null, ecControlIpAddress,
          ecControlPort);

      VlanIfFilterReadDetailListEcResponseBody vlanIfFilterReadDetailListEcResponseBody = JsonUtil.fromJson(
          restResponseBase.getResponseBody(), VlanIfFilterReadDetailListEcResponseBody.class,
          ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          vlanIfFilterReadDetailListEcResponseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return vlanIfFilterReadDetailListEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private VlanIfFilterDetailEcEntity getVlanIfFilterEcData(
      List<VlanIfFilterDetailEcEntity> vlanIfFilterDetailEcEntities, FcL2Cp fcL2Cp) throws MsfException {
    try {
      logger.methodStart();

      String vlanIfId = String.valueOf(fcL2Cp.getVlanIf().getId().getVlanIfId());

      String ecNodeId = null;
      if (fcL2Cp.getEdgePoint().getLagIf() != null) {
        ecNodeId = String.valueOf(fcL2Cp.getEdgePoint().getLagIf().getNode().getEcNodeId());
      } else if (fcL2Cp.getEdgePoint().getPhysicalIf() != null) {
        ecNodeId = String.valueOf(fcL2Cp.getEdgePoint().getPhysicalIf().getNode().getEcNodeId());
      } else {
        ecNodeId = String.valueOf(fcL2Cp.getEdgePoint().getBreakoutIf().getNode().getEcNodeId());
      }
      for (VlanIfFilterDetailEcEntity vlanIfFilterDetailEcEntity : vlanIfFilterDetailEcEntities) {
        if ((vlanIfId.equals(vlanIfFilterDetailEcEntity.getVlanIfId()))
            && (ecNodeId.equals(vlanIfFilterDetailEcEntity.getNodeId()))) {
          return vlanIfFilterDetailEcEntity;
        }
      }

      throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, "There is no appropriate data in the EC system.");
    } finally {
      logger.methodEnd();
    }
  }

  private FcL2Slice getL2Slice(SessionWrapper sessionWrapper, FcL2SliceDao fcL2SliceDao, String sliceId)
      throws MsfException {
    try {
      logger.methodStart();
      FcL2Slice fcL2Slice = fcL2SliceDao.read(sessionWrapper, sliceId);
      if (fcL2Slice == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            MessageFormat.format("target resource is not found. sliceId = {0}.", sliceId));
      }
      return fcL2Slice;
    } finally {
      logger.methodEnd();
    }
  }

  private TreeSet<Integer> getEcNodeIdsForL2Slice(FcL2Slice fcL2Slice) throws MsfException {
    try {
      logger.methodStart();
      TreeSet<Integer> ecNodeIds = new TreeSet<>();
      for (FcL2Cp fcL2Cp : fcL2Slice.getL2Cps()) {

        if (fcL2Cp.getEdgePoint().getLagIf() != null) {
          ecNodeIds.add(fcL2Cp.getEdgePoint().getLagIf().getNode().getEcNodeId());
        } else if (fcL2Cp.getEdgePoint().getPhysicalIf() != null) {
          ecNodeIds.add(fcL2Cp.getEdgePoint().getPhysicalIf().getNode().getEcNodeId());
        } else {
          ecNodeIds.add(fcL2Cp.getEdgePoint().getBreakoutIf().getNode().getEcNodeId());
        }
      }
      return ecNodeIds;
    } finally {
      logger.methodEnd();
    }
  }
}
