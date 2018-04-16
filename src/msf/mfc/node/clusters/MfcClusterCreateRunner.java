
package msf.mfc.node.clusters;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.system.SwCluster;
import msf.mfc.common.data.MfcEquipment;
import msf.mfc.common.data.MfcL2Slice;
import msf.mfc.common.data.MfcL3Slice;
import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.MfcDbManager;
import msf.mfc.db.dao.clusters.MfcEquipmentDao;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfc.db.dao.slices.MfcL2SliceDao;
import msf.mfc.db.dao.slices.MfcL3SliceDao;
import msf.mfc.node.MfcNodeManager;
import msf.mfc.slice.MfcSliceManager;
import msf.mfcfc.common.constant.ClusterProvisioningStatus;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.clusters.data.SwClusterCreateRequestBody;
import msf.mfcfc.node.clusters.data.SwClusterCreaterAsyncResponseBody;
import msf.mfcfc.node.clusters.data.SwClusterRequest;
import msf.mfcfc.node.equipments.data.EquipmentCreateRequestBody;
import msf.mfcfc.node.equipments.data.EquipmentCreateResponseBody;
import msf.mfcfc.node.equipments.data.EquipmentReadDetailListResponseBody;
import msf.mfcfc.node.equipments.data.entity.EquipmentTypeEntity;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.slice.slices.l2slice.data.L2SliceCreateRequestBody;
import msf.mfcfc.slice.slices.l2slice.data.L2SliceCreateResponseBody;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceCreateRequestBody;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceCreateResponseBody;

/**
 * Implementation class for the asynchronous process in the SW cluster addition
 * process.
 *
 * @author NTT
 *
 */
public class MfcClusterCreateRunner extends MfcAbstractClusterRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcClusterCreateRunner.class);

  private SwClusterCreateRequestBody requestBody;

  /**
   * Constructor.
   * <p>
   * Set the request information from scenario as arguments
   * </p>
   *
   * @param request
   *          Request
   * @param requestBody
   *          Request body
   */
  public MfcClusterCreateRunner(SwClusterRequest request, SwClusterCreateRequestBody requestBody) {

    this.requestBody = requestBody;

  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      logger.performance("start wait to equipment increasing process.");
      synchronized (MfcNodeManager.getInstance().getMfcEquipmentCreateLockObject()) {
        logger.performance("end wait to equipment increasing process.");
        logger.performance("start wait to l3 slice increasing process.");
        synchronized (MfcSliceManager.getInstance().getL3SliceCreateLockObject()) {
          logger.performance("end wait to l3 slice increasing process.");
          logger.performance("start wait to l2 slice increasing process.");
          synchronized (MfcSliceManager.getInstance().getL2SliceCreateLockObject()) {
            logger.performance("end wait to l2 slice increasing process.");

            RestResponseBase responseBase = null;
            SessionWrapper sessionWrapper = new SessionWrapper();

            try {
              sessionWrapper.openSession();

              MfcSwClusterDao mfcSwClusterDao = new MfcSwClusterDao();
              SwCluster swCluster = MfcConfigManager.getInstance()
                  .getSystemConfSwClusterData(Integer.valueOf(requestBody.getCluster().getClusterId())).getSwCluster();

              String fcControlAddress = swCluster.getFcControlAddress();
              int fcControlPort = swCluster.getFcControlPort();

              setEquipmentData(sessionWrapper, mfcSwClusterDao, fcControlAddress, fcControlPort);

              sessionWrapper.beginTransaction();

              setSliceData(sessionWrapper, fcControlAddress, fcControlPort);

              MfcSwCluster mfcSwCluster = new MfcSwCluster();
              mfcSwCluster.setSwClusterId(Integer.valueOf(requestBody.getCluster().getClusterId()));
              mfcSwCluster.setClusterStatus(ClusterProvisioningStatus.COMPLETED.getCode());
              mfcSwClusterDao.create(sessionWrapper, mfcSwCluster);

              responseBase = responseSwClusterCreateData(mfcSwCluster);

              sessionWrapper.commit();
            } catch (MsfException msfException) {
              logger.error(msfException.getMessage(), msfException);
              sessionWrapper.rollback();
              throw msfException;
            } finally {
              sessionWrapper.closeSession();
            }
            return responseBase;
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void setEquipmentData(SessionWrapper sessionWrapper, MfcSwClusterDao mfcSwClusterDao, String fcControlAddress,
      int fcControlPort) throws MsfException {
    try {
      logger.methodStart(new String[] { "mfcSwClusterDao", "fcControlAddress", "fcControlPort" },
          new Object[] { mfcSwClusterDao, fcControlAddress, fcControlPort });

      List<MfcSwCluster> mfcSwClusters = mfcSwClusterDao.readList(sessionWrapper);
      if (!mfcSwClusters.isEmpty()) {

        MfcEquipmentDao mfcEquipmentDao = new MfcEquipmentDao();
        List<MfcEquipment> mfcEquipments = mfcEquipmentDao.readList(sessionWrapper);
        if (!mfcEquipments.isEmpty()) {
          EquipmentReadDetailListResponseBody equipmentList = sendEquipmentReadList(mfcSwClusters.get(0));
          for (EquipmentTypeEntity equipmentTypeEntity : equipmentList.getEquipmentTypeList()) {
            sendEquipmentCreate(equipmentTypeEntity, fcControlAddress, fcControlPort);
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void setSliceData(SessionWrapper sessionWrapper, String fcControlAddress, int fcControlPort)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "fcControlAddress", "fcControlPort" },
          new Object[] { fcControlAddress, fcControlPort });
      MfcL2SliceDao mfcL2SliceDao = new MfcL2SliceDao();
      MfcL3SliceDao mfcL3SliceDao = new MfcL3SliceDao();
      List<MfcL2Slice> mfcL2Slices = mfcL2SliceDao.readList(sessionWrapper);
      List<MfcL3Slice> mfcL3Slices = mfcL3SliceDao.readList(sessionWrapper);

      if ((!mfcL2Slices.isEmpty()) || (!mfcL3Slices.isEmpty())) {

        logger.performance("start get slice resources lock.");
        MfcDbManager.getInstance().getResourceLock(mfcL2Slices, mfcL3Slices, sessionWrapper);
        logger.performance("end get slice resources lock.");

        for (MfcL2Slice mfcL2Slice : mfcL2Slices) {

          sendL2SliceCreate(mfcL2Slice, fcControlAddress, fcControlPort);
        }

        for (MfcL3Slice mfcL3Slice : mfcL3Slices) {

          sendL3SliceCreate(mfcL3Slice, fcControlAddress, fcControlPort);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private EquipmentReadDetailListResponseBody sendEquipmentReadList(MfcSwCluster mfcSwCluster) throws MsfException {
    try {
      logger.methodStart(new String[] { "mfcSwCluster" }, new Object[] { mfcSwCluster });
      SwCluster swCluster = MfcConfigManager.getInstance().getSystemConfSwClusterData(mfcSwCluster.getSwClusterId())
          .getSwCluster();

      String fcControlAddress = swCluster.getFcControlAddress();
      int fcControlPort = swCluster.getFcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(MfcFcRequestUri.EQUIPMENT_READ_LIST.getHttpMethod(),
          MfcFcRequestUri.EQUIPMENT_READ_LIST.getUri() + "?format=" + RestFormatOption.DETAIL_LIST.getMessage(), null,
          fcControlAddress, fcControlPort);

      EquipmentReadDetailListResponseBody body = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          EquipmentReadDetailListResponseBody.class, ErrorCode.FC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200, body.getErrorCode(),
          ErrorCode.FC_CONTROL_ERROR);

      return body;
    } finally {
      logger.methodEnd();
    }
  }

  private void sendEquipmentCreate(EquipmentTypeEntity equipmentTypeEntity, String fcControlAddress, int fcControlPort)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "equipmentTypeEntity", "fcControlAddress", "fcControlPort" },
          new Object[] { equipmentTypeEntity, fcControlAddress, fcControlPort });
      EquipmentCreateRequestBody equipmentRequestBody = new EquipmentCreateRequestBody();
      equipmentRequestBody.setEquipmentType(equipmentTypeEntity);

      RestRequestBase restRequest = new RestRequestBase();

      restRequest.setRequestBody(JsonUtil.toJson(equipmentRequestBody));

      RestResponseBase restResponseBase = RestClient.sendRequest(MfcFcRequestUri.EQUIPMENT_CREATE.getHttpMethod(),
          MfcFcRequestUri.EQUIPMENT_CREATE.getUri(), restRequest, fcControlAddress, fcControlPort);

      EquipmentCreateResponseBody equipmentCreateResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          EquipmentCreateResponseBody.class, ErrorCode.FC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.CREATED_201,
          equipmentCreateResponseBody.getErrorCode(), ErrorCode.FC_CONTROL_ERROR);

    } finally {
      logger.methodEnd();
    }
  }

  private void sendL2SliceCreate(MfcL2Slice mfcL2Slice, String fcControlAddress, int fcControlPort)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "mfcL2Slice", "fcControlAddress", "fcControlPort" },
          new Object[] { mfcL2Slice, fcControlAddress, fcControlPort });
      L2SliceCreateRequestBody l2SliceCreateRequestBody = new L2SliceCreateRequestBody();
      l2SliceCreateRequestBody.setSliceId(mfcL2Slice.getSliceId());
      l2SliceCreateRequestBody.setRemarkMenu(mfcL2Slice.getRemarkMenu());
      l2SliceCreateRequestBody.setVrfId(mfcL2Slice.getVrfId());

      RestRequestBase restRequest = new RestRequestBase();

      restRequest.setRequestBody(JsonUtil.toJson(l2SliceCreateRequestBody));

      RestResponseBase restResponseBase = RestClient.sendRequest(MfcFcRequestUri.SLICE_CREATE.getHttpMethod(),
          MfcFcRequestUri.SLICE_CREATE.getUri(SliceType.L2_SLICE.getMessage()), restRequest, fcControlAddress,
          fcControlPort);

      L2SliceCreateResponseBody l2SliceCreateResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          L2SliceCreateResponseBody.class, ErrorCode.FC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.CREATED_201,
          l2SliceCreateResponseBody.getErrorCode(), ErrorCode.FC_CONTROL_ERROR);

    } finally {
      logger.methodEnd();
    }
  }

  private void sendL3SliceCreate(MfcL3Slice mfcL3Slice, String fcControlAddress, int fcControlPort)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "mfcL3Slice", "fcControlAddress", "fcControlPort" },
          new Object[] { mfcL3Slice, fcControlAddress, fcControlPort });
      L3SliceCreateRequestBody l3SliceCreateRequestBody = new L3SliceCreateRequestBody();
      l3SliceCreateRequestBody.setSliceId(mfcL3Slice.getSliceId());
      l3SliceCreateRequestBody.setPlane(mfcL3Slice.getPlane());
      l3SliceCreateRequestBody.setRemarkMenu(mfcL3Slice.getRemarkMenu());
      l3SliceCreateRequestBody.setVrfId(mfcL3Slice.getVrfId());

      RestRequestBase restRequest = new RestRequestBase();

      restRequest.setRequestBody(JsonUtil.toJson(l3SliceCreateRequestBody));

      RestResponseBase restResponseBase = RestClient.sendRequest(MfcFcRequestUri.SLICE_CREATE.getHttpMethod(),
          MfcFcRequestUri.SLICE_CREATE.getUri(SliceType.L3_SLICE.getMessage()), restRequest, fcControlAddress,
          fcControlPort);

      L3SliceCreateResponseBody l3SliceCreateResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          L3SliceCreateResponseBody.class, ErrorCode.FC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.CREATED_201,
          l3SliceCreateResponseBody.getErrorCode(), ErrorCode.FC_CONTROL_ERROR);

    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseSwClusterCreateData(MfcSwCluster mfcSwCluster) {
    try {
      logger.methodStart(new String[] { "mfcSwCluster" }, new Object[] { mfcSwCluster });
      SwClusterCreaterAsyncResponseBody body = new SwClusterCreaterAsyncResponseBody();
      body.setClusterId(String.valueOf(mfcSwCluster.getSwClusterId()));
      return createRestResponse(body, HttpStatus.CREATED_201);
    } finally {
      logger.methodEnd();
    }
  }
}
