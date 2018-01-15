package msf.mfcfc.db.dao.common;

import java.sql.Timestamp;
import java.util.List;

import msf.mfcfc.common.data.AsyncRequest;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.db.dao.AbstractCommonDao;


public abstract class AsyncRequestsDao extends AbstractCommonDao<AsyncRequest, String> {


  public abstract List<AsyncRequest> readList(SessionWrapper session) throws MsfException;

  public abstract void updateList(SessionWrapper session, Integer beforeStatus, Integer afterStatus,
      String afterSubStatus) throws MsfException;

  public abstract void delete(SessionWrapper session, Timestamp targetTime) throws MsfException;

}
