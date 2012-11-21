package edu.njucs.timer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	GSEClock clock;
	ServerSocket serverSocket;
	ServerSocket serverMessageSocket;
	public static final int START=5678;
	public static final int SWITCH=1;
	public static final int PAUSE=2;
	public static final int NEXT_PHASE=3;
	public static final int ACK=8765;
	
	public static final int SET_TO_PAUSE=10000;
	public static final int SET_TO_RESUME=10001;
	public static final int EXIT=10003;
	public static final int STATE_0=10004;
	public static final int STATE_1=10005;
	public static final int STATE_2=10006;
	public static final int STATE_3=10007;
	public static final int END=10008;
	
	public Server()
	{
		clock=new GSEClock();
		try {
			serverSocket=new ServerSocket(8000);
			serverMessageSocket=new ServerSocket(8001);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		while (true)
		{
			Socket socket;
			Socket messageSocket;
			try {
				socket = serverSocket.accept();
				messageSocket= serverMessageSocket.accept();
				socket.setSoTimeout(1000);
				//建立新线程
	            HandleAClient handler = new HandleAClient(socket,messageSocket);
	            clock.setListener(handler);
	            new Thread(handler).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
		}
	}
	
	
	class HandleAClient implements Runnable, GSEClockListener
    {

		private boolean started=false;
        private Socket socket;
        private Socket messsageSocket;
        //8000
        private DataInputStream inputFromClient;
        private DataOutputStream outputToClient;
        private DataOutputStream messageToClient;
        
        public HandleAClient(Socket socket, Socket messsageSocket)
        {
            this.socket = socket;
            this.messsageSocket=messsageSocket;
        }

        public void run()
        {
            try {
                inputFromClient = new DataInputStream(socket.getInputStream());
                outputToClient = new DataOutputStream(socket.getOutputStream());
                messageToClient = new DataOutputStream(messsageSocket.getOutputStream());
                if (clock.paused)
                {
                	messageToClient.writeInt(SET_TO_PAUSE);
                }
                else
                {
                	messageToClient.writeInt(SET_TO_RESUME);
                }
                if (clock.end)
                {
                	messageToClient.writeInt(END);
                }
                else if (!clock.started)
                {
                	messageToClient.writeInt(STATE_0);
                }
                else
                {
                	messageToClient.writeInt(STATE_1+clock.phase);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.toString());
            }
            //成功
            try {
                int message;
            	//不断监听用户信息
                while (socket.isClosed() == false) {
                	try {
                		try{
                		      socket.sendUrgentData(0xFF);
                		}catch(Exception ex){
                		      break;
                		}
                        message=inputFromClient.readInt();
                        if (!started)
                        {
	                        if (message==START)
	                        {
	                        	started=true;
	                        	outputToClient.writeInt(ACK);
	                        }
	                        else
	                        {
	                        	socket.close();
	                        }
                        }
                        else
                        {
                        	if (message==SWITCH)
                        	{
                        		clock.switchSpeaker();
                        		outputToClient.writeInt(ACK);
                        	}
                        	else if (message==PAUSE)
                        	{
                        		clock.togglePause();
                        		outputToClient.writeInt(ACK);
                        	}
                        	else if (message==NEXT_PHASE)
                        	{
                        		clock.nextPhase();
                        		outputToClient.writeInt(ACK);
                        	}
                        }
                    }
                    catch (IOException e) {
                        continue;
                    }
                
                }
            }
            finally { //失去连接
                }
            }

		@Override
		public void paused() {
			try {
				messageToClient.writeInt(SET_TO_PAUSE);
			} catch (IOException e) {
			}
		}

		@Override
		public void resumed() {
			try {
				messageToClient.writeInt(SET_TO_RESUME);
			} catch (IOException e) {
			}
			
		}

		@Override
		public void exit() {
			try {
				messageToClient.writeInt(EXIT);
			} catch (IOException e) {
			}
		}

		@Override
		public void phaseChanged(int currentPhase) {
			try {
				messageToClient.writeInt(currentPhase+STATE_1);
			} catch (IOException e) {
			}
		}

		@Override
		public void end() {
			try {
				messageToClient.writeInt(END);
			} catch (IOException e) {
			}
		}
    }

	
	public static void main(String[] args)
	{
		Server server=new Server();
	}
}
