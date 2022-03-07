package com.smart.kit;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class Evaluator {
	
	static List<Integer> tScore = new ArrayList(); //3
	static List<Integer> hScore = new ArrayList(); //1
	static List<Integer> lScore = new ArrayList(); //2
	static List<Integer> wScore = new ArrayList(); //3
	static List<Integer> pScore = new ArrayList(); //1

	static int eTC;
	static int eHC;
	static int eLC;
	static int eWC;
	static int ePC;

	private int ranTemp;
	private int ranHum;
	private int ranLight;
	private int ranWater;
	private int ranPes;

	private int pTemp = 25;
	private int pHum = 60;
	private int pLight = 10;
	private int pWater = 200;
	private int pPes = 50;

	private int ai;

	double ranV;

	public int random(int l, int m) {
		ranV = Math.random();
		return (int) ((ranV * (m - l)) + l);
	}

	public String evaluateTemp(int temp) {

//		temp = 20;

		ranTemp = random(10, 30);

		System.out.println(ranTemp);

		eTC = eTC + 1;

		tScore.add(100 - Math.abs((pTemp - temp) * (ranTemp - ai) / 10));

		System.out.println(tScore.toString());
		return "evaluated";
	}

	public String evaluateHum(int hum) {

//		hum = 70;

		ranHum = random(10, 25);

		System.out.println(ranHum);

		eHC = eHC + 1;

		hScore.add(100 - Math.abs((pHum - hum) * (ranHum - ai) / 10));

		System.out.println(hScore.toString());
		return "evaluated";
	}

	public String evaluateLight(int light) {

//		light = 12;

		ranLight = random(5, 20);

		System.out.println(ranLight);

		eLC = eLC + 1;

		lScore.add(100 - Math.abs((pLight - light) * (ranLight - ai) / 10));

		System.out.println(lScore.toString());
		return "evaluated";
	}

	public String evaluateWater(int water) {

//		water = 100;

		ranWater = random(5, 30);

		System.out.println(ranWater);

		eWC = eWC + 1;

		wScore.add(100 - Math.abs((pWater - water) * (ranWater - ai) / 10));

		System.out.println(wScore.toString());
		return "evaluated";
	}

	public String evaluatePes(int pes) {

//		pes = 10;

		ranPes = random(5, 10);

		System.out.println(ranPes);

		ePC = ePC + 1;

		pScore.add(100 - Math.abs((pPes - pes) * (ranPes - ai) / 10));

		System.out.println(pScore.toString());
		return "evaluated";
	}
	
	public String evaluateFinal() {
		
		int sumT = 0;
		int sumH = 0;
		int sumL = 0;
		int sumW = 0;
		int sumP = 0;
		
		for(int num : tScore) {
			sumT += num;
		}
		for(int num : hScore) {
			sumH += num;
		}
		for(int num : lScore) {
			sumL += num;
		}
		for(int num : wScore) {
			sumW += num;
		}
		for(int num : pScore) {
			sumP += num;
		}
		
		double aT = sumT/tScore.size();
		double aH = sumH/hScore.size();
		double aL = sumL/lScore.size();
		double aW = sumW/wScore.size();
		double aP = sumP/pScore.size();
		
		double finalScore = (aT*3+aH*1+aL*2+aW*3+aP*1)/10;
		System.out.println(finalScore);
		
		return "evaluated";
	}
	
	public String evaluateAll(int t, int h, int l, int w, int p) {
		
		evaluateTemp(t);
		evaluateHum(h);
		evaluateLight(l);
		evaluateWater(w);
		evaluatePes(p);
		
		return "무작위 영향인자 발생";
	}

}
