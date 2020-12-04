package no.alexander.AdventOfCode;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdventOfCode4Application implements CommandLineRunner {
	private static Logger LOG = LoggerFactory.getLogger(AdventOfCode4Application.class);

	private static final String BYR = "byr";
	private static final String IYR = "iyr";
	private static final String EYR = "eyr";
	private static final String HGT = "hgt";
	private static final String HCL = "hcl";
	private static final String ECL = "ecl";
	private static final String PID = "pid";
	private static final String CID = "cid";
	
	public static void main(String[] args) {
		SpringApplication.run(AdventOfCode4Application.class, args);
	}

	@Override
	public void run(String... args) throws URISyntaxException, IOException {
		URL input = getClass().getClassLoader().getResource("input.txt");
		File file = new File(input.toURI());
		
		List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
		
		List<Passport> passports = new ArrayList<>();
		Passport passport = new Passport();
		
		for (String line : lines) {
			if (line.isBlank()) {
				passports.add(passport);
				passport = new Passport();
			} else {
				passport.readLine(line);
			}
		}
		
		partOne(passports);
		partTwo(passports);
	}
	
	private void partOne(List<Passport> passports) {
		var valid = 0;
		for (Passport p : passports) {
			if (p.partOneIsValid()) {
				valid++;
			}
		}
		LOG.info("Part one - " + valid);
	}
	
	
	
	private void partTwo(List<Passport> passports) {
		var valid = 0;
		for (Passport p : passports) {
			if (p.partTwoIsValid()) {
				valid++;
			}
		}
		LOG.info("Part two - " + valid);
	}
	
	private class Passport {
		private String byr;
		private String iyr;
		private String eyr;
		private String hgt;
		private String hcl;
		private String ecl;
		private String pid;
		private String cid;
		
		public void readLine(String line) {
			String[] tokens = line.split(" ");
			for (String token : tokens) {
				parseToken(token);
			}
		}
		
		private void parseToken(String token){
			String[] parts = token.split(":");
			
			if (parts[0].equals(BYR)) {
				byr = parts[1];
			} else if (parts[0].equals(IYR)) {
				iyr = parts[1];
			} else if (parts[0].equals(EYR)) {
				eyr = parts[1];
			} else if (parts[0].equals(HGT)) {
				hgt = parts[1];
			} else if (parts[0].equals(HCL)) {
				hcl = parts[1];
			} else if (parts[0].equals(ECL)) {
				ecl = parts[1];
			} else if (parts[0].equals(PID)) {
				pid = parts[1];
			} else if (parts[0].equals(CID)) {
				cid = parts[1];
			}
		}
		
		public boolean partOneIsValid() {
			return byr != null && iyr != null && eyr != null && hgt != null && hcl != null && ecl != null && pid != null;
		}
		
		public boolean partTwoIsValid() {
			var valid = true;
			Integer yearOfBirth = parseInt(byr);
			valid &= yearOfBirth >= 1920 && yearOfBirth <= 2002;
			
			Integer issueYear = parseInt(iyr);
			valid &= issueYear >= 2010 && issueYear <= 2020;
			
			Integer expiryYear = parseInt(eyr);
			valid &= expiryYear >= 2020 && expiryYear <= 2030;
			
			valid &= validHeight();
			valid &= validHair();
			valid &= validEyeColour();
			valid &= validPid();
			
			return valid;
		}
		
		private int parseInt(String s) {
			try {
				return Integer.parseInt(s);
			} catch (NumberFormatException ex) {
				return -1;
			}
		}
		
		private boolean validHeight() {
			if (hgt == null) {
				return false;
			}
			
			if (hgt.endsWith("cm")) {
				int i = hgt.indexOf("cm");
				String height = hgt.substring(0, i);
				int h = Integer.parseInt(height);
				return h >= 150 && h <= 193;
			} else if (hgt.endsWith("in")) {
				int i = hgt.indexOf("in");
				String height = hgt.substring(0, i);
				int h = Integer.parseInt(height);
				return h >= 59 && h <= 76;
			}
			
			return false;
		}
		
		private boolean validHair() {
			if (hcl == null || hcl.length() != 7) {
				return false;
			}
			
			if (hcl.startsWith("#")) {
				String hair = hcl.substring(1, 7);
				for ( char c : hair.toCharArray()) {
					if (!Character.isDigit(c) && !(c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e' || c == 'f')) {
						return false;
					}
				}

				return true;
			}
			return false;
		}
		
		private boolean validEyeColour() {
			if (ecl == null) {
				return false;
			}
			
			return ecl.equals("amb") || ecl.equals("blu") || ecl.equals("brn") || ecl.equals("gry") || ecl.equals("grn") || ecl.equals("hzl") || ecl.equals("oth");
		}
		
		private boolean validPid() {
			if (pid == null) {
				return false;
			}
			
			return pid.length() == 9 && parseInt(pid) >= 0;
		}
		
	}
	
}
