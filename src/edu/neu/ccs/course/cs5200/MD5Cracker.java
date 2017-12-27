package edu.neu.ccs.course.cs5200;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

public class MD5Cracker {
	
	public static void usage() {
		System.out.println(String.format("Usage: java %s [0aA-start-end] <hash>", MD5Cracker.class.getCanonicalName()));
		System.out.println(" If no pattern is provided, uses a list of top-1000 passwords from http://www.passwordrandom.com/most-popular-passwords");
		System.out.println("");
		System.out.println(" Otherwise, 0=[0-9]");
		System.out.println(" Otherwise, a=[a-z]");
		System.out.println(" Otherwise, A=[A-Z]");
		System.out.println("");
		System.out.println(" So 1-4-6 would be 4-to-6-digit iPhone PINs");
		System.out.println(" a-1-5 would be lowercase alpha strings of length 1 to 5");
		System.exit(0);
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		System.out.printf("Start: %s%n", dateFormat.format(new Date()));
		
		if (args.length == 1) {
			final String TARGET = args[args.length-1].toUpperCase();
			
			boolean found = false;
			int i;
			for (i=0; i<CommonHashes.MD5.length; i++) {
				if (CommonHashes.MD5[i].equals(TARGET)) {
					System.out.printf("Password: %s%n", CommonHashes.PW[i]);
					found = true;
					break;
				}
			}
			
			if (!found) {
				System.out.println("No matching hash found");
			}
			System.out.printf("Searched: %,d%n", i + (found?1:0));
			
		} else if (args.length == 2) {
			final String TARGET = args[args.length-1].toUpperCase();
			
			int START = -1;
			int END = -1;
			String CHARS = "";
			{
				final String[] P_ARRAY = args[0].split("-");
				if (P_ARRAY.length != 3) {
					usage();
				}
				
				try {
					START = Integer.parseInt(P_ARRAY[1]);
					END = Integer.parseInt(P_ARRAY[2]);
				} catch (Exception e) {
					usage();
				}
				
				if (START < 1 || END < START) {
					usage();
				}
				
				boolean nums = false;
				boolean lower = false;
				boolean upper = false;
				for (char c : P_ARRAY[0].toCharArray()) {
					if (c == '1') {
						if (!nums) {
							nums = true;
							CHARS += "0123456789";
						} else {
							usage();
						}
					} else if (c == 'a') {
						if (!lower) {
							lower = true;
							CHARS += "abcdefghijklmnopqrstuvwxyz";
						} else {
							usage();
						}
					} else if (c == 'A') {
						if (!upper) {
							upper = true;
							CHARS += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
						} else {
							usage();
						}
					}
				}
				
				System.out.printf("Generating strings of length [%d, %d] using %s%n", START, END, Arrays.toString(CHARS.toCharArray()));
				final MessageDigest md = MessageDigest.getInstance("MD5");
				
				boolean done = false;
				int count = 0;
				
				for (int i=START; i<=END; i++) {
					System.out.printf("Searching length %d...%n", i);
					
					int[] indexes = new int[i];
					byte[] pw = new byte[i];
					
					for (int j=0; j<pw.length; j++) {
						pw[j] = (byte) CHARS.charAt(0);
					}
					indexes[0] = -1;
					
					while (_next(CHARS, indexes, pw)) {
						
						md.update(pw);
					    final byte[] digest = md.digest();
					    final String myHash = DatatypeConverter.printHexBinary(digest);
					    count++;
					    
					    if (myHash.equals(TARGET)) {
					    		System.out.printf("Password found: %s%n", new String(pw));
					    		done = true;
					    		break;
					    }
					}
					
					if (done) break;
				}
				
				if (!done) System.out.println("No matching hash found");
				System.out.printf("Searched: %,d%n", count);
			}
			
		} else {
			usage();
		}
		
		System.out.printf("Stop: %s%n", dateFormat.format(new Date()));
	}
	
	private static boolean _next(String CHARS, int[] indexes, byte[] pw) {
		int i;
		for (i=0; i<indexes.length; i++) {
			if (indexes[i] != CHARS.length()-1) {
				break;
			}
		}
		
		if (i >= indexes.length)
			return false;
		
		indexes[i]++;
		pw[i] = (byte) CHARS.charAt(indexes[i]);
		
		for (int j=0; j<i; j++) {
			indexes[j] = 0;
			pw[j] = (byte) CHARS.charAt(0);
		}
		
		return true;
	}

}
