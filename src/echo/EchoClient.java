package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class EchoClient {
	private static final String SERVER_IP = "117.16.40.176";
	private static final int SERVER_PORT = 6060;
	
	public static void main(String[] args) {
		Socket socket = null;

		//1. 키보드 연결
		Scanner scanner = new Scanner( System.in );
		
		try {
		
			//2. 소켓 생성
			socket = new Socket();
			
			//3. 서버 연결
			socket.connect( new InetSocketAddress( SERVER_IP, SERVER_PORT) );
			
			//4. IOStream 세팅
			BufferedReader br =
				new BufferedReader( 
				new InputStreamReader( socket.getInputStream(), "UTF-8" ) );
			PrintWriter pw = 
				new PrintWriter(
				new OutputStreamWriter( socket.getOutputStream(), "UTF-8" ), true );	
			
			while( true ) {
				System.out.print(">>");
				String message = scanner.nextLine();
				if( "exit".equals( message ) ) {
					break;
				}
				
				//5. 메세지 보내기
				pw.println( message );
				
				//6. 에코 메세지 받기
				String echoMessage = br.readLine();
				if( echoMessage == null ) {
					System.out.println( "[client] disconnected by server" );
					break;
				}
				
				//7. 출력
				System.out.println( "<<" + echoMessage );
			}
		} catch(ConnectException e ){
			System.out.println( "[clinet] not connected to server" );
		} catch(SocketException e ){
			System.out.println( "[client] closed by server");
		} catch( IOException e ) {
			e.printStackTrace();
		} finally {
			try {
				if( socket != null && socket.isClosed() == false ){
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			scanner.close();
		}
	}
}
