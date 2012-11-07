/*
 * Copyright (c) 2012, Stefan Lange
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Stefan Lange nor the names of its contributors may
 *       be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.radicalfish.debug;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.BorderLayout;
import de.matthiasmann.twl.BorderLayout.Location;
import de.matthiasmann.twl.Color;
import de.matthiasmann.twl.ColumnLayout;
import de.matthiasmann.twl.ColumnLayout.Columns;
import de.matthiasmann.twl.ColumnLayout.Row;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.DialogLayout.Group;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.SplitPane;
import de.matthiasmann.twl.SplitPane.Direction;
import de.matthiasmann.twl.ToggleButton;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.utils.TintAnimator;

/**
 * A graph capable of displaying the time a certain update loop took in one frame. You can add
 * {@link PerformanceListener}s which can be used to visualize the update time. Use the setter for FPS and delta to
 * display them on the graph.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 24.05.2012
 */
public class PerformanceGraph extends ResizableFrame {
	
	private CheckBoxList list;
	private PGraphArea area;
	private Label fpsLabel, deltaLabel;
	
	private ArrayList<PGraphModel> listeners;
	
	/**
	 * Creates a new PeformanceGraph.
	 */
	public PerformanceGraph() {
		listeners = new ArrayList<PGraphModel>();
		createPanel();
	}
	
	// METHOD
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void addPerformanceListener(PerformanceListener listener, String name, Color color) {
		if (listener == null) {
			throw new NullPointerException("listener is null!");
		}
		if (name == null) {
			throw new NullPointerException("name is null!");
		}
		if (color == null) {
			throw new NullPointerException("color is null!");
		}
		
		PGraphModel model = new PGraphModel(color, listener);
		
		listeners.add(model);
		list.addCheckBox(name, color, model.checkbox, model.lastTime);
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void createPanel() {
		setTheme("resizableframe-title");
		setResizableAxis(ResizableAxis.BOTH);
		addCloseCallback(new Runnable() {
			public void run() {
				setVisible(false);
			}
		});
		setTitle("Performance Monitor");
		setSize(500, 140);
		setPosition(5, 100);
		
		// HAHA!!! My own layout (well sort of) in TWL me = happy :)
		BorderLayout l = new BorderLayout();
		
		area = new PGraphArea();
		list = new CheckBoxList();
		
		SplitPane pane = new SplitPane();
		pane.setDirection(Direction.HORIZONTAL);
		pane.setRespectMinSizes(true);
		pane.setSplitPosition(40);
		
		pane.add(list);
		pane.add(area);
		
		l.add(createTopBar(), Location.NORTH);
		l.add(pane, Location.CENTER);
		add(l);
		
	}
	private Widget createTopBar() {
		DialogLayout l = new DialogLayout();
		
		l.setTheme("");
		
		Label fps = new Label("FPS: ");
		
		fpsLabel = new Label("");
		fpsLabel.setTheme("fixedlabel");
		
		Label delta = new Label("Delta: ");
		
		deltaLabel = new Label("");
		deltaLabel.setTheme("fixedlabel");
		
		Group g = l.createSequentialGroup(fps, fpsLabel).addGap(5).addWidgets(delta, deltaLabel);
		
		l.setHorizontalGroup(g);
		l.setVerticalGroup(l.createParallelGroup(fps, fpsLabel, delta, deltaLabel));
		
		return l;
	}
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	/**
	 * @param fps
	 *            the current FPS to display
	 */
	public void setFPS(int fps) {
		fpsLabel.setText("" + fps);
	}
	/**
	 * @param delta
	 *            the current delta to display.
	 */
	public void setDelta(int delta) {
		deltaLabel.setText("" + delta);
	}
	
	// INTERN CLASSES
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private class PGraphArea extends Widget {
		private LWJGLRenderer renderer;
		
		@Override
		protected void paintWidget(GUI gui) {
			super.paintWidget(gui);
			
			if (renderer == null) {
				renderer = (LWJGLRenderer) gui.getRenderer();
				renderer.setUseQuadsForLines(true);
			}
			
			float right = getInnerRight();
			float bottom = getInnerBottom();
			float top = getInnerY();
			
			for (PGraphModel model : listeners) {
				model.checkArrayWidthAndUpdate(getInnerWidth());
				
				// set all points until the current step
				for (int i = 0; i < model.step; i++) {
					model.points[2 * i] = right - i;
					model.points[2 * i + 1] = Math.min(bottom, Math.max(top, bottom - model.samples[i] * 2 + 1));
				}
				
				// only draw if active
				// draw only the points until step
				if (model.checkbox.isActive()) {
					renderer.drawLine(model.points, model.step, 1.0f, model.color, false);
				}
				
				// update current sample and increment step
				model.samples[model.step] = (int) (model.listener.getMessuredTime() / 1000);
				model.setTimeLabelText(model.listener.getMessuredTime());
				model.step++;
				if (model.step >= model.samples.length) {
					model.step = 0;
				}
				
			}
			
		}
	}
	private static class PGraphModel {
		
		private Color color;
		private ToggleButton checkbox;
		private Label lastTime;
		
		private PerformanceListener listener;
		
		private int[] samples;
		private float[] points;
		private int step;
		
		public PGraphModel(Color color, PerformanceListener listener) {
			this.color = color;
			this.listener = listener;
			
			checkbox = new ToggleButton();
			checkbox.setTheme("checkbox");
			checkbox.setActive(true);
			
			lastTime = new Label();
			lastTime.setTheme("fixedlabel2");
			
		}
		
		public void setTimeLabelText(long microseconds) {
			lastTime.setText(String.format(Locale.ENGLISH, "%-3.2f ms", microseconds / 1000f));
		}
		
		public void checkArrayWidthAndUpdate(int width) {
			if (samples == null) {
				samples = new int[width];
				points = new float[samples.length * 2];
				step = 0;
			} else {
				if (width != samples.length) {
					samples = Arrays.copyOf(samples, width);
					points = new float[samples.length * 2];
					if (step >= samples.length) {
						step = Math.max(samples.length - 1, 0);
					} else {
						step = Math.min(step, samples.length);
					}
				}
			}
		}
	}
	private static class CheckBoxList extends ScrollPane {
		
		private ColumnLayout rows;
		private Columns col;
		
		public CheckBoxList() {
			createPanel();
		}
		
		public void addCheckBox(String name, Color color, ToggleButton checkbox, Label time) {
			Row row = rows.addRow(col);
			
			Label label = new Label(name);
			label.setTintAnimator(new TintAnimator(label, color));
			
			row.add(label, Alignment.TOPLEFT);
			row.add(checkbox, Alignment.TOP);
			row.add(time, Alignment.TOPRIGHT);
			
			invalidateLayout();
			
		}
		
		private void createPanel() {
			setTheme("scrollpane");
			setExpandContentSize(true);
			setFixed(Fixed.HORIZONTAL);
			
			rows = new ColumnLayout();
			rows.addDefaultGaps();
			setContent(rows);
			
			col = rows.getColumns("LABEL", "CHECKBOX", "TIME");
		}
	}
	
}
