package local

import akka.actor._
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.shape._
import scalafx.scene.canvas._
import scalafx.scene.input._
import scalafx.event.ActionEvent
import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

object HelloLocal extends App {
  
  
  
  implicit val system = ActorSystem("LocalSystem")
  val board = new Board()
  val localActor = system.actorOf(Props(new LocalActor(board)), name = "LocalActor")  
  localActor ! "START"                                                  
  board.app.main(Array())
}

class LocalActor(board: Board) extends Actor {
  val remote = context.actorFor("akka.tcp://HelloRemoteSystem@Lenovo-PC:5150/user/RemoteActor") //192.168.0.103
  var counter = 0
  
  def receive = {
    case "START" =>
        remote ! ("init",this.toString)
    case "complete" => board.drawBoard_complete(remote,this.toString)
    case "wait" => board.drawBoard_wait()
    case "Nice try" => println("Nice try")
    case ("chat",msg: String) => board.receiveMsg(msg)
  }
}