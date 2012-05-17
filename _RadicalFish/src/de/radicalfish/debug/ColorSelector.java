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
import de.matthiasmann.twl.Scrollbar;
import de.matthiasmann.twl.Scrollbar.Orientation;
import de.matthiasmann.twl.Widget;

public class ColorSelector extends ResizableFrame {
	
	private static final String NAME = "name", VALUE = "value", SEP = "SEP", LABEL = "label";
	
	private Scrollbar red, green, blue, chroma;
	private Columns seperatorColumns, sliderColumns;
	
	private ColorArea colorarea;
	
	public ColorSelector() {
		createPanel();
	}
	
	// INTERN
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private void createPanel() {
		setTheme("resizableframe-title");
		setTitle("Color Manipulator");
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
		
		createColorArea(layout);
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
	
	private void createColorArea(ColumnLayout layout) {
		colorarea = new ColorArea();
		
		Row row = layout.addRow(seperatorColumns);
		row.add(new Label("Color Preview: "), Alignment.FILL);
		row = layout.addRow(seperatorColumns);
		row.add(colorarea, Alignment.FILL);
		
	}
	private void createSliders(ColumnLayout layout) {
		final Label redLabel = new Label("");
		redLabel.setTheme("fixedlabel");
		red = new Scrollbar(Orientation.HORIZONTAL);
		red.addCallback(new Runnable() {
			public void run() {
				colorarea.setRed(red.getValue() / 255f);
				redLabel.setText("" + red.getValue());
			}
		});
		initScrollbar(red);
		final Label greenLabel = new Label("");
		greenLabel.setTheme("fixedlabel");
		green = new Scrollbar(Orientation.HORIZONTAL);
		green.addCallback(new Runnable() {
			public void run() {
				colorarea.setGreen(green.getValue() / 255f);
				greenLabel.setText("" + green.getValue());
			}
		});
		initScrollbar(green);
		final Label blueLabel = new Label("");
		blueLabel.setTheme("fixedlabel");
		blue = new Scrollbar(Orientation.HORIZONTAL);
		blue.addCallback(new Runnable() {
			public void run() {
				colorarea.setBlue(blue.getValue() / 255f);
				blueLabel.setText("" + blue.getValue());
			}
		});
		initScrollbar(blue);
		final Label chromaLabel = new Label("");
		chromaLabel.setTheme("fixedlabel");
		chroma = new Scrollbar(Orientation.HORIZONTAL);
		chroma.addCallback(new Runnable() {
			public void run() {
				colorarea.setChroma(chroma.getValue() / 100f);
				chromaLabel.setText("" + chroma.getValue());
			}
		});
		chroma.setTheme("hslider");
		chroma.setMinMaxValue(0, 200);
		chroma.setValue(100);
		
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
	private void initScrollbar(Scrollbar bar) {
		bar.setTheme("hslider");
		bar.setMinMaxValue(0, 255);
		bar.setValue(255);
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public ColorArea getColorArea() {
		return colorarea;
	}
}
