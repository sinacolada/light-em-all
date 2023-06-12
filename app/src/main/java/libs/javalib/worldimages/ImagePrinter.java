package libs.javalib.worldimages;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

class ImagePrinter {
   private static Map<Integer, String> indentations = new HashMap<>();

   private static String indentation(int INDENT) {
      String ans = indentations.get(INDENT);
      if (ans == null) {
         char[] spaces = new char[INDENT];
         Arrays.fill(spaces, ' ');
         ans = new String(spaces);
         indentations.put(INDENT, ans);
      }

      return ans;
   }

   private static boolean isWrapperClass(String name) {
      return name.equals("java.lang.Integer")
         || name.equals("java.lang.Long")
         || name.equals("java.lang.Short")
         || name.equals("java.math.BigInteger")
         || name.equals("java.math.BigDecimal")
         || name.equals("java.lang.Float")
         || name.equals("java.lang.Double")
         || name.equals("java.lang.Byte")
         || name.equals("java.lang.Boolean")
         || name.equals("java.lang.Character");
   }

   private static <T> String makePrimitiveStrings(String className, T value) {
      StringBuilder result = new StringBuilder();
      if (className.equals("java.lang.Short")) {
         return result + value.toString() + "S";
      } else if (className.equals("java.lang.Long")) {
         return result + value.toString() + "L";
      } else if (className.equals("java.lang.Float")) {
         return result + value.toString() + "F";
      } else if (className.equals("java.math.BigInteger")) {
         return result + value.toString() + "BigInteger";
      } else {
         return className.equals("java.math.BigDecimal") ? result + value.toString() + "BigDecimal" : result + value.toString();
      }
   }

   static StringBuilder makeString(Object obj, StringBuilder sb, String linePrefix, int indent) {
      int INDENT = 0;
      Stack<Object> worklist = new Stack<>();
      int valueCount = 0;
      worklist.push(obj);

      while(!worklist.empty()) {
         ++valueCount;
         obj = worklist.peek();
         if (worklist.size() > 100 || valueCount > 1000) {
            if (!(obj instanceof FieldsWLItem)) {
               if (obj == null) {
                  sb = sb.append("null");
               } else if (obj instanceof String) {
                  sb = sb.append("\"").append(((String)obj).replace("\\", "\\\\").replace("\"", "\\\"")).append("\"");
               } else if (obj instanceof Random) {
                  sb = sb.append("new Random()");
               } else if (obj instanceof Color) {
                  sb = formatColor((Color)obj, sb);
               } else if (obj instanceof Enum) {
                  Enum<?> e = (Enum)obj;
                  sb = sb.append(e.getDeclaringClass().getName().replace('$', '.')).append(".").append(e.name());
               } else if (obj.getClass().isPrimitive() || isWrapperClass(obj.getClass().getName())) {
                  sb = sb.append(makePrimitiveStrings(obj.getClass().getName(), obj));
               } else if (valueCount > 1000) {
                  sb = sb.append("<truncated; too many objects to print>");
               } else {
                  sb = sb.append("<truncated; objects are too deeply nested to print>");
               }

               worklist.pop();
               continue;
            }

            ((FieldsWLItem)obj).skipToEnd();
         }

         if (obj == null) {
            sb = sb.append("null");
            worklist.pop();
         } else if (obj instanceof String) {
            sb = sb.append("\"").append(((String)obj).replace("\\", "\\\\").replace("\"", "\\\"")).append("\"");
            worklist.pop();
         } else if (obj instanceof Random) {
            sb = sb.append("new Random()");
            worklist.pop();
         } else if (obj instanceof Color) {
            sb = formatColor((Color)obj, sb);
            worklist.pop();
         } else if (obj instanceof Posn) {
            sb = sb.append(((Posn)obj).coords());
            worklist.pop();
         } else if (obj instanceof Enum) {
            Enum<?> e = (Enum)obj;
            sb = sb.append(e.getDeclaringClass().getSimpleName().replace('$', '.')).append(".").append(e.name());
            worklist.pop();
         } else if (obj.getClass().isPrimitive() || isWrapperClass(obj.getClass().getName())) {
            sb = sb.append(makePrimitiveStrings(obj.getClass().getName(), obj));
            worklist.pop();
         } else if (obj instanceof WorldImage) {
            WorldImage img = (WorldImage)obj;
            INDENT += indent;
            worklist.pop();
            sb = img.toIndentedStringHelp(sb, worklist);
         } else if (obj instanceof FieldsWLItem) {
            FieldsWLItem fieldsWLItem = (FieldsWLItem)obj;
            if (fieldsWLItem.hasNext()) {
               if (!fieldsWLItem.first()) {
                  sb = sb.append(",");
               }

               ImageField f = fieldsWLItem.next();
               if (f.noNewlineBefore) {
                  sb = sb.append(" ");
               } else {
                  sb = sb.append("\n").append(linePrefix).append(indentation(INDENT));
               }

               sb = sb.append("this.").append(f.name).append(" = ");
               worklist.push(f.value);
            } else {
               INDENT -= indent;
               sb = sb.append(")");
               worklist.pop();
            }
         } else {
            sb = sb.append("Unknown object: ").append(obj.getClass().getName()).append("\n").append(obj.toString());
            worklist.pop();
         }
      }

      return sb;
   }

   private static StringBuilder formatColor(Color obj, StringBuilder sb) {
      sb = sb.append("[r=").append(obj.getRed()).append(",g=").append(obj.getGreen()).append(",b=").append(obj.getBlue());
      if (obj.getAlpha() < 255) {
         sb = sb.append(",a=").append(obj.getAlpha());
      }

      return sb.append("]");
   }
}
