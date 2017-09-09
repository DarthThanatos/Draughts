package local

object Storage {
  
  val screenSize = 600
  val chatSize = 600
  val chat_text_x = screenSize + chatSize/2;
  val chat_text_y = 50;
  val chat_window_x = screenSize + 20;
  val chat_window_y = chat_text_y + 20;
  val chat_window_width = chatSize - 40; 
  val chat_window_height = 300;
  val lineHeight = 16
  val stickerHeight = 50
  val linesPerSticker = 4
  
  val fieldSize = screenSize/8
  val maxRowLength = 78
  val linesInChat = 19
  
  val chat_button_width = 90; 
  val chat_button_height = 40; 
  val chat_button_x = screenSize + chatSize - 20 - chat_button_width ; 
  val chat_button_y = chat_window_y + chat_window_height + 20; 
  val chat_b_text_x = chat_button_x + chat_button_width/3;
  val chat_b_text_y = chat_button_y + chat_button_height/2;
  
  val sticker_button_x = chat_button_x - chat_button_width - 20
  val sticker_button_y = chat_button_y
  val sticker_button_height = chat_button_height
  val sticker_button_width = chat_button_width
  val sticker_b_text_x = sticker_button_x + sticker_button_width/3
  val sticker_b_text_y = sticker_button_y + sticker_button_height/2
}