package paris;


import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;

import paris.motor;

public class ServerParis
{
	private static ServerSocket serverSocket;
	private static final int PORT = 1;
	
	public static void main(String[] args)
			throws IOException
			{
		
		try
		{
			serverSocket = new ServerSocket(PORT);
			System.out.println("MULTI CLIENT SERVER pada PORT:" + PORT);
		}
		catch (IOException ioEx)
		{
			System.out.println("\nTidak Dapat Membuka Port!");
			System.exit(1);
		}
		do
		{
			//Tunggu Koneksi
			System.out.println("Server Siap...");
			Socket client = serverSocket.accept();
			InetAddress ip = client.getInetAddress();
			System.out.println("\nClient Baru masuk..\n"
							+"Dengan IP : "+ip.toString()+ 
							"\nNama : "+ip.getHostName().toString()+"\n");
			//Create a thread to handle communication with
			//this client and pass the constructor for this
			//thread a reference to the relevant socket...
			ClientHandler handler =
					new ClientHandler(client);
			handler.start();//As usual, method calls run .
		}while (true);
			}
}
class ClientHandler extends Thread
{
	private Socket client;
	private Scanner input;
	private PrintWriter output;
	private String gate;
	private motor motor;
	
	public ClientHandler(Socket socket)
	{
		//Set up reference to associated socket...
		client = socket;
		try
		{
			input = new Scanner(client.getInputStream());
			output = new PrintWriter(
					client.getOutputStream(),true);
		}
		catch(IOException ioEx){
			ioEx.printStackTrace();
		}
	}
	public void run()
	{
		motor = new motor();
		String received = null;
		//biar tahu sebagia apa dia masuk
		boolean y = false;
		do {
		received = input.nextLine();
		gate  = received;
		output.println("Selamat Datang di Server Parkir .. Anda Masuk Sebagai Gate : "+gate);
		if (!received.equalsIgnoreCase("QUIT"))
		{
			if (gate.equalsIgnoreCase("IN"))
			{
				do
				{
						//menerima pesan dari client
						//melalui socket input stream..
						received = input.nextLine();
						
							//memasukkan data ke array
							if (!received.equalsIgnoreCase("GANTI") && !received.equalsIgnoreCase("QUIT"))
							{
								paris.motor.masukkedatabase(received);
								
								//menampilkan yang tersimpan pada server (kalau tidak digunakan tinggal di comment)
								System.out.println("Masuk : "+received+"; No karcis : "+motor.getKarcis()+"; Masuk Pada : "+new Date().toString()+"; kapasitas tersisa : "+ paris.motor.getKapasitas());
								output.println("Masuk : "+received+"; No karcis : "+motor.getKarcis()+"; Masuk Pada : "+new Date().toString()+"; kapasitas tersisa : "+ paris.motor.getKapasitas());
							}
							else if (!received.equalsIgnoreCase("QUIT"))
							{
								output.println("Beralih Gate!");
							}
								
				}while (!received.equalsIgnoreCase("QUIT") || !received.equalsIgnoreCase("GANTI") );
				
			} 
			else if (gate.equalsIgnoreCase("OUT"))
			{
				boolean boy = false;
				do
				{
						//menerima pesan dari client
						//melalui socket input stream..
						received = input.nextLine();
						if (!received.equalsIgnoreCase("GANTI"))
						{
							String hasil;
							//update data array
							if(paris.motor.getNopol(received).equalsIgnoreCase("")){
								hasil = "Data dengan No Karcis "+received+" Tidak ditemukan!";
							}else{
								hasil = "Kendaraan keluar dengan nopol "+paris.motor.getNopol(received)+
										"pada "+new Date().toString();
								System.out.println("Keluar : "+paris.motor.getNopol(received)+" ; Pada Jam : "
													+new Date().toString()+" ; No Karcis : "+received);
							}
							output.println(hasil);
						}
						else if (received.equalsIgnoreCase("GANTI"))
						{
							output.println("Beralih Gate!");
							boy = true;
						}
				}while (boy == true);
			}
		}
		else
		{
			output.println("Terimakasih");
			y = true;
		}
		}while(y == true);
		
		try
		{
			if (client!=null)
			{
				System.out.println(
						"Menutup koneksi...sukses..");
				client.close();
			}
		}
		catch(IOException ioEx)
		{
			System.out.println("Gagal menutup koneksi!");
		}
	}
}