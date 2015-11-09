import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.math.RoundingMode;
import java.text.*;

public class IntegraPanel extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 362399698711709804L;

	private static final String[] FUNC_TYPES = {"ax^2 + bx + c",
		"ax^3 + bx^2 + cx",
		"ax * exp(-(b*x))",
		"log(x)"};

	private static final String[] CMETHODS = {"Midrectangle",
		"Trapezium",
		"Simpson",
		"Gaussian"};

	private JTextArea verboseOut;
	private JComboBox funcList, methodList;
	private JSpinner avarVal, bvarVal, cvarVal, intFromVal, intToVal;
	private JFormattedTextField desErrVal, rsltErrVal, rsltVal;
	private JButton processDataBtn, paintSomethingBtn;
	private NumberFormat digiform;
	private IntegraPh graphItself;
	private double intFrom, intTo, desErr, paramA, paramB, paramC;

	public IntegraPanel()
	{
		super(new BorderLayout());

		try  
		{  
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}  
		catch(Exception e)  
		{
		}

		intFrom = 1.0;
		intTo = 2.0;
		paramA = 1.0;
		paramB = 1.0;
		paramC = 1.0;
		desErr = 0.5;

		/*
		 * Elements
		 */
		digiform = DecimalFormat.getNumberInstance();
		//digiform = NumberFormat.getNumberInstance();
		//digiform.setRoundingMode(RoundingMode.HALF_EVEN);
		digiform.setMaximumIntegerDigits(Integer.MAX_VALUE);
		digiform.setMaximumFractionDigits(Integer.MAX_VALUE);
		digiform.setMinimumFractionDigits(4);

		funcList = new JComboBox(FUNC_TYPES);
		funcList.setSelectedIndex(3);

		methodList = new JComboBox(CMETHODS);
		methodList.setSelectedIndex(0);

		JLabel avarlb, bvarlb, cvarlb, revarlb, rsltlb1, rsltlb2;
		avarlb = new JLabel("A:");
		bvarlb = new JLabel("B:");
		cvarlb = new JLabel("C:");
		revarlb = new JLabel("pogr:");
		rsltlb1 = new JLabel("result:");
		rsltlb2 = new JLabel("pogr:");

		avarVal = new JSpinner(new SpinnerNumberModel(paramA, -99999999.0, 99999999.0, 0.001));
		bvarVal = new JSpinner(new SpinnerNumberModel(paramB, -99999999.0, 99999999.0, 0.001));
		cvarVal = new JSpinner(new SpinnerNumberModel(paramC, -99999999.0, 99999999.0, 0.001));
		intFromVal = new JSpinner(new SpinnerNumberModel(intFrom, -99999999.0, 99999999.0, 0.001));
		intToVal = new JSpinner(new SpinnerNumberModel(intTo, -99999999.0, 99999999.0, 0.001));
		desErrVal = new JFormattedTextField(digiform);
		desErrVal.setColumns(12);
		desErrVal.setValue(desErr);

		rsltErrVal = new JFormattedTextField(digiform);
		rsltErrVal.setColumns(12);
		rsltVal = new JFormattedTextField(digiform);
		rsltVal.setColumns(12);

		processDataBtn = new JButton("calc");
		paintSomethingBtn = new JButton("paint");
		
		verboseOut = new JTextArea(6, 36);
		verboseOut.setFont(getFont());
		verboseOut.setLineWrap(true);
		verboseOut.setWrapStyleWord(true);
		
		/*
		 * Action listener mod
		 */
		processDataBtn.addActionListener(this);
		paintSomethingBtn.addActionListener(this);
		

		/*
		 * Panels
		 */
		JPanel leftPane = null, coordPane, commandPane, solutionPane, progressPane;
		JScrollPane scroller;
		leftPane = new JPanel();
		coordPane = new JPanel();
		solutionPane = new JPanel();
		commandPane = new JPanel(new GridLayout(1, 2, 6, 6));
		progressPane = new JPanel(new BorderLayout());
		scroller = new JScrollPane(verboseOut);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		JPanel graphContentPane = new JPanel(new BorderLayout());
		
		graphItself = new IntegraPh();
		graphItself.setDaddy(this);
		graphItself.setBackground(new Color(255,255,255));

		/*
		 * Borders
		 */
		Border sixPixels = new EmptyBorder(4, 4, 4, 4);
		coordPane.setBorder(BorderFactory.createTitledBorder("coords"));
		solutionPane.setBorder(BorderFactory.createTitledBorder("solution"));
		commandPane.setBorder(sixPixels);
		progressPane.setBorder(BorderFactory.createTitledBorder("verbose output"));
		graphContentPane.setBorder(new CompoundBorder(sixPixels, new CompoundBorder(BorderFactory.createRaisedBevelBorder(), sixPixels)));

		/*
		 * Preferred sizes
		 */
		graphContentPane.setMinimumSize(new Dimension(400, 400));
		graphContentPane.setPreferredSize(new Dimension(600, 600));
		scroller.setMinimumSize(new Dimension(100, 100));
		scroller.setPreferredSize(new Dimension(100, 100));
		//commandPane.setMinimumSize(new Dimension(100, 100));
		//commandPane.setPreferredSize(new Dimension(100, 100));

		/*
		 * Laying out elements
		 */
		commandPane.add(processDataBtn);
		commandPane.add(paintSomethingBtn);

		GridBagConstraints coordPaneLayoutCon = new GridBagConstraints();
		GridBagLayout coordPaneLayout = new GridBagLayout();
		coordPane.setLayout(coordPaneLayout);
		gbcBuildCon(coordPaneLayoutCon, 0, 0, 2, 1, 0, 10); // selectbox
		coordPaneLayoutCon.fill = GridBagConstraints.HORIZONTAL;
		coordPaneLayoutCon.anchor = GridBagConstraints.NORTH;
		coordPaneLayout.setConstraints(funcList, coordPaneLayoutCon);
		coordPane.add(funcList);

		gbcBuildCon(coordPaneLayoutCon, 0, 1, 2, 1, 0, 10); // selectbox
		coordPaneLayoutCon.fill = GridBagConstraints.HORIZONTAL;
		coordPaneLayoutCon.anchor = GridBagConstraints.NORTH;
		coordPaneLayout.setConstraints(methodList, coordPaneLayoutCon);
		coordPane.add(methodList);

		gbcBuildCon(coordPaneLayoutCon, 0, 2, 1, 1, 0, 10); // label 1
		coordPaneLayoutCon.fill = GridBagConstraints.NONE;
		coordPaneLayoutCon.anchor = GridBagConstraints.EAST;
		coordPaneLayout.setConstraints(intFromVal, coordPaneLayoutCon);
		coordPane.add(intFromVal);
		
		gbcBuildCon(coordPaneLayoutCon, 1, 2, 1, 1, 0, 10); // label 1
		coordPaneLayoutCon.fill = GridBagConstraints.HORIZONTAL;
		coordPaneLayoutCon.anchor = GridBagConstraints.WEST;
		coordPaneLayout.setConstraints(intToVal, coordPaneLayoutCon);
		coordPane.add(intToVal);

		gbcBuildCon(coordPaneLayoutCon, 0, 3, 1, 1, 0, 10); // label 1
		coordPaneLayoutCon.fill = GridBagConstraints.NONE;
		coordPaneLayoutCon.anchor = GridBagConstraints.EAST;
		coordPaneLayout.setConstraints(avarlb, coordPaneLayoutCon);
		coordPane.add(avarlb);
		
		gbcBuildCon(coordPaneLayoutCon, 1, 3, 1, 1, 0, 10); // label 1
		coordPaneLayoutCon.fill = GridBagConstraints.HORIZONTAL;
		coordPaneLayoutCon.anchor = GridBagConstraints.WEST;
		coordPaneLayout.setConstraints(avarVal, coordPaneLayoutCon);
		coordPane.add(avarVal);

		gbcBuildCon(coordPaneLayoutCon, 0, 4, 1, 1, 0, 10); // label 2
		coordPaneLayoutCon.fill = GridBagConstraints.NONE;
		coordPaneLayoutCon.anchor = GridBagConstraints.EAST;
		coordPaneLayout.setConstraints(bvarlb, coordPaneLayoutCon);
		coordPane.add(bvarlb);
		
		gbcBuildCon(coordPaneLayoutCon, 1, 4, 1, 1, 0, 10); // label 2
		coordPaneLayoutCon.fill = GridBagConstraints.HORIZONTAL;
		coordPaneLayoutCon.anchor = GridBagConstraints.WEST;
		coordPaneLayout.setConstraints(bvarVal, coordPaneLayoutCon);
		coordPane.add(bvarVal);

		gbcBuildCon(coordPaneLayoutCon, 0, 5, 1, 1, 0, 10); // label 3
		coordPaneLayoutCon.fill = GridBagConstraints.NONE;
		coordPaneLayoutCon.anchor = GridBagConstraints.EAST;
		coordPaneLayout.setConstraints(cvarlb, coordPaneLayoutCon);
		coordPane.add(cvarlb);
		
		gbcBuildCon(coordPaneLayoutCon, 1, 5, 1, 1, 0, 10); // label 3
		coordPaneLayoutCon.fill = GridBagConstraints.HORIZONTAL;
		coordPaneLayoutCon.anchor = GridBagConstraints.WEST;
		coordPaneLayout.setConstraints(cvarVal, coordPaneLayoutCon);
		coordPane.add(cvarVal);

		gbcBuildCon(coordPaneLayoutCon, 0, 6, 1, 1, 0, 10); // label 4
		coordPaneLayoutCon.fill = GridBagConstraints.NONE;
		coordPaneLayoutCon.anchor = GridBagConstraints.EAST;
		coordPaneLayout.setConstraints(revarlb, coordPaneLayoutCon);
		coordPane.add(revarlb);
		
		gbcBuildCon(coordPaneLayoutCon, 1, 6, 1, 1, 0, 10); // label 4
		coordPaneLayoutCon.fill = GridBagConstraints.HORIZONTAL;
		coordPaneLayoutCon.anchor = GridBagConstraints.WEST;
		coordPaneLayout.setConstraints(desErrVal, coordPaneLayoutCon);
		coordPane.add(desErrVal);

		GridBagConstraints solutionPaneLayoutCon = new GridBagConstraints();
		GridBagLayout solutionPaneLayout = new GridBagLayout();
		solutionPane.setLayout(solutionPaneLayout);
		gbcBuildCon(solutionPaneLayoutCon, 0, 0, 1, 1, 0, 10); // label 1
		solutionPaneLayoutCon.fill = GridBagConstraints.NONE;
		solutionPaneLayoutCon.anchor = GridBagConstraints.EAST;
		solutionPaneLayout.setConstraints(rsltlb1, solutionPaneLayoutCon);
		solutionPane.add(rsltlb1);

		gbcBuildCon(solutionPaneLayoutCon, 1, 0, 1, 1, 0, 10); // label 1
		solutionPaneLayoutCon.fill = GridBagConstraints.HORIZONTAL;
		solutionPaneLayoutCon.anchor = GridBagConstraints.WEST;
		solutionPaneLayout.setConstraints(rsltVal, solutionPaneLayoutCon);
		solutionPane.add(rsltVal);

		gbcBuildCon(solutionPaneLayoutCon, 0, 1, 1, 1, 0, 10); // label 1
		solutionPaneLayoutCon.fill = GridBagConstraints.NONE;
		solutionPaneLayoutCon.anchor = GridBagConstraints.EAST;
		solutionPaneLayout.setConstraints(rsltlb2, solutionPaneLayoutCon);
		solutionPane.add(rsltlb2);

		gbcBuildCon(solutionPaneLayoutCon, 1, 1, 1, 1, 0, 10); // label 1
		solutionPaneLayoutCon.fill = GridBagConstraints.HORIZONTAL;
		solutionPaneLayoutCon.anchor = GridBagConstraints.WEST;
		solutionPaneLayout.setConstraints(rsltErrVal, solutionPaneLayoutCon);
		solutionPane.add(rsltErrVal);

		progressPane.add(scroller, BorderLayout.CENTER);

		graphContentPane.add(graphItself, BorderLayout.CENTER);

		GridBagConstraints leftPaneLayoutCon = new GridBagConstraints();
		GridBagLayout leftPaneLayout = new GridBagLayout();
		leftPane.setLayout(leftPaneLayout);
		gbcBuildCon(leftPaneLayoutCon, 0, 0, 1, 1, 0, 0); // label 1
		leftPaneLayoutCon.fill = GridBagConstraints.HORIZONTAL;
		leftPaneLayoutCon.anchor = GridBagConstraints.NORTH;
		leftPaneLayout.setConstraints(coordPane, leftPaneLayoutCon);
		leftPane.add(coordPane);

		gbcBuildCon(leftPaneLayoutCon, 0, 1, 1, GridBagConstraints.RELATIVE, 0, 0); // label 1
		leftPaneLayoutCon.fill = GridBagConstraints.HORIZONTAL;
		leftPaneLayoutCon.anchor = GridBagConstraints.NORTH;
		leftPaneLayout.setConstraints(solutionPane, leftPaneLayoutCon);
		leftPane.add(solutionPane);
		
		gbcBuildCon(leftPaneLayoutCon, 0, 2, 1, GridBagConstraints.REMAINDER, 0, 1); // label 1
		leftPaneLayoutCon.fill = GridBagConstraints.HORIZONTAL;
		leftPaneLayoutCon.anchor = GridBagConstraints.NORTH;
		leftPaneLayout.setConstraints(commandPane, leftPaneLayoutCon);
		leftPane.add(commandPane);
		/*
		GroupLayout leftPaneLayout = new GroupLayout(leftPane);
		leftPane.setLayout(leftPaneLayout);

		leftPaneLayout.setAutoCreateGaps(true);
		leftPaneLayout.setAutoCreateContainerGaps(true);

		leftPaneLayout.setVerticalGroup(leftPaneLayout.createSequentialGroup()
				.addComponent(coordPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addComponent(solutionPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
			    .addComponent(commandPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
			    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
	                     GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			);

		leftPaneLayout.setHorizontalGroup(leftPaneLayout.createSequentialGroup()
			    .addGroup(leftPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			    		.addComponent(coordPane)
						.addComponent(solutionPane)
					    .addComponent(commandPane))
		);
		*/

		this.add(leftPane, BorderLayout.WEST);
		this.add(graphContentPane, BorderLayout.CENTER);
		this.add(progressPane, BorderLayout.SOUTH);
	}

	private void gbcBuildCon(GridBagConstraints gbc, int gx, int gy, int gw, int gh, int wx, int wy)
	{
		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.insets = new Insets(4, 4, 4, 4);
	}

	private void parseIface()
	{
		// in case of misconfiguration
		double tempfrom = ((SpinnerNumberModel) intFromVal.getModel()).getNumber().doubleValue();
		double tempto = ((SpinnerNumberModel) intToVal.getModel()).getNumber().doubleValue();
		intFrom = Math.min(tempfrom, tempto);
		intTo = Math.max(tempfrom, tempto);
		
		// dirty bugfix
		if(intTo == intFrom)
		{
			JOptionPane.showMessageDialog(this, new String("Invalid interval. Fixed."));
			intTo = intFrom + 1.0;
			intToVal.setValue(intTo);
		}

		paramA = ((SpinnerNumberModel) avarVal.getModel()).getNumber().doubleValue();
		paramB = ((SpinnerNumberModel) bvarVal.getModel()).getNumber().doubleValue();
		paramC = ((SpinnerNumberModel) cvarVal.getModel()).getNumber().doubleValue();
		desErr = ((Number)desErrVal.getValue()).doubleValue();
	}
	
	public void actionPerformed(ActionEvent arg0) {
		Object src = arg0.getSource();
		if(src == paintSomethingBtn)
		{
			parseIface();
			graphItself.flushRealPoints(); // clear the cache
			double yval;
			int iter = 1;
			for(double xval = intFrom; xval <= intTo; xval += (Math.abs(intTo - intFrom) / graphItself.getMaxPoints()))
			{
				yval = fourtyTwo((short) funcList.getSelectedIndex(), xval, paramA, paramB, paramC);
				if(yval == Double.NaN)
					continue;
				graphItself.addRealPoint(xval, yval);
				debugOut("point " + iter++ + ": " + xval + " : " + yval);
			}
			graphItself.calcExtremeRealPoints();
			graphItself.repaint();
		}
		if(src == processDataBtn)
		{
			parseIface();
			double integr = Double.NaN;

			debugOut("Calculating...");
			
			integr = integraCore((short) funcList.getSelectedIndex(),
					(short) methodList.getSelectedIndex(),
					intFrom, intTo, paramA, paramB, paramC);
			if(!Double.isNaN(integr) && !Double.isInfinite(integr))
			{
				rsltVal.setValue(integr);
			}
			else
			{
				JOptionPane.showMessageDialog(this, new String("Invalid result. Giving up."));
			}
			
			debugOut("Done");
		}
	}

	private double integraCore(short fc, short meth, double from, double to, double a, double b, double c)
	{
		// here goes the magic
		int steps = 2;
		double step;
		double x, result = 0.0, midresult = 0.0;
		double accumErr;
		short insane = 0;

		if(from == to)
		{
			debugOut("Nevozmojno poschitat integral na nulevom promejutke");
			return result;
		}

		switch(meth)
		{
		case 0:
			do
			{
				step = (to - from) / steps;
				for(x = from + step * 0.5; x < to; x += step)
				{
					midresult += Math.abs(fourtyTwo(fc, x, a, b, c));
				}
				midresult *= step;
		
				accumErr = Math.abs(midresult - result) * Math.pow(to - from, 3) / (24 * Math.pow(steps, 2));
				result = midresult;

				steps *= 2;
				debugOut("Shagov x2: " + steps + ", accumErr = " + accumErr);
				insane++;
			}
			while (accumErr > desErr && insane < Short.MAX_VALUE - 4);

			rsltErrVal.setValue(accumErr);
			break;
		case 1:
			do
			{
				step = (to - from) / steps;
				for(x = from + step; x < to; x += step)
				{
					midresult += fourtyTwo(fc, x, a, b, c);
				}
				midresult = (midresult + (fourtyTwo(fc, from, a, b, c) + fourtyTwo(fc, to, a, b, c)) / 2) * step;
		
				accumErr = Math.abs(midresult - result) * Math.pow(to - from, 3) / (1 * Math.pow(steps, 2));
				result = midresult;

				steps *= 2;
				debugOut("Shagov x2: " + steps + ", accumErr = " + accumErr);
				insane++;
			}
			while (accumErr > desErr && insane < Short.MAX_VALUE - 4);

			rsltErrVal.setValue(accumErr);
			break;
		}
	
/*
		midresult = from * fourtyTwo(fc, (from + to) / 2, a, b, c); // half
		for(iter = from; iter <= to; iter += step, step /= 2)
		{
			double curDelta =
			fourtyTwo(fc, iter + step, a, b, c) -
			 fourtyTwo(fc, iter, a, b, c);
			midresult = curDelta * fourtyTwo(fc, (iter + step) / 2, a, b, c); // half
		}
*/
		return result;
	}

	private double fourtyTwo(short funcType, double x, double a, double b, double c)
	{
		switch(funcType)
		{
		case 3:
			return Math.log(x);
		case 2:
			return (a * x) * Math.exp(-(b * x));
		case 1:
			return a * Math.pow(x, 3) + b * Math.pow(x, 2) + c *x;
		case 0:
		default:
			return a * Math.pow(x, 2) + b * x + c;
		}
	}

	public void debugOut(String s)
	{
		verboseOut.append(s + "\n");
	}
}
