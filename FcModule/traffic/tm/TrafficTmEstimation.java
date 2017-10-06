package msf.fc.traffic.tm;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.data.TrafficHistoryL2slice;
import msf.fc.common.data.TrafficHistoryL2slicePK;
import msf.fc.common.data.TrafficHistoryL3slice;
import msf.fc.common.data.TrafficHistoryL3slicePK;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.traffic.TrafficHistoryL2sliceDao;
import msf.fc.db.dao.traffic.TrafficHistoryL3sliceDao;
import msf.fc.traffic.data.TrafficCommonData;
import msf.fc.traffic.tm.TrafficVirtualTopology.DiNodePair;
import msf.fc.traffic.tm.TrafficVirtualTopology.DiNodeType;

public class TrafficTmEstimation extends Thread {

  private static final MsfLogger logger = MsfLogger.getInstance(TrafficTmEstimation.class);

  private boolean isRunning = false;

  private boolean wakeupFlag = false;

  private static Date startTime = new Date();

  private static Date lastStartTime = new Date();

  private static Date endTime = new Date();

  private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
  private Date beforeTrafficValueTime;

  private TrafficVirtualTopology vtopology;
  private TrafficAtxt atxt;
  private TrafficXtxt xtxt;
  private TrafficInOuttxt inouttxt;
  private TrafficSdtxt sdtxt;
  private TrafficTgtxt tgtxt;
  private TrafficNodetrafficInfo nodeTrafficInfo;
  private TrafficCommonData trafficCommonData;

  private Set<DiNodePair> trafficValue;

  public static final Object LOCK = new Object();

  private boolean renewTopology = false;

  public TrafficTmEstimation() {
    vtopology = TrafficVirtualTopology.getInstance();
    trafficCommonData = TrafficCommonData.getInstance();

    atxt = new TrafficAtxt();
    inouttxt = new TrafficInOuttxt();
    xtxt = new TrafficXtxt();
    sdtxt = new TrafficSdtxt();
    tgtxt = new TrafficTgtxt();
    nodeTrafficInfo = new TrafficNodetrafficInfo();

    beforeTrafficValueTime = new Date();
  }

  public boolean isRunning() {
    return isRunning;
  }

  public void setWakeupFlag(boolean wakeupFlag) {
    this.wakeupFlag = wakeupFlag;
  }

  protected synchronized void lock() throws InterruptedException {
    logger.trace("Sleep TrafficTmEstimation...");

    while ((!wakeupFlag) && (!trafficCommonData.isForceStop())) {
      wait();
    }

    logger.trace("Wakeup TrafficTmEstimation.");

    isRunning = true;
    wakeupFlag = false;
  }

  @Override
  public void run() {
    while (true) {
      try {

        lock();


        executeTmEstimation();

      } catch (InterruptedException ie) {
        if (trafficCommonData.isForceStop()) {
          logger.warn("Force Stop.");
          return;
        }
        logger.debug("InterruptedException. Not ForceStop.");
      } catch (MsfException exp) {
      } finally {
        isRunning = false;
      }
    }
  }

  public void executeTmEstimation() throws MsfException, InterruptedException {
    logger.methodStart();

    setStartTime(new Date());
    boolean notSkip = false;

    try {
      final long tmStart = System.currentTimeMillis();

      if (this.getRenewTopology()) {

        vtopology.create();

        checkFile(TrafficAtxt.ATXT_FILE);
        atxt.createFile();

        checkFile(TrafficInOuttxt.INOUTTXT_FILE);
        inouttxt.createFile();

        checkFile(TrafficSdtxt.SDTXT_FILE);
        sdtxt.createFile();

        this.setRenewTopology(false);

      }

      nodeTrafficInfo.requestNodeTraffic();

      if (trafficCommonData.getTrafficResponse() != null) {
        Date respDate = DateUtils.parseDate(trafficCommonData.getTrafficResponse().getTime(),
            new String[] { DATE_FORMAT });
        if ((trafficCommonData.getTrafficResponse().isSuccess()) && !(beforeTrafficValueTime.equals(respDate))) {

          beforeTrafficValueTime = respDate;

          checkFile(TrafficXtxt.XTXT_FILE);
          xtxt.createFile();

          executeTmTool();

          trafficValue = tgtxt.createTgResult();

          final long saveStart = System.currentTimeMillis();
          saveTrafficHistory(trafficValue, getStartTime());
          long saveEnd = System.currentTimeMillis();
          logger.performance("Traffic information saveEnd(sec)：" + (saveEnd - saveStart) / 1000);

          notSkip = true;

          deleteTrafficDataRetentionPeriod();

          checkFile(TrafficTgtxt.TGTXT_FILE);

          long tmEnd = System.currentTimeMillis();
          logger.performance("TM Estimation execute end time(sec)：" + (tmEnd - tmStart) / 1000);

        } else {

          if (!trafficCommonData.getTrafficResponse().isSuccess()) {
            logger.debug("Traffic information receved form EC. But isSuccess is false.");
          }
          if (beforeTrafficValueTime.equals(respDate)) {
            logger.debug("Traffic Time from EC is Same at before Time.");
          }
          notSkip = true;
          saveSkipData(getStartTime());
        }
      } else {
        logger.warn("Traffic response data null form EC.");
        notSkip = true;
        saveSkipData(getStartTime());

      }
    } catch (MsfException mexp) {
      if (!notSkip) {
        try {
          saveSkipData(getStartTime());
        } catch (MsfException tmp) {
          throw tmp;
        }
      }
      throw mexp;

    } catch (ParseException | IllegalArgumentException pe) {
      logger.debug("String -> Date format Parse error.", pe);
      try {
        saveSkipData(getStartTime());
      } catch (MsfException tmp) {
        logger.warn("Traffic MsfException in saveSkipData().");
        throw tmp;
      }
    } catch (InterruptedException ie) {
      logger.warn("Traffic InterruptedException.");
      throw ie;
    } catch (Exception ex) {
      logger.warn("Some kind of Exception.", ex);
      throw new MsfException(ErrorCode.UNDEFINED_ERROR, "Some kind of Exception.");
    } finally {
      notSkip = false;
      setEndTime(new Date());
      logger.methodEnd();
    }
  }

  private void executeTmTool() throws MsfException, InterruptedException {

    logger.methodStart();

    final long toolStart = System.currentTimeMillis();

    List<String> command = new ArrayList<>();

    command.add(trafficCommonData.getTrafficTmToolPath() + TrafficVirtualTopology.TOMOGEAVITY);
    command.add(trafficCommonData.getTrafficTmInputFilePath() + TrafficInOuttxt.INOUTTXT_FILE);
    command.add(trafficCommonData.getTrafficTmInputFilePath() + TrafficAtxt.ATXT_FILE);
    command.add(trafficCommonData.getTrafficTmInputFilePath() + TrafficXtxt.XTXT_FILE);
    command.add("-o");
    command.add(trafficCommonData.getTrafficTmOutputFilePath() + TrafficTgtxt.TGTXT_FILE);
    command.add("-r");
    command.add(trafficCommonData.getTrafficTmInputFilePath() + TrafficSdtxt.SDTXT_FILE);

    logger.debug("TMTool command = " + command);
    try {

      ProcessBuilder tmPb = new ProcessBuilder(command);
      Process tmProc = tmPb.start();
      tmProc.waitFor();

    } catch (IOException ioexp) {
      logger.warn("TM TOOL Execute error. Reason : Catch IOException.", ioexp);
      throw new MsfException(ErrorCode.EXECUTE_FILE_ERROR, "TM TOOL Execute error.");

    } catch (InterruptedException inexp) {
      logger.warn("TM TOOL Execute error. Reason : Catch InterruptedException.", inexp);
      throw inexp;

    } finally {
      long toolEnd = System.currentTimeMillis();
      logger.performance("TM TOOL Execute time (sec)：" + (toolEnd - toolStart) / 1000);
      logger.methodEnd();

    }

  }

  private void saveTrafficHistory(Set<DiNodePair> trafficValue, Date occurredTime)
      throws MsfException, InterruptedException {
    logger.methodStart();

    SessionWrapper sessionWrapper = new SessionWrapper();

    try {
      sessionWrapper.openSession();
      sessionWrapper.beginTransaction();

      for (DiNodePair pair : trafficValue) {


        if (DiNodeType.L3CP.equals(pair.getFrom().getNodeType())) {
          TrafficHistoryL3slicePK l3slicePk = new TrafficHistoryL3slicePK();
          l3slicePk.setStartClusterId(pair.getFrom().getNode().getEquipment().getId().getSwClusterId());
          l3slicePk.setStartCpId(pair.getFrom().getL3Cp().getId().getCpId());
          l3slicePk.setStartLeafNodeId(pair.getFrom().getNode().getNodeId());

          l3slicePk.setEndClusterId(pair.getTo().getNode().getEquipment().getId().getSwClusterId());
          l3slicePk.setEndCpId(pair.getTo().getL3Cp().getId().getCpId());
          l3slicePk.setEndLeafNodeId(pair.getTo().getNode().getNodeId());

          l3slicePk.setSliceId(pair.getFrom().getL3Cp().getL3Slice().getSliceId());

          l3slicePk.setOccurredTime(occurredTime);

          TrafficHistoryL3slice l3slice = new TrafficHistoryL3slice();
          l3slice.setId(l3slicePk);

          l3slice.setValue(pair.getTrafficValue());

          TrafficHistoryL3sliceDao l3dao = new TrafficHistoryL3sliceDao();
          l3dao.create(sessionWrapper, l3slice);

        } else if (DiNodeType.L2EP.equals(pair.getFrom().getNodeType())) {
          TrafficHistoryL2slicePK l2slicePk = new TrafficHistoryL2slicePK();
          l2slicePk.setStartClusterId(pair.getFrom().getNode().getEquipment().getId().getSwClusterId());
          l2slicePk.setStartEdgePointId(pair.getFrom().getEdgePoint().getId().getEdgePointId());
          l2slicePk.setStartLeafNodeId(pair.getFrom().getNode().getNodeId());

          l2slicePk.setEndClusterId(pair.getFrom().getNode().getEquipment().getId().getSwClusterId());
          l2slicePk.setEndEdgePointId(pair.getTo().getEdgePoint().getId().getEdgePointId());
          l2slicePk.setEndLeafNodeId(pair.getTo().getNode().getNodeId());

          l2slicePk.setOccurredTime(occurredTime);

          TrafficHistoryL2slice l2slice = new TrafficHistoryL2slice();
          l2slice.setId(l2slicePk);

          l2slice.setValue(pair.getTrafficValue());

          TrafficHistoryL2sliceDao l2dao = new TrafficHistoryL2sliceDao();
          l2dao.create(sessionWrapper, l2slice);

        }
      }
      sessionWrapper.commit();

    } catch (MsfException exp) {
      logger.warn("TrafficHistory information save error.", exp);
      try {
        sessionWrapper.rollback();
      } catch (MsfException tmp) {
        throw tmp;
      }
      throw exp;
    } catch (InterruptedException ie) {
      try {
        sessionWrapper.rollback();
      } catch (MsfException exp) {
        logger.warn("saveSkipData(): InterruptedException. Execute rollback error.");
      }
      throw ie;
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  private void saveSkipData(Date occurredTime) throws InterruptedException, MsfException {
    logger.methodStart();

    SessionWrapper sessionWrapper = new SessionWrapper();

    try {
      sessionWrapper.openSession();
      sessionWrapper.beginTransaction();


      if ((vtopology.getDiEdgeSet() != null) && (vtopology.getDiEdgeSet().size() > 0)) {

        if (vtopology.getAllPair() == null) {
          atxt.createPair();
        }

        for (DiNodePair virtualEdge : vtopology.getAllPair()) {
          switch (virtualEdge.getFrom().getNodeType()) {
            case L3CP:
              switch (virtualEdge.getTo().getNodeType()) {
                case L3CP:
                  if (virtualEdge.getFrom().getL3Cp().getId().getSliceId()
                      .equals(virtualEdge.getTo().getL3Cp().getId().getSliceId())) {
                    saveTrafficHistorySkipL3(sessionWrapper, virtualEdge, occurredTime);
                    logger.warn("TM Estimation skipped. Saved DataBase L3 (-1).");
                  }
                  break;
                default:
                  break;
              }
              break;
            case L2EP:
              switch (virtualEdge.getTo().getNodeType()) {
                case L2EP:
                  saveTrafficHistorySkipL2(sessionWrapper, virtualEdge, occurredTime);
                  logger.warn("TM Estimation skipped. Saved DataBase L2 (-1).");
                  break;
                default:
                  break;
              }
              break;
              break;

          }
        }
        sessionWrapper.commit();

      } else {
      }
    } catch (MsfException exp) {
      try {
        sessionWrapper.rollback();
      } catch (MsfException tmp) {
        throw tmp;
      }
      throw exp;

    } catch (InterruptedException ie) {
      try {
        sessionWrapper.rollback();
      } catch (MsfException exp) {
        logger.warn("saveSkipData(): InterruptedException. Execute rollback error.");
      }
      throw ie;
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  private void saveTrafficHistorySkipL3(SessionWrapper sessionWrapper, DiNodePair virtualEdge, Date occurredTime)
      throws MsfException, InterruptedException {
    logger.methodStart();

    try {

      TrafficHistoryL3slicePK l3slicePk = new TrafficHistoryL3slicePK();
      l3slicePk.setStartClusterId(virtualEdge.getFrom().getNode().getEquipment().getId().getSwClusterId());
      l3slicePk.setStartCpId(virtualEdge.getFrom().getL3Cp().getId().getCpId());
      l3slicePk.setStartLeafNodeId(virtualEdge.getFrom().getNode().getNodeId());

      l3slicePk.setEndClusterId(virtualEdge.getTo().getNode().getEquipment().getId().getSwClusterId());
      l3slicePk.setEndCpId(virtualEdge.getTo().getL3Cp().getId().getCpId());
      l3slicePk.setEndLeafNodeId(virtualEdge.getTo().getNode().getNodeId());

      l3slicePk.setSliceId(virtualEdge.getFrom().getL3Cp().getL3Slice().getSliceId());

      l3slicePk.setOccurredTime(occurredTime);

      TrafficHistoryL3slice l3slice = new TrafficHistoryL3slice();
      l3slice.setId(l3slicePk);

      l3slice.setValue(TrafficCommonData.TMSKIPVALUE);

      TrafficHistoryL3sliceDao l3dao = new TrafficHistoryL3sliceDao();
      l3dao.create(sessionWrapper, l3slice);

    } catch (MsfException exp) {
      logger.warn("TM Estimation to skiped information save error (L3).", exp);
      throw exp;
    } finally {
      logger.methodEnd();
    }
  }

  private void saveTrafficHistorySkipL2(SessionWrapper sessionWrapper, DiNodePair virtualEdge, Date occurredTime)
      throws MsfException, InterruptedException {
    logger.methodStart();

    try {

      TrafficHistoryL2slicePK l2slicePk = new TrafficHistoryL2slicePK();
      l2slicePk.setStartClusterId(virtualEdge.getFrom().getNode().getEquipment().getId().getSwClusterId());
      l2slicePk.setStartEdgePointId(virtualEdge.getFrom().getEdgePoint().getId().getEdgePointId());
      l2slicePk.setStartLeafNodeId(virtualEdge.getFrom().getNode().getNodeId());

      l2slicePk.setEndClusterId(virtualEdge.getTo().getNode().getEquipment().getId().getSwClusterId());
      l2slicePk.setEndEdgePointId(virtualEdge.getTo().getEdgePoint().getId().getEdgePointId());
      l2slicePk.setEndLeafNodeId(virtualEdge.getTo().getNode().getNodeId());

      l2slicePk.setOccurredTime(occurredTime);

      TrafficHistoryL2slice l2slice = new TrafficHistoryL2slice();
      l2slice.setId(l2slicePk);

      l2slice.setValue(TrafficCommonData.TMSKIPVALUE);

      logger.info(l2slice.toString());

      TrafficHistoryL2sliceDao l2dao = new TrafficHistoryL2sliceDao();
      l2dao.create(sessionWrapper, l2slice);

    } catch (MsfException exp) {
      logger.warn("TM Estimation to skiped information save error (L2).", exp);
      throw exp;
    } finally {
      logger.methodEnd();
    }
  }

  private void deleteTrafficDataRetentionPeriod() throws MsfException {
    logger.methodStart();

    final long deleteStart = System.currentTimeMillis();
    SessionWrapper sessionWrapper = new SessionWrapper();

    try {

      sessionWrapper.openSession();
      sessionWrapper.beginTransaction();

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DAY_OF_MONTH, -TrafficCommonData.getInstance().getTrafficDataRetentionPeriod());
      TrafficHistoryL3sliceDao trfficL3HistroyDao = new TrafficHistoryL3sliceDao();
      trfficL3HistroyDao.deleteExpiredData(sessionWrapper, new java.sql.Date(cal.getTime().getTime()));
      TrafficHistoryL2sliceDao trfficL2HistroyDao = new TrafficHistoryL2sliceDao();
      trfficL2HistroyDao.deleteExpiredData(sessionWrapper, new java.sql.Date(cal.getTime().getTime()));

      sessionWrapper.commit();
    } catch (MsfException exp) {
      try {
        sessionWrapper.rollback();
      } catch (MsfException tmp) {
        throw tmp;
      }
      logger.warn("TrafficDataRetentionPeriod delete error.", exp);
      throw exp;
    } finally {
      sessionWrapper.closeSession();

      long deleteEnd = System.currentTimeMillis();
      logger.performance("Traffic information delete(sec)：" + (deleteEnd - deleteStart) / 1000);
      logger.methodEnd();
    }

  }

  protected void checkFile(String target) throws MsfException {
    logger.methodStart();

    try {
      File dir;
      if (TrafficTgtxt.TGTXT_FILE.equals(target)) {

        dir = new File(trafficCommonData.getTrafficTmOutputFilePath());

      } else {

        dir = new File(trafficCommonData.getTrafficTmInputFilePath());

      }
      if ((dir.exists()) && (dir.isDirectory())) {
        for (File file : dir.listFiles()) {
            if (file.delete()) {
              logger.debug(target + " File delete Success.");
              return;
            } else {
              logger.warn("File delete error. Reason : delete() is error");
              throw new MsfException(ErrorCode.FILE_DELETE_ERROR, "File delete error. Reason : delete() is false.");
            }
          }
        }
      } else {
        logger.warn("File path error or Not Directory.");
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "File path error or Not Directory.");
      }
    } catch (NullPointerException ne) {
      logger.warn("File delete error. Reason : NullPointerException.", ne);
      throw new MsfException(ErrorCode.FILE_DELETE_ERROR, "File delete error. Reason : NullPointerException.");
    } catch (SecurityException se) {
      logger.warn("File delete error. Reason : SecurityException.", se);
      throw new MsfException(ErrorCode.FILE_DELETE_ERROR, "File delete error. Reason : SecurityException.");
    } finally {
      logger.methodEnd();
    }
  }

  public static Date getStartTime() {
    return startTime;
  }

  public static void setStartTime(Date startTime) {
    setLastStartTime(TrafficTmEstimation.startTime);
    TrafficTmEstimation.startTime = startTime;
  }

  public static Date getLastStartTime() {
    return lastStartTime;
  }

  public static void setLastStartTime(Date lastStartTime) {
    TrafficTmEstimation.lastStartTime = lastStartTime;
  }

  public static Date getEndTime() {
    return endTime;
  }

  public static void setEndTime(Date endTime) {
    TrafficTmEstimation.endTime = endTime;
  }

  public void setRenewTopology(boolean flag) {
    synchronized (LOCK) {
      renewTopology = flag;
    }
  }

  public boolean getRenewTopology() {
    synchronized (LOCK) {
      return renewTopology;
    }

  }

}
