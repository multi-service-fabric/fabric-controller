
package msf.fc.node.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcInternalLinkIf;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.node.interfaces.physicalifs.FcAbstractPhysicalInterfaceScenarioBase;
import msf.fc.rest.ec.node.interfaces.breakout.data.entity.BreakoutIfEcEntity;
import msf.fc.rest.ec.node.interfaces.lag.data.entity.LagIfBreakoutIfEcEntity;
import msf.fc.rest.ec.node.interfaces.lag.data.entity.LagIfEcEntity;
import msf.fc.rest.ec.node.interfaces.lag.data.entity.LagIfPhysicalIfEcEntity;
import msf.fc.rest.ec.node.interfaces.physical.data.entity.PhysicalIfEcEntity;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.AbstractInterfaceScenarioBase;
import msf.mfcfc.node.interfaces.breakoutifs.data.entity.BreakoutIfBaseIfEntity;
import msf.mfcfc.node.interfaces.breakoutifs.data.entity.BreakoutIfEntity;
import msf.mfcfc.node.interfaces.internalifs.data.entity.InternalLinkIfEntity;
import msf.mfcfc.node.interfaces.lagifs.data.entity.LagIfEntity;
import msf.mfcfc.node.interfaces.lagifs.data.entity.LagIfInternalOptionEntity;
import msf.mfcfc.node.interfaces.lagifs.data.entity.LagIfOppositeEntity;
import msf.mfcfc.node.interfaces.physicalifs.data.entity.PhysicalIfBreakoutEntity;
import msf.mfcfc.node.interfaces.physicalifs.data.entity.PhysicalIfEntity;
import msf.mfcfc.node.interfaces.physicalifs.data.entity.PhysicalIfIfEntity;
import msf.mfcfc.node.interfaces.physicalifs.data.entity.PhysicalIfOppositeIfEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;

/**
 * Abstract class to implement common process of interface-related processing in
 * configuration management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherited the RestRequestBase class
 */
public abstract class FcAbstractInterfaceScenarioBase<T extends RestRequestBase>
    extends AbstractInterfaceScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractPhysicalInterfaceScenarioBase.class);

  protected List<InternalLinkIfEntity> getInternalIfEntities(List<FcInternalLinkIf> internalLinkIfs)
      throws MsfException {
    try {
      logger.methodStart();
      List<InternalLinkIfEntity> internalIfEntities = new ArrayList<>();
      for (FcInternalLinkIf internalLinkIf : internalLinkIfs) {
        internalIfEntities.add(getInternalIf(internalLinkIf));
      }
      return internalIfEntities;
    } finally {
      logger.methodEnd();
    }
  }

  protected InternalLinkIfEntity getInternalIf(FcInternalLinkIf fcInternalLinkIf) throws MsfException {
    try {
      logger.methodStart(new String[] { "internalLinkIf" }, new Object[] { fcInternalLinkIf });
      InternalLinkIfEntity internalIf = new InternalLinkIfEntity();
      internalIf.setInternalLinkIfId(String.valueOf(fcInternalLinkIf.getInternalLinkIfId()));

      if (fcInternalLinkIf.getLagIf() != null) {
        internalIf.setLagIfId(String.valueOf(fcInternalLinkIf.getLagIf().getLagIfId()));
      } else if (fcInternalLinkIf.getPhysicalIf() != null) {
        internalIf.setPhysicalIfId(fcInternalLinkIf.getPhysicalIf().getPhysicalIfId());
      } else {
        internalIf.setBreakoutIfId(fcInternalLinkIf.getBreakoutIf().getBreakoutIfId());
      }
      return internalIf;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<String> getInternalIfIdList(List<FcInternalLinkIf> fcInternalLinkIfs) {
    try {
      logger.methodStart();
      List<String> internalIfIdList = new ArrayList<>();
      for (FcInternalLinkIf fcInternalLinkIf : fcInternalLinkIfs) {
        internalIfIdList.add(String.valueOf(fcInternalLinkIf.getInternalLinkIfId()));
      }
      return internalIfIdList;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<LagIfEntity> getLagIfEntities(List<FcLagIf> fcLagIfs, List<LagIfEcEntity> lagIfEcList)
      throws MsfException {
    try {
      logger.methodStart();
      List<LagIfEntity> lagIfEntities = new ArrayList<>();
      boolean isExist;
      for (FcLagIf fcLagIf : fcLagIfs) {
        isExist = false;
        if (lagIfEcList != null) {
          for (LagIfEcEntity lagIfEcEntity : lagIfEcList) {
            if (lagIfEcEntity.getLagIfId().equals(String.valueOf(fcLagIf.getLagIfId()))) {
              lagIfEntities.add(getLagIfData(fcLagIf, lagIfEcEntity));
              isExist = true;
              break;
            }
          }
        }
        if (!isExist) {

          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, "There is only the data in the FC system.");
        }
      }
      if (lagIfEcList.size() != lagIfEntities.size()) {

        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, "There is only the data in the EC system.");
      }
      return lagIfEntities;
    } finally {
      logger.methodEnd();
    }
  }

  protected LagIfEntity getLagIfData(FcLagIf fcLagIf, LagIfEcEntity lagIfEcEntity) throws MsfException {
    try {
      logger.methodStart(new String[] { "lagIf", "readLagIfEcEntity" }, new Object[] { fcLagIf, lagIfEcEntity });
      LagIfEntity lagIfEntity = new LagIfEntity();
      lagIfEntity.setLagIfId(String.valueOf(fcLagIf.getLagIfId()));
      if (CollectionUtils.isNotEmpty(fcLagIf.getInternalLinkIfs())) {
        LagIfInternalOptionEntity lagIfInternalOptionEntity = new LagIfInternalOptionEntity();

        lagIfInternalOptionEntity.setIpv4Address(lagIfEcEntity.getIpv4Address());
        LagIfOppositeEntity lagIfOpposite = new LagIfOppositeEntity();
        FcInternalLinkIf oppositeInternalLinkIf = fcLagIf.getInternalLinkIfs().get(0).getOppositeInternalLinkIfs()
            .get(0);
        lagIfOpposite.setFabricType(oppositeInternalLinkIf.getLagIf().getNode().getNodeTypeEnum().getSingularMessage());
        lagIfOpposite.setNodeId(String.valueOf(oppositeInternalLinkIf.getLagIf().getNode().getNodeId()));
        lagIfOpposite.setLaglIfId(String.valueOf(oppositeInternalLinkIf.getLagIf().getLagIfId()));
        lagIfInternalOptionEntity.setOppositeIf(lagIfOpposite);
        lagIfEntity.setInternalOptions(lagIfInternalOptionEntity);
      }
      lagIfEntity.setSpeed(lagIfEcEntity.getLinkSpeed());
      lagIfEntity.setIfName(lagIfEcEntity.getIfName());
      int minimumLinks = 0;
      List<String> physicalIfIdList = new ArrayList<>();
      List<String> breakoutIfIdList = new ArrayList<>();

      if (CollectionUtils.isNotEmpty(lagIfEcEntity.getLagMember().getPhysicalIfList())) {
        for (LagIfPhysicalIfEcEntity lagIfPhysicalIfEcEntity : lagIfEcEntity.getLagMember().getPhysicalIfList()) {
          if (lagIfPhysicalIfEcEntity != null) {
            physicalIfIdList.add(lagIfPhysicalIfEcEntity.getPhysicalIfId());

            minimumLinks++;
          }
        }
      }
      lagIfEntity.setPhysicalIfIdList(physicalIfIdList);

      if (CollectionUtils.isNotEmpty(lagIfEcEntity.getLagMember().getBreakoutIfList())) {
        for (LagIfBreakoutIfEcEntity lagIfBreakoutIfEcEntity : lagIfEcEntity.getLagMember().getBreakoutIfList()) {
          if (lagIfBreakoutIfEcEntity != null) {
            breakoutIfIdList.add(lagIfBreakoutIfEcEntity.getBreakoutIfId());

            minimumLinks++;
          }
        }
      }
      lagIfEntity.setBreakoutIfIdList(breakoutIfIdList);

      lagIfEntity.setMinimumLinks(minimumLinks);
      return lagIfEntity;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<String> getLagIfIdList(List<FcLagIf> fcLagIfs) {
    try {
      logger.methodStart();
      List<String> lagIfIdList = new ArrayList<>();
      for (FcLagIf fcLagIf : fcLagIfs) {
        lagIfIdList.add(String.valueOf(fcLagIf.getLagIfId()));
      }
      return lagIfIdList;
    } finally {
      logger.methodEnd();
    }
  }

  protected ArrayList<PhysicalIfEntity> getPhysicalIfEntities(List<FcPhysicalIf> physicalIfs,
      List<PhysicalIfEcEntity> physicalIfEcList, List<BreakoutIfEcEntity> breakoutIfEcList) throws MsfException {
    try {
      logger.methodStart();
      ArrayList<PhysicalIfEntity> physicalIfEntities = new ArrayList<>();
      boolean isExist;
      for (FcPhysicalIf physicalIf : physicalIfs) {
        isExist = false;
        if (physicalIfEcList != null) {
          for (PhysicalIfEcEntity physicalIfData : physicalIfEcList) {
            if (physicalIf.getPhysicalIfId().equals(physicalIfData.getPhysicalIfId())) {
              physicalIfEntities.add(getPhysicalIfData(physicalIf, physicalIfData, breakoutIfEcList));
              isExist = true;
              break;
            }
          }
        }
        if (!isExist) {

          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, "There is only the data in the FC system.");
        }
      }
      if (physicalIfEcList.size() != physicalIfEntities.size()) {

        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, "There is only the data in the EC system.");
      }
      return physicalIfEntities;
    } finally {
      logger.methodEnd();
    }
  }

  protected PhysicalIfEntity getPhysicalIfData(FcPhysicalIf physicalIf, PhysicalIfEcEntity physicalIfEcResponseData,
      List<BreakoutIfEcEntity> breakoutIfEcList) throws MsfException {
    try {
      logger.methodStart(new String[] { "physicalIf", "physicalIfEcResponseData" },
          new Object[] { physicalIf, physicalIfEcResponseData });
      PhysicalIfEntity physicalIfData = new PhysicalIfEntity();
      physicalIfData.setPhysicalIfId(physicalIf.getPhysicalIfId());
      List<FcInternalLinkIf> fcInternalLinkIfList = physicalIf.getInternalLinkIfs();
      if (CollectionUtils.isNotEmpty(fcInternalLinkIfList)) {
        PhysicalIfOppositeIfEntity oppositeIf = new PhysicalIfOppositeIfEntity();
        FcInternalLinkIf fcInternalLinkIf = fcInternalLinkIfList.get(0).getOppositeInternalLinkIfs().get(0);
        FcPhysicalIf fcPhysicalIf = fcInternalLinkIf.getPhysicalIf();
        FcBreakoutIf fcBreakoutIf = fcInternalLinkIf.getBreakoutIf();
        if (fcPhysicalIf != null) {
          oppositeIf.setFabricType(fcPhysicalIf.getNode().getNodeTypeEnum().getSingularMessage());
          oppositeIf.setNodeId(String.valueOf(fcPhysicalIf.getNode().getNodeId()));
          oppositeIf.setIfType(InterfaceType.PHYSICAL_IF.getMessage());
          oppositeIf.setIfId(fcPhysicalIf.getPhysicalIfId());
        } else if (fcBreakoutIf != null) {
          oppositeIf.setFabricType(fcBreakoutIf.getNode().getNodeTypeEnum().getSingularMessage());
          oppositeIf.setNodeId(String.valueOf(fcBreakoutIf.getNode().getNodeId()));
          oppositeIf.setIfType(InterfaceType.BREAKOUT_IF.getMessage());
          oppositeIf.setIfId(fcBreakoutIf.getBreakoutIfId());
        }
        physicalIfData.setOppositeIf(oppositeIf);
      }
      physicalIfData.setSpeed(physicalIfEcResponseData.getLinkSpeed());
      physicalIfData.setIfName(physicalIfEcResponseData.getIfName());
      if (CollectionUtils.isNotEmpty(breakoutIfEcList)) {
        for (BreakoutIfEcEntity breakoutIfEcEntity : breakoutIfEcList) {
          if (breakoutIfEcEntity.getBasePhysicalIfId().equals(physicalIf.getPhysicalIfId())) {
            if (physicalIfData.getBreakout() == null) {
              PhysicalIfBreakoutEntity breakout = new PhysicalIfBreakoutEntity();
              breakout.setSpeed(breakoutIfEcEntity.getLinkSpeed());
              ArrayList<PhysicalIfIfEntity> psysicalIfIfEntities = new ArrayList<>();
              PhysicalIfIfEntity psysicalIfIfEntity = new PhysicalIfIfEntity();
              psysicalIfIfEntity.setIfName(breakoutIfEcEntity.getIfName());
              psysicalIfIfEntity.setPhysicalIfId(breakoutIfEcEntity.getBreakoutIfId());
              psysicalIfIfEntities.add(psysicalIfIfEntity);
              breakout.setIfList(psysicalIfIfEntities);
              physicalIfData.setBreakout(breakout);
            } else {
              List<PhysicalIfIfEntity> psysicalIfIfEntities = physicalIfData.getBreakout().getIfList();
              PhysicalIfIfEntity psysicalIfIfEntity = new PhysicalIfIfEntity();
              psysicalIfIfEntity.setIfName(breakoutIfEcEntity.getIfName());
              psysicalIfIfEntity.setPhysicalIfId(breakoutIfEcEntity.getBreakoutIfId());
              psysicalIfIfEntities.add(psysicalIfIfEntity);
              physicalIfData.getBreakout().setIfList(psysicalIfIfEntities);
            }
          }
        }
      }
      return physicalIfData;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<String> getPhysicalIfIdList(List<FcPhysicalIf> physicalIfs) {
    try {
      logger.methodStart();
      List<String> physicalIfIdList = new ArrayList<>();
      for (FcPhysicalIf physicalIf : physicalIfs) {
        physicalIfIdList.add(physicalIf.getPhysicalIfId());
      }
      return physicalIfIdList;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<BreakoutIfEntity> getBreakoutIfEntities(List<FcBreakoutIf> fcBreakoutIfs,
      List<BreakoutIfEcEntity> breakoutIfEcList) throws MsfException {
    try {
      logger.methodStart();
      List<BreakoutIfEntity> breakoutIfEntities = new ArrayList<>();
      boolean isExist;
      for (FcBreakoutIf fcBreakoutIf : fcBreakoutIfs) {
        isExist = false;
        if (breakoutIfEcList != null) {
          for (BreakoutIfEcEntity breakoutIfEcEntity : breakoutIfEcList) {
            if (breakoutIfEcEntity.getBreakoutIfId().equals(String.valueOf(fcBreakoutIf.getBreakoutIfId()))) {
              breakoutIfEntities.add(getBreakoutIfData(fcBreakoutIf, breakoutIfEcEntity));
              isExist = true;
              break;
            }
          }
        }
        if (!isExist) {

          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, "There is only the data in the FC system.");
        }
      }
      if (breakoutIfEcList.size() != breakoutIfEntities.size()) {

        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, "There is only the data in the EC system.");
      }
      return breakoutIfEntities;
    } finally {
      logger.methodEnd();
    }
  }

  protected BreakoutIfEntity getBreakoutIfData(FcBreakoutIf fcBreakoutIf, BreakoutIfEcEntity breakoutIfEcEntity)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "fcBreakoutIf", "breakoutIfEcEntity" },
          new Object[] { fcBreakoutIf, breakoutIfEcEntity });
      BreakoutIfEntity breakoutIfEntity = new BreakoutIfEntity();
      breakoutIfEntity.setBreakoutIfId(String.valueOf(fcBreakoutIf.getBreakoutIfId()));
      breakoutIfEntity.setSpeed(breakoutIfEcEntity.getLinkSpeed());
      breakoutIfEntity.setIfName(breakoutIfEcEntity.getIfName());
      BreakoutIfBaseIfEntity baseIf = new BreakoutIfBaseIfEntity();
      baseIf.setPhysicalIfId(breakoutIfEcEntity.getBasePhysicalIfId());
      breakoutIfEntity.setBaseIf(baseIf);
      return breakoutIfEntity;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<String> getBreakoutIfIdList(List<FcBreakoutIf> breakoutIfs) {
    try {
      logger.methodStart();
      List<String> breakoutIfIdList = new ArrayList<>();
      for (FcBreakoutIf fcBreakoutIf : breakoutIfs) {
        breakoutIfIdList.add(String.valueOf(fcBreakoutIf.getBreakoutIfId()));
      }
      return breakoutIfIdList;
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

  protected void checkNode(SessionWrapper sessionWrapper, Integer nodeType, Integer nodeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "swClusterId", "nodeType", "nodeId" },
          new Object[] { sessionWrapper, nodeType, nodeId });
      FcNodeDao nodeDao = new FcNodeDao();
      FcNode node = nodeDao.read(sessionWrapper, nodeType, nodeId);
      if (node == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, "target resource not found. parameters = node");
      }
    } finally {
      logger.methodEnd();
    }
  }

}
