package randclient;

import server_side.*;
import client_side.*;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POA;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import java.io.*;
import javax.swing.JOptionPane;

class ClientImpl extends ClientSidePOA {
    File file=new File("test.txt");
    PrintWriter writer=null;
    ClientImpl(){
        super();
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                            "error file test.txt couldn't be opened");
        }
    }
    public boolean recieveNum (int num){
        ClientUI.msgs.append("number recieved:"+num+"\n");
        return true;
    }
    public boolean recieveMsg (String msg){
        ClientUI.msgs.append("message recieved:"+msg+"\n");

        return true;
    }

    public boolean recieveFile (String line, int state){
        switch(state){
        case 0: //new file
            try {
                writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null,
                            "error file test.txt couldn't be opened");
            }
            break;
            case 1: //append
                if(writer!=null)
                    writer.println(line);
                ClientUI.msgs.append(line+"\n");
                break;
                case 2: //close file
                    if(writer!=null)
                        writer.close();
        }
        return true;
    }
}


public class RandThread extends Thread {
    public void run() {
        try {
            // create and initialize the ORB
            ORB orb = ORB.init(RandClient.args, null);
            // get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references(
                    "RootPOA"));
            rootpoa.the_POAManager().activate();

            // create servant and register it with the ORB
            ClientImpl impl = new ClientImpl();
            // get object reference from the servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(impl);
            ClientSide href = ClientSideHelper.narrow(ref);

            // get the root naming context
            org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");
            // Use NamingContextExt which is part of the Interoperable
            // Naming Service (INS) specification.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // bind the Object Reference in Naming
            String name = "RandClient";
            NameComponent path[] = ncRef.to_name(name);
            ncRef.rebind(path, href);

            System.out.println("HelloServer ready and waiting ...");

            // wait for invocations from clients
            orb.run();
        }

        catch (Exception e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.out);
        }

        System.out.println("HelloServer Exiting ...");
    }
}
