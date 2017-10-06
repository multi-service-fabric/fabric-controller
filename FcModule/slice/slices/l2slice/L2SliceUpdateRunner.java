package msf.fc.slice.slices.l2slice;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ActiveStatus;
import msf.fc.common.constant.EcCommonOperationAction;
import msf.fc.common.constant.EcRequestUri;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.InterfaceOperationStatus;
import msf.fc.common.constant.NodeType;
import msf.fc.common.constant.ReserveStatus;
import msf.fc.common.data.L2Cp;
import msf.fc.common.data.L2Slice;
import msf.fc.common.data.Node;
import msf.fc.common.data.SliceManagerBaseInfo;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.IpAddressUtil;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.DbManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.db.dao.slices.L2CpDao;
import msf.fc.db.dao.slices.L2SliceDao;
import msf.fc.db.dao.slices.SliceManagerBaseInfoDao;
import msf.fc.rest.common.JsonUtil;
import msf.fc.rest.ec.core.operation.data.OperationRequestBody;
import msf.fc.rest.ec.core.operation.data.entity.CreateL2CpEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.CreateL2CpsOptionEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.DeleteL2CpEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.DeleteL2CpsOptionEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.L2BaseIfEcEntity;
import msf.fc.slice.AbstractSliceCpRunnerBase;
import msf.fc.slice.slices.l2slice.data.L2SliceRequest;
import msf.fc.slice.slices.l2slice.data.L2SliceUpdateAsyncResponseBody;
import msf.fc.slice.slices.l2slice.data.L2SliceUpdateRequestBody;

public class L2SliceUpdateRunner extends AbstractSliceCpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(L2SliceUpdateRunner.class);
  private L2SliceRequest request;
  private L2SliceUpdateRequestBody requestBody;

  public L2SliceUpdateRunner(L2SliceRequest request, L2SliceUpdateRequestBody requestBody) {
    try {
      logger.methodStart(new String[] { "request", "requestBody" }, new Object[] { request, requestBody });
      this.request = request;
      this.requestBody = requestBody;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      sessionWrapper.openSession();
      L2SliceDao l2SliceDao = new L2SliceDao();
      L2Slice l2Slice = l2SliceDao.read(sessionWrapper, request.getSliceId());
      checkL2SlicePresence(l2Slice);
      sessionWrapper.beginTransaction();
      List<L2Slice> l2SliceList = new ArrayList<>();
      l2SliceList.add(l2Slice);
      logger.performance("start get l2slice resources lock.");
      DbManager.getInstance().getL2SlicesLock(l2SliceList, sessionWrapper);
      logger.performance("end get l2slice resources lock.");
      L2Slice l2SliceAfterLock = l2SliceDao.read(sessionWrapper, request.getSliceId());
      checkL2SlicePresence(l2SliceAfterLock);
      checkL2SliceStatus(l2SliceAfterLock);
      L2CpDao l2CpDao = new L2CpDao();
      List<L2Cp> l2CpListAfterLock = l2CpDao.readList(sessionWrapper, request.getSliceId(),
          getReserveStatusByAction().getCode());
      List<String> udpateCpIdList = getUpdateTargetIdList(l2CpListAfterLock);
      if (l2CpListAfterLock.size() > 0) {
        updateL2Cps(sessionWrapper, l2CpDao, l2CpListAfterLock);
        String requestJson = makeEcSendRequestBody(sessionWrapper, l2CpListAfterLock, l2SliceAfterLock);
        RestResponseBase restResponse = sendRequestToEc(requestJson, EcRequestUri.OPERATION_REQUEST, (String[]) null);
        commitTransaction(sessionWrapper, restResponse, true);
      } else {
        sessionWrapper.rollback();
      }
      return createResponse(udpateCpIdList);
    } catch (MsfException exp) {
      logger.error(exp.getMessage(), exp);
      sessionWrapper.rollback();
      throw exp;
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  private ReserveStatus getReserveStatusByAction() throws MsfException {
    try {
      logger.methodStart();
      switch (requestBody.getActionEnum()) {
        case ACTIVATE_CPS:
          return ReserveStatus.ACTIVATE_RESERVE;
        case DEACTIVATE_CPS:
          return ReserveStatus.DEACTIVATE_RESERVE;
        default:
          String logMsg = MessageFormat.format("action is undefined.action = {0}", requestBody.getAction());
          logger.error(logMsg);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private List<String> getUpdateTargetIdList(List<L2Cp> l2CpList) {
    try {
      logger.methodStart(new String[] { "l2CpList" }, new Object[] { l2CpList });
      List<String> cpIdList = new ArrayList<>();
      for (L2Cp l2Cp : l2CpList) {
        cpIdList.add(l2Cp.getId().getCpId());
      }
      return cpIdList;
    } finally {
      logger.methodEnd();
    }
  }

  private void updateL2Cps(SessionWrapper sessionWrapper, L2CpDao l2CpDao, List<L2Cp> l2CpList) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l2CpDao", "l2CpList" },
          new Object[] { sessionWrapper, l2CpDao, l2CpList });
      for (L2Cp l2Cp : l2CpList) {
        l2Cp.setReservationStatusEnum(ReserveStatus.NONE);
        l2Cp.setOperationStatusEnum(InterfaceOperationStatus.UNKNOWN);
        switch (requestBody.getActionEnum()) {
          case ACTIVATE_CPS:
            l2Cp.setStatusEnum(ActiveStatus.ACTIVE);
            break;
          case DEACTIVATE_CPS:
            l2Cp.setStatusEnum(ActiveStatus.INACTIVE);
            break;
          default:
            String logMsg = MessageFormat.format("action is undefined.action = {0}", requestBody.getAction());
            logger.error(logMsg);
            throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
        }
        l2CpDao.update(sessionWrapper, l2Cp);
      }

    } finally {
      logger.methodEnd();
    }
  }

  private String makeEcSendRequestBody(SessionWrapper sessionWrapper, List<L2Cp> l2CpList, L2Slice l2Slice)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l2CpList", "l2Slice" },
          new Object[] { sessionWrapper, l2CpList, l2Slice });
      NodeDao nodeDao = new NodeDao();
      OperationRequestBody ecRequestBody = new OperationRequestBody();
      switch (requestBody.getActionEnum()) {
        case ACTIVATE_CPS:
          List<CreateL2CpEcEntity> createL2CpEntityList = new ArrayList<>();
          for (L2Cp l2Cp : l2CpList) {
            String swClusterId = l2Cp.getEdgePoint().getId().getSwClusterId();
            int edgePointId = l2Cp.getEdgePoint().getId().getEdgePointId();
            Node node = nodeDao.read(sessionWrapper, swClusterId, edgePointId);
            checkNodePresence(node, swClusterId, edgePointId);

            createL2CpEntityList.add(makeCreateL2CpEcEntity(sessionWrapper, l2Cp, node));
          }

          String vxlanMulticastAddress = calculateVxlanMulticastAddress(sessionWrapper, l2Slice.getVrfId());

          CreateL2CpsOptionEcEntity createL2CpsOptionEcEntity = new CreateL2CpsOptionEcEntity();
          createL2CpsOptionEcEntity.setIpv4MulticastAddress(vxlanMulticastAddress);
          createL2CpsOptionEcEntity.setL2CpList(createL2CpEntityList);
          createL2CpsOptionEcEntity.setSliceId(l2Slice.getSliceId());
          createL2CpsOptionEcEntity.setVrfId(l2Slice.getVrfId());
          ecRequestBody.setActionEnum(EcCommonOperationAction.CREATE_L2CPS);
          ecRequestBody.setCreateL2cpsOption(createL2CpsOptionEcEntity);
          break;
        default:
          List<DeleteL2CpEcEntity> deleteL2CpEcEntityList = new ArrayList<>();
          for (L2Cp l2Cp : l2CpList) {
            DeleteL2CpEcEntity deleteL2CpEcEntity = new DeleteL2CpEcEntity();
            deleteL2CpEcEntity.setCpId(l2Cp.getId().getCpId());
            deleteL2CpEcEntityList.add(deleteL2CpEcEntity);
          }
          DeleteL2CpsOptionEcEntity deleteL2CpsOptionEcEntity = new DeleteL2CpsOptionEcEntity();
          deleteL2CpsOptionEcEntity.setSliceId(l2Slice.getSliceId());
          deleteL2CpsOptionEcEntity.setCpList(deleteL2CpEcEntityList);
          ecRequestBody.setActionEnum(EcCommonOperationAction.DELETE_L2CPS);
          ecRequestBody.setDeleteL2cpsOption(deleteL2CpsOptionEcEntity);
          break;
      }
      String requestJson = JsonUtil.toJson(ecRequestBody);
      return requestJson;

    } finally {
      logger.methodEnd();
    }
  }

  private void checkL2SlicePresence(L2Slice l2Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice" }, new Object[] { l2Slice });
      ParameterCheckUtil.checkNotNullTargetResource(l2Slice, new String[] { "sliceId" },
          new Object[] { request.getSliceId() });
    } finally {
      logger.methodEnd();
    }
  }

  private String calculateVxlanMulticastAddress(SessionWrapper sessionWrapper, int vrfId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "vrfId" }, new Object[] { sessionWrapper, vrfId });
      SliceManagerBaseInfoDao smbiDao = new SliceManagerBaseInfoDao();
      SliceManagerBaseInfo smbi = smbiDao.read(sessionWrapper, 1);
      int ipAddressInt = IpAddressUtil.convertIpAddressToIntFromStr(smbi.getL2vpnMulticastAddressBase());
      int newIpAddressInt = ipAddressInt + vrfId;
      String newIpAddressStr = IpAddressUtil.convertIpAddressToStrFromInt(newIpAddressInt);
      logger.debug("newIpAddress = {0}", newIpAddressStr);
      return newIpAddressStr;
    } catch (MsfException exp) {
      String logMsg = MessageFormat.format("failed to calculate vxlan multicast address. vrfId = {0}", vrfId);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.UPDATE_INFORMATION_ERROR, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  private CreateL2CpEcEntity makeCreateL2CpEcEntity(SessionWrapper sessionWrapper, L2Cp l2Cp, Node node)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l2Cp", "node" },
          new Object[] { sessionWrapper, l2Cp, node });
      L2BaseIfEcEntity l2BaseIfEcEntity = new L2BaseIfEcEntity();
      l2BaseIfEcEntity.setNodeTypeEnum(NodeType.LEAF);
      l2BaseIfEcEntity.setNodeId(String.valueOf(node.getNodeId()));
      l2BaseIfEcEntity.setTypeEnum(getIfType(l2Cp.getEdgePoint()));
      l2BaseIfEcEntity.setIfId(getIfId(sessionWrapper, l2Cp.getEdgePoint()));
      CreateL2CpEcEntity createL2CpEcEntity = new CreateL2CpEcEntity();
      createL2CpEcEntity.setBaseIf(l2BaseIfEcEntity);
      createL2CpEcEntity.setCpId(l2Cp.getId().getCpId());
      createL2CpEcEntity.setPortModeEnum(l2Cp.getPortModeEnum());
      createL2CpEcEntity.setVlanId(String.valueOf(l2Cp.getVlanId()));
      return createL2CpEcEntity;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkL2SliceStatus(L2Slice l2Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice" }, new Object[] { l2Slice });
      boolean isActiveStatusOk = false;
      boolean isReserveStatusOk = false;

      switch (requestBody.getActionEnum()) {
        case DEACTIVATE_CPS:
          isActiveStatusOk = ActiveStatus.ACTIVE.equals(l2Slice.getStatusEnum());
          isReserveStatusOk = ReserveStatus.NONE.equals(l2Slice.getReservationStatusEnum());
          break;
        default:
          String logMsg = MessageFormat.format("action = {0}", requestBody.getAction());
          logger.error(logMsg);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
      }

      if (!isActiveStatusOk || !isReserveStatusOk) {
        String logMsg = MessageFormat.format("slice status invalid. activeStatus = {0}, reserveStatus = {1}",
            l2Slice.getStatusEnum().name(), l2Slice.getReservationStatusEnum().name());
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(List<String> updatedCpIdList) {
    try {
      logger.methodStart(new String[] { "updatedCpIdList" }, new Object[] { updatedCpIdList });
      L2SliceUpdateAsyncResponseBody responseBody = new L2SliceUpdateAsyncResponseBody();
      responseBody.setUpdatedCpList(updatedCpIdList);
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, responseBody);

      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }
}
