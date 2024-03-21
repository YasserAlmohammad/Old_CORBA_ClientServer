package calcserver;

import calc.*;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POA;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;

class CalcImpl extends CalcPOA {
    public float calc(int op, float param1, float param2) {
        float res = 0;
        switch (op) {
        case 0: //+
            res = param1 + param2;
            CalcUI.msgs.append(param1+"+"+param2+"="+res+"\n");
            break;
        case 1: //-
            res = param1 - param2;
            CalcUI.msgs.append(param1+"-"+param2+"="+res+"\n");
            break;
        case 2: //*
            res = param1 * param2;
            CalcUI.msgs.append(param1+"*"+param2+"="+res+"\n");
            break;
        case 3: // /
            res = param1 / param2;
            CalcUI.msgs.append(param1+"/"+param2+"="+res+"\n");
            break;
        default:

        }
        return res;
    }
}


public class CalcThread extends Thread {
    static String args[] = null;
    public void run() {
        System.out.println("fuck");
        try {
            // create and initialize the ORB
            ORB orb = ORB.init(args, null);
            // get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references(
                    "RootPOA"));
            rootpoa.the_POAManager().activate();

            // create servant and register it with the ORB
            CalcImpl impl = new CalcImpl();
            // get object reference from the servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(impl);
            Calc href = CalcHelper.narrow(ref);

            // get the root naming context
            org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");
            // Use NamingContextExt which is part of the Interoperable
            // Naming Service (INS) specification.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // bind the Object Reference in Naming
            String name = "CalcServer";
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
