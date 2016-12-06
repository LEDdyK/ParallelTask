package fieldCollectionWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pt.runtime.ParaTask;
import sp.annotations.Future;
import sp.annotations.Task;
import sp.annotations.InitParaTask;

public class CollectionWrapperTest {
	
	@Future
	//List<Integer> myList = new ArrayList<>();
	static List<Integer> myList = ParaTask.getPtWrapper(new ArrayList<Integer>());
	
	public static int simulateWork(int i){
		Random rand = new Random();
		int random = rand.nextInt(10);
		try{
			System.out.println("Thread " + Thread.currentThread().getId() + " is going to sleep for " + random + " seconds.");
			Thread.sleep(random*1000);
		}catch(Exception e){e.printStackTrace();}
		System.out.println("Thread " + Thread.currentThread().getId() + " is returning " + (i * random));
		return (i * random);
	}
	
	@Task()
	public static int foo(int arg){
		return arg;
	}
	
	public static int foox(int arg){
		return arg;
	}
	
	@InitParaTask()
	public static void main(String[] args){
		
		@Future
		int specialNum = simulateWork(-2);
				
		for(int i = 0; i < 20; i++){
			@Future
			int num = simulateWork(i);
			
			if( i%3 == 0 )
				myList.add(i);
			else
				myList.add(num);
		}
		
		myList.add(specialNum);
		myList.add(specialNum + 2);
		
		for(int counter = 0; counter < myList.size(); counter++){
			int num = myList.get(foox(counter) + foo(0));
			int num2 = myList.get(foo(12));
			System.out.println("get(Index): " + myList.get(foo(counter)) + ", and num: " + (num + num2));
		}
	}
}
