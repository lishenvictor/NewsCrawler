package news.ssp.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.junit.Test;

import java.io.IOException;

public class LuenceFirst {

    // �鿴�������ķִ�Ч��
    @Test
    public void testAnanlyzer() throws IOException {
        // 1������һ������������
        Analyzer analyzer = new StandardAnalyzer(); // �ٷ��Ƽ��ı�׼������
        // 2���ӷ����������л��tokenStream����
        // ����1��������ƣ�����Ϊnull��������""
        // ����2��Ҫ�������ı�
        TokenStream tokenStream = analyzer.tokenStream("", "������򡰾ٱ��š������¶�̬��9��14�������Ͼ��������ν����������޹�˾���³ơ��Ͼ���������ʵ��������������΢�������¾Ž����������Ϸ��������ԡ�����ʾ����9��22�չ��һ���⿪ҵ������֮�ʴӻ������߲�׹����ɱ�����Ժ�ÿ���9��22�����ǵĿ�ҵ��佫Ҳ���ҵļ��գ����Ƕ���������ҡ�����Դ��£��������ʾ�Ѿ���������Ӧ�ƣ�����һֱ��Ŭ��ͨ�������ķ����ֶ���ά����ҵ�ĺϷ�Ȩ�����ҵ�����������Ǵ���û�У�Ҳ��ϣ���κ�һ���Լ��˷�ʽ�Դ����¡������ٱ��š�����粨�����������������ϵġ����顱��ʾ���Լ��ӹ��һ����ϸ�����Ľ�����Ӫ�������Ρ�ؤ���������ͻȻ�ͱ���ˡ�����Ұ�֡�������������λ��Ӣ�ۺ��ܡ������ַ���ð�䡱�����ڴ�ǰ����������ʾ�Ͼ������뻪����ǰ�ھ�˫������̸��С���꣬�ں����У����ڻ�������о�Ժ�������������Ͼ����������г���Ӫ���г����ء����������ţ���ʱ������Ӫ����ʵ�������Ѿ���������û�������Ͽɣ�˫����ʽǩԼ��2016��3��29�ա�ͬʱ���仹�ṩ�������ҵ��վ�Դ��µı����Լ�����ʼ�������֤����7��10�ջ�����CEO��Ѷ�ڽ��ܰ����϶�����ý��ɷ�ʱ��ʾ��������Ӫ�����ڳﱸ��������������ʵ�����ѣ�δ���������������⣬�ڡ����顱����������ʾ�������ݺ�ͬ�쾭�����ҷ�Ӧ������1749��-7741����û��Ҫ�������û��Ҫƽ������ֻ��������Ͷ�1749��ͬʱ��ŵ����֮�겻���Ừ������������ٽӴ����������ҵ���������Ͼ������뻪������ǰ�ĺ���������������ʾ������1��16�����յ�һ�ݻ���������ʼĹ����Ŀ�ݣ��ļ������ǻ������Ҫ������Ͼ��������εĺ�������Դ�ʮ�ֲ��⣬��ȷ��̬��ͬ�⻪������浥����ȡ���������˺����ͻ���������λ��Ա������5���µĹ�ͨ��˫��δ�ܴ��һ�¡����ڽ�Լһ�£�����������ʾ������1�£������Ͼ����������17��ϸ���洢������ռȫ��300��Ŀ���5.7%�����ڸ���ΥԼ��Ϊ���о�Ժ���͡���Լ֪ͨ������ʽ�����������һ���⼼�������ͬ����8��1�գ�����˫����Լһ�£������������ܲ�Τ쿱�ʾ��˫����Լ����ʵ���Ͼ�����ΥԼ���ȣ�����ֻ����ʹ�˷������Ȩ����Ŀǰ����������������Ͼ��������ε���Ȩ��Ϊ���ַ�����Ȩ�Ͳ��������������Ѿ������ں��Ͼ������������ϣ��Ͼ��й���������־�Ҳ�ѷ������溯������ע�����Ͼ������ν����������޹�˾���Ƶġ����һ����ϸ�����Ľ�����Ӫ���ġ��͡����һ����ϸ�����Ľ�����Ӫ���Ĳ���ר���¡���ö���¡�ͬʱ������������ٴλظ��ƣ��������ڻ�������о�Ժ���ָ���Ϊ�����ڻ���������ѧ�о�Ժ������ơ��о�Ժ�������Ͼ��������ν����������޹�˾����ơ��Ͼ���������ǩ���ġ����һ���⼼�������ͬ���������Ͼ��������ڸ���ΥԼ���о�Ժ����2018��1��������ʽ��Լ��2018��1�������Ͼ�������δ�ͽ�Լ��Ϊ�������κ�˾���������������ٲá�˫�����߰��������ڴӡ��ٱ��š����������Ͼ������뻪����չ���˳��������µ�����ս��˫��Ҳ��������˻������߶Է����϶������˽⵽����ǰ�Ͼ������߻�������ַ�����Ȩһ��ԭ����7��18�����翪ͥ����������������������ʾ���ð���ȷ���Ƴ���������������䣬�����淢���Ļظ����ʾ����ǰ�����ڻ������Ƽ����޹�˾����ơ������š�����������������������Ժ���Ͼ������������������ַ�����Ȩ֮�ߣ����ڻ���������ѧ�о�Ժ���Ͼ��н���������Ժ���Ͼ�����������������֮�ߣ�Ŀǰ�������򱻸淽ԭ�����ڿ�ͥ�������������Ͼ��������������ַ�����Ȩһ��������9��17�տ�ͥ���϶�����ע�⵽�����ٱ��š�һ�µ�������˫������Ԫ�����ˡ����ܽ��������������͵ġ��ٱ��š����������š������������۹ɽ�����ȷ���Ӱ�죬��ֹ9��14�����̣��������300676.sz����71.81Ԫ����ֵ287�ڣ����۷��ڵ�ǧ����ֵ�������ˮ�������䷢���İ��걨��ʾ�����ϰ���Ӫҵ����11.41��Ԫ��ͬ������28.44%�����������й�˾�ɶ��ľ�����2.08��Ԫ��ͬ������8.73%�����������й�˾�ɶ��Ŀ۳��Ǿ���������ľ�����Ϊ1.68��Ԫ���϶���������");

        // 3������һ������(�൱��ָ��)��������ÿ����Ƕ������ͣ������ǹؼ��ʵ����ã�ƫ���������õȵ�
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class); // charTermAttribute���������ǰ�Ĺؼ���
        // ƫ����(��ʵ���ǹؼ������ĵ��г��ֵ�λ�ã��õ����λ����ʲô���أ���Ϊ���ǽ�������Ҫ�Ըùؼ��ʽ��и�����ʾ�����и�����ʾҪ֪������ؼ������ģ�)
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
        // 4������tokenStream��reset�����������ø÷��������׳�һ���쳣
        tokenStream.reset();
        // 5��ʹ��whileѭ�������������б�
        while (tokenStream.incrementToken()) {
            System.out.println("start��" + offsetAttribute.startOffset()); // �ؼ�����ʼλ��
            // 6����ӡ����
            System.out.println(charTermAttribute);
            System.out.println("end��" + offsetAttribute.endOffset()); // �ؼ��ʽ���λ��
        }
        // 7���ر�tokenStream����
        tokenStream.close();
    }

}