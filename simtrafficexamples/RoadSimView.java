package pcd.ass01.simtrafficexamples;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

import pcd.ass01.simengineseq.AbstractAgent;
import pcd.ass01.simengineseq.AbstractEnvironment;
import pcd.ass01.simengineseq.AbstractSimulation;
import pcd.ass01.simengineseq.SimulationListener;
import pcd.ass01.simtrafficbase.*;

import java.awt.*;
import javax.swing.*;

public class RoadSimView extends JFrame implements SimulationListener {

	final private RoadSimViewPanel panel;
	private static final int CAR_DRAW_SIZE = 10;
	boolean newRun = true;

	public RoadSimView(AbstractSimulation sim) {
		super("RoadSim View");
		setSize(1500,600);

		panel = new RoadSimViewPanel(1500,600);
		panel.setSize(1500, 600);

		final JPanel cp = new JPanel();
		final JPanel menu = new JPanel();
		LayoutManager layout = new BorderLayout();
		cp.setLayout(layout);
		cp.add(BorderLayout.CENTER, panel);
		final JButton start = new JButton("Start");
		final JButton stop = new JButton("Stop");
		final JLabel label = new JLabel("Steps: ");
		final JTextField nStepsField = new JFormattedTextField("100");
		nStepsField.setColumns(4);
		stop.setEnabled(false);

		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int nSteps = Integer.parseInt(nStepsField.getText());
					sim.start();


					if(newRun) {
						sim.run(nSteps);
						newRun = false;
					}

					start.setEnabled(false);
					stop.setEnabled(true);
				} catch (Exception ex) {
					System.out.println(ex);
				}
			}
		});
		stop.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					sim.stop();

					start.setEnabled(true);
					stop.setEnabled(false);
				}
		});
		menu.setLayout(new FlowLayout());
		menu.add(start);
		menu.add(stop);
		menu.add(label);
		menu.add(nStepsField);
		cp.add(menu, BorderLayout.SOUTH);
		setContentPane(cp);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
			
	}
	
	public void display() {
		SwingUtilities.invokeLater(() -> {
			this.setVisible(true);
		});
	}

	@Override
	public void notifyInit(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyStepDone(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
		var e = ((RoadsEnv) env);
		panel.update(e.getRoads(), e.getAgentInfo(), e.getTrafficLights());
	}
	
	
	class RoadSimViewPanel extends JPanel {
		
		List<CarAgentInfo> cars;
		List<Road> roads;
		List<TrafficLight> sems;
		
		public RoadSimViewPanel(int w, int h){
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);   
	        Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.clearRect(0,0,this.getWidth(),this.getHeight());
			
			if (roads != null) {
				for (var r: roads) {
					g2.drawLine((int)r.getFrom().x(), (int)r.getFrom().y(), (int)r.getTo().x(), (int)r.getTo().y());
				}
			}
			
			if (sems != null) {
				for (var s: sems) {
					if (s.isGreen()) {
						g.setColor(new Color(0, 255, 0, 255));
					} else if (s.isRed()) {
						g.setColor(new Color(255, 0, 0, 255));
					} else {
						g.setColor(new Color(255, 255, 0, 255));
					}
					g2.fillRect((int)(s.getPos().x()-5), (int)(s.getPos().y()-5), 10, 10);
				}
			}
			
			g.setColor(new Color(0, 0, 0, 255));

			if (cars != null) {
				for (var c: cars) {
					double pos = c.getPos();
					Road r = c.getRoad();
					V2d dir = V2d.makeV2d(r.getFrom(), r.getTo()).getNormalized().mul(pos);
					g2.drawOval((int)(r.getFrom().x() + dir.x() - CAR_DRAW_SIZE/2), (int)(r.getFrom().y() + dir.y() - CAR_DRAW_SIZE/2), CAR_DRAW_SIZE , CAR_DRAW_SIZE);
				}
			}
  	   }
	
	   public void update(List<Road> roads, 
			   			  List<CarAgentInfo> cars,
			   			List<TrafficLight> sems) {
		   this.roads = roads;
		   this.cars = cars;
		   this.sems = sems;
		   repaint();
	   }
	}
}
