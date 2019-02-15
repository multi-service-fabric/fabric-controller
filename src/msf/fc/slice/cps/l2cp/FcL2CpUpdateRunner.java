
package msf.fc.slice.cps.l2cp;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcL2Cp;
import msf.fc.common.data.FcL2Slice;
import msf.fc.common.data.FcNode;
import msf.fc.db.FcDbManager;
import msf.fc.rest.ec.node.interfaces.vlan.data.VlanIfUpdateEcRequestBody;
import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfL2VlanOptionEcEntity;
import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfQosUpdateEcEntity;
import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfUpdateOptionEcEntity;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.EcVlanIfUpdateOperationType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.cps.l2cp.data.L2CpRequest;
import msf.mfcfc.slice.cps.l2cp.data.L2CpUpdateRequestBody;
import msf.mfcfc.slice.cps.l2cp.data.entity.L2CpQosCreateEntity;

/**
 * Implementation class for the asynchronous process in the L2CP modification
 * process.
 *
 * @author NTT
 *
 */
public class FcL2CpUpdateRunner extends FcAbstractL2CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL2CpUpdateRunner.class);

  private L2CpUpdateRequestBody requestBody;

  /**
   * Constructor.<br>
   * Take over the necessary information from scenario
   *
   * @param request
   *          Request for the L2CP control
   * @param requestBody
   *          Request Body part for the L2CP modification
   */
  public FcL2CpUpdateRunner(L2CpRequest request, L2CpUpdateRequestBody requestBody) {
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
      getL2CpAndCheck(sessionWrapper, request.getSliceId(), request.getCpId());

      sessionWrapper.beginTransaction();
      logger.performance("start get l2slice resources lock.");

      FcDbManager.getInstance().getL2SlicesLock(l2SliceList, sessionWrapper);
      logger.performance("end get l2slice resources lock.");

      getL2SliceAndCheck(sessionWrapper, request.getSliceId());
      FcL2Cp l2CpAfterLock = getL2CpAndCheck(sessionWrapper, request.getSliceId(), request.getCpId());
      FcNode nodeAfterLock = getNodeAndCheck(sessionWrapper, l2CpAfterLock.getEdgePoint().getEdgePointId());

      String requestJson = makeUpdateL2VlanIfData(requestBody.getUpdateOption().getQosUpdateOption(),
          l2Slice.getVrfId(), l2Slice.getVni());

      RestResponseBase restResponse = sendRequestToEc(requestJson, EcRequestUri.VLAN_IF_UPDATE,
          String.valueOf(nodeAfterLock.getEcNodeId()), String.valueOf(l2CpAfterLock.getVlanIf().getId().getVlanIfId()));

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

  private String makeUpdateL2VlanIfData(L2CpQosCreateEntity l2QosEntity, Integer vrfId, Integer vni) {
    try {
      logger.methodStart(new String[] { "l2QosEntity" }, new Object[] { l2QosEntity });

      VlanIfQosUpdateEcEntity qosUpdateEntity = new VlanIfQosUpdateEcEntity();

      qosUpdateEntity.setOperationType(EcVlanIfUpdateOperationType.ADD.getMessage());
      if (l2QosEntity != null) {
        qosUpdateEntity.setEgressQueue(l2QosEntity.getEgressQueueMenu());
        qosUpdateEntity.setInflowShapingRate(l2QosEntity.getIngressShapingRate());
        qosUpdateEntity.setOutflowShapingRate(l2QosEntity.getEgressShapingRate());
      }

      VlanIfL2VlanOptionEcEntity l2VlanOptionEntity = new VlanIfL2VlanOptionEcEntity();
      l2VlanOptionEntity.setVrfId(vrfId);
      l2VlanOptionEntity.setVni(vni);
      l2VlanOptionEntity.setQos(qosUpdateEntity);

      VlanIfUpdateOptionEcEntity updateOptionEntity = new VlanIfUpdateOptionEcEntity();
      updateOptionEntity.setL2vlanOption(l2VlanOptionEntity);

      VlanIfUpdateEcRequestBody requestBody = new VlanIfUpdateEcRequestBody();
      requestBody.setUpdateOption(updateOptionEntity);

      return JsonUtil.toJson(requestBody);
    } finally {
      logger.methodEnd();
    }
  }

}
