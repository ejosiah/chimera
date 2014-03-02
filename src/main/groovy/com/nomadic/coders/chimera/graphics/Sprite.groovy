package com.nomadic.coders.chimera.graphics

import java.awt.Image

/**
 * Created by jebhomenye on 27/02/2014.
 */
class Sprite {
    Map<State,Animation> animations
    State currentState
    float x
    float y
    float dx
    float dy

    Sprite(Map<State,Animation> animations, State startingState){
        this.animations = animations
        this.currentState = startingState
    }

    void update(long elapsedTime){
        x += dx * elapsedTime
        y += dy * elapsedTime
        animation.update(elapsedTime)
    }

    int getWidth(){
        image.getWidth null
    }

    int getHeight(){
        image.getHeight null
    }

    Image getImage(){
        animation.image
    }

    Animation getAnimation(){
        animations[currentState]
    }
}
