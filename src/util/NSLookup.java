package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookup {

	public static void main(String[] args) {
		Scanner scanner = new Scanner( System.in );

		while( true ) {
			System.out.print( ">" );
			String host = scanner.nextLine();
			if( "exit".equals( host ) ) {
				break;
			}
			
			try {
				InetAddress[] inetAddresses =
						InetAddress.getAllByName(host);
				for(InetAddress inetAddress : inetAddresses) {
					String hostAddress = inetAddress.getHostAddress();
					System.out.println( host + ":" + hostAddress);
				}
			} catch( UnknownHostException e ) {
				System.out.println( "알수 없는 도메인 입니다." );
			}
		}
		
		scanner.close();
	}

}
