
package msf.fc.services.filter.scenario.filters;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.common.data.FcPhysicalIfFilterInfo;
import msf.fc.common.data.FcPhysicalIfFilterInfoPK;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcPhysicalIfDao;
import msf.fc.db.dao.filters.FcPhysicalIfFilterInfoDao;
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
import msf.mfcfc.services.filter.scenario.filters.data.PhysicalIfFilterCreateDeleteAsyncResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.PhysicalIfFilterCreateDeleteRequestBody;
import msf.mfcfc.services.filter.scenario.filters.data.PhysicalIfFilterRequest;

/**
 * Class to implement the asynchronous processing in the physical IF filter
 * information registration/deletion.
 *
 * @author NTT
 *
 */
public class FcPhysicalIfFilterCreateDeleteRunner extends FcAbstractFilterRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcPhysicalIfFilterCreateDeleteRunner.class);

  private PhysicalIfFilterRequest request;
  private List<PhysicalIfFilterCreateDeleteRequestBody> requestBody;

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
  public FcPhysicalIfFilterCreateDeleteRunner(PhysicalIfFilterRequest request,
      List<PhysicalIfFilterCreateDeleteRequestBody> requestBody) {

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

        FcPhysicalIf fcPhysicalIf = getPhysicalInterface(sessionWrapper, new FcPhysicalIfDao(),
            request.getFabricTypeEnum().getCode(), Integer.parseInt(request.getNodeId()), request.getIfId());

        FcNode fcNode = fcPhysicalIf.getNode();

        logger.performance("start get leaf resources lock.");
        sessionWrapper.beginTransaction();

        List<FcNode> fcTableLockLeafNodes = new ArrayList<>();
        fcTableLockLeafNodes.add(fcNode);
        FcDbManager.getInstance().getLeafsLock(fcTableLockLeafNodes, sessionWrapper);
        logger.performance("end get leaf resources lock.");

        List<FilterOperationTermEcEntity> filterOperationTermEcEntities = new ArrayList<>();

        PatchOperation patchOperation = requestBody.get(0).getOpEnum();
        if (patchOperation.equals(PatchOperation.ADD)) {

          List<String> termIds = getPhysicalIfTermIds(true);

          FcPhysicalIfFilterInfoDao fcPhysicalIfFilterInfoDao = new FcPhysicalIfFilterInfoDao();

          List<FcPhysicalIfFilterInfo> fcPhysicalIfFilterInfos = fcPhysicalIfFilterInfoDao.readList(sessionWrapper,
              NodeType.LEAF.getCode(), Integer.parseInt(request.getNodeId()), request.getIfId());

          physicalIfFilterCreateProcess(termIds, fcPhysicalIfFilterInfoDao, fcPhysicalIfFilterInfos, sessionWrapper,
              filterOperationTermEcEntities, fcPhysicalIf.getPhysicalIfInfoId());

          sendFilterCreateDelete(fcNode.getEcNodeId(), EcFilterIfType.PHYSICAL_IFS.getMessage(),
              fcPhysicalIf.getPhysicalIfId(), filterOperationTermEcEntities);

          responseBase = responsePhysicalIfFilterCreateData(filterOperationTermEcEntities);

        } else {

          List<String> termIds = getPhysicalIfTermIds(false);

          FcPhysicalIfFilterInfoDao fcPhysicalIfFilterInfoDao = new FcPhysicalIfFilterInfoDao();

          List<FcPhysicalIfFilterInfo> fcPhysicalIfFilterInfos = fcPhysicalIfFilterInfoDao.readList(sessionWrapper,
              NodeType.LEAF.getCode(), Integer.parseInt(request.getNodeId()), request.getIfId());

          physicalIfFilterDeleteProcess(termIds, fcPhysicalIfFilterInfoDao, fcPhysicalIfFilterInfos, sessionWrapper,
              filterOperationTermEcEntities);

          sendFilterCreateDelete(fcNode.getEcNodeId(), EcFilterIfType.PHYSICAL_IFS.getMessage(),
              fcPhysicalIf.getPhysicalIfId(), filterOperationTermEcEntities);

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

  private void physicalIfFilterCreateProcess(List<String> termIds, FcPhysicalIfFilterInfoDao fcPhysicalIfFilterInfoDao,
      List<FcPhysicalIfFilterInfo> fcPhysicalIfFilterInfos, SessionWrapper sessionWrapper,
      List<FilterOperationTermEcEntity> filterOperationTermEcEntities, Long pysicalIfInfoId) throws MsfException {
    try {
      logger.methodStart();

      TreeSet<String> alreadyGeneratedIds = new TreeSet<>();
      for (FcPhysicalIfFilterInfo fcPhycicalIfFilterInfo : fcPhysicalIfFilterInfos) {

        alreadyGeneratedIds.add(fcPhycicalIfFilterInfo.getId().getTermId());
      }

      int generateCandidateId = 1;

      for (int i = 0; i < requestBody.size(); i++) {

        FcPhysicalIfFilterInfoPK createPhysicalIfFilterPk = new FcPhysicalIfFilterInfoPK();

        String termId = termIds.get(i);
        if (!termId.equals(AUTOMATIC_GENERATED_NUMBER)) {

          if (alreadyGeneratedIds.contains(termId)) {

            throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST,
                MessageFormat.format("target resouece already exist. termId = {0}", termId));
          }

          createPhysicalIfFilterPk.setTermId(termId);

          alreadyGeneratedIds.add(termId);
        } else {

          int nextTermId = getNextTermId(alreadyGeneratedIds, generateCandidateId);
          String formatTermId = String.format("%06d", nextTermId);

          createPhysicalIfFilterPk.setTermId(formatTermId);

          alreadyGeneratedIds.add(formatTermId);

          generateCandidateId = nextTermId + 1;
        }

        FcPhysicalIfFilterInfo createPhysicalIfFilter = new FcPhysicalIfFilterInfo();
        createPhysicalIfFilterPk.setPhysicalIfInfoId(pysicalIfInfoId);
        createPhysicalIfFilter.setId(createPhysicalIfFilterPk);

        fcPhysicalIfFilterInfoDao.create(sessionWrapper, createPhysicalIfFilter);

        PhysicalIfFilterCreateDeleteRequestBody physicalIfFilterCreateDeleteRequestBody = requestBody.get(i);

        filterOperationTermEcEntities.add(createFilterOperationTermEcEntity(EcFilterUpdateOperation.ADD.getMessage(),
            createPhysicalIfFilterPk.getTermId(), physicalIfFilterCreateDeleteRequestBody.getValue()));
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void physicalIfFilterDeleteProcess(List<String> termIds, FcPhysicalIfFilterInfoDao fcPhysicalIfFilterInfoDao,
      List<FcPhysicalIfFilterInfo> fcPhysicalIfFilterInfos, SessionWrapper sessionWrapper,
      List<FilterOperationTermEcEntity> filterOperationTermEcEntities) throws MsfException {
    try {
      logger.methodStart();

      for (int i = 0; i < requestBody.size(); i++) {

        FcPhysicalIfFilterInfo deletePhysicalIfFilter = null;

        String termId = termIds.get(i);
        for (FcPhysicalIfFilterInfo fcPhysicalIfFilterInfo : fcPhysicalIfFilterInfos) {
          if (termId.equals(fcPhysicalIfFilterInfo.getId().getTermId())) {
            deletePhysicalIfFilter = fcPhysicalIfFilterInfo;
          }
        }

        if (deletePhysicalIfFilter == null) {

          throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
              MessageFormat.format("target resource is not found. termId = {0}.", termId));
        }

        fcPhysicalIfFilterInfoDao.delete(sessionWrapper, deletePhysicalIfFilter.getId());

        PhysicalIfFilterCreateDeleteRequestBody physicalIfFilterCreateDeleteRequestBody = requestBody.get(i);

        filterOperationTermEcEntities.add(createFilterOperationTermEcEntity(EcFilterUpdateOperation.DELETE.getMessage(),
            deletePhysicalIfFilter.getId().getTermId(), physicalIfFilterCreateDeleteRequestBody.getValue()));
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected FcPhysicalIf getPhysicalInterface(SessionWrapper sessionWrapper, FcPhysicalIfDao fcPhysicalIfDao,
      Integer nodeType, Integer nodeId, String physicalIfId) throws MsfException {
    try {
      logger.methodStart();
      FcPhysicalIf fcPhysicalIf = fcPhysicalIfDao.read(sessionWrapper, nodeType, nodeId, physicalIfId);
      if (fcPhysicalIf == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            MessageFormat.format("target resource is not found. nodeType = {0}, nodeId = {1}, physicalIfId = {2}.",
                nodeType, nodeId, physicalIfId));
      }
      return fcPhysicalIf;
    } finally {
      logger.methodEnd();
    }
  }

  private List<String> getPhysicalIfTermIds(boolean isCreate) throws MsfException {
    try {
      logger.methodStart();

      List<String> pathList = new ArrayList<>();

      for (PhysicalIfFilterCreateDeleteRequestBody physicalIfFilterCreateDeleteRequestBody : requestBody) {
        pathList.add(physicalIfFilterCreateDeleteRequestBody.getPath());
      }

      return getTermIds(pathList, isCreate);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responsePhysicalIfFilterCreateData(
      List<FilterOperationTermEcEntity> filterOperationTermEcEntities) {
    try {
      logger.methodStart();
      PhysicalIfFilterCreateDeleteAsyncResponseBody responseBody = new PhysicalIfFilterCreateDeleteAsyncResponseBody();
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
