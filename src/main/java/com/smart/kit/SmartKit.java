package com.smart.kit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class SmartKit {
	
	@Autowired
	Logger logger;
	
	@Autowired
	Evaluator eva;
	
	private int status = 0;
	static Date startDate;

	private int temp;
	private int hum;
	private int light;
	private int water;
	private int pes;
	private int auto = 0;
	
	private int currentTemp;
	private int currentHum;
	
	private String lightStatus;
	private String latestWaterTime;
	private String latestPesTime;
	
	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
	static SimpleDateFormat formatCal = new SimpleDateFormat("HH");
	
//	static String DEVICE_ID = "2";
	//키트넘버, 작물, 필요시간, 작물유형 최조실행시 업데이트되도록 수정할예정
	static int kitNumber = 1;
	static String plant = "딸기";
	static int requiredDate = 14;
	static String plantClass = "과일";
	static int plantCount = 5;
	
	static String currentDay;
	static Date currentTime;

	private Timer m_timer;
	int tstatus = 0;
	

	List<Integer> dat = new ArrayList<>(40);

//	싱글톤 인스턴스
//	private static final SmartKit instance = new SmartKit();
//	인스턴스호출
//	public static SmartKit getInstance() {
//		return instance;
//	}
//	싱글톤 생성자
//	private SmartKit() {
//	}
	
	SmartKit() {
		for(int i=0; i<40; i++) {
			dat.add(0);
		}
	}

//	재배시작
	public String startGrow(int temp, int hum, int light, int water, int pes, int auto) {
		
		if(status==0) {
			startDate = new Date();

			List<String> log = new ArrayList<>();
			
//			List<String> status = new ArrayList<>();
//			status.add(Logger.DEVICE_ID);
//			status.add(format.format(startDate));
//			logger.dbWrite(status);
			this.temp = temp;
			this.hum = hum;
			this.light = light;
			this.water = water;
			this.pes = pes;
			this.auto = auto;
			
			this.status = 1;
			
			if(this.auto == 1) {
				log.add(format.format(startDate)+"자동재배 시작");
			}else if(this.auto == 0) {
				log.add(format.format(startDate)+"재배 시작 설정값(온도:"+temp+"습도:"+hum+"일사량:"+light+"급액량:"+water+"농약량:"+pes+")");
			}
			
			m_timer = new Timer();

			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			cal.add(Calendar.SECOND, requiredDate);
			
			String endDay = format.format(cal.getTime());
			
			
			TimerTask m_task = new TimerTask() {
				
				@Override
				public void run() {
					dat.remove(0);
					dat.add(currentTemp);
					System.out.println(dat.size());
					if (endDay.equals(currentDay)) {
						m_timer.cancel();
						
						SmartKit.this.status = 2;
						
					} else {
						
						currentTime = new Date();
						currentDay = format.format(currentTime);
						System.out.println(endDay);
						System.out.println(currentDay);
						
						if(SmartKit.this.temp != currentTemp && tstatus == 0) {
							log.add(currentDay + "온도 변화 감지 " + currentTemp);
							log.add(currentDay + thermoStat());
//							log.add(currentDay + "현재 온도 " + currentTemp);
							log.add(currentDay + "온도 영향인자 발생");
							eva.evaluateTemp(SmartKit.this.temp);
						}
						if(tstatus == 1) {
							thermoStat();
						}
						if(SmartKit.this.hum != currentHum) {
							log.add(currentDay + "습도 변화 감지 " + currentHum);
							log.add(currentDay + humController());
//							log.add(currentDay + "현재 습도 " + currentHum);
							eva.evaluateHum(SmartKit.this.hum);
						}
						if(formatCal.format(currentTime).equals("00")) {
							
							log.add(currentDay + lightController(1));
							System.out.println(waterController());
							System.out.println(pesController());
						}
						if(formatCal.format(currentTime).equals(Integer.toString(light))) {
							
							log.add(currentDay + lightController(0));
						}
					}
				}
			};
			
			TimerTask m_task2 = new TimerTask() {
				
				@Override
				public void run() {
					
					if(SmartKit.this.auto == 1) {
						eva.evaluateAll(SmartKit.this.temp, SmartKit.this.hum, SmartKit.this.light, SmartKit.this.water, SmartKit.this.pes);
						System.out.println("on auto growing");
					}else if(SmartKit.this.auto == 0) {
						currentTime = new Date();
						
						log.add(format.format(currentTime) + eva.evaluateAll(SmartKit.this.temp, SmartKit.this.hum, SmartKit.this.light, SmartKit.this.water, SmartKit.this.pes)); 				
						
						logger.write(log);
						
						log.clear();
						System.out.println("sent log");
					}
				}
			};
			
			m_timer.schedule(m_task, 0, 1000);
			
			m_timer.schedule(m_task2, 0, 10000);
			
			return "Grow Started";
		} else if(status==1) {
			
			return "Already Started";
		} else if(status==2) {
			
			return "Grow Completed";
		} else {
			
			return "Error";
		}
		
	}
	
//	재배중지
	public String stopGrow() {
		if(status == 0) {
			return "Already Stopped";
		}else if(status == 2) {
			status = 0;
			startDate = null;
			temp = 0;
			hum = 0;
			light = 0;
			water = 0;
			pes = 0;
			lightStatus = "Off";
			latestWaterTime = null;
			latestPesTime = null;
			
			return "Canceled";
		}else if(status == 1) {
			status = 0;
			startDate = null;
			temp = 0;
			hum = 0;
			light = 0;
			water = 0;
			pes = 0;
			lightStatus = "Off";
			latestWaterTime = null;
			latestPesTime = null;
			
			m_timer.cancel();
			
			return "Stop Grow";
		}else {
			return "Error";
		}
	}
	
//	재배완료
	public String completeGrow() {
		if(status == 2) {
			
			List<String> db = new ArrayList<>();
			int score = (int) Math.round(eva.evaluateFinal());
			db.add(Integer.toString(score));
			if(score <= 100 && score > 95) {
				db.add("최상");
			}else if(score <= 95 && score > 80) {
				db.add("상");
			}else if(score <= 80 && score > 50) {
				db.add("중");
			}else if(score <= 50 && score >= 0) {
				db.add("하");
			}else {
				System.out.println("점수오류");
				return "error";
			}
			db.add(plant);
			db.add(format.format(startDate));
			db.add(Integer.toString(kitNumber));
			db.add(Logger.DEVICE_ID);
			db.add(plantClass);
			db.add(Integer.toString(plantCount));
			
			if(auto == 1) {
				status = 0;
				startDate = null;
				temp = 0;
				hum = 0;
				light = 0;
				water = 0;
				pes = 0;
				auto = 0;
				lightStatus = "Off";
				latestWaterTime = null;
				latestPesTime = null;
				
				return logger.plantWrite(db);
			} else if (auto == 0) {

				status = 0;
				startDate = null;
				temp = 0;
				hum = 0;
				light = 0;
				water = 0;
				pes = 0;
				lightStatus = "Off";
				latestWaterTime = null;
				latestPesTime = null;
				
				return logger.diaryWrite(db);
			} else {
				return "Error";
			}
		}else {
			return "Error";
		}
	}
	
	
//	재배값 변경
	public String changeValue(int temp, int hum, int light, int water, int pes, int auto) {
		if(status == 0) {
			return "Start Grow First";
		}else if(status == 1 &this.temp==temp&this.hum==hum&this.light==light&this.water==water&this.pes==pes) {
			return "Same value as the current";
		}else if(status == 2) {
			return "Grow Completed";
		}else if(status == 1) {
			this.temp = temp;
			this.hum = hum;
			this.light = light;
			this.water = water;
			this.pes = pes;
			this.auto = auto;
			return "Grow Value Changed";
		}else {
			return "Error";
		}
	}
//	온도조절
	public String thermoStat() {
		if(temp>currentTemp) {
			tstatus = 1;
			currentTemp++;
		}else if(temp<currentTemp) {
			tstatus = 1;
			currentTemp--;
		}else if(temp==currentTemp) {
			tstatus = 0;
		}
		return "Operate ThermoStat";
	}
//	습도조절
	public String humController() {
		
		currentHum = hum;
		return "Operate All-In-One Humidifier&Dehumidifier";
	}
//	채광기
	public String lightController(int a) {
		
		if (a == 0) {
			lightStatus = "Off";
		}else if (a == 1) {
			lightStatus = "On";
		}else {
			return "error";
		}
		return "Light is" + lightStatus;
	}
//	자동급수
	public String waterController() {
		
		latestWaterTime = new Date().toGMTString();
		
		return water + "mL Watered at" + latestWaterTime;
	}
//	자동농약
	public String pesController() {
		
		latestPesTime = new Date().toGMTString();
		
		return pes + "Sprayed at" + latestPesTime;
	}
//	환경변화테스트
	public void changeEnv(int t, int h) {
		currentTemp = t;
		currentHum = h;
	}
	
}
