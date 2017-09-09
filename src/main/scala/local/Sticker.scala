package local

import scalafx.embed.swing.SwingFXUtils._
import javax.imageio.ImageIO
import java.io._
import scalafx.scene.image._
import scalafx.scene.canvas.GraphicsContext
import scalafx.event.ActionEvent
import scalafx.animation.AnimationTimer

class Sticker(parent : Board, var x : Int, var y : Int) {
  val parts = new Array[WritableImage](4)
  for (i <- 0 until 4) parts(i) = toFXImage(ImageIO.read(new File(".//naklejki//lisek//lisek_"  + (i + 1).toString + ".jpg")),null)
  var lastTime : Long = 0
  var index = 0
  val timer = AnimationTimer(t =>{
    if (t - lastTime > 16050000) {
      parent.drawSticker(this)
}
    lastTime = t
  })
  timer.start
}