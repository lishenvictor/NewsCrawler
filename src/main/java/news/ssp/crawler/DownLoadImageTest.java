package news.ssp.crawler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import news.ssp.util.DateUtil;
import news.ssp.util.PropertiesUtil;

public class DownLoadImageTest {
	
	private static Logger logger=Logger.getLogger(DownLoadImageTest.class);
	
	private static final String link="http://images2015.cnblogs.com/blog/952033/201705/952033-20170511210141910-342481715.png";
	
	public static void main(String[] args) {
		logger.info("��ʼ��ȡ"+link+"ͼƬ");
		CloseableHttpClient httpClient=HttpClients.createDefault(); // ��ȡHttpClientʵ��
		HttpGet httpget=new HttpGet(link); // ����httpgetʵ��
		RequestConfig config=RequestConfig.custom().setSocketTimeout(10000) // ���ö�ȡ��ʱʱ��
				                                   .setConnectTimeout(5000)  // �������ӳ�ʱʱ��
				                                   .build();
		httpget.setConfig(config);
		CloseableHttpResponse response=null;
		try {
			response=httpClient.execute(httpget);
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException",e);
		} catch (IOException e) {
			logger.error("IOException",e);
		}
		if(response!=null){
			HttpEntity entity=response.getEntity(); // ��ȡ����ʵ��
			// �жϷ���״̬�Ƿ�Ϊ200
			if(response.getStatusLine().getStatusCode()==200){
				try {
					InputStream inputStream=entity.getContent();
					String imageType=entity.getContentType().getValue();
					String urlB=imageType.split("/")[1];
					String uuid=UUID.randomUUID().toString();
					FileUtils.copyToFile(inputStream, new File(PropertiesUtil.getValue("imageFilePath")+DateUtil.getCurrentDatePath()+"/"+uuid+"."+urlB));
				} catch (UnsupportedOperationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				logger.error("����״̬��200");
			}
		}else{
			logger.error("���ӳ�ʱ");
		}
		try{
			if(response!=null){
				response.close();
			}
			if(httpClient!=null){
				httpClient.close();
			}
		}catch(Exception e){
			logger.error("Exception", e);
		}
		logger.info("������ȡ"+link+"ͼƬ");
	}
}
