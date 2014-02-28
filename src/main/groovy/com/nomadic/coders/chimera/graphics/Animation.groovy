package com.nomadic.coders.chimera.graphics

import groovy.transform.Synchronized

import java.awt.Image

/**
 * Created by jebhomenye on 27/02/2014.
 */
class Animation {
    List<Frame> frames
    int currentFrame
    long animationTime
    long totalDuration

    Animation(){
        frames = []
        totalDuration = 0
        start()
    }

    @Synchronized
    void start(){
        animationTime = 0
        currentFrame = 0
    }

    @Synchronized
    void update(long elapsedTime){
        if(frames){
            animationTime += elapsedTime
            if(animationTime >= totalDuration){
                animationTime %= totalDuration
                currentFrame = 0
            }
            while(animationTime > frames[currentFrame].endTime){
                currentFrame++
            }
        }
    }

    @Synchronized
    Image getImage(){
        frames[currentFrame]?.image
    }

    void addFrames(Image image, long duration){
        totalDuration += duration
        frames << new Frame(image: image, endTime: duration)
    }

    void leftShift(Map frame){
        addFrames(frame.image, frame.duration)
    }
}

class Frame{
    Image image
    long endTime
}