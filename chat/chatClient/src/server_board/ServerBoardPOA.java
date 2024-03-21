package server_board;


/**
* server_board/ServerBoardPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.1"
* from server_board.idl
* Wednesday, May 10, 2006 9:27:16 PM AST
*/

public abstract class ServerBoardPOA extends org.omg.PortableServer.Servant
 implements server_board.ServerBoardOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("connect", new java.lang.Integer (0));
    _methods.put ("disconnect", new java.lang.Integer (1));
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
       case 0:  // server_board/ServerBoard/connect
       {
         String nickName = in.read_string ();
         String $result = null;
         $result = this.connect (nickName);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 1:  // server_board/ServerBoard/disconnect
       {
         String nickName = in.read_string ();
         String $result = null;
         $result = this.disconnect (nickName);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 2:  // server_board/ServerBoard/recieveMsg
       {
         String nickName = in.read_string ();
         String msg = in.read_string ();
         String $result = null;
         $result = this.recieveMsg (nickName, msg);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:server_board/ServerBoard:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public ServerBoard _this() 
  {
    return ServerBoardHelper.narrow(
    super._this_object());
  }

  public ServerBoard _this(org.omg.CORBA.ORB orb) 
  {
    return ServerBoardHelper.narrow(
    super._this_object(orb));
  }


} // class ServerBoardPOA
