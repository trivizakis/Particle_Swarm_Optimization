package projectPSOfunc;

import java.text.DecimalFormat;
import java.util.ArrayList;

// the code is for 2-dimensional space problem

import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import plot.JFrameWithPicture;
import plot.MatlabChart;

public class PSOProcess implements PSOConstants {
	private Vector<Particle> swarm = new Vector<Particle>();
	private double[] pBest;
	private Vector<Location> pBestLocation = new Vector<Location>();
	private double gBest;
	private Location gBestLocation;
	private double[] fitnessValueList;
	private JTextArea outputGui;
	private ArrayList<Double> finalValues = new ArrayList<Double>();
	private ArrayList<Double> iterationsNumber= new ArrayList<Double>();
	private static int swarmy;
	private static int iterationsMax;
	private long startTime;
	
	Random generator = new Random();
	
	public void execute(JTextArea gui, int swarms, int iterations) {
		startTime = System.currentTimeMillis();
		this.outputGui=gui;
		
		if(swarms>15)
			swarmy = swarms;
		else
			swarmy = SWARM_SIZE;
		
		if(iterations>15)
			iterationsMax = iterations;
		else
			iterationsMax = MAX_ITERATION;
		
		pBest = new double[swarmy];
		fitnessValueList = new double[swarmy];
		
		initializeSwarm();
		updateFitnessList();
		
		for(int i=0; i<swarmy; i++) {
			pBest[i] = fitnessValueList[i];
			pBestLocation.add(swarm.get(i).getLocation());
		}
		
		int step = 0;
		int t = 0;
		double w;
		double err = 9999;
		
		while(t < iterationsMax && err > ProblemSet.ERR_TOLERANCE) {
			// step 1 - update pBest
			for(int i=0; i<swarmy; i++) {
				if(fitnessValueList[i] < pBest[i]) {
					pBest[i] = fitnessValueList[i];
					pBestLocation.set(i, swarm.get(i).getLocation());
				}
			}
				
			// step 2 - update gBest
			int bestParticleIndex = PSOUtility.getMinPos(fitnessValueList);
			if(t == 0 || fitnessValueList[bestParticleIndex] < gBest) {
				gBest = fitnessValueList[bestParticleIndex];
				gBestLocation = swarm.get(bestParticleIndex).getLocation();
			}
			
			w = W_UPPERBOUND - (((double) t) / iterationsMax) * (W_UPPERBOUND - W_LOWERBOUND);
			
			for(int i=0; i<swarmy; i++) {
				double r1 = generator.nextDouble();
				double r2 = generator.nextDouble();
				
				Particle p = swarm.get(i);
				
				// step 3 - update velocity
				double[] newVel = new double[PROBLEM_DIMENSION];
				
				newVel[0] = (w * p.getVelocity().getPos()[0]) + 
							(r1 * C1) * (pBestLocation.get(i).getLoc()[0] - p.getLocation().getLoc()[0]) +
							(r2 * C2) * (gBestLocation.getLoc()[0] - p.getLocation().getLoc()[0]);
				newVel[1] = (w * p.getVelocity().getPos()[1]) + 
							(r1 * C1) * (pBestLocation.get(i).getLoc()[1] - p.getLocation().getLoc()[1]) +
							(r2 * C2) * (gBestLocation.getLoc()[1] - p.getLocation().getLoc()[1]);
				
				Velocity vel = new Velocity(newVel);
				p.setVelocity(vel);
				
				// step 4 - update location
				double[] newLoc = new double[PROBLEM_DIMENSION];
				newLoc[0] = p.getLocation().getLoc()[0] + newVel[0];
				newLoc[1] = p.getLocation().getLoc()[1] + newVel[1];
				Location loc = new Location(newLoc);
				p.setLocation(loc);
			}
			
				err = ProblemSet.evaluate(gBestLocation) - 0; 				// minimizing the functions means it's getting closer to 0
			
			
				finalValues.add(ProblemSet.evaluate(gBestLocation));
				
				iterationsNumber.add((double)t);
			if(t==0){
				DecimalFormat df = new DecimalFormat("#"); 
				df.setMaximumFractionDigits(1);
				String str =df.format(((gBestLocation.getLoc()[1])/(gBestLocation.getLoc()[0]))*100).toString();
				this.outputGui.setText(outputGui.getText()+
						"ratio of y/z="+ str +"%\n");	
			}
			if(t==step){
				this.outputGui.setText(outputGui.getText()+
									"ITERATION " + t + ": \n"+
									"     Best Z: " + gBestLocation.getLoc()[0] +"\n"+
									"     Best Y: " + gBestLocation.getLoc()[1] +"\n"+
									"     Value: " + ProblemSet.evaluate(gBestLocation) +"\n");
				step+=10;
			}
			
			/*System.out.println("ITERATION " + t +" ("+step+ "): ");
			System.out.println("     Best X: " + gBestLocation.getLoc()[0]);
			System.out.println("     Best Y: " + gBestLocation.getLoc()[1]);
			System.out.println("     Value: " + ProblemSet.evaluate(gBestLocation));
			*/
			t++;
			updateFitnessList();
		}
		
		step=0;
		/*System.out.println();
		System.out.println("     Best X: " + gBestLocation.getLoc()[0]);
		System.out.println("     Best Y: " + gBestLocation.getLoc()[1]);*/
		this.outputGui.setText(outputGui.getText()+
				"********************************************************\n"+
				"Solution found at iteration -" + (t) + "-, the solutions is:\n"+
				"     Best Z: " + gBestLocation.getLoc()[0] +"\n"+
				"     Best Y: " + gBestLocation.getLoc()[1] +"\n"+
				"     Value: " + ProblemSet.evaluate(gBestLocation) +"\n"+
				"********************************************************\n");
		finalValues.add(ProblemSet.evaluate(gBestLocation));
		iterationsNumber.add((double)(t));
		long stopTime = System.currentTimeMillis();
		this.outputGui.setText(outputGui.getText()+
				"Execution time: "+ (stopTime-startTime) + " millisec."
				);
		plotValues();
	}
	
	private void plotValues() {
		double[] listFit = new double[finalValues.size()];
		double[] listIt = new double[iterationsNumber.size()];
		
		for (int i = 0; i < listFit.length; i++) {
		    listFit[i] = finalValues.get(i).doubleValue();
		    listIt[i] = iterationsNumber.get(i).doubleValue();
		}

		finalValues.clear();
		iterationsNumber.clear();
		
		MatlabChart fig = new MatlabChart();
		fig.plot(listFit, listIt , ":k", 3.0f, "PSO:Min!");
		fig.RenderPlot();                    
        fig.title("Particle Swarm Optimization");
		double xlim1 = listFit[0];
		double xlim2 = listFit[listFit.length-1];
		//System.out.println("xlim1: "+xlim1+" xlim2:"+xlim2+"\n");
        fig.xlim(xlim2, xlim1);
        fig.ylim(0, iterationsMax);                  
        fig.xlabel("f(z,y)");                  
        fig.ylabel("Iterations");                 
        fig.grid("on","on");                 
        fig.legend("northeast");             
        fig.font("Helvetica",15);
        fig.saveas("MyPlot.jpeg",640,480);
        plotFrame();
        
		/*
		// JAVA:                             // MATLAB:
        MatlabChart fig = new MatlabChart(); // figure('Position',[100 100 640 480]);
        fig.plot(x, y1, "-r", 2.0f, "AAPL"); // plot(x,y1,'-r','LineWidth',2);
        fig.plot(x, y2, ":k", 3.0f, "BAC");  // plot(x,y2,':k','LineWidth',3);
        fig.RenderPlot();                    // First render plot before modifying
        fig.title("Stock 1 vs. Stock 2");    // title('Stock 1 vs. Stock 2');
        fig.xlim(10, 100);                   // xlim([10 100]);
        fig.ylim(200, 300);                  // ylim([200 300]);
        fig.xlabel("Days");                  // xlabel('Days');
        fig.ylabel("Price");                 // ylabel('Price');
        fig.grid("on","on");                 // grid on;
        fig.legend("northeast");             // legend('AAPL','BAC','Location','northeast')
        fig.font("Helvetica",15);            // .. 'FontName','Helvetica','FontSize',15
        fig.saveas("MyPlot.jpeg",640,480);   // saveas(gcf,'MyPlot','jpeg');
		*/
	}

	public void initializeSwarm() {
		Particle p;
		for(int i=0; i<swarmy; i++) {
			p = new Particle();
			
			// randomize location inside a space defined in Problem Set
			double[] loc = new double[PROBLEM_DIMENSION];
			loc[0] = ProblemSet.LOC_X_LOW + generator.nextDouble() * (ProblemSet.LOC_X_HIGH - ProblemSet.LOC_X_LOW);
			loc[1] = ProblemSet.LOC_Y_LOW + generator.nextDouble() * (ProblemSet.LOC_Y_HIGH - ProblemSet.LOC_Y_LOW);
			Location location = new Location(loc);
			
			// randomize velocity in the range defined in Problem Set
			double[] vel = new double[PROBLEM_DIMENSION];
			vel[0] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			vel[1] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			Velocity velocity = new Velocity(vel);
			
			p.setLocation(location);
			p.setVelocity(velocity);
			swarm.add(p);
		}
	}
	
	public void updateFitnessList() {
		for(int i=0; i<swarmy; i++) {
			/*System.out.println("*******************************");
			System.out.println("********Swarm fittness" + swarm.get(i).getFitnessValue());
			System.out.println("*******************************");*/
			fitnessValueList[i] = swarm.get(i).getFitnessValue();
		}
	}
	public void plotFrame(){
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new JFrameWithPicture();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
