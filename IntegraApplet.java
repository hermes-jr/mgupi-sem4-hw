import javax.swing.*;

public class IntegraApplet extends JApplet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5469313149840592876L;

	public void init()
	{
		try
		{
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					createGUI();
				}
			});
		}
		catch (Exception e)
		{
			System.err.println("Couldn't create GUI");
		}
	}

	private void createGUI()
	{
		try  
		{  
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  
		}  
		catch(Exception e)  
		{  
		}
		IntegraPanel ipan = new IntegraPanel();
		setContentPane(ipan);
	}
}
