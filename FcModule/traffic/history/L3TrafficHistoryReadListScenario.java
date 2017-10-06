package msf.fc.traffic.history;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DateUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.TrafficHistoryL3slice;
import msf.fc.common.data.TrafficHistoryL3slicePK;
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

public class L3TrafficHistoryReadListScenario extends AbstractTrafficScenario<L3TrafficRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(L3TrafficHistoryReadListScenario.class);

  private L3TrafficRequest request;

  private Date startTime;
  private Date endTime;

  public L3TrafficHistoryReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
      TrafficHistoryL3sliceDao l3Dao = new TrafficHistoryL3sliceDao();
      List<TrafficHistoryL3slice> l3HistoryList = null;
      if (request.getSliceId() != null) {
        l3HistoryList = l3Dao.readList(sessionWrapper, startTime, endTime, request.getSliceId());
      } else {
        l3HistoryList = l3Dao.readList(sessionWrapper, startTime, endTime);
      }
      TrafficInfoReadResponseBody responseBody = createResponseBody(l3HistoryList, request.getSliceId(),
          request.getStartTime(), request.getEndTime(), request.getInterval());
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

  private TrafficInfoReadResponseBody createResponseBody(List<TrafficHistoryL3slice> l3HistoryList, String sliceId,
      String startTime, String endTime, Integer interval) {
    try {
      logger.methodStart();

      Map<String, List<TrafficHistoryL3slice>> l3SliceMap = new HashMap<>();
      for (TrafficHistoryL3slice l3slice : l3HistoryList) {
        String sliceIdl = l3slice.getId().getSliceId();
        if (l3SliceMap.get(sliceIdl) == null) {
          l3SliceMap.put(sliceIdl, new LinkedList<>());
        }
        List<TrafficHistoryL3slice> l3SliceList = l3SliceMap.get(sliceIdl);
        l3SliceList.add(l3slice);
      }

      List<SliceTrafficEntity> sliceList = new ArrayList<>();

      Map<String, List<TrafficHistoryL3slice>> sortedMap = new TreeMap<>(l3SliceMap);
      for (String sliceIdl : sortedMap.keySet()) {
        List<TrafficHistoryL3slice> pickupList = (interval == null) ? l3SliceMap.get(sliceIdl)
            : pickup(l3SliceMap.get(sliceIdl), interval);

        double totalTraffic = 0;
        Set<Date> dateSet = new TreeSet<>();
        SliceTrafficEntity sliceTrafficEntity = new SliceTrafficEntity();
        for (TrafficHistoryL3slice l3Slice : pickupList) {
          TrafficDataEntity trafficDataEntity = createSliceEntity(l3Slice);
          dateSet.add(l3Slice.getId().getOccurredTime());
          sliceTrafficEntity.getTrafficDataList().add(trafficDataEntity);
            totalTraffic += l3Slice.getValue();
          }
        }

        sliceTrafficEntity.setSliceType("l3vpn");
        sliceTrafficEntity.setSliceId(sliceIdl);

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

        sliceList.add(sliceTrafficEntity);
      }

      TrafficInfoReadResponseBody responseBody = new TrafficInfoReadResponseBody();
      responseBody.setSliceTrafficList(sliceList);

      return responseBody;

    } finally {
      logger.methodEnd();
    }
  }

  private List<TrafficHistoryL3slice> pickup(List<TrafficHistoryL3slice> l3HistoryList, int interval) {
    try {
      logger.methodStart();
      List<TrafficHistoryL3slice> pickupedList = new ArrayList<>();
      Map<String, TrafficHistoryL3slice> beforePickUpedMap = new LinkedHashMap<>();
      Map<String, TrafficHistoryL3slice> beforeCheckedMap = new LinkedHashMap<>();
      Map<String, Date> nextTargetDateMap = new LinkedHashMap<>();

      for (TrafficHistoryL3slice l3Slice : l3HistoryList) {
        Date targetL3SliceOccurredDate = DateUtils.truncate(l3Slice.getId().getOccurredTime(), Calendar.SECOND);
        String key = getKey(l3Slice);
        if (!beforePickUpedMap.containsKey(key)) {
          beforePickUpedMap.put(key, l3Slice);
          pickupedList.add(l3Slice);
          nextTargetDateMap.put(key,
              new Date(targetL3SliceOccurredDate.getTime() + TimeUnit.MINUTES.toMillis(interval)));
        } else {
          Date nextTargetDate = nextTargetDateMap.get(key);
          if (targetL3SliceOccurredDate.getTime() == nextTargetDate.getTime()) {
            pickupedList.add(l3Slice);
            nextTargetDateMap.put(key, new Date(nextTargetDate.getTime() + TimeUnit.MINUTES.toMillis(interval)));
          } else if (targetL3SliceOccurredDate.after(nextTargetDate)) {
            TrafficHistoryL3slice pickupTarget = beforeCheckedMap.get(key);
            long multiply = ((targetL3SliceOccurredDate.getTime() - nextTargetDate.getTime())
                / TimeUnit.MINUTES.toMillis(interval)) + 1;
            long remainder = (targetL3SliceOccurredDate.getTime() - nextTargetDate.getTime())
                % TimeUnit.MINUTES.toMillis(interval);
            nextTargetDateMap.put(key,
                new Date(nextTargetDate.getTime() + multiply * TimeUnit.MINUTES.toMillis(interval)));
            if (!pickupedList.contains(pickupTarget)) {
              pickupedList.add(pickupTarget);
            }
            if (remainder == 0) {
              pickupedList.add(l3Slice);
            }
          }
        }
        beforeCheckedMap.put(key, l3Slice);
      }
      for (String key : beforeCheckedMap.keySet()) {
        if (!pickupedList.contains(beforeCheckedMap.get(key))) {
          pickupedList.add(beforeCheckedMap.get(key));
        }
      }
      Collections.sort(pickupedList, new Comparator<TrafficHistoryL3slice>() {

        @Override
        public int compare(TrafficHistoryL3slice thl3s1, TrafficHistoryL3slice thl3s2) {
          return thl3s1.getId().getOccurredTime().compareTo(thl3s2.getId().getOccurredTime());
        }
      });
      return pickupedList;
    } finally {
      logger.methodEnd();
    }
  }

  private String getKey(TrafficHistoryL3slice l3Slice) {
    try {
      logger.methodStart();
      TrafficHistoryL3slicePK pk = l3Slice.getId();
      String key = pk.getSliceId() + pk.getStartClusterId() + pk.getStartLeafNodeId() + pk.getStartCpId()
          + pk.getEndClusterId() + pk.getEndLeafNodeId() + pk.getEndCpId();
      logger.trace(key);
      return key;
    } finally {
      logger.methodEnd();
    }
  }

}
