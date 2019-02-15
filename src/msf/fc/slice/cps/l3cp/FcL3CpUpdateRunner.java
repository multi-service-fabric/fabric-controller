
package msf.fc.slice.cps.l3cp;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcL3Cp;
import msf.fc.common.data.FcL3Slice;
import msf.fc.common.data.FcNode;
import msf.fc.db.FcDbManager;
import msf.fc.rest.ec.node.interfaces.vlan.data.VlanIfUpdateEcRequestBody;
import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfL3VlanOptionEcEntity;
import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfQosUpdateEcEntity;
import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfUpdateOptionEcEntity;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.EcVlanIfUpdateOperationType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;
import msf.mfcfc.slice.cps.l3cp.data.L3CpUpdateRequestBody;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpQosCreateEntity;

/**
 * Implementation class for the asynchronous process in the L3CP modification
 * process.
 *
 * @author NTT
 *
 */
public class FcL3CpUpdateRunner extends FcAbstractL3CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL3CpUpdateRunner.class);

  private L3CpUpdateRequestBody requestBody;

  /**
   * Constructor. <br>
   * Take over the necessary information from scenario
   *
   * @param request
   *          Request for the L3CP control
   * @param requestBody
   *          Request Body part for the L3CP modification
   */
  public FcL3CpUpdateRunner(L3CpRequest request, L3CpUpdateRequestBody requestBody) {
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

      sessionWrapper.beginTransaction();
      logger.performance("start get l3slice resources lock.");

      FcDbManager.getInstance().getL3SlicesLock(l3SliceList, sessionWrapper);
      logger.performance("end get l3slice resources lock.");

      getL3SliceAndCheck(sessionWrapper, request.getSliceId());
      FcL3Cp l3CpAfterLock = getL3CpAndCheck(sessionWrapper, request.getSliceId(), request.getCpId());
      FcNode nodeAfterLock = getNodeAndCheck(sessionWrapper, l3CpAfterLock.getEdgePoint().getEdgePointId());

      String requestJson = makeUpdateL3VlanIfData(requestBody.getUpdateOption().getQosUpdateOption(),
          l3Slice.getVrfId());

      RestResponseBase restResponse = sendRequestToEc(requestJson, EcRequestUri.VLAN_IF_UPDATE,
          String.valueOf(nodeAfterLock.getEcNodeId()), String.valueOf(l3CpAfterLock.getVlanIf().getId().getVlanIfId()));

      commitTransaction(sessionWrapper, restResponse, HttpStatus.OK_200, false);
      return new RestResponseBase(HttpStatus.OK_200, (String) null);
    } catch (MsfException exp) {
      logger.error(exp.getMessage(), exp);
      sessionWrapper.rollback();
      throw exp;
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  private String makeUpdateL3VlanIfData(L3CpQosCreateEntity l3QosEntity, Integer vrfId) {
    try {
      logger.methodStart(new String[] { "l3QosEntity" }, new Object[] { l3QosEntity });

      VlanIfQosUpdateEcEntity qosUpdateEntity = new VlanIfQosUpdateEcEntity();

      qosUpdateEntity.setOperationType(EcVlanIfUpdateOperationType.ADD.getMessage());
      if (l3QosEntity != null) {
        qosUpdateEntity.setEgressQueue(l3QosEntity.getEgressQueueMenu());
        qosUpdateEntity.setInflowShapingRate(l3QosEntity.getIngressShapingRate());
        qosUpdateEntity.setOutflowShapingRate(l3QosEntity.getEgressShapingRate());
      }

      VlanIfL3VlanOptionEcEntity l3VlanOptionEntity = new VlanIfL3VlanOptionEcEntity();
      l3VlanOptionEntity.setVrfId(vrfId);
      l3VlanOptionEntity.setQos(qosUpdateEntity);

      VlanIfUpdateOptionEcEntity updateOptionEntity = new VlanIfUpdateOptionEcEntity();
      updateOptionEntity.setL3vlanOption(l3VlanOptionEntity);

      VlanIfUpdateEcRequestBody requestBody = new VlanIfUpdateEcRequestBody();
      requestBody.setUpdateOption(updateOptionEntity);

      return JsonUtil.toJson(requestBody);
    } finally {
      logger.methodEnd();
    }
  }

}
