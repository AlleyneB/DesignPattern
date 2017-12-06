package package1;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
public class FindGeneCode {
	public static void main(String[] args){
		String[] chosedGeneHead=new String[] {"GAG","AAA","AAT"};
		File seqsFile=new File("C:\\Users\\Alleyne\\Documents\\javaStudy\\R.txt");
		Map<String, String> seqsMap=getSeqsMap(seqsFile,chosedGeneHead);
		/*for(Map.Entry<String,String> entry:seqsMap.entrySet()) {
			System.out.println(entry.getKey()+"="+entry.getValue());
		}
		System.out.println(seqsMap);*/
		File codesFile=new File("C:\\Users\\Alleyne\\Documents\\javaStudy\\T.txt");
		Map<String, String> codesMap=getCodesMap(codesFile);
		File[] subFiles=new File("C:\\Users\\Alleyne\\Documents\\javaStudy\\pta").listFiles();
		String str=null;
		for(File geneFile:subFiles) {
			str=findGeneCode(geneFile,seqsMap);
			if((str!=null)&&str.length()>0) {
				System.out.println(geneFile.getName());
				System.out.println("---R匹配结果---"+"\r\n"+str);
				System.out.println("---T匹配结果---"+"\r\n"+findTCode(codesMap, str));
			}
		}
	}
	/*返回基因序列和编号的map*/
	static Map<String,String> getSeqsMap(File seqsFile,String[] heads){
		Map<String,String> rMap=new LinkedHashMap<>();
		try(BufferedReader bfr=new BufferedReader(new FileReader(seqsFile))){
			String str1=null;
			String str2=null;
			while((str1=bfr.readLine())!=null) {
				str2=bfr.readLine();		
				for(String head:heads) {
					if(str2.substring(0, 3).equals(head)) {//符合开头字符的才添加到map中
						rMap.put(str1.substring(2),str2);//第一行截取编号，第二行是序列
						break;
					}
				}
				bfr.readLine();//略去空白行
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rMap;
	}
	/*返回基因文件包含的基因序列的编码*/
	static String findGeneCode(File geneFile,Map<String, String> seqsMap){
		StringBuilder geneCodeSb=new StringBuilder();
		StringBuilder geneSb=new StringBuilder();
		String result=null;
		try(BufferedReader br=new BufferedReader(new FileReader(geneFile))){
			String str = null;
			
			while((str=br.readLine())!=null) geneSb.append(str);//将基因文件读入内存
			
		/*从基因文件开头往后匹配，匹配到序列，则删除基因文件开头匹配到的序列，未匹配到，则删除文件第一个字符，继续匹配循环*/	
			
			while(geneSb.length()>0) {
				boolean flag=false;//匹配成功标志
				for(Entry entry:seqsMap.entrySet()) {
					String k=(String) entry.getKey();
					String v=(String) entry.getValue();
					if(geneSb.length()>=v.length()&&v.equals(geneSb.substring(0, v.length()))) {
						geneSb.delete(0, v.length());
						geneCodeSb.append(k+"-");
						flag=true;
						break;
					}
					else {
						flag=false;
					}
				}
				if(!flag)geneSb.deleteCharAt(0);

			}

			result=geneCodeSb.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result=geneCodeSb.toString();
		if((result==null)||result.length()<=0) {
			return null;
		}
		else {
			return result.substring(0, result.length()-1);
		}
	}
	/*返回T文件标号与序列号的map*/
	static Map<String, String> getCodesMap(File codesFile){
		Map<String,String> tMap=new LinkedHashMap<>();
		try(BufferedReader bfr=new BufferedReader(new FileReader(codesFile))){
			String str=new String();
			String[] strs=new String[2];
			while((str=bfr.readLine())!=null) {
				strs=str.split(",");
				if(strs.length<2)continue;//跳过特殊行NT
				tMap.put(strs[0],strs[1]);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tMap;
	}
	/*查找对应T码的标号*/
	static String findTCode(Map<String, String> codesMap,String geneCode) {
		StringBuilder tCodeSb = new StringBuilder();
		codesMap.forEach((k,v)->{
			if(geneCode.equals(v)) {
				tCodeSb.append(k);
			}
		});
		if(tCodeSb.length()<=0)return null;
		else return tCodeSb.toString();
	}
}
 