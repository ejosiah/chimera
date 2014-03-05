package com.nomadic.coders.chimera

import com.nomadic.coders.chimera.core.GameCore
import com.nomadic.coders.chimera.graphics.VoidRepaintManager
import com.nomadic.coders.chimera.input.Action

import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JPanel
import java.awt.AlphaComposite
import java.awt.Container
import java.awt.Cursor
import java.awt.FlowLayout
import java.awt.Graphics2D
import java.awt.Image
import java.awt.Transparency
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Created by jebhomenye on 03/03/2014.
 */
class MenuTest extends InputManagerTest implements ActionListener{

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
        playButton = createButton("play", "Continue")
        pauseButton = createButton("pause", "Pause")
        configButton = createButton("config", "Change Settings")

        playButtonSpace = new JPanel()
        playButtonSpace.opaque = false
        playButtonSpace.add pauseButton

        Container contentPane = screen.fullScreenWindow.contentPane
        contentPane.opaque = false

        contentPane.layout = new FlowLayout(FlowLayout.LEFT)
        contentPane.add playButtonSpace
        contentPane.add configButton
        contentPane.add quitButton

        screen.fullScreenWindow.validate()

    }

    JButton createButton(String name, String toolTip) {
        println "Loading button $name, $toolTip"
        String path = "images/menu/${name}.png"
        ImageIcon iconRollover = new ImageIcon(path)
        int w = iconRollover.iconWidth
        int h = iconRollover.iconHeight

        Cursor cursor = Cursor.getPredefinedCursor Cursor.HAND_CURSOR


        // make translucent default image
        Image image = screen.createCompatibleImage(w, h, Transparency.TRANSLUCENT)
        Graphics2D g = image.graphics
        g.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5F)
        g.drawImage iconRollover.image, 0, 0, null
        g.dispose()
        ImageIcon iconDefault = new ImageIcon(image)

        // make pressed image
        image = screen.createCompatibleImage(w, h, Transparency.TRANSLUCENT)
        g = image.graphics
        g.drawImage iconRollover.image, 2, 2, null
        g.dispose()
        ImageIcon iconPressed = new ImageIcon(image)

        JButton button = new JButton()
        button.addActionListener this
        button.ignoreRepaint = true
        button.focusable = false
        button.toolTipText = toolTip
        button.border = null
        button.contentAreaFilled = false
        button.cursor = cursor
        button.icon = iconDefault
        button.rolloverIcon = iconRollover
        button.pressedIcon = iconPressed

        return button

    }

    @Override
    def draw(Graphics2D g) {
        super.draw g
        screen.fullScreenWindow.layeredPane.paintComponents g
    }

    @Override
    void setPaused(boolean p){
        super.setPaused(p)
        playButtonSpace.removeAll()
        if(paused){
            playButtonSpace.add playButton
        }else{
            playButtonSpace.add pauseButton
        }
    }

    @Override
    void actionPerformed(ActionEvent e) {
        if(e.source == quitButton){
            exit.tap()
        }else if (e.source == configButton){
            configAction.tap()
        }else if (e.source == playButton || e.source == pauseButton){
            pause.tap()
        }
    }

    static main(args){
        new MenuTest().run()
    }
}
