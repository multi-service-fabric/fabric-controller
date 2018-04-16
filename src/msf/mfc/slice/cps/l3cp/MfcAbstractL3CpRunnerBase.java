
package msf.mfc.slice.cps.l3cp;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.data.MfcL3Cp;
import msf.mfc.common.data.MfcL3CpPK;
import msf.mfc.common.data.MfcL3Slice;
import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.dao.slices.MfcL3CpDao;
import msf.mfc.db.dao.slices.MfcL3SliceDao;
import msf.mfc.slice.cps.MfcAbstractCpRunnerBase;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.HttpMethod;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.PatchOperation;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestRequestData;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.cps.l3cp.data.L3CpCreateDeleteRequestBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpReadResponseBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpBgpEntity;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpQosCreateEntity;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpStaticRouteEntity;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpValueEntity;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpVrrpEntity;

/**
 * Abstract class to implement the common process of L3CP-related asynchronous
 * runner processing in slice management function.
 *
 * @author NTT
 *
 */
public abstract class MfcAbstractL3CpRunnerBase extends MfcAbstractCpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcAbstractL3CpRunnerBase.class);

  protected L3CpRequest request;

  protected Map<Integer, List<L3CpCreateDeleteRequestBody>> fcRequestBodyMap = new HashMap<>();

  protected MfcL3Slice getL3SliceAndCheck(SessionWrapper sessionWrapper, String sliceId) throws MsfException {
    try {
      logger.methodStart();
      MfcL3SliceDao l3SliceDao = new MfcL3SliceDao();
      MfcL3Slice l3Slice = l3SliceDao.read(sessionWrapper, sliceId);

      checkSlicePresence(l3Slice, sliceId);
      return l3Slice;
    } finally {
      logger.methodEnd();
    }
  }

  protected String getCpIdAndCheck(SessionWrapper sessionWrapper, String sliceId, String cpId,
      List<String> requestCpIdList) throws MsfException {
    try {
      logger.methodStart();
      MfcL3CpDao l3CpDao = new MfcL3CpDao();

      if (cpId == null) {

        List<MfcL3Cp> l3CpList = l3CpDao.readListBySliceId(sessionWrapper, sliceId);
        Set<String> cpIdSet = createCpIdSet(l3CpList);
        if (requestCpIdList != null) {
          cpIdSet.addAll(requestCpIdList);
        }
        return String.valueOf(getNextCpId(sessionWrapper, cpIdSet, sliceId, SliceType.L3_SLICE));
      } else {
        checkCpDuplicate(sessionWrapper, l3CpDao, sliceId, cpId);
        return cpId;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private Set<String> createCpIdSet(List<MfcL3Cp> l3CpList) {
    try {
      logger.methodStart(new String[] { "l3CpList" }, new Object[] { l3CpList });
      Set<String> cpIdSet = new TreeSet<>();
      for (MfcL3Cp l3Cp : l3CpList) {
        cpIdSet.add(l3Cp.getId().getCpId());
      }
      return cpIdSet;
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkCpDuplicate(SessionWrapper sessionWrapper, MfcL3CpDao l3CpDao, String sliceId, String cpId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l3CpDao", "sliceId", "cpId" },
          new Object[] { sessionWrapper, l3CpDao, sliceId, cpId });
      MfcL3CpPK l3CpPk = new MfcL3CpPK();
      l3CpPk.setSliceId(sliceId);
      l3CpPk.setCpId(cpId);
      MfcL3Cp l3Cp = l3CpDao.read(sessionWrapper, l3CpPk);
      if (l3Cp != null) {
        String logMsg = MessageFormat.format("sliceId = {0}, cpId = {1}", sliceId, cpId);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  protected List<RestRequestData> makeSendRequestDataList(String sliceId, Map<Integer, String> lowerOperationIdMap) {
    try {
      logger.methodStart(new String[] { "sliceId", "lowerOperationIdMap" },
          new Object[] { sliceId, lowerOperationIdMap });
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
        requestData.setTargetUri(MfcFcRequestUri.CP_CREATE_DELETE.getUri(SliceType.L3_SLICE.getMessage(), sliceId));
        RestRequestBase requestBase = new RestRequestBase();
        requestBase.setRequestBody(JsonUtil.toJson(fcRequestBodyMap.get(clusterId)));
        requestData.setRequest(requestBase);

        if (lowerOperationIdMap != null) {
          requestData.setLowerOperationId(lowerOperationIdMap.get(clusterId));
        }
        requestDataList.add(requestData);
      }
      return requestDataList;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<RestRequestData> makeSendRequestDataList(String sliceId) {
    return makeSendRequestDataList(sliceId, null);
  }

  protected void processCreateL3Cp(SessionWrapper sessionWrapper, MfcL3Slice l3Slice, String cpId, String clusterId,
      String edgePointId, int vlanId, int mtu, String ipv4Address, String ipv6Address, Integer ipv4Prefix,
      Integer ipv6Prefix, L3CpBgpEntity bgpEntity, List<L3CpStaticRouteEntity> staticRouteEntityList,
      L3CpVrrpEntity vrrpEntity, Double trafficThreshold, L3CpQosCreateEntity qosEntity) throws MsfException {
    try {
      logger.methodStart();

      L3CpCreateDeleteRequestBody requestBody = makeL3CpCreateDeleteRequestBody(PatchOperation.ADD, cpId, clusterId,
          edgePointId, vlanId, mtu, ipv4Address, ipv6Address, ipv4Prefix, ipv6Prefix, bgpEntity, staticRouteEntityList,
          vrrpEntity, trafficThreshold, qosEntity);
      putL3CpCreateDeleteRequestBodyForMap(Integer.valueOf(clusterId), requestBody);
    } finally {
      logger.methodEnd();
    }
  }

  protected L3CpCreateDeleteRequestBody makeL3CpCreateDeleteRequestBody(PatchOperation op, String cpId,
      String clusterId, String edgePointId, Integer vlanId, Integer mtu, String ipv4Address, String ipv6Address,
      Integer ipv4Prefix, Integer ipv6Prefix, L3CpBgpEntity bgpEntity,
      List<L3CpStaticRouteEntity> staticRouteEntityList, L3CpVrrpEntity vrrpEntity, Double trafficThreshold,
      L3CpQosCreateEntity qosEntity) {
    try {
      logger.methodStart();
      L3CpCreateDeleteRequestBody body = new L3CpCreateDeleteRequestBody();
      body.setOpEnum(op);
      body.setPath("/" + cpId);
      if (op.equals(PatchOperation.ADD)) {
        L3CpValueEntity entity = new L3CpValueEntity();
        entity.setClusterId(clusterId);
        entity.setEdgePointId(edgePointId);
        entity.setIpv4Address(ipv4Address);
        entity.setIpv4Prefix(ipv4Prefix);
        entity.setIpv6Address(ipv6Address);
        entity.setIpv6Prefix(ipv6Prefix);
        entity.setMtu(mtu);
        entity.setQos(qosEntity);
        entity.setTrafficThreshold(trafficThreshold);
        entity.setVlanId(vlanId);
        entity.setBgp(bgpEntity);
        entity.setStaticRouteList(staticRouteEntityList);
        entity.setVrrp(vrrpEntity);
        body.setValue(entity);
      }
      return body;
    } finally {
      logger.methodEnd();
    }
  }

  protected L3CpCreateDeleteRequestBody makeL3CpCreateDeleteRequestBodyForDelete(String cpId) {
    return makeL3CpCreateDeleteRequestBody(PatchOperation.REMOVE, cpId, null, null, null, null, null, null, null, null,
        null, null, null, null, null);
  }

  protected void putL3CpCreateDeleteRequestBodyForMap(int clusterId, L3CpCreateDeleteRequestBody body) {
    if (fcRequestBodyMap.get(clusterId) == null) {
      fcRequestBodyMap.put(clusterId, new ArrayList<>());
    }
    fcRequestBodyMap.get(clusterId).add(body);
  }

  protected MfcL3Cp makeNewL3Cp(MfcL3Slice l3Slice, String cpId, String clusterId) throws MsfException {
    try {
      logger.methodStart();
      MfcL3Cp newL3Cp = new MfcL3Cp();
      MfcL3CpPK l3CpPk = new MfcL3CpPK();
      l3CpPk.setSliceId(l3Slice.getSliceId());
      l3CpPk.setCpId(cpId);
      newL3Cp.setId(l3CpPk);
      newL3Cp.setL3Slice(l3Slice);
      MfcSwCluster swCluster = new MfcSwCluster();
      swCluster.setSwClusterId(Integer.valueOf(clusterId));
      newL3Cp.setSwCluster(swCluster);

      return newL3Cp;
    } finally {
      logger.methodEnd();
    }
  }

  protected MfcL3Cp getL3CpAndCheck(SessionWrapper sessionWrapper, String sliceId, String cpId) throws MsfException {
    try {
      logger.methodStart();
      MfcL3CpDao l3CpDao = new MfcL3CpDao();
      MfcL3CpPK l3CpPk = new MfcL3CpPK();
      l3CpPk.setSliceId(sliceId);
      l3CpPk.setCpId(cpId);
      MfcL3Cp l3Cp = l3CpDao.read(sessionWrapper, l3CpPk);

      checkCpPresence(l3Cp, sliceId, cpId);
      return l3Cp;
    } finally {
      logger.methodEnd();
    }
  }

  protected void processDeleteL3Cp(MfcL3Cp l3Cp, boolean needRollbackData) throws MsfException {
    try {
      logger.methodStart();

      if (needRollbackData) {

        RestResponseBase response = getCpDataFromFc(SliceType.L3_SLICE, l3Cp.getId().getSliceId(),
            l3Cp.getId().getCpId(), l3Cp.getSwCluster().getSwClusterId());

        rollbackData.addL3CpEntity(JsonUtil
            .fromJson(response.getResponseBody(), L3CpReadResponseBody.class, ErrorCode.UNDEFINED_ERROR).getL3Cp());
      }

      L3CpCreateDeleteRequestBody requestBody = makeL3CpCreateDeleteRequestBodyForDelete(l3Cp.getId().getCpId());
      putL3CpCreateDeleteRequestBodyForMap(l3Cp.getSwCluster().getSwClusterId(), requestBody);
    } finally {
      logger.methodEnd();
    }
  }

}
