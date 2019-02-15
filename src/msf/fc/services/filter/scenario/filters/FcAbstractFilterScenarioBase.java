
package msf.fc.services.filter.scenario.filters;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.data.FcCpFilterInfo;
import msf.fc.common.data.FcLagIfFilterInfo;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIfFilterInfo;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.services.filter.rest.ec.filters.data.entity.FilterTermEcEntity;
import msf.fc.services.filter.rest.ec.filters.data.entity.LagIfFilterDetailEcEntity;
import msf.fc.services.filter.rest.ec.filters.data.entity.PhysicalIfFilterDetailEcEntity;
import msf.fc.services.filter.rest.ec.filters.data.entity.VlanIfFilterDetailEcEntity;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.services.filter.scenario.filters.AbstractFilterScenarioBase;
import msf.mfcfc.services.filter.scenario.filters.data.entity.FilterTermEntity;

/**
 * Abstract class to implement the common process of FC in the filter management
 * function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits RestRequestBase class
 */
public abstract class FcAbstractFilterScenarioBase<T extends RestRequestBase> extends AbstractFilterScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractFilterScenarioBase.class);

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

  protected List<FilterTermEntity> getL2CpFilterTermEntities(List<FcCpFilterInfo> cpFilters,
      VlanIfFilterDetailEcEntity vlanIfEcFilter) throws MsfException {
    try {
      logger.methodStart();

      List<FilterTermEntity> terms = new ArrayList<>();
      boolean isExist;
      for (FcCpFilterInfo fcL2CpFilterInfo : cpFilters) {
        isExist = false;
        if (vlanIfEcFilter.getTerms() != null) {
          for (FilterTermEcEntity filterTermEcEntity : vlanIfEcFilter.getTerms()) {
            if (fcL2CpFilterInfo.getId().getTermId().equals(filterTermEcEntity.getTermName())) {
              terms.add(getFilterTermData(filterTermEcEntity));
              isExist = true;
              break;
            }
          }
        }
        if (!isExist) {

          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
              "There is no appropriate data in the EC system.");
        }
      }
      int ecDataSize = 0;
      if (vlanIfEcFilter.getTerms() != null) {
        ecDataSize = vlanIfEcFilter.getTerms().size();
      }
      if (ecDataSize != terms.size()) {

        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, "There is no appropriate data in the FC system.");
      }
      return terms;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<FilterTermEntity> getPhysicalIfFilterTermEntities(List<FcPhysicalIfFilterInfo> physicalIfFilters,
      PhysicalIfFilterDetailEcEntity physicalIfEcFilter) throws MsfException {
    try {
      logger.methodStart();

      List<FilterTermEntity> terms = new ArrayList<>();
      boolean isExist;
      for (FcPhysicalIfFilterInfo fcPhysicalIfFilterInfo : physicalIfFilters) {
        isExist = false;
        if (physicalIfEcFilter.getTerms() != null) {
          for (FilterTermEcEntity filterTermEcEntity : physicalIfEcFilter.getTerms()) {
            if (fcPhysicalIfFilterInfo.getId().getTermId().equals(filterTermEcEntity.getTermName())) {
              terms.add(getFilterTermData(filterTermEcEntity));
              isExist = true;
              break;
            }
          }
        }
        if (!isExist) {

          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
              "There is no appropriate data in the EC system.");
        }
      }
      int ecDataSize = 0;
      if (physicalIfEcFilter.getTerms() != null) {
        ecDataSize = physicalIfEcFilter.getTerms().size();
      }
      if (ecDataSize != terms.size()) {

        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, "There is no appropriate data in the FC system.");
      }
      return terms;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<FilterTermEntity> getLagIfFilterTermEntities(List<FcLagIfFilterInfo> lagIfFilters,
      LagIfFilterDetailEcEntity lagIfEcFilter) throws MsfException {
    try {
      logger.methodStart();

      List<FilterTermEntity> terms = new ArrayList<>();
      boolean isExist;
      for (FcLagIfFilterInfo fcLagIfFilterInfo : lagIfFilters) {
        isExist = false;
        if (lagIfEcFilter.getTerms() != null) {
          for (FilterTermEcEntity filterTermEcEntity : lagIfEcFilter.getTerms()) {
            if (fcLagIfFilterInfo.getId().getTermId().equals(filterTermEcEntity.getTermName())) {
              terms.add(getFilterTermData(filterTermEcEntity));
              isExist = true;
              break;
            }
          }
        }
        if (!isExist) {

          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
              "There is no appropriate data in the EC system.");
        }
      }
      int ecDataSize = 0;
      if (lagIfEcFilter.getTerms() != null) {
        ecDataSize = lagIfEcFilter.getTerms().size();
      }
      if (ecDataSize != terms.size()) {

        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, "There is no appropriate data in the FC system.");
      }
      return terms;
    } finally {
      logger.methodEnd();
    }
  }

  private FilterTermEntity getFilterTermData(FilterTermEcEntity filterTermEcEntity) {
    try {
      logger.methodStart();
      FilterTermEntity filterTermEntity = new FilterTermEntity();
      filterTermEntity.setTermId(filterTermEcEntity.getTermName());
      filterTermEntity.setAction(filterTermEcEntity.getAction());
      filterTermEntity.setDirection(filterTermEcEntity.getDirection());
      filterTermEntity.setSourceMacAddress(filterTermEcEntity.getSourceMacAddress());
      filterTermEntity.setDestMacAddress(filterTermEcEntity.getDestMacAddress());
      filterTermEntity.setSourceIpAddress(filterTermEcEntity.getSourceIpAddress());
      filterTermEntity.setDestIpAddress(filterTermEcEntity.getDestIpAddress());
      filterTermEntity.setProtocol(filterTermEcEntity.getProtocol());
      filterTermEntity.setSourcePort(filterTermEcEntity.getSourcePort());
      filterTermEntity.setDestPort(filterTermEcEntity.getDestPort());
      return filterTermEntity;
    } finally {
      logger.methodEnd();
    }
  }

  protected FcNode getNode(SessionWrapper sessionWrapper, FcNodeDao fcNodeDao, Integer nodeType, Integer nodeId)
      throws MsfException {
    try {
      logger.methodStart();
      FcNode fcNode = fcNodeDao.read(sessionWrapper, nodeType, nodeId);
      if (fcNode == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            MessageFormat.format("target resource is not found. nodeType = {0}, nodeId = {1}.", nodeType, nodeId));
      }
      return fcNode;
    } finally {
      logger.methodEnd();
    }
  }
}
