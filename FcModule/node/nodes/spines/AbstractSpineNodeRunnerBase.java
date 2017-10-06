package msf.fc.node.nodes.spines;

import msf.fc.common.data.SwCluster;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.IpAddressUtil;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.SwClusterDao;
import msf.fc.node.nodes.AbstractNodeRunnerBase;

public abstract class AbstractSpineNodeRunnerBase extends AbstractNodeRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractSpineNodeRunnerBase.class);
  protected static final Integer RP_NUMBER_1 = 1;
  protected static final Integer RP_NUMBER_2 = 2;

  protected String createLoopbackIpAddress(SessionWrapper session, String swClusterId, int spineNodeId)
      throws MsfException {
    SwClusterDao swClusterDao = new SwClusterDao();
    try {
      logger.methodStart(new String[] { "session", "swClusterId", "nodeId" },
          new Object[] { session, swClusterId, spineNodeId });

      SwCluster swCluster = swClusterDao.read(session, swClusterId);

      String startAddress = swCluster.getLoopbackStartAddress();
      int startIntAddress = IpAddressUtil.convertIpAddressToIntFromStr(startAddress);

      String address = IpAddressUtil.convertIpAddressToStrFromInt(startIntAddress + spineNodeId);

      return address;
    } finally {
      logger.methodEnd();
    }
  }

}
