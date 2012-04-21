package net.zzorn.utils


import javax.swing.{JComponent, JFrame}
import java.awt.{Container, Component, Dimension}

/**
 * A Swing JFrame with sensible default settings.
 * Makes UI prototyping easier.
 */
class SimpleFrame(title : String, content : Component, w: Int = 800, h: Int = 600) extends JFrame {

  setTitle(title)
  setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE )

  val contentPanel: Container = getContentPane
  contentPanel.add(content)
  contentPanel.setPreferredSize( new Dimension(w, h) )
  contentPanel.setLayout(null)

  content.setBounds(0, 0, w, h)

  pack()
  setVisible(true)
}


