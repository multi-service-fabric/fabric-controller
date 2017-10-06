package msf.fc.node.interfaces.internalifs;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.NodeType;
import msf.fc.common.exception.MsfException;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.node.interfaces.AbstractInterfaceScenarioBase;

public abstract class AbstractInternalInterfaceScenarioBase<T extends RestRequestBase>
    extends AbstractInterfaceScenarioBase<T> {

  protected void checkFabricType(String fabricType) throws MsfException {
    ParameterCheckUtil.checkNotNull(NodeType.getEnumFromPluralMessage(fabricType));
  }

  protected void checkNodeId(String nodeId) throws MsfException {
    ParameterCheckUtil.checkNumericId(nodeId, ErrorCode.RELATED_RESOURCE_NOT_FOUND);
  }

}
