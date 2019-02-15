
package msf.fc.node.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcInternalLinkIf;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.dao.clusters.FcBreakoutIfDao;
import msf.fc.db.dao.clusters.FcInternalLinkIfDao;
import msf.fc.db.dao.clusters.FcLagIfDao;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.clusters.FcPhysicalIfDao;
import msf.fc.rest.ec.node.interfaces.data.InterfaceReadListEcResponseBody;
import msf.fc.rest.ec.node.interfaces.data.entity.InterfacesEcEntity;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.data.InterfaceInfoReadDetailListResponseBody;
import msf.mfcfc.node.interfaces.data.InterfaceInfoReadListResponseBody;
import msf.mfcfc.node.interfaces.data.InterfaceRequest;

/**
 * Implementation class for the interface information list acquisition.
 *
 * @author NTT
 *
 */
public class FcInterfaceReadListScenario extends FcAbstractInterfaceScenarioBase<InterfaceRequest> {

  private InterfaceRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcInterfaceReadListScenario.class);

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
  public FcInterfaceReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(InterfaceRequest request) throws MsfException {

    try {

      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);

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
        FcPhysicalIfDao fcPhysicalIfDao = new FcPhysicalIfDao();
        FcInternalLinkIfDao fcInternalLinkIfDao = new FcInternalLinkIfDao();
        FcLagIfDao fcLagIfDao = new FcLagIfDao();
        FcBreakoutIfDao fcBreakoutIfDao = new FcBreakoutIfDao();

        List<FcPhysicalIf> physicalIfs = fcPhysicalIfDao.readList(sessionWrapper,
            NodeType.getEnumFromPluralMessage(request.getFabricType()).getCode(),
            Integer.parseInt(request.getNodeId()));
        List<FcInternalLinkIf> internalLinkIfs = fcInternalLinkIfDao.readList(sessionWrapper,
            NodeType.getEnumFromPluralMessage(request.getFabricType()).getCode(),
            Integer.parseInt(request.getNodeId()));
        List<FcLagIf> lagIfs = fcLagIfDao.readList(sessionWrapper,
            NodeType.getEnumFromPluralMessage(request.getFabricType()).getCode(),
            Integer.parseInt(request.getNodeId()));
        List<FcBreakoutIf> breakoutIfs = fcBreakoutIfDao.readList(sessionWrapper,
            NodeType.getEnumFromPluralMessage(request.getFabricType()).getCode(),
            Integer.parseInt(request.getNodeId()));

        if ((physicalIfs.isEmpty()) && (internalLinkIfs.isEmpty()) && (lagIfs.isEmpty()) && (breakoutIfs.isEmpty())) {

          checkNode(sessionWrapper, NodeType.getEnumFromPluralMessage(request.getFabricType()).getCode(),
              Integer.parseInt(request.getNodeId()));
        }

        FcNodeDao nodeDao = new FcNodeDao();
        FcNode node = nodeDao.read(sessionWrapper, request.getFabricTypeEnum().getCode(),
            Integer.parseInt(request.getNodeId()));

        responseBase = responseInterfaceReadListData(physicalIfs, internalLinkIfs, lagIfs, breakoutIfs,
            request.getFormat(), node.getEcNodeId());

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

  protected RestResponseBase responseInterfaceReadListData(List<FcPhysicalIf> fcPhysicalIfs,
      List<FcInternalLinkIf> fcInternalLinkIfs, List<FcLagIf> fcLagIfs, List<FcBreakoutIf> fcBreakoutIfs, String format,
      Integer ecNodeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcPhysicalIf", "fcInternalLinkIfs", "fcLagIfs", "format", "ecNodeId" },
          new Object[] { fcPhysicalIfs, fcInternalLinkIfs, fcLagIfs, format, ecNodeId });
      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        InterfaceInfoReadDetailListResponseBody body = new InterfaceInfoReadDetailListResponseBody();
        if ((!fcPhysicalIfs.isEmpty()) || (!fcInternalLinkIfs.isEmpty()) || (!fcLagIfs.isEmpty())
            || (!fcBreakoutIfs.isEmpty())) {
          InterfaceReadListEcResponseBody interfaceReadListEcResponseBody = sendInterfaceReadList(ecNodeId);
          InterfacesEcEntity interfacesEcEntity = interfaceReadListEcResponseBody.getIfs();

          body.setPhysicalIfList(getPhysicalIfEntities(fcPhysicalIfs, interfacesEcEntity.getPhysicalIfList(),
              interfacesEcEntity.getBreakoutIfList()));
          body.setInternalLinkIfList(getInternalIfEntities(fcInternalLinkIfs));
          body.setLagIfList(getLagIfEntities(fcLagIfs, interfacesEcEntity.getLagIfList()));
          body.setBreakoutIfList(getBreakoutIfEntities(fcBreakoutIfs, interfacesEcEntity.getBreakoutIfList()));
        } else {

          body.setPhysicalIfList(new ArrayList<>());
          body.setInternalLinkIfList(new ArrayList<>());
          body.setLagIfList(new ArrayList<>());
          body.setBreakoutIfList(new ArrayList<>());
        }
        return createRestResponse(body, HttpStatus.OK_200);
      } else {
        InterfaceInfoReadListResponseBody body = new InterfaceInfoReadListResponseBody();
        body.setPhysicalIfIdList(getPhysicalIfIdList(fcPhysicalIfs));
        body.setInternalLinkIfIdList(getInternalIfIdList(fcInternalLinkIfs));
        body.setLagIfIdList(getLagIfIdList(fcLagIfs));
        body.setBreakoutIfIdList(getBreakoutIfIdList(fcBreakoutIfs));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

}
