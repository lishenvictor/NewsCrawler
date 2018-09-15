package news.ssp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ���ڹ�����
 * @author user
 *
 */
public class DateUtil {

	/**
	 * ��ȡ��ǰ������·��
	 * @return
	 * @throws Exception
	 */
	public static String getCurrentDatePath(){
		try {
			Date date=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
			return sdf.format(date);
		}catch (Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(getCurrentDatePath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
