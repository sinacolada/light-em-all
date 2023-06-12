package lightemall;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class App {

  @Parameter(names = {"--rows", "-r"})
  int numRows = 7;

  @Parameter(names = {"--cols", "-c"})
  int numCols = 7;
  
  public static void main(String... args) {
    App app = new App();
    JCommander.newBuilder().addObject(app).build().parse(args);
    app.run();
  }

  public void run() {
    LightEmAll lightEmAll = new LightEmAll(numCols, numRows);
    lightEmAll.bigBang(numCols*100, (numRows + 1) * 100, 1);
  }
}
