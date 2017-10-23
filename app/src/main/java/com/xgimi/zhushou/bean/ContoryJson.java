package com.xgimi.zhushou.bean;

import android.content.Context;

import com.xgimi.zhushou.R;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;

public class ContoryJson {
public Context context;
	public static ContoryJson contory;
	public String json;
	public static ContoryJson getInstance(Context context){
		if(contory==null){
			contory=new ContoryJson(context);
		}
		return  contory;
	}
	private ContoryJson(Context conte){
		this.context=conte;
		readFromRaw();
	}
	/**
	 * 从raw中读取txt
	 */
	private void readFromRaw() {
		try {
			InputStream is = context.getResources().openRawResource(R.raw.tel);
			readTextFromSDcard(is);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


private String readTextFromSDcard(InputStream is) throws Exception {
//    InputStreamReader reader = new InputStreamReader(is);
//    BufferedReader bufferedReader = new BufferedReader(reader);
//    StringBuffer buffer = new StringBuffer("");
//    String str;
//    while ((str = bufferedReader.readLine()) != null) {
//        buffer.append(str);
//        buffer.append("\n");
//    }
//    json=buffer.toString();
//    return buffer.toString();
	String res = ""; 
	try{ 
	InputStream in =context. getResources().openRawResource(R.raw.tel); 
	int length = in.available(); 
	byte [] buffer = new byte[length]; 
	in.read(buffer); 
	res = EncodingUtils.getString(buffer,"gbk");//选择合适的编码，如果不调整会乱码 
	json=res;
	in.close(); 
	}catch(Exception e){ 
	e.printStackTrace(); 
	} 
	return res;
}
	
//	
//	public ArrayList<Contory> getContoryList(){
//		ArrayList<Contory> contories=new ArrayList<>();
//		Contory contory0=new Contory("中国","86");
//		Contory contory1=new Contory("台湾","886");
//		Contory contory2=new Contory("中国香港","852");
//		Contory contory3=new Contory("Brazil","55");
//		Contory contory4=new Contory("India","86");
//		Contory contory5=new Contory("Indonesia","62");
//		Contory contory6=new Contory("Malaysia","60");
//		Contory contory7=new Contory("Afghanistan","93");
//		Contory contory8=new Contory("Albania","355");
//		Contory contory9=new Contory("Algeria","213");
//		Contory contory10=new Contory("Andorra","376");
//		Contory contory11=new Contory("Angola","244");
//		Contory contor12y=new Contory("Anguilla","1264");
//		Contory contory13=new Contory("Antigua and Barbuda","1368");
//		Contory contory14=new Contory("Argentina","54");
//		Contory contory15=new Contory("Armenia","374");
//		Contory contory16=new Contory("Aruba","297");
//		Contory contory17=new Contory("Australia","61");
//		Contory contory18=new Contory("Austria","43");
//		Contory contory19=new Contory("Azerbaijan","994");
//		Contory contory20=new Contory("Bahamas","1242");
//		Contory contory21=new Contory("Bahrain","973");
//		Contory contory22=new Contory("Bangladesh","880");
//		Contory contory23=new Contory("Barbados","1246");
//		Contory contory24=new Contory("Belarus","375");
//		Contory contory25=new Contory("Belgium","32");
//		Contory contory26=new Contory("Belize","501");
//		Contory contory27=new Contory("Benin","229");
//		Contory contory28=new Contory("Bermuda","1441");
//		Contory contory29=new Contory("Bhutan","975");
//		Contory contory30=new Contory("Bolivia","591");
//		Contory contory31=new Contory("Bosnia and Herzegovina","387");
//		Contory contory32=new Contory("Botswana","267");
//		Contory contory33=new Contory("Brunei","673");
//		Contory contory34=new Contory("Bulgaria","359");
//		Contory contory35=new Contory("Burkina Faso","226");
//		Contory contory36=new Contory("Burundi","257");
//		Contory contory37=new Contory("Cambodia","855");
//		Contory contory38=new Contory("Cameroon","237");
//		Contory contory39=new Contory("Canada","1");
//		Contory contory40=new Contory("Cape Verde","238");
//		Contory contory41=new Contory("Cayman Islands","1345");
//		Contory contory42=new Contory("Central African Republic","236");
//		Contory contory43=new Contory("Chad","235");
//		Contory contory44=new Contory("Chile","56");
//		Contory contory45=new Contory("Colombia","57");
//		Contory contory46=new Contory("Comoros","269");
//		Contory contory47=new Contory("Cook Islands","682");
//		Contory contory48=new Contory("Costa Rica","506");
//		Contory contory49=new Contory("Cuba","385");
//		Contory contory50=new Contory("Croatia","53");
//		Contory contory51=new Contory("Cyprus","357");
//		Contory contory52=new Contory("Czech Republic","420");
//		Contory contory53=new Contory("Democratic Republic of the Congo","243");
//		Contory contory54=new Contory("Denmark","45");
//		Contory contory55=new Contory("Djibouti","253");
//		Contory contory56=new Contory("Dominica","1767");
//		Contory contory57=new Contory("Dominican Republic","1809");
//		Contory contory58=new Contory("East Timor","670");
//		Contory contory59=new Contory("Ecuador","593");
//		Contory contory60=new Contory("Egypt","20");
//		Contory contory61=new Contory("El Salvador","503");
//		Contory contory62=new Contory("Equatorial Guinea","240");
//		Contory contory63=new Contory("Estonia","372");
//		Contory contory64=new Contory("Ethiopia","251");
//		Contory contory65=new Contory("Faroe Islands","298");
//		Contory contory66=new Contory("Fiji","679");
//		Contory contory67=new Contory("Finland","358");
//		Contory contory68=new Contory("France","33");
//		Contory contory69=new Contory("French Guiana","594");
//		Contory contory70=new Contory("French Polynesia","689");
//		Contory contory71=new Contory("Gabon","241");
//		Contory contory72=new Contory("Gambia","220");
//		Contory contory73=new Contory("Georgia","995");
//		Contory contory74=new Contory("Germany","49");
//		Contory contory75=new Contory("Ghana","233");
//		Contory contory76=new Contory("Gibraltar","350");
//		Contory contory77=new Contory("Greece","30");
//		Contory contory78=new Contory("Greenland","299");
//		Contory contory79=new Contory("Grenada","1473");
//		Contory contory80=new Contory("Guadeloupe","590");
//		Contory contory81=new Contory("Guam","1671");
//		Contory contory82=new Contory("Guatemala","502");
//		Contory contory83=new Contory("Guinea","224");
//		Contory contory84=new Contory("Guinea-Bissau","245");
//		Contory contory85=new Contory("Guyana","592");
//		Contory contory86=new Contory("Haiti","509");
//		Contory contory87=new Contory("Honduras","504");
//		Contory contory88=new Contory("Hungary","36");
//		Contory contory89=new Contory("Iceland","354");
//		Contory contory90=new Contory("Iran","98");
//		Contory contory91=new Contory("Iraq","964");
//		Contory contory92=new Contory("Ireland","353");
//		Contory contory93=new Contory("Israel","972");
//		Contory contory94=new Contory("Italy","39");
//		Contory contory95=new Contory("Ivory Coast","225");
//		Contory contory96=new Contory("Jamaica","1876");
//		Contory contory97=new Contory("Japan","81");
//		Contory contory98=new Contory("Jordan","962");
//		Contory contory99=new Contory("Kazakhstan","7");
//		Contory contory100=new Contory("Kenya","254");
//		Contory contory101=new Contory("Kiribati","686");
//		Contory contory102=new Contory("Kuwait","965");
//		Contory contory103=new Contory("Kyrgyzstan","996");
//		Contory contory104=new Contory("Laos","856");
//		Contory contory105=new Contory("Latvia","371");
//		Contory contory106=new Contory("Lebanon","961");
//		Contory contory107=new Contory("Lesotho","266");
//		Contory contory108=new Contory("Liberia","231");
//		Contory contory109=new Contory("Libya","218");
//		Contory contory110=new Contory("Liechtenstein","423");
//		Contory contory111=new Contory("Lithuania","370");
//		Contory contory112=new Contory("Luxembourg","352");
//		Contory contory113=new Contory("Macau","853");
//		Contory contory114=new Contory("Macedonia","253");
//		Contory contory115=new Contory("Djibouti","253");
//		Contory contory116=new Contory("Djibouti","253");
//	}
}
