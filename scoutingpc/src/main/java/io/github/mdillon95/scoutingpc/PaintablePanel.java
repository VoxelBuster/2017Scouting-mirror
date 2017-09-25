package io.github.mdillon95.scoutingpc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PaintablePanel extends JPanel implements MouseMotionListener, MouseListener {
	private static final long serialVersionUID = 1L;
	
	private HashMap<ArrayList<Point>, Color> strokes = new HashMap<ArrayList<Point>, Color>();
	
	private ArrayList<Point> currentStroke = new ArrayList<Point>();

	private boolean erase = false;

	private Color color = Color.BLACK;

	private boolean bg = true;
	
	private BufferedImage bgImg;
	File savefile = null;
	private boolean save = false;

	public PaintablePanel() {
		super();
		try {
			bgImg = ImageIO.read(new File("assets/field.png"));
		} catch (IOException e) {
			Log.e(this.getClass(), e.getMessage());
		}
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
	}
	
	public void enableEraser(boolean par1) {
		erase  = par1;
	}
	
	public void enableBg(boolean par1) {
		bg  = par1;
	}
	
	public void setColor(Color c) {
		color  = c;
	}
	
	public void paint(Graphics gd) {
		super.paint(gd);

		BufferedImage bImg = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) bImg.getGraphics();
		if (bg) g.drawImage(bgImg, 0, 0, null);
		strokes.forEach((k,v) -> drawStroke(g,k,v));
		gd.drawImage(bImg, 0, 0, null);
		if (save) {
			JPanel dPanel = new JPanel();
			dPanel.setSize(1920,1080);
			Graphics2D cg = bImg.createGraphics();
			dPanel.paintAll(cg);
			dPanel.paintAll(g);
			try {
				ImageIO.write(bImg, "png", new File(savefile.getAbsolutePath() + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			save = false;
		}
		
		repaint();
	}

	private void drawStroke(Graphics2D g, ArrayList<Point> k, Color v) {
		g.setColor(v);
		int[] xPoints = new int[k.size()];
		int[] yPoints = new int[k.size()];
		
		for (int i = 0; i < k.size(); i++) {
			xPoints[i] = k.get(i).x;
			yPoints[i] = k.get(i).y;
		}
		
		g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.drawPolyline(xPoints, yPoints, k.size());
	}

	public void clear() {
		strokes.clear();
		currentStroke.clear();
	}

	@Override
	public void mouseDragged(MouseEvent m) {
	    if (erase) {
	        strokes.forEach((k,v) -> erase(strokes,k,v,m.getPoint()));
	        return;
        }
		strokes.remove(currentStroke);
		currentStroke.add(m.getPoint());
		strokes.put(currentStroke, color);
	}

    private void erase(HashMap<ArrayList<Point>,Color> map, ArrayList<Point> pts, Color c, Point mouse) {
	    for (Point p : pts) {
	        if (p.x > mouse.x - 10 && p.x < mouse.x + 10 && p.y > mouse.y - 10 && p.y < mouse.y + 10) {
	            map.remove(pts);
            }
        }
    }

    @Override
	public void mouseMoved(MouseEvent arg0) {}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent m) {}

	@Override
	public void mouseReleased(MouseEvent m) {
        if (erase) {
            strokes.forEach((k,v) -> erase(strokes,k,v,m.getPoint()));
            return;
        }
		strokes.remove(currentStroke);
		currentStroke.add(m.getPoint());
		strokes.put(currentStroke, color);
		currentStroke = new ArrayList<Point>();
	}

    public void save(File f) {
		savefile = f;
		save = true;
    }
}
