package http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;

public class RequestHandler extends Thread {
	private Socket socket;
	
	public RequestHandler( Socket socket ) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			// get IOStream
			BufferedReader br = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
			OutputStream os = socket.getOutputStream();

			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = ( InetSocketAddress )socket.getRemoteSocketAddress();
			consoleLog( "connected from " + inetSocketAddress.getHostName() + ":" + inetSocketAddress.getPort() );
			
			String request = null;
			
			while( true ) {
				String line = br.readLine();
				if( line == null || "".equals(line) ) {
					break;
				}
				if( request == null ) {
					request = line;
				}
			}
			
			consoleLog( request );
			
			// 요청 분석
			String[] tokens = request.split( " " );
			if( "GET".equals( tokens[ 0 ] ) ) {
				responseStaticResource( os, tokens[1], tokens[2] );
			} else {
				//POST, DELETE, PUT
				// 심플 웹 서버에서는 잘못된 요청으로 처리( 400 )
			}
			
			// 예제 응답입니다.
			// 서버 시작과 테스트를 마친 후, 주석 처리 합니다.
			// outputStream.write( "HTTP/1.1 200 OK\r\n".getBytes( "UTF-8" ) );
			// outputStream.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) );
			// outputStream.write( "\r\n".getBytes() );
			// outputStream.write( "<h1>이 페이지가 잘 보이면 실습과제 SimpleHttpServer를 시작할 준비가 된 것입니다.</h1>".getBytes( "UTF-8" ) );

		} catch( Exception ex ) {
			consoleLog( "error:" + ex );
		} finally {
			// clean-up
			try{
				if( socket != null && socket.isClosed() == false ) {
					socket.close();
				}
				
			} catch( IOException ex ) {
				consoleLog( "error:" + ex );
			}
		}			
	}
	
	public void responseStaticResource(
			OutputStream os,
			String url,
			String protocol ) 
			throws IOException {
		
		if( "/".equals( url ) ) {
			url = "/index.html"; // deafult 문서 처리 
		}
		
		// ./webapp -> Document Root
		File file = new File( "./webapp/" + url );
		if( file.exists() == false ) {
			// error 처리
			// 404 -> File Not Found
			//response404Error( os, protocol );
			return;
		}
		
		byte[] body = Files.readAllBytes( file.toPath() );
		String mimeType = Files.probeContentType( file.toPath() );
		
		//header 전송
		os.write( (protocol + " 200 OK\r\n").getBytes( "UTF-8" ) );
		os.write( ("Content-Type:" + mimeType + "; charset=utf-8\r\n").getBytes( "UTF-8" ) );
		os.write( "\r\n".getBytes( "UTF-8" ));
		//body 전송
		os.write( body );
	}
	
	public void consoleLog( String message ) {
		System.out.println( "[RequestHandler#" + getId() + "] " + message );
	}
}
