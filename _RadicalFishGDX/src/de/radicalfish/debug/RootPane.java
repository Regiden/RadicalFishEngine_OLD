package de.radicalfish.debug;

import org.lwjgl.opengl.Display;

import de.matthiasmann.twl.BoxLayout;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DesktopArea;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ToggleButton;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.SimpleBooleanModel;

public class RootPane extends Widget {
    final DesktopArea desk;

    final BoxLayout btnBox;

    final BoxLayout vsyncBox;

    boolean reduceLag = true;

    public RootPane() {
        setTheme("");

        desk = new DesktopArea();
        desk.setTheme("");

        btnBox = new BoxLayout(BoxLayout.Direction.HORIZONTAL);
        btnBox.setTheme("buttonBox");

        vsyncBox = new BoxLayout(BoxLayout.Direction.HORIZONTAL);
        vsyncBox.setTheme("buttonBox");

        final SimpleBooleanModel vsyncModel = new SimpleBooleanModel(true);
        vsyncModel.addCallback(new Runnable() {
            public void run() {
                Display.setVSyncEnabled(vsyncModel.getValue());
            }
        });

        ToggleButton vsyncBtn = new ToggleButton(vsyncModel);
        vsyncBtn.setTheme("checkbox");
        Label l = new Label("VSync");
        l.setLabelFor(vsyncBtn);

        vsyncBox.add(l);
        vsyncBox.add(vsyncBtn);

        add(desk);
        add(btnBox);
        add(vsyncBox);

        Runnable r = new Runnable() {
            public void run() {
            }
        };

        addButton("Test", r);
        addButton("Test 2", r);
        addButton("Test 3", r);
    }

    public Button addButton(String text, Runnable cb) {
        Button btn = new Button(text);
        btn.addCallback(cb);
        btnBox.add(btn);
        invalidateLayout();
        return btn;
    }

    public Button addButton(String text, String ttolTip, Runnable cb) {
        Button btn = addButton(text, cb);
        btn.setTooltipContent(ttolTip);
        return btn;
    }

    @Override
    protected void layout() {
        btnBox.adjustSize();
        btnBox.setPosition(0, getParent().getHeight() - btnBox.getHeight());
        desk.setSize(getParent().getWidth(), getParent().getHeight());
        vsyncBox.adjustSize();
        vsyncBox.setPosition(getParent().getWidth() - vsyncBox.getWidth(), getParent().getHeight() - vsyncBox.getHeight());
    }

    @Override
    protected void afterAddToGUI(GUI gui) {
        super.afterAddToGUI(gui);
        validateLayout();
    }

    @Override
    protected boolean handleEvent(Event evt) {
        if(evt.getType() == Event.Type.KEY_PRESSED && evt.getKeyCode() == Event.KEY_L && (evt.getModifiers() & Event.MODIFIER_CTRL) != 0 && (evt.getModifiers() & Event.MODIFIER_SHIFT) != 0) {
            reduceLag ^= true;
            System.out.println("reduceLag = " + reduceLag);
        }

        return super.handleEvent(evt);
    }

}