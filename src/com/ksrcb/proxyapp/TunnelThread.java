package com.ksrcb.proxyapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.apache.log4j.Logger;

public class TunnelThread
  implements Runnable
{
  private static Logger log = Logger.getLogger(TunnelThread.class);

  private Socket incomeSocket = null;
  private Socket outcomeSocket = null;
  private String outcomeip = null;
  private int outcomeport = 0;
  private boolean running = false;

  public TunnelThread(Socket incomeSocket, String outcomeip, int outcomeport)
  {
    this.incomeSocket = incomeSocket;
    this.outcomeip = outcomeip;
    this.outcomeport = outcomeport;
  }

  public void run() {
    running = true;
    outcomeSocket = new Socket();

    log.info("start client " + incomeSocket.getInetAddress() + ":" + incomeSocket.getPort() + ", remote address " + outcomeip + ":" + outcomeport);
    try
    {
      outcomeSocket.connect(new InetSocketAddress(outcomeip, outcomeport));

      if ((outcomeSocket != null) && (outcomeSocket.isConnected())) {
        new Thread(new StreamTunnel(incomeSocket.getInputStream(), outcomeSocket.getOutputStream())).start();
        new Thread(new StreamTunnel(outcomeSocket.getInputStream(), incomeSocket.getOutputStream())).start();
      }
    }
    catch (IOException ioe) {
      log.error(ioe);
    }
  }

  private void closeTunnelThread() {
    if (running) {
      log.info("closing tunnel thread");

      running = false;
      try
      {
        incomeSocket.close();

        if (outcomeSocket != null)
          outcomeSocket.close();
      }
      catch (Exception localException)
      {
      }
    }
  }

  private class StreamTunnel implements Runnable {
    private InputStream is = null;
    private OutputStream os = null;

    private StreamTunnel(InputStream is, OutputStream os)
    {
      this.is = is;
      this.os = os;
    }

    // ERROR //
    public void run()
    {
    	
    	byte[] b=new byte[1024];
    	try {
			while (is.read(b, 0, 1024)!=-1) {
				os.write(b, 0, 1024);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	
    	
    	
      // Byte code:
      //   0: sipush 512
      //   3: newarray byte
      //   5: astore_1
      //   6: iconst_0
      //   7: istore_2
      //   8: aload_0
      //   9: getfield 22	com/ksrcb/proxyapp/TunnelThread$StreamTunnel:is	Ljava/io/InputStream;
      //   12: aload_1
      //   13: invokevirtual 31	java/io/InputStream:read	([B)I
      //   16: istore_2
      //   17: iload_2
      //   18: ifle +51 -> 69
      //   21: aload_0
      //   22: getfield 24	com/ksrcb/proxyapp/TunnelThread$StreamTunnel:os	Ljava/io/OutputStream;
      //   25: aload_1
      //   26: iconst_0
      //   27: iload_2
      //   28: invokevirtual 37	java/io/OutputStream:write	([BII)V
      //   31: aload_0
      //   32: getfield 24	com/ksrcb/proxyapp/TunnelThread$StreamTunnel:os	Ljava/io/OutputStream;
      //   35: invokevirtual 43	java/io/OutputStream:flush	()V
      //   38: invokestatic 46	com/ksrcb/proxyapp/TunnelThread:access$1	()Lorg/apache/log4j/Logger;
      //   41: new 52	java/lang/StringBuffer
      //   44: dup
      //   45: ldc 54
      //   47: invokespecial 56	java/lang/StringBuffer:<init>	(Ljava/lang/String;)V
      //   50: new 59	java/lang/String
      //   53: dup
      //   54: aload_1
      //   55: iconst_0
      //   56: iload_2
      //   57: invokespecial 61	java/lang/String:<init>	([BII)V
      //   60: invokevirtual 63	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
      //   63: invokevirtual 67	java/lang/StringBuffer:toString	()Ljava/lang/String;
      //   66: invokevirtual 71	org/apache/log4j/Logger:debug	(Ljava/lang/Object;)V
      //   69: aload_0
      //   70: getfield 20	com/ksrcb/proxyapp/TunnelThread$StreamTunnel:this$0	Lcom/ksrcb/proxyapp/TunnelThread;
      //   73: invokestatic 77	com/ksrcb/proxyapp/TunnelThread:access$2	(Lcom/ksrcb/proxyapp/TunnelThread;)Z
      //   76: ifeq +33 -> 109
      //   79: iload_2
      //   80: ifgt -72 -> 8
      //   83: goto +26 -> 109
      //   86: astore_3
      //   87: goto +22 -> 109
      //   90: astore 5
      //   92: jsr +6 -> 98
      //   95: aload 5
      //   97: athrow
      //   98: astore 4
      //   100: aload_0
      //   101: getfield 20	com/ksrcb/proxyapp/TunnelThread$StreamTunnel:this$0	Lcom/ksrcb/proxyapp/TunnelThread;
      //   104: invokestatic 81	com/ksrcb/proxyapp/TunnelThread:access$0	(Lcom/ksrcb/proxyapp/TunnelThread;)V
      //   107: ret 4
      //   109: jsr -11 -> 98
      //   112: return
      //
      // Exception table:
      //   from	to	target	type
      //   8	83	86	java/io/IOException
      //   8	87	90	finally
      //   109	112	90	finally
    }
  }
}