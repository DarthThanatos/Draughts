package local

import Storage._
//import scala.util.control.Breaks._

class SendingManager(parent : Board) {
  
  var stickers = List[Sticker]()
  var StickerY = chat_window_y
  var actualLinesAmount = 0  
  var chat = "" // history of chat
  
  def processChat(newLine : String, msg :String = null){
      if (actualLinesAmount == linesInChat){
        val Lines = chat.split("\n")
        chat  = ""
        for (i <- 1 until Lines.length) chat = chat + Lines(i) + "\n"
        moveStickers(lineHeight)
        StickerY = chat_window_y + chat_window_height - stickerHeight
      }
      else {
        actualLinesAmount += 1
        StickerY += lineHeight 
      }
      if (msg == null)chat = chat + newLine + "\n" else chat = chat + msg + "\n"
  }
  
  def sendMsg(newLine : String){
      processChat(newLine)
      parent.remote ! (parent.sender, newLine)
      parent.buildChat()
      parent.updateWritingPane("You: ")
  }
  
  def receiveMsg(msg : String){
    processChat(msg)
    parent.buildChat()
  }   

  def dealWithSpaces(){
      if (actualLinesAmount == linesInChat){
        val Lines = chat.split("\n")
        chat  = ""
        var start = 0
        while(Lines(start) == " ") start += 1
        actualLinesAmount -= start
        StickerY -= start * lineHeight
        for (sticker <- stickers) sticker.y -= start * lineHeight
        for (i <- start until Lines.length) chat = chat + Lines(i) + "\n"
      }
  }
  
  def moveStickers(dy : Int){
    StickerY = chat_window_y + chat_window_height - stickerHeight
    if (! stickers.isEmpty && stickers.head.y - dy < chat_window_y){
      val head = stickers.head
      stickers = stickers.tail
      head.timer.stop()
      dealWithSpaces()
    }
    for (sticker <- stickers) sticker.y -= dy
  }
  
  def destroyLines(amount : Int){
    val Lines = chat.split("\n")
    chat  = ""
    for (i <-amount  until Lines.length) chat = chat + Lines(i) + "\n"
    moveStickers(amount*lineHeight)
  }
  
  def sendSticker(){
    var x = chat_window_x + 50
    if(actualLinesAmount < linesInChat){
      val delta = linesInChat - actualLinesAmount - linesPerSticker
      println("d")
      if(delta < 0){
        actualLinesAmount = linesInChat
        val lines_to_destroy = math.abs(delta)
        destroyLines(lines_to_destroy)
        stickers = stickers ++ (List(new Sticker(parent,x,StickerY)))
        println("a")
      }
      else{
        actualLinesAmount += linesPerSticker
        stickers = stickers ++ (List(new Sticker(parent,x,StickerY)))
        StickerY += (stickerHeight + lineHeight)
        println("b")
      }
    }
    else{   
      destroyLines(linesPerSticker - 1)
      stickers = stickers ++ (List(new Sticker(parent,x,StickerY)))
      println("c")
      
    }
    chat = chat + "You:\n \n \n \n"
    parent.buildChat()
    println("")
  }
 
}