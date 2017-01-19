package test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHost {

	public static void main(String[] args) {
		try {
			
			InetAddress inetAddress = 
					InetAddress.getLocalHost();
			
			String hostAddress = 
					inetAddress.getHostAddress();
			String hostName = 
					inetAddress.getHostName();
			byte[] addresses = 
					inetAddress.getAddress();
			
			System.out.println( hostAddress );
			System.out.println( hostName );
			for( int i = 0; i < addresses.length; i++) {
				int intAddress = 
						addresses[i] & 0x000000ff;
				System.out.print( intAddress );
				if( i < 3 ) {
					System.out.print( "." );
				}
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		

	}

}
