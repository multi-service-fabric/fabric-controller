
package msf.mfc.failure.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.system.SwCluster;
import msf.mfc.common.data.MfcClusterLinkIf;
import msf.mfc.common.data.MfcL2Cp;
import msf.mfc.common.data.MfcL2Slice;
import msf.mfc.common.data.MfcL3Cp;
import msf.mfc.common.data.MfcL3Slice;
import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.dao.clusters.MfcClusterLinkIfDao;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfc.db.dao.slices.MfcL2SliceDao;
import msf.mfc.db.dao.slices.MfcL3SliceDao;
import msf.mfc.failure.search.MfcFailurePathSearcher;
import msf.mfcfc.common.constant.ClusterType;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.FailureStatus;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.constant.SliceUnitReachableOppositeType;
import msf.mfcfc.common.constant.SliceUnitReachableStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestRequestData;
import msf.mfcfc.core.scenario.RestResponseData;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.failure.search.CpData;
import msf.mfcfc.failure.status.data.AbstractFailureStatusScenarioBase;
import msf.mfcfc.failure.status.data.FailureStatusNotifyRequestBody;
import msf.mfcfc.failure.status.data.FailureStatusReadListResponseBody;
import msf.mfcfc.failure.status.data.entity.FailureStatusClusterFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusReachableStatusFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceClusterLinkFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceClusterLinkReachableStatusEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceUnitEntity;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Abstract class to implement the common process in failure management
 * function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class MfcAbstractFailureStatusScenarioBase<T extends RestRequestBase>
    extends AbstractFailureStatusScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcAbstractFailureStatusScenarioBase.class);

  protected List<MfcSwCluster> getSwClusterList(SessionWrapper sessionWrapper, MfcSwClusterDao mfcSwClusterDao)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "mfcSwClusterDao" }, new Object[] { mfcSwClusterDao });
      List<MfcSwCluster> mfcSwClusters = mfcSwClusterDao.readList(sessionWrapper);
      return mfcSwClusters;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<RestResponseData> sendFailureReadListRequestForFc(SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();
      MfcSwClusterDao mfcSwClusterDao = new MfcSwClusterDao();
      List<MfcSwCluster> mfcSwClusters = getSwClusterList(sessionWrapper, mfcSwClusterDao);
      List<RestRequestData> requestDataList = new ArrayList<>();

      if (mfcSwClusters.isEmpty()) {
        return new ArrayList<RestResponseData>();
      }

      for (MfcSwCluster mfcSwCluster : mfcSwClusters) {

        SwCluster swCluster = MfcConfigManager.getInstance()
            .getSystemConfSwClusterData(Integer.valueOf(mfcSwCluster.getSwClusterId())).getSwCluster();
        String fcControlAddress = swCluster.getFcControlAddress();
        int fcControlPort = swCluster.getFcControlPort();

        RestRequestData requestData = new RestRequestData(Integer.valueOf(mfcSwCluster.getSwClusterId()),
            fcControlAddress, fcControlPort, MfcFcRequestUri.FAILURE_READ_LIST.getHttpMethod(),
            MfcFcRequestUri.FAILURE_READ_LIST.getUri(), null, HttpStatus.OK_200);

        requestDataList.add(requestData);
      }
      List<RestResponseData> restResponseDataList = sendRequest(requestDataList, RequestType.REQUEST);
      return restResponseDataList;
    } finally {
      logger.methodEnd();
    }
  }

  protected Map<Integer, FailureStatusReadListResponseBody> createFailureStatusFromFcMap(
      List<RestResponseData> restResponseDataList) throws MsfException {
    Map<Integer, FailureStatusReadListResponseBody> failureStatusFromFcMap = new HashMap<>();

    for (RestResponseData restResponseData : restResponseDataList) {
      FailureStatusReadListResponseBody failureStatusReadListResponseBody = JsonUtil.fromJson(
          restResponseData.getResponse().getResponseBody(), FailureStatusReadListResponseBody.class,
          ErrorCode.FC_CONTROL_ERROR);
      failureStatusFromFcMap.put(restResponseData.getRequest().getClusterId(), failureStatusReadListResponseBody);
    }
    return failureStatusFromFcMap;
  }

  @SuppressWarnings("unchecked")
  protected FailureStatusSliceUnitEntity createAllSliceNotifyEntity(SessionWrapper sessionWrapper,
      Map<Integer, FailureStatusReadListResponseBody> failureStatusFromFcMap,
      FailureStatusNotifyRequestBody notifyRequestBody) throws MsfException {
    try {
      logger.methodStart(new String[] { "failureStatusFromFcMap", "notifyRequestBody" },
          new Object[] { failureStatusFromFcMap, notifyRequestBody });

      if (failureStatusFromFcMap == null || failureStatusFromFcMap.isEmpty()) {
        return null;
      }

      FailureStatusSliceUnitEntity failureStatusSliceUnitEntity = searchFailurePath(sessionWrapper,
          failureStatusFromFcMap);

      if (notifyRequestBody != null) {
        logger.debug("check what slice unit failure changed.");

        FailureStatusSliceUnitEntity responseSliceUnitEntity = new FailureStatusSliceUnitEntity();
        responseSliceUnitEntity.setSliceList(new ArrayList<>());

        if (isContainsClusterLinkIfFailure(notifyRequestBody)) {
          logger.debug("notification contains cluster unit failure.");

          Map<Integer, FailureStatusReadListResponseBody> failureStatusFromFcMapCp = (Map<Integer, FailureStatusReadListResponseBody>) deepCopy(
              failureStatusFromFcMap);

          Map<Integer, FailureStatusReadListResponseBody> failureStatusFromFcMapBeforeNotify = createFailureStatusReadMapBeforeNotify(
              failureStatusFromFcMapCp, notifyRequestBody);

          FailureStatusSliceUnitEntity failureStatusSliceUnitEntityBeforeNotify = searchFailurePath(sessionWrapper,
              failureStatusFromFcMapBeforeNotify);

          FailureStatusSliceUnitEntity diffEntity = takeDiffBetweenBeforeAndAfterNotify(
              failureStatusSliceUnitEntityBeforeNotify, failureStatusSliceUnitEntity, false);

          for (FailureStatusSliceFailureEntity sliceFailureEntity : diffEntity.getSliceList()) {
            addSliceUnitFailure(responseSliceUnitEntity, sliceFailureEntity);
          }

        }

        if (isContainsSliceUnitFailure(notifyRequestBody)) {
          logger.debug("notification contains slice unit failure.");

          Map<Integer, FailureStatusReadListResponseBody> failureStatusFromFcMapCp = (Map<Integer, FailureStatusReadListResponseBody>) deepCopy(
              failureStatusFromFcMap);

          if (isContainsClusterLinkIfFailure(notifyRequestBody)) {
            createFailureStatusReadMapBeforeNotify(failureStatusFromFcMapCp, notifyRequestBody);
          }

          for (FailureStatusSliceFailureEntity sliceFailureNotify : notifyRequestBody.getSliceUnit().getSliceList()) {
            if (!checkCpsOverCluster(sessionWrapper, sliceFailureNotify)) {
              logger.debug("cps are exist only in one cluster. sliceType = {0}, sliceId = {1}",
                  sliceFailureNotify.getSliceType(), sliceFailureNotify.getSliceId());

              addSliceUnitFailure(responseSliceUnitEntity, sliceFailureNotify);
            } else {

              boolean changedReachableBetweenCps = false;

              for (FailureStatusReachableStatusFailureEntity reachableEntityNotify : sliceFailureNotify
                  .getReachableStatusList()) {

                if (reachableEntityNotify.getOppositeTypeEnum()
                    .equals(SliceUnitReachableOppositeType.CLUSTER_LINK_IF)) {
                  setOppositeReachabilityForBetweenCpAndClusterLinkIf(sliceFailureNotify.getSliceId(),
                      sliceFailureNotify.getSliceTypeEnum(), reachableEntityNotify.getCpId(),
                      reachableEntityNotify.getOppositeId(), failureStatusFromFcMapCp);
                } else {

                  if (!changedReachableBetweenCps) {
                    setOppositeReachabilityForBetweenCps(sliceFailureNotify.getSliceId(),
                        sliceFailureNotify.getSliceTypeEnum(), reachableEntityNotify.getCpId(),
                        reachableEntityNotify.getOppositeId(), sliceFailureNotify.getFailureStatusEnum(),
                        failureStatusFromFcMapCp);
                    changedReachableBetweenCps = true;
                  }
                }
              }
            }
          }

          FailureStatusSliceUnitEntity failureStatusSliceUnitEntityBeforeNotify = searchFailurePath(sessionWrapper,
              failureStatusFromFcMapCp);

          FailureStatusSliceUnitEntity diffEntity = takeDiffBetweenBeforeAndAfterNotify(
              failureStatusSliceUnitEntityBeforeNotify, failureStatusSliceUnitEntity, false);

          for (FailureStatusSliceFailureEntity sliceFailureEntity : diffEntity.getSliceList()) {
            addSliceUnitFailure(responseSliceUnitEntity, sliceFailureEntity);
          }

        }

        if (responseSliceUnitEntity.getSliceList().size() == 0) {
          return null;
        } else {
          return responseSliceUnitEntity;
        }
      } else {
        return failureStatusSliceUnitEntity;
      }

    } finally {
      logger.methodEnd();
    }
  }

  private boolean isContainsClusterLinkIfFailure(FailureStatusNotifyRequestBody notifyRequestBody) {
    if (notifyRequestBody.getClusterUnit() != null) {
      for (FailureStatusClusterFailureEntity entity : notifyRequestBody.getClusterUnit().getClusterList()) {
        if (entity.getClusterTypeEnum().equals(ClusterType.CLUSTER_LINK_IF)) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean isContainsSliceUnitFailure(FailureStatusNotifyRequestBody notifyRequestBody) {

    if (notifyRequestBody.getSliceUnit() != null) {

      if (!notifyRequestBody.getSliceUnit().getSliceList().isEmpty()) {
        return true;
      }

      if (notifyRequestBody.getSliceUnit().getClusterLink() != null
          && !notifyRequestBody.getSliceUnit().getClusterLink().getReachableStatusList().isEmpty()) {
        return true;
      }

      return false;
    } else {
      return false;
    }
  }

  private boolean checkCpsOverCluster(SessionWrapper sessionWrapper, FailureStatusSliceFailureEntity sliceFailure)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sliceFailure" }, new Object[] { sliceFailure });
      Set<Integer> clusterIdSet = new HashSet<>();
      if (sliceFailure.getSliceTypeEnum().equals(SliceType.L2_SLICE)) {
        MfcL2SliceDao l2SliceDao = new MfcL2SliceDao();
        MfcL2Slice l2Slice = l2SliceDao.read(sessionWrapper, sliceFailure.getSliceId());
        for (MfcL2Cp l2Cp : l2Slice.getL2Cps()) {
          clusterIdSet.add(l2Cp.getSwCluster().getSwClusterId());
        }
      } else {
        MfcL3SliceDao l3SliceDao = new MfcL3SliceDao();
        MfcL3Slice l3Slice = l3SliceDao.read(sessionWrapper, sliceFailure.getSliceId());
        for (MfcL3Cp l3Cp : l3Slice.getL3Cps()) {
          clusterIdSet.add(l3Cp.getSwCluster().getSwClusterId());
        }
      }

      if (clusterIdSet.size() > 1) {
        return true;
      } else {
        return false;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void setOppositeReachabilityForBetweenCpAndClusterLinkIf(String sliceId, SliceType sliceType, String cpId,
      String oppositeClusterLinkIfId, Map<Integer, FailureStatusReadListResponseBody> failureStatusFromFcMap) {
    try {
      logger.methodStart(new String[] { "sliceId", "sliceType", "cpId", "oppositeClusterLinkIfId" },
          new Object[] { sliceId, sliceType, cpId, oppositeClusterLinkIfId });

      for (FailureStatusReadListResponseBody responseBody : failureStatusFromFcMap.values()) {
        for (FailureStatusSliceFailureEntity sliceFailureEntity : responseBody.getSliceUnit().getSliceList()) {

          if (sliceId.equals(sliceFailureEntity.getSliceId())
              && sliceType.equals(sliceFailureEntity.getSliceTypeEnum())) {
            for (FailureStatusReachableStatusFailureEntity reachableEntity : sliceFailureEntity
                .getReachableStatusList()) {

              if (reachableEntity.getCpId().equals(cpId)
                  && reachableEntity.getOppositeTypeEnum().equals(SliceUnitReachableOppositeType.CLUSTER_LINK_IF)
                  && reachableEntity.getOppositeId().equals(oppositeClusterLinkIfId)) {
                if (reachableEntity.getReachableStatusEnum().equals(SliceUnitReachableStatus.REACHABLE)) {

                  reachableEntity.setReachableStatusEnum(SliceUnitReachableStatus.UNREACHABLE);
                } else {

                  reachableEntity.setReachableStatusEnum(SliceUnitReachableStatus.REACHABLE);
                }

                return;
              }
            }
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void setOppositeReachabilityForBetweenCps(String sliceId, SliceType sliceType, String cpId,
      String oppositeCpId, FailureStatus sliceFailureStatusNotify,
      Map<Integer, FailureStatusReadListResponseBody> failureStatusFromFcMap) {
    try {
      logger.methodStart(new String[] { "cpId", "oppositeCpId", "sliceFailureStatusNotify", "failureStatusFromFcMap" },
          new Object[] { cpId, oppositeCpId, sliceFailureStatusNotify, failureStatusFromFcMap });

      for (FailureStatusReadListResponseBody responseBody : failureStatusFromFcMap.values()) {
        for (FailureStatusSliceFailureEntity sliceFailureEntity : responseBody.getSliceUnit().getSliceList()) {

          boolean isFound = false;

          if (sliceFailureEntity.getSliceId().equals(sliceId)
              && sliceFailureEntity.getSliceTypeEnum().equals(sliceType)) {
            for (FailureStatusReachableStatusFailureEntity reachableEntity : sliceFailureEntity
                .getReachableStatusList()) {

              if (reachableEntity.getCpId().equals(cpId)
                  && reachableEntity.getOppositeTypeEnum().equals(SliceUnitReachableOppositeType.CP)
                  && reachableEntity.getOppositeId().equals(oppositeCpId)) {
                isFound = true;
                break;
              }
            }
          }

          if (isFound) {
            for (FailureStatusReachableStatusFailureEntity reachableEntity : sliceFailureEntity
                .getReachableStatusList()) {
              if (reachableEntity.getOppositeTypeEnum().equals(SliceUnitReachableOppositeType.CP)) {
                if (sliceFailureStatusNotify.equals(FailureStatus.UP)) {

                  reachableEntity.setReachableStatusEnum(SliceUnitReachableStatus.UNREACHABLE);
                } else {

                  reachableEntity.setReachableStatusEnum(SliceUnitReachableStatus.REACHABLE);
                }
              }
            }

            return;
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void addSliceUnitFailure(FailureStatusSliceUnitEntity responseEntity,
      FailureStatusSliceFailureEntity sliceFailureEntity) {
    boolean containsSliceFailure = false;
    for (FailureStatusSliceFailureEntity entity : responseEntity.getSliceList()) {
      if (entity.getSliceId().equals(sliceFailureEntity.getSliceId())
          && entity.getSliceType().equals(sliceFailureEntity.getSliceType())) {
        containsSliceFailure = true;
      }
    }

    if (!containsSliceFailure) {

      FailureStatusSliceFailureEntity cpcpReachableOnlyEntity = removeReachableToClusterLinkIf(sliceFailureEntity);

      if (cpcpReachableOnlyEntity.getReachableStatusList().size() != 0) {
        responseEntity.getSliceList().add(cpcpReachableOnlyEntity);
      } else {
        logger.debug("target slice only has reachability to cluster link if. slice = {0}", sliceFailureEntity);
      }
    }
  }

  private Map<MfcClusterLinkIf, FailureStatus> createClusterLinkIfStatusMap(SessionWrapper sessionWrapper,
      Map<Integer, FailureStatusReadListResponseBody> failureStatusFromFcMap) throws MsfException {
    Map<MfcClusterLinkIf, FailureStatus> clusterLinkIfStatusMap = new HashMap<>();
    MfcClusterLinkIfDao mfcClusterLinkIfDao = new MfcClusterLinkIfDao();
    for (FailureStatusReadListResponseBody responseBody : failureStatusFromFcMap.values()) {

      if (responseBody.getClusterUnit() != null) {
        for (FailureStatusClusterFailureEntity clusterFailureEntity : responseBody.getClusterUnit().getClusterList()) {

          if (clusterFailureEntity.getClusterTypeEnum().equals(ClusterType.CLUSTER_LINK_IF)) {

            MfcClusterLinkIf mfcClusterLinkIf = mfcClusterLinkIfDao.read(sessionWrapper,
                Integer.valueOf(clusterFailureEntity.getId()));

            if (mfcClusterLinkIf != null) {
              clusterLinkIfStatusMap.put(mfcClusterLinkIf, clusterFailureEntity.getFailureStatusEnum());
            } else {

              logger.warn("cluster link if record is not found. id = {0}", clusterFailureEntity.getId());
            }
          }
        }
      }
    }
    return clusterLinkIfStatusMap;
  }

  private Map<Integer, Map<CpData, SliceUnitReachableStatus>> createClusterCpReachabilityMap(
      Map<Integer, FailureStatusReadListResponseBody> failureStatusFromFcMap) {
    Map<Integer, Map<CpData, SliceUnitReachableStatus>> clusterCpReachabilityMap = new HashMap<>();
    for (FailureStatusReadListResponseBody responseBody : failureStatusFromFcMap.values()) {

      if (responseBody.getSliceUnit() != null) {
        for (FailureStatusSliceFailureEntity sliceFailureEntity : responseBody.getSliceUnit().getSliceList()) {

          if (sliceFailureEntity.getReachableStatusList() != null) {
            for (FailureStatusReachableStatusFailureEntity statusFailure : sliceFailureEntity
                .getReachableStatusList()) {

              if (statusFailure.getOppositeTypeEnum().equals(SliceUnitReachableOppositeType.CLUSTER_LINK_IF)) {
                int clusterLinkIfId = Integer.valueOf(statusFailure.getOppositeId());
                clusterCpReachabilityMap.putIfAbsent(clusterLinkIfId, new HashMap<>());
                Map<CpData, SliceUnitReachableStatus> cpDataMap = clusterCpReachabilityMap.get(clusterLinkIfId);
                CpData cpData = new CpData(sliceFailureEntity.getSliceId(), sliceFailureEntity.getSliceTypeEnum(),
                    statusFailure.getCpId());
                cpDataMap.put(cpData, statusFailure.getReachableStatusEnum());
              }
            }
          }
        }
      }
    }
    return clusterCpReachabilityMap;
  }

  private FailureStatusSliceUnitEntity mergeFailureStatusSliceUnitEntity(
      Map<Integer, FailureStatusReadListResponseBody> failureStatusFromFcMap) {
    FailureStatusSliceUnitEntity entity = new FailureStatusSliceUnitEntity();
    List<FailureStatusSliceFailureEntity> sliceFailureEntityList = new ArrayList<>();
    List<FailureStatusSliceClusterLinkReachableStatusEntity> clusterLinkReachableStatusList = new ArrayList<>();

    for (FailureStatusReadListResponseBody responseBody : failureStatusFromFcMap.values()) {

      if (responseBody.getSliceUnit() != null) {

        if (responseBody.getSliceUnit().getSliceList() != null) {
          sliceFailureEntityList.addAll(responseBody.getSliceUnit().getSliceList());
        }

        if (responseBody.getSliceUnit().getClusterLink() != null) {
          if (responseBody.getSliceUnit().getClusterLink().getReachableStatusList() != null) {
            clusterLinkReachableStatusList
                .addAll(responseBody.getSliceUnit().getClusterLink().getReachableStatusList());
          }
        }
      }
    }
    FailureStatusSliceClusterLinkFailureEntity clusterLinkFailureEntity = new FailureStatusSliceClusterLinkFailureEntity();
    clusterLinkFailureEntity.setReachableStatusList(clusterLinkReachableStatusList);

    entity.setSliceList(sliceFailureEntityList);
    entity.setClusterLink(clusterLinkFailureEntity);
    return entity;
  }

  private FailureStatusSliceUnitEntity searchFailurePath(SessionWrapper sessionWrapper,
      Map<Integer, FailureStatusReadListResponseBody> failureStatusFromFcMap) throws MsfException {
    try {
      logger.methodStart(new String[] { "failureStatusFromFcMap" }, new Object[] { failureStatusFromFcMap });

      Map<MfcClusterLinkIf, FailureStatus> clusterLinkIfStatusMap = createClusterLinkIfStatusMap(sessionWrapper,
          failureStatusFromFcMap);

      Map<Integer, Map<CpData, SliceUnitReachableStatus>> clusterCpReachabilityMap = createClusterCpReachabilityMap(
          failureStatusFromFcMap);

      FailureStatusSliceUnitEntity failureStatusSliceUnitEntityFromFc = mergeFailureStatusSliceUnitEntity(
          failureStatusFromFcMap);

      MfcFailurePathSearcher mfcFailurePathSearcher = new MfcFailurePathSearcher();

      FailureStatusSliceUnitEntity failureStatusSliceUnitEntity = mfcFailurePathSearcher.searchFailurePath(
          sessionWrapper, clusterLinkIfStatusMap, clusterCpReachabilityMap, failureStatusSliceUnitEntityFromFc);
      return failureStatusSliceUnitEntity;
    } finally {
      logger.methodEnd();
    }
  }

  private Map<Integer, FailureStatusReadListResponseBody> createFailureStatusReadMapBeforeNotify(
      Map<Integer, FailureStatusReadListResponseBody> failureStatusFromFcMap,
      FailureStatusNotifyRequestBody notifyRequestBody) {
    try {
      logger.methodStart(new String[] { "failureStatusFromFcMap", "notifyRequestBody" },
          new Object[] { failureStatusFromFcMap, notifyRequestBody });

      for (FailureStatusReadListResponseBody responseBody : failureStatusFromFcMap.values()) {
        for (FailureStatusClusterFailureEntity clusterFailureNotify : notifyRequestBody.getClusterUnit()
            .getClusterList()) {

          if (responseBody.getClusterUnit() != null) {
            for (FailureStatusClusterFailureEntity clusterFailure : responseBody.getClusterUnit().getClusterList()) {

              if (clusterFailureNotify.getClusterId().equals(clusterFailure.getClusterId())
                  && clusterFailureNotify.getClusterTypeEnum().equals(ClusterType.CLUSTER_LINK_IF)
                  && clusterFailureNotify.getType().equals(clusterFailure.getType())
                  && clusterFailureNotify.getId().equals(clusterFailure.getId())) {

                if (clusterFailureNotify.getFailureStatusEnum().equals(FailureStatus.UP)) {
                  clusterFailure.setFailureStatusEnum(FailureStatus.DOWN);

                  setStatusUnreachable(responseBody, clusterFailureNotify.getId());
                } else {
                  clusterFailure.setFailureStatusEnum(FailureStatus.UP);
                }
              }
            }
          }
        }
      }
      return failureStatusFromFcMap;
    } finally {
      logger.methodEnd();
    }
  }

  private void setStatusUnreachable(FailureStatusReadListResponseBody responseBody, String clusterLinkIfId) {
    if (responseBody.getSliceUnit() == null) {
      return;
    }
    for (FailureStatusSliceFailureEntity sliceFailureEntity : responseBody.getSliceUnit().getSliceList()) {

      if (sliceFailureEntity.getReachableStatusList() != null) {
        for (FailureStatusReachableStatusFailureEntity reachableStatus : sliceFailureEntity.getReachableStatusList()) {

          if (reachableStatus.getOppositeTypeEnum().equals(SliceUnitReachableOppositeType.CLUSTER_LINK_IF)
              && reachableStatus.getOppositeId().equals(clusterLinkIfId)) {

            reachableStatus.setReachableStatusEnum(SliceUnitReachableStatus.UNREACHABLE);
          }
        }
      }
    }
  }

  private FailureStatusSliceFailureEntity removeReachableToClusterLinkIf(FailureStatusSliceFailureEntity targetEntity) {
    FailureStatusSliceFailureEntity copyEntity = (FailureStatusSliceFailureEntity) deepCopy(targetEntity);

    Iterator<FailureStatusReachableStatusFailureEntity> iterator = copyEntity.getReachableStatusList().iterator();
    while (iterator.hasNext()) {
      FailureStatusReachableStatusFailureEntity entity = iterator.next();

      if (entity.getOppositeTypeEnum().equals(SliceUnitReachableOppositeType.CLUSTER_LINK_IF)) {

        iterator.remove();
      }
    }
    return copyEntity;
  }

}
