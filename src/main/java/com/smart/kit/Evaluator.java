package com.smart.kit;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class Evaluator {
	
	static List<Integer> tScore = new ArrayList<Integer>(); //3
	static List<Integer> hScore = new ArrayList<Integer>(); //1
	static List<Integer> lScore = new ArrayList<Integer>(); //2
	static List<Integer> wScore = new ArrayList<Integer>(); //3
	static List<Integer> pScore = new ArrayList<Integer>(); //1

	private int ranTemp;
	private int ranHum;
	private int ranLight;
	private int ranWater;
	private int ranPes;

	private int pTemp = 25;
	private int pHum = 60;
	private int pLight = 10;
	private int pWater = 200;
	private int pPes = 20;

	private int ai;

	double ranV;
	
//	랜덤요인발생시 랜덤값발생기
	public int random(int l, int m) {
		ranV = Math.random();
		return (int) ((ranV * (m - l)) + l);
	}

//	온도점수
	public String evaluateTemp(int temp) {

		ranTemp = random(10, 40);

		System.out.println(ranTemp);

		tScore.add(100 - Math.abs((pTemp - temp) * (ranTemp - ai) / 10));

		System.out.println(tScore.toString());
		return "evaluated";
	}

//	습도점수
	public String evaluateHum(int hum) {

		ranHum = random(10, 20);

		System.out.println(ranHum);

		hScore.add(100 - Math.abs((pHum - hum) * (ranHum - ai) / 10));

		System.out.println(hScore.toString());
		return "evaluated";
	}

//	조명점수
	public String evaluateLight(int light) {

		ranLight = random(10, 90);

		System.out.println(ranLight);

		lScore.add(100 - Math.abs((pLight - light) * (ranLight - ai) / 10));
		
		System.out.println(lScore.toString());
		return "evaluated";
	}

//	급수점수
	public String evaluateWater(int water) {

		ranWater = random(1, 5);

		System.out.println(ranWater);

		wScore.add(100 - Math.abs((pWater - water) * (ranWater - ai) / 10));
		
		System.out.println(wScore.toString());
		return "evaluated";
	}

//	농약점수
	public String evaluatePes(int pes) {

		ranPes = random(1, 10);

		System.out.println(ranPes);

		pScore.add(100 - Math.abs((pPes - pes) * (ranPes - ai) / 10));
		
		System.out.println(pScore.toString());
		return "evaluated";
	}
	
//	모든인자 요인의 점수 한번에
	public String evaluateAll(int t, int h, int l, int w, int p) {
		
		evaluateTemp(t);
		evaluateHum(h);
		evaluateLight(l);
		evaluateWater(w);
		evaluatePes(p);

		return "무작위 영향인자 발생";
	}
	
//	최종 점수계산
	public double evaluateFinal() {
		
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
		
		return finalScore;
	}

}
