
package msf.mfcfc.node.clusters.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.node.clusters.data.entity.SwClusterClusterEntity;
import msf.mfcfc.rest.common.RestRequestValidator;

public class SwClusterCreateRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(SwClusterCreateRequestBody.class);

  @SerializedName("cluster")
  private SwClusterClusterEntity cluster;

  public SwClusterClusterEntity getCluster() {
    return cluster;
  }

  public void setCluster(SwClusterClusterEntity cluster) {
    this.cluster = cluster;
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();

      ParameterCheckUtil.checkNotNull(cluster);

      ParameterCheckUtil.checkNumericId(cluster.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
    } finally {
      logger.methodEnd();
    }

  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
