package com.nomadic.coders.chimera.graphics

import java.awt.Image

/**
 * Created by jebhomenye on 27/02/2014.
 */
class Sprite {
    Animation animation
    float x
    float y
    float dx
    float dy

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
}
