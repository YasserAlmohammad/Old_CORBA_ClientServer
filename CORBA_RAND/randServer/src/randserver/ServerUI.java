package randserver;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.*;
import javax.swing.JScrollPane;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ServerUI extends JFrame {
    JPanel contentPane;
    BorderLayout borderLayout1 = new BorderLayout();

    public ServerUI() {
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Component initialization.
     *
     * @throws java.lang.Exception
     */
    RandThread randThread=new RandThread();
    static JTextArea msgs = new JTextArea();
     JScrollPane scroll = new JScrollPane(msgs);
    private void jbInit() throws Exception {
        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(borderLayout1);
        setSize(new Dimension(400, 300));
        setTitle("Server");
        randThread.start();
        msgs.setText("");
        contentPane.add(scroll, java.awt.BorderLayout.CENTER);
    }
}
