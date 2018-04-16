
package msf.fc.slice.slices.l2slice;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcL2Cp;
import msf.fc.common.data.FcL2Slice;
import msf.fc.common.data.FcNode;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.slices.FcL2SliceDao;
import msf.fc.rest.ec.core.operation.data.OperationRequestBody;
import msf.fc.rest.ec.core.operation.data.entity.OperationUpdateL2VlanIfOptionEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationVlanIfEcEntity;
import msf.mfcfc.common.constant.EcCommonOperationAction;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.slices.l2slice.data.L2SliceRequest;
import msf.mfcfc.slice.slices.l2slice.data.L2SliceUpdateAsyncResponseBody;
import msf.mfcfc.slice.slices.l2slice.data.L2SliceUpdateRequestBody;

/**
 * Implementation class for the asynchronous process in the L2 slice
 * modification process.
 *
 * @author NTT
 *
 */
public class FcL2SliceUpdateRunner extends FcAbstractL2SliceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL2SliceUpdateRunner.class);

  private L2SliceUpdateRequestBody requestBody;

  /**
   * Constructor. <br>
   * Take over the necessary information from scenario
   *
   * @param request
   *          Request for L2 slice control
   * @param requestBody
   *          Request Body part for L2 slice modification
   */
  public FcL2SliceUpdateRunner(L2SliceRequest request, L2SliceUpdateRequestBody requestBody) {
    this.request = request;
    this.requestBody = requestBody;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();

      checkRemarkMenuList(requestBody.getRemarkMenu());
      try {
        sessionWrapper.openSession();
        FcL2SliceDao l2SliceDao = new FcL2SliceDao();
        FcL2Slice l2Slice = l2SliceDao.read(sessionWrapper, request.getSliceId());

        checkL2SlicePresence(l2Slice, request.getSliceId());
        List<FcL2Slice> l2SliceList = new ArrayList<>();
        l2SliceList.add(l2Slice);

        sessionWrapper.beginTransaction();
        logger.performance("start get l2slice resources lock.");

        FcDbManager.getInstance().getL2SlicesLock(l2SliceList, sessionWrapper);
        logger.performance("end get l2slice resources lock.");

        FcL2Slice l2SliceAfterLock = l2SliceDao.read(sessionWrapper, request.getSliceId());

        checkL2SlicePresence(l2SliceAfterLock, request.getSliceId());

        l2SliceAfterLock.setRemarkMenu(requestBody.getRemarkMenu());

        l2SliceDao.update(sessionWrapper, l2SliceAfterLock);

        if (l2SliceAfterLock.getL2Cps() == null || l2SliceAfterLock.getL2Cps().isEmpty()) {
          sessionWrapper.commit();
        } else {

          RestResponseBase restResponse = sendVlanIfUpdateRequest(sessionWrapper, l2SliceAfterLock);

          commitTransaction(sessionWrapper, restResponse, HttpStatus.OK_200, true);
        }

        return createResponse(l2SliceAfterLock);

      } catch (MsfException exp) {
        logger.error(exp.getMessage(), exp);
        sessionWrapper.rollback();
        throw exp;
      } finally {
        sessionWrapper.closeSession();
      }
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase sendVlanIfUpdateRequest(SessionWrapper sessionWrapper, FcL2Slice fcL2Slice)
      throws MsfException {
    try {
      logger.methodStart();

      FcNodeDao fcNodeDao = new FcNodeDao();
      OperationUpdateL2VlanIfOptionEcEntity l2VlanIfOptionEcEntity = new OperationUpdateL2VlanIfOptionEcEntity();
      List<OperationVlanIfEcEntity> vlanIfEcEntityList = new ArrayList<>();
      for (FcL2Cp fcL2Cp : fcL2Slice.getL2Cps()) {
        OperationVlanIfEcEntity vlanIfEcEntity = new OperationVlanIfEcEntity();

        FcNode fcNode = fcNodeDao.read(sessionWrapper, fcL2Cp.getEdgePoint().getEdgePointId());
        vlanIfEcEntity.setVlanIfId(String.valueOf(fcL2Cp.getVlanIf().getId().getVlanIfId()));
        vlanIfEcEntity.setNodeId(String.valueOf(fcNode.getEcNodeId()));
        vlanIfEcEntityList.add(vlanIfEcEntity);
      }
      l2VlanIfOptionEcEntity.setVlanIfList(vlanIfEcEntityList);
      l2VlanIfOptionEcEntity.setVrfId(String.valueOf(fcL2Slice.getVrfId()));
      l2VlanIfOptionEcEntity.setRemarkMenu(fcL2Slice.getRemarkMenu());
      String requestJson = makeUpdateL2VlanIfData(l2VlanIfOptionEcEntity);

      RestResponseBase responseBase = sendRequestToEc(requestJson, EcRequestUri.OPERATION_REQUEST, (String[]) null);
      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(FcL2Slice fcL2Slice) throws MsfException {
    try {
      logger.methodStart();
      List<String> updatedCpIdList = new ArrayList<>();
      for (FcL2Cp fcL2Cp : fcL2Slice.getL2Cps()) {
        updatedCpIdList.add(fcL2Cp.getId().getCpId());
      }
      L2SliceUpdateAsyncResponseBody responseBody = new L2SliceUpdateAsyncResponseBody();
      responseBody.setUpdatedCpIdList(updatedCpIdList);
      return new RestResponseBase(HttpStatus.OK_200, responseBody);
    } finally {
      logger.methodEnd();
    }
  }

  private String makeUpdateL2VlanIfData(OperationUpdateL2VlanIfOptionEcEntity l2VlanIfOptionEcEntity) {
    try {
      logger.methodStart(new String[] { "l2VlanIfOptionEcEntity" }, new Object[] { l2VlanIfOptionEcEntity });
      OperationRequestBody body = new OperationRequestBody();
      body.setAction(EcCommonOperationAction.UPDATE_L2VLAN_IF.getMessage());
      body.setUpdateL2vlanIfOption(l2VlanIfOptionEcEntity);
      return JsonUtil.toJson(body);
    } finally {
      logger.methodEnd();
    }
  }
}
