package com.nomadic.coders.chimera.graphics

import com.nomadic.coders.chimera.input.InputManager

import java.awt.Image
import java.awt.geom.AffineTransform
import java.awt.geom.Point2D

/**
 * Created by jebhomenye on 27/02/2014.
 */
class Sprite {

    Screen screen
    InputManager inputManager

    Map<State,Animation> animations
    State currentState

    float x
    float y
    float dx
    float dy
    AffineTransform transform

    Sprite(Animation animation){
       animations = [:]
       this.animations[State.STATE_LESS] = animation
       this.currentState = State.STATE_LESS
        transform = new AffineTransform()
    }

    Sprite(Map<State,Animation> animations, State startingState){
        this.animations = animations
        this.currentState = startingState
        transform = new AffineTransform()
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

    Sprite translate(){
        transform.setToTranslation(x, y)
        this
    }

    void scale(w, h){
        transform.scale(w, h)
        this
    }

    Point2D location(){
        new Point2D.Float(x, y)
    }

    float distanceFrom(Sprite anotherSprite) {
        location().distance(anotherSprite.location())
    }

    int getIntX(){
        return Math.round(x)
    }

    int getIntY(){
        return  Math.round(y)
    }

    boolean collidesWith(Sprite anotherSprite){
        if(this == anotherSprite){
            return false
        }else{
            return (intX < anotherSprite.intX + anotherSprite.width) && (anotherSprite.intX < intX + width) && (intY < anotherSprite.intY + anotherSprite.height) && anotherSprite.intY < intY + height
        }
    }
}
