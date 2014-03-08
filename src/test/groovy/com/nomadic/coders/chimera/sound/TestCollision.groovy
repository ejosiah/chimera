package com.nomadic.coders.chimera.sound

import com.nomadic.coders.chimera.core.GameCore
import com.nomadic.coders.chimera.graphics.Animation
import com.nomadic.coders.chimera.graphics.Sprite

import java.awt.Image

/**
 * Created by jay on 06/03/14.
 */
class TestCollision {

    static Sprite sprite1
    static Sprite sprite2

    static main(args){
        createSprites()
        if(sprite1.collidesWith(sprite2)){
            println "collision occurred"
        }else{
            println "No collision"
        }
    }

    static createSprites() {
        Animation animation = new Animation()
        new File("images/").eachFile{
            if(it.name.contains("fly")){
                Image image = GameCore.loadImage(it.toURL())
                animation.addFrames(image, 50)
            }
        }
        sprite1 = new Sprite(animation)
        sprite1.x = sprite1.y = 100
        Image ear = GameCore.loadImage("images/ear.png")
        animation = new Animation()
        animation.addFrames(ear, 0)

        sprite2 = new Sprite(animation)
        sprite2.x = 100
        sprite2.y = 100
    }
}
