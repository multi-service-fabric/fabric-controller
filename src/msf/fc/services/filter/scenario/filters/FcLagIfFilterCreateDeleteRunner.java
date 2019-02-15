
package msf.fc.services.filter.scenario.filters;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcLagIfFilterInfo;
import msf.fc.common.data.FcLagIfFilterInfoPK;
import msf.fc.common.data.FcNode;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcLagIfDao;
import msf.fc.db.dao.filters.FcLagIfFilterInfoDao;
import msf.fc.services.filter.rest.ec.filters.data.entity.FilterOperationTermEcEntity;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.PatchOperation;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.services.filter.common.constant.EcFilterIfType;
import msf.mfcfc.services.filter.common.constant.EcFilterUpdateOperation;
import msf.mfcfc.services.filter.scenario.filters.data.LagIfFilterCreateDeleteAsyncResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.LagIfFilterCreateDeleteRequestBody;
import msf.mfcfc.services.filter.scenario.filters.data.LagIfFilterRequest;

/**
 * Class to implement the asynchronous processing in the LagIF filter
 * information registration/deletion.
 *
 * @author NTT
 *
 */
public class FcLagIfFilterCreateDeleteRunner extends FcAbstractFilterRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcLagIfFilterCreateDeleteRunner.class);

  private LagIfFilterRequest request;
  private List<LagIfFilterCreateDeleteRequestBody> requestBody;

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
  public FcLagIfFilterCreateDeleteRunner(LagIfFilterRequest request,
      List<LagIfFilterCreateDeleteRequestBody> requestBody) {

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

        FcLagIf fcLagIf = getLagInterface(sessionWrapper, new FcLagIfDao(), request.getFabricTypeEnum().getCode(),
            Integer.parseInt(request.getNodeId()), Integer.parseInt(request.getLagIfId()));

        FcNode fcNode = fcLagIf.getNode();

        logger.performance("start get leaf resources lock.");
        sessionWrapper.beginTransaction();

        List<FcNode> fcTableLockLeafNodes = new ArrayList<>();
        fcTableLockLeafNodes.add(fcNode);
        FcDbManager.getInstance().getLeafsLock(fcTableLockLeafNodes, sessionWrapper);
        logger.performance("end get leaf resources lock.");

        List<FilterOperationTermEcEntity> filterOperationTermEcEntities = new ArrayList<>();

        PatchOperation patchOperation = requestBody.get(0).getOpEnum();
        if (patchOperation.equals(PatchOperation.ADD)) {

          List<String> termIds = getLagIfTermIds(true);

          FcLagIfFilterInfoDao fcLagIfFilterInfoDao = new FcLagIfFilterInfoDao();

          List<FcLagIfFilterInfo> fcLagIfFilterInfos = fcLagIfFilterInfoDao.readList(sessionWrapper,
              NodeType.LEAF.getCode(), Integer.parseInt(request.getNodeId()), Integer.parseInt(request.getLagIfId()));

          lagIfFilterCreateProcess(termIds, fcLagIfFilterInfoDao, fcLagIfFilterInfos, sessionWrapper,
              filterOperationTermEcEntities);

          sendFilterCreateDelete(fcNode.getEcNodeId(), EcFilterIfType.LAG_IFS.getMessage(),
              String.valueOf(fcLagIf.getLagIfId()), filterOperationTermEcEntities);

          responseBase = responseLagIfFilterCreateData(filterOperationTermEcEntities);

        } else {

          List<String> termIds = getLagIfTermIds(false);

          FcLagIfFilterInfoDao fcLagIfFilterInfoDao = new FcLagIfFilterInfoDao();

          List<FcLagIfFilterInfo> fcLagIfFilterInfos = fcLagIfFilterInfoDao.readList(sessionWrapper,
              NodeType.LEAF.getCode(), Integer.parseInt(request.getNodeId()), Integer.parseInt(request.getLagIfId()));

          lagIfFilterDeleteProcess(termIds, fcLagIfFilterInfoDao, fcLagIfFilterInfos, sessionWrapper,
              filterOperationTermEcEntities);

          sendFilterCreateDelete(fcNode.getEcNodeId(), EcFilterIfType.LAG_IFS.getMessage(),
              String.valueOf(fcLagIf.getLagIfId()), filterOperationTermEcEntities);

          responseBase = responseFilterDeleteData();

        }

        sessionWrapper.commit();
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private void lagIfFilterCreateProcess(List<String> termIds, FcLagIfFilterInfoDao fcLagIfFilterInfoDao,
      List<FcLagIfFilterInfo> fcLagIfFilterInfos, SessionWrapper sessionWrapper,
      List<FilterOperationTermEcEntity> filterOperationTermEcEntities) throws MsfException {
    try {
      logger.methodStart();

      TreeSet<String> alreadyGeneratedIds = new TreeSet<>();
      for (FcLagIfFilterInfo fcLagIfFilterInfo : fcLagIfFilterInfos) {

        alreadyGeneratedIds.add(fcLagIfFilterInfo.getId().getTermId());
      }

      int generateCandidateId = 1;

      for (int i = 0; i < requestBody.size(); i++) {

        FcLagIfFilterInfoPK createLagIfFilterPk = new FcLagIfFilterInfoPK();

        String termId = termIds.get(i);
        if (!termId.equals(AUTOMATIC_GENERATED_NUMBER)) {

          if (alreadyGeneratedIds.contains(termId)) {

            throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST,
                MessageFormat.format("target resouece already exist. termId = {0}", termId));
          }

          createLagIfFilterPk.setTermId(termId);

          alreadyGeneratedIds.add(termId);
        } else {

          int nextTermId = getNextTermId(alreadyGeneratedIds, generateCandidateId);
          String formatTermId = String.format("%06d", nextTermId);

          createLagIfFilterPk.setTermId(formatTermId);

          alreadyGeneratedIds.add(formatTermId);

          generateCandidateId = nextTermId + 1;
        }

        FcLagIfFilterInfo createLagIfFilter = new FcLagIfFilterInfo();
        createLagIfFilterPk.setLagIfId(Integer.parseInt(request.getLagIfId()));
        createLagIfFilter.setId(createLagIfFilterPk);

        fcLagIfFilterInfoDao.create(sessionWrapper, createLagIfFilter);

        LagIfFilterCreateDeleteRequestBody lagIfFilterCreateDeleteRequestBody = requestBody.get(i);

        filterOperationTermEcEntities.add(createFilterOperationTermEcEntity(EcFilterUpdateOperation.ADD.getMessage(),
            createLagIfFilterPk.getTermId(), lagIfFilterCreateDeleteRequestBody.getValue()));
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void lagIfFilterDeleteProcess(List<String> termIds, FcLagIfFilterInfoDao fcLagIfFilterInfoDao,
      List<FcLagIfFilterInfo> fcLagIfFilterInfos, SessionWrapper sessionWrapper,
      List<FilterOperationTermEcEntity> filterOperationTermEcEntities) throws MsfException {
    try {
      logger.methodStart();

      for (int i = 0; i < requestBody.size(); i++) {

        FcLagIfFilterInfo deleteLagIfFilter = null;

        String termId = termIds.get(i);
        for (FcLagIfFilterInfo fcLagIfFilterInfo : fcLagIfFilterInfos) {
          if (termId.equals(fcLagIfFilterInfo.getId().getTermId())) {
            deleteLagIfFilter = fcLagIfFilterInfo;
          }
        }

        if (deleteLagIfFilter == null) {

          throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
              MessageFormat.format("target resource is not found. termId = {0}.", termId));
        }

        fcLagIfFilterInfoDao.delete(sessionWrapper, deleteLagIfFilter.getId());

        LagIfFilterCreateDeleteRequestBody lagIfFilterCreateDeleteRequestBody = requestBody.get(i);

        filterOperationTermEcEntities.add(createFilterOperationTermEcEntity(EcFilterUpdateOperation.DELETE.getMessage(),
            deleteLagIfFilter.getId().getTermId(), lagIfFilterCreateDeleteRequestBody.getValue()));
      }
    } finally {
      logger.methodEnd();
    }
  }

  private FcLagIf getLagInterface(SessionWrapper sessionWrapper, FcLagIfDao fcLagIfDao, Integer nodeType,
      Integer nodeId, Integer lagIfId) throws MsfException {
    try {
      logger.methodStart();
      FcLagIf fcLagIf = fcLagIfDao.read(sessionWrapper, nodeType, nodeId, lagIfId);
      if (fcLagIf == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, MessageFormat.format(
            "target resource is not found. nodeType = {0}, nodeId = {1}, lagIfId = {2}.", nodeType, nodeId, lagIfId));
      }
      return fcLagIf;
    } finally {
      logger.methodEnd();
    }
  }

  private List<String> getLagIfTermIds(boolean isCreate) throws MsfException {
    try {
      logger.methodStart();

      List<String> pathList = new ArrayList<>();

      for (LagIfFilterCreateDeleteRequestBody lagIfFilterCreateDeleteRequestBody : requestBody) {
        pathList.add(lagIfFilterCreateDeleteRequestBody.getPath());
      }

      return getTermIds(pathList, isCreate);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseLagIfFilterCreateData(
      List<FilterOperationTermEcEntity> filterOperationTermEcEntities) {
    try {
      logger.methodStart();
      LagIfFilterCreateDeleteAsyncResponseBody responseBody = new LagIfFilterCreateDeleteAsyncResponseBody();
      List<String> termIds = new ArrayList<>();
      for (FilterOperationTermEcEntity filterOperationTermEcEntity : filterOperationTermEcEntities) {
        termIds.add(filterOperationTermEcEntity.getTermName());
      }
      responseBody.setTermIds(termIds);
      return createRestResponse(responseBody, HttpStatus.CREATED_201);
    } finally {
      logger.methodEnd();
    }
  }

}
