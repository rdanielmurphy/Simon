package com.commonsware.android.simon;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Random;

public class GameEngine {
	
	public final String RED = "0";
	public final String YELLOW = "1";
	public final String GREEN = "2";
	public final String BLUE = "3";
	
	Queue<String> User;
	Queue<String> CPU;
	Random r;
	
	public GameEngine(){
		User = new LinkedList<String>();
		CPU = new LinkedList<String>();
		r = new Random();
	}
	
	public Queue Round(int roundNumber){
		FillCPUQueue(roundNumber);
		
		return CPU;
	}
	
	public boolean AddClickToQueue(String sColor){
		User.add(sColor);
		
		return true;
	}
	
	private void FillCPUQueue(int numberOfClicks){
		//for(int i = 0; i<numberOfClicks; i++){
			String integer = Integer.toString(Math.abs(RandomColor()));
			CPU.add((integer));
		//}
	}
	
	private int RandomColor(){		
		return r.nextInt()%4;
	}
	
	public boolean DidUserClickTooMany(){
		if(CPU.size() < User.size())
			return true;
		else 
			return false;
	}
	
	public boolean IsItAMatch(){
		int uSize = User.size();
		int cSize = CPU.size();
		for(int i = 0; i < uSize; i++){
			String user= User.remove();
			String cpu = CPU.remove();
			if(user.charAt(0) != cpu.charAt(0))
				return false;
			else{
				User.add(user);
				CPU.add(cpu);
			}
		}
		//rebuild
		for(int j = 0; j < cSize - uSize; j++){
			String cpu = CPU.remove();
			CPU.add(cpu);
			
		}
		return true;
	}
	
	public boolean AreQueuesSameSize(){
		if(CPU.size() == User.size())
			return true;
		else 
			return false;
	}
	
	public void ClearUserQueue(){
		User.clear();
	}
}
