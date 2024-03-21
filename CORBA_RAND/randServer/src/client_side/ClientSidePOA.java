package client_side;


/**
* client_side/ClientSidePOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.1"
* from randclient.idl
* Sunday, May 14, 2006 9:02:39 PM AST
*/

public abstract class ClientSidePOA extends org.omg.PortableServer.Servant
 implements client_side.ClientSideOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("recieveFile", new java.lang.Integer (0));
    _methods.put ("recieveNum", new java.lang.Integer (1));
    _methods.put ("recieveMsg", new java.lang.Integer (2));
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
       case 0:  // client_side/ClientSide/recieveFile
       {
         String line = in.read_string ();
         int state = in.read_long ();
         boolean $result = false;
         $result = this.recieveFile (line, state);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 1:  // client_side/ClientSide/recieveNum
       {
         int num = in.read_long ();
         boolean $result = false;
         $result = this.recieveNum (num);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 2:  // client_side/ClientSide/recieveMsg
       {
         String msg = in.read_string ();
         boolean $result = false;
         $result = this.recieveMsg (msg);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:client_side/ClientSide:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public ClientSide _this() 
  {
    return ClientSideHelper.narrow(
    super._this_object());
  }

  public ClientSide _this(org.omg.CORBA.ORB orb) 
  {
    return ClientSideHelper.narrow(
    super._this_object(orb));
  }


} // class ClientSidePOA