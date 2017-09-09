package local

import akka.actor._
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.shape._
import scalafx.scene.canvas._
import scalafx.scene.input._
import scalafx.scene.paint.Color._
import scalafx.scene.layout.HBox
import scalafx.geometry.Insets
import scalafx.scene.effect.DropShadow
import scalafx.scene.paint.{Stops, LinearGradient}
import scalafx.scene.text._
import scalafx.embed.swing.SwingFXUtils._
import javax.imageio.ImageIO
import java.io._
import Storage._


class Board{

  val canvas = new Canvas(screenSize + chatSize,screenSize)
  var gc : GraphicsContext = null//canvas.graphicsContext2D
  var remote : ActorRef = null
  var sender : String = null
  var gamingPhase = false // if not - app is in waiting phase
  var em : EventManager = null 
  var sm : SendingManager = null 
  
  val app = new JFXApp{
    stage = new JFXApp.PrimaryStage{
      title = "Draughts"
      scene = new Scene(screenSize + chatSize,screenSize){
        content = canvas
        onKeyPressed = (e : KeyEvent) =>{
          if (gamingPhase) em.managePressKeys(e.text,e.code)
        }
        onKeyReleased = (e : KeyEvent) =>{
          if (gamingPhase) em.manageRelKeys(e.text,e.code)
        }
      }
    }
  }
  
  def receiveMsg(msg : String) {
    sm.receiveMsg(msg)
  }
  
  def getColour(i : Int, j : Int) = { // colours of board
   if (i%2 == 0){
     if ((i * 8 + j) % 2 == 0) Green else Cyan}
   else 
     if ((i * 8 + j) % 2 == 0) Cyan else Green  
  }
  
  def isInBorders(e_x : Int ,e_y : Int,x : Int, y: Int, x1 : Int, y1: Int) : Boolean = { //determines whether the cursor is within button lines/borders
    var result = false
    if( e_x >= x && e_x <= x + x1 )
      if (e_y >=y && e_y <= y + y1)
        result = true
    result
  }
   
  def setChatPanel(){
    gc.fill = White; gc.fillRect(screenSize,0,chatSize,screenSize)
    gc.strokeText("Chat", chat_text_x, chat_text_y )
    gc.fill = ANTIQUEWHITE; gc.fillRoundRect(chat_window_x, chat_window_y, chat_window_width, chat_window_height, 10, 10)
  }
  
  def updateChatContent(){
    gc.strokeText(sm.chat,chat_window_x,chat_window_y + 13)
  }
  
  def updateSendingContent(){
    gc.strokeText(em.newLine,chat_window_x,500)
  }
  
  def buildChat(){
    setChatPanel()
    placeButton(chat_button_x,chat_b_text_x,"Send")
    placeButton(sticker_button_x,sticker_b_text_x,"Sticker")
    updateChatContent()
    updateSendingContent()
  }
  
  def updateWritingPane(newLine : String){
      gc.fill = White; gc.fillRect(chat_window_x,450,chat_window_width,100)
      gc.strokeText(newLine,chat_window_x,500) 
  }
  
  def placeButton(button_x : Int, b_text_x : Int, desc : String, e_x :Int = -1, e_y : Int = -1){
    if (isInBorders(e_x,e_y.toInt, button_x, chat_button_y , chat_button_width, chat_button_height)) gc.fill = Gray else gc.fill = Blue
    gc.fillRect(button_x, chat_button_y , chat_button_width, chat_button_height)
    gc.strokeText(desc,b_text_x, chat_b_text_y)
  }
  
  def refreshChat(e_x : Int, e_y : Int) {
    placeButton( chat_button_x, chat_b_text_x,"Send",e_x, e_y)
    placeButton(sticker_button_x, sticker_b_text_x,"Sticker", e_x, e_y)
    canvas.onMouseClicked = (e: MouseEvent) =>{
      if (isInBorders(e.x.toInt,e.y.toInt,chat_button_x, chat_button_y , chat_button_width, chat_button_height)) {
        em.clickedSend()
      }
      if (isInBorders(e.x.toInt,e.y.toInt,sticker_button_x, sticker_button_y , sticker_button_width, sticker_button_height)) {
        em.clickedSticker()
      }
    }
  }
  
  def drawSticker(sticker: Sticker){
      val x = sticker.x; val y = sticker.y;
      gc.drawImage(sticker.parts(sticker.index),0,0,50,50,x,y,50,50)
      sticker.index +=1
      if (sticker.index == 4) sticker.index = 0
  }
  
  def board_Draw(){
    buildChat()
    for (i <- 0 until 8)
      for (j <- 0 until 8){
         gc.fill = getColour(i,j)
         gc.fillRect( j*fieldSize, i* fieldSize, fieldSize, fieldSize) 
      }
  }
  
  var lastx = 0
  var lasty = 0
  
  def updateBoard(e_x :   Int, e_y : Int){
     val j = e_y.toInt - e_y.toInt % fieldSize; val i = e_x.toInt - e_x.toInt % fieldSize
     if (i < screenSize){
       gc.fill = getColour(lasty,lastx); gc.fillRect(lasty,lastx,fieldSize,fieldSize)
       gc.fill = Yellow; gc.fillRect(i,j,fieldSize,fieldSize)
       lastx = j; lasty = i
     }
  }
  
  def board_Ev(){
     canvas.onMouseMoved = (e : MouseEvent) =>{
       updateBoard(e.x.toInt,e.y.toInt)
       refreshChat(e.x.toInt,e.y.toInt)
     }  
  }
  
  //phases of connection
  def drawBoard_wait(){
    if (gc == null) gc = canvas.graphicsContext2D
    gc.fill = GOLDENROD; gc.strokeText ("WAIT. CONNECTING WITH ANOTHER PLAYER...",(screenSize+chatSize)/2 - 150,screenSize/2)
  }
  
  def drawBoard_complete(remote : ActorRef, sender : String){
    this.remote = remote
    this.sender = sender
    this.sm =  new SendingManager(this)
    this.em = new EventManager(this,sm)
    gamingPhase = true
    if (gc == null) gc = canvas.graphicsContext2D
    board_Draw()
    board_Ev()
  }
}