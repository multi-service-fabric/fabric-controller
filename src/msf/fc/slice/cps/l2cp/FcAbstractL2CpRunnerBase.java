
package msf.fc.slice.cps.l2cp;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcEdgePoint;
import msf.fc.common.data.FcL2Cp;
import msf.fc.common.data.FcL2CpPK;
import msf.fc.common.data.FcL2Slice;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcVlanIf;
import msf.fc.common.data.FcVlanIfPK;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.slices.FcL2CpDao;
import msf.fc.db.dao.slices.FcL2SliceDao;
import msf.fc.rest.ec.core.operation.data.OperationRequestBody;
import msf.fc.rest.ec.core.operation.data.entity.OperationBaseIfEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationCreateUpdateL2VlanIfOptionEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationCreateVlanIfEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationDeleteUpdateL2VlanIfOptionEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationDeleteVlanIfEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationQosEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationUpdateVlanIfEcEntity;
import msf.fc.rest.ec.node.interfaces.vlan.data.VlanIfReadEcResponseBody;
import msf.fc.slice.cps.FcAbstractCpRunnerBase;
import msf.mfcfc.common.constant.EcCommonOperationAction;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.EsiUtil;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.cps.l2cp.data.L2CpRequest;
import msf.mfcfc.slice.cps.l2cp.data.entity.L2CpQosCreateEntity;

/**
 * Abstract class to implement the common process of L2CP-related asynchronous
 * runner processing in slice management function.
 *
 * @author NTT
 *
 */
public abstract class FcAbstractL2CpRunnerBase extends FcAbstractCpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractL2CpRunnerBase.class);

  protected L2CpRequest request;

  protected List<OperationCreateVlanIfEcEntity> createVlanIfEntityList = new ArrayList<>();

  protected List<OperationUpdateVlanIfEcEntity> updateVlanIfEntityList = new ArrayList<>();

  protected List<String> beforeUpdateEsiList = new ArrayList<>();

  protected List<OperationDeleteVlanIfEcEntity> deleteVlanIfEntityList = new ArrayList<>();

  protected OperationCreateVlanIfEcEntity makeOperationCreateVlanIfEcEntity(SessionWrapper sessionWrapper, FcL2Cp l2Cp,
      String portMode, Integer vlanId, L2CpQosCreateEntity qosEntity) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l2Cp", "portMode", "vlanId", "qosEntity" },
          new Object[] { sessionWrapper, l2Cp, portMode, vlanId, ToStringBuilder.reflectionToString(qosEntity) });
      FcNodeDao nodeDao = new FcNodeDao();
      FcNode requestTargetNode = nodeDao.read(sessionWrapper, l2Cp.getEdgePoint().getEdgePointId());
      OperationBaseIfEcEntity baseIfEntity = new OperationBaseIfEcEntity();
      baseIfEntity.setIfId(getIfId(l2Cp.getEdgePoint()));
      baseIfEntity.setIfType(getIfType(l2Cp.getEdgePoint()).getMessage());
      baseIfEntity.setNodeId(String.valueOf(requestTargetNode.getEcNodeId()));
      OperationCreateVlanIfEcEntity entity = new OperationCreateVlanIfEcEntity();
      entity.setBaseIf(baseIfEntity);
      entity.setEsi(l2Cp.getEsi());
      entity.setLacpSystemId(getNextLacpSystemId(l2Cp.getEsi()));
      entity.setNodeId(String.valueOf(requestTargetNode.getEcNodeId()));
      entity.setPortMode(portMode);
      entity.setQos(makeOperationQosEcEntity(l2Cp, qosEntity));
      entity.setVlanId(vlanId);
      entity.setVlanIfId(String.valueOf(l2Cp.getVlanIf().getId().getVlanIfId()));
      return entity;
    } finally {
      logger.methodEnd();
    }
  }

  protected OperationUpdateVlanIfEcEntity makeOperationUpdateVlanIfEcEntity(SessionWrapper sessionWrapper, FcL2Cp l2Cp)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l2Cp" }, new Object[] { sessionWrapper, l2Cp });
      FcNodeDao nodeDao = new FcNodeDao();
      FcNode pairCpTargetNode = nodeDao.read(sessionWrapper, l2Cp.getEdgePoint().getEdgePointId());
      OperationUpdateVlanIfEcEntity entity = new OperationUpdateVlanIfEcEntity();

      if (l2Cp.getEsi() == null) {
        entity.setEsi("0");
      } else {
        entity.setEsi(l2Cp.getEsi());
      }
      entity.setLacpSystemId(getNextLacpSystemId(entity.getEsi()));
      entity.setNodeId(String.valueOf(pairCpTargetNode.getEcNodeId()));
      entity.setVlanIfId(String.valueOf(l2Cp.getVlanIf().getId().getVlanIfId()));
      return entity;
    } finally {
      logger.methodEnd();
    }
  }

  protected OperationDeleteVlanIfEcEntity makeOperationDeleteVlanIfEcEntity(Integer ecNodeId, Integer vlanIfId) {
    try {
      logger.methodStart(new String[] { "ecNodeId", "vlanIfId" }, new Object[] { ecNodeId, vlanIfId });
      OperationDeleteVlanIfEcEntity entity = new OperationDeleteVlanIfEcEntity();
      entity.setNodeId(String.valueOf(ecNodeId));
      entity.setVlanIfId(String.valueOf(vlanIfId));
      return entity;
    } finally {
      logger.methodEnd();
    }
  }

  protected String makeCreateUpdateL2VlanIfData(List<OperationCreateVlanIfEcEntity> createVlanIfEntityList,
      List<OperationUpdateVlanIfEcEntity> updateVlanIfEntityList, Integer vrfId) {
    try {
      logger.methodStart(new String[] { "createVlanIfEntityList", "updateVlanIfEntityList", "vrfId" },
          new Object[] { createVlanIfEntityList, updateVlanIfEntityList, vrfId });
      OperationCreateUpdateL2VlanIfOptionEcEntity entity = new OperationCreateUpdateL2VlanIfOptionEcEntity();
      entity.setCreateVlanIfList(createVlanIfEntityList);
      entity.setUpdateVlanIfList(updateVlanIfEntityList);
      entity.setVrfId(vrfId);
      OperationRequestBody body = new OperationRequestBody();
      body.setAction(EcCommonOperationAction.CREATE_UPDATE_L2VLAN_IF.getMessage());
      body.setCreateUpdateL2vlanIfOption(entity);
      return JsonUtil.toJson(body);
    } finally {
      logger.methodEnd();
    }
  }

  protected String makeDeleteUpdateL2VlanIfData(List<OperationDeleteVlanIfEcEntity> deleteVlanIfEntityList,
      List<OperationUpdateVlanIfEcEntity> updateVlanIfEntityList, String vrfId) {
    try {
      logger.methodStart(new String[] { "deleteVlanIfEntityList", "updateVlanIfEntityList", "vrfId" },
          new Object[] { deleteVlanIfEntityList, updateVlanIfEntityList, vrfId });
      OperationDeleteUpdateL2VlanIfOptionEcEntity entity = new OperationDeleteUpdateL2VlanIfOptionEcEntity();
      entity.setDeleteVlanIfList(deleteVlanIfEntityList);
      entity.setUpdateVlanIfList(updateVlanIfEntityList);
      entity.setVrfId(vrfId);
      OperationRequestBody body = new OperationRequestBody();
      body.setAction(EcCommonOperationAction.DELETE_UPDATE_L2VLAN_IF.getMessage());
      body.setDeleteUpdateL2vlanIfOption(entity);
      return JsonUtil.toJson(body);
    } finally {
      logger.methodEnd();
    }
  }

  protected FcL2Cp getL2CpAndCheck(SessionWrapper sessionWrapper, String sliceId, String cpId) throws MsfException {
    try {
      logger.methodStart();
      FcL2CpDao l2CpDao = new FcL2CpDao();
      FcL2CpPK l2CpPk = new FcL2CpPK();
      l2CpPk.setSliceId(sliceId);
      l2CpPk.setCpId(cpId);
      FcL2Cp l2Cp = l2CpDao.read(sessionWrapper, l2CpPk);

      checkCpPresence(l2Cp, sliceId, cpId);
      return l2Cp;
    } finally {
      logger.methodEnd();
    }
  }

  protected FcL2Slice getL2SliceAndCheck(SessionWrapper sessionWrapper, String sliceId) throws MsfException {
    try {
      logger.methodStart();
      FcL2SliceDao l2SliceDao = new FcL2SliceDao();
      FcL2Slice l2Slice = l2SliceDao.read(sessionWrapper, sliceId);

      checkSlicePresence(l2Slice, sliceId);
      return l2Slice;
    } finally {
      logger.methodEnd();
    }
  }

  protected void processCreateL2Cp(SessionWrapper sessionWrapper, FcL2Slice l2Slice, FcNode node, String cpId,
      int edgePointId, String portMode, int vlanId, String pairCpId, String esi, L2CpQosCreateEntity qosEntity)
      throws MsfException {
    try {
      logger.methodStart(
          new String[] { "sessionWrapper", "l2Slice", "node", "cpId", "edgePointId", "portMode", "vlanId", "pairCpId",
              "qosEntity" },
          new Object[] { sessionWrapper, l2Slice, node, cpId, edgePointId, portMode, vlanId, pairCpId,
              ToStringBuilder.reflectionToString(qosEntity) });

      checkL2NwConstraints(sessionWrapper, l2Slice, node, vlanId, edgePointId);

      FcL2CpDao l2CpDao = new FcL2CpDao();
      FcL2Cp newL2Cp = makeNewL2Cp(sessionWrapper, l2Slice, node, cpId, edgePointId, esi);

      if (pairCpId != null) {
        FcL2CpPK pairL2CpPk = new FcL2CpPK();
        pairL2CpPk.setSliceId(l2Slice.getSliceId());
        pairL2CpPk.setCpId(pairCpId);
        FcL2Cp pairL2Cp = l2CpDao.read(sessionWrapper, pairL2CpPk);
        if (pairL2Cp == null) {

          processCreateL2CpForPairCpNotFound(sessionWrapper, newL2Cp, node, pairCpId, portMode, vlanId, edgePointId);
        } else {

          processCreateL2CpForPairCpFound(sessionWrapper, node, pairL2Cp, newL2Cp, portMode, vlanId);
        }
      } else {

        if (checkEsiClosedInSelf(esi)) {
          logger.debug("esi {0} is closed in this cluster.", esi);
          FcL2Cp pairL2Cp = getPairCpFromDb(sessionWrapper, l2CpDao, newL2Cp);
          if (pairL2Cp != null) {
            logger.debug("l2cp found with same esi. cp id = {0}", pairL2Cp.getId().getCpId());
            FcNode pairNode = getNodeAndCheck(sessionWrapper, pairL2Cp.getEdgePoint().getEdgePointId());

            if (!checkCreateProcessedPairCpId(newL2Cp.getEsi())) {

              checkPairCpVlanIdAndPortMode(sessionWrapper, portMode, vlanId, pairNode.getNodeInfoId().intValue(),
                  pairL2Cp.getVlanIf().getId().getVlanIfId(), null, null);
            }

            checkCreateTargetNode(sessionWrapper, pairL2Cp.getId().getCpId(), edgePointId,
                pairL2Cp.getEdgePoint().getEdgePointId());
          }
        }
      }

      OperationCreateVlanIfEcEntity createVlanIfEntity = makeOperationCreateVlanIfEcEntity(sessionWrapper, newL2Cp,
          portMode, vlanId, qosEntity);
      createVlanIfEntityList.add(createVlanIfEntity);

      l2CpDao.create(sessionWrapper, newL2Cp);

    } finally {
      logger.methodEnd();
    }
  }

  private void checkL2NwConstraints(SessionWrapper sessionWrapper, FcL2Slice l2Slice, FcNode node, int vlanId,
      int edgePointId) throws MsfException {
    try {
      logger.methodStart();
      FcL2CpDao l2CpDao = new FcL2CpDao();

      checkVlanIdSameCheck(sessionWrapper, l2Slice, node, l2CpDao, vlanId);

      checkEdgePointDuplicateCheck(sessionWrapper, l2Slice, l2CpDao, edgePointId);
    } finally {
      logger.methodEnd();
    }
  }

  private void checkVlanIdSameCheck(SessionWrapper sessionWrapper, FcL2Slice l2Slice, FcNode node, FcL2CpDao l2CpDao,
      int vlanId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l2Slice", "node", "l2CpDao", "vlanId" },
          new Object[] { sessionWrapper, l2Slice, node, l2CpDao, vlanId });

      List<FcL2Cp> l2CpList = l2CpDao.readListByNodeInfo(sessionWrapper, l2Slice.getSliceId(), node.getNodeInfoId());
      if (l2CpList != null && l2CpList.size() != 0) {

        FcVlanIf vlanIf = l2CpList.get(0).getVlanIf();

        for (OperationCreateVlanIfEcEntity entity : createVlanIfEntityList) {

          if (entity.getBaseIf().getNodeId().equals(String.valueOf(node.getEcNodeId()))
              && entity.getVlanIfId().equals(String.valueOf(vlanIf.getId().getVlanIfId()))) {
            logger.debug("found l2cp from other cp create request. cp id = {0}, vlan id = {1}",
                l2CpList.get(0).getId().getCpId(), entity.getVlanId());
            if (entity.getVlanId() != vlanId) {

              String logMsg = MessageFormat.format(
                  "l2 nw constraint violation.l2cps vlan id are not same.request vlan id = {0},exist vlan id = {1}",
                  vlanId, entity.getVlanId());
              logger.error(logMsg);
              throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
            }

            return;
          }
        }
        logger.debug("found l2cp that same vlan id. cp id = {0}", l2CpList.get(0).getId().getCpId());
        VlanIfReadEcResponseBody responseBody = getVlanIf(sessionWrapper, node.getNodeInfoId(),
            vlanIf.getId().getVlanIfId());

        int vlanIdFromEc;
        if (responseBody.getVlanIf().getVlanId() == null) {
          vlanIdFromEc = 0;
        } else {
          vlanIdFromEc = Integer.valueOf(responseBody.getVlanIf().getVlanId());
        }
        logger.debug("vlan id from ec = {0}", vlanIdFromEc);
        if (vlanId != vlanIdFromEc) {

          String logMsg = MessageFormat.format(
              "l2 nw constraint violation.l2cps vlan id are not same.request vlan id = {0},exist vlan id = {1}", vlanId,
              vlanIdFromEc);
          logger.error(logMsg);
          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
        }
      }

    } finally {
      logger.methodEnd();
    }
  }

  private void checkEdgePointDuplicateCheck(SessionWrapper sessionWrapper, FcL2Slice l2Slice, FcL2CpDao l2CpDao,
      int edgePointId) throws MsfException {
    try {
      logger.methodStart();
      List<FcL2Cp> l2CpList = l2CpDao.readListByEdgePoint(sessionWrapper, l2Slice.getSliceId(), edgePointId);
      if (l2CpList != null && l2CpList.size() != 0) {
        String logMsg = MessageFormat.format("already exist l2Cp in the same edgepoint. edgepoint id = {0}",
            edgePointId);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  protected String getCpIdAndCheck(SessionWrapper sessionWrapper, String sliceId, String cpId,
      List<String> requestCpIdList) throws MsfException {
    try {
      logger.methodStart();
      FcL2CpDao l2CpDao = new FcL2CpDao();

      if (cpId == null) {

        List<FcL2Cp> l2CpList = l2CpDao.readListBySliceId(sessionWrapper, sliceId);
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

  private Set<String> createCpIdSet(List<FcL2Cp> l2CpList) {
    try {
      logger.methodStart(new String[] { "l2CpList" }, new Object[] { l2CpList });
      Set<String> cpIdSet = new TreeSet<>();
      for (FcL2Cp l2Cp : l2CpList) {
        cpIdSet.add(l2Cp.getId().getCpId());
      }
      return cpIdSet;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkCpDuplicate(SessionWrapper sessionWrapper, FcL2CpDao l2CpDao, String sliceId, String cpId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l2CpDao" }, new Object[] { sessionWrapper, l2CpDao });
      FcL2CpPK l2CpPk = new FcL2CpPK();
      l2CpPk.setSliceId(sliceId);
      l2CpPk.setCpId(cpId);
      FcL2Cp l2Cp = l2CpDao.read(sessionWrapper, l2CpPk);
      if (l2Cp != null) {
        String logMsg = MessageFormat.format("sliceId = {0}, cpId = {1}", sliceId, cpId);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  protected Set<String> createEsiIdSet(List<FcL2Cp> l2CpList) {
    try {
      logger.methodStart(new String[] { "l2CpList" }, new Object[] { l2CpList });
      Set<String> esiIdSet = new TreeSet<>();
      for (FcL2Cp l2Cp : l2CpList) {
        if (l2Cp.getEsi() != null) {
          esiIdSet.add(l2Cp.getEsi());
        }
      }
      return esiIdSet;
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkPairCpVlanIdAndPortMode(SessionWrapper sessionWrapper, String portMode, int vlanId,
      Integer pairNodeInfoId, Integer pairCpVlanIfId, String pairCpPortMode, Integer pairCpVlanId) throws MsfException {
    try {
      logger.methodStart();

      if (pairCpVlanIfId != null) {

        VlanIfReadEcResponseBody pairVlanIfReadEcResponseBody = getVlanIf(sessionWrapper, pairNodeInfoId.longValue(),
            pairCpVlanIfId);
        if (pairVlanIfReadEcResponseBody.getVlanIf().getVlanId() == null) {
          pairCpVlanId = 0;
        } else {
          pairCpVlanId = Integer.valueOf(pairVlanIfReadEcResponseBody.getVlanIf().getVlanId());
        }
        pairCpPortMode = pairVlanIfReadEcResponseBody.getVlanIf().getPortMode();
      }

      checkPairVlanId(vlanId, pairCpVlanId);

      checkPairPorMode(portMode, pairCpPortMode);

    } finally {
      logger.methodEnd();
    }
  }

  protected void checkCreateTargetNode(SessionWrapper sessionWrapper, String pairCpId, int edgePointId,
      int pairCpEdgePointId) throws MsfException {
    try {
      logger.methodStart();
      FcNodeDao nodeDao = new FcNodeDao();
      FcNode requestTargetNode = nodeDao.read(sessionWrapper, edgePointId);
      FcNode pairCpTargetNode = nodeDao.read(sessionWrapper, pairCpEdgePointId);

      if (requestTargetNode.getNodeInfoId().equals(pairCpTargetNode.getNodeInfoId())) {
        String logMsg = MessageFormat.format("request cp and pair cp are specified same node. pair cp id = {0}",
            pairCpId);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void processCreateL2CpForPairCpNotFound(SessionWrapper sessionWrapper, FcL2Cp newL2Cp, FcNode node,
      String pairCpId, String portMode, int vlanId, int edgePointId) throws MsfException {
    String logMsg = MessageFormat.format("pair cp is not found. pair cp id = {0}", pairCpId);
    logger.error(logMsg);
    throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, logMsg);
  }

  protected void processCreateL2CpForPairCpFound(SessionWrapper sessionWrapper, FcNode node, FcL2Cp pairL2Cp,
      FcL2Cp newL2Cp, String portMode, int vlanId) throws MsfException {
    try {
      logger.methodStart();

      if (pairL2Cp.getEsi() != null) {
        String logMsg = MessageFormat.format("esi value is already set. pair cp id = {0}", pairL2Cp.getId().getCpId());
        logger.error(logMsg);
        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
      }
      FcL2CpDao l2CpDao = new FcL2CpDao();

      checkPairCpVlanIdAndPortMode(sessionWrapper, portMode, vlanId, pairL2Cp.getVlanIf().getId().getNodeInfoId(),
          pairL2Cp.getVlanIf().getId().getVlanIfId(), null, null);

      checkCreateTargetNode(sessionWrapper, pairL2Cp.getId().getCpId(), newL2Cp.getEdgePoint().getEdgePointId(),
          pairL2Cp.getEdgePoint().getEdgePointId());

      List<FcL2Cp> l2CpList = l2CpDao.readList(sessionWrapper);
      Set<String> esiIdSet = createEsiIdSet(l2CpList);

      int clusterId = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId();
      String esi = getNextEsi(sessionWrapper, esiIdSet, clusterId, clusterId);
      newL2Cp.setEsi(esi);
      pairL2Cp.setEsi(esi);

      OperationUpdateVlanIfEcEntity updateVlanIfEntity = makeOperationUpdateVlanIfEcEntity(sessionWrapper, pairL2Cp);
      updateVlanIfEntityList.add(updateVlanIfEntity);

      l2CpDao.update(sessionWrapper, pairL2Cp);
    } finally {
      logger.methodEnd();
    }
  }

  protected FcL2Cp makeNewL2Cp(SessionWrapper sessionWrapper, FcL2Slice l2Slice, FcNode node, String cpId,
      int edgePointId, String esi) throws MsfException {
    try {
      logger.methodStart();
      FcL2Cp newL2Cp = new FcL2Cp();
      FcL2CpPK l2CpPk = new FcL2CpPK();
      l2CpPk.setSliceId(l2Slice.getSliceId());
      l2CpPk.setCpId(cpId);
      newL2Cp.setId(l2CpPk);
      newL2Cp.setL2Slice(l2Slice);
      if (esi == null || esi.equals("0")) {
        newL2Cp.setEsi(null);
      } else {
        newL2Cp.setEsi(esi);
      }

      FcVlanIfPK vlanIfPk = new FcVlanIfPK();
      Set<String> vlanIfIdSet = createVlanIfIdSet(sessionWrapper, node.getNodeInfoId());
      int vlanIfId = getNextVlanIfId(sessionWrapper, vlanIfIdSet, node.getNodeInfoId().intValue());
      vlanIfPk.setVlanIfId(vlanIfId);
      vlanIfPk.setNodeInfoId(node.getNodeInfoId().intValue());
      FcVlanIf vlanIf = new FcVlanIf();
      vlanIf.setId(vlanIfPk);
      newL2Cp.setVlanIf(vlanIf);

      FcEdgePoint edgePoint = getEdgePointFromDb(sessionWrapper, edgePointId);
      newL2Cp.setEdgePoint(edgePoint);
      return newL2Cp;
    } finally {
      logger.methodEnd();
    }
  }

  protected void processDeleteL2Cp(SessionWrapper sessionWrapper, FcL2Cp l2Cp, FcNode node) throws MsfException {
    try {
      logger.methodStart();
      FcL2CpDao l2CpDao = new FcL2CpDao();
      if (checkEsiClosedInSelf(l2Cp.getEsi())) {
        FcL2Cp pairL2Cp = getPairCpFromDb(sessionWrapper, l2CpDao, l2Cp);
        if (pairL2Cp == null) {

          if (!beforeUpdateEsiList.contains(l2Cp.getEsi())) {
            String logMsg = MessageFormat.format("pair cp is not found. esi = {0}", l2Cp.getEsi());
            logger.error(logMsg);
            throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, logMsg);
          }
        } else {

          processDeleteL2CpForPairCpFound(sessionWrapper, pairL2Cp);
        }
      }

      OperationDeleteVlanIfEcEntity deleteVlanIfEntity = makeOperationDeleteVlanIfEcEntity(node.getEcNodeId(),
          l2Cp.getVlanIf().getId().getVlanIfId());
      deleteVlanIfEntityList.add(deleteVlanIfEntity);
      l2CpDao.delete(sessionWrapper, l2Cp.getId());
    } finally {
      logger.methodEnd();
    }
  }

  protected void processDeleteL2CpForPairCpFound(SessionWrapper sessionWrapper, FcL2Cp pairL2Cp) throws MsfException {
    try {
      logger.methodStart();
      FcL2CpDao l2CpDao = new FcL2CpDao();

      pairL2Cp.setEsi(null);

      OperationUpdateVlanIfEcEntity updateVlanIfEntity = makeOperationUpdateVlanIfEcEntity(sessionWrapper, pairL2Cp);
      updateVlanIfEntityList.add(updateVlanIfEntity);
      l2CpDao.update(sessionWrapper, pairL2Cp);
    } finally {
      logger.methodEnd();
    }
  }

  private boolean checkEsiClosedInSelf(String esi) throws MsfException {
    try {
      logger.methodStart();

      int clusterId = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId();
      if (esi != null) {

        return (clusterId == EsiUtil.getHigherSwClusterId(esi) && clusterId == EsiUtil.getLowerSwClusterId(esi));
      } else {
        return false;
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected FcL2Cp getPairCpFromDb(SessionWrapper sessionWrapper, FcL2CpDao l2CpDao, FcL2Cp l2Cp) throws MsfException {
    try {
      logger.methodStart();
      List<FcL2Cp> pairL2CpList = l2CpDao.readListByEsi(sessionWrapper, l2Cp.getEsi());
      for (FcL2Cp pairL2Cp : pairL2CpList) {

        if (!pairL2Cp.getId().equals(l2Cp.getId()) && pairL2Cp.getEsi().equals(l2Cp.getEsi())) {
          return pairL2Cp;
        }
      }
      return null;
    } finally {
      logger.methodEnd();
    }
  }

  protected boolean checkCreateProcessedPairCpId(String esi) {
    try {
      logger.methodStart();

      for (OperationCreateVlanIfEcEntity entity : createVlanIfEntityList) {

        if (esi != null && esi.equals(entity.getEsi())) {
          return true;
        }
      }
      return false;
    } finally {
      logger.methodEnd();
    }
  }

  private OperationQosEcEntity makeOperationQosEcEntity(FcL2Cp fcL2Cp, L2CpQosCreateEntity qosEntity)
      throws MsfException {
    OperationQosEcEntity qosEcEntity = new OperationQosEcEntity();

    if (fcL2Cp.getL2Slice().getRemarkMenu() == null && (qosEntity == null || (qosEntity.getEgressQueueMenu() == null
        && qosEntity.getEgressShapingRate() == null && qosEntity.getIngressShapingRate() == null))) {
      return null;
    }
    qosEcEntity.setRemarkMenu(fcL2Cp.getL2Slice().getRemarkMenu());
    if (qosEntity != null) {
      qosEcEntity.setEgressQueue(qosEntity.getEgressQueueMenu());
      qosEcEntity.setInflowShapingRate(qosEntity.getIngressShapingRate());
      qosEcEntity.setOutflowShapingRate(qosEntity.getEgressShapingRate());
    }
    return qosEcEntity;
  }
}
