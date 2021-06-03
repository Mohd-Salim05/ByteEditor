package controllers;

import java.util.ArrayList;
import java.util.List;

public class KMP {

	
	public static int[] getLPS(String pattern) {
		

		int pl=pattern.length();
		int lps[]=new int[pl];
		int k=0;
		lps[0]=-1;
		lps[1]=0;
		
		for(int i=2;i<pl;i++) {
			
			while(k>0 && pattern.charAt(k+1)!=pattern.charAt(i)) {	
				k=lps[k];
			}
			
			if(pattern.charAt(k+1)==pattern.charAt(i)) {
				k=k+1;
			}
			lps[i]=k;
			
		}
		
		return lps;
	}
	
	public static List<Integer> search(String text,String p) {

		List<Integer> pos=new ArrayList<Integer>();
		text="v"+text;
		int lps[]=getLPS(p);
		int tl=text.length();
		int i;
		int j=0;
		
		for(i=1;i<tl;i++) {
		
			while(text.charAt(i)!=p.charAt(j+1) && j>0) {
				j=lps[j];
			}
			
			if(text.charAt(i)==p.charAt(j+1)) {
				j=j+1;
			}
			if(j==(p.length()-1)) {
				pos.add(i-(p.length()-2));
				j=0;
				i=i-2;
			}
		}
		return pos;
	}
	

}
