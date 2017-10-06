package msf.fc.node.interfaces.lagifs;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.NodeType;
import msf.fc.common.exception.MsfException;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.node.interfaces.AbstractInterfaceScenarioBase;

public abstract class AbstractLagInterfaceScenarioBase<T extends RestRequestBase>
    extends AbstractInterfaceScenarioBase<T> {

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

  protected void checkLagIfId(String lagIfId) throws MsfException {
    ParameterCheckUtil.checkNumericId(lagIfId, ErrorCode.TARGET_RESOURCE_NOT_FOUND);
  }

}
