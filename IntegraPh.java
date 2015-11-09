import java.awt.*;
import java.awt.geom.Point2D;
import javax.swing.*;
import java.util.*;

public class IntegraPh extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6956437926061150419L;
	private double minx = -1, maxx = 1, miny = -1, maxy = 1;
	private IntegraPanel whosDaddy;
	private Vector<Point2D.Double> dataset;

	public IntegraPh() {
		super();
		whosDaddy = null;
		dataset = new Vector<Point2D.Double>();
		setBackground(new Color(255,255,255));
	}

	public void setDaddy(Object arg0)
	{
		try
		{
			this.whosDaddy = (IntegraPanel) arg0;
		}
		catch (Exception e) {}
	}

	private void echoToDaddy(String s)
	{
		if(whosDaddy == null)
			return;
		whosDaddy.debugOut(s);
	}

	public void flushRealPoints()
	{
		minx = -1; miny = -1; maxx = 1; maxy = 1;
		dataset.clear();
	}

	public void addRealPoint(double x, double y)
	{
		if(!(Double.isInfinite(x) || Double.isNaN(x)))
		{
			if(x > maxx)
				maxx = x;
			if(x < minx)
				minx = x;
		}
		if(!(Double.isInfinite(y) || Double.isNaN(y)))
		{
			if(y > maxy)
				maxy = y;
			if(y < miny)
				miny = y;
		}
		dataset.add(new Point2D.Double(x, y));
	}

	public int getMaxPoints()
	{
		return Math.max(this.getWidth(), this.getHeight());
	}

	public void calcExtremeRealPoints()
	{
		if(dataset.size() < 1)
		{
			return;
		}
		Point2D.Double temp;
		minx = maxx = dataset.get(0).x;
		miny = maxy = dataset.get(0).y;
		for(int iter = 0; iter < dataset.size(); iter++)
		{
			temp = dataset.get(iter);
			if(!(Double.isInfinite(temp.x) || Double.isNaN(temp.x)))
			{
				if(temp.x > maxx)
					maxx = temp.x;
				if(temp.x < minx)
					minx = temp.x;
			}
			if(!(Double.isInfinite(temp.y) || Double.isNaN(temp.y)))
			{
				if(temp.y > maxy)
					maxy = temp.y;
				if(temp.y < miny)
					miny = temp.y;
			}
		}
	}

	public void paint(Graphics g1d) {
		super.paint(g1d);

		int width = getWidth() - 1;
		int height = getHeight() - 1;
		int rectSize = Math.min(width, height);
		// that much pixels in value
		double zoomfactor = rectSize / Math.max(Math.abs(maxx - minx), Math.abs(maxy - miny));

		Graphics2D g = (Graphics2D) g1d;
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		g.setColor(Color.gray);
		g.drawRect((width - rectSize)/2, (height - rectSize)/2, rectSize, rectSize);

		//g.translate(0.0, height);  // Move the origin to the lower left
		g.translate((width - rectSize)/2, (height - rectSize)/2 + rectSize);  // Move the origin to the lower left of the visible block
		g.scale(1.0, -1.0);

		// coordinates pseudonull crosshair
		if(this.dataset.size() > 0)
		{
			int xpos = (int)((rectSize - Math.abs(maxx - minx) * zoomfactor) / 2);
			int ypos = (int)((rectSize - Math.abs(maxy - miny) * zoomfactor) / 2);
			int pseudonulx = (int)(-minx * zoomfactor) + xpos;
			int pseudonuly = (int)(-miny * zoomfactor) + ypos;
			//g.fillOval((int)(-minx * zoomfactor), (int)(-miny * zoomfactor), 5, 5);
			g.drawLine(-20 + pseudonulx, pseudonuly, 20 + pseudonulx, pseudonuly);
			g.drawLine(pseudonulx, -20 + pseudonuly, pseudonulx, 20 + pseudonuly);
		}
		/*
		g.drawLine(0, 0, 0, rectSize);
		g.drawLine(0, 0, rectSize, 0);

		for (int iter = 0; iter < rectSize; iter += 10) {
			g.drawLine(iter, 0, iter, 2);
		}
		for (int iter = 0; iter < rectSize; iter += 10) {
			g.drawLine(0, iter, 2, iter);
		}
		*/

		g.setColor(Color.blue); // graph
		if(this.dataset.size() > 0)
		{
			for(int iter = 0; iter < this.dataset.size(); iter++)
			{
				int xpos = (int)((rectSize - Math.abs(maxx - minx) * zoomfactor) / 2);
				int ypos = (int)((rectSize - Math.abs(maxy - miny) * zoomfactor) / 2);
				int drpx = (int) ((this.dataset.get(iter).x - minx) * zoomfactor);
				int drpy = (int)((this.dataset.get(iter).y - miny) * zoomfactor);
				g.fillOval(drpx + xpos, drpy + ypos, 2, 2);
			}
		}

		// g.setColor(Color.red); // nullpoint
		// g.fillOval((int)(Math.random()*width), (int)(Math.random()*height), 15, 15);

	}

}
