package msf.fc.traffic.history;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.TrafficHistoryL3slice;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.traffic.TrafficHistoryL3sliceDao;
import msf.fc.traffic.data.TrafficCommonData;
import msf.fc.traffic.history.data.L3TrafficRequest;
import msf.fc.traffic.history.data.TrafficInfoReadResponseBody;
import msf.fc.traffic.history.data.entity.SliceTrafficEntity;
import msf.fc.traffic.history.data.entity.TrafficDataEntity;
import msf.fc.traffic.tm.TrafficTmEstimation;

public class L3TrafficLatestReadListScenario extends AbstractTrafficScenario<L3TrafficRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(L3TrafficLatestReadListScenario.class);

  private L3TrafficRequest request;

  public L3TrafficLatestReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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

      TrafficHistoryL3sliceDao l3Dao = new TrafficHistoryL3sliceDao();
      List<TrafficHistoryL3slice> l3HistoryList = null;
      if (request.getSliceId() != null) {
        l3HistoryList = l3Dao.readList(sessionWrapper, targetStart, targetEnd, request.getSliceId());
      } else {
        l3HistoryList = l3Dao.readList(sessionWrapper, targetStart, targetEnd);
      }

      TrafficInfoReadResponseBody responseBody = createResponseBody(l3HistoryList, request.getSliceId(), targetStart);
      return new RestResponseBase(HttpStatus.OK_200, responseBody);

    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  @Override
  protected void checkParameter(L3TrafficRequest request) throws MsfException {
    try {
      logger.methodStart();
      if (request.getSliceId() != null) {
        ParameterCheckUtil.checkIdSpecifiedByUri(request.getSliceId());
      }
      this.request = request;

    } finally {
      logger.methodEnd();
    }

  }

  private TrafficInfoReadResponseBody createResponseBody(List<TrafficHistoryL3slice> l3HistoryList, String sliceId,
      Date date) {
    try {
      logger.methodStart();

      Map<String, List<TrafficHistoryL3slice>> l3SliceMap = new HashMap<>();
      for (TrafficHistoryL3slice l3slice : l3HistoryList) {
          continue;
        }
        if (l3SliceMap.get(l3slice.getId().getSliceId()) == null) {
          l3SliceMap.put(l3slice.getId().getSliceId(), new LinkedList<>());
        }
        List<TrafficHistoryL3slice> l3SliceList = l3SliceMap.get(l3slice.getId().getSliceId());
        l3SliceList.add(l3slice);
        date = l3slice.getId().getOccurredTime();
      }

      List<SliceTrafficEntity> sliceList = new ArrayList<>();
      Map<String, List<TrafficHistoryL3slice>> sortedMap = new TreeMap<>(l3SliceMap);
      for (String sliceIdl : sortedMap.keySet()) {

        double totalTraffic = 0;
        SliceTrafficEntity sliceTrafficEntity = new SliceTrafficEntity();
        for (TrafficHistoryL3slice l3Slice : l3SliceMap.get(sliceIdl)) {
          TrafficDataEntity trafficDataEntity = createSliceEntity(l3Slice);
          sliceTrafficEntity.getTrafficDataList().add(trafficDataEntity);
            totalTraffic += l3Slice.getValue();
          }
        }

        sliceTrafficEntity.setSliceType("l3vpn");
        sliceTrafficEntity.setSliceId(sliceIdl);
        sliceTrafficEntity.setStartTime(format(date));
        sliceTrafficEntity.setEndTime(format(date));
        sliceTrafficEntity.setTotalTraffic((int) totalTraffic);
        sliceTrafficEntity.setTotalDataNum(sliceTrafficEntity.getTrafficDataList().size());

        sliceList.add(sliceTrafficEntity);
      }

      TrafficInfoReadResponseBody responseBody = new TrafficInfoReadResponseBody();
      responseBody.setSliceTrafficList(sliceList);

      return responseBody;

    } finally {
      logger.methodEnd();
    }
  }

}
