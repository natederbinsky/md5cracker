package edu.northeastern.ccis;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class MD5 {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		if (args.length != 1) {
			System.out.printf("Usage: java %s <password>%n", MD5.class.getCanonicalName());
		} else {
			final MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(args[0].getBytes());

			System.out.println(DatatypeConverter.printHexBinary(md.digest()));
		}
	}

}
