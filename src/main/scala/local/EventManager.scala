package local

import Storage._
import scalafx.scene.input._

class EventManager(parent : Board, sender : SendingManager) {
  
  var lowerCase = true
  var newLine = "You: "
  
  def specialChars(key : String, code : String) = code match{
    case "SLASH" => "?"
    case "DIGIT1" => "!"
    case "DIGIT9" => "("
    case "DIGIT0" => ")"
    case "SEMICOLON" => ":"
    case _ => key.toUpperCase()     
  }
  
  def managePressKeys(key : String,code : KeyCode) = code.toString match{
    case "ENTER" => sender.sendMsg(newLine) ; newLine = "You: "
    case "SHIFT" => lowerCase = false
    case "CAPS" => if (lowerCase ==false) lowerCase = true else lowerCase = false
    case "BACK_SPACE" => {
      if (newLine.length > 5) newLine = newLine.substring(0,newLine.length - 1)
      parent.updateWritingPane(newLine)
    }
    case _ => {
      var processedKey: String = null
      if (!lowerCase) processedKey = specialChars(key,code.toString()) else processedKey = key
      if (newLine.length < maxRowLength) newLine = newLine + processedKey else {
          if (! newLine.endsWith("...")) newLine = newLine + "..."
      }
      parent.updateWritingPane(newLine)
    }
  }
  
  def manageRelKeys(key : String, code : KeyCode) = code.toString match{
    case "SHIFT" => lowerCase = true
    case _ => 
  }
  
  def clickedSend(){
    sender.sendMsg(newLine)
  }
  
  def clickedSticker(){
    sender.sendSticker()
  }
  

}