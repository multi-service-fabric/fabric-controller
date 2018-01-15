
package msf.mfcfc.slice.cps.l3cp.data;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.AddressType;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.PatchOperation;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.rest.common.RestRequestValidator;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpStaticRouteEntity;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpValueEntity;


public class L3CpCreateDeleteRequestBody implements RestRequestValidator {

  
  private static final MsfLogger logger = MsfLogger.getInstance(L3CpCreateDeleteAsyncResponseBody.class);

  
  @SerializedName("op")
  private String op;

  
  @SerializedName("path")
  private String path;

  
  @SerializedName("value")
  private L3CpValueEntity value;

  
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

  
  public L3CpValueEntity getValue() {
    return value;
  }

  
  public void setValue(L3CpValueEntity value) {
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


    ParameterCheckUtil.checkPatchPath(path, true);

    if (value != null) {
      String logMsg = "value is not null.";
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    }
  }

  private void validateAddOp() throws MsfException {


    ParameterCheckUtil.checkPatchPath(path, false);

    ParameterCheckUtil.checkNotNull(value);

    validateAddValue();
  }

  private void validateAddValue() throws MsfException {


    ParameterCheckUtil.checkNotNullAndLength(value.getClusterId());

    ParameterCheckUtil.checkNotNullAndLength(value.getEdgePointId());

    ParameterCheckUtil.checkNotNull(value.getVlanId());
    ParameterCheckUtil.checkNumberRange(value.getVlanId(), 0, 4096);

    ParameterCheckUtil.checkNotNull(value.getMtu());


    if (value.getIpv4Address() != null) {
      value.setIpv4Address(ParameterCheckUtil.checkIpv4Address(value.getIpv4Address()));
    }

    if (value.getIpv6Address() != null) {
      value.setIpv6Address(ParameterCheckUtil.checkIpv6Address(value.getIpv6Address()));
    }

    if (value.getIpv4Address() == null && value.getIpv6Address() == null) {
      String logMsg = "not set both IPv4 and IPv6.";
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    }

    if (value.getIpv4Address() != null) {
      ParameterCheckUtil.checkNotNull(value.getIpv4Prefix());

      ParameterCheckUtil.checkNumberRange(value.getIpv4Prefix(), 0, 31);
    } else {
      value.setIpv4Prefix(null);
    }

    if (value.getIpv6Address() != null) {
      ParameterCheckUtil.checkNotNull(value.getIpv6Prefix());

      ParameterCheckUtil.checkNumberRange(value.getIpv6Prefix(), 0, 64);
    } else {
      value.setIpv6Prefix(null);
    }

    if (value.getBgp() != null) {
      validateBgp();
    }

    if (value.getStaticRouteList() != null && value.getStaticRouteList().size() > 0) {
      validateStaticRouteList();
    }

    if (value.getVrrp() != null) {
      validateVrrp();
    }


  }

  private void validateBgp() throws MsfException {


    ParameterCheckUtil.checkNotNull(value.getBgp().getRoleEnum());

    ParameterCheckUtil.checkNotNull(value.getBgp().getNeighborAs());

    if (value.getIpv4Address() != null) {
      value.getBgp()
          .setNeighborIpv4Address(ParameterCheckUtil.checkIpv4Address(value.getBgp().getNeighborIpv4Address()));
    } else {
      value.getBgp().setNeighborIpv4Address(null);
    }

    if (value.getIpv6Address() != null) {
      value.getBgp()
          .setNeighborIpv6Address(ParameterCheckUtil.checkIpv6Address(value.getBgp().getNeighborIpv6Address()));
    } else {
      value.getBgp().setNeighborIpv6Address(null);
    }
  }

  private void validateStaticRouteList() throws MsfException {

    for (L3CpStaticRouteEntity staticRoute : value.getStaticRouteList()) {
      validateStaticRoute(staticRoute);
    }
  }

  private void validateStaticRoute(L3CpStaticRouteEntity staticRoute) throws MsfException {


    ParameterCheckUtil.checkNotNull(staticRoute.getAddrTypeEnum());

    if (AddressType.IPV4.equals(staticRoute.getAddrTypeEnum())) {
      staticRoute.setAddress(ParameterCheckUtil.checkIpv4Address(staticRoute.getAddress()));

      ParameterCheckUtil.checkNotNull(staticRoute.getPrefix());
      ParameterCheckUtil.checkNumberRange(staticRoute.getPrefix(), 0, 32);

      staticRoute.setNextHop(ParameterCheckUtil.checkIpv4Address(staticRoute.getNextHop()));
    } else {
      staticRoute.setAddress(ParameterCheckUtil.checkIpv6Address(staticRoute.getAddress()));

      ParameterCheckUtil.checkNotNull(staticRoute.getPrefix());
      ParameterCheckUtil.checkNumberRange(staticRoute.getPrefix(), 0, 128);

      staticRoute.setNextHop(ParameterCheckUtil.checkIpv6Address(staticRoute.getNextHop()));
    }
  }

  private void validateVrrp() throws MsfException {

    ParameterCheckUtil.checkNotNull(value.getVrrp().getGroupId());

    ParameterCheckUtil.checkNotNull(value.getVrrp().getRoleEnum());

    if (value.getIpv4Address() != null) {
      value.getVrrp()
          .setVirtualIpv4Address(ParameterCheckUtil.checkIpv4Address(value.getVrrp().getVirtualIpv4Address()));
    } else {
      value.getVrrp().setVirtualIpv4Address(null);
    }
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
