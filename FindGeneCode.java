package package1;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class FindGeneCode {
	public static void main(String[] args){
		File seqsFile=new File("C:\\Users\\Alleyne\\Documents\\javaStudy\\R.txt");
		Map<String, String> seqsMap=getSeqsMap(seqsFile);
		File codesFile=new File("C:\\Users\\Alleyne\\Documents\\javaStudy\\T.txt");
		Map<String, String> codesMap=getCodesMap(codesFile);
		File[] subFiles=new File("C:\\Users\\Alleyne\\Documents\\javaStudy\\pta").listFiles();
		String str=null;
		for(File geneFile:subFiles) {
			str=findGeneCode(geneFile,seqsMap);
			if(str!=null) {
				System.out.println(geneFile.getName());
				System.out.println("---R匹配结果---"+"\r\n"+str);
				System.out.println("---T匹配结果---"+"\r\n"+findTCode(codesMap, str));
			}
		}
	}
	/*返回基因序列和编号的map*/
	static Map<String,String> getSeqsMap(File seqsFile){
		Map<String,String> rMap=new HashMap<>();
		try(BufferedReader bfr=new BufferedReader(new FileReader(seqsFile))){
			String str=new String();
			while((str=bfr.readLine())!=null) {
				rMap.put(str.substring(2),bfr.readLine());//第一行截取编号，第二行是序列
				bfr.readLine();
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
			while((str=br.readLine())!=null) {geneSb.append(str);}
			String gene=geneSb.toString();
			seqsMap.forEach((k,v)->{
				if(gene.indexOf(v)!=-1) {
					geneCodeSb.append(k+"-");
				}
			});
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result=geneCodeSb.toString();
		if(result.length()==0) {
			return null;
		}
		else {
			return result.substring(0, result.length()-1);
		}
	}
	/*返回T文件标号与序列号的map*/
	static Map<String, String> getCodesMap(File codesFile){
		Map<String,String> tMap=new HashMap<>();
		try(BufferedReader bfr=new BufferedReader(new FileReader(codesFile))){
			String str=new String();
			String[] strs=new String[2];
			while((str=bfr.readLine())!=null) {
				strs=str.split(",");
				if(strs.length<2)continue;//跳过特殊行NT
				tMap.put(strs[0],strs[1]);
				//System.out.println(strs[0]+"--"+strs[1]);
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
		if(tCodeSb.length()==0)return null;
		else return tCodeSb.toString();
	}
}
 