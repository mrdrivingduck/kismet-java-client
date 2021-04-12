package iot.mrdrivingduck.kismet.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * Used for getting manufacturer info from
 * <a href="https://standards.ieee.org/products-services/regauth/index.html#">MAC Address</a>.
 *
 * @author Mr Dk.
 * @version 2019/01/10
 */
public class VendorUtil {

  private static final VendorUtil instance = new VendorUtil();

  /**
   * key: MAC address block
   * value: Organization name
   */
  private final Map<String, String> largeBlock = new HashMap<>(26000);
  private final Map<String, String> mediumBlock = new HashMap<>(2500);
  private final Map<String, String> smallBlock = new HashMap<>(3200);

  private VendorUtil() {
    try {
      /*
       * CSV file path
       */
      String LARGE_BLOCK_PATH = "/oui.csv";
      Reader in = new InputStreamReader(this.getClass().getResourceAsStream(LARGE_BLOCK_PATH));
      Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
      /*
       * Index of columns in CSV file
       * [1] - MAC address block
       * [2] - Organization name
       */
      int INDEX_MAC = 1;
      int INDEX_ORG = 2;

      for (CSVRecord record : records) {
        String mac = record.get(INDEX_MAC);
        String organization = record.get(INDEX_ORG);
        largeBlock.put(mac, organization);
      }
      in.close();

      String MEDIUM_BLOCK_PATH = "/mam.csv";
      in = new InputStreamReader(this.getClass().getResourceAsStream(MEDIUM_BLOCK_PATH));
      records = CSVFormat.RFC4180.parse(in);
      for (CSVRecord record : records) {
        String mac = record.get(INDEX_MAC);
        String organization = record.get(INDEX_ORG);
        mediumBlock.put(mac, organization);
      }
      in.close();

      String SMALL_BLOCK_PATH = "/oui36.csv";
      in = new InputStreamReader(this.getClass().getResourceAsStream(SMALL_BLOCK_PATH));
      records = CSVFormat.RFC4180.parse(in);
      for (CSVRecord record : records) {
        String mac = record.get(INDEX_MAC);
        String organization = record.get(INDEX_ORG);
        smallBlock.put(mac, organization);
      }
      in.close();

    } catch(IOException e) {
      System.err.println("Failed to load CSV files");
    }
  }

  public static VendorUtil getInstance() {
    return instance;
  }

  /**
   * Getting organization from MAC address.
   *
   * @param mac Mac address in the form of "XX:XX:XX:XX:XX:XX".
   * @return Organization string.
   */
  public String getOrganization(String mac) {
    String[] splits = mac.split(":");
    if (splits.length != 6) {
      return "";
    }

    StringBuilder key = new StringBuilder();
    for (String split : splits) {
      key.append(split);
    }

    // Small block address : 36-bit (9-bit in HEX)
    if (smallBlock.containsKey(key.substring(0, 9))) {
      return smallBlock.get(key.substring(0, 9));
    }

    // Medium block address : 28-bit (7-bit in HEX)
    if (mediumBlock.containsKey(key.substring(0, 7))) {
      return mediumBlock.get(key.substring(0, 7));
    }

    // Small block address : 24-bit (6-bit in HEX)
    if (largeBlock.containsKey(key.substring(0, 6))) {
      return largeBlock.get(key.substring(0, 6));
    }

    return "";
  }
}
