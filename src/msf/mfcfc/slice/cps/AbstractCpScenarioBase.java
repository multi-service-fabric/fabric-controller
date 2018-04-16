
package msf.mfcfc.slice.cps;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.PatchOperation;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.AbstractScenario;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.cps.l2cp.data.L2CpCreateDeleteRequestBody;
import msf.mfcfc.slice.cps.l2cp.data.L2CpCreateRequestBody;
import msf.mfcfc.slice.cps.l2cp.data.L2CpRequest;
import msf.mfcfc.slice.cps.l3cp.data.L3CpCreateDeleteRequestBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpCreateRequestBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;
import msf.mfcfc.slice.cps.l3cp.data.L3CpStaticRouteCreateDeleteRequestBody;

/**
 * Abstract class to implement the common process of CP-related processing in
 * slice management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class AbstractCpScenarioBase<T extends RestRequestBase> extends AbstractScenario<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractCpScenarioBase.class);

  protected void checkFormatOption(String format) throws MsfException {
    try {
      logger.methodStart(new String[] { "format" }, new Object[] { format });

      if (format == null) {
        return;
      }
      RestFormatOption formatEnum = RestFormatOption.getEnumFromMessage(format);
      if (formatEnum == null) {
        String logMsg = MessageFormat.format("param is undefined.param = {0}, value = {1}", "format", format);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected List<L2CpCreateDeleteRequestBody> checkParameterL2CpCreateDelete(L2CpRequest request) throws MsfException {

    ParameterCheckUtil.checkIpv4Address(request.getNotificationAddress());
    ParameterCheckUtil.checkPortNumber(request.getNotificationPort());

    List<L2CpCreateDeleteRequestBody> requestBody = (List<L2CpCreateDeleteRequestBody>) JsonUtil
        .fromJson(request.getRequestBody(), new TypeToken<ArrayList<L2CpCreateDeleteRequestBody>>() {
        });
    ParameterCheckUtil.checkNotNullAndLength(requestBody);
    List<PatchOperation> opList = new ArrayList<>();
    for (L2CpCreateDeleteRequestBody body : requestBody) {
      body.validate();
      opList.add(body.getOpEnum());
    }
    ParameterCheckUtil.checkPatchOperationMix(opList);
    logger.debug("requestBody=" + request.getRequestBody());
    return requestBody;
  }

  protected L2CpCreateRequestBody checkParameterL2CpCreate(L2CpRequest request) throws MsfException {

    ParameterCheckUtil.checkIpv4Address(request.getNotificationAddress());
    ParameterCheckUtil.checkPortNumber(request.getNotificationPort());

    L2CpCreateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(), L2CpCreateRequestBody.class);
    requestBody.validate();
    logger.debug("requestBody=" + request.getRequestBody());
    return requestBody;
  }

  protected List<L3CpCreateDeleteRequestBody> checkParameterL3CpCreateDelete(L3CpRequest request) throws MsfException {

    ParameterCheckUtil.checkIpv4Address(request.getNotificationAddress());
    ParameterCheckUtil.checkPortNumber(request.getNotificationPort());

    List<L3CpCreateDeleteRequestBody> requestBody = (List<L3CpCreateDeleteRequestBody>) JsonUtil
        .fromJson(request.getRequestBody(), new TypeToken<ArrayList<L3CpCreateDeleteRequestBody>>() {
        });
    ParameterCheckUtil.checkNotNullAndLength(requestBody);
    List<PatchOperation> opList = new ArrayList<>();
    for (L3CpCreateDeleteRequestBody body : requestBody) {
      body.validate();
      opList.add(body.getOpEnum());
    }
    ParameterCheckUtil.checkPatchOperationMix(opList);
    logger.debug("requestBody=" + request.getRequestBody());
    return requestBody;
  }

  protected L3CpCreateRequestBody checkParameterL3CpCreate(L3CpRequest request) throws MsfException {

    ParameterCheckUtil.checkIpv4Address(request.getNotificationAddress());
    ParameterCheckUtil.checkPortNumber(request.getNotificationPort());

    L3CpCreateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(), L3CpCreateRequestBody.class);
    requestBody.validate();
    logger.debug("requestBody=" + request.getRequestBody());
    return requestBody;

  }

  protected List<L3CpStaticRouteCreateDeleteRequestBody> checkParameterL3CpStaticRouteCreateDelete(L3CpRequest request)
      throws MsfException {

    ParameterCheckUtil.checkIpv4Address(request.getNotificationAddress());
    ParameterCheckUtil.checkPortNumber(request.getNotificationPort());

    List<L3CpStaticRouteCreateDeleteRequestBody> requestBody = (List<L3CpStaticRouteCreateDeleteRequestBody>) JsonUtil
        .fromJson(request.getRequestBody(), new TypeToken<ArrayList<L3CpStaticRouteCreateDeleteRequestBody>>() {
        });
    ParameterCheckUtil.checkNotNullAndLength(requestBody);
    List<PatchOperation> opList = new ArrayList<>();
    for (L3CpStaticRouteCreateDeleteRequestBody body : requestBody) {
      body.validate();
      opList.add(body.getOpEnum());
    }
    ParameterCheckUtil.checkPatchOperationMix(opList);
    logger.debug("requestBody=" + request.getRequestBody());
    return requestBody;
  }

}
