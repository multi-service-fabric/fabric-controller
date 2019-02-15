
package msf.fc.node.interfaces.breakoutifs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcNode;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcBreakoutIfDao;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.rest.ec.core.operation.data.OperationRequestBody;
import msf.fc.rest.ec.core.operation.data.entity.OperationDeleteBreakoutOptionIfEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationRegisterBreakoutOptionIfEcEntity;
import msf.mfcfc.common.constant.EcCommonOperationAction;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.PatchOperation;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.breakoutifs.data.BreakoutIfCreateAsyncResponseBody;
import msf.mfcfc.node.interfaces.breakoutifs.data.BreakoutIfCreateDeleteRequestBody;
import msf.mfcfc.node.interfaces.breakoutifs.data.BreakoutIfRequest;
import msf.mfcfc.node.interfaces.breakoutifs.data.entity.BreakoutIfValueEntity;
import msf.mfcfc.rest.common.EcControlStatusUtil;
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Class to implement the asynchronous processing in the breakout interface
 * registration/deletion.
 *
 * @author NTT
 *
 */
public class FcBreakoutInterfaceCreateDeleteRunner extends FcAbstractBreakoutInterfaceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcBreakoutInterfaceCreateDeleteRunner.class);

  private BreakoutIfRequest request;
  private List<BreakoutIfCreateDeleteRequestBody> requestBody;

  private ErrorCode ecResponseStatus = null;

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
  public FcBreakoutInterfaceCreateDeleteRunner(BreakoutIfRequest request,
      List<BreakoutIfCreateDeleteRequestBody> requestBody) {

    this.request = request;
    this.requestBody = requestBody;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      RestResponseBase responseBase = null;
      SessionWrapper sessionWrapper = new SessionWrapper();

      try {
        sessionWrapper.openSession();
        FcNodeDao fcNodeDao = new FcNodeDao();

        FcNode fcNode = getNode(sessionWrapper, fcNodeDao, request.getFabricTypeEnum(),
            Integer.valueOf(request.getNodeId()));

        FcBreakoutIfDao fcBreakoutIfDao = new FcBreakoutIfDao();

        PatchOperation patchOperation = PatchOperation.getEnumFromMessage(requestBody.get(0).getOp());
        if (patchOperation.equals(PatchOperation.ADD)) {
          if (!NodeType.getEnumFromCode(fcNode.getNodeType()).equals(NodeType.LEAF)) {

            throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "fabricType = " + request.getFabricType());
          }

          logger.performance("start get leaf resources lock.");
          sessionWrapper.beginTransaction();
          List<FcNode> nodes = new ArrayList<>();
          nodes.add(fcNode);
          FcDbManager.getInstance().getLeafsLock(nodes, sessionWrapper);
          logger.performance("end get leaf resources lock.");

          TreeMap<String, List<BreakoutIfCreateDeleteRequestBody>> physicalIfIdMap = createRequestMapForphysicalIfId();

          ArrayList<String> breakoutIfIds = checkBreakoutIfForCreate(sessionWrapper, fcNode, fcBreakoutIfDao,
              physicalIfIdMap);

          createBreakoutIfs(sessionWrapper, fcNode, breakoutIfIds, fcBreakoutIfDao);

          for (Entry<String, List<BreakoutIfCreateDeleteRequestBody>> entry : physicalIfIdMap.entrySet()) {
            sendBreakoutIfCreate(fcNode, breakoutIfIds, entry.getValue());
          }

          responseBase = responseBreakoutInterfaceCreateAsyncData(breakoutIfIds);

          sessionWrapper.commit();
        } else {
          List<FcNode> nodes = new ArrayList<>();
          switch (NodeType.getEnumFromCode(fcNode.getNodeType())) {
            case LEAF:

              logger.performance("start get leaf resources lock.");
              sessionWrapper.beginTransaction();
              nodes.add(fcNode);
              FcDbManager.getInstance().getLeafsLock(nodes, sessionWrapper);
              logger.performance("end get leaf resources lock.");
              break;

            case SPINE:

              logger.performance("start get spine resources lock.");
              sessionWrapper.beginTransaction();
              nodes.add(fcNode);
              FcDbManager.getInstance().getSpinesLock(nodes, sessionWrapper);
              logger.performance("end get spine resources lock.");
              break;

            default:

              throw new MsfException(ErrorCode.UNDEFINED_ERROR,
                  "NodeType = " + NodeType.getEnumFromCode(fcNode.getNodeType()).getSingularMessage());
          }

          ArrayList<String> breakoutIfIds = checkBreakoutIfForDelete(sessionWrapper, fcNode, fcBreakoutIfDao);

          sendBreakoutIfDelete(fcNode, breakoutIfIds);

          responseBase = responseBreakoutInterfaceDeleteAsyncData();

          sessionWrapper.commit(EcControlStatusUtil.getStatusFromFcErrorCode(ecResponseStatus));
        }
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      if (ecResponseStatus == ErrorCode.EC_CONTROL_ERROR_EM_CONTROL_COMPLETED) {

        throw new MsfException(ecResponseStatus, "Breakout Interface Delete Complete. EC Control Error.");
      }

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private FcNode getNode(SessionWrapper sessionWrapper, FcNodeDao fcNodeDao, NodeType nodeType, Integer nodeId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNodeDao", "nodeType", "nodeId" },
          new Object[] { fcNodeDao, nodeType, nodeId });
      FcNode fcNode = fcNodeDao.read(sessionWrapper, nodeType.getCode(), nodeId);
      if (fcNode == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, "target resource is not found. parameters = node");
      }
      return fcNode;
    } finally {
      logger.methodEnd();
    }
  }

  private TreeMap<String, List<BreakoutIfCreateDeleteRequestBody>> createRequestMapForphysicalIfId() {
    try {
      logger.methodStart();

      TreeMap<String, List<BreakoutIfCreateDeleteRequestBody>> physicalIfIdMap = new TreeMap<>();

      for (BreakoutIfCreateDeleteRequestBody breakoutIfCreateDeleteRequestBody : requestBody) {
        String physicalIfId = breakoutIfCreateDeleteRequestBody.getValue().getBaseIf().getPhysicalIfId();
        List<BreakoutIfCreateDeleteRequestBody> requestBodies = null;
        if (physicalIfIdMap.containsKey(physicalIfId)) {
          requestBodies = physicalIfIdMap.get(physicalIfId);
        } else {
          requestBodies = new ArrayList<>();
        }
        requestBodies.add(breakoutIfCreateDeleteRequestBody);
        physicalIfIdMap.put(physicalIfId, requestBodies);
      }
      return physicalIfIdMap;
    } finally {
      logger.methodEnd();
    }
  }

  private ArrayList<String> checkBreakoutIfForCreate(SessionWrapper sessionWrapper, FcNode fcNode,
      FcBreakoutIfDao fcBreakoutIfDao, TreeMap<String, List<BreakoutIfCreateDeleteRequestBody>> physicalIfIdMap)
      throws MsfException {

    try {
      logger.methodStart(new String[] { "fcNode", "fcBreakoutIfDao", "physicalIfIdMap" },
          new Object[] { fcNode, fcBreakoutIfDao, physicalIfIdMap });
      ArrayList<String> breakoutIfIds = new ArrayList<>();

      if (physicalIfIdMap.size() != 1) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "another physical IF is set.");
      }
      for (Entry<String, List<BreakoutIfCreateDeleteRequestBody>> entry : physicalIfIdMap.entrySet()) {

        checkPhisicalIf(sessionWrapper, fcNode, entry.getKey());

        breakoutIfIds = checkRequestParamerter(entry.getValue());

        checkBreakoutIfCreateFromDb(sessionWrapper, fcNode, breakoutIfIds, fcBreakoutIfDao);
      }
      return breakoutIfIds;
    } finally {
      logger.methodEnd();
    }
  }

  private ArrayList<String> checkRequestParamerter(List<BreakoutIfCreateDeleteRequestBody> breakoutIfCreateRequestBody)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "breakoutIfCreateRequestBody" }, new Object[] { breakoutIfCreateRequestBody });

      Integer divisionNumber = null;
      String breakoutIfSpeed = null;
      ArrayList<String> breakoutIfIds = new ArrayList<>();

      for (BreakoutIfCreateDeleteRequestBody breakoutIfCreateDeleteRequestBody : breakoutIfCreateRequestBody) {

        if (divisionNumber != null) {
          if (!divisionNumber.equals(breakoutIfCreateDeleteRequestBody.getValue().getDivisionNumber())) {

            throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "another divisionNumber is set.");
          }
        } else {
          divisionNumber = breakoutIfCreateDeleteRequestBody.getValue().getDivisionNumber();
        }

        if (breakoutIfSpeed != null) {
          if (!breakoutIfSpeed.equals(breakoutIfCreateDeleteRequestBody.getValue().getBreakoutIfSpeed())) {

            throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "another breakoutIfSpeed is set.");
          }
        } else {
          breakoutIfSpeed = breakoutIfCreateDeleteRequestBody.getValue().getBreakoutIfSpeed();
        }

        if (breakoutIfIds.contains(breakoutIfCreateDeleteRequestBody.getPath().substring(1))) {

          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "same breakoutIF ID has been specified.");
        }
        breakoutIfIds.add(breakoutIfCreateDeleteRequestBody.getPath().substring(1));
      }
      if (!divisionNumber.equals(breakoutIfCreateRequestBody.size())) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR,
            "breaoutif ID of the specified number is not specified.");
      }
      return breakoutIfIds;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkBreakoutIfCreateFromDb(SessionWrapper sessionWrapper, FcNode fcNode,
      ArrayList<String> breakoutIfIds, FcBreakoutIfDao fcBreakoutIfDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNode", "breakoutIfIds", "fcBreakoutIfDao" },
          new Object[] { fcNode, breakoutIfIds, fcBreakoutIfDao });
      for (String breakoutIfId : breakoutIfIds) {
        FcBreakoutIf fcBreakoutIf = fcBreakoutIfDao.read(sessionWrapper, fcNode.getNodeType(), fcNode.getNodeId(),
            breakoutIfId);
        if (fcBreakoutIf != null) {

          throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST,
              "target resouece already exist. target = breakoutIF");
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void createBreakoutIfs(SessionWrapper sessionWrapper, FcNode fcNode, ArrayList<String> breakoutIfIds,
      FcBreakoutIfDao fcBreakoutIfDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNode", "breakoutIfIds", "fcBreakoutIfDao" },
          new Object[] { fcNode, breakoutIfIds, fcBreakoutIfDao });
      for (String breakoutIfId : breakoutIfIds) {
        FcBreakoutIf fcBreakoutIf = new FcBreakoutIf();
        fcBreakoutIf.setNode(fcNode);
        fcBreakoutIf.setBreakoutIfId(breakoutIfId);
        fcBreakoutIfDao.create(sessionWrapper, fcBreakoutIf);
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase sendBreakoutIfCreate(FcNode fcNode, ArrayList<String> breakoutIfIds,
      List<BreakoutIfCreateDeleteRequestBody> breakoutIfCreateRequestBody) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNode", "breakoutIfIds", "breakoutIfCreateRequestBody" },
          new Object[] { fcNode, breakoutIfIds, breakoutIfCreateRequestBody });

      OperationRequestBody operationRequestBody = new OperationRequestBody();
      operationRequestBody.setAction(EcCommonOperationAction.REGISTER_BREAKOUT_IF.getMessage());
      OperationRegisterBreakoutOptionIfEcEntity breakoutIfOption = new OperationRegisterBreakoutOptionIfEcEntity();
      breakoutIfOption.setNodeId(String.valueOf(fcNode.getEcNodeId()));

      BreakoutIfValueEntity breakoutIfValueEntity = breakoutIfCreateRequestBody.get(0).getValue();
      breakoutIfOption.setBasePhysicalIfId(breakoutIfValueEntity.getBaseIf().getPhysicalIfId());
      breakoutIfOption.setSpeed(breakoutIfValueEntity.getBreakoutIfSpeed());
      breakoutIfOption.setBreakoutIfIdList(breakoutIfIds);
      operationRequestBody.setRegisterBreakoutIfOption(breakoutIfOption);

      RestRequestBase restRequest = new RestRequestBase();

      restRequest.setRequestBody(JsonUtil.toJson(operationRequestBody));

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.OPERATION_REQUEST.getHttpMethod(),
          EcRequestUri.OPERATION_REQUEST.getUri(), restRequest, ecControlIpAddress, ecControlPort);

      String errorCode = null;
      if (StringUtils.isNotEmpty(restResponseBase.getResponseBody())) {
        ErrorInternalResponseBody body = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            ErrorInternalResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        errorCode = body.getErrorCode();
      }

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.CREATED_201, errorCode,
          ErrorCode.EC_CONTROL_ERROR);

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseBreakoutInterfaceCreateAsyncData(ArrayList<String> breakoutIfIds) {
    try {
      logger.methodStart(new String[] { "breakoutIfIds" }, new Object[] { breakoutIfIds });
      BreakoutIfCreateAsyncResponseBody body = new BreakoutIfCreateAsyncResponseBody();
      body.setBreakoutIfIdList(breakoutIfIds);
      return createRestResponse(body, HttpStatus.CREATED_201);
    } finally {
      logger.methodEnd();
    }
  }

  private ArrayList<String> checkBreakoutIfForDelete(SessionWrapper sessionWrapper, FcNode fcNode,
      FcBreakoutIfDao fcBreakoutIfDao) throws MsfException {

    try {
      logger.methodStart(new String[] { "fcNode", "fcBreakoutIfDao" }, new Object[] { fcNode, fcBreakoutIfDao });
      ArrayList<String> breakoutIfIds = new ArrayList<>();
      for (BreakoutIfCreateDeleteRequestBody breakoutIfCreateDeleteRequestBody : requestBody) {

        if (breakoutIfIds.contains(breakoutIfCreateDeleteRequestBody.getPath().substring(1))) {

          throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "same breakoutIF ID has been specified.");
        }
        breakoutIfIds.add(breakoutIfCreateDeleteRequestBody.getPath().substring(1));
      }
      for (String breakoutIfId : breakoutIfIds) {

        FcBreakoutIf fcBreakoutIf = checkBreakoutIfDeleteFromDb(sessionWrapper, fcNode, breakoutIfId);
        deleteBreakoutIf(sessionWrapper, fcBreakoutIf, fcBreakoutIfDao);
      }
      return breakoutIfIds;
    } finally {
      logger.methodEnd();
    }
  }

  private FcBreakoutIf checkBreakoutIfDeleteFromDb(SessionWrapper sessionWrapper, FcNode fcNode, String breakoutIfId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNode", "breakoutIfId" }, new Object[] { fcNode, breakoutIfId });
      FcBreakoutIfDao fcBreakoutIfDao = new FcBreakoutIfDao();
      FcBreakoutIf fcBreakoutIf = fcBreakoutIfDao.read(sessionWrapper, fcNode.getNodeType(), fcNode.getNodeId(),
          breakoutIfId);
      if (fcBreakoutIf == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource is not found. parameters = breakoutIf");
      }

      if ((CollectionUtils.isNotEmpty(fcBreakoutIf.getClusterLinkIfs()))
          || (CollectionUtils.isNotEmpty(fcBreakoutIf.getInternalLinkIfs()))
          || (CollectionUtils.isNotEmpty(fcBreakoutIf.getEdgePoints()))) {

        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "Specified breakout If is used for other.");
      }
      return fcBreakoutIf;
    } finally {
      logger.methodEnd();
    }
  }

  private void deleteBreakoutIf(SessionWrapper sessionWrapper, FcBreakoutIf fcBreakoutIf,
      FcBreakoutIfDao fcBreakoutIfDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcBreakoutIf", "fcBreakoutIfDao" },
          new Object[] { fcBreakoutIf, fcBreakoutIfDao });
      fcBreakoutIfDao.delete(sessionWrapper, fcBreakoutIf.getBreakoutIfInfoId());
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase sendBreakoutIfDelete(FcNode fcNode, ArrayList<String> breakoutIfIds) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNode", "breakoutIfIds" }, new Object[] { fcNode, breakoutIfIds });

      OperationRequestBody operationRequestBody = new OperationRequestBody();
      operationRequestBody.setAction(EcCommonOperationAction.DELETE_BREAKOUT_IF.getMessage());
      OperationDeleteBreakoutOptionIfEcEntity breakoutIfOption = new OperationDeleteBreakoutOptionIfEcEntity();
      breakoutIfOption.setNodeId(String.valueOf(fcNode.getEcNodeId()));
      breakoutIfOption.setBreakoutIfIdList(breakoutIfIds);
      operationRequestBody.setDeleteBreakoutIfOption(breakoutIfOption);

      RestRequestBase restRequest = new RestRequestBase();

      restRequest.setRequestBody(JsonUtil.toJson(operationRequestBody));

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.OPERATION_REQUEST.getHttpMethod(),
          EcRequestUri.OPERATION_REQUEST.getUri(), restRequest, ecControlIpAddress, ecControlPort);

      if (restResponseBase.getHttpStatusCode() != HttpStatus.NO_CONTENT_204) {
        ErrorInternalResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            ErrorInternalResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

        ecResponseStatus = EcControlStatusUtil.checkEcEmControlErrorCode(responseBody.getErrorCode());
        if (ecResponseStatus == ErrorCode.EC_CONTROL_ERROR) {

          String errorMsg = MessageFormat.format("HttpStatusCode = {0}, ErrorCode = {1}",
              restResponseBase.getHttpStatusCode(), responseBody.getErrorCode());
          logger.error(errorMsg);
          throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);
        }
      }
      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseBreakoutInterfaceDeleteAsyncData() {
    try {
      logger.methodStart();
      if (ecResponseStatus != null) {

        return null;
      }

      return new RestResponseBase(HttpStatus.NO_CONTENT_204, (String) null);
    } finally {
      logger.methodEnd();
    }
  }

}
