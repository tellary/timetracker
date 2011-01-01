package ru.silvestrov.timetracker.view;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;

/**
 * Created by Silvestrov Ilya.
 * Date: Feb 1, 2009
 * Time: 9:50:19 PM
 */
public class WindowSettingsSaver extends WindowAdapter {
    private static final String FILE = "window.properties"; 

    private int defaultX = 100;
    private int defaultY = 100;
    private int defaultWidth = 100;
    private int defaultHeight = 100;

    private Properties props = new Properties();

    private HashSet<Window> windows = new HashSet<Window>();

    {
        try {
            File f = new File(FILE);
            if (!f.exists())
                f.createNewFile();
            props.load(new FileInputStream(f));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private class Loader {
        private String name;
        private Window window;

        private Loader(WindowEvent e) {
            name = (window = e.getWindow()).getName();
        }

        private int intValue(String property, int defaultValue) {
            String val = props.getProperty(name + "." + property, String.valueOf(defaultValue));
            return Integer.parseInt(val);
        }

        public void load() {
            window.setSize(
                    intValue("width", defaultWidth),
                    intValue("height", defaultHeight));
            window.setLocation(
                    intValue("x", defaultX),
                    intValue("y", defaultY)
            );

            windows.add(window);
        }
    }

    public void windowOpened(WindowEvent e) {
        new Loader(e).load();
    }

    private class Saver {
        private String name;
        private Window window;
        private Properties props;

        private Saver(Window window, Properties props) {
            this.props = props;
            name = window.getName();
            this.window = window;
        }

        private void intValue(String property, int val) {
            props.setProperty(name + "." + property, String.valueOf(val));
        }

        public void save() {
            intValue("width", (int) window.getSize().getWidth());
            intValue("height", (int) window.getSize().getHeight());

            intValue("x", (int) window.getLocation().getX());
            intValue("y", (int) window.getLocation().getY());
        }
    }

    public void windowClosing(WindowEvent e) {
        try {
            Properties props = new Properties();
            for (Window window : windows) {
                new Saver(window, props).save();
            }
            props.store(new FileOutputStream(FILE), "");
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
