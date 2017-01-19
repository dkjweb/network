package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class EchoServer {
	private static final int PORT = 6000;
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		
		try {
			//1. 서버 소켓 생성
			serverSocket = new ServerSocket();

			//2. binding
			InetAddress inetAddress = InetAddress.getLocalHost();
			String localhostAddress = inetAddress.getHostAddress();
			serverSocket.bind( new InetSocketAddress( localhostAddress, PORT ) );
			System.out.println( "[server] binding " + localhostAddress + ":" + PORT );
			
			//3. accept 연결 요청 기다림
			Socket socket = serverSocket.accept(); //blocking 
			
			//4. 연결 성공
			InetSocketAddress remoteAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
			int remoteHostPort = remoteAddress.getPort();
			String remoteHostAddress = remoteAddress.getAddress().getHostAddress();
			System.out.println( "[server] connected from " + remoteHostAddress + ":" + remoteHostPort );
			
			try {
				//5 IOStream 받아오기(from socket)
				BufferedReader br = new BufferedReader( new InputStreamReader( socket.getInputStream(), "UTF-8" ) );
				PrintWriter pw = new PrintWriter( new OutputStreamWriter( socket.getOutputStream(), "UTF-8"), true );
				
				while( true ) {
					//6. 데이터 읽기
					String data = br.readLine(); // blocking
					if( data == null ) {
						// 클라이언트가 소켓을 닫은 경우(클라이언트가 정상 종료)
						System.out.println( "[server] disconnected by client");
						break;
					}

					System.out.println( "[server] received :" + data);
					
					//7. 데이터 쓰기
					pw.println( data );
					// pw.print( data + "\n" );
				}
			} catch( SocketException e ) {
				// 클라이언트가 소켓을 정상적으로 닫지 않고 종료한 경우
				System.out.println( "[server] closed by client" );
			} catch( IOException e ) {
				e.printStackTrace();
			} finally {
				//자원 정리
				try{
					if( socket != null && socket.isClosed() == false ) {
						socket.close();
					}
				} catch( IOException e ) {
					e.printStackTrace();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				//자원정리
				if( serverSocket != null && serverSocket.isClosed() == false ) {
					serverSocket.close();
				}
			} catch( IOException e ) {
				e.printStackTrace();
			}
		}		
	}
}
