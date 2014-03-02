package com.nomadic.coders.chimera

import com.nomadic.coders.chimera.graphics.Animation
import com.nomadic.coders.chimera.graphics.Sprite
import com.nomadic.coders.chimera.graphics.State

import java.awt.geom.AffineTransform

/**
 * Created by jay on 02/03/14.
 */
class Player extends Sprite{

    static final float SPEED = 0.3F
    static final float GRAVITY = 0.002F
    static final State JUMPING = new Jumping()
    static final State STANDING = new Standing()
    static final State WALKING = new Walking()

    int floorY

    Player(Map animations, State startingState){
        super(animations, startingState)
    }

    void jump(){
        dy = -1
        currentState = JUMPING
    }

    void update(long elapsedTime){
        if(currentState == JUMPING){
            dy += GRAVITY * elapsedTime
        }

        super.update(elapsedTime)

        if(currentState == JUMPING && y >= floorY){
            dy = 0
            y = floorY
            currentState = STANDING
        }
    }

    void setFloorY(int floorY){
        this.floorY = floorY
        y = floorY

    }
}
