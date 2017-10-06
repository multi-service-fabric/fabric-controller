package msf.fc.traffic.history;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.TrafficHistoryL2slice;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.traffic.TrafficHistoryL2sliceDao;
import msf.fc.traffic.data.TrafficCommonData;
import msf.fc.traffic.history.data.L2TrafficRequest;
import msf.fc.traffic.history.data.TrafficInfoReadResponseBody;
import msf.fc.traffic.history.data.entity.SliceTrafficEntity;
import msf.fc.traffic.history.data.entity.TrafficDataEntity;
import msf.fc.traffic.tm.TrafficTmEstimation;

public class L2TrafficLatestReadListScenario extends AbstractTrafficScenario<L2TrafficRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(L2TrafficHistoryReadListScenario.class);

  public L2TrafficLatestReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    logger.methodStart();
    SessionWrapper sessionWrapper = new SessionWrapper();

    try {
      sessionWrapper.openSession();
      Date targetStart = TrafficTmEstimation.getStartTime();
      Date targetEnd = TrafficTmEstimation.getEndTime();
      if (targetStart.after(targetEnd)) {
        targetStart = TrafficTmEstimation.getLastStartTime();
      }

        targetStart = new Date();
        targetEnd = targetStart;
      }

      TrafficHistoryL2sliceDao l2Dao = new TrafficHistoryL2sliceDao();
      List<TrafficHistoryL2slice> l2HistoryList = l2Dao.readList(sessionWrapper, targetStart, targetEnd);

      TrafficInfoReadResponseBody responseBody = createResponseBody(l2HistoryList, targetStart);
      return new RestResponseBase(HttpStatus.OK_200, responseBody);

    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  @Override
  protected void checkParameter(L2TrafficRequest request) throws MsfException {
    try {
      logger.methodStart();
      if (request.getSliceId() != null) {
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "Parameter value error. Because the slice ID is set");
      }

    } finally {
      logger.methodEnd();
    }
  }

  private TrafficInfoReadResponseBody createResponseBody(List<TrafficHistoryL2slice> l2HistoryList, Date date) {
    try {
      logger.methodStart();

      SliceTrafficEntity sliceTrafficEntity = new SliceTrafficEntity();

      List<SliceTrafficEntity> sliceList = new ArrayList<>();
      sliceList.add(sliceTrafficEntity);

      double totalTraffic = 0;

      if (l2HistoryList.size() > 0) {
        for (TrafficHistoryL2slice l2Slice : l2HistoryList) {
            continue;
          }
          TrafficDataEntity trafficDataEntity = createSliceEntity(l2Slice);
          sliceTrafficEntity.getTrafficDataList().add(trafficDataEntity);
            totalTraffic += l2Slice.getValue();
          }
          date = l2Slice.getId().getOccurredTime();
        }
      }

      sliceTrafficEntity.setSliceType("l2vpn");
      sliceTrafficEntity.setSliceId(null);
      sliceTrafficEntity.setStartTime(format(date));
      sliceTrafficEntity.setEndTime(format(date));
      sliceTrafficEntity.setTotalTraffic((int) totalTraffic);
      sliceTrafficEntity.setTotalDataNum(sliceTrafficEntity.getTrafficDataList().size());

      TrafficInfoReadResponseBody responseBody = new TrafficInfoReadResponseBody();
      responseBody.setSliceTrafficList(sliceList);

      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

}
