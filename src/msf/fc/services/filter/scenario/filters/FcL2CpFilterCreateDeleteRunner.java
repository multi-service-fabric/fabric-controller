
package msf.fc.services.filter.scenario.filters;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcCpFilterInfo;
import msf.fc.common.data.FcCpFilterInfoPK;
import msf.fc.common.data.FcL2Cp;
import msf.fc.common.data.FcL2CpPK;
import msf.fc.common.data.FcL2Slice;
import msf.fc.common.data.FcNode;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.filters.FcCpFilterInfoDao;
import msf.fc.db.dao.slices.FcL2CpDao;
import msf.fc.services.filter.rest.ec.filters.data.entity.FilterOperationTermEcEntity;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.PatchOperation;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.services.filter.common.constant.EcFilterIfType;
import msf.mfcfc.services.filter.common.constant.EcFilterUpdateOperation;
import msf.mfcfc.services.filter.scenario.filters.data.L2CpFilterCreateDeleteAsyncResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.L2CpFilterCreateDeleteRequestBody;
import msf.mfcfc.services.filter.scenario.filters.data.L2CpFilterRequest;

/**
 * Class to implement the asynchronous processing in the L2CP filter information
 * registration/deletion.
 *
 * @author NTT
 *
 */
public class FcL2CpFilterCreateDeleteRunner extends FcAbstractFilterRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL2CpFilterCreateDeleteRunner.class);

  private L2CpFilterRequest request;
  private List<L2CpFilterCreateDeleteRequestBody> requestBody;

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
  public FcL2CpFilterCreateDeleteRunner(L2CpFilterRequest request,
      List<L2CpFilterCreateDeleteRequestBody> requestBody) {

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

        FcL2Cp l2Cp = getL2Cp(sessionWrapper, new FcL2CpDao(), request.getSliceId(), request.getCpId());

        FcNode fcNode = null;
        if (l2Cp.getEdgePoint().getLagIf() != null) {
          fcNode = l2Cp.getEdgePoint().getLagIf().getNode();
        } else if (l2Cp.getEdgePoint().getPhysicalIf() != null) {
          fcNode = l2Cp.getEdgePoint().getPhysicalIf().getNode();
        } else {
          fcNode = l2Cp.getEdgePoint().getBreakoutIf().getNode();
        }

        logger.performance("start get l2slice and node resources lock.");
        sessionWrapper.beginTransaction();

        FcL2Slice l2Slice = l2Cp.getL2Slice();
        List<FcL2Slice> fcTableLockl2Slices = new ArrayList<>();
        fcTableLockl2Slices.add(l2Slice);

        List<FcNode> fcTableLockLeafNodes = new ArrayList<>();
        fcTableLockLeafNodes.add(fcNode);
        FcDbManager.getInstance().getResourceLock(fcTableLockl2Slices, null, fcTableLockLeafNodes, null,
            sessionWrapper);
        logger.performance("end get l2slice and node resources lock.");

        List<FilterOperationTermEcEntity> filterOperationTermEcEntities = new ArrayList<>();

        PatchOperation patchOperation = requestBody.get(0).getOpEnum();
        if (patchOperation.equals(PatchOperation.ADD)) {

          List<String> termIds = getL2CpTermIds(true);

          FcCpFilterInfoDao fcCpFilterInfoDao = new FcCpFilterInfoDao();

          List<FcCpFilterInfo> fcCpFilterInfos = fcCpFilterInfoDao.readList(sessionWrapper, request.getSliceId(),
              request.getCpId());

          l2CpFilterCreateProcess(termIds, fcCpFilterInfoDao, fcCpFilterInfos, sessionWrapper,
              filterOperationTermEcEntities);

          sendFilterCreateDelete(fcNode.getEcNodeId(), EcFilterIfType.VLAN_IFS.getMessage(),
              String.valueOf(l2Cp.getVlanIf().getId().getVlanIfId()), filterOperationTermEcEntities);

          responseBase = responseL2CpFilterCreateData(filterOperationTermEcEntities);

        } else {

          List<String> termIds = getL2CpTermIds(false);

          FcCpFilterInfoDao fcCpFilterInfoDao = new FcCpFilterInfoDao();

          List<FcCpFilterInfo> fcCpFilterInfos = fcCpFilterInfoDao.readList(sessionWrapper, request.getSliceId(),
              request.getCpId());

          l2CpFilterDeleteProcess(termIds, fcCpFilterInfoDao, fcCpFilterInfos, sessionWrapper,
              filterOperationTermEcEntities);

          sendFilterCreateDelete(fcNode.getEcNodeId(), EcFilterIfType.VLAN_IFS.getMessage(),
              String.valueOf(l2Cp.getVlanIf().getId().getVlanIfId()), filterOperationTermEcEntities);

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

  private void l2CpFilterCreateProcess(List<String> termIds, FcCpFilterInfoDao fcCpFilterInfoDao,
      List<FcCpFilterInfo> fcCpFilterInfos, SessionWrapper sessionWrapper,
      List<FilterOperationTermEcEntity> filterOperationTermEcEntities) throws MsfException {
    try {
      logger.methodStart();

      TreeSet<String> alreadyGeneratedIds = new TreeSet<>();
      for (FcCpFilterInfo fcCpFilterInfo : fcCpFilterInfos) {

        alreadyGeneratedIds.add(fcCpFilterInfo.getId().getTermId());
      }

      int generateCandidateId = 1;

      for (int i = 0; i < requestBody.size(); i++) {

        FcCpFilterInfoPK createCpFilterPk = new FcCpFilterInfoPK();

        String termId = termIds.get(i);
        if (!termId.equals(AUTOMATIC_GENERATED_NUMBER)) {

          if (alreadyGeneratedIds.contains(termId)) {

            throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST,
                MessageFormat.format("target resouece already exist. termId = {0}", termId));
          }

          createCpFilterPk.setTermId(termId);

          alreadyGeneratedIds.add(termId);
        } else {

          int nextTermId = getNextTermId(alreadyGeneratedIds, generateCandidateId);
          String formatTermId = String.format("%06d", nextTermId);

          createCpFilterPk.setTermId(formatTermId);

          alreadyGeneratedIds.add(formatTermId);

          generateCandidateId = nextTermId + 1;
        }

        FcCpFilterInfo createCpFilter = new FcCpFilterInfo();
        createCpFilterPk.setSliceId(request.getSliceId());
        createCpFilterPk.setCpId(request.getCpId());
        createCpFilter.setId(createCpFilterPk);

        fcCpFilterInfoDao.create(sessionWrapper, createCpFilter);

        L2CpFilterCreateDeleteRequestBody l2CpFilterCreateDeleteRequestBody = requestBody.get(i);

        filterOperationTermEcEntities.add(createFilterOperationTermEcEntity(EcFilterUpdateOperation.ADD.getMessage(),
            createCpFilterPk.getTermId(), l2CpFilterCreateDeleteRequestBody.getValue()));
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void l2CpFilterDeleteProcess(List<String> termIds, FcCpFilterInfoDao fcCpFilterInfoDao,
      List<FcCpFilterInfo> fcCpFilterInfos, SessionWrapper sessionWrapper,
      List<FilterOperationTermEcEntity> filterOperationTermEcEntities) throws MsfException {
    try {
      logger.methodStart();

      for (int i = 0; i < requestBody.size(); i++) {

        FcCpFilterInfo deleteCpFilter = null;

        String termId = termIds.get(i);
        for (FcCpFilterInfo fcCpFilterInfo : fcCpFilterInfos) {
          if (termId.equals(fcCpFilterInfo.getId().getTermId())) {
            deleteCpFilter = fcCpFilterInfo;
          }
        }

        if (deleteCpFilter == null) {

          throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
              MessageFormat.format("target resource is not found. termId = {0}.", termId));
        }

        fcCpFilterInfoDao.delete(sessionWrapper, deleteCpFilter.getId());

        L2CpFilterCreateDeleteRequestBody l2CpFilterCreateDeleteRequestBody = requestBody.get(i);

        filterOperationTermEcEntities.add(createFilterOperationTermEcEntity(EcFilterUpdateOperation.DELETE.getMessage(),
            deleteCpFilter.getId().getTermId(), l2CpFilterCreateDeleteRequestBody.getValue()));
      }
    } finally {
      logger.methodEnd();
    }
  }

  private FcL2Cp getL2Cp(SessionWrapper sessionWrapper, FcL2CpDao fcL2CpDao, String sliceId, String cpId)
      throws MsfException {
    try {
      logger.methodStart();
      FcL2CpPK pk = new FcL2CpPK();
      pk.setSliceId(sliceId);
      pk.setCpId(cpId);
      FcL2Cp fcL2Cp = fcL2CpDao.read(sessionWrapper, pk);
      if (fcL2Cp == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            MessageFormat.format("target resource is not found. sliceId = {0}, cpId = {1}.", sliceId, cpId));
      }
      return fcL2Cp;
    } finally {
      logger.methodEnd();
    }
  }

  private List<String> getL2CpTermIds(boolean isCreate) throws MsfException {
    try {
      logger.methodStart();

      List<String> pathList = new ArrayList<>();

      for (L2CpFilterCreateDeleteRequestBody l2CpFilterCreateDeleteRequestBody : requestBody) {
        pathList.add(l2CpFilterCreateDeleteRequestBody.getPath());
      }

      return getTermIds(pathList, isCreate);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseL2CpFilterCreateData(
      List<FilterOperationTermEcEntity> filterOperationTermEcEntities) {
    try {
      logger.methodStart();
      L2CpFilterCreateDeleteAsyncResponseBody responseBody = new L2CpFilterCreateDeleteAsyncResponseBody();
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
