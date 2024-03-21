package chatclient;

import client_board.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;


class ClientThread extends Thread{
    String name=null;
    ORB orb=null;
    public void run(){

         try{
                 // create and initialize the ORB
           ORB orb = ORB.init(ChatClient.args, null);

           // get reference to rootpoa & activate the POAManager
           POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
           rootpoa.the_POAManager().activate();

           // create servant and register it with the ORB
           Client client=new Client();
           org.omg.CORBA.Object ref = rootpoa.servant_to_reference(client);
           ClientBoard href = ClientBoardHelper.narrow(ref);

           // get the root naming context
           org.omg.CORBA.Object objRef =
               orb.resolve_initial_references("NameService");
           // Use NamingContextExt which is part of the Interoperable
           // Naming Service (INS) specification.
           NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

           // bind the Object Reference in Naming
           NameComponent path[] = ncRef.to_name( name );
           ncRef.rebind(path, href);


           System.out.println("chat client ready and waiting ...");

           // wait for invocations from clients
           orb.run();
           System.out.println("exiting orb\n");
         }

           catch (Exception e) {
             System.err.println("ERROR: " + e);
             e.printStackTrace(System.out);
      }
    }
}

public class Client extends ClientBoardPOA {
    public static ChatUI ui = null;
    public Client() {
        super();
    }

    /**
     * recive a message from others(server) and print it
     * @param msg String
     * @return Object
     * @throws RemoteException
     */
    public String recieveMsg(String msg) {
        //noitfy all
        ui.msgs.append(msg + "\n");
        return "ok";
    }

}
