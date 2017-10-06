package msf.fc.traffic.history;

import java.text.SimpleDateFormat;
import java.util.Date;

import msf.fc.common.data.TrafficHistoryL2slice;
import msf.fc.common.data.TrafficHistoryL3slice;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.AbstractScenario;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.traffic.history.data.entity.TrafficDataEntity;

public abstract class AbstractTrafficScenario<T extends RestRequestBase> extends AbstractScenario<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractTrafficScenario.class);

  private static final SimpleDateFormat writeDateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");

  protected Date checkDateStyle(String strDate) throws MsfException {
    try {
      logger.methodStart();

      Date date = null;
      if ((strDate != null) && (!"".equals(strDate))) {
        date = ParameterCheckUtil.checkDatetime(strDate);
      }
      return date;

    } finally {
      logger.methodEnd();
    }
  }

  protected String format(Date date) {
    synchronized (writeDateFormat) {
      return writeDateFormat.format(date);
    }
  }

  protected TrafficDataEntity createSliceEntity(TrafficHistoryL2slice l2Slice) {
    try {
      logger.methodStart();
      TrafficDataEntity trafficDataEntity = new TrafficDataEntity();
      trafficDataEntity.setStartClusterId(l2Slice.getId().getStartClusterId());
      trafficDataEntity.setStartLeafNodeId(l2Slice.getId().getStartLeafNodeId().toString());
      trafficDataEntity.setStartEdgePointId(l2Slice.getId().getStartEdgePointId().toString());
      trafficDataEntity.setStartCpId(null);
      trafficDataEntity.setEndClusterId(l2Slice.getId().getEndClusterId());
      trafficDataEntity.setEndLeafNodeId(l2Slice.getId().getEndLeafNodeId().toString());
      trafficDataEntity.setEndEdgePointId(l2Slice.getId().getEndEdgePointId().toString());
      trafficDataEntity.setEndCpId(null);
      trafficDataEntity.setTime(format(l2Slice.getId().getOccurredTime()));
      trafficDataEntity.setValue((float) l2Slice.getValue());

      return trafficDataEntity;
    } finally {
      logger.methodEnd();
    }
  }

  protected TrafficDataEntity createSliceEntity(TrafficHistoryL3slice l3Slice) {
    try {
      logger.methodStart();
      TrafficDataEntity trafficDataEntity = new TrafficDataEntity();
      trafficDataEntity.setStartClusterId(l3Slice.getId().getStartClusterId());
      trafficDataEntity.setStartLeafNodeId(l3Slice.getId().getStartLeafNodeId().toString());
      trafficDataEntity.setStartEdgePointId(null);
      trafficDataEntity.setStartCpId(l3Slice.getId().getStartCpId());
      trafficDataEntity.setEndClusterId(l3Slice.getId().getEndClusterId());
      trafficDataEntity.setEndLeafNodeId(l3Slice.getId().getEndLeafNodeId().toString());
      trafficDataEntity.setEndEdgePointId(null);
      trafficDataEntity.setEndCpId(l3Slice.getId().getEndCpId());
      trafficDataEntity.setTime(format(l3Slice.getId().getOccurredTime()));
      trafficDataEntity.setValue((float) l3Slice.getValue());

      return trafficDataEntity;
    } finally {
      logger.methodEnd();
    }
  }


}
