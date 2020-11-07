package databases;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	
	// Source: https://mkyong.com/java/java-how-to-convert-bytes-to-hex/
	public static String hex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
             result.append(String.format("%02X", aByte));
        }
        return result.toString();
    }

	public static void main(String[] args) throws NoSuchAlgorithmException {
		if (args.length != 1) {
			System.out.printf("Usage: java %s <password>%n", MD5.class.getCanonicalName());
		} else {
			final MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(args[0].getBytes());

			System.out.println(hex(md.digest()));
		}
	}

}
