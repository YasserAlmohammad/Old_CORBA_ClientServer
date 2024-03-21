package randserver;

import server_side.*;
import client_side.*;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POA;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.JOptionPane;

class RandImpl extends ServerSidePOA {
    ORB orb = null;
    public int getRand(int param) {
        try {
            ORB orb = ORB.init(RandServer.args, null);

            // get the root naming context
            org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");
            // Use NamingContextExt instead of NamingContext. This is
            // part of the Interoperable naming Service.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // resolve the Object Reference in Naming
            String name = "RandClient";
            ClientSide client = ClientSideHelper.narrow(ncRef.resolve_str(
                    "RandClient"));

            ServerUI.msgs.append("rand=" + param + "\n");
            if (param == 0) {
                File file = new File("test.txt");
                if (!file.exists()) {
                    JOptionPane.showMessageDialog(null,
                            "error happend while reading from client");
                } else {
                    BufferedReader fileReader = new BufferedReader(new
                            FileReader(
                                    file));
                    String line;
                    int i = 0;
                    client.recieveFile(" ", 0); //new file
                    while ((line = fileReader.readLine()) != null) {
                        client.recieveFile(line, 1);
                    }
                    client.recieveFile(" ", 2); //close file
                }
            }
                if (param > 0) {
                    client.recieveNum(param + 3);
                }

                if (param < 0) {
                    client.recieveMsg("negative value, resend");
                }
            } catch (Exception ex) {

            }

            return 0;
        }
    }


    public class RandThread extends Thread {
        static String args[] = null;
        public void run() {
            try {
                // create and initialize the ORB
                ORB orb = ORB.init(RandServer.args, null);
                // get reference to rootpoa & activate the POAManager
                POA rootpoa = POAHelper.narrow(orb.resolve_initial_references(
                        "RootPOA"));
                rootpoa.the_POAManager().activate();

                // create servant and register it with the ORB
                RandImpl impl = new RandImpl();
                // get object reference from the servant
                org.omg.CORBA.Object ref = rootpoa.servant_to_reference(impl);
                ServerSide href = ServerSideHelper.narrow(ref);

                // get the root naming context
                org.omg.CORBA.Object objRef =
                        orb.resolve_initial_references("NameService");
                // Use NamingContextExt which is part of the Interoperable
                // Naming Service (INS) specification.
                NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

                // bind the Object Reference in Naming
                String name = "RandServer";
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
