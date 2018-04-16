
package msf.mfc.node.clusters;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.system.SwCluster;
import msf.mfc.common.data.MfcClusterLinkIf;
import msf.mfc.common.data.MfcL2Cp;
import msf.mfc.common.data.MfcL2CpPK;
import msf.mfc.common.data.MfcL2Slice;
import msf.mfc.common.data.MfcL3Slice;
import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.MfcDbManager;
import msf.mfc.db.dao.clusters.MfcClusterLinkIfDao;
import msf.mfc.db.dao.clusters.MfcEquipmentDao;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfc.db.dao.slices.MfcL2CpDao;
import msf.mfc.db.dao.slices.MfcL2SliceDao;
import msf.mfc.db.dao.slices.MfcL3SliceDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.HttpMethod;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.PatchOperation;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestRequestData;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.scenario.RestResponseData;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.clusters.AbstractClusterRunnerBase;
import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.cps.l2cp.data.L2CpCreateDeleteRequestBody;
import msf.mfcfc.slice.cps.l2cp.data.entity.L2CpValueEntity;

/**
 * Abstract class to implement the common process of SW cluster-related
 * asynchronous processing in configuration management function.
 *
 * @author NTT
 *
 */
public abstract class MfcAbstractClusterRunnerBase extends AbstractClusterRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcAbstractClusterRunnerBase.class);

  protected Map<Integer, List<L2CpCreateDeleteRequestBody>> fcRequestBodyMap = new HashMap<>();

  protected MfcSwCluster getSwClusterForDelete(SessionWrapper sessionWrapper, MfcSwClusterDao mfcSwClusterDao,
      Integer swClusterId) throws MsfException {
    try {
      logger.methodStart(new String[] { "mfcSwClusterDao", "swClusterId" },
          new Object[] { mfcSwClusterDao, swClusterId });

      MfcSwCluster mfcSwCluster = mfcSwClusterDao.readAfterDeleteProcess(sessionWrapper, swClusterId);
      if (mfcSwCluster == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = swCluster");
      }
      return mfcSwCluster;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase createRestResponse(AbstractResponseBody body, int statusCode) {
    try {
      logger.methodStart(new String[] { "body" }, new Object[] { ToStringBuilder.reflectionToString(body) });

      RestResponseBase response = new RestResponseBase(statusCode, body);
      return response;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase deleteL2Cps(SessionWrapper sessionWrapper, MfcSwCluster swCluster) throws MsfException {
    try {
      logger.methodStart(new String[] { "swCluster" }, new Object[] { swCluster });
      RestResponseBase responseBase = null;
      MfcL2CpDao mfcL2CpDao = new MfcL2CpDao();

      TreeMap<String, List<String>> esiMap = createEsiMapForSliceId(swCluster);

      Entry<String, List<String>> esiListEntry = esiMap.firstEntry();
      if (esiListEntry != null) {
        ArrayList<MfcL2Cp> mfcL2Cps = new ArrayList<>();
        for (String esi : esiListEntry.getValue()) {
          for (MfcL2Cp mfcL2Cp : mfcL2CpDao.readListByEsi(sessionWrapper, esi)) {
            if (mfcL2Cp.getSwCluster().getSwClusterId().equals(swCluster.getSwClusterId())) {
              mfcL2Cps.add(mfcL2Cp);
            }
          }
        }

        setOperationEndFlag(false);

        responseBase = sendL2CpDelete(sessionWrapper, mfcL2Cps, esiListEntry.getKey());
      }
      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private TreeMap<String, List<String>> createEsiMapForSliceId(MfcSwCluster swCluster) throws MsfException {
    try {
      logger.methodStart(new String[] { "swCluster" }, new Object[] { swCluster });
      TreeMap<String, List<String>> esiMap = new TreeMap<>();

      if (swCluster.getL2Cps() != null) {
        for (MfcL2Cp mfcL2Cp : swCluster.getL2Cps()) {
          if (mfcL2Cp.getEsi() != null) {
            String sliceId = mfcL2Cp.getL2Slice().getSliceId();
            if (esiMap.containsKey(sliceId)) {
              List<String> esiList = esiMap.get(sliceId);
              if (esiList.contains(mfcL2Cp.getEsi())) {

                esiList.remove(mfcL2Cp.getEsi());
                if (esiList.isEmpty()) {

                  esiMap.remove(sliceId);
                } else {

                  esiMap.put(sliceId, esiList);
                }
              } else {

                esiList.add(mfcL2Cp.getEsi());
                esiMap.put(sliceId, esiList);
              }
            } else {
              ArrayList<String> esiList = new ArrayList<>();
              esiList.add(mfcL2Cp.getEsi());
              esiMap.put(sliceId, esiList);
            }
          }
        }
      }
      return esiMap;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase sendL2CpDelete(SessionWrapper sessionWrapper, List<MfcL2Cp> mfcL2Cps, String sliceId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "mfcL2Cps", "sliceId" }, new Object[] { mfcL2Cps, sliceId });

      MfcL2Slice l2Slice = getL2SliceAndCheck(sessionWrapper, sliceId);
      List<MfcL2Slice> l2SliceList = new ArrayList<>();
      l2SliceList.add(l2Slice);

      logger.performance("start get l2slice resources lock.");
      MfcDbManager.getInstance().getResourceLock(l2SliceList, new ArrayList<>(), sessionWrapper);
      logger.performance("end get l2slice resources lock.");

      for (MfcL2Cp mfcL2Cp : mfcL2Cps) {
        processDeleteL2Cp(sessionWrapper, mfcL2Cp);
      }
      RestResponseBase responseBase = sendAsyncRequestAndCreateResponse(sliceId);

      sessionWrapper.rollback();

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  protected MfcL2Slice getL2SliceAndCheck(SessionWrapper sessionWrapper, String sliceId) throws MsfException {
    try {
      logger.methodStart();
      MfcL2SliceDao l2SliceDao = new MfcL2SliceDao();
      MfcL2Slice l2Slice = l2SliceDao.read(sessionWrapper, sliceId);

      checkSlicePresence(l2Slice, sliceId);
      return l2Slice;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkSlicePresence(Object sliceEntity, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sliceEntity", "sliceId" }, new Object[] { sliceEntity, sliceId });
      ParameterCheckUtil.checkNotNullRelatedResource(sliceEntity, new String[] { "sliceId" }, new Object[] { sliceId });

    } finally {
      logger.methodEnd();
    }
  }

  private void processDeleteL2Cp(SessionWrapper sessionWrapper, MfcL2Cp l2Cp) throws MsfException {
    try {
      logger.methodStart();

      MfcL2CpDao l2CpDao = new MfcL2CpDao();

      L2CpCreateDeleteRequestBody requestBody = makeL2CpCreateDeleteRequestBodyForDelete(l2Cp.getId().getCpId());
      putL2CpCreateDeleteRequestBodyForMap(l2Cp.getSwCluster().getSwClusterId(), requestBody);

      MfcL2Cp pairL2Cp = getPairCpFromDb(sessionWrapper, l2CpDao, l2Cp);
      processDeleteL2CpForPairCpFound(sessionWrapper, pairL2Cp);
    } finally {
      logger.methodEnd();
    }
  }

  protected MfcL2Cp getPairCpFromDb(SessionWrapper sessionWrapper, MfcL2CpDao l2CpDao, MfcL2Cp l2Cp)
      throws MsfException {
    try {
      logger.methodStart();
      List<MfcL2Cp> pairL2CpList = l2CpDao.readListByEsi(sessionWrapper, l2Cp.getEsi());
      for (MfcL2Cp pairL2Cp : pairL2CpList) {

        if (!pairL2Cp.getId().equals(l2Cp.getId()) && pairL2Cp.getEsi().equals(l2Cp.getEsi())) {
          return pairL2Cp;
        }
      }
      return null;
    } finally {
      logger.methodEnd();
    }
  }

  protected void processDeleteL2CpForPairCpFound(SessionWrapper sessionWrapper, MfcL2Cp pairL2Cp) throws MsfException {
    try {
      logger.methodStart();

      L2CpCreateDeleteRequestBody requestBody = makeL2CpCreateDeleteRequestBodyForUpdate(pairL2Cp.getId().getCpId(),
          "0");
      putL2CpCreateDeleteRequestBodyForMap(pairL2Cp.getSwCluster().getSwClusterId(), requestBody);
    } finally {
      logger.methodEnd();
    }
  }

  protected void putL2CpCreateDeleteRequestBodyForMap(int clusterId, L2CpCreateDeleteRequestBody body) {
    if (fcRequestBodyMap.get(clusterId) == null) {
      fcRequestBodyMap.put(clusterId, new ArrayList<>());
    }
    fcRequestBodyMap.get(clusterId).add(body);
  }

  private L2CpCreateDeleteRequestBody makeL2CpCreateDeleteRequestBodyForDelete(String cpId) {
    try {
      logger.methodStart();
      L2CpCreateDeleteRequestBody body = new L2CpCreateDeleteRequestBody();
      body.setOpEnum(PatchOperation.REMOVE);
      body.setPath("/" + cpId);
      return body;
    } finally {
      logger.methodEnd();
    }
  }

  private L2CpCreateDeleteRequestBody makeL2CpCreateDeleteRequestBodyForUpdate(String cpId, String esi) {
    try {
      logger.methodStart();
      L2CpCreateDeleteRequestBody body = new L2CpCreateDeleteRequestBody();
      body.setOpEnum(PatchOperation.REPLACE);
      body.setPath("/" + cpId);
      L2CpValueEntity entity = new L2CpValueEntity();
      entity.setEsi(esi);
      entity.setLacpSystemId(esi);
      body.setValue(entity);
      return body;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase sendAsyncRequestAndCreateResponse(String sliceId) throws MsfException {
    try {
      logger.methodStart();

      List<RestRequestData> requestDataList = makeSendRequestDataList(sliceId);

      List<RestResponseData> responseDataList = sendRequest(requestDataList, RequestType.REQUEST);

      if (checkResponseAllSuccess(responseDataList)) {
        return new RestResponseBase(HttpStatus.OK_200, (String) null);
      } else {

        return createErrorResponse(responseDataList, null);
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected List<RestRequestData> makeSendRequestDataList(String sliceId) {
    try {
      logger.methodStart();
      List<RestRequestData> requestDataList = new ArrayList<>();

      for (int clusterId : fcRequestBodyMap.keySet()) {
        RestRequestData requestData = new RestRequestData();
        requestData.setClusterId(clusterId);
        requestData.setExpectHttpStatusCode(HttpStatus.ACCEPTED_202);
        requestData.setHttpMethod(HttpMethod.PATCH);
        requestData.setIpAddress(
            MfcConfigManager.getInstance().getSystemConfSwClusterData(clusterId).getSwCluster().getFcControlAddress());
        requestData.setPort(
            MfcConfigManager.getInstance().getSystemConfSwClusterData(clusterId).getSwCluster().getFcControlPort());
        requestData.setTargetUri(MfcFcRequestUri.CP_CREATE_DELETE.getUri(SliceType.L2_SLICE.getMessage(), sliceId));
        RestRequestBase requestBase = new RestRequestBase();
        requestBase.setRequestBody(JsonUtil.toJson(fcRequestBodyMap.get(clusterId)));
        requestData.setRequest(requestBase);
        requestDataList.add(requestData);
      }
      return requestDataList;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase deleteClusterLinkIf(SessionWrapper sessionWrapper, MfcSwCluster swCluster)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "swCluster" }, new Object[] { swCluster });

      RestResponseBase responseBase = null;

      List<MfcClusterLinkIf> clusterLinkIfs = swCluster.getClusterLinkIfs();
      if (CollectionUtils.isNotEmpty(clusterLinkIfs)) {

        setOperationEndFlag(false);

        responseBase = sendClusterLinkInterfaceDelete(clusterLinkIfs.get(0));
      }

      MfcClusterLinkIfDao mfcClusterLinkIfDao = new MfcClusterLinkIfDao();
      List<MfcClusterLinkIf> oppositeClusterLinkIfs = mfcClusterLinkIfDao.readListByOppositeSwClusterId(sessionWrapper,
          String.valueOf(swCluster.getSwClusterId()));

      if (CollectionUtils.isNotEmpty(oppositeClusterLinkIfs)) {

        setOperationEndFlag(false);

        responseBase = sendClusterLinkInterfaceDelete(oppositeClusterLinkIfs.get(0));
      }
      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase sendClusterLinkInterfaceDelete(MfcClusterLinkIf mfcClusterLinkIf) throws MsfException {
    try {
      logger.methodStart(new String[] { "mfcClusterLinkIf" }, new Object[] { mfcClusterLinkIf });

      Integer swClusterId = mfcClusterLinkIf.getSwCluster().getSwClusterId();

      SwCluster swCluster = MfcConfigManager.getInstance().getSystemConfSwClusterData(swClusterId).getSwCluster();
      String fcControlAddress = swCluster.getFcControlAddress();
      int fcControlPort = swCluster.getFcControlPort();

      RestRequestData requestData = new RestRequestData(swClusterId, fcControlAddress, fcControlPort,
          MfcFcRequestUri.CLUSTER_LINK_IF_DELETE.getHttpMethod(), MfcFcRequestUri.CLUSTER_LINK_IF_DELETE
              .getUri(String.valueOf(swClusterId), String.valueOf(mfcClusterLinkIf.getClusterLinkIfId())),
          null, HttpStatus.ACCEPTED_202);
      RestResponseData restResponseData = sendRequest(requestData, RequestType.REQUEST);

      if (restResponseData.getResponse().getHttpStatusCode() != HttpStatus.ACCEPTED_202) {

        String errorMsg = MessageFormat.format("HttpStatusCode = {0}",
            restResponseData.getResponse().getHttpStatusCode());
        logger.error(errorMsg);
        return createErrorResponse(restResponseData);
      }

      return restResponseData.getResponse();
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkSwClusters(SessionWrapper sessionWrapper, MfcSwClusterDao mfcSwClusterDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "mfcSwClusterDao" }, new Object[] { mfcSwClusterDao });

      List<MfcSwCluster> mfcSwClusters = mfcSwClusterDao.readList(sessionWrapper);
      if (mfcSwClusters.isEmpty()) {

        MfcEquipmentDao mfcEquipmentDao = new MfcEquipmentDao();

        mfcEquipmentDao.deleteAll(sessionWrapper);

        MfcL2SliceDao mfcL2SliceDao = new MfcL2SliceDao();
        MfcL3SliceDao mfcL3SliceDao = new MfcL3SliceDao();
        List<MfcL2Slice> mfcL2Slices = mfcL2SliceDao.readList(sessionWrapper);
        List<MfcL3Slice> mfcL3Slices = mfcL3SliceDao.readList(sessionWrapper);

        if (CollectionUtils.isNotEmpty(mfcL2Slices) || CollectionUtils.isNotEmpty(mfcL3Slices)) {

          logger.performance("start get slice resources lock.");
          MfcDbManager.getInstance().getResourceLock(mfcL2Slices, mfcL3Slices, sessionWrapper);
          logger.performance("end get slice resources lock.");

          mfcL2SliceDao.deleteAll(sessionWrapper);

          mfcL3SliceDao.deleteAll(sessionWrapper);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase responseSwClusterDeleteData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.NO_CONTENT_204, (String) null);
    } finally {
      logger.methodEnd();
    }
  }

  protected String getIdFromPath(String path) {
    try {
      logger.methodStart();

      String id = path.replace("/", "");
      if (id.isEmpty()) {
        return null;
      } else {
        return id;
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected MfcL2Cp getL2CpAndCheck(SessionWrapper sessionWrapper, String sliceId, String cpId) throws MsfException {
    try {
      logger.methodStart();
      MfcL2CpDao l2CpDao = new MfcL2CpDao();
      MfcL2CpPK l2CpPk = new MfcL2CpPK();
      l2CpPk.setSliceId(sliceId);
      l2CpPk.setCpId(cpId);
      MfcL2Cp l2Cp = l2CpDao.read(sessionWrapper, l2CpPk);

      checkCpPresence(l2Cp, sliceId, cpId);
      return l2Cp;
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkCpPresence(Object cpEntity, String sliceId, String cpId) throws MsfException {
    try {
      logger.methodStart(new String[] { "cpEntity" }, new Object[] { cpEntity });
      ParameterCheckUtil.checkNotNullTargetResource(cpEntity, new String[] { "sliceId", "cpId" },
          new Object[] { sliceId, cpId });
    } finally {
      logger.methodEnd();
    }
  }

}
