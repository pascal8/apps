package com.ksrcb.proxyapp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;

public class Tunnel
  implements Runnable
{
  private static Logger log = Logger.getLogger(Tunnel.class);

  private int incomeport = 0;
  private String outcomeip = null;
  private int outcomeport = 0;

  private boolean running = false;

  private ServerSocket serverSocket = null;

  public Tunnel(int incomeport, String outcomeip, int outcomeport)
  {
    this.incomeport = incomeport;
    this.outcomeip = outcomeip;
    this.outcomeport = outcomeport;
  }

  public void run() {
    running = true;
    try
    {
      serverSocket = new ServerSocket();
      serverSocket.bind(new InetSocketAddress(incomeport));
      log.info("start to listen to " + incomeport);
    } catch (IOException ioe) {
      log.error("failed to listen to " + incomeport, ioe);
      return;
    }
    do
      try
      {
        Socket socket = serverSocket.accept();

        new Thread(new TunnelThread(socket, outcomeip, outcomeport)).start();
      } catch (IOException ioe) {
        log.error("failed to accept a socket", ioe);
      }
    while (running);
  }
}