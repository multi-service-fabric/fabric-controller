
package msf.fc.services.filter.scenario.filters;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.services.filter.rest.ec.filters.data.IfFilterCreateDeleteEcRequestBody;
import msf.fc.services.filter.rest.ec.filters.data.entity.FilterOperationTermEcEntity;
import msf.fc.services.filter.rest.ec.filters.data.entity.IfFilterTermEcEntity;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.services.filter.common.constant.EcRequestUri;
import msf.mfcfc.services.filter.scenario.filters.AbstractFilterRunnerBase;
import msf.mfcfc.services.filter.scenario.filters.data.entity.FilterValueEntity;

/**
 * Abstract class to implement the common process of the FC asynchronous runner
 * processing in the filter management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits RestRequestBase class
 */
public abstract class FcAbstractFilterRunnerBase extends AbstractFilterRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractFilterRunnerBase.class);

  protected static final String TERM_ID_FIRST_CHAR = "0";
  protected static final String AUTOMATIC_GENERATED_NUMBER = "Automatic Generated Number";
  protected static final int MAX_GENERATED_NUMBER = 99999;

  protected RestResponseBase createRestResponse(AbstractResponseBody body, int statusCode) {
    try {
      logger.methodStart(new String[] { "body", "statusCode" },
          new Object[] { ToStringBuilder.reflectionToString(body), statusCode });

      RestResponseBase response = new RestResponseBase(statusCode, body);
      return response;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<String> getTermIds(List<String> pathList, boolean isCreate) throws MsfException {
    try {
      logger.methodStart();

      List<String> termIds = new ArrayList<>();
      for (String path : pathList) {
        if ((path != null) && (!path.isEmpty())) {
          String termId = path;
          if ((isCreate) && (!termId.startsWith(TERM_ID_FIRST_CHAR))) {

            termId = TERM_ID_FIRST_CHAR + termId;
          }
          if (termIds.contains(termId)) {

            throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR,
                MessageFormat.format("There is the same term Id information. termId = {0}.", termId));
          }
          termIds.add(termId);
        } else {

          termIds.add(AUTOMATIC_GENERATED_NUMBER);
        }
      }
      return termIds;
    } finally {
      logger.methodEnd();
    }
  }

  protected int getNextTermId(TreeSet<String> alreadyGeneratedIds, int generatedCandidateId) throws MsfException {
    try {
      logger.methodStart(new String[] { "alreadyGeneratedIds", "generatedCandidateId" },
          new Object[] { alreadyGeneratedIds, generatedCandidateId });
      do {

        if (generatedCandidateId > MAX_GENERATED_NUMBER) {

          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "There is no ID to be assined.");
        }
        if (alreadyGeneratedIds.contains(String.format("%06d", generatedCandidateId))) {

          generatedCandidateId++;
        } else {

          return generatedCandidateId;
        }
      } while (true);

    } finally {
      logger.methodEnd();
    }
  }

  protected FilterOperationTermEcEntity createFilterOperationTermEcEntity(String filterOperation, String termName,
      FilterValueEntity filterValueEntity) {
    try {
      logger.methodStart(new String[] { "filterOperation", "termName", "filterValueEntity" },
          new Object[] { filterOperation, termName, filterValueEntity });

      FilterOperationTermEcEntity ecEntity = new FilterOperationTermEcEntity();
      ecEntity.setOperation(filterOperation);
      ecEntity.setTermName(termName);
      if (filterValueEntity != null) {
        ecEntity.setAction(filterValueEntity.getAction());
        ecEntity.setDirection(filterValueEntity.getDirection());
        ecEntity.setSourceMacAddress(filterValueEntity.getSourceMacAddress());
        ecEntity.setDestMacAddress(filterValueEntity.getDestMacAddress());
        ecEntity.setSourceIpAddress(filterValueEntity.getSourceIpAddress());
        ecEntity.setDestIpAddress(filterValueEntity.getDestIpAddress());
        ecEntity.setProtocol(filterValueEntity.getProtocol());
        ecEntity.setSourcePort(filterValueEntity.getSourcePort());
        ecEntity.setDestPort(filterValueEntity.getDestPort());
      }
      return ecEntity;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase sendFilterCreateDelete(Integer ecNodeId, String ifType, String ifId,
      List<FilterOperationTermEcEntity> filterOperationTermEcEntities) throws MsfException {
    try {
      logger.methodStart();

      IfFilterCreateDeleteEcRequestBody ifFilterCreateDeleteEcRequestBody = new IfFilterCreateDeleteEcRequestBody();
      IfFilterTermEcEntity ifFilter = new IfFilterTermEcEntity();
      ifFilter.setTerms(filterOperationTermEcEntities);
      ifFilterCreateDeleteEcRequestBody.setIfFilter(ifFilter);
      RestRequestBase restRequest = new RestRequestBase();

      restRequest.setRequestBody(JsonUtil.toJson(ifFilterCreateDeleteEcRequestBody));

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();

      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.FILTER_CREATE_DELETE.getHttpMethod(),
          EcRequestUri.FILTER_CREATE_DELETE.getUri(String.valueOf(ecNodeId), ifType, ifId), restRequest,
          ecControlIpAddress, ecControlPort);

      String errorCode = null;
      if (StringUtils.isNotEmpty(restResponseBase.getResponseBody())) {
        ErrorInternalResponseBody filterCreateDeleteEcResponseBody = JsonUtil
            .fromJson(restResponseBase.getResponseBody(), ErrorInternalResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        errorCode = filterCreateDeleteEcResponseBody.getErrorCode();
      }

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200, errorCode,
          ErrorCode.EC_CONTROL_ERROR);

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase responseFilterDeleteData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.NO_CONTENT_204, (String) null);
    } finally {
      logger.methodEnd();
    }
  }
}
