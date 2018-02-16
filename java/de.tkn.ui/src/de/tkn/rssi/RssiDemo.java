package de.tkn.rssi;

import net.tinyos.message.Message;
import net.tinyos.message.MessageListener;
import net.tinyos.message.MoteIF;
import net.tinyos.packet.BuildSource;
import net.tinyos.packet.PhoenixSource;
import net.tinyos.util.PrintStreamMessenger;

public class RssiDemo implements MessageListener {

  private final MoteIF moteIF;
  
  public RssiDemo(final MoteIF moteIF) {
    this.moteIF = moteIF;
    this.moteIF.registerListener(new RssiMsg(), this);
  }
    
  @Override
public void messageReceived(final int to, final Message message) {
    final RssiMsg msg = (RssiMsg) message;
    final int source = message.getSerialPacket().get_header_src();
    System.out.println("Rssi Message received from node " + source + 
		       ": Rssi = " +  msg.get_rssi());
  }
  
  private static void usage() {
    System.err.println("usage: RssiDemo [-comm <source>]");
  }
  
  public static void main(final String[] args) throws Exception {
    final String source = "serial@/dev/ttyUSB1:telosb";
    
    PhoenixSource phoenix;
    
//    if (source == null) {
//      phoenix = BuildSource.makePhoenix(PrintStreamMessenger.err);
//    }
//    else {
      phoenix = BuildSource.makePhoenix(source, PrintStreamMessenger.err);
    //}

    final MoteIF mif = new MoteIF(phoenix);
    final RssiDemo serial = new RssiDemo(mif);
  }


}
