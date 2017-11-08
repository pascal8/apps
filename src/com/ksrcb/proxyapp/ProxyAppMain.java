package com.ksrcb.proxyapp;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;

public class ProxyAppMain
{
  public static String MAPPING_FILE = "mapping.properties";
  private static Logger log = Logger.getLogger(ProxyAppMain.class);

  public static void main(String[] args)
  {
    ProxyAppMain app = new ProxyAppMain();
    app.start();
  }

  public void start() {
    Properties prop = new Properties();
    try {
      prop.load(ProxyAppMain.class.getClassLoader().getResourceAsStream(MAPPING_FILE));
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }

    Iterator iterator = prop.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry)iterator.next();
      String income = (String)entry.getKey();
      String outcome = (String)entry.getValue();

      processEntry(income, outcome);
    }
  }

  private boolean processEntry(String income, String outcome) {
    log.debug("start process: " + income + "=" + outcome);

    int incomeport = 0;
    String outcomeip = null;
    int outcomeport = 0;
    try
    {
      incomeport = Integer.parseInt(income.trim());
      int pos = outcome.indexOf(":");
      outcomeip = outcome.substring(0, pos).trim();
      outcomeport = Integer.parseInt(outcome.substring(pos + 1).trim());
    } catch (Exception e) {
      log.error(e);
      return false;
    }

    Tunnel tunnel = new Tunnel(incomeport, outcomeip, outcomeport);
    new Thread(tunnel).start();

    return true;
  }
}