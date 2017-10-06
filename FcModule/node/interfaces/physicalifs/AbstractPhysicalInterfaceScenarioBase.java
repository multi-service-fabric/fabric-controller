package msf.fc.node.interfaces.physicalifs;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.NodeType;
import msf.fc.common.data.PhysicalIf;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.PhysicalIfDao;
import msf.fc.node.interfaces.AbstractInterfaceScenarioBase;

public abstract class AbstractPhysicalInterfaceScenarioBase<T extends RestRequestBase>
    extends AbstractInterfaceScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractPhysicalInterfaceScenarioBase.class);

  protected void checkFabricTypeLeaf(String fabricType) throws MsfException {

    if (!NodeType.LEAF.equals(NodeType.getEnumFromPluralMessage(fabricType))) {
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "fabricType = " + fabricType);
    }

  }

  protected void checkFabricType(String fabricType) throws MsfException {

    ParameterCheckUtil.checkNotNull(NodeType.getEnumFromPluralMessage(fabricType));
  }

  protected void checkNodeId(String nodeId) throws MsfException {
    ParameterCheckUtil.checkNumericId(nodeId, ErrorCode.RELATED_RESOURCE_NOT_FOUND);
  }

  protected void checkPhysicalIfId(String physicalIfId) throws MsfException {
    ParameterCheckUtil.checkIdSpecifiedByUri(physicalIfId);
  }

  protected PhysicalIf getPhysicalInterface(SessionWrapper sessionWrapper, PhysicalIfDao physicalIfDao,
      String swClusterId, Integer nodeType, Integer nodeId, String physicalIfId) throws MsfException {
    try {
      logger.methodStart();
      PhysicalIf physicalIf = physicalIfDao.read(sessionWrapper, swClusterId, nodeType, nodeId, physicalIfId);
      if (physicalIf == null) {
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = physicalIf");
      }
      return physicalIf;
    } finally {
      logger.methodEnd();
    }
  }

}
