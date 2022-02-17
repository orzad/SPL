package bgu.spl.net.impl;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.Operation;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;

public class EncoderDecoderImpl<T> implements MessageEncoderDecoder<Operation> {
    private byte[] bytes;
    private int counter;
    private int length;
    private int currIndex;
    private Operation operation;

    public EncoderDecoderImpl() {
        bytes = new byte[1<<10];
        counter = 0;
        length = 0;
        currIndex = 2;
        operation = new Operation();
    }

    @Override
    public Operation decodeNextByte(byte nextByte) {

        // finding the operation
        short op = 0;
        if (length <= 1 ){
            if(nextByte!=';') {
                pushByte(nextByte);
            }
        }
        else {
            byte[] bb = {bytes[0], bytes[1]};
            String n = new String(bb, StandardCharsets.UTF_8);
            int i = Integer.parseInt(n);
            op = (short) i;
//           op = bytesToShort(bytes);


            // conditions for adding or pop
            if (nextByte == '\0') {

                counter++;
                if (counter == 1) {

                    if (op == 1 | op == 2 | op == 6) {
                        setName();
                    } else if (op == 5) {
                        setContent();
                        return setOp(op);
                    } else if (op == 8) {
                        setNameList();
                        return setOp(op);
                    } else if (op == 9) {
                        setPostingUser();
                    } else if (op == 12) {
                        setName();
                        return setOp(op);
                    }
                } else if (counter == 2) {
                    if (op == 1 | op == 2) {
                        setPassword();
                    } else if (op == 6) {
                        setContent();
                    } else if (op == 9) {
                        setContent();
                        return setOp(op);
                    }
                } else if (counter == 3) {
                    if (op == 1) {
                        setBirthday();
                        return setOp(op);
                    } else if (op == 6) {
                        setTime();
                        return setOp(op);
                    }
                    else if (op == 2){
//                        byte[] b = {bytes[length-1]};
//                        System.out.println("b is" + bytes[length-1]);
//                        int c = Integer.parseInt(new String(b, StandardCharsets.UTF_8));
//                        System.out.println("first captcha is " + c);
//                        operation.setCaptcha((byte) c);
//                        return setOp(op);
                    }
                }
            }
            else if (nextByte == ';') {
                if (op == 4) {
                    byte[] b = {bytes[currIndex]};
                    int c = Integer.parseInt(new String(b, StandardCharsets.UTF_8));
                    operation.setFollow((byte) c);
                    currIndex++;
                    setName();
                    return setOp(op);
                }
                if ((op == 10 | op == 11) & length == 4) { // conditions may change
                    setMessage(bytes);
                    return setOp(op);
                } else if (op == 10 & length > 4) {
                    setMessage(bytes);
                    setContent();
                    return setOp(op);
                }
                else if (op == 2 & counter == 2 ) {
                    byte[] b = {bytes[length-1]};
                    int c = Integer.parseInt(new String(b, StandardCharsets.UTF_8));
                    operation.setCaptcha((byte) c);
                    return setOp(op);
                }
                else if (op == 7 | op == 3) {
                    return setOp(op);
            }

            }
            else {
                pushByte(nextByte);
            }
        }
        return null;
    }

    @Override
    public byte[] encode(Operation message) {
        String s="";
        if (message.getOp() == 9 ){
            String OPS = "0"+String.valueOf(message.getOp());
            String type = "0"+String.valueOf(message.getNotification());
            String postingUser = message.getPostingUser();
            String content = message.getContent();
            s = OPS+type+postingUser+' '+content+';';
        }
        else if (message.getOp() == 10 ){
            String OPS = String.valueOf(message.getOp());
            String type = this.setMessageForSend(message);
            String content = message.getContent();
            s = OPS+type+content+';';

        }
        else if (message.getOp() == 11 ){
            String OPS = String.valueOf(message.getOp());
            String type = this.setMessageForSend(message);
            s = OPS+type+message.getContent()+';';
        }
        return s.getBytes(StandardCharsets.UTF_8);
    }

    private String setMessageForSend(Operation op ){
        if(op.getMessage()<10){
            return "0"+String.valueOf(op.getMessage());
        }
        return String.valueOf(op.getMessage());
    }

    private void pushByte(byte b){
        if(length >= bytes.length){
            bytes = Arrays.copyOf(bytes, length*2);
        }
        bytes[length] = b;
        length++;
    }

    private void setName(){
        operation.setUsername(
                new String(Arrays.copyOfRange(bytes, currIndex, length),
                        StandardCharsets.UTF_8));
        currIndex = length;
    }

    private void setContent(){

        operation.setContent(
                new java.lang.String(Arrays.copyOfRange(bytes, currIndex, length),
                        StandardCharsets.UTF_8));
        currIndex = length;
    }

    private void setPostingUser(){
        operation.setNotification(bytes[currIndex]);
        currIndex++;
        operation.setPostingUser(
                new java.lang.String(Arrays.copyOfRange(bytes, currIndex, length),
                        StandardCharsets.UTF_8));
        currIndex = length;
    }

    private void setNameList(){
        LinkedList<String> names = new LinkedList<>();
        String nameString = new java.lang.String(Arrays.copyOfRange(bytes, currIndex, length),
                StandardCharsets.UTF_8);
        String s = "";
        int startIndex = 0;
        int finalIndex = 0;
        while(finalIndex< nameString.length()){
            if (nameString.charAt(finalIndex)=='|'){
                names.add((String) nameString.subSequence(startIndex,finalIndex));
                startIndex = finalIndex+1;
            }
            finalIndex++;
        }
        operation.setUsernames(names);

    }

    private void setPassword(){

        operation.setPassword(
                new java.lang.String(Arrays.copyOfRange(bytes, currIndex, length),
                        StandardCharsets.UTF_8));
        currIndex = length;

    }

    private void setBirthday(){
        operation.setBirthday(
                new java.lang.String(Arrays.copyOfRange(bytes, currIndex, length),
                        StandardCharsets.UTF_8));
        currIndex = length;

    }

    private void setTime(){
        operation.setSendTime(
                new java.lang.String(Arrays.copyOfRange(bytes, currIndex, length),
                        StandardCharsets.UTF_8));
        currIndex = length;
    }

    private void setMessage(byte[] bytes){
        short result = (short)((bytes[2] & 0xff) << 8);
        result += (short)(bytes[3] & 0xff);
        operation.setMessage(result);
    }

    private Operation setOp(short op){
        operation.setOp(op);
        bytes  = new byte[1<<10];
        length = 0;
        counter = 0;
        currIndex = 2;
        return operation;
    }

    public short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }
}
