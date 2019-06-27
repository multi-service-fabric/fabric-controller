
package msf.fc.slice.cps.l2cp;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcEdgePoint;
import msf.fc.common.data.FcIrbInstance;
import msf.fc.common.data.FcL2Cp;
import msf.fc.common.data.FcL2CpPK;
import msf.fc.common.data.FcL2Slice;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcVlanIf;
import msf.fc.common.data.FcVlanIfPK;
import msf.fc.common.util.FcIpAddressUtil;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.slices.FcIrbInstanceDao;
import msf.fc.db.dao.slices.FcL2CpDao;
import msf.fc.db.dao.slices.FcL2SliceDao;
import msf.fc.db.dao.slices.FcVlanIfDao;
import msf.fc.rest.ec.core.operation.data.OperationRequestBody;
import msf.fc.rest.ec.core.operation.data.entity.OperationBaseIfEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationCreateUpdateL2VlanIfOptionEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationCreateVlanIfEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationDeleteUpdateL2VlanIfOptionEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationDeleteVlanIfEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationIrbEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationL3VniEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationLoopbackInterfaceEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationMultiHomingEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationQosEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationUpdateVlanIfEcEntity;
import msf.fc.rest.ec.node.interfaces.vlan.data.VlanIfReadEcResponseBody;
import msf.fc.rest.ec.node.interfaces.vlan.data.VlanIfReadListEcResponseBody;
import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfBaseIfEcEntity;
import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfEcEntity;
import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfIrbEcEntity;
import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfQosEcEntity;
import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfSetValueEcEntity;
import msf.fc.rest.ec.node.nodes.data.NodeReadEcResponseBody;
import msf.fc.rest.ec.node.nodes.data.NodeReadListEcResponseBody;
import msf.fc.rest.ec.node.nodes.data.entity.NodeEcEntity;
import msf.fc.slice.cps.FcAbstractCpRunnerBase;
import msf.mfcfc.common.constant.EcCommonOperationAction;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.IrbType;
import msf.mfcfc.common.constant.PlaneBelongsTo;
import msf.mfcfc.common.constant.QInQType;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.EsiUtil;
import msf.mfcfc.common.util.IpAddressUtil;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.cps.l2cp.data.L2CpRequest;
import msf.mfcfc.slice.cps.l2cp.data.entity.L2CpIrbEntity;
import msf.mfcfc.slice.cps.l2cp.data.entity.L2CpQosCreateEntity;

/**
 * Abstract class to implement the common process of the L2CP-related
 * asynchronous runner processing in the slice management function.
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

  protected Map<Long, VlanIfReadListEcResponseBody> vlanIfReadListEcResponseBodyMap = null;

  private static final int CLAG_IF_IP_ADDRESS_PREFIX = 30;

  private static final int ASYMMETRIC_IRB_VNI_BASE = 10000;

  private static final int SYMMETRIC_IRB_VNI_BASE = 15000;

  private static final String VRF_LOOPBACK_ADDRESS_BASE = "1.1.1.0";

  private static final int VRF_LOOPBACK_ADDRESS_PREFIX = 32;

  private List<FcVlanIfPK> deletedVlanIfPkList = new ArrayList<>();

  private List<FcL2CpPK> deletedL2CpPkList = new ArrayList<>();

  protected Map<Long, NodeReadEcResponseBody> nodeReadEcResponseBodyForQinqCheckMap = new HashMap<>();

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
      entity.setClagId(l2Cp.getClagId());

      if (entity.getEsi() != null && isNecessaryMultiHomingSettingParameters(sessionWrapper, requestTargetNode)) {

        setMultiHomingSettingParameters(sessionWrapper, requestTargetNode, entity);
      }
      return entity;
    } finally {
      logger.methodEnd();
    }
  }

  protected OperationCreateVlanIfEcEntity makeCreateVlanIfEcEntityForIrbDummy(SessionWrapper sessionWrapper,
      FcVlanIf vlanIf, String irbAddress, String virtualGatewayAddress, Integer irbIpv4Prefix) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "vlanIf" }, new Object[] { sessionWrapper, vlanIf });
      FcNodeDao nodeDao = new FcNodeDao();
      FcNode requestTargetNode = nodeDao.read(sessionWrapper, vlanIf.getId().getNodeInfoId());
      OperationCreateVlanIfEcEntity entity = new OperationCreateVlanIfEcEntity();
      entity.setNodeId(String.valueOf(requestTargetNode.getEcNodeId()));
      entity.setVlanId(vlanIf.getIrbInstance().getVlanId());
      entity.setVlanIfId(String.valueOf(vlanIf.getId().getVlanIfId()));

      OperationIrbEcEntity irbEntity = new OperationIrbEcEntity();
      irbEntity.setIpv4Address(
          assignIrbDummyAddress(vlanIf.getIrbInstance().getVlanId(), irbAddress, virtualGatewayAddress, irbIpv4Prefix));
      irbEntity.setIpv4Prefix(irbIpv4Prefix);
      irbEntity.setVirtualGatewayAddress(virtualGatewayAddress);
      irbEntity.setVni(vlanIf.getIrbInstance().getVni());
      entity.setIrb(irbEntity);
      entity.setIsDummy(true);
      entity.setRouteDistinguisher(makeRouteDistinguisher(vlanIf.getIrbInstance().getL2Slice().getVrfId(),
          FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId(),
          requestTargetNode.getEcNodeId()));
      return entity;
    } finally {
      logger.methodEnd();
    }
  }

  protected void updateOperationCreateVlanIfEcEntityForIrb(OperationCreateVlanIfEcEntity createVlanIfEntity,
      FcL2Cp l2Cp, String irbIpv4Address, String virtualGatewayAddress, int ipv4Prefix) throws MsfException {
    try {
      logger.methodStart(
          new String[] { "createVlanIfEntity", "l2Cp", "irbIpv4Address", "virtualGatewayAddress", "ipv4Prefix" },
          new Object[] { createVlanIfEntity, l2Cp, irbIpv4Address, virtualGatewayAddress, ipv4Prefix });

      OperationIrbEcEntity irbEntity = new OperationIrbEcEntity();
      irbEntity.setIpv4Address(irbIpv4Address);
      irbEntity.setIpv4Prefix(ipv4Prefix);
      irbEntity.setVirtualGatewayAddress(virtualGatewayAddress);
      irbEntity.setVni(l2Cp.getVlanIf().getIrbInstance().getVni());
      createVlanIfEntity.setIrb(irbEntity);
      createVlanIfEntity
          .setRouteDistinguisher(makeRouteDistinguisher(l2Cp.getVlanIf().getIrbInstance().getL2Slice().getVrfId(),
              FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId(),
              Integer.valueOf(createVlanIfEntity.getNodeId())));
    } finally {
      logger.methodEnd();
    }
  }

  protected OperationUpdateVlanIfEcEntity createOperationUpdateVlanIfEcEntityForIrb(
      OperationCreateVlanIfEcEntity createVlanIfEntity, FcVlanIf vlanIfDummy, FcL2Cp newL2Cp, FcNode node,
      String irbIpv4Address, String virtualGatewayAddress, int ipv4Prefix) throws MsfException {
    try {
      logger.methodStart(
          new String[] { "createVlanIfEntity", "vlanIfDummy", "newL2Cp", "node", "irbIpv4Address",
              "virtualGatewayAddress", "ipv4Prefix" },
          new Object[] { createVlanIfEntity, vlanIfDummy, newL2Cp, node, irbIpv4Address, virtualGatewayAddress,
              ipv4Prefix });

      newL2Cp.setVlanIf(vlanIfDummy);

      OperationUpdateVlanIfEcEntity entity = new OperationUpdateVlanIfEcEntity();

      entity.setVlanIfId(String.valueOf(vlanIfDummy.getId().getVlanIfId()));
      entity.setNodeId(String.valueOf(node.getEcNodeId()));

      entity.setBaseIf(createVlanIfEntity.getBaseIf());

      entity.setEsi(createVlanIfEntity.getEsi());
      entity.setLacpSystemId(createVlanIfEntity.getLacpSystemId());
      entity.setClagId(createVlanIfEntity.getClagId());

      entity.setPortMode(createVlanIfEntity.getPortMode());

      entity.setQos(createVlanIfEntity.getQos());

      OperationIrbEcEntity irbEntity = new OperationIrbEcEntity();
      irbEntity.setIpv4Address(irbIpv4Address);
      irbEntity.setIpv4Prefix(ipv4Prefix);
      irbEntity.setVirtualGatewayAddress(virtualGatewayAddress);
      irbEntity.setVni(vlanIfDummy.getIrbInstance().getVni());
      entity.setIrb(irbEntity);

      return entity;
    } finally {
      logger.methodEnd();
    }
  }

  protected String makeRouteDistinguisher(int vrfId, int clusterId, int ecNodeId) {
    return vrfId + ":" + (clusterId * MULTIPLY_VALUE_FOR_RD_CALCULATION_ + ecNodeId);
  }

  protected OperationUpdateVlanIfEcEntity makeOperationUpdateVlanIfEcEntity(SessionWrapper sessionWrapper, FcL2Cp l2Cp,
      boolean isDummy) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l2Cp" }, new Object[] { sessionWrapper, l2Cp });
      FcNodeDao nodeDao = new FcNodeDao();
      FcNode pairCpTargetNode = nodeDao.read(sessionWrapper, l2Cp.getEdgePoint().getEdgePointId());
      OperationUpdateVlanIfEcEntity entity = new OperationUpdateVlanIfEcEntity();
      entity.setNodeId(String.valueOf(pairCpTargetNode.getEcNodeId()));
      entity.setVlanIfId(String.valueOf(l2Cp.getVlanIf().getId().getVlanIfId()));

      if (isDummy) {
        entity.setDummyFlag(true);
      } else {

        if (l2Cp.getEsi() == null) {
          entity.setEsi("0");
        } else {
          entity.setEsi(l2Cp.getEsi());
        }
        entity.setLacpSystemId(getNextLacpSystemId(entity.getEsi()));

        if (l2Cp.getClagId() == null) {
          entity.setClagId(-1);
        } else {
          entity.setClagId(l2Cp.getClagId());
        }
      }
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
      List<OperationUpdateVlanIfEcEntity> updateVlanIfEntityList, FcL2Slice l2Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "createVlanIfEntityList", "updateVlanIfEntityList", "l2Slice" },
          new Object[] { createVlanIfEntityList, updateVlanIfEntityList, l2Slice });
      OperationCreateUpdateL2VlanIfOptionEcEntity entity = new OperationCreateUpdateL2VlanIfOptionEcEntity();
      entity.setCreateVlanIfList(createVlanIfEntityList);
      entity.setUpdateVlanIfList(updateVlanIfEntityList);

      IrbType irbType = IrbType.getEnumFromCode(l2Slice.getIrbType());
      if (irbType.equals(IrbType.NONE)) {
        entity.setVni(l2Slice.getVni());
      } else {
        entity.setVrfId(l2Slice.getVrfId());
        entity.setPlane(PlaneBelongsTo.A_PLANE.getMessage());
        if (irbType.equals(IrbType.ASYMMETRIC)) {
          OperationLoopbackInterfaceEcEntity opeLoIfEcEntity = new OperationLoopbackInterfaceEcEntity();
          opeLoIfEcEntity
              .setAddress(IpAddressUtil.getBitsAddedIpAddress(VRF_LOOPBACK_ADDRESS_BASE, l2Slice.getVrfId()));
          opeLoIfEcEntity.setPrefix(VRF_LOOPBACK_ADDRESS_PREFIX);
          entity.setLoopbackInterface(opeLoIfEcEntity);
        } else if (irbType.equals(IrbType.SYMMETRIC)) {
          OperationL3VniEcEntity opeL3VniEcEntity = new OperationL3VniEcEntity();
          opeL3VniEcEntity.setVniId(l2Slice.getL3vni());
          opeL3VniEcEntity.setVlanId(l2Slice.getL3vniVlanId());
          entity.setL3Vni(opeL3VniEcEntity);
        }
      }

      entity.setQInQ(l2Slice.getQInQEnable());

      OperationRequestBody body = new OperationRequestBody();
      body.setAction(EcCommonOperationAction.CREATE_UPDATE_L2VLAN_IF.getMessage());
      body.setCreateUpdateL2vlanIfOption(entity);
      return JsonUtil.toJson(body);
    } finally {
      logger.methodEnd();
    }
  }

  protected String makeDeleteUpdateL2VlanIfData(List<OperationDeleteVlanIfEcEntity> deleteVlanIfEntityList,
      List<OperationUpdateVlanIfEcEntity> updateVlanIfEntityList, FcL2Slice l2Slice) {
    try {
      logger.methodStart(new String[] { "deleteVlanIfEntityList", "updateVlanIfEntityList", "l2Slice" },
          new Object[] { deleteVlanIfEntityList, updateVlanIfEntityList, l2Slice });
      OperationDeleteUpdateL2VlanIfOptionEcEntity entity = new OperationDeleteUpdateL2VlanIfOptionEcEntity();
      entity.setDeleteVlanIfList(deleteVlanIfEntityList);
      entity.setUpdateVlanIfList(updateVlanIfEntityList);

      IrbType irbType = IrbType.getEnumFromCode(l2Slice.getIrbType());
      if (irbType.equals(IrbType.NONE)) {
        entity.setVni(l2Slice.getVni());
      } else {
        entity.setVrfId(String.valueOf(l2Slice.getVrfId()));
      }

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
      int edgePointId, String portMode, int vlanId, String pairCpId, String esi, L2CpQosCreateEntity qosEntity,
      L2CpIrbEntity irbEntity, Double trafficThreshold) throws MsfException {
    try {
      logger.methodStart(
          new String[] { "sessionWrapper", "l2Slice", "node", "cpId", "edgePointId", "portMode", "vlanId", "pairCpId",
              "qosEntity" },
          new Object[] { sessionWrapper, l2Slice, node, cpId, edgePointId, portMode, vlanId, pairCpId,
              ToStringBuilder.reflectionToString(qosEntity) });

      checkL2NwConstraints(sessionWrapper, l2Slice, node, vlanId, edgePointId);

      FcL2CpDao l2CpDao = new FcL2CpDao();
      FcL2Cp newL2Cp = makeNewL2Cp(sessionWrapper, l2Slice, node, cpId, edgePointId, esi, trafficThreshold);

      checkQinQTypeConstraints(sessionWrapper, newL2Cp);

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

              checkPairCpVlanIdAndPortMode(sessionWrapper, portMode, vlanId, pairNode.getNodeInfoId(),
                  pairL2Cp.getVlanIf().getId().getVlanIfId(), null, null);
            }

            checkCreateTargetNode(sessionWrapper, pairL2Cp.getId().getCpId(), edgePointId,
                pairL2Cp.getEdgePoint().getEdgePointId());
          }
        }
      }

      OperationCreateVlanIfEcEntity createVlanIfEntity = makeOperationCreateVlanIfEcEntity(sessionWrapper, newL2Cp,
          portMode, vlanId, qosEntity);

      if (l2Slice.getIrbType() != null) {
        processCreateL2CpWithIrbEnabled(sessionWrapper, createVlanIfEntity, newL2Cp, node, vlanId, irbEntity);
      } else {
        createVlanIfEntityList.add(createVlanIfEntity);

        l2CpDao.create(sessionWrapper, newL2Cp);
      }

    } finally {
      logger.methodEnd();
    }
  }

  protected String getCpIdAndCheck(SessionWrapper sessionWrapper, String sliceId, String cpId,
      List<String> requestCpIdList) throws MsfException {
    try {
      logger.methodStart(new String[] { "sliceId", "cpId", "requestCpIdList" },
          new Object[] { sliceId, cpId, requestCpIdList });
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
      logger.methodStart(new String[] { "sliceId", "cpId" }, new Object[] { sliceId, cpId });
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

  protected Set<Integer> createClagIdSet(List<FcL2Cp> l2CpList) {
    try {
      logger.methodStart(new String[] { "l2CpList" }, new Object[] { l2CpList });
      Set<Integer> clagIdSet = new TreeSet<>();
      for (FcL2Cp l2Cp : l2CpList) {
        if (l2Cp.getClagId() != null) {
          clagIdSet.add(l2Cp.getClagId());
        }
      }
      return clagIdSet;
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
      logger.methodStart(new String[] { "node", "pairL2Cp", "newL2Cp", "portMode", "vlanId" },
          new Object[] { node, pairL2Cp, newL2Cp, portMode, vlanId });

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

      Set<Integer> clagIdSet = createClagIdSet(l2CpList);

      int clagId = getNextClagId(clagIdSet);
      newL2Cp.setClagId(clagId);
      pairL2Cp.setClagId(clagId);

      OperationUpdateVlanIfEcEntity updateVlanIfEntity = makeOperationUpdateVlanIfEcEntity(sessionWrapper, pairL2Cp,
          false);
      updateVlanIfEntityList.add(updateVlanIfEntity);

      l2CpDao.update(sessionWrapper, pairL2Cp);
    } finally {
      logger.methodEnd();
    }
  }

  protected FcL2Cp makeNewL2Cp(SessionWrapper sessionWrapper, FcL2Slice l2Slice, FcNode node, String cpId,
      int edgePointId, String esi, Double trafficThreshold) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice", "node", "cpId", "edgePointId", "esi", "trafficThreshold" },
          new Object[] { l2Slice, node, cpId, edgePointId, esi, trafficThreshold });
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
      int vlanIfId = getNextVlanIfId(sessionWrapper, vlanIfIdSet, node.getNodeInfoId());
      vlanIfPk.setVlanIfId(vlanIfId);
      vlanIfPk.setNodeInfoId(node.getNodeInfoId());
      FcVlanIf vlanIf = new FcVlanIf();
      vlanIf.setId(vlanIfPk);
      newL2Cp.setVlanIf(vlanIf);

      FcEdgePoint edgePoint = getEdgePointFromDb(sessionWrapper, edgePointId);
      newL2Cp.setEdgePoint(edgePoint);

      newL2Cp.setTrafficThreshold(trafficThreshold);
      return newL2Cp;
    } finally {
      logger.methodEnd();
    }
  }

  protected void updateL2CpWithIrb(FcL2Cp l2Cp, int vlanId) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Cp", "vlanId" }, new Object[] { l2Cp, vlanId });
      FcIrbInstance newIrbInstance = new FcIrbInstance();
      newIrbInstance.setL2Slice(l2Cp.getL2Slice());
      newIrbInstance.setNodeInfoId(l2Cp.getVlanIf().getId().getNodeInfoId());
      newIrbInstance.setVlanId(vlanId);
      newIrbInstance.setVni(getVniForIrbInstance(vlanId, l2Cp.getL2Slice().getIrbType()));
      l2Cp.getVlanIf().setIrbInstance(newIrbInstance);

    } finally {
      logger.methodEnd();
    }
  }

  protected void updateL2CpWithIrb(SessionWrapper sessionWrapper, FcL2Cp l2Cp, long nodeInfoId, int vlanIfId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Cp", "nodeInfoId", "vlanIfId" },
          new Object[] { l2Cp, nodeInfoId, vlanIfId });
      FcVlanIfDao vlanIfDao = new FcVlanIfDao();
      FcVlanIfPK pk = new FcVlanIfPK();
      pk.setNodeInfoId(nodeInfoId);
      pk.setVlanIfId(vlanIfId);
      FcVlanIf vlanIf = vlanIfDao.read(sessionWrapper, pk);
      l2Cp.getVlanIf().setIrbInstance(vlanIf.getIrbInstance());

    } finally {
      logger.methodEnd();
    }
  }

  private int getVniForIrbInstance(int vlanId, int irbType) {
    try {
      logger.methodStart(new String[] { "vlanId", "irbType" }, new Object[] { vlanId, irbType });
      if (irbType == IrbType.ASYMMETRIC.getCode()) {
        return ASYMMETRIC_IRB_VNI_BASE + vlanId;
      } else {
        return SYMMETRIC_IRB_VNI_BASE + vlanId;
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void processDeleteL2Cp(SessionWrapper sessionWrapper, FcL2Cp l2Cp, FcNode node) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Cp", "node" }, new Object[] { l2Cp, node });
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

      if (l2Cp.getL2Slice().getIrbType() != null) {
        processDeleteL2CpWithIrbEnabled(sessionWrapper, l2Cp, node, deleteVlanIfEntity);
      } else {
        deleteVlanIfEntityList.add(deleteVlanIfEntity);

        l2CpDao.delete(sessionWrapper, l2Cp.getId());
      }

    } finally {
      logger.methodEnd();
    }
  }

  protected void processDeleteL2CpForPairCpFound(SessionWrapper sessionWrapper, FcL2Cp pairL2Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "pairL2Cp" }, new Object[] { pairL2Cp });

      pairL2Cp.setEsi(null);

      pairL2Cp.setClagId(null);
      FcL2CpDao l2CpDao = new FcL2CpDao();

      OperationUpdateVlanIfEcEntity updateVlanIfEntity = makeOperationUpdateVlanIfEcEntity(sessionWrapper, pairL2Cp,
          false);
      updateVlanIfEntityList.add(updateVlanIfEntity);
      l2CpDao.update(sessionWrapper, pairL2Cp);
    } finally {
      logger.methodEnd();
    }
  }

  private boolean checkEsiClosedInSelf(String esi) throws MsfException {
    try {
      logger.methodStart(new String[] { "esi" }, new Object[] { esi });

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
      logger.methodStart(new String[] { "l2Cp" }, new Object[] { l2Cp });
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
      logger.methodStart(new String[] { "esi" }, new Object[] { esi });

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
    try {
      logger.methodStart(new String[] { "fcL2Cp", "qosEntity" }, new Object[] { fcL2Cp, qosEntity });
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
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkL2NwConstraints(SessionWrapper sessionWrapper, FcL2Slice l2Slice, FcNode node, int vlanId,
      int edgePointId) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice", "node", "vlanId", "edgePointId" },
          new Object[] { l2Slice, node, vlanId, edgePointId });

      checkVlanIdDifferentFromOtherInIrbDisabled(sessionWrapper, l2Slice, node, vlanId);

      checkEdgePointDuplicateCheck(sessionWrapper, l2Slice, edgePointId);

      NodeReadEcResponseBody nodeReadEcResponseBody = getNodeFromEc(sessionWrapper, node.getNodeInfoId());

      IrbType nodeIrbType = IrbType.getEnumFromEcMessage(nodeReadEcResponseBody.getNode().getIrbType());

      if (IrbType.ASYMMETRIC.equals(nodeIrbType)) {

        checkVlanIdInAsymmetricIrb(sessionWrapper, l2Slice, node, vlanId);
      } else if (IrbType.SYMMETRIC.equals(nodeIrbType)) {

        checkVlanIdWithL3VniVlanIdInSymmetricIrb(sessionWrapper, vlanId);

        checkVlanIdInSymmetricIrb(sessionWrapper, l2Slice, node, vlanId);
      }

      if (l2Slice.getIrbType() != null) {

        checkIrbTypeMatchNodeWithSlice(l2Slice, nodeReadEcResponseBody.getNode().getIrbType());
      } else {

        checkVlanIdSameInIrbDisabled(sessionWrapper, l2Slice, node, vlanId);
      }

    } finally {
      logger.methodEnd();
    }
  }

  protected void checkVlanIdSameInIrbDisabled(SessionWrapper sessionWrapper, FcL2Slice l2Slice, FcNode node, int vlanId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice", "node", "vlanId" }, new Object[] { l2Slice, node, vlanId });

      FcL2CpDao l2CpDao = new FcL2CpDao();
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

        int vlanIdFromEc = responseBody.getVlanIf().getVlanIdInt();
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

  protected void checkEdgePointDuplicateCheck(SessionWrapper sessionWrapper, FcL2Slice l2Slice, int edgePointId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice", "edgePointId" }, new Object[] { l2Slice, edgePointId });
      FcL2CpDao l2CpDao = new FcL2CpDao();
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

  protected void checkPairCpVlanIdAndPortMode(SessionWrapper sessionWrapper, String portMode, int vlanId,
      Long pairNodeInfoId, Integer pairCpVlanIfId, String pairCpPortMode, Integer pairCpVlanId) throws MsfException {
    try {
      logger.methodStart(
          new String[] { "portMode", "vlanId", "pairNodeInfoId", "pairCpVlanIfId", "pairCpPortMode", "pairCpVlanId" },
          new Object[] { portMode, vlanId, pairNodeInfoId, pairCpVlanIfId, pairCpPortMode, pairCpVlanId });

      if (pairCpVlanIfId != null) {

        VlanIfReadEcResponseBody pairVlanIfReadEcResponseBody = getVlanIf(sessionWrapper, pairNodeInfoId,
            pairCpVlanIfId);
        pairCpVlanId = pairVlanIfReadEcResponseBody.getVlanIf().getVlanIdInt();
        pairCpPortMode = pairVlanIfReadEcResponseBody.getVlanIf().getPortMode();
      }

      checkPairVlanId(vlanId, pairCpVlanId);

      checkPairPorMode(portMode, pairCpPortMode);

    } finally {
      logger.methodEnd();
    }
  }

  protected void checkVlanIdInAsymmetricIrb(SessionWrapper sessionWrapper, FcL2Slice l2Slice, FcNode node, int vlanId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice", "node", "vlanId" }, new Object[] { l2Slice, node, vlanId });
      FcL2CpDao l2CpDao = new FcL2CpDao();
      NodeReadListEcResponseBody nodeReadListEcResponseBody = getNodeListFromEc();
      for (NodeEcEntity nodeEcEntity : nodeReadListEcResponseBody.getNodeList()) {

        IrbType irbType = IrbType.getEnumFromEcMessage(nodeEcEntity.getIrbType());

        if (irbType.equals(IrbType.ASYMMETRIC)) {
          FcNode checkTargetNode = new FcNodeDao().readByEcNodeId(sessionWrapper,
              Integer.valueOf(nodeEcEntity.getNodeId()));
          List<FcL2Cp> l2CpList = l2CpDao.readListByNodeInfo(sessionWrapper, checkTargetNode.getNodeInfoId());
          if (!l2CpList.isEmpty()) {
            VlanIfReadListEcResponseBody responseBody = getVlanIfList(sessionWrapper, checkTargetNode.getNodeInfoId());
            for (FcL2Cp l2Cp : l2CpList) {
              if (!l2Slice.getSliceId().equals(l2Cp.getL2Slice().getSliceId())) {
                if (l2Slice.getIrbType() != null) {
                  int checkTargetVlanId = 0;
                  if (l2Cp.getL2Slice().getIrbType() != null) {
                    checkTargetVlanId = l2Cp.getVlanIf().getIrbInstance().getVlanId();
                  } else {
                    for (VlanIfEcEntity vlanIfEcEntity : responseBody.getVlanIfList()) {
                      if (vlanIfEcEntity.getVlanIfId().equals(String.valueOf(l2Cp.getVlanIf().getId().getVlanIfId()))) {
                        checkTargetVlanId = vlanIfEcEntity.getVlanIdInt();
                        break;
                      }
                    }
                  }
                  if (vlanId == checkTargetVlanId) {
                    String logMsg = MessageFormat.format(
                        "occurred l2 constraint violation of vlan id in the case of creating l2cp"
                            + " that irb enabled on asymmetric irb type leaf."
                            + " vlan id = {0}, target node id= {1}, target vlan id = {2}",
                        vlanId, checkTargetNode.getNodeId(), checkTargetVlanId);
                    logger.error(logMsg);
                    throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
                  }
                } else {
                  if (l2Cp.getL2Slice().getIrbType() != null) {
                    if (vlanId == l2Cp.getVlanIf().getIrbInstance().getVlanId()) {
                      String logMsg = MessageFormat.format(
                          "occurred l2 constraint violation of vlan id in the case of creating l2cp"
                              + " that irb disabled on asymmetric irb type leaf."
                              + " vlan id = {0}, target node id= {1}, target vlan id = {2}",
                          vlanId, checkTargetNode.getNodeId(), l2Cp.getVlanIf().getIrbInstance().getVlanId());
                      logger.error(logMsg);
                      throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
                    }
                  }
                }
              }
            }
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkVlanIdWithL3VniVlanIdInSymmetricIrb(SessionWrapper sessionWrapper, int vlanId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "vlanId" }, new Object[] { vlanId });
      FcL2SliceDao l2SliceDao = new FcL2SliceDao();
      List<FcL2Slice> l2SliceList = l2SliceDao.readListByIrbType(sessionWrapper, IrbType.SYMMETRIC.getCode());
      for (FcL2Slice l2Slice : l2SliceList) {

        if (vlanId == l2Slice.getL3vniVlanId()) {
          String logMsg = MessageFormat.format(
              "occurred l2 constraint violation of vlan id for creating l3 vni in the case of creating l2cp"
                  + " that irb enabled on symmetric irb type leaf. vlan id = {0}, target slice id = {1}",
              vlanId, l2Slice.getSliceId());
          logger.error(logMsg);
          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkVlanIdInSymmetricIrb(SessionWrapper sessionWrapper, FcL2Slice l2Slice, FcNode node, int vlanId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice", "node", "vlanId" }, new Object[] { l2Slice, node, vlanId });
      FcIrbInstanceDao irbInstanceDao = new FcIrbInstanceDao();
      List<FcIrbInstance> irbInstanceList = irbInstanceDao.readListByNodeInfoId(sessionWrapper, node.getNodeInfoId());
      for (FcIrbInstance irbInstance : irbInstanceList) {

        if (!l2Slice.getSliceId().equals(irbInstance.getL2Slice().getSliceId())) {

          if (vlanId == irbInstance.getVlanId()) {
            String logMsg = MessageFormat.format(
                "occurred l2 constraint violation of vlan id in the case of creating l2cp"
                    + " that irb enabled on symmetric irb type leaf. vlan id = {0}, target slice id = {1}",
                vlanId, l2Slice.getSliceId());
            logger.error(logMsg);
            throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkVlanIdDifferentFromOtherInIrbDisabled(SessionWrapper sessionWrapper, FcL2Slice l2Slice,
      FcNode node, int vlanId) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice", "node", "vlanId" }, new Object[] { l2Slice, node, vlanId });
      FcL2CpDao l2CpDao = new FcL2CpDao();
      List<FcL2Cp> l2CpList = l2CpDao.readListByNodeInfo(sessionWrapper, node.getNodeInfoId());

      VlanIfReadListEcResponseBody responseBody = getVlanIfList(sessionWrapper, node.getNodeInfoId());

      for (FcL2Cp l2Cp : l2CpList) {

        if (!l2Slice.getSliceId().equals(l2Cp.getL2Slice().getSliceId()) && l2Cp.getL2Slice().getIrbType() == null) {

          int vlanIdFromEc = getVlanIdFromVlanIfReadListEcResponseBody(l2Cp.getVlanIf().getId().getVlanIfId(),
              responseBody);

          if (vlanId == vlanIdFromEc) {
            String logMsg = MessageFormat
                .format("occurred l2 constraint violation of vlan id duplication in the case of creating l2cp"
                    + " that irb disabled. vlan id = {0}, target slice id = {1}", vlanId, l2Slice.getSliceId());
            logger.error(logMsg);
            throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
          }
        }
      }

    } finally {
      logger.methodEnd();
    }
  }

  protected int getVlanIdFromVlanIfReadListEcResponseBody(Integer targetVlanIfId,
      VlanIfReadListEcResponseBody responseBody) throws MsfException {
    try {
      logger.methodStart(new String[] { "targetVlanIfId", "responseBody" },
          new Object[] { targetVlanIfId, responseBody });
      if (responseBody.getVlanIfList() != null) {
        for (VlanIfEcEntity entity : responseBody.getVlanIfList()) {
          if (targetVlanIfId.equals(Integer.valueOf(entity.getVlanIfId()))) {
            return entity.getVlanIdInt();
          }
        }
      }
      String logMsg = MessageFormat
          .format("vlan id not found from vlan if information list responded from ec.vlan if id = {0}", targetVlanIfId);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkIrbTypeMatchNodeWithSlice(FcL2Slice l2Slice, String nodeIrbTypeFromEc) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice", "nodeIrbTypeFromEc" }, new Object[] { l2Slice, nodeIrbTypeFromEc });

      IrbType l2SliceIrbType = IrbType.getEnumFromCode(l2Slice.getIrbType());

      IrbType nodeIrbType = IrbType.getEnumFromEcMessage(nodeIrbTypeFromEc);

      if (!l2SliceIrbType.equals(nodeIrbType)) {
        String logMsg = MessageFormat.format("l2slice irb type {0} and node irb type {1} are mismatched.",
            l2SliceIrbType.getMessage(), nodeIrbType.getMessage());
        logger.error(logMsg);
        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  protected void checkIrbAddressVgaPrefixToMatchTarget(String requestIrbAddress, String requestVga,
      Integer requestPrefix, String existIrbAddress, String existVga, int existPrefix) throws MsfException {
    try {
      logger.methodStart(
          new String[] { "requestIrbAddress", "requestVga", "requestPrefix", "existIrbAddress", "existVga",
              "existPrefix" },
          new Object[] { requestIrbAddress, requestVga, requestPrefix, existIrbAddress, existVga, existPrefix });

      if (requestIrbAddress != null) {

        if (!requestIrbAddress.equals(existIrbAddress)) {
          String logMsg = MessageFormat.format(
              "request irb address({0}) and check target irb address({1}) are mismatched.", requestIrbAddress,
              existIrbAddress);
          logger.error(logMsg);
          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
        }
      }

      if (requestVga != null) {

        if (!requestVga.equals(existVga)) {
          String logMsg = MessageFormat.format("request vga({0}) and check target vga({1}) are mismatched.", requestVga,
              existVga);
          logger.error(logMsg);
          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
        }
      }

      if (requestPrefix != null) {

        if (!requestPrefix.equals(existPrefix)) {
          String logMsg = MessageFormat.format("request prefix({0}) and check target prefix({1}) are mismatched.",
              requestPrefix, existPrefix);
          logger.error(logMsg);
          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkNetworkAndHostAddress(String requestIrbAddress, String targetIrbAddress, int prefix)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "requestIrbAddress", "targetIrbAddress", "prefix" },
          new Object[] { requestIrbAddress, targetIrbAddress, prefix });

      if (requestIrbAddress == null) {
        String logMsg = MessageFormat.format("request irb address is null.", requestIrbAddress, targetIrbAddress);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
      }

      if (requestIrbAddress.equals(targetIrbAddress)) {
        String logMsg = MessageFormat.format("request irb address({0}) and check target irb address({1}) are matched.",
            requestIrbAddress, targetIrbAddress);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
      }

      String networkAddressFromRequestIrbAddress = IpAddressUtil.getNetworkAddress(requestIrbAddress, prefix);

      String networkAddressFromTargetIrbAddress = IpAddressUtil.getNetworkAddress(targetIrbAddress, prefix);

      if (!networkAddressFromRequestIrbAddress.equals(networkAddressFromTargetIrbAddress)) {
        String logMsg = MessageFormat.format(
            "request irb network address({0}) and check target irb network address({1}) are mismatched.",
            networkAddressFromRequestIrbAddress, networkAddressFromTargetIrbAddress);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  protected void checkCreateTargetNode(SessionWrapper sessionWrapper, String pairCpId, int edgePointId,
      int pairCpEdgePointId) throws MsfException {
    try {
      logger.methodStart(new String[] { "pairCpId", "edgePointId", "pairCpEdgePointId" },
          new Object[] { pairCpId, edgePointId, pairCpEdgePointId });
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

  protected String assignIrbDummyAddress(int irbVlanId, String irbAddress, String vga, Integer prefix)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "irbVlanId" }, new Object[] { irbVlanId });

      Set<String> sameVlanIdIrbAddressSet = new HashSet<>();

      String sameVlanIdVga = null;
      int sameVlanIdPrefix = 0;
      if (irbAddress != null) {
        sameVlanIdIrbAddressSet.add(irbAddress);
      }
      if (vga != null) {
        sameVlanIdVga = vga;
      }
      if (prefix != null) {
        sameVlanIdPrefix = prefix;
      }

      if (vlanIfReadListEcResponseBodyMap == null || vlanIfReadListEcResponseBodyMap.isEmpty()) {
        String logMsg = "vlan if readlist info is not exist.";
        logger.error(logMsg);
        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
      }
      for (Map.Entry<Long, VlanIfReadListEcResponseBody> entry : vlanIfReadListEcResponseBodyMap.entrySet()) {
        if (entry.getValue().getVlanIfList() != null) {
          for (VlanIfEcEntity entity : entry.getValue().getVlanIfList()) {

            if (entity.getIrb() != null && irbVlanId == entity.getVlanIdInt()) {

              sameVlanIdIrbAddressSet.add(entity.getIrb().getIpv4Address());

              if (entity.getIrb().getVirtualGatewayAddress() != null && sameVlanIdVga == null) {
                sameVlanIdVga = entity.getIrb().getVirtualGatewayAddress();
                sameVlanIdPrefix = entity.getIrb().getIpv4Prefix();
              }
            }
          }
        }
      }

      if (sameVlanIdIrbAddressSet.iterator().hasNext()) {

        String anyIrbAddress = sameVlanIdIrbAddressSet.iterator().next();

        String maxHostAddress = IpAddressUtil.getMaxHostAddress(anyIrbAddress, sameVlanIdPrefix);

        String minHostAddress = IpAddressUtil.getMinHostAddress(anyIrbAddress, sameVlanIdPrefix);

        logger.debug("maxHostAddress=" + maxHostAddress + " minHostAddress=" + minHostAddress);
        String checkTargetIpAddress = maxHostAddress;
        while (true) {

          if (!sameVlanIdIrbAddressSet.contains(checkTargetIpAddress) && !checkTargetIpAddress.equals(sameVlanIdVga)) {

            return checkTargetIpAddress;
          } else {

            if (checkTargetIpAddress.equals(minHostAddress)) {
              break;
            }

            checkTargetIpAddress = IpAddressUtil.getBitsAddedIpAddress(checkTargetIpAddress, -1);
          }
        }
      }

      String logMsg = MessageFormat.format("could not be assign irb dummy address. vlan id = {0}", irbVlanId);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);

    } finally {
      logger.methodEnd();
    }
  }

  protected boolean isNecessaryMultiHomingSettingParameters(SessionWrapper sessionWrapper, FcNode node)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "node" }, new Object[] { node });
      FcL2CpDao l2CpDao = new FcL2CpDao();
      List<FcL2Cp> l2CpList = l2CpDao.readListByNodeInfo(sessionWrapper, node.getNodeInfoId());
      for (FcL2Cp l2Cp : l2CpList) {

        if (l2Cp.getEsi() != null) {
          return false;
        }
      }

      return true;
    } finally {
      logger.methodEnd();
    }
  }

  protected void setMultiHomingSettingParameters(SessionWrapper sessionWrapper, FcNode node,
      OperationCreateVlanIfEcEntity createVlanIfEntity) throws MsfException {
    try {
      logger.methodStart(new String[] { "node", "createVlanIfEntity" }, new Object[] { node, createVlanIfEntity });

      NodeReadEcResponseBody nodeReadResponseBody = getNodeFromEc(sessionWrapper, node.getNodeInfoId());
      OperationMultiHomingEcEntity operationMultiHomingEcEntity = new OperationMultiHomingEcEntity();

      operationMultiHomingEcEntity.setBackupAddress(nodeReadResponseBody.getNode().getLoopbackIfAddress());

      operationMultiHomingEcEntity.setAnycastAddress(FcIpAddressUtil.getX0any(node.getNodeId()));

      int peerLeafNodeId;
      if (node.getNodeId() % 2 == 0) {
        peerLeafNodeId = node.getNodeId() - 1;
      } else {
        peerLeafNodeId = node.getNodeId() + 1;
      }
      logger.debug("perrLeafNodeId=" + peerLeafNodeId);

      operationMultiHomingEcEntity.setIfAddress(FcIpAddressUtil.getX0peer(node.getNodeId()));

      operationMultiHomingEcEntity.setPeerAddress(FcIpAddressUtil.getX0peer(peerLeafNodeId));

      operationMultiHomingEcEntity.setIfPrefix(CLAG_IF_IP_ADDRESS_PREFIX);

      createVlanIfEntity.setMultiHoming(operationMultiHomingEcEntity);

    } finally {
      logger.methodEnd();
    }
  }

  protected void processCreateL2CpWithIrbEnabled(SessionWrapper sessionWrapper,
      OperationCreateVlanIfEcEntity newCreateVlanIfEntity, FcL2Cp newL2Cp, FcNode node, int vlanId,
      L2CpIrbEntity irbEntity) throws MsfException {
    try {
      logger.methodStart(new String[] { "newCreateVlanIfEntity", "newL2Cp", "node", "vlanId", "irbEntity" },
          new Object[] { newCreateVlanIfEntity, newL2Cp, node, vlanId, irbEntity });

      FcL2CpDao l2CpDao = new FcL2CpDao();

      saveVlanIfListFromEcOnMemory(sessionWrapper, newL2Cp.getL2Slice().getSliceId(), l2CpDao);

      L2CpIrbEntity irbEntityAfterCheck = checkIrbAddressParameter(sessionWrapper, irbEntity, newL2Cp.getL2Slice(),
          node, vlanId);

    	FcVlanIfDao vlanIfDao = new FcVlanIfDao();

      checkAndCreateIrbDummyInterface(sessionWrapper, vlanIfDao, l2CpDao, newL2Cp, node, vlanId, irbEntityAfterCheck);

      checkAndCreateL2CpWithIrbEnabled(sessionWrapper, vlanIfDao, l2CpDao, newL2Cp, node, vlanId, irbEntityAfterCheck,
          newCreateVlanIfEntity);
    } finally {
      logger.methodEnd();
    }
  }

  protected void processDeleteL2CpWithIrbEnabled(SessionWrapper sessionWrapper, FcL2Cp l2Cp, FcNode node,
      OperationDeleteVlanIfEcEntity deleteVlanIfEntity) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Cp", "node", "deleteVlanIfEntity" },
          new Object[] { l2Cp, node, deleteVlanIfEntity });

      FcL2CpDao l2CpDao = new FcL2CpDao();
      FcVlanIfDao vlanIfDao = new FcVlanIfDao();

      int vlanId = l2Cp.getVlanIf().getIrbInstance().getVlanId();

      saveVlanIfListFromEcOnMemory(sessionWrapper, l2Cp.getL2Slice().getSliceId(), l2CpDao);

      checkAndDeleteIrbDummyInterface(sessionWrapper, vlanIfDao, l2CpDao, l2Cp, node, vlanId);

      checkAndDeleteL2CpWithIrbEnabled(sessionWrapper, l2CpDao, l2Cp, node, vlanId, deleteVlanIfEntity);

    } finally {
      logger.methodEnd();
    }
  }

  protected void saveVlanIfListFromEcOnMemory(SessionWrapper sessionWrapper, String sliceId, FcL2CpDao l2CpDao)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sliceId" }, new Object[] { sliceId });

      if (vlanIfReadListEcResponseBodyMap == null) {
        vlanIfReadListEcResponseBodyMap = new HashMap<>();

        List<FcL2Cp> l2CpList = l2CpDao.readListBySliceId(sessionWrapper, sliceId);

        Set<Long> nodeInfoIdSet = new HashSet<>();

        for (FcL2Cp l2Cp : l2CpList) {
          nodeInfoIdSet.add(getNodeFromL2Cp(l2Cp).getNodeInfoId());
        }
        for (Long nodeInfoId : nodeInfoIdSet) {
          VlanIfReadListEcResponseBody responseBody = getVlanIfList(sessionWrapper, nodeInfoId);
          vlanIfReadListEcResponseBodyMap.put(nodeInfoId, responseBody);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected L2CpIrbEntity checkIrbAddressParameter(SessionWrapper sessionWrapper, L2CpIrbEntity originalIrbEntity,
      FcL2Slice l2Slice, FcNode node, int vlanId) throws MsfException {
    try {
      logger.methodStart(new String[] { "originalIrbEntity", "l2Slice", "node", "vlanId" },
          new Object[] { originalIrbEntity, l2Slice, node, vlanId });

      String irbAddress = null;
      String irbVga = null;
      Integer irbPrefix = null;
      if (originalIrbEntity != null) {
        irbAddress = originalIrbEntity.getIrbIpv4Address();
        irbVga = originalIrbEntity.getVgaIpv4Address();
        irbPrefix = originalIrbEntity.getIpv4AddressPrefix();
      }

      VlanIfReadListEcResponseBody responseBody = vlanIfReadListEcResponseBodyMap.get(node.getNodeInfoId());
      if (responseBody != null && responseBody.getVlanIfList() != null) {
        for (VlanIfEcEntity ecEntity : responseBody.getVlanIfList()) {

          if (vlanId == ecEntity.getVlanIdInt() && ecEntity.getIrb() != null) {

            if (ecEntity.getIrb().getVirtualGatewayAddress() != null) {

              checkIrbAddressVgaPrefixToMatchTarget(irbAddress, irbVga, irbPrefix, ecEntity.getIrb().getIpv4Address(),
                  ecEntity.getIrb().getVirtualGatewayAddress(), ecEntity.getIrb().getIpv4Prefix());

              irbAddress = ecEntity.getIrb().getIpv4Address();
              irbVga = ecEntity.getIrb().getVirtualGatewayAddress();
              irbPrefix = ecEntity.getIrb().getIpv4Prefix();

              break;
            } else {

              if (irbAddress == null) {
                irbAddress = ecEntity.getIrb().getIpv4Address();
              }
            }
          }
        }
      }

      for (Map.Entry<Long, VlanIfReadListEcResponseBody> entry : vlanIfReadListEcResponseBodyMap.entrySet()) {

        if (!entry.getKey().equals(node.getNodeInfoId()) && entry.getValue().getVlanIfList() != null) {
          for (VlanIfEcEntity ecEntity : entry.getValue().getVlanIfList()) {

            int vlanIfId = Integer.valueOf(ecEntity.getVlanIfId());
            if (ecEntity.getIrb() != null && vlanId == ecEntity.getVlanIdInt()
                && isVlanIfMemberOfSlice(sessionWrapper, entry.getKey(), vlanIfId, l2Slice)) {
              if (ecEntity.getIrb().getVirtualGatewayAddress() != null) {

                checkIrbAddressVgaPrefixToMatchTarget(null, irbVga, irbPrefix, ecEntity.getIrb().getIpv4Address(),
                    ecEntity.getIrb().getVirtualGatewayAddress(), ecEntity.getIrb().getIpv4Prefix());

                irbVga = ecEntity.getIrb().getVirtualGatewayAddress();
                irbPrefix = ecEntity.getIrb().getIpv4Prefix();
              }

              checkNetworkAndHostAddress(irbAddress, ecEntity.getIrb().getIpv4Address(),
                  ecEntity.getIrb().getIpv4Prefix());
            }
          }
        }
      }

      if (irbAddress == null || irbVga == null || irbPrefix == null) {
        String logMsg = MessageFormat.format("requested irb parameters specification condition do not meet. "
            + "irb address = {0}, vga = {1}, irb address prefix = {2}", irbAddress, irbVga, irbPrefix);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
      }

      L2CpIrbEntity entityAfterCheck = new L2CpIrbEntity();
      entityAfterCheck.setIrbIpv4Address(irbAddress);
      entityAfterCheck.setVgaIpv4Address(irbVga);
      entityAfterCheck.setIpv4AddressPrefix(irbPrefix);
      return entityAfterCheck;
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkAndCreateIrbDummyInterface(SessionWrapper sessionWrapper, FcVlanIfDao vlanIfDao,
      FcL2CpDao l2CpDao, FcL2Cp newL2Cp, FcNode node, int vlanId, L2CpIrbEntity irbEntity) throws MsfException {
    try {
      logger.methodStart(new String[] { "newL2Cp", "node", "vlanId", "irbEntity" },
          new Object[] { newL2Cp, node, vlanId, irbEntity });

      if (newL2Cp.getL2Slice().getIrbType().equals(IrbType.ASYMMETRIC.getCode())) {

        Set<Integer> irbDummyVlanIdSet = new HashSet<>();
        for (Map.Entry<Long, VlanIfReadListEcResponseBody> entry : vlanIfReadListEcResponseBodyMap.entrySet()) {

          if (node.getNodeInfoId().equals(entry.getKey())) {
            continue;
          }

          if (isExistL2CpOnNode(sessionWrapper, l2CpDao, newL2Cp.getL2Slice(), entry.getKey(), null)) {

            boolean isExistVlanIfSameVlanAndSlice = false;
            if (entry.getValue().getVlanIfList() != null) {
              for (VlanIfEcEntity ecEntity : entry.getValue().getVlanIfList()) {

                if (ecEntity.getIrb() != null && isVlanIfMemberOfSlice(sessionWrapper, entry.getKey(),
                    Integer.valueOf(ecEntity.getVlanIfId()), newL2Cp.getL2Slice())) {

                  if (vlanId == ecEntity.getVlanIdInt()) {

                    isExistVlanIfSameVlanAndSlice = true;
                  } else {

                    if (!isExistL2CpOnNode(sessionWrapper, l2CpDao, newL2Cp.getL2Slice(), node.getNodeInfoId(), null)) {

                      if (!irbDummyVlanIdSet.contains(ecEntity.getVlanIdInt())) {
                        irbDummyVlanIdSet.add(ecEntity.getVlanIdInt());

                        FcVlanIf newVlanIfForIrbDummy = makeNewVlanIfForIrbDummy(sessionWrapper, newL2Cp.getL2Slice(),
                            node.getNodeInfoId(), ecEntity.getVlanIdInt());

                        OperationCreateVlanIfEcEntity vlanIfEcEntityDummy = makeCreateVlanIfEcEntityForIrbDummy(
                            sessionWrapper, newVlanIfForIrbDummy, ecEntity.getIrb().getIpv4Address(),
                            ecEntity.getIrb().getVirtualGatewayAddress(), ecEntity.getIrb().getIpv4Prefix());
                        createVlanIfEntityList.add(vlanIfEcEntityDummy);
                        vlanIfDao.create(sessionWrapper, newVlanIfForIrbDummy);

                        addVlanIfEcEntity(node.getNodeInfoId(), vlanIfEcEntityDummy);
                      }
                    }
                  }
                }
              }
            }

            if (!isExistVlanIfSameVlanAndSlice) {

              FcVlanIf newVlanIfForIrbDummy = makeNewVlanIfForIrbDummy(sessionWrapper, newL2Cp.getL2Slice(),
                  entry.getKey(), vlanId);

              OperationCreateVlanIfEcEntity vlanIfEcEntityDummy = makeCreateVlanIfEcEntityForIrbDummy(sessionWrapper,
                  newVlanIfForIrbDummy, irbEntity.getIrbIpv4Address(), irbEntity.getVgaIpv4Address(),
                  irbEntity.getIpv4AddressPrefix());
              createVlanIfEntityList.add(vlanIfEcEntityDummy);
              vlanIfDao.create(sessionWrapper, newVlanIfForIrbDummy);

              addVlanIfEcEntity(entry.getKey(), vlanIfEcEntityDummy);
            }
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkAndDeleteIrbDummyInterface(SessionWrapper sessionWrapper, FcVlanIfDao vlanIfDao,
      FcL2CpDao l2CpDao, FcL2Cp l2Cp, FcNode node, int vlanId) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Cp", "node", "vlanId" }, new Object[] { l2Cp, node, vlanId });

      if (l2Cp.getL2Slice().getIrbType().equals(IrbType.ASYMMETRIC.getCode())) {

        if (!isExistL2CpOnNode(sessionWrapper, l2CpDao, l2Cp.getL2Slice(), node.getNodeInfoId(),
            l2Cp.getId().getCpId())) {

          for (FcIrbInstance irbInstance : l2Cp.getL2Slice().getIrbInstances()) {

            if (irbInstance.getVlanId() != vlanId && irbInstance.getNodeInfoId().equals(node.getNodeInfoId())) {
              for (FcVlanIf vlanIf : irbInstance.getVlanIfs()) {
                if (deletedVlanIfPkList.contains(vlanIf.getId())) {
                  logger.debug("skiped:" + vlanIf.getId());
                  continue;
                }

                OperationDeleteVlanIfEcEntity vlanIfEntityDummy = makeOperationDeleteVlanIfEcEntity(node.getEcNodeId(),
                    vlanIf.getId().getVlanIfId());
                deleteVlanIfEntityList.add(vlanIfEntityDummy);

                vlanIfDao.delete(sessionWrapper, vlanIf.getId());
                deletedVlanIfPkList.add(vlanIf.getId());

                deleteVlanIfReadEntity(node.getNodeInfoId(), vlanIf.getId().getVlanIfId());
              }
            }
          }
        }

        boolean isFoundSameVlanIdCp = false;
        for (FcL2Cp tmpL2Cp : l2Cp.getL2Slice().getL2Cps()) {

          if (tmpL2Cp.getId().getCpId().equals(l2Cp.getId().getCpId())) {
            continue;
          }

          if (deletedL2CpPkList.contains(tmpL2Cp.getId())) {
            continue;
          }

          if (tmpL2Cp.getVlanIf().getIrbInstance().getVlanId().equals(vlanId)) {
            logger.debug("found l2cp. tmpL2Cp = " + ToStringBuilder.reflectionToString(tmpL2Cp.getId()));
            isFoundSameVlanIdCp = true;
            break;
          }
        }

        logger.debug("isFoundSameVlanIdCp=" + isFoundSameVlanIdCp);

        if (!isFoundSameVlanIdCp) {

          for (Map.Entry<Long, VlanIfReadListEcResponseBody> entry : vlanIfReadListEcResponseBodyMap.entrySet()) {

            if (entry.getKey().equals(node.getNodeInfoId())) {
              continue;
            }
            if (entry.getValue().getVlanIfList() != null) {
              Iterator<VlanIfEcEntity> ite = entry.getValue().getVlanIfList().iterator();
              while (ite.hasNext()) {
                VlanIfEcEntity ecEntity = ite.next();

                if (isVlanIfMemberOfSlice(sessionWrapper, entry.getKey(), Integer.valueOf(ecEntity.getVlanIfId()),
                    l2Cp.getL2Slice())) {

                  if (vlanId == ecEntity.getVlanIdInt()) {
                    FcNodeDao nodeDao = new FcNodeDao();
                    FcNode targetNode = nodeDao.read(sessionWrapper, entry.getKey());
                    Integer vlanIfId = Integer.valueOf(ecEntity.getVlanIfId());

                    OperationDeleteVlanIfEcEntity vlanIfEntityDummy = makeOperationDeleteVlanIfEcEntity(
                        targetNode.getEcNodeId(), vlanIfId);
                    deleteVlanIfEntityList.add(vlanIfEntityDummy);

                    FcVlanIfPK pk = new FcVlanIfPK();
                    pk.setNodeInfoId(entry.getKey());
                    pk.setVlanIfId(vlanIfId);
                    vlanIfDao.delete(sessionWrapper, pk);
                    deletedVlanIfPkList.add(pk);

                    ite.remove();
                  }
                }
              }
            }
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkAndCreateL2CpWithIrbEnabled(SessionWrapper sessionWrapper, FcVlanIfDao vlanIfDao,
      FcL2CpDao l2CpDao, FcL2Cp newL2Cp, FcNode node, int vlanId, L2CpIrbEntity irbEntity,
      OperationCreateVlanIfEcEntity newCreateVlanIfEntity) throws MsfException {
    logger.methodStart(new String[] { "newL2Cp", "node", "vlanId", "irbEntity", "newCreateVlanIfEntity" },
        new Object[] { newL2Cp, node, vlanId, irbEntity, newCreateVlanIfEntity });
    try {

      if (!isExistL2CpOnNode(sessionWrapper, l2CpDao, newL2Cp.getL2Slice(), node.getNodeInfoId(), null)) {

        updateL2CpWithIrb(newL2Cp, vlanId);

        updateOperationCreateVlanIfEcEntityForIrb(newCreateVlanIfEntity, newL2Cp, irbEntity.getIrbIpv4Address(),
            irbEntity.getVgaIpv4Address(), irbEntity.getIpv4AddressPrefix());
        createVlanIfEntityList.add(newCreateVlanIfEntity);
        l2CpDao.create(sessionWrapper, newL2Cp);

        addVlanIfEcEntity(node.getNodeInfoId(), newCreateVlanIfEntity);
      } else {

        VlanIfEcEntity ecEntity = getSameVlanIdVlanIf(vlanId, vlanIfReadListEcResponseBodyMap.get(node.getNodeInfoId()),
            false);
        if (ecEntity != null) {

          updateL2CpWithIrb(sessionWrapper, newL2Cp, node.getNodeInfoId(), Integer.valueOf(ecEntity.getVlanIfId()));

          updateOperationCreateVlanIfEcEntityForIrb(newCreateVlanIfEntity, newL2Cp, irbEntity.getIrbIpv4Address(),
              irbEntity.getVgaIpv4Address(), irbEntity.getIpv4AddressPrefix());
          createVlanIfEntityList.add(newCreateVlanIfEntity);
          l2CpDao.create(sessionWrapper, newL2Cp);

          addVlanIfEcEntity(node.getNodeInfoId(), newCreateVlanIfEntity);
        } else {

          if (newL2Cp.getL2Slice().getIrbType().equals(IrbType.ASYMMETRIC.getCode())) {

            VlanIfEcEntity ecEntityIrbDummy = getSameVlanIdVlanIf(vlanId,
                vlanIfReadListEcResponseBodyMap.get(node.getNodeInfoId()), true);
            if (ecEntityIrbDummy != null) {

              FcVlanIfPK vlanIfPk = new FcVlanIfPK();
              vlanIfPk.setNodeInfoId(node.getNodeInfoId());
              vlanIfPk.setVlanIfId(Integer.valueOf(ecEntityIrbDummy.getVlanIfId()));
              FcVlanIf vlanIfDummy = vlanIfDao.read(sessionWrapper, vlanIfPk);

              Iterator<OperationCreateVlanIfEcEntity> createIterator = createVlanIfEntityList.iterator();
              boolean isFoundIrbDummyInCreateVlanIfEntityList = false;
              while (createIterator.hasNext()) {
                OperationCreateVlanIfEcEntity createEntity = createIterator.next();

                if (createEntity.getIsDummy() != null && createEntity.getIsDummy() == true
                    && createEntity.getVlanId().equals(vlanId)
                    && createEntity.getNodeId().equals(node.getNodeId().toString())) {

                  createIterator.remove();

                  newL2Cp.setVlanIf(vlanIfDummy);
                  newCreateVlanIfEntity.setVlanIfId(String.valueOf(vlanIfDummy.getId().getVlanIfId()));

                  updateOperationCreateVlanIfEcEntityForIrb(newCreateVlanIfEntity, newL2Cp,
                      irbEntity.getIrbIpv4Address(), irbEntity.getVgaIpv4Address(), irbEntity.getIpv4AddressPrefix());
                  createVlanIfEntityList.add(newCreateVlanIfEntity);
                  l2CpDao.create(sessionWrapper, newL2Cp);

                  deleteVlanIfReadEntity(node.getNodeInfoId(), vlanIfDummy.getId().getVlanIfId());

                  addVlanIfEcEntity(node.getNodeInfoId(), newCreateVlanIfEntity);

                  isFoundIrbDummyInCreateVlanIfEntityList = true;
                  break;
                }
              }

              if (!isFoundIrbDummyInCreateVlanIfEntityList) {

                OperationUpdateVlanIfEcEntity updateVlanIfEcEntity = createOperationUpdateVlanIfEcEntityForIrb(
                    newCreateVlanIfEntity, vlanIfDummy, newL2Cp, node, irbEntity.getIrbIpv4Address(),
                    irbEntity.getVgaIpv4Address(), irbEntity.getIpv4AddressPrefix());
                updateVlanIfEntityList.add(updateVlanIfEcEntity);

                l2CpDao.create(sessionWrapper, newL2Cp);

                VlanIfBaseIfEcEntity vlanIfBaseIfEcEntity = new VlanIfBaseIfEcEntity();
                vlanIfBaseIfEcEntity.setIfId(updateVlanIfEcEntity.getBaseIf().getIfId());
                vlanIfBaseIfEcEntity.setIfType(updateVlanIfEcEntity.getBaseIf().getIfType());
                vlanIfBaseIfEcEntity.setNodeId(updateVlanIfEcEntity.getBaseIf().getNodeId());
                ecEntityIrbDummy.setBaseIf(vlanIfBaseIfEcEntity);
                ecEntityIrbDummy.getIrb().setVirtualGatewayAddress(irbEntity.getVgaIpv4Address());
                ecEntityIrbDummy.getIrb().setVirtualGatewayPrefix(irbEntity.getIpv4AddressPrefix());
              }
            } else {

              updateL2CpWithIrb(newL2Cp, vlanId);

              updateOperationCreateVlanIfEcEntityForIrb(newCreateVlanIfEntity, newL2Cp, irbEntity.getIrbIpv4Address(),
                  irbEntity.getVgaIpv4Address(), irbEntity.getIpv4AddressPrefix());
              createVlanIfEntityList.add(newCreateVlanIfEntity);
              l2CpDao.create(sessionWrapper, newL2Cp);

              addVlanIfEcEntity(node.getNodeInfoId(), newCreateVlanIfEntity);
            }
          } else {

            updateL2CpWithIrb(newL2Cp, vlanId);

            updateOperationCreateVlanIfEcEntityForIrb(newCreateVlanIfEntity, newL2Cp, irbEntity.getIrbIpv4Address(),
                irbEntity.getVgaIpv4Address(), irbEntity.getIpv4AddressPrefix());
            createVlanIfEntityList.add(newCreateVlanIfEntity);
            l2CpDao.create(sessionWrapper, newL2Cp);

            addVlanIfEcEntity(node.getNodeInfoId(), newCreateVlanIfEntity);
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkAndDeleteL2CpWithIrbEnabled(SessionWrapper sessionWrapper, FcL2CpDao l2CpDao, FcL2Cp l2Cp,
      FcNode node, int vlanId, OperationDeleteVlanIfEcEntity deleteVlanIfEntity) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Cp", "node", "vlanId", "deleteVlanIfEntity" },
          new Object[] { ToStringBuilder.reflectionToString(l2Cp.getId()), node, vlanId, deleteVlanIfEntity });

      if (l2Cp.getL2Slice().getIrbType().equals(IrbType.ASYMMETRIC.getCode())) {

        boolean isExistL2CpOnSameNodeWithDifferentVlanId = false;

        boolean isExistL2CpOnSameNodeWithSameVlanId = false;

        boolean isExistL2CpOnOtherNodeWithSameVlanId = false;
        logger.debug("deletedVlanIfPkList=" + ToStringBuilder.reflectionToString(deletedVlanIfPkList));
        for (FcIrbInstance irbInstance : l2Cp.getL2Slice().getIrbInstances()) {

          if (!irbInstance.getVlanId().equals(vlanId) && irbInstance.getNodeInfoId().equals(node.getNodeInfoId())) {

            for (FcVlanIf vlanIf : irbInstance.getVlanIfs()) {
              if (deletedVlanIfPkList.contains(vlanIf.getId())) {
                logger.debug("skiped:" + ToStringBuilder.reflectionToString(vlanIf.getId()));
                continue;
              }
              if (vlanIf.getL2Cps() != null && !vlanIf.getL2Cps().isEmpty()) {
                isExistL2CpOnSameNodeWithDifferentVlanId = true;
              }
            }
          }

          if (irbInstance.getVlanId().equals(vlanId) && irbInstance.getNodeInfoId().equals(node.getNodeInfoId())) {

            for (FcVlanIf vlanIf : irbInstance.getVlanIfs()) {
              if (deletedVlanIfPkList.contains(vlanIf.getId())) {
                logger.debug("skiped:" + ToStringBuilder.reflectionToString(vlanIf.getId()));
                continue;
              }

              if (vlanIf.getId().getNodeInfoId().equals(l2Cp.getVlanIf().getId().getNodeInfoId())
                  && vlanIf.getL2Cps() != null && !vlanIf.getL2Cps().isEmpty()) {
                isExistL2CpOnSameNodeWithSameVlanId = true;
              }
            }
          }

          if (!irbInstance.getNodeInfoId().equals(node.getNodeInfoId()) && irbInstance.getVlanId().equals(vlanId)) {
            for (FcVlanIf vlanIf : irbInstance.getVlanIfs()) {
              if (deletedVlanIfPkList.contains(vlanIf.getId())) {
                logger.debug("skiped:" + ToStringBuilder.reflectionToString(vlanIf.getId()));
                continue;
              }

              if (!vlanIf.getId().getNodeInfoId().equals(l2Cp.getVlanIf().getId().getNodeInfoId())
                  && vlanIf.getL2Cps() != null && !vlanIf.getL2Cps().isEmpty()) {
                isExistL2CpOnOtherNodeWithSameVlanId = true;
              }
            }
          }
        }

        if (isExistL2CpOnSameNodeWithDifferentVlanId && isExistL2CpOnSameNodeWithSameVlanId
            && isExistL2CpOnOtherNodeWithSameVlanId) {
          logger.debug("all true.");
          OperationUpdateVlanIfEcEntity updateVlanIfEntity = makeOperationUpdateVlanIfEcEntity(sessionWrapper, l2Cp,
              true);
          updateVlanIfEntityList.add(updateVlanIfEntity);

          l2CpDao.delete(sessionWrapper, l2Cp.getId(), true);
          deletedL2CpPkList.add(l2Cp.getId());

          VlanIfEcEntity ecEntity = getSameVlanIdVlanIf(vlanId,
              vlanIfReadListEcResponseBodyMap.get(node.getNodeInfoId()), false);
          if (ecEntity != null) {

            ecEntity.setBaseIf(null);
            ecEntity.getIrb().setVirtualGatewayAddress(null);
            ecEntity.getIrb().setVirtualGatewayPrefix(null);
          }
        } else {

          logger.debug("isExistL2CpOnSameNodeWithDifferentVlanId:" + isExistL2CpOnSameNodeWithDifferentVlanId
              + " isExistL2CpOnSameNodeWithSameVlanId:" + isExistL2CpOnSameNodeWithSameVlanId
              + " isExistL2CpOnOtherNodeWithSameVlanId:" + isExistL2CpOnOtherNodeWithSameVlanId);
          deleteVlanIfEntityList.add(deleteVlanIfEntity);

          l2CpDao.delete(sessionWrapper, l2Cp.getId(), false);
          deletedL2CpPkList.add(l2Cp.getId());
          deletedVlanIfPkList.add(l2Cp.getVlanIf().getId());
          deleteVlanIfReadEntity(node.getNodeInfoId(), l2Cp.getVlanIf().getId().getVlanIfId());
        }
      } else {
        deleteVlanIfEntityList.add(deleteVlanIfEntity);
        l2CpDao.delete(sessionWrapper, l2Cp.getId(), false);
        deletedL2CpPkList.add(l2Cp.getId());
        deletedVlanIfPkList.add(l2Cp.getVlanIf().getId());
        deleteVlanIfReadEntity(node.getNodeInfoId(), l2Cp.getVlanIf().getId().getVlanIfId());
      }
      Iterator<OperationUpdateVlanIfEcEntity> updateIterator = updateVlanIfEntityList.iterator();
      while (updateIterator.hasNext()) {
        OperationUpdateVlanIfEcEntity updateEntity = updateIterator.next();
        for (OperationDeleteVlanIfEcEntity deleteEntity : deleteVlanIfEntityList) {

          if (updateEntity.getNodeId().equals(deleteEntity.getNodeId())
              && updateEntity.getVlanIfId().equals(deleteEntity.getVlanIfId())) {
            updateIterator.remove();
            break;
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private FcNode getNodeFromL2Cp(FcL2Cp l2Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Cp" }, new Object[] { l2Cp });
      if (l2Cp.getEdgePoint().getPhysicalIf() != null) {
        return l2Cp.getEdgePoint().getPhysicalIf().getNode();
      } else if (l2Cp.getEdgePoint().getLagIf() != null) {
        return l2Cp.getEdgePoint().getLagIf().getNode();
      } else {
        return l2Cp.getEdgePoint().getBreakoutIf().getNode();
      }
    } finally {
      logger.methodEnd();
    }
  }

  private boolean isVlanIfMemberOfSlice(SessionWrapper sessionWrapper, long nodeInfoId, int vlanIfId, FcL2Slice l2Slice)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "nodeInfoId", "vlanIfId", "l2Slice" },
          new Object[] { nodeInfoId, vlanIfId, l2Slice });
      FcVlanIfDao vlanIfDao = new FcVlanIfDao();
      FcVlanIfPK vlanIfPk = new FcVlanIfPK();
      vlanIfPk.setNodeInfoId(nodeInfoId);
      vlanIfPk.setVlanIfId(vlanIfId);
      FcVlanIf vlanIf = vlanIfDao.read(sessionWrapper, vlanIfPk);
      if (vlanIf == null) {
        return false;
      }

      if (deletedVlanIfPkList.contains(vlanIf.getId())) {

        return false;
      }

      if (vlanIf.getL2Cps() != null && !vlanIf.getL2Cps().isEmpty()
          && vlanIf.getL2Cps().get(0).getL2Slice().getSliceId().equals(l2Slice.getSliceId())) {
        return true;
      } else if (vlanIf.getIrbInstance() != null
          && vlanIf.getIrbInstance().getL2Slice().getSliceId().equals(l2Slice.getSliceId())) {

        return true;
      } else {
        return false;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private boolean isExistL2CpOnNode(SessionWrapper sessionWrapper, FcL2CpDao l2CpDao, FcL2Slice l2Slice,
      Long nodeInfoId, String excludeCpId) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice", "nodeInfoId", "excludeCpId" },
          new Object[] { l2Slice, nodeInfoId, excludeCpId });
      List<FcL2Cp> l2CpList = l2CpDao.readListByNodeInfo(sessionWrapper, l2Slice.getSliceId(), nodeInfoId);

      if (!l2CpList.isEmpty()) {

        if (excludeCpId != null) {
          for (FcL2Cp l2Cp : l2CpList) {

            if (!l2Cp.getId().getCpId().equals(excludeCpId)) {
              return true;
            }
          }
          return false;
        }
        return true;
      } else {
        return false;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private FcVlanIf makeNewVlanIfForIrbDummy(SessionWrapper sessionWrapper, FcL2Slice l2Slice, Long nodeInfoId,
      int vlanId) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice", "nodeInfoId", "vlanId" },
          new Object[] { l2Slice, nodeInfoId, vlanId });

      FcVlanIfPK vlanIfPk = new FcVlanIfPK();
      Set<String> vlanIfIdSet = createVlanIfIdSet(sessionWrapper, nodeInfoId);
      int vlanIfId = getNextVlanIfId(sessionWrapper, vlanIfIdSet, nodeInfoId);
      vlanIfPk.setVlanIfId(vlanIfId);
      vlanIfPk.setNodeInfoId(nodeInfoId);
      FcVlanIf vlanIf = new FcVlanIf();
      vlanIf.setId(vlanIfPk);

      FcIrbInstance irbInstance = new FcIrbInstance();
      irbInstance.setL2Slice(l2Slice);
      irbInstance.setNodeInfoId(nodeInfoId);
      irbInstance.setVlanId(vlanId);
      irbInstance.setVni(getVniForIrbInstance(vlanId, IrbType.ASYMMETRIC.getCode()));

      vlanIf.setIrbInstance(irbInstance);
      return vlanIf;
    } finally {
      logger.methodEnd();
    }
  }

  private VlanIfEcEntity getSameVlanIdVlanIf(int vlanId, VlanIfReadListEcResponseBody vlanIfReadListEcResponseBody,
      boolean isDummy) {
    try {
      logger.methodStart(new String[] { "vlanId", "vlanIfReadListEcResponseBody", "isDummy" },
          new Object[] { vlanId, vlanIfReadListEcResponseBody, isDummy });
      if (vlanIfReadListEcResponseBody != null && vlanIfReadListEcResponseBody.getVlanIfList() != null) {
        for (VlanIfEcEntity ecEntity : vlanIfReadListEcResponseBody.getVlanIfList()) {
          if (vlanId == ecEntity.getVlanIdInt() && ecEntity.getIrb() != null) {
            if (isDummy) {

              if (ecEntity.getIrb().getVirtualGatewayAddress() == null) {
                return ecEntity;
              }
            } else {

              if (ecEntity.getIrb().getVirtualGatewayAddress() != null) {
                return ecEntity;
              }
            }
          }
        }
      }
      return null;
    } finally {
      logger.methodEnd();
    }
  }

  private void addVlanIfEcEntity(long nodeInfoId, OperationCreateVlanIfEcEntity createVlanIfEntity) {
    try {
      logger.methodStart(new String[] { "nodeInfoId", "createVlanIfEntity" },
          new Object[] { nodeInfoId, createVlanIfEntity });

      VlanIfReadListEcResponseBody responseBody = vlanIfReadListEcResponseBodyMap.get(nodeInfoId);

      if (responseBody == null) {
        responseBody = new VlanIfReadListEcResponseBody();
        vlanIfReadListEcResponseBodyMap.put(nodeInfoId, responseBody);
      }
      if (responseBody.getVlanIfList() == null) {
        responseBody.setVlanIfList(new ArrayList<>());
      }

      VlanIfEcEntity vlanIfEcEntity = new VlanIfEcEntity();
      vlanIfEcEntity.setPortMode(createVlanIfEntity.getPortMode());
      vlanIfEcEntity.setVlanId(String.valueOf(createVlanIfEntity.getVlanId()));
      vlanIfEcEntity.setVlanIfId(createVlanIfEntity.getVlanIfId());

      if (createVlanIfEntity.getBaseIf() != null) {
        VlanIfBaseIfEcEntity vlanIfBaseIfEcEntity = new VlanIfBaseIfEcEntity();
        vlanIfBaseIfEcEntity.setIfId(createVlanIfEntity.getBaseIf().getIfId());
        vlanIfBaseIfEcEntity.setIfType(createVlanIfEntity.getBaseIf().getIfType());
        vlanIfBaseIfEcEntity.setNodeId(createVlanIfEntity.getBaseIf().getNodeId());
        vlanIfEcEntity.setBaseIf(vlanIfBaseIfEcEntity);
      }

      if (createVlanIfEntity.getIrb() != null) {
        VlanIfIrbEcEntity vlanIfIrbEcEntity = new VlanIfIrbEcEntity();
        vlanIfIrbEcEntity.setIpv4Address(createVlanIfEntity.getIrb().getIpv4Address());
        vlanIfIrbEcEntity.setIpv4Prefix(createVlanIfEntity.getIrb().getIpv4Prefix());
        if (createVlanIfEntity.getIsDummy() == null || createVlanIfEntity.getIsDummy() == false) {
          vlanIfIrbEcEntity.setVirtualGatewayAddress(createVlanIfEntity.getIrb().getVirtualGatewayAddress());
        }
        vlanIfEcEntity.setIrb(vlanIfIrbEcEntity);
      }

      if (createVlanIfEntity.getQos() != null) {
        VlanIfSetValueEcEntity vlanIfSetValueEcEntity = new VlanIfSetValueEcEntity();
        vlanIfSetValueEcEntity.setEgressMenu(createVlanIfEntity.getQos().getEgressQueue());
        vlanIfSetValueEcEntity.setInflowShapingRate(createVlanIfEntity.getQos().getInflowShapingRate());
        vlanIfSetValueEcEntity.setOutflowShapingRate(createVlanIfEntity.getQos().getOutflowShapingRate());
        vlanIfSetValueEcEntity.setRemarkMenu(createVlanIfEntity.getQos().getRemarkMenu());
        VlanIfQosEcEntity vlanIfQosEcEntity = new VlanIfQosEcEntity();
        vlanIfQosEcEntity.setCapability(null);
        vlanIfQosEcEntity.setSetValue(vlanIfSetValueEcEntity);
        vlanIfEcEntity.setQos(vlanIfQosEcEntity);
      }

      responseBody.getVlanIfList().add(vlanIfEcEntity);
    } finally {
      logger.methodEnd();
    }
  }

  private void deleteVlanIfReadEntity(long nodeInfoId, int deleteTargetVlanIfId) {
    try {
      logger.methodStart(new String[] { "nodeInfoId", "deleteTargetVlanIfId" },
          new Object[] { nodeInfoId, deleteTargetVlanIfId });

      VlanIfReadListEcResponseBody responseBody = vlanIfReadListEcResponseBodyMap.get(nodeInfoId);
      Iterator<VlanIfEcEntity> ite = responseBody.getVlanIfList().iterator();
      while (ite.hasNext()) {
        VlanIfEcEntity entity = ite.next();
        if (entity.getVlanIfId().equals(String.valueOf(deleteTargetVlanIfId))) {
          ite.remove();
          logger.debug("delete vlan if read info.");
          break;
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkQinQTypeConstraints(SessionWrapper sessionWrapper, FcL2Cp l2Cp) throws MsfException {
    try {
      logger.methodStart();
      NodeReadEcResponseBody nodeReadEcResponseBody = null;

      Long targetNodeInfoId = l2Cp.getVlanIf().getId().getNodeInfoId();
      if (nodeReadEcResponseBodyForQinqCheckMap.containsKey(targetNodeInfoId)) {

        nodeReadEcResponseBody = nodeReadEcResponseBodyForQinqCheckMap.get(targetNodeInfoId);
      } else {

        nodeReadEcResponseBody = getNodeFromEc(sessionWrapper, targetNodeInfoId);

        nodeReadEcResponseBodyForQinqCheckMap.put(targetNodeInfoId, nodeReadEcResponseBody);
      }

      QInQType qinqType = QInQType.getEnumFromEcMessage(nodeReadEcResponseBody.getNode().getQInQType());

      if (l2Cp.getL2Slice().getQInQEnable() == true) {

        if (qinqType.equals(QInQType.Q_IN_Q_UNSUPPORT)) {
          String logMsg = "l2slice supports Q-in-Q but node does not support.";
          logger.warn(logMsg);
          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
        }
      } else {

        if (qinqType.equals(QInQType.Q_IN_Q_ONLY)) {
          String logMsg = "l2slice does not support Q-in-Q but node supports Q-in-Q only.";
          logger.warn(logMsg);
          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
        }
      }

    } finally {
      logger.methodEnd();
    }
  }
}
