package msf.fc.node.nodes.rrs;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.exception.MsfException;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.node.nodes.AbstractNodeScenarioBase;

public abstract class AbstractRrNodeScenarioBase<T extends RestRequestBase> extends AbstractNodeScenarioBase<T> {

  protected void checkNodeId(String nodeId) throws MsfException {

    ParameterCheckUtil.checkNumericId(nodeId, ErrorCode.TARGET_RESOURCE_NOT_FOUND);

  }
}
