import java.io.*;
import java.util.*;

/* Mac Address Spoofer
   OSX
*/

public class ipChanger{
	public static void main(String []args) throws IOException{
        
		Runtime rt = Runtime.getRuntime(); //gets runtime

		//List of commands for usage
		String[] commands = {"ifconfig", "arp -a", "sudo ifconfig en0 ether "}; 

		Process p = rt.exec(commands[0]);
		String[] macAndIP = findMac(p);
		p.destroy(); //Kills ifconfig process, no longer needed

		String subnet = printBasics(macAndIP);
		scan(rt, subnet);

	    p = rt.exec(commands[1]);

		List<String> list = printOtherComputers(p);
		p.destroy();
		System.out.println();

		Scanner input = new Scanner(System.in);
		System.out.print("\t\t Enter the number of the Computer whose Mac Address you want to copy ");

		int temp = input.nextInt() - 1;
		Process change = rt.exec(commands[2] + list.get(temp));
		String password = input.next();


		p = rt.exec(commands[0]);
		String[] newMac = findMac(p);
		p.destroy();
		print("Your Mac Address is now: " + newMac[0]);

		while(temp != -1){
			System.out.print("\t\t Enter the number of the Computer whose Mac Address you want to copy, enter 0 to go back to the original ");
			temp = input.nextInt() - 1;
			rt.exec(commands[2] + list.get(temp));
			p = rt.exec(commands[0]);
			newMac = findMac(p);
			p.destroy();
			print("Your Mac Address is now: " + newMac[0]);
			System.out.println();
		}

		print("Reverting Mac Address back to original!");
		rt.exec(commands[2] + macAndIP[0]);
		p = rt.exec(commands[0]);
		newMac = findMac(p);
		p.destroy();
		print("Your Mac Address is back to: " + newMac[0]);
		print("Thank you for using this program!");
		System.out.println();

		// System.out.println();
		// print("Success!");

		// print("Click anything to revert your Mac Address back to the original");

		// String next = input.next();

		// p.destroy();

		// rt.exec(commands[2] + macAddress);

		// System.out.println();

		// print("Success, Program is done!");
		// System.out.println();
		

	}

	public static String printBasics(String[] macAndIP){
		System.out.println();
		print("Your Mac Address is " + macAndIP[0]);
		print("Your IP Address is " + macAndIP[1]);
		String subnet = macAndIP[1].substring(0, macAndIP[1].lastIndexOf(".") + 1); 
		print("The Subnet IP Address is " + subnet + "x");
		System.out.println();
		return subnet;
	}


	// Pings every IP address for arp -a command later
	public static void scan(Runtime rt, String x) throws IOException{
		//System.out.print("Pinging all IP addresses on router... ");
		for(int i = 1; i < 255; i++){
			Process p = rt.exec("ping " + x + i);
			System.out.print("\r\t\t Pinging all IP addresses on subnet... " + (i+1) + "/255");
			try {
    			Thread.sleep(50);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
    			Thread.currentThread().interrupt();
			}
			p.destroy();
		}
		System.out.println();
	}
	

	public static void print(String s){
		System.out.println("\t\t " + s );
	}

	public static List<String> printOtherComputers(Process p1) throws IOException{
		print("Other Computers on this network:");
		print("Name:\t\t\t\t\tIP:\t\t\tMac:\t\t\tType:");
		int n = 0;
		List<String> list = new ArrayList<String>();

		BufferedReader in = new BufferedReader(
           new InputStreamReader(p1.getInputStream()) );
		String line;
		//in.readLine();
		while((line = in.readLine()) != null){
				if(!(line.contains("(incomplete)") || line.contains("ermanen"))){
					n++;
					print(n + ") " + nextComp(line, list));
				}
			
		}

		if(n == 0){
			System.out.println();
		    print("No other computers on this Network D:");
		}

		return list;
	}

	public static String nextComp(String line, List<String> list){
		Scanner s = new Scanner(line);
		String name = s.next();
		String ip = s.next();
		ip = ip.substring(1, ip.length() - 1);
		s.next();
		String mac = s.next();
		list.add(mac);
		for (int i = 0; i < 3; i++)
			s.next();
		String connection = s.next();
		connection = connection.substring(1, connection.length() - 1);
		String space = "\t\t\t\t\t";
		String space2 = "\t";
		if(name.length() > 10)
			space = "\t\t";
		if(mac.length() < 16)
			space2 = "\t\t";
		return name + space + ip + "\t\t" + mac + space2 + connection;
	}

	public static String[] findMac(Process p) throws IOException{
		String[] macAndSub = new String[2]; 

		BufferedReader in = new BufferedReader(
           new InputStreamReader(p.getInputStream()) );

		String line;
		while((line = in.readLine()) != null){
			if(line.startsWith("en0:")){
				//in.readLine(); //skips line
				macAndSub[0] = in.readLine().substring(7);
				in.readLine();
				Scanner s = new Scanner(in.readLine());
				s.next();
				macAndSub[1] = s.next();
			}
		}
		in.close();

		return macAndSub;

	}

}