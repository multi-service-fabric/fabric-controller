
package msf.fc.node.equipments;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcEquipment;
import msf.fc.db.dao.clusters.FcEquipmentDao;
import msf.fc.node.FcNodeManager;
import msf.fc.rest.ec.node.equipment.data.EquipmentCreateEcRequestBody;
import msf.fc.rest.ec.node.equipment.data.entity.EquipmentCapabilitiesEcEntity;
import msf.fc.rest.ec.node.equipment.data.entity.EquipmentEcEntity;
import msf.fc.rest.ec.node.equipment.data.entity.EquipmentIfEcEntity;
import msf.fc.rest.ec.node.equipment.data.entity.EquipmentIfNameRulesaEcEntity;
import msf.fc.rest.ec.node.equipment.data.entity.EquipmentZtpEcEntity;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.equipments.data.EquipmentCreateRequestBody;
import msf.mfcfc.node.equipments.data.EquipmentCreateResponseBody;
import msf.mfcfc.node.equipments.data.EquipmentRequest;
import msf.mfcfc.node.equipments.data.entity.EquipmentPortEntity;
import msf.mfcfc.node.equipments.data.entity.EquipmentSlotEntity;
import msf.mfcfc.node.equipments.data.entity.EquipmentTypeEntity;
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Implementation class for model information registration.
 *
 * @author NTT
 *
 */
public class FcEquipmentCreateScenario extends FcAbstractEquipmentScenarioBase<EquipmentRequest> {

  private EquipmentCreateRequestBody requestBody;

  
  private static final MsfLogger logger = MsfLogger.getInstance(FcEquipmentCreateScenario.class);

  /**
   * Constructor.
   *
   * <p>
   * Set the "operation type" and "system interface type" as arguments
   * </p>
   *
   * @param operationType
   *          Operation type
   * @param systemInterfaceType
   *          System interface type
   *
   */
  public FcEquipmentCreateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(EquipmentRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });


      EquipmentCreateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          EquipmentCreateRequestBody.class);
      requestBody.validate();
      logger.debug("requestBody=" + request.getRequestBody());


      this.requestBody = requestBody;

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      logger.performance("start wait to equipment increasing process.");
      synchronized (FcNodeManager.getInstance().getFcEquipmentCreateLockObject()) {
        logger.performance("end wait to equipment increasing process.");
        RestResponseBase responseBase = null;
        SessionWrapper sessionWrapper = new SessionWrapper();
        try {
          sessionWrapper.openSession();
          sessionWrapper.beginTransaction();
          FcEquipmentDao fcEquipmentDao = new FcEquipmentDao();


          FcEquipment fcEquipment = setEquipmentData(sessionWrapper, fcEquipmentDao);


          fcEquipmentDao.create(sessionWrapper, fcEquipment);


          sendEquipmentCreate(fcEquipment);


          responseBase = responseEquipmentCreateData(fcEquipment);

          sessionWrapper.commit();
        } catch (MsfException msfException) {
          logger.error(msfException.getMessage(), msfException);
          sessionWrapper.rollback();
          throw msfException;
        } finally {
          sessionWrapper.closeSession();
        }

        return responseBase;
      }
    } finally {
      logger.methodEnd();
    }
  }

  
  private void checkForDuplicate(SessionWrapper sessionWrapper, FcEquipmentDao equipmentDao) throws MsfException {
    try {
      logger.methodStart();

      FcEquipment equipment = equipmentDao.read(sessionWrapper,
          Integer.valueOf(requestBody.getEquipmentType().getEquipmentTypeId()));
      if (equipment != null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST,
            "target resouece already exist. target = equipment");
      }
    } finally {
      logger.methodEnd();
    }
  }

  
  protected FcEquipment setEquipmentData(SessionWrapper sessionWrapper, FcEquipmentDao fcEquipmentDao)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "equipmentDao" },
          new Object[] { sessionWrapper, fcEquipmentDao });
      FcEquipment fcEquipment = new FcEquipment();

      if (requestBody.getEquipmentType().getEquipmentTypeId() == null) {

        fcEquipment.setEquipmentTypeId(getNextEquipmentTypeId(sessionWrapper, fcEquipmentDao));
      } else {

        checkForDuplicate(sessionWrapper, fcEquipmentDao);
        fcEquipment.setEquipmentTypeId(Integer.valueOf(requestBody.getEquipmentType().getEquipmentTypeId()));
      }
      return fcEquipment;
    } finally {
      logger.methodEnd();
    }
  }

  
  protected void sendEquipmentCreate(FcEquipment fcEquipment) throws MsfException {
    try {
      logger.methodStart(new String[] { "equipment" }, new Object[] { fcEquipment });

      EquipmentCreateEcRequestBody equipmentCreateEcRequestBody = new EquipmentCreateEcRequestBody();

      equipmentCreateEcRequestBody.setEquipment(setEquimpmentCreateEcData(fcEquipment));
      RestRequestBase restRequest = new RestRequestBase();

      restRequest.setRequestBody(JsonUtil.toJson(equipmentCreateEcRequestBody));

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();

      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();


      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.EQUIPMENT_CREATE.getHttpMethod(),
          EcRequestUri.EQUIPMENT_CREATE.getUri(), restRequest, ecControlIpAddress, ecControlPort);


      String errorCode = null;
      if (StringUtils.isNotEmpty(restResponseBase.getResponseBody())) {
        ErrorInternalResponseBody equipmentCreateEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            ErrorInternalResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        errorCode = equipmentCreateEcResponseBody.getErrorCode();
      }


      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.CREATED_201, errorCode,
          ErrorCode.EC_CONTROL_ERROR);
    } finally {
      logger.methodEnd();
    }
  }

  
  private EquipmentEcEntity setEquimpmentCreateEcData(FcEquipment fcEquipment) {
    try {
      logger.methodStart();
      EquipmentEcEntity equipmentEc = new EquipmentEcEntity();
      EquipmentTypeEntity equipmentType = requestBody.getEquipmentType();

      equipmentEc.setEquipmentTypeId(String.valueOf(fcEquipment.getEquipmentTypeId()));
      equipmentEc.setLagPrefix(equipmentType.getIfDefinitions().getLagPrefix());
      equipmentEc.setUnitConnector(equipmentType.getIfDefinitions().getUnitConnector());
      equipmentEc.setIfNameOid(equipmentType.getSnmp().getIfNameOid());
      equipmentEc.setSnmptrapIfNameOid(equipmentType.getSnmp().getSnmptrapIfNameOid());
      equipmentEc.setMaxRepetitions(equipmentType.getSnmp().getMaxRepetitions());
      equipmentEc.setPlatform(equipmentType.getPlatform());
      equipmentEc.setOs(equipmentType.getOs());
      equipmentEc.setFirmware(equipmentType.getFirmware());
      equipmentEc.setRouterType(equipmentType.getRouterType());
      equipmentEc.setPhysicalIfNameSyntax(equipmentType.getPhysicalIfNameSyntax());
      equipmentEc.setBreakoutIfNameSyntax(equipmentType.getBreakoutIfNameSyntax());
      equipmentEc.setBreakoutIfNameSuffixList(equipmentType.getBreakoutIfNameSuffixList());

      EquipmentZtpEcEntity ztp = new EquipmentZtpEcEntity();
      ztp.setDhcpTemplate(equipmentType.getDhcp().getDhcpTemplate());
      ztp.setConfigTemplate(equipmentType.getDhcp().getConfigTemplate());
      ztp.setInitialConfig(equipmentType.getDhcp().getInitialConfig());
      ztp.setBootCompleteMsg(equipmentType.getBootCompleteMsg());
      ztp.setBootErrorMsgList(equipmentType.getBootErrorMsgList());
      equipmentEc.setZtp(ztp);

      EquipmentCapabilitiesEcEntity capabilities = new EquipmentCapabilitiesEcEntity();
      capabilities.setL2vpn(equipmentType.getCapability().getVpn().getL2());
      capabilities.setL3vpn(equipmentType.getCapability().getVpn().getL3());
      capabilities.setEvpn(equipmentType.getCapability().getVpn().getL2());
      equipmentEc.setCapabilities(capabilities);

      List<EquipmentIfNameRulesaEcEntity> ifNameRules = new ArrayList<>();
      for (EquipmentPortEntity equipmentPort : equipmentType.getIfDefinitions().getPortList()) {
        EquipmentIfNameRulesaEcEntity ifNameRule = new EquipmentIfNameRulesaEcEntity();
        ifNameRule.setSpeed(equipmentPort.getSpeed());
        ifNameRule.setPortPrefix(equipmentPort.getPortPrefix());
        ifNameRules.add(ifNameRule);
      }
      equipmentEc.setIfNameRulesaList(ifNameRules);

      List<EquipmentIfEcEntity> equipmentIfs = new ArrayList<>();
      for (EquipmentSlotEntity equipmentSlot : equipmentType.getSlotList()) {
        EquipmentIfEcEntity equipmentIf = new EquipmentIfEcEntity();
        equipmentIf.setPhysicalIfId(equipmentSlot.getIfId());
        equipmentIf.setIfSlot(equipmentSlot.getIfSlot());
        equipmentIf.setPortSpeedTypeList(equipmentSlot.getSpeedCapacityList());
        equipmentIfs.add(equipmentIf);
      }
      equipmentEc.setEquipmentIfList(equipmentIfs);

      return equipmentEc;
    } finally {
      logger.methodEnd();
    }
  }

  
  private RestResponseBase responseEquipmentCreateData(FcEquipment fcEquipment) {
    try {
      logger.methodStart(new String[] { "fcEquipment" }, new Object[] { fcEquipment });
      EquipmentCreateResponseBody body = new EquipmentCreateResponseBody();
      body.setEquipmentTypeId(String.valueOf(fcEquipment.getEquipmentTypeId()));
      return createRestResponse(body, HttpStatus.CREATED_201);
    } finally {
      logger.methodEnd();
    }
  }
}
