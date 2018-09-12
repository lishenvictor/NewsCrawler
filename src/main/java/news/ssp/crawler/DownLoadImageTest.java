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
		logger.info("开始爬取"+link+"图片");
		CloseableHttpClient httpClient=HttpClients.createDefault(); // 获取HttpClient实例
		HttpGet httpget=new HttpGet(link); // 创建httpget实例
		RequestConfig config=RequestConfig.custom().setSocketTimeout(10000) // 设置读取超时时间
				                                   .setConnectTimeout(5000)  // 设置连接超时时间
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
			HttpEntity entity=response.getEntity(); // 获取返回实体
			// 判断返回状态是否为200
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
				logger.error("返回状态非200");
			}
		}else{
			logger.error("连接超时");
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
		logger.info("结束爬取"+link+"图片");
	}
}
