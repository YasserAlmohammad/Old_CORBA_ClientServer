package chatserver;

import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JOptionPane;

import server_board.*;
import client_board.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;


/**
 * implementation of the remote interface methods
 *
 * clients into is held into a hash set(unique nicknames)
 */

class ServerThread extends Thread{
    ORB orb=null;
    public void run(){
        String name = "ChatServer";
        try {
            // create and initialize the ORB

            orb = ORB.init(ChatServer.args, null);
            // get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references(
                    "RootPOA"));
            rootpoa.the_POAManager().activate();

            // create servant and register it with the ORB
            //   HelloImpl helloImpl = new HelloImpl();
            //   helloImpl.setORB(orb);
         //   this.setORB(orb);
            // get object reference from the servant
            Server server=new Server();
            server.orb=orb;
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(server);
            ServerBoard href = ServerBoardHelper.narrow(ref);

            // get the root naming context
            org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");
            // Use NamingContextExt which is part of the Interoperable
            // Naming Service (INS) specification.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // bind the Object Reference in Naming
            //    String name = "Hello";
            NameComponent path[] = ncRef.to_name(name);
            ncRef.rebind(path, href);

            System.out.println("ChatServer ready and waiting ...");

            // wait for invocations from clients
            orb.run();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "corba is not inited well");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}

public class Server extends ServerBoardPOA {
    public static ServerUI ui = null;
    HashSet hashset = new HashSet(20);
    public ORB orb=null;

    class ClientInfo {
        String nickName = null;
        ClientBoard lookup = null;
        ClientInfo(String nick, ClientBoard client) {
            nickName = nick;
            lookup = client;
        }

        /**
         * equality through nickname only
         */
        public boolean equals(java.lang.Object o) {
            ClientInfo info = (ClientInfo) o;
            return info.nickName.equals(nickName);
        }

        /**
         * we return the nickName hashcode
         * @return int
         */
        public int hashCode() {
            return nickName.hashCode();
        }
    }


    public Server() {
        super();
    }


    /**
     * connect by passing his nickname nicknames are unique
     *
     * we notify everyone with this connection
     *
     * @param clientAddr String
     * @param nickName String
     * @return Object
     * @throws RemoteException
     */
    POA rootpoa=null;
    public String connect(String nickName) {
        ClientInfo cinfo = new ClientInfo(nickName, null);
        if (hashset.add(cinfo)) {
            //look it up
            ClientBoard client = null;
            try {
                    // create and initialize the ORB
                    orb = ORB.init(ChatServer.args, null);
                    // get reference to rootpoa & activate the POAManager
                    rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
                    rootpoa.the_POAManager().activate();

                    // get the root naming context
                    org.omg.CORBA.Object objRef =
                            orb.resolve_initial_references("NameService");
                    // Use NamingContextExt instead of NamingContext. This is
                    // part of the Interoperable naming Service.
                    NamingContextExt ncRef = NamingContextExtHelper.narrow(
                            objRef);

                        // resolve the Object Reference in Naming
                    String name =nickName;
                    client = ClientBoardHelper.narrow(ncRef.resolve_str(name));

                    //  System.out.println("Obtained a handle on server object: " + helloImpl);
                    //  System.out.println(helloImpl.sayHello());
                    //  helloImpl.shutdown();
            } catch (Exception ex1) {
                ui.msgs.append("exception in lookup\n");
            }
            if (client == null) {
                ui.msgs.append("client not found\n");
                return "not";
            }
            cinfo.lookup = client;

            //notify all others
            String msg = ">>" + nickName + " has joined in...";
            ui.msgs.append(msg + "\n");

            Iterator itr = hashset.iterator();
            while (itr.hasNext()) {
                try {
                    ClientInfo info = (ClientInfo) itr.next();
                    String nick = info.nickName;
                    if (info.lookup != null)
                        info.lookup.recieveMsg(msg);

                } catch (Exception ex) {
                    ui.msgs.append("Client exception: " + ex.getMessage() +
                                   "\n");
                    ex.printStackTrace();
                }

            }
            return "ok";
        } else {
            return "not";
        }
    }

    /**
     * disconnect from the server by removing clients info from the hashset
     * and notifying all others
     *
     * @param nickName String
     * @return Object
     * @throws RemoteException
     */
    public String disconnect(String nickName){
        hashset.remove(new ClientInfo(nickName, null));
        //notify all
        String msg = ">>" + nickName + " has disconnected...";
        ui.msgs.append(msg + "\n");

        Iterator itr = hashset.iterator();
        while (itr.hasNext()) {
            try {
                ClientInfo info = (ClientInfo) itr.next();
                String nick = info.nickName;
                if (info.lookup != null)
                    info.lookup.recieveMsg(msg);

            } catch (Exception ex) {
                ui.msgs.append("Client exception: " + ex.getMessage() + "\n");
                ex.printStackTrace();
            }

        }

        return "ok";
    }

    /**
     * getting a message from a client we cast it to all clients
     *
     * @param nickName String
     * @param msg String
     * @return Object
     * @throws RemoteException
     */
    public String recieveMsg(String nickName, String msg){
        //noitfy all
        String castMsg = "[" + nickName + "]:" + msg;
        ui.msgs.append(castMsg + "\n");
        Iterator itr = hashset.iterator();
        while (itr.hasNext()) {
            try {
                ClientInfo info = (ClientInfo) itr.next();
                String nick = info.nickName;
                if (info.lookup != null)
                    info.lookup.recieveMsg(castMsg);

            } catch (Exception ex) {
                ui.msgs.append("Client exception: " + ex.getMessage() + "\n");
                ex.printStackTrace();
            }

        }
        return "ok";
    }

}
