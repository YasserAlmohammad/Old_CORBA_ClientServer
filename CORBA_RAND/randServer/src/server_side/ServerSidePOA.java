package server_side;


/**
* server_side/ServerSidePOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.1"
* from rand.idl
* Wednesday, May 10, 2006 11:48:36 PM AST
*/

public abstract class ServerSidePOA extends org.omg.PortableServer.Servant
 implements server_side.ServerSideOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("getRand", new java.lang.Integer (0));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // server_side/ServerSide/getRand
       {
         int param1 = in.read_long ();
         int $result = (int)0;
         $result = this.getRand (param1);
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:server_side/ServerSide:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public ServerSide _this() 
  {
    return ServerSideHelper.narrow(
    super._this_object());
  }

  public ServerSide _this(org.omg.CORBA.ORB orb) 
  {
    return ServerSideHelper.narrow(
    super._this_object(orb));
  }


} // class ServerSidePOA
