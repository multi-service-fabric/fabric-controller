package msf.fc.node.interfaces.physicalifs;

import java.text.MessageFormat;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.EcRequestUri;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.PhysicalIf;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.PhysicalIfDao;
import msf.fc.node.equipments.EquipmentCreateScenario;
import msf.fc.node.interfaces.physicalifs.data.PhysicalIfReadResponseBody;
import msf.fc.node.interfaces.physicalifs.data.PhysicalIfRequest;
import msf.fc.rest.common.JsonUtil;
import msf.fc.rest.common.RestClient;
import msf.fc.rest.ec.node.interfaces.physical.data.PhysicalIfReadEcResponseBody;

public class PhysicalInterfaceReadScenario extends AbstractPhysicalInterfaceScenarioBase<PhysicalIfRequest> {

  private PhysicalIfRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(EquipmentCreateScenario.class);

  public PhysicalInterfaceReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(PhysicalIfRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      checkFabricType(request.getFabricType());

      checkNodeId(request.getNodeId());

      checkPhysicalIfId(request.getIfId());

      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      RestResponseBase responseBase = null;
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        sessionWrapper.openSession();
        PhysicalIfDao physicalIfDao = new PhysicalIfDao();
        PhysicalIf physicalIf = getPhysicalInterface(sessionWrapper, physicalIfDao, request.getClusterId(),
            request.getFabricTypeEnum().getCode(), Integer.parseInt(request.getNodeId()), request.getIfId());

        responseBase = responsePhysicalInterfaceReadData(physicalIf);

      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responsePhysicalInterfaceReadData(PhysicalIf physicalIf) throws MsfException {
    try {
      logger.methodStart(new String[] { "physicalIf" }, new Object[] { physicalIf });
      PhysicalIfReadEcResponseBody physicalIfReadEcResponseBody = sendPhysicalInterfaceRead();

      PhysicalIfReadResponseBody body = new PhysicalIfReadResponseBody();
      body.setPhysicalIf(getPhysicalIfData(physicalIf, physicalIfReadEcResponseBody.getPhysicalIf()));
      return createRestResponse(body, HttpStatus.OK_200);
    } finally {
      logger.methodEnd();
    }
  }

  private PhysicalIfReadEcResponseBody sendPhysicalInterfaceRead() throws MsfException {
    try {
      logger.methodStart();
      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.PHYSICAL_IF_READ.getHttpMethod(),
          EcRequestUri.PHYSICAL_IF_READ.getUri(request.getFabricType(), request.getNodeId(), request.getIfId()), null);

      PhysicalIfReadEcResponseBody physicalIfReadEcResponseBody = new PhysicalIfReadEcResponseBody();

      physicalIfReadEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          PhysicalIfReadEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      if (restResponseBase.getHttpStatusCode() != HttpStatus.OK_200) {
        String errorMsg = MessageFormat.format("HttpStatusCode = {0}, ErrorCode = {1}",
            restResponseBase.getHttpStatusCode(), physicalIfReadEcResponseBody.getErrorCode());
        logger.error(errorMsg);
        throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);
      }

      return physicalIfReadEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

}
