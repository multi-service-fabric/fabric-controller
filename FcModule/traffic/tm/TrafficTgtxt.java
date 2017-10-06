package msf.fc.traffic.tm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.traffic.data.TrafficCommonData;
import msf.fc.traffic.tm.TrafficVirtualTopology.DiNodePair;

public class TrafficTgtxt extends AbstractTrafficFileController {

  private static final MsfLogger logger = MsfLogger.getInstance(TrafficAtxt.class);

  public static final String TGTXT_FILE = "Tg.txt";

  Set<DiNodePair> createTgResult() throws MsfException, InterruptedException {

    logger.methodStart();

    TrafficCommonData trafficCommonData = TrafficCommonData.getInstance();

    File tgtxt = new File(trafficCommonData.getTrafficTmOutputFilePath(), TGTXT_FILE);
    Set<DiNodePair> ret = new TreeSet<>();

    try (FileReader fr = new FileReader(tgtxt); BufferedReader in = new BufferedReader(fr)) {

      if (!checkReadFile(tgtxt)) {
        logger.warn("Read Tg.txt error. Reason : CheckReadFile error.");
        throw new MsfException(ErrorCode.FILE_READ_ERROR, "Tg.txt read error.");
      }

      TrafficVirtualTopology vt = TrafficVirtualTopology.getInstance();

      String header = in.readLine();
      Pattern pattern = Pattern.compile("Tg (\\d+)");
      Matcher matcher = pattern.matcher(header);

      if (!matcher.find()) {
        throw new MsfException(ErrorCode.UNDEFINED_ERROR, "Tg.txt Fromat error.");
      }
      int tgTxtSize = Integer.parseInt(matcher.group(1));
      if (tgTxtSize != vt.getGroundPair().size()) {
        throw new MsfException(ErrorCode.UNDEFINED_ERROR,
      }
      for (DiNodePair pair : vt.getGroundPair()) {

        Double data = Double.parseDouble(in.readLine());

        switch (pair.getFrom().getNodeType()) {
          case L3CP:
            switch (pair.getTo().getNodeType()) {
              case L3CP:
                if (pair.getFrom().getL3Cp().getId().getSliceId().equals(pair.getTo().getL3Cp().getId().getSliceId())) {
                  pair.setTrafficValue(data);
                  ret.add(pair);
                }
                break;
              default:
                break;
            }
            break;
          case L2EP:
            switch (pair.getTo().getNodeType()) {
              case L2EP:
                pair.setTrafficValue(data);
                ret.add(pair);
                break;
              default:
                break;
            }
            break;
            break;
        }
      }
      if (CollectionUtils.isEmpty(ret)) {
        logger.error("CreateTgResult error. return data isEmpty or null.");
        throw new MsfException(ErrorCode.UNDEFINED_ERROR, "Tg.txt CreateTgResult error.");
      }

      return ret;

    } catch (IOException exp) {
      logger.warn("Read Tg.txt error. Reason : Catch IOException.", exp);
      throw new MsfException(ErrorCode.FILE_READ_ERROR, "Tg.txt Catch IOException.");
    } finally {
      logger.methodEnd();
    }

  }

  @Override
  void createFile() throws MsfException {
  }

}
