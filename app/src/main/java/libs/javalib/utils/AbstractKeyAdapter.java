package libs.javalib.utils;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Locale;

public class AbstractKeyAdapter extends KeyAdapter {
   protected AbstractKeyAdapter.Consumer<String> onKey;
   protected AbstractKeyAdapter.Consumer<String> onReleased;

   public AbstractKeyAdapter(AbstractKeyAdapter.Consumer<String> onKey, AbstractKeyAdapter.Consumer<String> onReleased) {
      this.onKey = onKey;
      this.onReleased = onReleased;
   }

   @Override
   public void keyTyped(KeyEvent e) {
   }

   @Override
   public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() != 0) {
         this.onKey.apply(this.getNamedKey(e));
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {
      if (e.getKeyCode() != 0) {
         this.onReleased.apply(this.getNamedKey(e));
      }
   }

   String getNamedKey(KeyEvent e) {
      return this.getLocationPrefix(e) + this.getKeyName(e);
   }

   String getKeyName(KeyEvent e) {
      Locale.setDefault(Locale.ROOT);
      switch(e.getKeyChar()) {
         case '\b':
            return "backspace";
         case '\t':
            return "tab";
         case '\n':
            return "enter";
         case '\u001b':
            return "escape";
         case '\u007f':
            return "delete";
         case '\uffff':
            return getKeyText(e.getKeyCode()).toLowerCase().replace(' ', '-');
         default:
            return "" + e.getKeyChar();
      }
   }

   String getLocationPrefix(KeyEvent e) {
      switch(e.getKeyLocation()) {
         case 3:
            return "right-";
         case 4:
            return "numpad-";
         default:
            return "";
      }
   }

   public static String getKeyText(int keyCode) {
      if ((keyCode < 48 || keyCode > 57) && (keyCode < 65 || keyCode > 90)) {
         switch(keyCode) {
            case 3:
               return "Cancel";
            case 8:
               return "Backspace";
            case 9:
               return "Tab";
            case 10:
               return "Enter";
            case 12:
               return "Clear";
            case 16:
               return "Shift";
            case 17:
               return "Control";
            case 18:
               return "Alt";
            case 19:
               return "Pause";
            case 20:
               return "Caps Lock";
            case 21:
               return "Kana";
            case 24:
               return "Final";
            case 25:
               return "Kanji";
            case 27:
               return "Escape";
            case 28:
               return "Convert";
            case 29:
               return "No Convert";
            case 30:
               return "Accept";
            case 31:
               return "Mode Change";
            case 32:
               return "Space";
            case 33:
               return "Page Up";
            case 34:
               return "Page Down";
            case 35:
               return "End";
            case 36:
               return "Home";
            case 37:
               return "Left";
            case 38:
               return "Up";
            case 39:
               return "Right";
            case 40:
               return "Down";
            case 44:
               return "Comma";
            case 45:
               return "Minus";
            case 46:
               return "Period";
            case 47:
               return "Slash";
            case 59:
               return "Semicolon";
            case 61:
               return "Equals";
            case 91:
               return "Open Bracket";
            case 92:
               return "Back Slash";
            case 93:
               return "Close Bracket";
            case 106:
               return "NumPad *";
            case 107:
               return "NumPad +";
            case 108:
               return "NumPad ,";
            case 109:
               return "NumPad -";
            case 110:
               return "NumPad .";
            case 111:
               return "NumPad /";
            case 112:
               return "F1";
            case 113:
               return "F2";
            case 114:
               return "F3";
            case 115:
               return "F4";
            case 116:
               return "F5";
            case 117:
               return "F6";
            case 118:
               return "F7";
            case 119:
               return "F8";
            case 120:
               return "F9";
            case 121:
               return "F10";
            case 122:
               return "F11";
            case 123:
               return "F12";
            case 127:
               return "Delete";
            case 128:
               return "Dead Grave";
            case 129:
               return "Dead Acute";
            case 130:
               return "Dead Circumflex";
            case 131:
               return "Dead Tilde";
            case 132:
               return "Dead Macron";
            case 133:
               return "Dead Breve";
            case 134:
               return "Dead Above Dot";
            case 135:
               return "Dead Diaeresis";
            case 136:
               return "Dead Above Ring";
            case 137:
               return "Dead Double Acute";
            case 138:
               return "Dead Caron";
            case 139:
               return "Dead Cedilla";
            case 140:
               return "Dead Ogonek";
            case 141:
               return "Dead Iota";
            case 142:
               return "Dead Voiced Sound";
            case 143:
               return "Dead Semivoiced Sound";
            case 144:
               return "Num Lock";
            case 145:
               return "Scroll Lock";
            case 150:
               return "Ampersand";
            case 151:
               return "Asterisk";
            case 152:
               return "Double Quote";
            case 153:
               return "Less";
            case 154:
               return "Print Screen";
            case 155:
               return "Insert";
            case 156:
               return "Help";
            case 157:
               return "Meta";
            case 160:
               return "Greater";
            case 161:
               return "Left Brace";
            case 162:
               return "Right Brace";
            case 192:
               return "Back Quote";
            case 222:
               return "Quote";
            case 224:
               return "Up";
            case 225:
               return "Down";
            case 226:
               return "Left";
            case 227:
               return "Right";
            case 240:
               return "Alphanumeric";
            case 241:
               return "Katakana";
            case 242:
               return "Hiragana";
            case 243:
               return "Full-Width";
            case 244:
               return "Half-Width";
            case 245:
               return "Roman Characters";
            case 256:
               return "All Candidates";
            case 257:
               return "Previous Candidate";
            case 258:
               return "Code Input";
            case 259:
               return "Japanese Katakana";
            case 260:
               return "Japanese Hiragana";
            case 261:
               return "Japanese Roman";
            case 262:
               return "Kana Lock";
            case 263:
               return "Input Method On/Off";
            case 512:
               return "At";
            case 513:
               return "Colon";
            case 514:
               return "Circumflex";
            case 515:
               return "Dollar";
            case 516:
               return "Euro";
            case 517:
               return "Exclamation Mark";
            case 518:
               return "Inverted Exclamation Mark";
            case 519:
               return "Left Parenthesis";
            case 520:
               return "Number Sign";
            case 521:
               return "Plus";
            case 522:
               return "Right Parenthesis";
            case 523:
               return "Underscore";
            case 524:
               return "Windows";
            case 525:
               return "Context Menu";
            case 61440:
               return "F13";
            case 61441:
               return "F14";
            case 61442:
               return "F15";
            case 61443:
               return "F16";
            case 61444:
               return "F17";
            case 61445:
               return "F18";
            case 61446:
               return "F19";
            case 61447:
               return "F20";
            case 61448:
               return "F21";
            case 61449:
               return "F22";
            case 61450:
               return "F23";
            case 61451:
               return "F24";
            case 65312:
               return "Compose";
            case 65368:
               return "Begin";
            case 65406:
               return "Alt Graph";
            case 65480:
               return "Stop";
            case 65481:
               return "Again";
            case 65482:
               return "Props";
            case 65483:
               return "Undo";
            case 65485:
               return "Copy";
            case 65487:
               return "Paste";
            case 65488:
               return "Find";
            case 65489:
               return "Cut";
            default:
               if (keyCode >= 96 && keyCode <= 105) {
                  String numpad = "NumPad";
                  char c = (char)(keyCode - 96 + 48);
                  return numpad + "-" + c;
               } else if ((keyCode & 16777216) != 0) {
                  return String.valueOf((char)(keyCode ^ 16777216));
               } else {
                  String unknown = "Unknown";
                  return unknown + " keyCode: 0x" + Integer.toString(keyCode, 16);
               }
         }
      } else {
         return String.valueOf((char)keyCode);
      }
   }

   public interface Consumer<T> {
      void apply(T var1);
   }
}
