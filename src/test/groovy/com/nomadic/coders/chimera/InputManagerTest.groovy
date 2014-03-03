package com.nomadic.coders.chimera

import com.nomadic.coders.chimera.core.GameCore
import com.nomadic.coders.chimera.graphics.Animation
import com.nomadic.coders.chimera.input.Action
import com.nomadic.coders.chimera.input.EmptyInputListener
import com.nomadic.coders.chimera.input.InputManager
import java.awt.Color
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
    KeyLogger keyLogger
    MoveDetector moveDetector

    void init(){
        super.init()
        inputManager = new InputManager(screen)
        inputManager.relativeMouseMode = true
        inputManager.cursor = InputManager.INVISIBLE_CURSOR
        keyLogger = new KeyLogger(inputManager: inputManager)
        moveDetector = new MoveDetector(inputManager: inputManager)
        screen.addInputListener keyLogger
        screen.addInputListener moveDetector

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
        bgImage = loadImage "images/arena.png"

        def imagePaths = [:]
        imagePaths[Player.STANDING] = "images/subzero/standing"
        imagePaths[Player.WALKING] = "images/subzero/walking"
        imagePaths[Player.JUMPING] = "images/subzero/jumping"
        imagePaths[Player.RUNNING] = "images/subzero/running"

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
            if(moveDetector.running){
                dx += Player.SPEED * 1.5
                player.currentState = Player.RUNNING
            }else{
                player.currentState = Player.WALKING
            }
        }
        if((player.currentState == Player.WALKING
            || player.currentState == Player.RUNNING)
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
        int x = -(bgImage.getWidth(null) - screen.width)/2
        g.drawImage bgImage, x, 0, null

//        g.setColor Color.BLUE
//        g.fillRect 0, 0, screen.width, screen.height
//        g.setColor Color.WHITE

//        if(keyLogger.list){
//            g.drawString(keyLogger.data, 5, FONT_SIZE)
//        }
        player.translate().scale(2, 2)

        g.drawImage player.image, player.transform, null
    }

    static main(args){
        new InputManagerTest().run()
    }
}

class KeyLogger extends EmptyInputListener{
    def list = []
    InputManager inputManager
    boolean keyHeld

    @Override @Synchronized
    void keyPressed(KeyEvent e) {
        Action action = inputManager.getKeyAction e
        if(action){
            switch(e.keyCode){
                case KeyEvent.VK_RIGHT:
                    if(!keyHeld){
                        list << "->"
                        keyHeld = true
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if(!keyHeld){
                        list << "<-"
                        keyHeld = true
                    }
                    break;
                case KeyEvent.VK_UP:
                    list << "up"
                    break;
                case KeyEvent.VK_DOWN:
                    list << "down"
            }
        }
    }

    @Override
    void keyReleased(KeyEvent e){
        keyHeld = false
    }

    String getData(){
        list.join(", ")
    }
}

class MoveDetector extends EmptyInputListener{
    def actions = []
    InputManager inputManager
    boolean keyHeld
    long lastInputTime

    @Override @Synchronized
    void keyPressed(KeyEvent e) {
        Action action = inputManager.getKeyAction e
        if(action){
            switch(e.keyCode){
                case KeyEvent.VK_RIGHT:
                    if(!keyHeld){
                        if(lastInputTime){
                            if(System.currentTimeMillis() - lastInputTime > 300L){
                                actions.clear()
                            }
                        }
                        actions << action
                        lastInputTime = System.currentTimeMillis()
                        keyHeld = true
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if(!keyHeld){
                        actions << action
                        keyHeld = true
                    }
                    break;
                case KeyEvent.VK_UP:
                    break;
                case KeyEvent.VK_DOWN:
                    break;
            }
        }
    }

    @Override
    void keyReleased(KeyEvent e){
        switch (e.keyCode){
            case KeyEvent.VK_RIGHT:
                if(running){
                    actions.clear()
                }
            case KeyEvent.VK_LEFT:
                keyHeld = false
        }

    }

    boolean isRunning(){
        println( actions*.name)
        actions*.name == ["moveRight", "moveRight"]
    }
}