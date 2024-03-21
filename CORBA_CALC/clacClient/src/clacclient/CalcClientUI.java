package clacclient;

import java.awt.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import calc.*;
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
public class CalcClientUI extends JFrame {
    JPanel contentPane;
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jPanel1 = new JPanel();
    JTextField param1 = new JTextField();
    JTextField param2 = new JTextField();
    JComboBox jComboBox1 = new JComboBox();
    JButton calcBtn = new JButton();
    public CalcClientUI() {
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            jbInit();
            jComboBox1.addItem(new String("+"));
            jComboBox1.addItem(new String("-"));
            jComboBox1.addItem(new String("*"));
            jComboBox1.addItem(new String("/"));
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
        setSize(new Dimension(400, 300));
        setTitle("Calculator Client UI");
        param1.setPreferredSize(new Dimension(100, 20));
        param1.setText("");
        param2.setPreferredSize(new Dimension(100, 20));
        param2.setText("");
        calcBtn.setText("calc");
        calcBtn.addActionListener(new CalcClientUI_calcBtn_actionAdapter(this));
        msgs.setEditable(false);
        param2.addActionListener(new CalcClientUI_param2_actionAdapter(this));
        contentPane.add(jPanel1, java.awt.BorderLayout.NORTH);
        jPanel1.add(param1);
        jPanel1.add(param2);
        jPanel1.add(jComboBox1);
        jPanel1.add(calcBtn);
        contentPane.add(msgs, java.awt.BorderLayout.CENTER);
    }
    static String args[]=null;
    static Calc calcImpl=null;
    JTextArea msgs = new JTextArea();
    public void calcBtn_actionPerformed(ActionEvent e) {
        try{
          // create and initialize the ORB
          ORB orb = ORB.init(args, null);

          // get the root naming context
          org.omg.CORBA.Object objRef =
          orb.resolve_initial_references("NameService");
          // Use NamingContextExt instead of NamingContext. This is
          // part of the Interoperable naming Service.
          NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

          // resolve the Object Reference in Naming
          String name = "CalcServer";
          calcImpl = CalcHelper.narrow(ncRef.resolve_str(name));

          int op=this.jComboBox1.getSelectedIndex();
          float p1=Float.parseFloat(param1.getText());
          float p2=Float.parseFloat(param2.getText());
        //  System.out.println(calcImpl.calc(op,p1,p1));
        msgs.append(param1.getText()+jComboBox1.getSelectedItem()+param2.getText()+"="+calcImpl.calc(op,p1,p2)+"\n");


      //    calcImpl.shutdown();

  } catch (Exception ex) {
            System.out.println("ERROR : " + ex) ;
    ex.printStackTrace(System.out);
  }
    }

    public void param2_actionPerformed(ActionEvent e) {

    }
}


class CalcClientUI_param2_actionAdapter implements ActionListener {
    private CalcClientUI adaptee;
    CalcClientUI_param2_actionAdapter(CalcClientUI adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.param2_actionPerformed(e);
    }
}


class CalcClientUI_calcBtn_actionAdapter implements ActionListener {
    private CalcClientUI adaptee;
    CalcClientUI_calcBtn_actionAdapter(CalcClientUI adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.calcBtn_actionPerformed(e);
    }
}
