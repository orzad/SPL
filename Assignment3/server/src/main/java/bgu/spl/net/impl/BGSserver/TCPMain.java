package bgu.spl.net.impl.BGSserver;

import bgu.spl.net.api.BidiMessagingProtocol;
import bgu.spl.net.impl.BidiMessagingProtocolImpl;
import bgu.spl.net.impl.EncoderDecoderImpl;
import bgu.spl.net.srv.Server;

import java.nio.charset.StandardCharsets;

public class TCPMain {
    public static void main(String[] args){
        Database.getInstance();
        Server.threadPerClient(
                Integer.valueOf(Integer.parseInt(args[0])), //port
                BidiMessagingProtocolImpl::new, //protocol factory
                EncoderDecoderImpl::new //message encoder decoder factory
        ).serve();
    }
}
