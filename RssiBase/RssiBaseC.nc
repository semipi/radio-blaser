#include "ApplicationDefinitions.h"
#include "RssiDemoMessages.h"

module RssiBaseC {
  uses interface Boot;
  uses interface Intercept as RssiMsgIntercept;
  uses interface Timer<TMilli> as LaserTimer;
  uses interface Read<uint16_t> as LaserSensor;

#ifdef __CC2420_H__
  uses interface CC2420Packet;
#elif defined(TDA5250_MESSAGE_H)
  uses interface Tda5250Packet;    
#else
  uses interface PacketField<uint8_t> as PacketRSSI;
#endif 
} implementation {

  enum {
    LASER_ON,
    LASER_OFF,
  };

  enum {
    LASER_SENSING_PERIOD = 100, // ms
    LASER_THRESHOLD = 1000,
  };

  enum {
    NONE = 0,
    START = 1,
    END = 2,
  };

  int state = LASER_ON;
  int msg_state = NONE;

  int start_sent = 0;
  int end_sent = 1;

  uint16_t getRssi(message_t *msg);

  event void Boot.booted() {
    call LaserTimer.startPeriodic(LASER_SENSING_PERIOD);
  }

  event void LaserTimer.fired() {
    call LaserSensor.read();
  }

  event void LaserSensor.readDone(error_t ok, uint16_t val) {
    if(ok == SUCCESS) {
      if (val < LASER_THRESHOLD) {
        state = LASER_OFF;
        msg_state = START;
      } else {
        state = LASER_ON;
        msg_state = END;
      }
    }
  }

  event bool RssiMsgIntercept.forward(message_t *msg, void *payload, uint8_t len) {
    if (state == LASER_OFF) {
      RssiMsg *rssiMsg = (RssiMsg*) payload;
      rssiMsg->rssi = getRssi(msg);
      rssiMsg->state = NONE;
      if (start_sent == 0) {
        rssiMsg->state = START;
        start_sent = 1;
        end_sent = 0;
      }
    }
    if (state == LASER_ON && end_sent == 0) {
      RssiMsg *rssiMsg = (RssiMsg*) payload;
      rssiMsg->rssi = getRssi(msg);
      rssiMsg->state = END;
      start_sent = 0;
      end_sent = 1;
    }
    return TRUE;
  }

#ifdef __CC2420_H__  
  uint16_t getRssi(message_t *msg){
    return (uint16_t) call CC2420Packet.getRssi(msg);
  }
#elif defined(CC1K_RADIO_MSG_H)
    uint16_t getRssi(message_t *msg){
    cc1000_metadata_t *md =(cc1000_metadata_t*) msg->metadata;
    return md->strength_or_preamble;
  }
#elif defined(PLATFORM_IRIS) || defined(PLATFORM_UCMINI)
  uint16_t getRssi(message_t *msg){
    if(call PacketRSSI.isSet(msg))
      return (uint16_t) call PacketRSSI.get(msg);
    else
      return 0xFFFF;
  }
#elif defined(TDA5250_MESSAGE_H)
   uint16_t getRssi(message_t *msg){
       return call Tda5250Packet.getSnr(msg);
   }
#else
  #error Radio chip not supported! This demo currently works only \
         for motes with CC1000, CC2420, RF230, RFA1 or TDA5250 radios.  
#endif
}
