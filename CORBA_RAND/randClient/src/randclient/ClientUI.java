package randclient;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.*;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import client_side.*;
import server_side.*;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CORBA.ORB;
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
public class ClientUI extends JFrame {
    JPanel contentPane;
    BorderLayout borderLayout1 = new BorderLayout();
    static JTextArea msgs = new JTextArea();
    JPanel jPanel1 = new JPanel();
    JTextField val = new JTextField();
    JButton jButton1 = new JButton();
    JScrollPane scroll = new JScrollPane(msgs);
    public ClientUI() {
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
    RandThread clientThread=new RandThread();
    private void jbInit() throws Exception {
        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(borderLayout1);
        setSize(new Dimension(400, 300));
        setTitle("Client");
        msgs.setText("");
        val.setPreferredSize(new Dimension(100, 21));
        val.setToolTipText("");
        val.setText("");
        jButton1.setText("generate rand");
        jButton1.addActionListener(new ClientUI_jButton1_actionAdapter(this));
        contentPane.add(scroll, java.awt.BorderLayout.CENTER);
        contentPane.add(jPanel1, java.awt.BorderLayout.NORTH);
        jPanel1.add(val);
        jPanel1.add(jButton1);
        clientThread.start();
    }

    public void jButton1_actionPerformed(ActionEvent e) {
        try{
        ORB orb = ORB.init(RandClient.args, null);

        // get the root naming context
        org.omg.CORBA.Object objRef =
                orb.resolve_initial_references("NameService");
        // Use NamingContextExt instead of NamingContext. This is
        // part of the Interoperable naming Service.
        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

        // resolve the Object Reference in Naming
        String name = "RandServer";
        ServerSide server = ServerSideHelper.narrow(ncRef.resolve_str(name));
        double rand=Math.random()*10-5;
        server.getRand((int)rand);
        val.setText((int)rand+"");
        }
        catch(Exception ex){
            System.out.println("exception");
        }
    //    msgs.append("rand="+param);
    }
}


class ClientUI_jButton1_actionAdapter implements ActionListener {
    private ClientUI adaptee;
    ClientUI_jButton1_actionAdapter(ClientUI adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jButton1_actionPerformed(e);
    }
}
