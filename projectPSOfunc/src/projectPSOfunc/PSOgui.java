package projectPSOfunc;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PSOgui extends JFrame{
	private JTextField tfEquation;
	private JTextField tfSwarmSize;
	private JTextField tfIterations;
	private JTextArea taOutput;
	
	public PSOgui() {
		initGui();
	}

private void initGui(){
	setTitle("Particle Swarm Optimization - Minimization");
	getContentPane().setLayout(null);
	
	
	taOutput = new JTextArea();
	taOutput.setBounds(10, 93, 302, 610);
	taOutput.setEditable(false);
	taOutput.setVisible(true);
	JScrollPane scrollMe = new JScrollPane(taOutput);
	scrollMe.setBounds(10, 93, 302, 610);
	scrollMe.setVisible(true);
	getContentPane().add(scrollMe);
	
	JLabel lblEquation = new JLabel("User, please... Insert equation:");
	lblEquation.setBounds(10, 11, 302, 14);
	getContentPane().add(lblEquation);
	
	tfEquation = new JTextField();
	tfEquation.setBounds(10, 28, 302, 20);
	getContentPane().add(tfEquation);
	tfEquation.setColumns(10);

	
	JLabel lblSwarmSize = new JLabel("Swarm size:");
	lblSwarmSize.setBounds(10, 59, 73, 14);
	getContentPane().add(lblSwarmSize);
	
	tfSwarmSize = new JTextField("0");
	tfSwarmSize.setBounds(85, 59, 71, 20);
	getContentPane().add(tfSwarmSize);
	tfSwarmSize.setColumns(10);
	
	JLabel lblIterations = new JLabel("iterations:");
	lblIterations.setBounds(177, 59, 62, 14);
	getContentPane().add(lblIterations);
	
	tfIterations = new JTextField("0");
	tfIterations.setBounds(241, 59, 71, 20);
	getContentPane().add(tfIterations);
	tfIterations.setColumns(10);
//button and listener START
		JButton btnMinimize = new JButton("minimize");
		btnMinimize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//ProblemSet.setFormula("pow((1.5-#{x}+#{x}*pow(#{y},4)),2)+pow((0.5*#{x}+#{x}*pow(#{y},4)),2)+pow((1+#{x}),2 )");
				//ProblemSet.setFormula("(1.5-x+x*pow(y,2))+(pow(3.5-x+x*pow(y,2),2))+(pow(2-x,2))");
				whenClick();
			}
		});
		btnMinimize.setBounds(322, 27, 102, 23);
		getContentPane().add(btnMinimize);
		
		JLabel lblTzagkarakisMp = new JLabel("TZAGKARAKIS, MP141");
		lblTzagkarakisMp.setBounds(296, 707, 138, 14);
		getContentPane().add(lblTzagkarakisMp);
		
		JLabel lblTrivizakisMp = new JLabel("TRIVIZAKIS, MP143");
		lblTrivizakisMp.setBounds(155, 707, 131, 14);
		getContentPane().add(lblTrivizakisMp);
		
		JLabel lblSinfzy = new JLabel("sin(F(z,y))");
		lblSinfzy.setBounds(331, 94, 93, 14);
		getContentPane().add(lblSinfzy);
		
		JLabel lblCosfzy = new JLabel("cos(F(z,y))");
		lblCosfzy.setBounds(331, 119, 93, 14);
		getContentPane().add(lblCosfzy);
		
		JLabel lblExpfzy = new JLabel("exp(F(z,y))");
		lblExpfzy.setBounds(331, 209, 93, 14);
		getContentPane().add(lblExpfzy);
		
		JLabel lblPowfzyp = new JLabel("pow(F(z,y),p)");
		lblPowfzyp.setBounds(331, 169, 93, 14);
		getContentPane().add(lblPowfzyp);
		
		JLabel lblTanfzy = new JLabel("tan(F(z,y))");
		lblTanfzy.setBounds(331, 144, 93, 14);
		getContentPane().add(lblTanfzy);
		
		JLabel lblSqrtfzy = new JLabel("sqrt(F(z,y))");
		lblSqrtfzy.setBounds(331, 190, 93, 14);
		getContentPane().add(lblSqrtfzy);
//button and listener END
		
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(25, 25, 450, 750);
		setVisible(true);
}

public String getUserFunc(){
	return tfEquation.getText();
}
public int getSwarmSize(){
	return Integer.valueOf(tfSwarmSize.getText());
}
public int getUserIterations(){
	return Integer.valueOf(tfIterations.getText());
}

private String getTfEquation() {
	return tfEquation.getText();
}
private String getTfSwarmSize() {
	return tfSwarmSize.getText();
}
private String getTfIterations() {
	return tfIterations.getText();
}

private JTextArea getOutput() {
	return taOutput;
}
public void setOuput(String toAddStuff){
	taOutput.setText(toAddStuff);
}

private void whenClick() {
	ProblemSet.setFormula(getTfEquation());
	taOutput.setText("");
	//System.out.println(Integer.valueOf(this.getTfSwarmSize())+ "_"+ Integer.valueOf(this.getTfIterations()));
	new PSOProcess().execute(getOutput(), Integer.valueOf(this.getTfSwarmSize()), Integer.valueOf(this.getTfIterations()));
}
}
