
package msf.mfcfc.slice.cps.l3cp.data;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.PatchOperation;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.rest.common.RestRequestValidator;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpStaticRouteValueEntity;

public class L3CpStaticRouteCreateDeleteRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(L3CpStaticRouteCreateDeleteRequestBody.class);

  private static final Pattern STATIC_ROUTE_ID_PATTERN = Pattern
      .compile("/static_routes/ipv[4|6]_([a-fA-F0-9\\.:]+)_[0-9]+_([a-fA-F0-9\\.:]+)$");

  @SerializedName("op")
  private String op;

  @SerializedName("path")
  private String path;

  @SerializedName("value")
  private L3CpStaticRouteValueEntity value;

  public String getOp() {
    return op;
  }

  public void setOp(String op) {
    this.op = op;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public L3CpStaticRouteValueEntity getValue() {
    return value;
  }

  public void setValue(L3CpStaticRouteValueEntity value) {
    this.value = value;
  }

  public PatchOperation getOpEnum() {
    return PatchOperation.getEnumFromMessage(op);
  }

  public void setOpEnum(PatchOperation op) {
    this.op = op.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();

      ParameterCheckUtil.checkNotNull(getOpEnum());

      ParameterCheckUtil.checkNotNullAndLength(path);

      switch (getOpEnum()) {
        case ADD:
          validateAddOp();
          break;
        case REMOVE:
          validateRemoveOp();
          break;
        case REPLACE:
        case TEST:
        case COPY:
        case MOVE:
          String logMsg = "op is not set add or remove.";
          logger.error(logMsg);
          throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
        default:

          throw new IllegalArgumentException(MessageFormat.format("op={0}", getOpEnum()));
      }
    } finally {
      logger.methodEnd();
    }

  }

  private void validateRemoveOp() throws MsfException {

    Matcher matcher = STATIC_ROUTE_ID_PATTERN.matcher(path);
    if (!matcher.matches()) {
      String logMsg = MessageFormat.format("path param is not match the pattern . path = {0}", path);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    } else {

      String ipAddress = matcher.group(1);
      ParameterCheckUtil.checkIpAddress(ipAddress);
      String nextHop = matcher.group(2);
      ParameterCheckUtil.checkIpAddress(nextHop);
    }

    if (value != null) {
      String logMsg = "value is not null.";
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    }
  }

  private void validateAddOp() throws MsfException {

    if (!path.matches("/static_routes$")) {
      String logMsg = MessageFormat.format("path param is not match the pattern . path = {0}", path);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    }

    ParameterCheckUtil.checkNotNull(value);

    validateValue();
  }

  private void validateValue() throws MsfException {

    ParameterCheckUtil.checkNotNull(value.getStaticRoute());

    validateStaticRoute();
  }

  private void validateStaticRoute() throws MsfException {

    ParameterCheckUtil.checkNotNull(value.getStaticRoute().getAddrTypeEnum());

    switch (value.getStaticRoute().getAddrTypeEnum()) {
      case IPV4:

        value.getStaticRoute().setAddress(ParameterCheckUtil.checkIpv4Address(value.getStaticRoute().getAddress()));

        ParameterCheckUtil.checkNotNull(value.getStaticRoute().getPrefix());
        ParameterCheckUtil.checkNumberRange(value.getStaticRoute().getPrefix(), 0, 32);

        value.getStaticRoute().setNextHop(ParameterCheckUtil.checkIpv4Address(value.getStaticRoute().getNextHop()));
        break;
      case IPV6:

        value.getStaticRoute().setAddress(ParameterCheckUtil.checkIpv6Address(value.getStaticRoute().getAddress()));

        ParameterCheckUtil.checkNotNull(value.getStaticRoute().getPrefix());
        ParameterCheckUtil.checkNumberRange(value.getStaticRoute().getPrefix(), 0, 128);

        value.getStaticRoute().setNextHop(ParameterCheckUtil.checkIpv6Address(value.getStaticRoute().getNextHop()));
        break;
      default:

        throw new IllegalArgumentException(
            MessageFormat.format("addrType={0}", value.getStaticRoute().getAddrTypeEnum()));
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
