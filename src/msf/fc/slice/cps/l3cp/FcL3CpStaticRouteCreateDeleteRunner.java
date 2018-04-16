
package msf.fc.slice.cps.l3cp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcL3Cp;
import msf.fc.common.data.FcL3Slice;
import msf.fc.common.data.FcNode;
import msf.fc.db.FcDbManager;
import msf.fc.rest.ec.node.interfaces.vlan.data.VlanIfUpdateEcRequestBody;
import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfL3VlanOptionEcEntity;
import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfStaticRouteUpdateEcEntity;
import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfUpdateOptionEcEntity;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.EcVlanIfUpdateOperationType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;
import msf.mfcfc.slice.cps.l3cp.data.L3CpStaticRouteCreateDeleteRequestBody;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpStaticRouteEntity;

/**
 * Class to implement the asynchronous processing in L3CP static route
 * addition/deletion.
 *
 * @author NTT
 *
 */
public class FcL3CpStaticRouteCreateDeleteRunner extends FcAbstractL3CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL3CpStaticRouteCreateDeleteRunner.class);

  private List<L3CpStaticRouteCreateDeleteRequestBody> requestBody;
  private List<String> createdStaticRouteIdList = new ArrayList<>();

  protected List<VlanIfStaticRouteUpdateEcEntity> staticRouteUpdateEntityList = new ArrayList<>();

  /**
   * Constructor. <br>
   * Take over the necessary information from scenario
   *
   * @param request
   *          Request for L3CP control
   * @param requestBody
   *          Request body for L3CP static route addition/deletion
   */
  public FcL3CpStaticRouteCreateDeleteRunner(L3CpRequest request,
      List<L3CpStaticRouteCreateDeleteRequestBody> requestBody) {
    this.request = request;
    this.requestBody = requestBody;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      sessionWrapper.openSession();

      FcL3Slice l3Slice = getL3SliceAndCheck(sessionWrapper, request.getSliceId());
      List<FcL3Slice> l3SliceList = new ArrayList<>();
      l3SliceList.add(l3Slice);

      getL3CpAndCheck(sessionWrapper, request.getSliceId(), request.getCpId());

      logger.performance("start get l3slice resources lock.");
      FcDbManager.getInstance().getResourceLock(new ArrayList<>(), l3SliceList, new ArrayList<>(), new ArrayList<>(),
          sessionWrapper);
      logger.performance("end get l3slice resources lock.");

      FcL3Slice l3SliceAfterLock = getL3SliceAndCheck(sessionWrapper, request.getSliceId());

      FcL3Cp l3CpAfterLock = getL3CpAndCheck(sessionWrapper, request.getSliceId(), request.getCpId());
      FcNode nodeAfterLock = getNodeAndCheck(sessionWrapper, l3CpAfterLock.getEdgePoint().getEdgePointId());

      Iterator<L3CpStaticRouteCreateDeleteRequestBody> iterator = requestBody.iterator();
      while (iterator.hasNext()) {
        L3CpStaticRouteCreateDeleteRequestBody body = iterator.next();

        iterator.remove();
        switch (body.getOpEnum()) {
          case ADD:
            L3CpStaticRouteEntity addEntity = body.getValue().getStaticRoute();

            createdStaticRouteIdList.add(makeStaticRouteId(addEntity));

            checkL3NwConstraints(addEntity);

            staticRouteUpdateEntityList
                .add(makeVlanIfStaticRouteUpdateEcEntity(EcVlanIfUpdateOperationType.ADD, addEntity));
            break;
          case REMOVE:
            L3CpStaticRouteEntity delEntity = makeL3CpStaticRouteEntityFromId(getIdFromPath(body.getPath()));

            staticRouteUpdateEntityList
                .add(makeVlanIfStaticRouteUpdateEcEntity(EcVlanIfUpdateOperationType.DELETE, delEntity));
            break;
          default:
            String message = logger.debug("Unexpected argument.(arg={0})", body.getOp());
            throw new IllegalArgumentException(message);
        }
      }

      String requestJson = makeVlanIfUpdateData(l3SliceAfterLock.getVrfId());

      RestResponseBase restResponse = sendRequestToEc(requestJson, EcRequestUri.VLAN_IF_UPDATE,
          String.valueOf(nodeAfterLock.getEcNodeId()), String.valueOf(l3CpAfterLock.getVlanIf().getId().getVlanIfId()));

      commitTransaction(sessionWrapper, restResponse, HttpStatus.OK_200, false);

      RestResponseBase response = null;
      if (createdStaticRouteIdList.size() != 0) {
        response = createResponseForCreateStaticRoute(createdStaticRouteIdList);
      } else {
        response = new RestResponseBase(HttpStatus.NO_CONTENT_204, (String) null);
      }
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

  private VlanIfStaticRouteUpdateEcEntity makeVlanIfStaticRouteUpdateEcEntity(EcVlanIfUpdateOperationType type,
      L3CpStaticRouteEntity entity) {
    VlanIfStaticRouteUpdateEcEntity ecEntity = new VlanIfStaticRouteUpdateEcEntity();
    ecEntity.setOperationType(type.getMessage());
    ecEntity.setAddress(entity.getAddress());
    ecEntity.setAddressType(entity.getAddrType());
    ecEntity.setNextHop(entity.getNextHop());
    ecEntity.setPrefix(entity.getPrefix());
    return ecEntity;
  }

  private String makeVlanIfUpdateData(int vrfId) {
    try {
      logger.methodStart();
      VlanIfL3VlanOptionEcEntity l3VlanOption = new VlanIfL3VlanOptionEcEntity();

      l3VlanOption.setStaticRouteList(staticRouteUpdateEntityList);
      l3VlanOption.setVrfId(vrfId);
      VlanIfUpdateOptionEcEntity updateOption = new VlanIfUpdateOptionEcEntity();

      updateOption.setL3vlanOption(l3VlanOption);
      VlanIfUpdateEcRequestBody body = new VlanIfUpdateEcRequestBody();
      body.setUpdateOption(updateOption);
      return JsonUtil.toJson(body);
    } finally {
      logger.methodEnd();
    }
  }
}
