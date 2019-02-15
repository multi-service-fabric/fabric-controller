
package msf.fc.slice.cps.l2cp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcL2Cp;
import msf.fc.common.data.FcL2Slice;
import msf.fc.common.data.FcNode;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.slices.FcL2CpDao;
import msf.fc.rest.ec.core.operation.data.entity.OperationCreateVlanIfEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationDeleteVlanIfEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationUpdateVlanIfEcEntity;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.cps.l2cp.data.L2CpCreateDeleteRequestBody;
import msf.mfcfc.slice.cps.l2cp.data.L2CpRequest;

/**
 * Class to implement the asynchronous processing in the L2CP
 * addition/deletion(/modification).
 *
 * @author NTT
 *
 */
public class FcL2CpCreateDeleteRunner extends FcAbstractL2CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL2CpCreateDeleteRunner.class);

  private List<L2CpCreateDeleteRequestBody> requestBody;

  private Set<FcNode> nodeSetForExclusive = new HashSet<>();
  private List<String> createdCpIdList = new ArrayList<>();

  /**
   * Constructor. <br>
   * Take over the necessary information from scenario
   *
   * @param request
   *          Request for the L2CP control
   * @param requestBody
   *          Request body for the L2CP addition/deletion(/modification)
   */
  public FcL2CpCreateDeleteRunner(L2CpRequest request, List<L2CpCreateDeleteRequestBody> requestBody) {
    this.request = request;
    this.requestBody = requestBody;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      sessionWrapper.openSession();

      FcL2Slice l2Slice = getL2SliceAndCheck(sessionWrapper, request.getSliceId());
      List<FcL2Slice> l2SliceList = new ArrayList<>();
      l2SliceList.add(l2Slice);

      for (L2CpCreateDeleteRequestBody body : requestBody) {
        switch (body.getOpEnum()) {
          case ADD:
            FcNode nodeAdd = getNodeAndCheck(sessionWrapper, Integer.valueOf(body.getValue().getEdgePointId()));
            nodeSetForExclusive.add(nodeAdd);
            break;
          case REMOVE:
          case REPLACE:
            FcL2Cp l2Cp = getL2CpAndCheck(sessionWrapper, request.getSliceId(), getIdFromPath(body.getPath()));
            FcNode nodeRemRep = getNodeAndCheck(sessionWrapper, l2Cp.getEdgePoint().getEdgePointId());
            nodeSetForExclusive.add(nodeRemRep);
            break;
          default:
            String message = logger.debug("Unexpected argument.(arg={0})", body.getOp());
            throw new IllegalArgumentException(message);
        }
      }

      sessionWrapper.beginTransaction();

      logger.performance("start get l2slice and node resources lock.");
      FcDbManager.getInstance().getResourceLock(l2SliceList, new ArrayList<>(), new ArrayList<>(nodeSetForExclusive),
          new ArrayList<>(), sessionWrapper);
      logger.performance("end get l2slice and node resources lock.");

      FcL2Slice l2SliceAfterLock = getL2SliceAndCheck(sessionWrapper, request.getSliceId());

      List<String> requestCpIdList = getNewL2CpCreateIdListFromPatchRequest(requestBody);

      Iterator<L2CpCreateDeleteRequestBody> iterator = requestBody.iterator();
      while (iterator.hasNext()) {
        L2CpCreateDeleteRequestBody body = iterator.next();

        iterator.remove();
        switch (body.getOpEnum()) {
          case ADD:
            FcNode nodeAfterLockAdd = getNodeAndCheck(sessionWrapper,
                Integer.valueOf(body.getValue().getEdgePointId()));

            String cpId = getCpIdAndCheck(sessionWrapper, request.getSliceId(), getIdFromPath(body.getPath()),
                requestCpIdList);

            createdCpIdList.add(cpId);

            processCreateL2Cp(sessionWrapper, l2SliceAfterLock, nodeAfterLockAdd, cpId,
                Integer.valueOf(body.getValue().getEdgePointId()), body.getValue().getPortMode(),
                body.getValue().getVlanId(), body.getValue().getPairCpId(), body.getValue().getEsi(),
                body.getValue().getQos(), body.getValue().getIrb(), body.getValue().getTrafficThreshold());
            break;
          case REMOVE:
            FcL2Cp l2CpAfterLockRemove = getL2CpAndCheck(sessionWrapper, request.getSliceId(),
                getIdFromPath(body.getPath()));
            FcNode nodeAfterLockRemove = getNodeAndCheck(sessionWrapper,
                l2CpAfterLockRemove.getEdgePoint().getEdgePointId());

            processDeleteL2Cp(sessionWrapper, l2CpAfterLockRemove, nodeAfterLockRemove);
            break;
          case REPLACE:
            FcL2Cp l2CpAfterLockReplace = getL2CpAndCheck(sessionWrapper, request.getSliceId(),
                getIdFromPath(body.getPath()));

            processUpdateL2Cp(sessionWrapper, l2CpAfterLockReplace, body.getValue().getEsi());
            break;
          default:
            String message = logger.debug("Unexpected argument.(arg={0})", body.getOp());
            throw new IllegalArgumentException(message);
        }

        iterator = requestBody.iterator();
      }

      String requestJson = null;

      RestResponseBase response = null;

      if (createVlanIfEntityList.size() != 0) {
        requestJson = makeCreateUpdateL2VlanIfData(createVlanIfEntityList, updateVlanIfEntityList, l2Slice);
        response = createResponseForMultiL2CpCreate(createdCpIdList);
      } else if (deleteVlanIfEntityList.size() == 0 && updateVlanIfEntityList.size() != 0) {

        OperationUpdateVlanIfEcEntity operationUpdateVlanIfEcEntity = updateVlanIfEntityList.get(0);
        if (operationUpdateVlanIfEcEntity.getEsi() != null) {
          if (operationUpdateVlanIfEcEntity.getEsi().equals("0")) {
            requestJson = makeDeleteUpdateL2VlanIfData(null, updateVlanIfEntityList, l2Slice);
          } else {
            requestJson = makeCreateUpdateL2VlanIfData(null, updateVlanIfEntityList, l2Slice);
          }
        } else {
          if (operationUpdateVlanIfEcEntity.getDummyFlag() != null) {
            requestJson = makeDeleteUpdateL2VlanIfData(null, updateVlanIfEntityList, l2Slice);
          } else {
            requestJson = makeCreateUpdateL2VlanIfData(null, updateVlanIfEntityList, l2Slice);
          }
        }
        response = new RestResponseBase(HttpStatus.OK_200, (String) null);
      } else {
        requestJson = makeDeleteUpdateL2VlanIfData(deleteVlanIfEntityList, updateVlanIfEntityList, l2Slice);
        response = new RestResponseBase(HttpStatus.NO_CONTENT_204, (String) null);
      }

      RestResponseBase restResponse = sendRequestToEc(requestJson, EcRequestUri.OPERATION_REQUEST, (String[]) null);

      commitTransaction(sessionWrapper, restResponse, HttpStatus.OK_200);
      return response;

    } catch (MsfException exp) {
      logger.error(exp.getMessage(), exp);
      sessionWrapper.rollback();
      throw exp;
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  @Override
  protected void processCreateL2CpForPairCpNotFound(SessionWrapper sessionWrapper, FcL2Cp newL2Cp, FcNode node,
      String pairCpId, String portMode, int vlanId, int edgePointId) throws MsfException {
    try {
      logger.methodStart();
      FcL2CpDao l2CpDao = new FcL2CpDao();

      Iterator<L2CpCreateDeleteRequestBody> iterator = requestBody.iterator();
      boolean isFoundPairCp = false;
      while (iterator.hasNext()) {
        L2CpCreateDeleteRequestBody body = iterator.next();

        if (pairCpId.equals(getIdFromPath(body.getPath())) && body.getValue() != null
            && newL2Cp.getId().getCpId().equals(body.getValue().getPairCpId())) {

          iterator.remove();

          createdCpIdList.add(pairCpId);

          isFoundPairCp = true;
          FcNode pairNode = getNodeAndCheck(sessionWrapper, Integer.valueOf(body.getValue().getEdgePointId()));

          checkPairCpVlanIdAndPortMode(sessionWrapper, portMode, vlanId, pairNode.getNodeInfoId().intValue(), null,
              body.getValue().getPortMode(), body.getValue().getVlanId());

          checkCreateTargetNode(sessionWrapper, pairCpId, edgePointId,
              Integer.valueOf(body.getValue().getEdgePointId()));

          FcL2Cp pairL2Cp = makeNewL2Cp(sessionWrapper, newL2Cp.getL2Slice(), pairNode, pairCpId,
              Integer.valueOf(body.getValue().getEdgePointId()), body.getValue().getEsi(),
              body.getValue().getTrafficThreshold());

          List<FcL2Cp> l2CpList = l2CpDao.readList(sessionWrapper);
          Set<String> esiIdSet = createEsiIdSet(l2CpList);

          int clusterId = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId();
          String esi = getNextEsi(sessionWrapper, esiIdSet, clusterId, clusterId);
          newL2Cp.setEsi(esi);
          pairL2Cp.setEsi(esi);

          Set<Integer> clagIdSet = createClagIdSet(l2CpList);

          int clagId = getNextClagId(clagIdSet);
          newL2Cp.setClagId(clagId);
          pairL2Cp.setClagId(clagId);

          OperationCreateVlanIfEcEntity createVlanIfEntity = makeOperationCreateVlanIfEcEntity(sessionWrapper, pairL2Cp,
              body.getValue().getPortMode(), body.getValue().getVlanId(), body.getValue().getQos());

          if (newL2Cp.getL2Slice().getIrbType() != null) {
            processCreateL2CpWithIrbEnabled(sessionWrapper, createVlanIfEntity, pairL2Cp, pairNode, vlanId,
                body.getValue().getIrb());
          } else {

            createVlanIfEntityList.add(createVlanIfEntity);
            l2CpDao.create(sessionWrapper, pairL2Cp);
          }
          break;
        }
      }

      if (!isFoundPairCp) {
        super.processCreateL2CpForPairCpNotFound(sessionWrapper, newL2Cp, node, pairCpId, portMode, vlanId,
            edgePointId);
      }
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected void processDeleteL2CpForPairCpFound(SessionWrapper sessionWrapper, FcL2Cp pairL2Cp) throws MsfException {
    try {
      logger.methodStart();
      FcL2CpDao l2CpDao = new FcL2CpDao();

      Iterator<L2CpCreateDeleteRequestBody> iterator = requestBody.iterator();
      boolean isFoundPairCp = false;
      while (iterator.hasNext()) {
        L2CpCreateDeleteRequestBody body = iterator.next();

        if (pairL2Cp.getId().getCpId().equals(getIdFromPath(body.getPath()))) {

          iterator.remove();

          isFoundPairCp = true;

          FcNodeDao nodeDao = new FcNodeDao();
          FcNode pairCpNode = nodeDao.read(sessionWrapper, pairL2Cp.getEdgePoint().getEdgePointId());
          OperationDeleteVlanIfEcEntity deleteVlanIfEntity = makeOperationDeleteVlanIfEcEntity(pairCpNode.getEcNodeId(),
              pairL2Cp.getVlanIf().getId().getVlanIfId());

          if (pairL2Cp.getL2Slice().getIrbType() != null) {
            processDeleteL2CpWithIrbEnabled(sessionWrapper, pairL2Cp, pairCpNode, deleteVlanIfEntity);
          } else {
            deleteVlanIfEntityList.add(deleteVlanIfEntity);

            l2CpDao.delete(sessionWrapper, pairL2Cp.getId());
          }
        }
      }

      if (!isFoundPairCp) {
        super.processDeleteL2CpForPairCpFound(sessionWrapper, pairL2Cp);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void processUpdateL2Cp(SessionWrapper sessionWrapper, FcL2Cp updateL2Cp, String updateEsi)
      throws MsfException {
    try {
      logger.methodStart();
      FcL2CpDao l2CpDao = new FcL2CpDao();

      if (updateEsi.equals("0")) {
        beforeUpdateEsiList.add(updateL2Cp.getEsi());
        updateL2Cp.setEsi(null);
      } else {
        updateL2Cp.setEsi(updateEsi);
      }
      OperationUpdateVlanIfEcEntity updateVlanIfEntity = makeOperationUpdateVlanIfEcEntity(sessionWrapper, updateL2Cp,
          false);
      updateVlanIfEntityList.add(updateVlanIfEntity);

      l2CpDao.update(sessionWrapper, updateL2Cp);
    } finally {
      logger.methodEnd();
    }
  }
}
