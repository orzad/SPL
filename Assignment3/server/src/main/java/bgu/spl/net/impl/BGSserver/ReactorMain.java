package bgu.spl.net.impl.BGSserver;

import bgu.spl.net.impl.BidiMessagingProtocolImpl;
import bgu.spl.net.impl.EncoderDecoderImpl;
import bgu.spl.net.srv.Reactor;

public class ReactorMain {
    public static void main(String[] args){
        Reactor server = new Reactor(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                ()-> new BidiMessagingProtocolImpl<>(), ()-> new EncoderDecoderImpl<>());
        server.serve();
    }
    }

