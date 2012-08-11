/*
 * Copyright (c) 2011, Stefan Lange
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
import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.ColumnLayout;
import de.matthiasmann.twl.ColumnLayout.Columns;
import de.matthiasmann.twl.ColumnLayout.Row;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Scrollbar.Orientation;
import de.matthiasmann.twl.Widget;
import de.radicalfish.effects.ToneModel;

/**
 * A simple color edtior which uses a {@link RPGMakerToneArea} as color area and 4 sliders for red, green, blue and
 * chroma.
 * 
 * @author Stefan Lange
 * @version 1.0.0
 * @since 19.05.2012
 */
public class ToneEditor extends ResizableFrame {
	
	private static final String NAME = "name", VALUE = "value", SEP = "SEP", LABEL = "label";
	
	private Slider red, green, blue, chroma;
	private Columns seperatorColumns, sliderColumns;
	
	private RPGMakerToneArea colorarea;
	
	public ToneEditor() {
		createPanel(new ToneModel());
	}
	public ToneEditor(ToneModel toneModel) {
		createPanel(toneModel);
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void createPanel(ToneModel model) {
		setTheme("resizableframe-title");
		setTitle("Tone Editor");
		setPosition(800, 260);
		setResizableAxis(ResizableAxis.HORIZONTAL);
		addCloseCallback(new Runnable() {
			public void run() {
				setVisible(false);
			}
		});
		
		ColumnLayout layout = new ColumnLayout();
		layout.addDefaultGaps();
		
		sliderColumns = layout.getColumns(NAME, VALUE, LABEL);
		seperatorColumns = layout.getColumns(SEP);
		
		createColorArea(layout, model);
		Row row = layout.addRow(seperatorColumns);
		row.add(new Label("Color: "), Alignment.FILL);
		addSeparator(layout);
		createSliders(layout);
		
		add(layout);
	}
	private void addSeparator(ColumnLayout layout) {
		Row row = layout.addRow(seperatorColumns);
		Widget w = new Widget();
		w.setTheme("hseparator");
		row.add(w);
	}
	
	private void createColorArea(ColumnLayout layout, ToneModel model) {
		colorarea = new RPGMakerToneArea(model);
		
		Row row = layout.addRow(seperatorColumns);
		row.add(new Label("Tone Preview: "), Alignment.FILL);
		row = layout.addRow(seperatorColumns);
		row.add(colorarea, Alignment.FILL);
		
	}
	private void createSliders(ColumnLayout layout) {
		final Label redLabel = new Label("");
		redLabel.setTheme("fixedlabel");
		red = new Slider(Orientation.HORIZONTAL);
		red.addCallback(new Runnable() {
			public void run() {
				float val = red.getValue() / 100f;
				float low = Math.min(1f, val);
				float high = Math.max(val - 1f, 0f);
				
				colorarea.setRed(low);
				colorarea.setRedOvershoot(high);
				redLabel.setText("" + red.getValue());
			}
		});
		initSlider(red);
		final Label greenLabel = new Label("");
		greenLabel.setTheme("fixedlabel");
		green = new Slider(Orientation.HORIZONTAL);
		green.addCallback(new Runnable() {
			public void run() {
				float val = green.getValue() / 100f;
				float low = Math.min(1f, val);
				float high = Math.max(val - 1f, 0f);
				
				colorarea.setGreen(low);
				colorarea.setGreenOvershoot(high);
				greenLabel.setText("" + green.getValue());
			}
		});
		initSlider(green);
		final Label blueLabel = new Label("");
		blueLabel.setTheme("fixedlabel");
		blue = new Slider(Orientation.HORIZONTAL);
		blue.addCallback(new Runnable() {
			public void run() {
				float val = blue.getValue() / 100f;
				float low = Math.min(1f, val);
				float high = Math.max(val - 1f, 0f);
				
				colorarea.setBlue(low);
				colorarea.setBlueOvershoot(high);
				blueLabel.setText("" + blue.getValue());
			}
		});
		initSlider(blue);
		final Label chromaLabel = new Label("");
		chromaLabel.setTheme("fixedlabel");
		chroma = new Slider(Orientation.HORIZONTAL);
		chroma.addCallback(new Runnable() {
			public void run() {
				float val = chroma.getValue() / 100f;
				float low = Math.min(1f, val);
				float high = Math.max(val - 1f, 0f);
				
				colorarea.setChroma(low);
				colorarea.setChromaOvershoot(high);
				chromaLabel.setText("" + chroma.getValue());
			}
		});
		initSlider(chroma);
		
		Row row = layout.addRow(sliderColumns);
		row.addLabel("Red: ").add(red, Alignment.FILL).add(redLabel);
		row = layout.addRow(sliderColumns);
		row.addLabel("Green: ").add(green, Alignment.FILL).add(greenLabel);
		row = layout.addRow(sliderColumns);
		row.addLabel("Blue: ").add(blue, Alignment.FILL).add(blueLabel);
		addSeparator(layout);
		row = layout.addRow(sliderColumns);
		row.add(new Label("Chroma: "), Alignment.FILL).add(new Label("")).add(chromaLabel, Alignment.RIGHT);
		row = layout.addRow(seperatorColumns);
		row.add(chroma, Alignment.FILL);
		
	}
	private void initSlider(Slider bar) {
		bar.setMinMaxValue(0, 200);
		bar.setValue(100);
		bar.setCanAcceptKeyboardFocus(true);
		bar.setStepSize(10);
		bar.setPageSize(10);
		bar.setValue(100);
		for (int i = 0; i < bar.getNumChildren(); i++) {
			if (bar.getChild(i).getTheme().equals("thumb")) {
				bar.getChild(i).setCanAcceptKeyboardFocus(true);
			}
		}
		
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public RPGMakerToneArea getColorArea() {
		return colorarea;
	}
}
