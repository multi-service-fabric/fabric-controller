
package msf.fc.slice.cps.l3cp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcL3Cp;
import msf.fc.common.data.FcL3Slice;
import msf.fc.db.dao.slices.FcL3CpDao;
import msf.fc.db.dao.slices.FcL3SliceDao;
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
import msf.mfcfc.slice.cps.l3cp.data.L3CpReadDetailListResponseBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpReadListResponseBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpEntity;

/**
 * Implementation class for L3CP information list acquisition.
 *
 * @author NTT
 *
 */
public class FcL3CpReadListScenario extends FcAbstractL3CpScenarioBase<L3CpRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL3CpReadListScenario.class);

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
  public FcL3CpReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
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
      List<FcL3Cp> l3CpList = getL3CpListFromDb(sessionWrapper, request.getSliceId());

      if (l3CpList.size() == 0) {
        FcL3SliceDao l3SliceDao = new FcL3SliceDao();
        FcL3Slice l3Slice = l3SliceDao.read(sessionWrapper, request.getSliceId());

        ParameterCheckUtil.checkNotNullRelatedResource(l3Slice, new String[] { "sliceId" },
            new Object[] { request.getSliceId() });
      }

      return createResponse(sessionWrapper, l3CpList, request.getFormatEnum());
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  @Override
  protected void checkParameter(L3CpRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      checkFormatOption(request.getFormat());

      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  private List<FcL3Cp> getL3CpListFromDb(SessionWrapper sessionWrapper, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "sliceId" }, new Object[] { sessionWrapper, sliceId });
      FcL3CpDao l3CpDao = new FcL3CpDao();
      return l3CpDao.readListBySliceId(sessionWrapper, sliceId);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(SessionWrapper sessionWrapper, List<FcL3Cp> l3CpList,
      RestFormatOption formatOption) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3CpList", "formatOption" }, new Object[] { l3CpList, formatOption });
      if (RestFormatOption.DETAIL_LIST.equals(formatOption)) {
        RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200,
            createL3CpDetailReadResponseBody(sessionWrapper, l3CpList));
        return restResponse;
      } else {
        RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, createL3CpReadResponseBody(l3CpList));
        return restResponse;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private L3CpReadDetailListResponseBody createL3CpDetailReadResponseBody(SessionWrapper sessionWrapper,
      List<FcL3Cp> l3CpList) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3CpList" }, new Object[] { l3CpList });
      List<L3CpEntity> l3CpEntityList = new ArrayList<>();

      Set<Integer> nodeInfoIdSet = new HashSet<>();
      Map<Integer, VlanIfReadListEcResponseBody> vlanIfListMap = new HashMap<>();
      for (FcL3Cp l3Cp : l3CpList) {
        nodeInfoIdSet.add(l3Cp.getVlanIf().getId().getNodeInfoId());
      }
      for (Integer nodeInfoId : nodeInfoIdSet) {

        VlanIfReadListEcResponseBody vlanIfResponseBody = getVlanIfList(sessionWrapper, nodeInfoId.longValue());
        vlanIfListMap.put(nodeInfoId, vlanIfResponseBody);
      }
      for (FcL3Cp l3Cp : l3CpList) {

        VlanIfReadListEcResponseBody vlanIfListBody = vlanIfListMap.get(l3Cp.getVlanIf().getId().getNodeInfoId());

        if (vlanIfListBody.getVlanIfList() != null) {
          for (VlanIfEcEntity entity : vlanIfListBody.getVlanIfList()) {
            if (entity.getVlanIfId().equals(String.valueOf(l3Cp.getVlanIf().getId().getVlanIfId()))) {
              L3CpEntity l3CpEntity = createL3CpEntity(sessionWrapper, l3Cp, entity);
              l3CpEntityList.add(l3CpEntity);
              break;
            }
          }
        }
      }
      L3CpReadDetailListResponseBody responseBody = new L3CpReadDetailListResponseBody();
      responseBody.setL3CpList(l3CpEntityList);
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private L3CpReadListResponseBody createL3CpReadResponseBody(List<FcL3Cp> l3CpList) {
    try {
      logger.methodStart(new String[] { "l3CpList" }, new Object[] { l3CpList });
      List<String> l3CpIdList = new ArrayList<>();
      for (FcL3Cp l3Cp : l3CpList) {
        l3CpIdList.add(l3Cp.getId().getCpId());
      }
      L3CpReadListResponseBody responseBody = new L3CpReadListResponseBody();
      responseBody.setL3CpIdList(l3CpIdList);
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }
}
