package local

import scalafx.Includes._
import scalafx.application._
import javafx.embed.swing.JFXPanel
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.scene.input.MouseEvent
import scalafx.stage.Stage
import scalafx.scene.image.Image
import scalafx.scene.canvas._
import scalafx.embed.swing.SwingFXUtils._
import javax.imageio.ImageIO
import java.io._

object Main extends App{
  
  val screenHeight : Double = 300
  val screenWidth :Double = 313
  
  new JFXPanel()
  var closeableStage : Stage = null
  Platform.runLater{
    closeableStage = new Stage{
      title = "Welcome to draughts"
      scene = new Scene(screenWidth,screenHeight){
        val canvas = new Canvas(screenWidth,screenHeight)
        val gc = canvas.graphicsContext2D
        val img = toFXImage(ImageIO.read(new File("naklejki//draughts.jpg")),null)
        gc.drawImage(img, 0,0,screenWidth,screenHeight,0,0,screenWidth,screenHeight)
        val createServer = new Button("Create server"){
          onMouseClicked = (e : MouseEvent) =>{
            println("server")
            
            joinGame.disable = false
          }
        }
        val joinGame = new Button("Join Game"){
          onMouseClicked = (e : MouseEvent) =>{
            println("game")
            makeInvisible()
          }
        }
        val quit = new Button("Quit"){
          onMouseClicked = (e : MouseEvent) =>{
            println("Quit")
            joinGame.disable = true
          }
        }
        val line = new HBox(50) {
          println(quit.width)
          println(createServer.width)
          println(joinGame.width)
          children = List (createServer, joinGame, quit)
        }
        
        val borderPane = new BorderPane{
          //center = new BorderImage(new Image(""),10,101,10,101,10)
          top = line
          center = canvas
        }
        
        
        root = borderPane
      }
    }
    closeableStage.maxWidth = screenWidth
    closeableStage.minWidth = screenWidth
    closeableStage.maxHeight = screenHeight
    closeableStage.minHeight = screenHeight
    closeableStage.showAndWait()
  }  

  
  def makeInvisible(){
    closeableStage.showing_=(false)
  }
}