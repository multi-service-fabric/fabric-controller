package msf.fc.slice;

import java.text.MessageFormat;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.RestFormatOption;
import msf.fc.common.data.L2Slice;
import msf.fc.common.data.L3Slice;
import msf.fc.common.data.Node;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.AbstractScenario;
import msf.fc.core.scenario.RestRequestBase;

public abstract class AbstractSliceCpScenarioBase<T extends RestRequestBase> extends AbstractScenario<T> {
  private static final MsfLogger logger = MsfLogger.getInstance(AbstractSliceCpScenarioBase.class);

  protected void checkFormatOption(String format) throws MsfException {
    try {
      logger.methodStart(new String[] { "format" }, new Object[] { format });
      if (format == null) {
        return;
      }
      RestFormatOption formatEnum = RestFormatOption.getEnumFromMessage(format);
      if (formatEnum == null) {
        String logMsg = MessageFormat.format("param is undefined.param = {0}, value = {1}", "format", format);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkNodePresence(Node node, String swClusterId, int edgePointId) throws MsfException {
    try {
      logger.methodStart(new String[] { "node", "swClusterId", "edgePointId" },
          new Object[] { node, swClusterId, edgePointId });
      ParameterCheckUtil.checkNotNullRelatedResource(node, new String[] { "swClusterId", "edgePointId" },
          new Object[] { swClusterId, String.valueOf(edgePointId) });
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkL2SlicePresence(L2Slice l2Slice, String sliceId, boolean isTarget) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice", "sliceId", "isTarget" },
          new Object[] { l2Slice, sliceId, isTarget });
      if (isTarget) {
        ParameterCheckUtil.checkNotNullTargetResource(l2Slice, new String[] { "sliceId" }, new Object[] { sliceId });
      } else {
        ParameterCheckUtil.checkNotNullRelatedResource(l2Slice, new String[] { "sliceId" }, new Object[] { sliceId });
      }

    } finally {
      logger.methodEnd();
    }
  }

  protected void checkL3SlicePresence(L3Slice l3Slice, String sliceId, boolean isTarget) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Slice", "sliceId", "isTarget" },
          new Object[] { l3Slice, sliceId, isTarget });
      if (isTarget) {
        ParameterCheckUtil.checkNotNullTargetResource(l3Slice, new String[] { "sliceId" }, new Object[] { sliceId });
      } else {
        ParameterCheckUtil.checkNotNullRelatedResource(l3Slice, new String[] { "sliceId" }, new Object[] { sliceId });
      }
    } finally {
      logger.methodEnd();
    }
  }
}
