
package msf.mfcfc.failure.status.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.SliceUnitReachableOppositeType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.AbstractScenario;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.failure.status.data.entity.FailureStatusReachableStatusFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceClusterLinkReachableStatusEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceUnitEntity;
import msf.mfcfc.rest.common.RestClient;

public abstract class AbstractFailureStatusScenarioBase<T extends RestRequestBase> extends AbstractScenario<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractFailureStatusScenarioBase.class);

  protected Object deepCopy(Object obj) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      new ObjectOutputStream(baos).writeObject(obj);
      return new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())).readObject();
    } catch (IOException | ClassNotFoundException ioe) {
      throw new IllegalArgumentException();
    }
  }

  protected FailureStatusSliceUnitEntity takeDiffBetweenBeforeAndAfterNotify(
      FailureStatusSliceUnitEntity beforeNotifyEntity, FailureStatusSliceUnitEntity afterNotifyEntity,
      boolean withClusterLinkIf) {
    try {
      logger.methodStart(new String[] { "beforeNotifyEntity", "afterNotifyEntity", "withClusterLinkIf" },
          new Object[] { beforeNotifyEntity, afterNotifyEntity, withClusterLinkIf });

      FailureStatusSliceUnitEntity diffEntity = (FailureStatusSliceUnitEntity) deepCopy(afterNotifyEntity);

      Iterator<FailureStatusSliceFailureEntity> iteratorSliceDiffEntity = diffEntity.getSliceList().iterator();

      while (iteratorSliceDiffEntity.hasNext()) {
        FailureStatusSliceFailureEntity afterEntity = iteratorSliceDiffEntity.next();
        for (FailureStatusSliceFailureEntity beforeEntity : beforeNotifyEntity.getSliceList()) {

          if (beforeEntity.getSliceId().equals(afterEntity.getSliceId())
              && beforeEntity.getSliceType().equals(afterEntity.getSliceType())) {

            removeAllReachableBetweenCps(beforeEntity, afterEntity);

            if (withClusterLinkIf) {
              removeReachableBetweenCpAndClusterLinkIf(beforeEntity, afterEntity);
            }

            if (afterEntity.getReachableStatusList().size() == 0) {
              iteratorSliceDiffEntity.remove();
            }
            break;
          }
        }
      }

      if (withClusterLinkIf) {

        removeReachableBetweenClusterLinkIf(beforeNotifyEntity, diffEntity);
      }
      return diffEntity;
    } finally {
      logger.methodEnd();
    }
  }

  protected void notifyFailureInfo(RestRequestBase request, String ipAddress, int port, int timeout, int retryNum) {

    try {
      logger.methodStart(new String[] { "request", "ipAddress", "port", "timeout", "retryNum" },
          new Object[] { request, ipAddress, port, timeout, retryNum });

      for (int cnt = 0; cnt <= retryNum; cnt++) {
        try {
          RestClient.sendRequest(MfcFcRequestUri.FAILURE_NOTIFY.getHttpMethod(),
              MfcFcRequestUri.FAILURE_NOTIFY.getUri(), request, ipAddress, port);
          break;
        } catch (MsfException msfException) {

          try {
            Thread.sleep(timeout);
          } catch (InterruptedException ie) {

          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void removeAllReachableBetweenCps(FailureStatusSliceFailureEntity beforeEntity,
      FailureStatusSliceFailureEntity afterEntity) {
    try {
      logger.methodStart(new String[] { "beforeEntity", "afterEntity" }, new Object[] { beforeEntity, afterEntity });

      if (afterEntity.getFailureStatus().equals(beforeEntity.getFailureStatus())) {
        Iterator<FailureStatusReachableStatusFailureEntity> cpCpReachableIterator = afterEntity.getReachableStatusList()
            .iterator();

        while (cpCpReachableIterator.hasNext()) {
          FailureStatusReachableStatusFailureEntity afterReachable = cpCpReachableIterator.next();

          if (afterReachable.getOppositeTypeEnum().equals(SliceUnitReachableOppositeType.CP)) {
            cpCpReachableIterator.remove();
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void removeReachableBetweenCpAndClusterLinkIf(FailureStatusSliceFailureEntity beforeEntity,
      FailureStatusSliceFailureEntity afterEntity) {
    try {
      logger.methodStart(new String[] { "beforeEntity", "afterEntity" }, new Object[] { beforeEntity, afterEntity });

      Iterator<FailureStatusReachableStatusFailureEntity> cpCpReachableIterator = afterEntity.getReachableStatusList()
          .iterator();
      while (cpCpReachableIterator.hasNext()) {
        FailureStatusReachableStatusFailureEntity afterReachable = cpCpReachableIterator.next();
        for (FailureStatusReachableStatusFailureEntity beforeReachable : beforeEntity.getReachableStatusList()) {

          if (beforeReachable.getCpId().equals(afterReachable.getCpId())
              && beforeReachable.getOppositeId().equals(afterReachable.getOppositeId())
              && beforeReachable.getOppositeType().equals(afterReachable.getOppositeType())
              && beforeReachable.getOppositeTypeEnum().equals(SliceUnitReachableOppositeType.CLUSTER_LINK_IF)) {

            if (beforeReachable.getReachableStatus().equals(afterReachable.getReachableStatus())) {
              cpCpReachableIterator.remove();
            }

            break;
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void removeReachableBetweenClusterLinkIf(FailureStatusSliceUnitEntity beforeNotifyEntity,
      FailureStatusSliceUnitEntity afterNotifyEntity) {
    try {
      logger.methodStart(new String[] { "beforeNotifyEntity", "afterNotifyEntity" },
          new Object[] { beforeNotifyEntity, afterNotifyEntity });

      if (afterNotifyEntity.getClusterLink() != null && beforeNotifyEntity.getClusterLink() != null) {
        Iterator<FailureStatusSliceClusterLinkReachableStatusEntity> iteratorClusterLinkIfDiffEntity = afterNotifyEntity
            .getClusterLink().getReachableStatusList().iterator();
        while (iteratorClusterLinkIfDiffEntity.hasNext()) {
          FailureStatusSliceClusterLinkReachableStatusEntity afterReachable = iteratorClusterLinkIfDiffEntity.next();
          for (FailureStatusSliceClusterLinkReachableStatusEntity beforeReachable : beforeNotifyEntity.getClusterLink()
              .getReachableStatusList()) {

            if (beforeReachable.getClusterLinkIfId().equals(afterReachable.getClusterLinkIfId())
                && beforeReachable.getOppositeClusterLinkIfId().equals(afterReachable.getOppositeClusterLinkIfId())) {

              if (beforeReachable.getReachableStatus().equals(afterReachable.getReachableStatus())) {
                iteratorClusterLinkIfDiffEntity.remove();
              }
              break;
            }
          }
        }

        if (afterNotifyEntity.getClusterLink().getReachableStatusList().size() == 0) {
          afterNotifyEntity.setClusterLink(null);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }
}
