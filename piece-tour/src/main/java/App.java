import com.chess.tour.PieceTourSolution;
import com.chess.tour.impl.WarnsdorffImpl;
import com.chess.tour.presentation.Console;
import com.chess.tour.presentation.Gui;

public class App {
    public static void main(String[] args) {

        PieceTourSolution pieceTourSolution = new WarnsdorffImpl();
        System.out.println("For GUI presentation pass gui and for Console does not pass any argument");

        if (args.length == 0) {
            System.out.println("Run in console mode");
            new Console(pieceTourSolution);

        } else if (args.length == 1 && "gui".equalsIgnoreCase(args[0])) {
            System.out.println("Run in GUI mode");
            new Gui(pieceTourSolution);

        } else {
            throw new IllegalArgumentException("Only support gui and console presentation");
        }
    }
}
