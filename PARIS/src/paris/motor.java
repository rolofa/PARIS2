package paris;

import java.util.Date;
import java.util.Vector;

public class motor {
	private static int x = 0;
	private static int kapasitas = 0;
	private static Vector<String> karcis=new Vector<String>();
	private static Vector<String> nopol=new Vector<String>();
	private static Vector<String> tglMasuk=new Vector<String>();
	private static Vector<String> tglKeluar=new Vector<String>();
	private static Vector<Boolean> status=new Vector<Boolean>();
	public motor() {
		// TODO Auto-generated constructor stub
	}

	public static int getKapasitas(){
		return (5-kapasitas);
	}
	public String getKarcis()
	{
		return karcis.elementAt(x-1);
	}
	public static void masukkedatabase(String plat)
	{
		try{
			if (karcis.size() <= getKapasitas())
			{
				karcis.add("STIKOM-"+(x+1));
				nopol.add(plat);
				tglMasuk.add(new Date().toString());
				tglKeluar.add("");
				status.add(false);
				
				x++;
				kapasitas++;
			}
			else 
			{
				System.out.println("Maaf Parkir Penuh!");
			}
		}catch(Exception e)
		{
			System.out.println("error : "+ e);
		}
	}
	
	public static String getNopol(String k){
		String ketNopol="";
		for(int i=0;i<karcis.size();i++){
			if(k.equalsIgnoreCase(karcis.elementAt(i)) && status.elementAt(i)==false){
				ketNopol=nopol.elementAt(i);
				tglKeluar.setElementAt(new Date().toString(), i);
				status.setElementAt(true, i);
				kapasitas--;
			}
		}
		return ketNopol;
	}
}

