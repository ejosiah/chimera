package com.nomadic.coders.chimera

import com.nomadic.coders.chimera.core.GameCore
import com.nomadic.coders.chimera.graphics.VoidRepaintManager
import com.nomadic.coders.chimera.input.Action

import javax.swing.JButton
import javax.swing.JPanel
import java.awt.Graphics2D

/**
 * Created by jebhomenye on 03/03/2014.
 */
class MenuTest extends InputManagerTest {

    Action configAction

    JButton playButton
    JButton configButton
    JButton quitButton
    JButton pauseButton
    JPanel playButtonSpace


    @Override
    void init(){
        super.init()
        VoidRepaintManager.install()

        configAction = new Action("config")

        quitButton = createButton("quit", "Quit")


    }

    @Override
    def draw(Graphics2D g) {
        return null
    }
}
