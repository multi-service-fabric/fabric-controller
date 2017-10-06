package msf.fc.node.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.InterfaceOperationStatus;
import msf.fc.common.data.InternalLinkIf;
import msf.fc.common.data.LagConstruction;
import msf.fc.common.data.LagIf;
import msf.fc.common.data.Node;
import msf.fc.common.data.PhysicalIf;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.AbstractScenario;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.node.interfaces.internalifs.data.entity.InternalIfEntity;
import msf.fc.node.interfaces.lagifs.data.entity.InternalOptionEntity;
import msf.fc.node.interfaces.lagifs.data.entity.LagIfEntity;
import msf.fc.node.interfaces.lagifs.data.entity.OppositeLagIfEntity;
import msf.fc.node.interfaces.physicalifs.data.entity.OppositePhysicalIfEntity;
import msf.fc.node.interfaces.physicalifs.data.entity.PhysicalIfEntity;
import msf.fc.rest.common.AbstractResponseBody;
import msf.fc.rest.ec.node.interfaces.lag.data.entity.ReadLagIfEcEntity;
import msf.fc.rest.ec.node.interfaces.physical.data.entity.PhysicalIfEcEntity;

public abstract class AbstractInterfaceScenarioBase<T extends RestRequestBase> extends AbstractScenario<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractInterfaceScenarioBase.class);

  protected List<InternalIfEntity> getInternalIfEntities(List<InternalLinkIf> internalLinkIfs) throws MsfException {
    try {
      logger.methodStart();
      List<InternalIfEntity> internalIfEntities = new ArrayList<>();
      for (InternalLinkIf internalLinkIf : internalLinkIfs) {
        internalIfEntities.add(getInternalIf(internalLinkIf));
      }
      return internalIfEntities;
    } finally {
      logger.methodEnd();
    }
  }

  protected InternalIfEntity getInternalIf(InternalLinkIf internalLinkIf) throws MsfException {
    try {
      logger.methodStart(new String[] { "internalLinkIf" }, new Object[] { internalLinkIf });
      InternalIfEntity internalIf = new InternalIfEntity();
      internalIf.setInternalIfId(String.valueOf(internalLinkIf.getInternalLinkIfId()));
      internalIf.setLaglIfId(String.valueOf(internalLinkIf.getLagIf().getLagIfId()));
      internalIf.setOperationStatusEnum(InterfaceOperationStatus.getEnumFromCode(internalLinkIf.getOperationStatus()));
      return internalIf;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<String> getInternalIfIdList(List<InternalLinkIf> internalLinkIfs) {
    try {
      logger.methodStart();
      List<String> internalIfIdList = new ArrayList<>();
      for (InternalLinkIf internalLinkIf : internalLinkIfs) {
        internalIfIdList.add(String.valueOf(internalLinkIf.getInternalLinkIfId()));
      }
      return internalIfIdList;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<LagIfEntity> getLagIfEntities(List<LagIf> lagIfs, List<ReadLagIfEcEntity> lagIfEcList)
      throws MsfException {
    try {
      logger.methodStart();
      List<LagIfEntity> lagIfEntities = new ArrayList<>();
      boolean isExist;
      for (LagIf lagIf : lagIfs) {
        isExist = false;
        for (ReadLagIfEcEntity readLagIfEcEntity : lagIfEcList) {
          if (readLagIfEcEntity.getLagIfId().equals(String.valueOf(lagIf.getLagIfId()))) {
            lagIfEntities.add(getLagIfData(lagIf, readLagIfEcEntity));
            isExist = true;
            break;
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

  protected LagIfEntity getLagIfData(LagIf lagIf, ReadLagIfEcEntity readLagIfEcEntity) throws MsfException {
    try {
      logger.methodStart(new String[] { "lagIf", "readLagIfEcEntity" }, new Object[] { lagIf, readLagIfEcEntity });
      LagIfEntity lagIfEntity = new LagIfEntity();
      lagIfEntity.setLagIfId(String.valueOf(lagIf.getLagIfId()));
      if (null != lagIf.getInternalLinkIf()) {
        InternalOptionEntity internalOption = new InternalOptionEntity();
        internalOption.setIpv4Address(lagIf.getIpv4Address());
        OppositeLagIfEntity oppositeIf = new OppositeLagIfEntity();
        oppositeIf.setFabricType(lagIf.getOppositeNode().getNodeTypeEnum().getSingularMessage());
        oppositeIf.setNodeId(String.valueOf(lagIf.getOppositeNode().getNodeId()));
        oppositeIf.setLaglIfId(String.valueOf(lagIf.getOppositeLagIfId()));
        internalOption.setOppositeIf(oppositeIf);
        lagIfEntity.setInternalOption(internalOption);
      }
      lagIfEntity.setMinimumLinks(lagIf.getMinimumLinks());
      lagIfEntity.setSpeed(lagIf.getSpeed());
      lagIfEntity.setIfName(readLagIfEcEntity.getIfName());
      List<String> physicalIfIdList = new ArrayList<>();
      for (LagConstruction lagConstruction : lagIf.getLagConstructions()) {
        physicalIfIdList.add(lagConstruction.getPhysicalIf().getPhysicalIfId());
      }
      lagIfEntity.setPhysicalIfIdList(physicalIfIdList);
      return lagIfEntity;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<String> getLagIfIdList(List<LagIf> lagIfs) {
    try {
      logger.methodStart();
      List<String> lagIfIdList = new ArrayList<>();
      for (LagIf lagIf : lagIfs) {
        lagIfIdList.add(String.valueOf(lagIf.getLagIfId()));
      }
      return lagIfIdList;
    } finally {
      logger.methodEnd();
    }
  }

  protected ArrayList<PhysicalIfEntity> getPhysicalIfEntities(List<PhysicalIf> physicalIfs,
      List<PhysicalIfEcEntity> physicalIfEcList) throws MsfException {
    try {
      logger.methodStart();
      ArrayList<PhysicalIfEntity> physicalIfEntities = new ArrayList<>();
      boolean isExist;
      for (PhysicalIf physicalIf : physicalIfs) {
        isExist = false;
        for (PhysicalIfEcEntity physicalIfData : physicalIfEcList) {
          if (physicalIf.getPhysicalIfId().equals(physicalIfData.getPhysicalIfId())) {
            physicalIfEntities.add(getPhysicalIfData(physicalIf, physicalIfData));
            isExist = true;
            break;
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

  protected PhysicalIfEntity getPhysicalIfData(PhysicalIf physicalIf, PhysicalIfEcEntity physicalIfEcResponseData)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "physicalIf", "physicalIfEcResponseData" },
          new Object[] { physicalIf, physicalIfEcResponseData });
      PhysicalIfEntity physicalIfData = new PhysicalIfEntity();
      physicalIfData.setPhysicalIfId(physicalIf.getPhysicalIfId());
      Node oppositeNode = physicalIf.getOppositeNode();
      if (null != oppositeNode) {
        OppositePhysicalIfEntity oppositeIf = new OppositePhysicalIfEntity();
        oppositeIf.setFabricType(oppositeNode.getNodeTypeEnum().getSingularMessage());
        oppositeIf.setNodeId(String.valueOf(oppositeNode.getNodeId()));
        oppositeIf.setPhysicalIfId(physicalIf.getOppositePhysicalIfId());
        physicalIfData.setOppositeIf(oppositeIf);
      }
      if (null != physicalIf.getSpeed()) {
        physicalIfData.setSpeed(physicalIf.getSpeed());
        physicalIfData.setIfName(physicalIfEcResponseData.getIfName());
      }
      return physicalIfData;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<String> getPhysicalIfIdList(List<PhysicalIf> physicalIfs) {
    try {
      logger.methodStart();
      List<String> physicalIfIdList = new ArrayList<>();
      for (PhysicalIf physicalIf : physicalIfs) {
        physicalIfIdList.add(physicalIf.getPhysicalIfId());
      }
      return physicalIfIdList;
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

  protected void checkNode(SessionWrapper sessionWrapper, String swClusterId, Integer nodeType, Integer nodeId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "swClusterId", "nodeType", "nodeId" },
          new Object[] { sessionWrapper, swClusterId, nodeType, nodeId });
      NodeDao nodeDao = new NodeDao();
      Node node = nodeDao.read(sessionWrapper, swClusterId, nodeType, nodeId);
      if (node == null) {
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, "target resource not found. parameters = node");
      }
    } finally {
      logger.methodEnd();
    }
  }

}
