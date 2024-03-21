package chatclient;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

import javax.swing.JLabel;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import javax.swing.JScrollPane;


import server_board.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;

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
public class ChatUI extends JFrame {
    JPanel contentPane;
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel top = new JPanel();
    JButton connectBtn = new JButton();
    JTextArea msgs = new JTextArea();
    JPanel bottom = new JPanel();
    JButton sendBtn = new JButton();
    JTextArea msg = new JTextArea();
    ServerBoard server = null;
    JTextField nickNameField = new JTextField();
    JButton disconnect = new JButton();
    String nickName = "nickName";
    boolean connected = false;
    JTextField hostname = new JTextField();
    JLabel jLabel1 = new JLabel();
    ClientThread clientThread = null;
    String serverName = null;
    JScrollPane scroll = new JScrollPane(msgs);
    JScrollPane scroll2 = new JScrollPane(msg);
    public ChatUI() {
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            jbInit();
            serverName = java.net.InetAddress.getLocalHost().getHostName();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Component initialization.
     *
     * @throws java.lang.Exception
     */
    private void jbInit() throws Exception {
        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(borderLayout1);
        setSize(new Dimension(500, 300));
        setTitle("client window");
        this.addWindowListener(new ChatUI_this_windowAdapter(this));
        connectBtn.setText("connect");
        connectBtn.addActionListener(new ChatUI_connectBtn_actionAdapter(this));
        msgs.setText("");
        sendBtn.setText("send");
        sendBtn.addActionListener(new ChatUI_sendBtn_actionAdapter(this));
        msg.setPreferredSize(new Dimension(300, 50));
        msg.setText("");
        nickNameField.setPreferredSize(new Dimension(100, 20));
        nickNameField.setText("nickName");
        disconnect.setToolTipText("");
        disconnect.setText("disconnect");
        disconnect.addActionListener(new ChatUI_disconnect_actionAdapter(this));
        hostname.setPreferredSize(new Dimension(70, 20));
        hostname.setText("mery");
        contentPane.setMinimumSize(new Dimension(410, 88));
        contentPane.setPreferredSize(new Dimension(500, 112));
        jLabel1.setText("host name");
        nickNameField.addActionListener(new
                                        ChatUI_nickNameField_actionAdapter(this));
        contentPane.add(top, java.awt.BorderLayout.NORTH);
        top.add(jLabel1);
        top.add(hostname);
        top.add(nickNameField);
        top.add(connectBtn);
        top.add(disconnect);
        contentPane.add(scroll, java.awt.BorderLayout.CENTER);
        contentPane.add(bottom, java.awt.BorderLayout.SOUTH);
        bottom.add(scroll2);
        bottom.add(sendBtn);

        Client.ui = this;
    }

    /**
     * connect when disconnected or coonection lost by it self
     * @param e ActionEvent
     */
    public void connectBtn_actionPerformed(ActionEvent e) {
        if ((!connected)) {
            try {
                String name="ChatServer";
                // create and initialize the ORB
                ORB orb = ORB.init(ChatClient.args, null);

                // get the root naming context
                org.omg.CORBA.Object objRef =
                        orb.resolve_initial_references("NameService");
                NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
                server = ServerBoardHelper.narrow(ncRef.resolve_str(name));

                nickName = nickNameField.getText();
                connected = true;

                //now spawn the client in the naming service
                clientThread = new ClientThread();
                clientThread.name = nickName;
                clientThread.start();
                if (server != null) {
                    //    try {
                    String answer = (String) server.connect(nickName);
                    if (!answer.equals("ok")) {
                        JOptionPane.showMessageDialog(this,
                                nickName+" already exists try another one");
                        connected = false;
                        //      client.unbind();
                        return;
                    }

                    //  } catch (RemoteException ex) {
                    // }
                } else {
                    connected = false;
                    JOptionPane.showMessageDialog(this,
                                                  "no connection to the server, try to connect again");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "can't connect to the server, try to connect again");
                connected=false;
            }
        } else {
            JOptionPane.showMessageDialog(this,"already connected, disconnect first and try to connect back");
        }
    }

    public void sendBtn_actionPerformed(ActionEvent e) {
        if (connected) {
            try {
                server.recieveMsg(nickName, msg.getText());
                msg.setText("");
            } catch (Exception ex) {
            }
        }
        else
            JOptionPane.showMessageDialog(this,"connect first");
    }

    public void nickNameField_actionPerformed(ActionEvent e) {

    }

    public void disconnect_actionPerformed(ActionEvent e) {

        if (connected) {
                server.disconnect(nickName);
                msgs.append("you have been disconnected\n");
                 connected = false;
                clientThread.orb.shutdown(false);
        }
        else
            JOptionPane.showMessageDialog(this,"connect first");


    }

    public void this_windowClosing(WindowEvent e) {
        if (connected) {
            connected = false;
            if (server != null)

                    server.disconnect(nickName);

               //     client.shutDown();

        }
    }
}


class ChatUI_this_windowAdapter extends WindowAdapter {
    private ChatUI adaptee;
    ChatUI_this_windowAdapter(ChatUI adaptee) {
        this.adaptee = adaptee;
    }

    public void windowClosing(WindowEvent e) {

        adaptee.this_windowClosing(e);
    }
}


class ChatUI_disconnect_actionAdapter implements ActionListener {
    private ChatUI adaptee;
    ChatUI_disconnect_actionAdapter(ChatUI adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.disconnect_actionPerformed(e);
    }
}


class ChatUI_nickNameField_actionAdapter implements ActionListener {
    private ChatUI adaptee;
    ChatUI_nickNameField_actionAdapter(ChatUI adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.nickNameField_actionPerformed(e);
    }
}


class ChatUI_sendBtn_actionAdapter implements ActionListener {
    private ChatUI adaptee;
    ChatUI_sendBtn_actionAdapter(ChatUI adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {

        adaptee.sendBtn_actionPerformed(e);
    }
}


class ChatUI_connectBtn_actionAdapter implements ActionListener {
    private ChatUI adaptee;
    ChatUI_connectBtn_actionAdapter(ChatUI adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.connectBtn_actionPerformed(e);
    }
}
