package news.ssp.cutword;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by xufeng on 2015/12/9.
 */
public class StandardAnalyze {
    public static void main(String[] args) {
        try {
            File file = new File("D:\\搜狐\\2018\\09\\22\\传火箭或两套方案追巴特勒 莫雷为追勇士真拼了.txt");
            InputStreamReader reader=new InputStreamReader(new FileInputStream(file),"GBK");
            Analyzer a = new StandardAnalyzer(Version.LUCENE_36);
            TokenStream ts = a.tokenStream("", reader);
            //分词
            CharTermAttribute term=ts.getAttribute(CharTermAttribute.class);
            //遍历分词数据
            while(ts.incrementToken()){
                System.out.print(term.toString() + "|");
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
