package news.ssp.cutword;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 */
public class ikAnalyzer {
    public static void main(String[] args) throws IOException {

        File file = new File("D:\\�Ѻ�\\2018\\09\\22\\����������׷���׷������ Ī��Ϊ׷��ʿ��ƴ��.txt");
        InputStreamReader reader=new InputStreamReader(new FileInputStream(file),"GBK");        //??????????
        Analyzer anal2=new org.wltea.analyzer.lucene.IKAnalyzer(true);
        //�ִ�
        TokenStream ts=anal2.tokenStream("", reader);
        CharTermAttribute term=ts.getAttribute(CharTermAttribute.class);
        //�����ִ�����
        while(ts.incrementToken()){
            System.out.print(term.toString()+"|");
        }
        reader.close();
        System.out.println();
    }
}
