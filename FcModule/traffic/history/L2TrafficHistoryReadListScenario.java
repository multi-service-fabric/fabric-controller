package msf.fc.traffic.history;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DateUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.TrafficHistoryL2slice;
import msf.fc.common.data.TrafficHistoryL2slicePK;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.traffic.TrafficHistoryL2sliceDao;
import msf.fc.traffic.data.TrafficCommonData;
import msf.fc.traffic.history.data.L2TrafficRequest;
import msf.fc.traffic.history.data.TrafficInfoReadResponseBody;
import msf.fc.traffic.history.data.entity.SliceTrafficEntity;
import msf.fc.traffic.history.data.entity.TrafficDataEntity;
import msf.fc.traffic.tm.TrafficTmEstimation;

public class L2TrafficHistoryReadListScenario extends AbstractTrafficScenario<L2TrafficRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(L2TrafficHistoryReadListScenario.class);

  private L2TrafficRequest request;

  private Date startTime;
  private Date endTime;

  public L2TrafficHistoryReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
      TrafficHistoryL2sliceDao l2Dao = new TrafficHistoryL2sliceDao();
      List<TrafficHistoryL2slice> l2HistoryList = l2Dao.readList(sessionWrapper, startTime, endTime);

      TrafficInfoReadResponseBody responseBody = createResponseBody(l2HistoryList, request.getStartTime(),
          request.getEndTime(), request.getInterval());
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
      startTime = checkDateStyle(request.getStartTime());
        startTime = new Date(0);
      }

      endTime = checkDateStyle(request.getEndTime());
      TrafficTmEstimation trafficEstimation = new TrafficTmEstimation();
      Date tmEndTime = trafficEstimation.getEndTime();
      if ((endTime == null) || (endTime.after(tmEndTime))) {
        endTime = tmEndTime;
      }

      if (request.getInterval() != null) {
        ParameterCheckUtil.checkNumberRange(request.getInterval(), 1, null);
      }

      this.request = request;

    } finally {
      logger.methodEnd();
    }

  }

  private TrafficInfoReadResponseBody createResponseBody(List<TrafficHistoryL2slice> l2HistoryList, String startTime,
      String endTime, Integer interval) {
    try {
      logger.methodStart();

      Set<Date> dateSet = new TreeSet<>();

      SliceTrafficEntity sliceTrafficEntity = new SliceTrafficEntity();

      List<SliceTrafficEntity> sliceList = new ArrayList<>();
      sliceList.add(sliceTrafficEntity);

      double totalTraffic = 0;

      if (l2HistoryList.size() > 0) {

        List<TrafficHistoryL2slice> pickupList = (interval == null) ? l2HistoryList : pickup(l2HistoryList, interval);

        for (TrafficHistoryL2slice l2Slice : pickupList) {
          TrafficDataEntity trafficDataEntity = createSliceEntity(l2Slice);
          dateSet.add(l2Slice.getId().getOccurredTime());
          sliceTrafficEntity.getTrafficDataList().add(trafficDataEntity);
            totalTraffic += l2Slice.getValue();
          }
        }
      }

      sliceTrafficEntity.setSliceType("l2vpn");
      sliceTrafficEntity.setSliceId(null);

      Date[] dateArray = dateSet.toArray(new Date[0]);
      if ((startTime == null) || ("".equals(startTime))) {
        startTime = "";
      }
      if (("".equals(startTime)) && (dateSet.size() > 0)) {
        sliceTrafficEntity.setStartTime(format(dateArray[0]));
      } else {
        sliceTrafficEntity.setStartTime(startTime);
      }

      if ((endTime == null) || ("".equals(endTime))) {
        endTime = "";
      }
      if (("".equals(endTime)) && (dateSet.size() > 0)) {
        sliceTrafficEntity.setEndTime(format(dateArray[dateArray.length - 1]));
      } else {
        sliceTrafficEntity.setEndTime(endTime);
      }

      if (interval != null) {
        sliceTrafficEntity.setInterval(interval);
      } else {
        sliceTrafficEntity.setInterval(TrafficCommonData.getInstance().getTrafficInterval());
      }
      sliceTrafficEntity.setTotalTraffic((int) totalTraffic);
      sliceTrafficEntity.setTotalDataNum(sliceTrafficEntity.getTrafficDataList().size());

      TrafficInfoReadResponseBody responseBody = new TrafficInfoReadResponseBody();
      responseBody.setSliceTrafficList(sliceList);

      return responseBody;

    } finally {
      logger.methodEnd();
    }
  }

  private List<TrafficHistoryL2slice> pickup(List<TrafficHistoryL2slice> l2HistoryList, int interval) {
    try {
      logger.methodStart();
      List<TrafficHistoryL2slice> pickupedList = new ArrayList<>();
      Map<String, TrafficHistoryL2slice> beforePickUpedMap = new LinkedHashMap<>();
      Map<String, TrafficHistoryL2slice> beforeCheckedMap = new LinkedHashMap<>();
      Map<String, Date> nextTargetDateMap = new LinkedHashMap<>();

      for (TrafficHistoryL2slice l2Slice : l2HistoryList) {
        Date targetL2SliceOccurredDate = DateUtils.truncate(l2Slice.getId().getOccurredTime(), Calendar.SECOND);
        String key = getKey(l2Slice);
        if (!beforePickUpedMap.containsKey(key)) {
          beforePickUpedMap.put(key, l2Slice);
          pickupedList.add(l2Slice);
          nextTargetDateMap.put(key,
              new Date(targetL2SliceOccurredDate.getTime() + TimeUnit.MINUTES.toMillis(interval)));
        } else {
          Date nextTargetDate = nextTargetDateMap.get(key);
          if (targetL2SliceOccurredDate.getTime() == nextTargetDate.getTime()) {
            pickupedList.add(l2Slice);
            nextTargetDateMap.put(key, new Date(nextTargetDate.getTime() + TimeUnit.MINUTES.toMillis(interval)));
          } else if (targetL2SliceOccurredDate.after(nextTargetDate)) {
            TrafficHistoryL2slice pickupTarget = beforeCheckedMap.get(key);
            long multiply = ((targetL2SliceOccurredDate.getTime() - nextTargetDate.getTime())
                / TimeUnit.MINUTES.toMillis(interval)) + 1;
            long remainder = (targetL2SliceOccurredDate.getTime() - nextTargetDate.getTime())
                % TimeUnit.MINUTES.toMillis(interval);
            nextTargetDateMap.put(key,
                new Date(nextTargetDate.getTime() + multiply * TimeUnit.MINUTES.toMillis(interval)));
            if (!pickupedList.contains(pickupTarget)) {
              pickupedList.add(pickupTarget);
            }
            if (remainder == 0) {
              pickupedList.add(l2Slice);
            }
          }
        }
        beforeCheckedMap.put(key, l2Slice);
      }
      for (String key : beforeCheckedMap.keySet()) {
        if (!pickupedList.contains(beforeCheckedMap.get(key))) {
          pickupedList.add(beforeCheckedMap.get(key));
        }
      }
      Collections.sort(pickupedList, new Comparator<TrafficHistoryL2slice>() {

        @Override
        public int compare(TrafficHistoryL2slice thl2s1, TrafficHistoryL2slice thl2s2) {
          return thl2s1.getId().getOccurredTime().compareTo(thl2s2.getId().getOccurredTime());
        }
      });
      return pickupedList;
    } finally {
      logger.methodEnd();
    }
  }

  private String getKey(TrafficHistoryL2slice l2Slice) {
    try {
      logger.methodStart();
      TrafficHistoryL2slicePK pk = l2Slice.getId();
      String key = pk.getStartClusterId() + pk.getStartLeafNodeId() + pk.getStartEdgePointId() + pk.getEndClusterId()
          + pk.getEndLeafNodeId() + pk.getEndEdgePointId();
      logger.trace(key);
      return key;
    } finally {
      logger.methodEnd();
    }
  }

}
