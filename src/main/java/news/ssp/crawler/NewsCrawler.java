package news.ssp.crawler;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Status;
import news.ssp.util.DateUtil;
import news.ssp.util.DbUtil;
import news.ssp.util.PropertiesUtil;

public class NewsCrawler {

	private static Logger logger = Logger.getLogger(NewsCrawler.class);

	private static final String URL =PropertiesUtil.getValue("Url");

	private static Connection con = null;

	private static CacheManager manager = null; // cache管理器
	private static Cache cache = null; // cache缓存对象

	/**
	 * 解析主页
	 */
	private static void parseHomePage() {
//		while (true) {
			logger.info("开始爬取" + URL + "网页");
			System.setProperty(net.sf.ehcache.CacheManager.ENABLE_SHUTDOWN_HOOK_PROPERTY, "true");
			CacheManager cm = new CacheManager(PropertiesUtil.getValue("cacheFilePath"));
			manager = CacheManager.getCacheManager(cm.getName());
			if (manager == null) {
				manager = CacheManager.create();
			}
			cache = manager.getCache("news");
			cache.flush();
			CloseableHttpClient httpClient = HttpClients.createDefault(); // 获取HttpClient实例
			HttpGet httpget = new HttpGet(URL); // 创建httpget实例
			RequestConfig config = RequestConfig.custom().setSocketTimeout(100000) // 设置读取超时时间
					.setConnectTimeout(5000) // 设置连接超时时间
					.build();
			httpget.setConfig(config);
			CloseableHttpResponse response = null;
			try {
				response = httpClient.execute(httpget);
			} catch (ClientProtocolException e) {
				logger.error(URL + "-ClientProtocolException", e);
			} catch (IOException e) {
				logger.error(URL + "-IOException", e);
			}
			if (response != null) {
				HttpEntity entity = response.getEntity(); // 获取返回实体
				// 判断返回状态是否为200
				if (response.getStatusLine().getStatusCode() == 200) {
					String webPageContent = null;
					try {
						webPageContent = EntityUtils.toString(entity, "utf-8");
						parseHomeWebPage(webPageContent);
					} catch (ParseException e) {
						logger.error(URL + "-ParseException", e);
					} catch (IOException e) {
						logger.error(URL + "-IOException", e);
					}
				} else {
					logger.error(URL + "-返回状态非200");
				}
			} else {
				logger.error(URL + "-连接超时");
			}
			try {
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (Exception e) {
				logger.error(URL + "Exception", e);
			}
			if (cache.getStatus() == Status.STATUS_ALIVE) {
				cache.flush(); // 把缓存写入文件
			}
			manager.shutdown();
//			try {
//				Thread.sleep(1 * 60 * 1000); // 每隔10分钟抓取一次网页数据
//			} catch (InterruptedException e) {
//				logger.error("InterruptedException", e);
//			}
			logger.info("结束爬取" + URL + "网页");
		}
//	}

	/**
	 * 解析首页内容 提取新闻link
	 * 
	 * @param webPageContent
	 */
	private static void parseHomeWebPage(String webPageContent) {
		if ("".equals(webPageContent)) {
			return;
		}
		Document doc = Jsoup.parse(webPageContent);
		Elements links = doc.select(PropertiesUtil.getValue("link"));
		for (int i = 0; i < links.size(); i++) {
			Element link = links.get(i);
			String url = link.attr("href");
			if(URL.contains("http://news.sohu.com/")){
				if(url != null && url != ""){
					if(!url.contains("http:") && !url.contains("https:")){
						url = "http:" + url;
					}
				}
			}
			System.out.println(url);
			cache.flush();
			if (cache.get(url) != null) { // 如果缓存中存在就不插入
				logger.info(url + "-缓存中存在");
				continue;
			}
			parseBlogLink(url);
		}

	}

	/**
	 * 解析新闻链接地址 获取新闻内容
	 * 
	 * @param link
	 */
	private static void parseBlogLink(String link) {
		logger.info("开始爬取" + link + "网页");
		CloseableHttpClient httpClient = HttpClients.createDefault(); // 获取HttpClient实例
		HttpGet httpget = new HttpGet(link); // 创建httpget实例
		RequestConfig config = RequestConfig.custom().setSocketTimeout(100000) // 设置读取超时时间
				.setConnectTimeout(5000) // 设置连接超时时间
				.build();
		httpget.setConfig(config);
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpget);
		} catch (ClientProtocolException e) {
			logger.error(URL + "-ClientProtocolException", e);
		} catch (IOException e) {
			logger.error(URL + "-IOException", e);
		}
		if (response != null) {
			HttpEntity entity = response.getEntity(); // 获取返回实体
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				String blogContent = null;
				try {
					blogContent = EntityUtils.toString(entity, "utf-8");
					parseBlogPage(blogContent, link);
				} catch (ParseException e) {
					logger.error(URL + "-ParseException", e);
				} catch (IOException e) {
					logger.error(URL + "-IOException", e);
				}
			} else {
				logger.error(URL + "-返回状态非200");
			}
		} else {
			logger.error(URL + "-连接超时");
		}
		try {
			if (response != null) {
				response.close();
			}
			if (httpClient != null) {
				httpClient.close();
			}
		} catch (Exception e) {
			logger.error(URL + "Exception", e);
		}
		logger.info("结束爬取" + link + "网页");
	}

	/**
	 * 解析新闻内容，提取有效信息
	 * 
	 * @param blogContent
	 * @param link
	 */
	private static void parseBlogPage(String blogContent, String link) {
		if ("".equals(blogContent)) {
			return;
		}
		Document doc = Jsoup.parse(blogContent);
		Elements titleElements = doc.select(PropertiesUtil.getValue("title")); // 获取新闻标题
		if (titleElements.size() == 0) {
			logger.error(link + "-未获取到新闻标题");
			return;
		}
		String title = titleElements.get(0).text();
		System.out.println("新闻标题：" + title);

		Elements contentElements = doc.select(PropertiesUtil.getValue("content")); // 获取新闻内容
		Elements imgElements = contentElements.select("img"); // 获取所有图片元素
		if (contentElements.size() == 0) {
			logger.error(link + "-未获取到新闻内容");
			return;
		}
		String content = contentElements.get(0).html();
		System.out.println("新闻内容：" + content);

		List<String> imgUrlList = new LinkedList<String>();
		for (int i = 0; i < imgElements.size(); i++) {
			Element imgEle = imgElements.get(i);
			String url = imgEle.attr("src");
			imgUrlList.add(url);
			System.out.println(url);
		}

//		if (imgUrlList.size() > 0) {
//			Map<String, String> replaceImgMap = downLoadImages(imgUrlList);
//			String newContent = replaceWebPageImages(content, replaceImgMap);
//			content = newContent;
//		}
			try {
				String currentDatePath = DateUtil.getCurrentDatePath();
				InputStream inputStream = new ByteArrayInputStream(getTextFromHtml(content).getBytes());
				FileUtils.copyToFile(inputStream, new File(
						PropertiesUtil.getValue("filePath") + currentDatePath + "/" + title.trim().replace(":","-")
								.replace("?"," ")+ ".txt"));
			}catch (Exception e){
				e.printStackTrace();;
			}



		// 插入数据库
		String sql = "insert into t_article values(null,?,?,null,now(),0,0,null,?,0,null,?,?)";
		try {
			String source=PropertiesUtil.getValue("source");
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			pstmt.setString(3, link);
			pstmt.setString(4,source);
			pstmt.setString(5,getTextFromHtml(content));
			if (pstmt.executeUpdate() == 1) {
				logger.info(link + "-成功插入数据库");
				cache.put(new net.sf.ehcache.Element(link, link));
				cache.flush();
				logger.info(link + "-已加入缓存");
			} else {
				logger.info(link + "-插入数据库失败");
			}
		} catch (SQLException e) {
			logger.error("SQLException", e);
		}
	}

//	/**
//	 * 把原来的网页图片地址换成本地新的
//	 *
//	 * @param content
//	 * @param replaceImgMap
//	 * @return
//	 */
//	private static String replaceWebPageImages(String content, Map<String, String> replaceImgMap) {
//		for (String url : replaceImgMap.keySet()) {
//			String newPath = replaceImgMap.get(url);
//			content = content.replace(url, newPath);
//		}
//		return content;
//	}

//	/**
//	 * 下载图片到本地
//	 *
//	 * @param imgUrlList
//	 * @return
//	 */
//	private static Map<String, String> downLoadImages(List<String> imgUrlList) {
//		Map<String, String> replaceImgMap = new HashMap<String, String>();
//
//		RequestConfig config = RequestConfig.custom().setSocketTimeout(10000) // 设置读取超时时间
//				.setConnectTimeout(5000) // 设置连接超时时间
//				.build();
//		CloseableHttpClient httpClient = HttpClients.createDefault(); // 获取HttpClient实例
//		for (int i = 0; i < imgUrlList.size(); i++) {
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			String url = imgUrlList.get(i);
//			logger.info("开始爬取" + url + "图片");
//
//			CloseableHttpResponse response = null;
//
//			try {
//				HttpGet httpget = new HttpGet(url); // 创建httpget实例
//				httpget.setConfig(config);
//				response = httpClient.execute(httpget);
//			} catch (ClientProtocolException e) {
//				logger.error(url + "-ClientProtocolException");
//			} catch (IOException e) {
//				logger.error(url + "-IOException");
//			}
//			if (response != null) {
//				HttpEntity entity = response.getEntity(); // 获取返回实体
//				// 判断返回状态是否为200
//				if (response.getStatusLine().getStatusCode() == 200) {
//					try {
//						InputStream inputStream = entity.getContent();
//						String imageType = entity.getContentType().getValue();
//						String urlB = imageType.split("/")[1];
//						String uuid = UUID.randomUUID().toString();
//						String currentDatePath = DateUtil.getCurrentDatePath();
//						String newPath = PropertiesUtil.getValue("imagePath") + currentDatePath + "/" + uuid + "."
//								+ urlB;
//						FileUtils.copyToFile(inputStream, new File(
//								PropertiesUtil.getValue("imageFilePath") + currentDatePath + "/" + uuid + "." + urlB));
//						replaceImgMap.put(url, newPath);
//					} catch (UnsupportedOperationException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				} else {
//					logger.error("返回状态非200");
//				}
//			} else {
//				logger.error("连接超时");
//			}
//			try {
//				if (response != null) {
//					response.close();
//				}
//			} catch (Exception e) {
//				logger.error("Exception", e);
//			}
//			logger.info("结束爬取" + url + "图片");
//		}
//
//		return replaceImgMap;
//	}

	public static void start() {
		DbUtil dbUtil = new DbUtil();
		try {
			con = dbUtil.getCon();
		} catch (Exception e) {
			logger.error("创建数据库连接失败", e);
		}
		parseHomePage();
	}


    public static String delHtmlTags(String htmlStr) {
        //定义script的正则表达式，去除js可以防止注入
        String scriptRegex="<script[^>]*?>[\\s\\S]*?<\\/script>";
        //定义style的正则表达式，去除style样式，防止css代码过多时只截取到css样式代码
        String styleRegex="<style[^>]*?>[\\s\\S]*?<\\/style>";
        //定义HTML标签的正则表达式，去除标签，只提取文字内容
        String htmlRegex="<[^>]+>";
        //定义空格,回车,换行符,制表符
        String spaceRegex = "\\s*|\t|\r|\n";

        // 过滤script标签
        htmlStr = htmlStr.replaceAll(scriptRegex, "");
        // 过滤style标签
        htmlStr = htmlStr.replaceAll(styleRegex, "");
        // 过滤html标签
        htmlStr = htmlStr.replaceAll(htmlRegex, "");
        // 过滤空格等
        htmlStr = htmlStr.replaceAll(spaceRegex, "");
        return htmlStr.trim(); // 返回文本字符串
    }
    /**
     * 获取HTML代码里的内容
     * @param htmlStr
     * @return
     */
    public static String getTextFromHtml(String htmlStr){
		if(URL.contains("http://news.sohu.com/")) {
			if (htmlStr.contains("data-role=\"original-title\"")) {
				htmlStr = htmlStr.substring(htmlStr.indexOf("</p>") + 6);
			}
		}
        //去除html标签
        htmlStr = delHtmlTags(htmlStr);
        //去除空格" "
        htmlStr = htmlStr.replaceAll(" ","");
		htmlStr = htmlStr.replaceAll("返回搜狐，查看更多责任编辑：","");
		return htmlStr;
    }

    public static void main(String[] args) {
		start();
	}
}
