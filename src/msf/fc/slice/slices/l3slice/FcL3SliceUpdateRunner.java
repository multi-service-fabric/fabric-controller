
package msf.fc.slice.slices.l3slice;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcL3Cp;
import msf.fc.common.data.FcL3Slice;
import msf.fc.common.data.FcNode;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.slices.FcL3SliceDao;
import msf.fc.rest.ec.core.operation.data.OperationRequestBody;
import msf.fc.rest.ec.core.operation.data.entity.OperationUpdateL3VlanIfOptionEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationVlanIfEcEntity;
import msf.mfcfc.common.constant.EcCommonOperationAction;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceRequest;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceUpdateAsyncResponseBody;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceUpdateRequestBody;

/**
 * Implementation class for the asynchronous process in L3 the slice
 * modification process.
 *
 * @author NTT
 *
 */
public class FcL3SliceUpdateRunner extends FcAbstractL3SliceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL3SliceUpdateRunner.class);

  private L3SliceUpdateRequestBody requestBody;

  /**
   * Constructor. <br>
   * Take over the necessary information from scenario
   *
   * @param request
   *          Request for the L3 slice control
   * @param requestBody
   *          Request Body part for the L3 slice modification
   */
  public FcL3SliceUpdateRunner(L3SliceRequest request, L3SliceUpdateRequestBody requestBody) {
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
        FcL3SliceDao l3SliceDao = new FcL3SliceDao();
        FcL3Slice l3Slice = l3SliceDao.read(sessionWrapper, request.getSliceId());

        checkL3SlicePresence(l3Slice, request.getSliceId());
        List<FcL3Slice> l3SliceList = new ArrayList<>();
        l3SliceList.add(l3Slice);

        sessionWrapper.beginTransaction();
        logger.performance("start get l3slice resources lock.");

        FcDbManager.getInstance().getL3SlicesLock(l3SliceList, sessionWrapper);
        logger.performance("end get l3slice resources lock.");

        FcL3Slice l3SliceAfterLock = l3SliceDao.read(sessionWrapper, request.getSliceId());

        checkL3SlicePresence(l3SliceAfterLock, request.getSliceId());

        l3SliceAfterLock.setRemarkMenu(requestBody.getRemarkMenu());

        l3SliceDao.update(sessionWrapper, l3SliceAfterLock);

        if (l3SliceAfterLock.getL3Cps() == null || l3SliceAfterLock.getL3Cps().isEmpty()) {
          sessionWrapper.commit();
        } else {

          RestResponseBase restResponse = sendVlanIfUpdateRequest(sessionWrapper, l3SliceAfterLock);

          commitTransaction(sessionWrapper, restResponse, HttpStatus.OK_200, true);
        }

        return createResponse(l3SliceAfterLock);

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

  private RestResponseBase sendVlanIfUpdateRequest(SessionWrapper sessionWrapper, FcL3Slice fcL3Slice)
      throws MsfException {
    try {
      logger.methodStart();

      FcNodeDao fcNodeDao = new FcNodeDao();
      OperationUpdateL3VlanIfOptionEcEntity l3VlanIfOptionEcEntity = new OperationUpdateL3VlanIfOptionEcEntity();
      List<OperationVlanIfEcEntity> vlanIfEcEntityList = new ArrayList<>();
      for (FcL3Cp fcL3Cp : fcL3Slice.getL3Cps()) {
        OperationVlanIfEcEntity vlanIfEcEntity = new OperationVlanIfEcEntity();

        FcNode fcNode = fcNodeDao.read(sessionWrapper, fcL3Cp.getEdgePoint().getEdgePointId());
        vlanIfEcEntity.setVlanIfId(String.valueOf(fcL3Cp.getVlanIf().getId().getVlanIfId()));
        vlanIfEcEntity.setNodeId(String.valueOf(fcNode.getEcNodeId()));
        vlanIfEcEntityList.add(vlanIfEcEntity);
      }
      l3VlanIfOptionEcEntity.setVlanIfList(vlanIfEcEntityList);
      l3VlanIfOptionEcEntity.setVrfId(String.valueOf(fcL3Slice.getVrfId()));
      l3VlanIfOptionEcEntity.setRemarkMenu(fcL3Slice.getRemarkMenu());
      String requestJson = makeUpdateL3VlanIfData(l3VlanIfOptionEcEntity);

      RestResponseBase responseBase = sendRequestToEc(requestJson, EcRequestUri.OPERATION_REQUEST, (String[]) null);
      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(FcL3Slice fcL3Slice) throws MsfException {
    try {
      logger.methodStart();
      List<String> updatedCpIdList = new ArrayList<>();
      for (FcL3Cp fcL3Cp : fcL3Slice.getL3Cps()) {
        updatedCpIdList.add(fcL3Cp.getId().getCpId());
      }
      L3SliceUpdateAsyncResponseBody responseBody = new L3SliceUpdateAsyncResponseBody();
      responseBody.setUpdatedCpIdList(updatedCpIdList);
      return new RestResponseBase(HttpStatus.OK_200, responseBody);
    } finally {
      logger.methodEnd();
    }
  }

  private String makeUpdateL3VlanIfData(OperationUpdateL3VlanIfOptionEcEntity l3VlanIfOptionEcEntity) {
    try {
      logger.methodStart(new String[] { "l3VlanIfOptionEcEntity" }, new Object[] { l3VlanIfOptionEcEntity });
      OperationRequestBody body = new OperationRequestBody();
      body.setAction(EcCommonOperationAction.UPDATE_L3VLAN_IF.getMessage());
      body.setUpdateL3vlanIfOption(l3VlanIfOptionEcEntity);
      return JsonUtil.toJson(body);
    } finally {
      logger.methodEnd();
    }
  }
}
