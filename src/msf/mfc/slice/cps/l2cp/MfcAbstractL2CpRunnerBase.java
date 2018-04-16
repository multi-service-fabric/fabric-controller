
package msf.mfc.slice.cps.l2cp;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.data.MfcL2Cp;
import msf.mfc.common.data.MfcL2CpPK;
import msf.mfc.common.data.MfcL2Slice;
import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.dao.slices.MfcL2CpDao;
import msf.mfc.db.dao.slices.MfcL2SliceDao;
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
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.slice.cps.l2cp.data.L2CpCreateDeleteRequestBody;
import msf.mfcfc.slice.cps.l2cp.data.L2CpReadResponseBody;
import msf.mfcfc.slice.cps.l2cp.data.L2CpRequest;
import msf.mfcfc.slice.cps.l2cp.data.entity.L2CpQosCreateEntity;
import msf.mfcfc.slice.cps.l2cp.data.entity.L2CpValueEntity;

/**
 * Abstract class to implement the common process of L2CP-related asynchronous
 * runner processing in slice management function.
 *
 * @author NTT
 *
 */
public abstract class MfcAbstractL2CpRunnerBase extends MfcAbstractCpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcAbstractL2CpRunnerBase.class);

  protected L2CpRequest request;

  protected Map<Integer, List<L2CpCreateDeleteRequestBody>> fcRequestBodyMap = new HashMap<>();

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

  protected String getCpIdAndCheck(SessionWrapper sessionWrapper, String sliceId, String cpId,
      List<String> requestCpIdList) throws MsfException {
    try {
      logger.methodStart();
      MfcL2CpDao l2CpDao = new MfcL2CpDao();

      if (cpId == null) {

        List<MfcL2Cp> l2CpList = l2CpDao.readListBySliceId(sessionWrapper, sliceId);
        Set<String> cpIdSet = createCpIdSet(l2CpList);
        if (requestCpIdList != null) {
          cpIdSet.addAll(requestCpIdList);
        }
        return String.valueOf(getNextCpId(sessionWrapper, cpIdSet, sliceId, SliceType.L2_SLICE));
      } else {
        checkCpDuplicate(sessionWrapper, l2CpDao, sliceId, cpId);
        return cpId;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private Set<String> createCpIdSet(List<MfcL2Cp> l2CpList) {
    try {
      logger.methodStart(new String[] { "l2CpList" }, new Object[] { l2CpList });
      Set<String> cpIdSet = new TreeSet<>();
      for (MfcL2Cp l2Cp : l2CpList) {
        cpIdSet.add(l2Cp.getId().getCpId());
      }
      return cpIdSet;
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkCpDuplicate(SessionWrapper sessionWrapper, MfcL2CpDao l2CpDao, String sliceId, String cpId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l2CpDao", "sliceId", "cpId" },
          new Object[] { sessionWrapper, l2CpDao, sliceId, cpId });
      MfcL2CpPK l2CpPk = new MfcL2CpPK();
      l2CpPk.setSliceId(sliceId);
      l2CpPk.setCpId(cpId);
      MfcL2Cp l2Cp = l2CpDao.read(sessionWrapper, l2CpPk);
      if (l2Cp != null) {
        String logMsg = MessageFormat.format("sliceId = {0}, cpId = {1}", sliceId, cpId);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  protected void processCreateL2Cp(SessionWrapper sessionWrapper, MfcL2Slice l2Slice, String cpId, int edgePointId,
      String portMode, int vlanId, String pairCpId, int clusterId, L2CpQosCreateEntity qosEntity) throws MsfException {
    try {
      logger.methodStart(
          new String[] { "sessionWrapper", "l2Slice", "cpId", "edgePointId", "portMode", "vlanId", "pairCpId",
              "clusterId", "qosEntity" },
          new Object[] { sessionWrapper, l2Slice, cpId, edgePointId, portMode, vlanId, pairCpId, clusterId,
              ToStringBuilder.reflectionToString(qosEntity) });

      MfcL2CpDao l2CpDao = new MfcL2CpDao();
      MfcL2Cp newL2Cp = makeNewL2Cp(sessionWrapper, l2Slice.getSliceId(), cpId, clusterId);

      if (pairCpId != null) {
        MfcL2CpPK pairL2CpPk = new MfcL2CpPK();
        pairL2CpPk.setSliceId(l2Slice.getSliceId());
        pairL2CpPk.setCpId(pairCpId);
        MfcL2Cp pairL2Cp = l2CpDao.read(sessionWrapper, pairL2CpPk);
        if (pairL2Cp == null) {

          processCreateL2CpForPairCpNotFound(sessionWrapper, newL2Cp, pairCpId, portMode, vlanId, edgePointId);
        } else {

          processCreateL2CpForPairCpFound(sessionWrapper, pairL2Cp, newL2Cp, portMode, vlanId);
        }
      }

      L2CpCreateDeleteRequestBody requestBody = makeL2CpCreateDeleteRequestBody(PatchOperation.ADD, cpId, clusterId,
          edgePointId, newL2Cp.getEsi(), portMode, vlanId, qosEntity);
      putL2CpCreateDeleteRequestBodyForMap(clusterId, requestBody);

    } finally {
      logger.methodEnd();
    }
  }

  protected MfcL2Cp makeNewL2Cp(SessionWrapper sessionWrapper, String sliceId, String cpId, int clusterId)
      throws MsfException {
    MfcL2Cp newL2Cp = new MfcL2Cp();
    MfcL2CpPK l2CpPk = new MfcL2CpPK();
    l2CpPk.setSliceId(sliceId);
    l2CpPk.setCpId(cpId);
    newL2Cp.setId(l2CpPk);
    MfcSwCluster swCluster = new MfcSwCluster();
    swCluster.setSwClusterId(clusterId);
    newL2Cp.setSwCluster(swCluster);
    return newL2Cp;
  }

  protected L2CpCreateDeleteRequestBody makeL2CpCreateDeleteRequestBody(PatchOperation op, String cpId,
      Integer clusterId, Integer edgePointId, String esi, String portMode, Integer vlanId,
      L2CpQosCreateEntity qosEntity) {
    try {
      logger.methodStart(
          new String[] { "op", "cpId", "clusterId", "edgePointId", "esi", "portMode", "vlanId", "qosEntity" },
          new Object[] { op, cpId, clusterId, edgePointId, esi, portMode, vlanId,
              ToStringBuilder.reflectionToString(qosEntity) });
      L2CpCreateDeleteRequestBody body = new L2CpCreateDeleteRequestBody();
      body.setOpEnum(op);
      body.setPath("/" + cpId);
      L2CpValueEntity entity = new L2CpValueEntity();
      switch (op) {
        case ADD:
          entity.setClusterId(String.valueOf(clusterId));
          entity.setEdgePointId(String.valueOf(edgePointId));
          entity.setVlanId(vlanId);
          entity.setQos(qosEntity);
          entity.setEsi(esi);
          entity.setLacpSystemId(getNextLacpSystemId(esi));
          entity.setPortMode(portMode);
          break;
        case REPLACE:
          entity.setEsi(esi);
          entity.setLacpSystemId(getNextLacpSystemId(esi));
          break;
        default:

          entity = null;
          break;

      }
      body.setValue(entity);
      return body;
    } finally {
      logger.methodEnd();
    }
  }

  protected L2CpCreateDeleteRequestBody makeL2CpCreateDeleteRequestBodyForUpdate(String cpId, String esi) {
    return makeL2CpCreateDeleteRequestBody(PatchOperation.REPLACE, cpId, null, null, esi, null, null, null);
  }

  protected L2CpCreateDeleteRequestBody makeL2CpCreateDeleteRequestBodyForDelete(String cpId) {
    return makeL2CpCreateDeleteRequestBody(PatchOperation.REMOVE, cpId, null, null, null, null, null, null);
  }

  protected void processCreateL2CpForPairCpNotFound(SessionWrapper sessionWrapper, MfcL2Cp newL2Cp, String pairCpId,
      String portMode, int vlanId, int edgePointId) throws MsfException {
    String logMsg = MessageFormat.format("pair cp is not found. pair cp id = {0}", pairCpId);
    logger.error(logMsg);
    throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, logMsg);
  }

  protected void processCreateL2CpForPairCpFound(SessionWrapper sessionWrapper, MfcL2Cp pairL2Cp, MfcL2Cp newL2Cp,
      String portMode, int vlanId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "pairL2Cp", "newL2Cp", "portMode", "vlanId" },
          new Object[] { sessionWrapper, pairL2Cp, newL2Cp, portMode, vlanId });

      if (pairL2Cp.getEsi() != null) {
        String logMsg = MessageFormat.format("esi value is already set. pair cp id = {0}", pairL2Cp.getId().getCpId());
        logger.error(logMsg);
        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
      }
      MfcL2CpDao l2CpDao = new MfcL2CpDao();

      if (!newL2Cp.getSwCluster().getSwClusterId().equals(pairL2Cp.getSwCluster().getSwClusterId())) {

        checkPairCpVlanIdAndPortMode(newL2Cp.getId().getSliceId(), portMode, vlanId,
            pairL2Cp.getSwCluster().getSwClusterId(), pairL2Cp.getId().getCpId(), null, null);
      }

      List<MfcL2Cp> l2CpList = l2CpDao.readList(sessionWrapper);
      Set<String> esiIdSet = createEsiIdSet(l2CpList);

      int pairCpClusterId = pairL2Cp.getSwCluster().getSwClusterId();
      int newCpClusterId = newL2Cp.getSwCluster().getSwClusterId();
      String esi;
      if (pairCpClusterId > newCpClusterId) {
        esi = getNextEsi(sessionWrapper, esiIdSet, newCpClusterId, pairCpClusterId);
      } else {
        esi = getNextEsi(sessionWrapper, esiIdSet, pairCpClusterId, newCpClusterId);
      }
      newL2Cp.setEsi(esi);

      L2CpCreateDeleteRequestBody requestBody = makeL2CpCreateDeleteRequestBodyForUpdate(pairL2Cp.getId().getCpId(),
          esi);
      putL2CpCreateDeleteRequestBodyForMap(pairCpClusterId, requestBody);

    } finally {
      logger.methodEnd();
    }
  }

  protected void checkPairCpVlanIdAndPortMode(String sliceId, String portMode, int vlanId, int pairCpClusterId,
      String pairCpId, String pairCpPortMode, Integer pairCpVlanId) throws MsfException {
    try {
      logger.methodStart();

      if (pairCpId != null) {

        L2CpReadResponseBody pairL2CpResponseBody = getL2CpFromMfc(pairCpClusterId, sliceId, pairCpId);
        pairCpVlanId = pairL2CpResponseBody.getL2Cp().getVlanId();
        pairCpPortMode = pairL2CpResponseBody.getL2Cp().getPortMode();
      }

      checkPairVlanId(vlanId, pairCpVlanId);

      checkPairPorMode(portMode, pairCpPortMode);

    } finally {
      logger.methodEnd();
    }
  }

  protected L2CpReadResponseBody getL2CpFromMfc(int clusterId, String sliceId, String cpId) throws MsfException {
    try {
      logger.methodStart();
      RestResponseBase restResponseBase = RestClient.sendRequest(MfcFcRequestUri.CP_READ.getHttpMethod(),
          MfcFcRequestUri.CP_READ.getUri(SliceType.L2_SLICE.getMessage(), sliceId, cpId), null,
          MfcConfigManager.getInstance().getSystemConfSwClusterData(clusterId).getSwCluster().getFcControlAddress(),
          MfcConfigManager.getInstance().getSystemConfSwClusterData(clusterId).getSwCluster().getFcControlPort());
      L2CpReadResponseBody body = JsonUtil.fromJson(restResponseBase.getResponseBody(), L2CpReadResponseBody.class,
          ErrorCode.FC_CONTROL_ERROR);
      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200, body.getErrorCode(),
          ErrorCode.FC_CONTROL_ERROR);
      return body;
    } finally {
      logger.methodEnd();
    }
  }

  protected Set<String> createEsiIdSet(List<MfcL2Cp> l2CpList) {
    try {
      logger.methodStart(new String[] { "l2CpList" }, new Object[] { l2CpList });
      Set<String> esiIdSet = new TreeSet<>();
      for (MfcL2Cp l2Cp : l2CpList) {
        if (l2Cp.getEsi() != null) {
          esiIdSet.add(l2Cp.getEsi());
        }
      }
      return esiIdSet;
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

  protected List<RestRequestData> makeSendRequestDataList(String sliceId, Map<Integer, String> lowerOperationIdMap) {
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

  protected void processDeleteL2Cp(SessionWrapper sessionWrapper, MfcL2Cp l2Cp) throws MsfException {
    try {
      logger.methodStart();

      RestResponseBase response = getCpDataFromFc(SliceType.L2_SLICE, l2Cp.getId().getSliceId(), l2Cp.getId().getCpId(),
          l2Cp.getSwCluster().getSwClusterId());

      rollbackData.addL2CpEntity(JsonUtil
          .fromJson(response.getResponseBody(), L2CpReadResponseBody.class, ErrorCode.UNDEFINED_ERROR).getL2Cp());

      MfcL2CpDao l2CpDao = new MfcL2CpDao();

      if (l2Cp.getEsi() != null) {
        MfcL2Cp pairL2Cp = getPairCpFromDb(sessionWrapper, l2CpDao, l2Cp);
        if (pairL2Cp == null) {
          String logMsg = MessageFormat.format("pair cp is not found. esi = {0}", l2Cp.getEsi());
          logger.error(logMsg);
          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, logMsg);
        }

        processDeleteL2CpForPairCpFound(sessionWrapper, pairL2Cp);
      }

      L2CpCreateDeleteRequestBody requestBody = makeL2CpCreateDeleteRequestBodyForDelete(l2Cp.getId().getCpId());
      putL2CpCreateDeleteRequestBodyForMap(l2Cp.getSwCluster().getSwClusterId(), requestBody);
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
}
