package client_side;


/**
* client_side/ClientSideOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.1"
* from randclient.idl
* Sunday, May 14, 2006 9:02:39 PM AST
*/

public interface ClientSideOperations 
{
  boolean recieveFile (String line, int state);
  boolean recieveNum (int num);
  boolean recieveMsg (String msg);
} // interface ClientSideOperations
