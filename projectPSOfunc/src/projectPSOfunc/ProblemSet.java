package projectPSOfunc;

import java.math.BigDecimal;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

public class ProblemSet {
	public static final double LOC_X_LOW = 1;
	public static final double LOC_X_HIGH = 4;
	public static final double LOC_Y_LOW = -1;
	public static final double LOC_Y_HIGH = 1;
	public static final double VEL_LOW = -1;
	public static final double VEL_HIGH = 1;
	private static String func;
	
	public static final double ERR_TOLERANCE = 1E-20; // the smaller the tolerance, the more accurate the result, 
	                                                  // but the number of iteration is increased
	
	public static double evaluate(Location location) {
		double result = 0;
		double x = location.getLoc()[0]; // the "x" part of the location
		double y = location.getLoc()[1]; // the "y" part of the location
		
		result = getEvalFuncDouble(x,y,func);
		
/*		result = Math.pow(2.8125 - x + x * Math.pow(y, 4), 2) + 
				Math.pow(2.25 - x + x * Math.pow(y, 2), 2) + 
				Math.pow(1.5 - x + x * y, 2) +
				Math.exp((x*y)*666);*/
		
		return result;
	}
	public static void setFormula(String function){
		function = function.replaceAll("z","#{z}").replaceAll("y","#{y}");
		
		dialogMsg(function);
		
		func = function;
	}
	
	private static double getEvalFuncDouble(double x, double y, String func) {
		double result=0;
		Evaluator evalEngine = new Evaluator();
		
		evalEngine.putVariable("z", String.valueOf(new BigDecimal(x)));
		evalEngine.putVariable("y", String.valueOf(new BigDecimal(y)));
		try {
			result = evalEngine.getNumberResult(func);
			//result = Double.parseDouble(evalEngine.evaluate(func));
			//System.out.println("<=====ta dika moy====> "+result);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			//dialogMsg("O M G, Num #$$%^##@ !");
			e.printStackTrace();
		} catch (EvaluationException e) {
			// TODO Auto-generated catch block
			//dialogMsg("houston,JEval #$$%^##@ !");
			e.printStackTrace();
		}
		
		return result;
		
	}
	private static void dialogMsg(String msg){
		JOptionPane dialogBox = new JOptionPane();
		dialogBox.showMessageDialog(new JDialog(), msg);
		dialogBox.setVisible(true);
	}
}
