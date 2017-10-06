package msf.fc.node.interfaces;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.EcRequestUri;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.NodeType;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.RestFormatOption;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.InternalLinkIf;
import msf.fc.common.data.LagIf;
import msf.fc.common.data.PhysicalIf;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.InternalLinkIfDao;
import msf.fc.db.dao.clusters.LagIfDao;
import msf.fc.db.dao.clusters.PhysicalIfDao;
import msf.fc.node.interfaces.data.InterfaceInfoReadDetailListResponseBody;
import msf.fc.node.interfaces.data.InterfaceInfoReadListResponseBody;
import msf.fc.node.interfaces.data.InterfaceRequest;
import msf.fc.rest.common.JsonUtil;
import msf.fc.rest.common.RestClient;
import msf.fc.rest.ec.node.interfaces.data.InterfaceReadListEcResponseBody;
import msf.fc.rest.ec.node.interfaces.data.entity.InterfacesEcEntity;

public class InterfaceReadListScenario extends AbstractInterfaceScenarioBase<InterfaceRequest> {

  private InterfaceRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(InterfaceReadListScenario.class);

  public InterfaceReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(InterfaceRequest request) throws MsfException {

    try {

      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNotNull(NodeType.getEnumFromPluralMessage(request.getFabricType()));

      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);

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
        InternalLinkIfDao internalLinkIfDao = new InternalLinkIfDao();
        LagIfDao lagIfDao = new LagIfDao();
        List<PhysicalIf> physicalIfs = physicalIfDao.readList(sessionWrapper, request.getClusterId(),
            request.getFabricTypeEnum().getCode(), Integer.parseInt(request.getNodeId()));
        List<InternalLinkIf> internalLinkIfs = internalLinkIfDao.readList(sessionWrapper, request.getClusterId(),
            request.getFabricTypeEnum().getCode(), Integer.parseInt(request.getNodeId()));
        List<LagIf> lagIfs = lagIfDao.readList(sessionWrapper, request.getClusterId(),
            request.getFabricTypeEnum().getCode(), Integer.parseInt(request.getNodeId()));

        if ((physicalIfs.isEmpty()) && (internalLinkIfs.isEmpty()) && (lagIfs.isEmpty())) {
          checkNode(sessionWrapper, request.getClusterId(), request.getFabricTypeEnum().getCode(),
              Integer.parseInt(request.getNodeId()));
        }

        responseBase = responseInterfaceReadListData(physicalIfs, internalLinkIfs, lagIfs, request.getFormat());

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

  private RestResponseBase responseInterfaceReadListData(List<PhysicalIf> physicalIfs,
      List<InternalLinkIf> internalLinkIfs, List<LagIf> lagIfs, String format) throws MsfException {
    try {
      logger.methodStart();
      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        InterfaceInfoReadDetailListResponseBody body = new InterfaceInfoReadDetailListResponseBody();
        if ((!physicalIfs.isEmpty()) || (!internalLinkIfs.isEmpty()) || (!lagIfs.isEmpty())) {
          InterfaceReadListEcResponseBody interfaceReadListEcResponseBody = sendInterfaceReadList();
          InterfacesEcEntity interfacesEcEntity = interfaceReadListEcResponseBody.getInterfacesEcEntity();
          body.setPhysicalIfList(getPhysicalIfEntities(physicalIfs, interfacesEcEntity.getPhysicalIfList()));
          body.setInternalIfList(getInternalIfEntities(internalLinkIfs));
          body.setLagIfList(getLagIfEntities(lagIfs, interfacesEcEntity.getLagIf()));
        } else {
          body.setPhysicalIfList(new ArrayList<>());
          body.setInternalIfList(new ArrayList<>());
          body.setLagIfList(new ArrayList<>());
        }
        return createRestResponse(body, HttpStatus.OK_200);
      } else {
        InterfaceInfoReadListResponseBody body = new InterfaceInfoReadListResponseBody();
        body.setPhysicalIfIdList(getPhysicalIfIdList(physicalIfs));
        body.setInternalIfIdList(getInternalIfIdList(internalLinkIfs));
        body.setLagIfIdList(getLagIfIdList(lagIfs));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private InterfaceReadListEcResponseBody sendInterfaceReadList() throws MsfException {
    try {
      logger.methodStart();
      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.IF_READ_LIST.getHttpMethod(),
          EcRequestUri.IF_READ_LIST.getUri(request.getFabricType(), request.getNodeId()), null);

      InterfaceReadListEcResponseBody interfaceReadListEcResponseBody = new InterfaceReadListEcResponseBody();

      interfaceReadListEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          InterfaceReadListEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      if (restResponseBase.getHttpStatusCode() != HttpStatus.OK_200) {
        String errorMsg = MessageFormat.format("HttpStatusCode = {0}, ErrorCode = {1}",
            restResponseBase.getHttpStatusCode(), interfaceReadListEcResponseBody.getErrorCode());
        logger.error(errorMsg);
        throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);
      }

      return interfaceReadListEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }
}
