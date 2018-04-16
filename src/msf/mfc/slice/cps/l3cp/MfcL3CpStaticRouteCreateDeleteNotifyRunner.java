
package msf.mfc.slice.cps.l3cp;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import com.google.gson.reflect.TypeToken;

import msf.mfcfc.common.data.AsyncRequest;
import msf.mfcfc.common.data.AsyncRequestsForLower;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.cps.l3cp.data.L3CpStaticRouteCreateDeleteRequestBody;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpStaticRouteEntity;

/**
 * Implementation class for the reception process of operation result
 * notification in L3CP static routes addition/deletion.
 *
 * @author NTT
 *
 */
public class MfcL3CpStaticRouteCreateDeleteNotifyRunner extends MfcAbstractL3CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL3CpStaticRouteCreateDeleteNotifyRunner.class);
  private List<String> createdStaticRouteIdList = new ArrayList<>();

  public MfcL3CpStaticRouteCreateDeleteNotifyRunner(AsyncRequest asyncRequestForNotify) {
    this.asyncRequestForNotify = asyncRequestForNotify;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      AsyncRequestsForLower lower = asyncRequestForNotify.getAsyncRequestsForLowerList().get(0);
      List<L3CpStaticRouteCreateDeleteRequestBody> requestBody = (List<L3CpStaticRouteCreateDeleteRequestBody>) JsonUtil
          .fromJson(lower.getRequestBody(), new TypeToken<ArrayList<L3CpStaticRouteCreateDeleteRequestBody>>() {
          });
      for (L3CpStaticRouteCreateDeleteRequestBody body : requestBody) {
        switch (body.getOpEnum()) {
          case ADD:
            L3CpStaticRouteEntity addEntity = body.getValue().getStaticRoute();

            createdStaticRouteIdList.add(makeStaticRouteId(addEntity));
            break;
          default:
            break;
        }
      }

      if (createdStaticRouteIdList.size() != 0) {
        return createResponseForCreateStaticRoute(createdStaticRouteIdList);
      } else {
        return new RestResponseBase(HttpStatus.NO_CONTENT_204, (String) null);
      }
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeRollbackImpl() throws MsfException {

    return null;
  }

}
