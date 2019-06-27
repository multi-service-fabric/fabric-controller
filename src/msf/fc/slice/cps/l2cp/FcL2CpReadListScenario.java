
package msf.fc.slice.cps.l2cp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcL2Cp;
import msf.fc.common.data.FcL2Slice;
import msf.fc.db.dao.slices.FcL2CpDao;
import msf.fc.db.dao.slices.FcL2SliceDao;
import msf.fc.rest.ec.node.interfaces.vlan.data.VlanIfReadListEcResponseBody;
import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfEcEntity;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.cps.l2cp.data.L2CpReadDetailListResponseBody;
import msf.mfcfc.slice.cps.l2cp.data.L2CpReadListResponseBody;
import msf.mfcfc.slice.cps.l2cp.data.L2CpRequest;
import msf.mfcfc.slice.cps.l2cp.data.entity.L2CpEntity;

/**
 * Implementation class for the L2CP information list acquisition.
 *
 * @author NTT
 *
 */
public class FcL2CpReadListScenario extends FcAbstractL2CpScenarioBase<L2CpRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL2CpReadListScenario.class);

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
  public FcL2CpReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      sessionWrapper.openSession();
      List<FcL2Cp> l2CpList = getL2CpListFromDb(sessionWrapper, request.getSliceId());

      if (l2CpList.size() == 0) {
        FcL2SliceDao l2SliceDao = new FcL2SliceDao();
        FcL2Slice l2Slice = l2SliceDao.read(sessionWrapper, request.getSliceId());

        ParameterCheckUtil.checkNotNullRelatedResource(l2Slice, new String[] { "sliceId" },
            new Object[] { request.getSliceId() });
      }

      return createResponse(sessionWrapper, l2CpList, request.getFormatEnum());
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  @Override
  protected void checkParameter(L2CpRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      checkFormatOption(request.getFormat());

      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  private List<FcL2Cp> getL2CpListFromDb(SessionWrapper sessionWrapper, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "sliceId" }, new Object[] { sessionWrapper, sliceId });
      FcL2CpDao l2CpDao = new FcL2CpDao();
      return l2CpDao.readListBySliceId(sessionWrapper, sliceId);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(SessionWrapper sessionWrapper, List<FcL2Cp> l2CpList,
      RestFormatOption formatOption) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2CpList", "formatOption" }, new Object[] { l2CpList, formatOption });
      if (RestFormatOption.DETAIL_LIST.equals(formatOption)) {
        RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200,
            createL2CpDetailReadResponseBody(sessionWrapper, l2CpList));
        return restResponse;
      } else {
        RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, createL2CpReadResponseBody(l2CpList));
        return restResponse;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private L2CpReadDetailListResponseBody createL2CpDetailReadResponseBody(SessionWrapper sessionWrapper,
      List<FcL2Cp> l2CpList) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2CpList" }, new Object[] { l2CpList });
      List<L2CpEntity> l2CpEntityList = new ArrayList<>();

      Set<Long> nodeInfoIdSet = new HashSet<>();
      Map<Long, VlanIfReadListEcResponseBody> vlanIfListMap = new HashMap<>();
      for (FcL2Cp l2Cp : l2CpList) {
        nodeInfoIdSet.add(l2Cp.getVlanIf().getId().getNodeInfoId());
      }
      for (Long nodeInfoId : nodeInfoIdSet) {

        VlanIfReadListEcResponseBody vlanIfResponseBody = getVlanIfList(sessionWrapper, nodeInfoId);
        vlanIfListMap.put(nodeInfoId, vlanIfResponseBody);
      }
      for (FcL2Cp l2Cp : l2CpList) {

        VlanIfReadListEcResponseBody vlanIfListBody = vlanIfListMap.get(l2Cp.getVlanIf().getId().getNodeInfoId());

        if (vlanIfListBody.getVlanIfList() != null) {
          for (VlanIfEcEntity entity : vlanIfListBody.getVlanIfList()) {
            if (entity.getVlanIfId().equals(String.valueOf(l2Cp.getVlanIf().getId().getVlanIfId()))) {
              L2CpEntity l2CpEntity = createL2CpEntity(sessionWrapper, l2Cp, entity);
              l2CpEntityList.add(l2CpEntity);
              break;
            }
          }
        }
      }

      L2CpReadDetailListResponseBody responseBody = new L2CpReadDetailListResponseBody();
      responseBody.setL2CpList(l2CpEntityList);
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private L2CpReadListResponseBody createL2CpReadResponseBody(List<FcL2Cp> l2CpList) {
    try {
      logger.methodStart(new String[] { "l2CpList" }, new Object[] { l2CpList });
      List<String> l2CpIdList = new ArrayList<>();
      for (FcL2Cp l2Cp : l2CpList) {
        l2CpIdList.add(l2Cp.getId().getCpId());
      }
      L2CpReadListResponseBody responseBody = new L2CpReadListResponseBody();
      responseBody.setL2CpIdList(l2CpIdList);
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }
}
