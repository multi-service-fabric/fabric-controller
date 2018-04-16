
package msf.fc.node.interfaces.lagifs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcLagIfId;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcBreakoutIfDao;
import msf.fc.db.dao.clusters.FcLagIfDao;
import msf.fc.db.dao.clusters.FcLagIfIdDao;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.clusters.FcPhysicalIfDao;
import msf.fc.node.FcNodeManager;
import msf.fc.rest.ec.node.interfaces.lag.data.LagIfCreateEcRequestBody;
import msf.fc.rest.ec.node.interfaces.lag.data.entity.LagIfCreateEcEntity;
import msf.fc.rest.ec.node.interfaces.lag.data.entity.LagIfPhysicalIfCreateEcEntity;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.lagifs.data.LagIfCreateAsyncResponseBody;
import msf.mfcfc.node.interfaces.lagifs.data.LagIfCreateRequestBody;
import msf.mfcfc.node.interfaces.lagifs.data.LagIfRequest;
import msf.mfcfc.rest.common.EcControlStatusUtil;
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Class to implement the asynchronous processing in Lag interface addition.
 *
 * @author NTT
 *
 */
public class FcLagInterfaceCreateRunner extends FcAbstractLagInterfaceRunnerBase {

  private LagIfRequest request;
  private LagIfCreateRequestBody requestBody;

  private ErrorCode ecResponseStatus = null;

  private static final MsfLogger logger = MsfLogger.getInstance(FcLagInterfaceCreateRunner.class);

  /**
   * Constructor.
   * <p>
   * Set the request information from scenario as argument
   * </p>
   *
   * @param request
   *          Request
   * @param requestBody
   *          Request body
   */
  public FcLagInterfaceCreateRunner(LagIfRequest request, LagIfCreateRequestBody requestBody) {

    this.request = request;
    this.requestBody = requestBody;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      RestResponseBase responseBase = null;
      SessionWrapper sessionWrapper = new SessionWrapper();

      Integer lagInterfaceId = null;
      FcNode fcNode;
      FcLagIfDao fcLagIfDao = new FcLagIfDao();
      FcNodeDao fcNodeDao = new FcNodeDao();

      try {
        sessionWrapper.openSession();

        fcNode = getNode(sessionWrapper, fcNodeDao, request.getFabricTypeEnum(), Integer.valueOf(request.getNodeId()));

        sessionWrapper.beginTransaction();

        List<FcLagIf> fcLagIfs = fcLagIfDao.readList(sessionWrapper);
        Set<Integer> lagIfIdSet = createLagIfIdSet(fcLagIfs);
        lagInterfaceId = getNextLagInterfaceId(sessionWrapper, lagIfIdSet);

        sessionWrapper.commit();
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      sessionWrapper = new SessionWrapper();

      try {
        sessionWrapper.openSession();

        logger.performance("start get leaf resources lock.");
        sessionWrapper.beginTransaction();
        List<FcNode> fcNodes = new ArrayList<>();

        fcNode = getNode(sessionWrapper, fcNodeDao, request.getFabricTypeEnum(), Integer.valueOf(request.getNodeId()));
        fcNodes.add(fcNode);
        FcDbManager.getInstance().getLeafsLock(fcNodes, sessionWrapper);
        logger.performance("end get leaf resources lock.");

        FcPhysicalIfDao fcPhysicalIfDao = new FcPhysicalIfDao();
        List<FcPhysicalIf> fcPhysicalIfs = checkForPhysicalInterface(sessionWrapper, fcPhysicalIfDao,
            requestBody.getPhysicalIfIdList());

        checkPhysicalIfForLagIfRelation(fcPhysicalIfs, sessionWrapper);

        FcBreakoutIfDao fcBreakoutIfDao = new FcBreakoutIfDao();
        List<FcBreakoutIf> fcBreakoutIfs = checkForBreakoutInterface(sessionWrapper, fcBreakoutIfDao,
            requestBody.getBreakoutIfIdList());

        checkBreakoutIfForLagIfRelation(fcBreakoutIfs, sessionWrapper);

        FcLagIf fcLagIf = createLagConstructionWithLagIf(sessionWrapper, fcLagIfDao, lagInterfaceId, fcNode);

        sendLagInterfaceCreate(fcLagIf, requestBody.getPhysicalIfIdList(), requestBody.getBreakoutIfIdList(), fcNode);

        responseBase = responseLagInterfaceCreateAsyncData(fcLagIf.getLagIfId());

        sessionWrapper.commit(EcControlStatusUtil.getStatusFromFcErrorCode(ecResponseStatus));
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      if (ecResponseStatus == ErrorCode.EC_CONTROL_ERROR_EM_CONTROL_COMPLETED) {

        throw new MsfException(ecResponseStatus, "Lag Interface Create Complete. EC Control Error.");
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

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, "target resource not found. parameters = node");
      }
      return fcNode;
    } finally {
      logger.methodEnd();
    }
  }

  private List<FcPhysicalIf> checkForPhysicalInterface(SessionWrapper sessionWrapper, FcPhysicalIfDao fcPhysicalIfDao,
      List<String> physicalIfIdList) throws MsfException {
    try {
      logger.methodStart();
      List<FcPhysicalIf> fcPhysicalIfs = new ArrayList<FcPhysicalIf>();

      for (String physicalIfId : physicalIfIdList) {

        FcPhysicalIf fcPhysicalIf = fcPhysicalIfDao.read(sessionWrapper, request.getFabricTypeEnum().getCode(),
            Integer.valueOf(request.getNodeId()), physicalIfId);
        if (fcPhysicalIf == null) {

          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
              "target resource not found. parameters = physicalIf");
        }
        fcPhysicalIfs.add(fcPhysicalIf);
      }

      return fcPhysicalIfs;
    } finally {
      logger.methodEnd();
    }
  }

  private List<FcBreakoutIf> checkForBreakoutInterface(SessionWrapper sessionWrapper, FcBreakoutIfDao fcBreakoutIfDao,
      List<String> breakoutIfIdList) throws MsfException {
    try {
      logger.methodStart();
      List<FcBreakoutIf> fcBreakoutIfs = new ArrayList<FcBreakoutIf>();

      for (String breakoutIfId : breakoutIfIdList) {
        FcBreakoutIf fcBreakoutIf = fcBreakoutIfDao.read(sessionWrapper, request.getFabricTypeEnum().getCode(),
            Integer.valueOf(request.getNodeId()), breakoutIfId);
        if (fcBreakoutIf == null) {

          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
              "target resource not found. parameters = breakoutIf");
        }
        fcBreakoutIfs.add(fcBreakoutIf);
      }
      return fcBreakoutIfs;
    } finally {
      logger.methodEnd();
    }
  }

  private Set<Integer> createLagIfIdSet(List<FcLagIf> fcLagIfs) {
    try {
      logger.methodStart(new String[] { "fcLagIfs" }, new Object[] { fcLagIfs });
      Set<Integer> lagIfIdSet = new HashSet<>();
      for (FcLagIf fcLagIf : fcLagIfs) {
        lagIfIdSet.add(fcLagIf.getLagIfId());
      }
      return lagIfIdSet;
    } finally {
      logger.methodEnd();
    }
  }

  private Integer getNextLagInterfaceId(SessionWrapper sessionWrapper, Set<Integer> lagIfIdSet) throws MsfException {
    try {
      logger.methodStart();
      FcLagIfIdDao fcLagIfIdDao = new FcLagIfIdDao();
      List<FcLagIfId> lagIfIdList = fcLagIfIdDao.readList(sessionWrapper);

      int firstNextId = lagIfIdList.get(0).getNextId();

      int targetNextId = firstNextId;
      logger.performance("start get available lag interface id.");
      do {
        if (lagIfIdSet.contains(targetNextId)) {

          targetNextId++;

          if (!checkLagInterfaceIdRange(targetNextId)) {

            targetNextId = FcNodeManager.FC_LAG_IF_START_ID;
          }
        } else {

          updateLagInterfaceId(sessionWrapper, fcLagIfIdDao, targetNextId + 1, firstNextId);
          logger.performance("end get available lag interface id.");
          return targetNextId;
        }

      } while (targetNextId != firstNextId);
      logger.performance("end get available cp id.");

      String logMsg = MessageFormat.format("threre is no available lag interface id. firstCheckId = {0}", firstNextId);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, logMsg);

    } finally {
      logger.methodEnd();
    }
  }

  private void updateLagInterfaceId(SessionWrapper sessionWrapper, FcLagIfIdDao fcLagIfIdDao, int nextId,
      int firstNextId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "fcLagIfIdDao", "nextId" },
          new Object[] { sessionWrapper, fcLagIfIdDao, nextId });

      if (!checkLagInterfaceIdRange(nextId)) {

        nextId = FcNodeManager.FC_LAG_IF_START_ID;
      }

      FcLagIfId fcLagIfId = new FcLagIfId();
      fcLagIfId.setNextId(nextId);
      fcLagIfIdDao.create(sessionWrapper, fcLagIfId);
      fcLagIfIdDao.delete(sessionWrapper, firstNextId);
    } finally {
      logger.methodEnd();
    }

  }

  private boolean checkLagInterfaceIdRange(int checkTargetId) {
    try {
      logger.methodStart(new String[] { "checkTargetId" }, new Object[] { checkTargetId });
      if (checkTargetId >= 0 && checkTargetId < Integer.MAX_VALUE) {
        return true;
      } else {
        return false;
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkPhysicalIfForLagIfRelation(List<FcPhysicalIf> physicalIfs, SessionWrapper sessionWrapper)
      throws MsfException {
    try {
      logger.methodStart();

      for (FcPhysicalIf physicalIf : physicalIfs) {

        if (CollectionUtils.isNotEmpty(physicalIf.getEdgePoints())) {
          throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "Registered to the edge point.");
        }

        if (CollectionUtils.isNotEmpty(physicalIf.getInternalLinkIfs())) {
          throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "Registered to the internal link interface.");
        }

        if (CollectionUtils.isNotEmpty(physicalIf.getClusterLinkIfs())) {
          throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "Registered to the cluster link interface.");
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void checkBreakoutIfForLagIfRelation(List<FcBreakoutIf> fcBreakoutIfs, SessionWrapper sessionWrapper)
      throws MsfException {
    try {
      logger.methodStart();

      for (FcBreakoutIf fcBreakoutIf : fcBreakoutIfs) {

        if (CollectionUtils.isNotEmpty(fcBreakoutIf.getEdgePoints())) {
          throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "Registered to the edge point.");
        }

        if (CollectionUtils.isNotEmpty(fcBreakoutIf.getInternalLinkIfs())) {
          throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "Registered to the internal link interface.");
        }

        if (CollectionUtils.isNotEmpty(fcBreakoutIf.getClusterLinkIfs())) {
          throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "Registered to the cluster link interface.");
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private FcLagIf createLagConstructionWithLagIf(SessionWrapper sessionWrapper, FcLagIfDao fcLagIfDao,
      Integer lagInterfaceId, FcNode fcNode) throws MsfException {
    try {
      logger.methodStart();
      FcLagIf fcLagIf = new FcLagIf();
      fcLagIf.setLagIfId(lagInterfaceId);
      fcLagIf.setNode(fcNode);
      fcLagIfDao.create(sessionWrapper, fcLagIf);
      return fcLagIf;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase sendLagInterfaceCreate(FcLagIf fcLagIf, List<String> physicalIfIdList,
      List<String> breakoutIfIdList, FcNode fcNode) throws MsfException {
    try {
      logger.methodStart();

      LagIfCreateEcEntity lagIfCreateEcEntity = new LagIfCreateEcEntity();
      lagIfCreateEcEntity.setLagIfId(String.valueOf(fcLagIf.getLagIfId()));

      List<LagIfPhysicalIfCreateEcEntity> physicalIfList = new ArrayList<>();

      if (CollectionUtils.isNotEmpty(physicalIfIdList)) {
        for (String physicalIfId : physicalIfIdList) {
          LagIfPhysicalIfCreateEcEntity physicalIfCreateEcEntity = new LagIfPhysicalIfCreateEcEntity();
          physicalIfCreateEcEntity.setIfId(physicalIfId);
          physicalIfCreateEcEntity.setIfType(InterfaceType.PHYSICAL_IF.getMessage());
          physicalIfList.add(physicalIfCreateEcEntity);
        }
      }

      if (CollectionUtils.isNotEmpty(breakoutIfIdList)) {
        for (String breakoutIfId : breakoutIfIdList) {
          LagIfPhysicalIfCreateEcEntity breakoutIfCreateEcEntity = new LagIfPhysicalIfCreateEcEntity();
          breakoutIfCreateEcEntity.setIfId(breakoutIfId);
          breakoutIfCreateEcEntity.setIfType(InterfaceType.BREAKOUT_IF.getMessage());
          physicalIfList.add(breakoutIfCreateEcEntity);
        }
      }
      lagIfCreateEcEntity.setPhysicalIfList(physicalIfList);

      LagIfCreateEcRequestBody ecRequestBody = new LagIfCreateEcRequestBody();
      ecRequestBody.setLagIf(lagIfCreateEcEntity);
      RestRequestBase restRequest = new RestRequestBase();

      restRequest.setRequestBody(JsonUtil.toJson(ecRequestBody));

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.LAG_IF_CREATE.getHttpMethod(),
          EcRequestUri.LAG_IF_CREATE.getUri(String.valueOf(fcNode.getEcNodeId())), restRequest, ecControlIpAddress,
          ecControlPort);

      if (restResponseBase.getHttpStatusCode() != HttpStatus.CREATED_201) {
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

  private RestResponseBase responseLagInterfaceCreateAsyncData(Integer lagIfId) {
    try {
      logger.methodStart(new String[] { "lagIfId" }, new Object[] { lagIfId });
      if (ecResponseStatus != null) {

        return null;
      }
      LagIfCreateAsyncResponseBody body = new LagIfCreateAsyncResponseBody();
      body.setLagIfId(String.valueOf(lagIfId));
      return createRestResponse(body, HttpStatus.CREATED_201);
    } finally {
      logger.methodEnd();
    }
  }
}
