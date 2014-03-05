package com.nomadic.coders.chimera.sound.io

/**
 * Created by jebhomenye on 05/03/2014.
 */
class LoopingByteInputStream extends ByteArrayInputStream{

    private closed

    LoopingByteInputStream(byte[] bytes) {
        super(bytes)
        closed = false
    }

    @Override
    int read(byte[] buffer, int offset, int lenght){
        if(closed){
            return -1
        }
        int totalbytesRead = 0;

        while(totalbytesRead < lenght){
            int numBytesRead = super.read(buffer, offset + totalbytesRead, lenght - totalbytesRead)
            if(numBytesRead > 0){
                totalbytesRead += numBytesRead
            }else{
                reset()
            }
        }
        return totalbytesRead
    }

    @Override
    void close(){
        super.close()
        closed = true
    }
}
