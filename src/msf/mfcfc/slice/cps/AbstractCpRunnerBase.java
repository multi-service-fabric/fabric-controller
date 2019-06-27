
package msf.mfcfc.slice.cps;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.PatchOperation;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.data.CpId;
import msf.mfcfc.common.data.CpIdPK;
import msf.mfcfc.common.data.EsiId;
import msf.mfcfc.common.data.EsiIdPK;
import msf.mfcfc.common.data.VlanIfId;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.EsiUtil;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.AbstractAsyncRunner;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.DbManager;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.db.dao.slices.CpIdDao;
import msf.mfcfc.db.dao.slices.EsiDao;
import msf.mfcfc.db.dao.slices.VlanIfIdDao;
import msf.mfcfc.slice.cps.l2cp.data.L2CpCreateAsyncResponseBody;
import msf.mfcfc.slice.cps.l2cp.data.L2CpCreateDeleteAsyncResponseBody;
import msf.mfcfc.slice.cps.l2cp.data.L2CpCreateDeleteRequestBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpCreateAsyncResponseBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpCreateDeleteAsyncResponseBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpCreateDeleteRequestBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpStaticRouteCreateDeleteAsyncResponseBody;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpStaticRouteEntity;

/**
 * Abstract class to implement the common process of the asynchronous runner
 * processing in the CP management.
 *
 * @author NTT
 *
 */
public abstract class AbstractCpRunnerBase extends AbstractAsyncRunner {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractCpRunnerBase.class);

  public static final String STATIC_ROUTE_ID_SEPARATOR = "_";

  public static final String STATIC_ROUTE_PATH_PREFIX = "/static_routes";

  protected int getNextCpId(SessionWrapper sessionWrapper, Set<String> cpIdSet, String sliceId, SliceType sliceType)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "cpIdSet", "sliceId", "sliceType" },
          new Object[] { sessionWrapper, cpIdSet, sliceId, sliceType });
      CpIdDao cpIdDao = DbManager.getInstance().createCpIdDao();
      CpIdPK cpIdPk = new CpIdPK();
      cpIdPk.setLayerTypeEnum(sliceType);
      cpIdPk.setSliceId(sliceId);

      CpId cpId = cpIdDao.read(sessionWrapper, cpIdPk);

      int firstNextId = cpId.getNextId();

      int targetNextId = firstNextId;
      logger.performance("start get available cp id.");
      do {
        if (cpIdSet.contains(String.valueOf(targetNextId))) {

          targetNextId++;

          if (!checkIdRange(targetNextId, 0, Integer.MAX_VALUE)) {

            targetNextId = 1;
          }
        } else {

          updateCpId(sessionWrapper, cpIdDao, cpId, targetNextId + 1);
          logger.performance("end get available cp id.");
          return targetNextId;
        }

      } while (targetNextId != firstNextId);
      logger.performance("end get available cp id.");

      String logMsg = MessageFormat.format("threre is no available cp id. firstCheckId = {0}", firstNextId);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, logMsg);

    } finally {
      logger.methodEnd();
    }
  }

  protected String getNextEsi(SessionWrapper sessionWrapper, Set<String> esiSet, int lowerClusterId,
      int higherClusterId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "esiSet", "lowerClusterId", "higherClusterId" },
          new Object[] { sessionWrapper, esiSet, lowerClusterId, higherClusterId });
      EsiDao esiDao = DbManager.getInstance().createEsiDao();
      EsiIdPK esiPk = new EsiIdPK();
      esiPk.setSwClusterId1(Integer.valueOf(lowerClusterId));
      esiPk.setSwClusterId2(Integer.valueOf(higherClusterId));

      EsiId esiId = esiDao.read(sessionWrapper, esiPk);

      int firstNextId = esiId.getNextId();

      int targetNextId = firstNextId;
      logger.performance("start get available esi.");
      do {
        if (esiSet.contains(EsiUtil.createEsi(lowerClusterId, higherClusterId, targetNextId))) {

          targetNextId++;

          if (!checkIdRange(targetNextId, 0, EsiUtil.ESI_SERIAL_MAX)) {

            targetNextId = 1;
          }
        } else {

          updateEsiId(sessionWrapper, esiDao, esiId, targetNextId + 1);
          logger.performance("end get available cp id.");
          return EsiUtil.createEsi(lowerClusterId, higherClusterId, targetNextId);
        }

      } while (targetNextId != firstNextId);
      logger.performance("end get available esi id.");

      String logMsg = MessageFormat.format("threre is no available esi id. firstCheckId = {0}", firstNextId);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, logMsg);

    } finally {
      logger.methodEnd();
    }

  }

  protected String getNextLacpSystemId(String esi) {
    return EsiUtil.getLacpSystemIdFromEsi(esi);
  }

  protected int getNextVlanIfId(SessionWrapper sessionWrapper, Set<String> vlanIfIdSet, long nodeInfoId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "vlanIfIdSet", "nodeInfoId" },
          new Object[] { sessionWrapper, vlanIfIdSet, nodeInfoId });
      VlanIfIdDao vlanIfIdDao = DbManager.getInstance().createVlanIfIdDao();

      VlanIfId vlanIfId = vlanIfIdDao.read(sessionWrapper, nodeInfoId);

      int firstNextId = vlanIfId.getNextId();

      int targetNextId = firstNextId;
      logger.performance("start get available vlan if id.");
      do {
        if (vlanIfIdSet.contains(String.valueOf(targetNextId))) {

          targetNextId++;

          if (!checkIdRange(targetNextId, 0, Integer.MAX_VALUE)) {

            targetNextId = 1;
          }
        } else {

          updateVlanIfId(sessionWrapper, vlanIfIdDao, vlanIfId, targetNextId + 1);
          logger.performance("end get available vlan if id.");
          return targetNextId;
        }

      } while (targetNextId != firstNextId);
      logger.performance("end get available vlan if id.");

      String logMsg = MessageFormat.format("threre is no available vlan if id. firstCheckId = {0}", firstNextId);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, logMsg);

    } finally {
      logger.methodEnd();
    }
  }

  private void updateCpId(SessionWrapper sessionWrapper, CpIdDao cpIdDao, CpId cpId, int nextId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "cpIdDao", "cpId", "nextId" },
          new Object[] { sessionWrapper, cpIdDao, cpId, nextId });

      if (!checkIdRange(nextId, 0, Integer.MAX_VALUE)) {

        nextId = 1;
      }
      cpId.setNextId(nextId);

      cpIdDao.update(sessionWrapper, cpId);
    } finally {
      logger.methodEnd();
    }
  }

  private boolean checkIdRange(int checkTargetId, int min, int max) {
    try {
      logger.methodStart(new String[] { "checkTargetId" }, new Object[] { checkTargetId });
      if (checkTargetId >= min && checkTargetId < max) {
        return true;
      } else {
        return false;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void updateEsiId(SessionWrapper sessionWrapper, EsiDao esiDao, EsiId esiId, int nextId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "esiDao", "esiId", "nextId" },
          new Object[] { sessionWrapper, esiDao, esiId, nextId });

      if (!checkIdRange(nextId, 0, EsiUtil.ESI_SERIAL_MAX)) {

        nextId = 1;
      }
      esiId.setNextId(nextId);

      esiDao.update(sessionWrapper, esiId);
    } finally {
      logger.methodEnd();
    }
  }

  private void updateVlanIfId(SessionWrapper sessionWrapper, VlanIfIdDao vlanIfIdDao, VlanIfId vlanIfId, int nextId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "vlanIfIdDao", "vlanIfId", "nextId" },
          new Object[] { sessionWrapper, vlanIfIdDao, vlanIfId, nextId });

      if (!checkIdRange(nextId, 0, Integer.MAX_VALUE)) {

        nextId = 1;
      }
      vlanIfId.setNextId(nextId);

      vlanIfIdDao.update(sessionWrapper, vlanIfId);
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkSlicePresence(Object sliceEntity, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sliceEntity", "sliceId" }, new Object[] { sliceEntity, sliceId });
      ParameterCheckUtil.checkNotNullRelatedResource(sliceEntity, new String[] { "sliceId" }, new Object[] { sliceId });

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

  protected void checkNodePresence(Object nodeEntity, int edgePointId) throws MsfException {
    try {
      logger.methodStart(new String[] { "node", "edgePointId" }, new Object[] { nodeEntity, edgePointId });
      ParameterCheckUtil.checkNotNullRelatedResource(nodeEntity, new String[] { "edgePointId" },
          new Object[] { String.valueOf(edgePointId) });
    } finally {
      logger.methodEnd();
    }
  }

  protected String getIdFromPath(String path) {
    try {
      logger.methodStart();

      if (path.startsWith(STATIC_ROUTE_PATH_PREFIX)) {
        path = path.replace(STATIC_ROUTE_PATH_PREFIX, "");
      }

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

  protected RestResponseBase createResponseForL2CpCreate(String cpId) {
    try {
      logger.methodStart(new String[] { "cpId" }, new Object[] { cpId });

      L2CpCreateAsyncResponseBody responseBody = new L2CpCreateAsyncResponseBody();
      responseBody.setCpId(cpId);
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.CREATED_201, responseBody);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase createResponseForMultiL2CpCreate(List<String> cpIdList) {
    try {
      logger.methodStart(new String[] { "cpIdList" }, new Object[] { cpIdList });

      L2CpCreateDeleteAsyncResponseBody responseBody = new L2CpCreateDeleteAsyncResponseBody();
      responseBody.setCpIdList(cpIdList);
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.CREATED_201, responseBody);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase createResponseForL3CpCreate(String cpId) {
    try {
      logger.methodStart(new String[] { "cpId" }, new Object[] { cpId });

      L3CpCreateAsyncResponseBody responseBody = new L3CpCreateAsyncResponseBody();
      responseBody.setCpId(cpId);
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.CREATED_201, responseBody);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase createResponseForMultiL3CpCreate(List<String> cpIdList) {
    try {
      logger.methodStart(new String[] { "cpIdList" }, new Object[] { cpIdList });

      L3CpCreateDeleteAsyncResponseBody responseBody = new L3CpCreateDeleteAsyncResponseBody();
      responseBody.setCpIdList(cpIdList);
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.CREATED_201, responseBody);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkPairVlanId(Integer vlanId, Integer pairCpVlanId) throws MsfException {

    if (!vlanId.equals(pairCpVlanId)) {
      String logMsg = MessageFormat.format(
          "vlan id is different from pair cps vlan id. request vlan id = {0}, pair cps vlan id = {1}", vlanId,
          pairCpVlanId);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
    }
  }

  protected void checkPairPorMode(String portMode, String pairCpPortMode) throws MsfException {

    if (!portMode.equals(pairCpPortMode)) {
      String logMsg = MessageFormat.format(
          "port mode is different from pair cps port mode. request port mode = {0}, pair cps port mode = {1}", portMode,
          pairCpPortMode);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
    }
  }

  protected RestResponseBase createResponseForCreateStaticRoute(List<String> staticRouteIdList) {
    try {
      logger.methodStart(new String[] { "staticRouteIdList" }, new Object[] { staticRouteIdList });

      L3CpStaticRouteCreateDeleteAsyncResponseBody responseBody = new L3CpStaticRouteCreateDeleteAsyncResponseBody();
      responseBody.setStaticRouteIdList(staticRouteIdList);
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.CREATED_201, responseBody);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

  protected String makeStaticRouteId(L3CpStaticRouteEntity entity) {
    return entity.getAddrType() + STATIC_ROUTE_ID_SEPARATOR + entity.getAddress() + STATIC_ROUTE_ID_SEPARATOR
        + entity.getPrefix() + STATIC_ROUTE_ID_SEPARATOR + entity.getNextHop();
  }

  protected L3CpStaticRouteEntity makeL3CpStaticRouteEntityFromId(String staticRouteId) {
    try {
      logger.methodStart(new String[] { "staticRouteId" }, new Object[] { staticRouteId });
      String[] staticRouteStrs = staticRouteId.split(STATIC_ROUTE_ID_SEPARATOR);
      L3CpStaticRouteEntity entity = new L3CpStaticRouteEntity();
      entity.setAddrType(staticRouteStrs[0]);
      entity.setAddress(staticRouteStrs[1]);
      entity.setPrefix(Integer.valueOf(staticRouteStrs[2]));
      entity.setNextHop(staticRouteStrs[3]);
      return entity;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<String> getNewL2CpCreateIdListFromPatchRequest(List<L2CpCreateDeleteRequestBody> requestList) {
    try {
      logger.methodStart();
      List<String> cpIdList = new ArrayList<>();
      for (L2CpCreateDeleteRequestBody body : requestList) {
        if (PatchOperation.ADD.equals(body.getOpEnum())) {
          String cpId = getIdFromPath(body.getPath());
          if (cpId != null) {
            cpIdList.add(cpId);
          }
        }
      }
      return cpIdList;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<String> getNewL3CpCreateIdListFromPatchRequest(List<L3CpCreateDeleteRequestBody> requestList) {
    try {
      logger.methodStart();
      List<String> cpIdList = new ArrayList<>();
      for (L3CpCreateDeleteRequestBody body : requestList) {
        if (PatchOperation.ADD.equals(body.getOpEnum())) {
          String cpId = getIdFromPath(body.getPath());
          if (cpId != null) {
            cpIdList.add(cpId);
          }
        }
      }
      return cpIdList;
    } finally {
      logger.methodEnd();
    }
  }

  protected int getNextClagId(Set<Integer> clagIdSet) throws MsfException {
    try {
      logger.methodStart(new String[] { "clagIdSet" }, new Object[] { clagIdSet });

      Set<Integer> allClagIdSet = new TreeSet<>();
      for (int i = 1; i <= 65535; i++) {
        allClagIdSet.add(i);
      }

      allClagIdSet.removeAll(clagIdSet);

      if (!allClagIdSet.isEmpty()) {
        return allClagIdSet.iterator().next();
      } else {

        String logMsg = "could not be assigned clag id.";
        logger.error(logMsg);
        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }
}
