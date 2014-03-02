package com.nomadic.coders.chimera

import com.nomadic.coders.chimera.core.GameCore
import com.nomadic.coders.chimera.graphics.Animation
import com.nomadic.coders.chimera.input.Action
import com.nomadic.coders.chimera.input.InputManager
import groovy.transform.Synchronized

import java.awt.Graphics2D
import java.awt.Image
import java.awt.event.KeyEvent
import java.awt.geom.AffineTransform

/**
 * Created by jay on 02/03/14.
 */
class InputManagerTest extends GameCore {

    Action jump
    Action exit
    Action moveLeft
    Action moveRight
    Action pause
    InputManager inputManager
    Player player
    Image bgImage
    boolean paused

    void init(){
        super.init()
        inputManager = new InputManager(screen)
        inputManager.relativeMouseMode = true
        inputManager.cursor = InputManager.INVISIBLE_CURSOR

        createGameActions()
        createSprite()
        paused = false
    }

    void createGameActions() {
        jump = new Action("jump", Action.Behavior.DETECT_INITIAL_PRESS_ONLY)
        exit = new Action("exit", Action.Behavior.DETECT_INITIAL_PRESS_ONLY)
        moveLeft = new Action("moveLeft")
        moveRight = new Action("moveRight")
        pause = new Action("pause", Action.Behavior.DETECT_INITIAL_PRESS_ONLY)

        inputManager.mapToKey exit, KeyEvent.VK_ESCAPE
        inputManager.mapToKey pause, KeyEvent.VK_P
        inputManager.mapToKey jump, KeyEvent.VK_SPACE
        inputManager.mapToMouse jump, InputManager.MouseCode.BUTTON_1

        inputManager.mapToKey moveLeft, KeyEvent.VK_LEFT
        inputManager.mapToKey moveRight, KeyEvent.VK_RIGHT

        inputManager.mapToKey moveLeft, KeyEvent.VK_A
        inputManager.mapToKey moveRight, KeyEvent.VK_D

        inputManager.mapToMouse moveLeft, InputManager.MouseCode.MOVE_LEFT
        inputManager.mapToMouse moveRight, InputManager.MouseCode.MOVE_RIGHT
    }

    void createSprite() {
        bgImage = loadImage "images/background.jpg"

        def imagePaths = [:]
        imagePaths[Player.STANDING] = "images/subzero/standing"
        imagePaths[Player.WALKING] = "images/subzero/walking"
        imagePaths[Player.JUMPING] = "images/subzero/jumping"

        Map animations = [:]
        imagePaths.each { state, path ->
            Animation animation = new Animation()
            new File(path).eachFile{
                Image image = loadImage it.toURL()
                animation.addFrames image, 100
            }
            animations[state] = animation
        }




        player = new Player(animations, Player.STANDING)
        player.floorY = screen.height/2
    }

    void setPaused(boolean p){
        if(paused != p){
            paused = p
            inputManager.resetAllActions()
        }
    }

    @Override
    void update(long elapsedTime){
        checkSystemInput()

        if(!paused){
            checkGameInput()

            player.update(elapsedTime)

            checkForCollision();
        }
    }

    void checkForCollision() {
        if(player.x <= 0){
            player.x = 0
        }
        if(player.x + (player.width * 2) >= screen.width){
            player.x = screen.width - (player.width * 2)
        }
    }

    void checkSystemInput() {
        if(pause.pressed){
            paused = !paused
        }
        if(exit.pressed){
            stop()
        }
    }

    void checkGameInput() {

        float dx = 0
        if(moveLeft.pressed){
            dx -= Player.SPEED
            player.currentState = Player.WALKING
        }
        if(moveRight.pressed){
            dx += Player.SPEED
            player.currentState = Player.WALKING
        }
        if(player.currentState == Player.WALKING
                && (moveLeft.state == Action.State.RELEASED
                    && moveRight.state == Action.State.RELEASED)){
            player.currentState = new Standing()
        }
        player.dx = dx

        if(jump.pressed && player.currentState != Player.JUMPING){
            player.jump()
        }
    }

    @Synchronized @Override
    def draw(Graphics2D g){
        g.drawImage bgImage, 0, 0, null

        AffineTransform transform = new AffineTransform()
        transform.setToTranslation(player.x, player.y)
        transform.scale(2, 2)
        g.drawImage player.image, transform, null
    }

    static main(args){
        new InputManagerTest().run()
    }
}
