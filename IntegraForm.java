import javax.swing.*;

public class IntegraForm
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		JFrame mainframe = new JFrame("Integra");
		try  
		{  
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			JFrame.setDefaultLookAndFeelDecorated(true);
		}  
		catch(Exception e)  
		{
		}
		mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainframe.setContentPane(new IntegraPanel());
		mainframe.pack();
		mainframe.setVisible(true);
	}

}
