package msf.fc.node.interfaces.physicalifs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.EcRequestUri;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.RestFormatOption;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.PhysicalIf;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.PhysicalIfDao;
import msf.fc.node.interfaces.physicalifs.data.PhysicalIfReadDetailListResponseBody;
import msf.fc.node.interfaces.physicalifs.data.PhysicalIfReadListResponseBody;
import msf.fc.node.interfaces.physicalifs.data.PhysicalIfRequest;
import msf.fc.rest.common.JsonUtil;
import msf.fc.rest.common.RestClient;
import msf.fc.rest.ec.node.interfaces.physical.data.PhysicalIfReadListEcResponseBody;

public class PhysicalInterfaceReadListScenario extends AbstractPhysicalInterfaceScenarioBase<PhysicalIfRequest> {

  private PhysicalIfRequest request;
  private static final MsfLogger logger = MsfLogger.getInstance(PhysicalInterfaceReadListScenario.class);

  public PhysicalInterfaceReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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

      if (request.getFormat() != null) {
        ParameterCheckUtil.checkNotNull(request.getFormatEnum());
      }

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
        List<PhysicalIf> physicalIfs = physicalIfDao.readList(sessionWrapper, request.getClusterId(),
            request.getFabricTypeEnum().getCode(), Integer.parseInt(request.getNodeId()));

        if (physicalIfs.isEmpty()) {
          checkNode(sessionWrapper, request.getClusterId(), request.getFabricTypeEnum().getCode(),
              Integer.parseInt(request.getNodeId()));
        }

        responseBase = responsePhysicalInterfaceReadListData(physicalIfs, request.getFormat());

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

  private RestResponseBase responsePhysicalInterfaceReadListData(List<PhysicalIf> physicalIfs, String format)
      throws MsfException {
    try {
      logger.methodStart();
      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        PhysicalIfReadDetailListResponseBody body = new PhysicalIfReadDetailListResponseBody();
        if (!physicalIfs.isEmpty()) {
          PhysicalIfReadListEcResponseBody physicalIfReadListEcResponseBody = sendPhysicalInterfaceReadList();
          body.setPhysicalIfList(
              getPhysicalIfEntities(physicalIfs, physicalIfReadListEcResponseBody.getPhysicalIfList()));
        } else {
          body.setPhysicalIfList(new ArrayList<>());
        }
        return createRestResponse(body, HttpStatus.OK_200);
      } else {
        PhysicalIfReadListResponseBody body = new PhysicalIfReadListResponseBody();
        body.setPhysicalIfIdList(getPhysicalIfIdList(physicalIfs));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private PhysicalIfReadListEcResponseBody sendPhysicalInterfaceReadList() throws MsfException {
    try {
      logger.methodStart();
      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.PHYSICAL_IF_READ_LIST.getHttpMethod(),
          EcRequestUri.PHYSICAL_IF_READ_LIST.getUri(request.getFabricType(), request.getNodeId()), null);

      PhysicalIfReadListEcResponseBody physicalIfReadListEcResponseBody = new PhysicalIfReadListEcResponseBody();

      physicalIfReadListEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          PhysicalIfReadListEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      if (restResponseBase.getHttpStatusCode() != HttpStatus.OK_200) {
        String errorMsg = MessageFormat.format("HttpStatusCode = {0}, ErrorCode = {1}",
            restResponseBase.getHttpStatusCode(), physicalIfReadListEcResponseBody.getErrorCode());
        logger.error(errorMsg);
        throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);
      }

      return physicalIfReadListEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

}
