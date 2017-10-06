package msf.fc.node.interfaces.lagifs;

import msf.fc.common.data.LagIf;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.LagIfDao;
import msf.fc.node.interfaces.AbstractInterfaceRunnerBase;

public abstract class AbstractLagInterfaceRunnerBase extends AbstractInterfaceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractLagInterfaceRunnerBase.class);

  private static final Integer FIRST_ID_NUM = 1;

  protected Integer getNextLagIfId(SessionWrapper sessionWrapper, LagIfDao lagIfDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "lagIfDao" }, new Object[] { sessionWrapper, lagIfDao });
      LagIf biggestIdLagIf = lagIfDao.readFromBiggestId(sessionWrapper);
      if (null == biggestIdLagIf) {
        return FIRST_ID_NUM;
      } else {
        return biggestIdLagIf.getLagIfId() + 1;
      }
    } finally {
      logger.methodEnd();
    }
  }

}
