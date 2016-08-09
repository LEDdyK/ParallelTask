package pt.mapReducers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pt.functionalInterfaces.*;
import pt.runtime.ParaTask;
import pt.runtime.ParaTask.TaskType;
import pt.runtime.ParaTask.ThreadPoolType;
import pt.runtime.TaskID;
import pt.runtime.TaskIDGroup;
import pt.runtime.TaskInfoNoArgs;
import pu.RedLib.Reduction;
import pt.mapReducers.AbstractDynamicMapReducer;
import pu.pi.ParIterator;
import pu.pi.ParIterator.Schedule;
import pu.pi.ParIteratorFactory;

public class PtDynamicMapReducer<T, E> extends AbstractDynamicMapReducer<T, E> {
	
	private FunctorOneArgWithReturn<T, E> userDefinedFunctor = null;
	private TaskID<Void> computationGroup = null;
	private List<TaskID<Void>> reductionOperations = null;
	private ParIterator<E> parIterator = null;
	private int numOfComputationTasks = 0;
	private int numberOfMultiThreads = 0;
	private int numberOfOneOffThreads = 0;
	private boolean mapReducerInterrup = false;
	private Thread waitingThread = null;
	private Collection<E> thisDataCollection = null;
	private boolean doMultiTask = false;

	

	public PtDynamicMapReducer(Reduction<T> reduction,FunctorOneArgWithReturn<T, E> functor, Collection<E> dataCollection, int threadCount, boolean multiTask) {
		
		reductionOperations = new ArrayList<>();
		setReduction(reduction);
		userDefinedFunctor = functor;
		thisDataCollection = dataCollection;
		doMultiTask = multiTask;
		
		if(doMultiTask)
			setThreadPoolSizesDoMulti(threadCount);
		else
			setThreadPoolSizesOneOff(threadCount);
		
		parIterator = ParIteratorFactory.createParIterator(dataCollection, numberOfMultiThreads);
		numOfComputationTasks = dataCollection.size();		
	}
	
	public PtDynamicMapReducer(Reduction<T> reduction, FunctorOneArgWithReturn<T, E> functor, Collection<E> dataCollection, int threadCount, Schedule schedulePolicy, 
			boolean multiTask){
		
		reductionOperations = new ArrayList<>();
		setReduction(reduction);
		userDefinedFunctor = functor;
		thisDataCollection = dataCollection;
		doMultiTask = multiTask;
		
		if(doMultiTask)
			setThreadPoolSizesDoMulti(threadCount);
		else
			setThreadPoolSizesOneOff(threadCount);
		
		parIterator = ParIteratorFactory.createParIterator(dataCollection, numberOfMultiThreads, schedulePolicy);
		numOfComputationTasks = dataCollection.size();
	}
	
	public PtDynamicMapReducer(Reduction<T> reduction, FunctorOneArgWithReturn<T, E> functor, Collection<E> dataCollection, int threadCount, Schedule schedulePolicy, 
			int chunkSize, boolean multiTask){
		
		reductionOperations = new ArrayList<>();
		setReduction(reduction);
		userDefinedFunctor = functor;
		thisDataCollection = dataCollection;
		doMultiTask = multiTask;
		
		if(doMultiTask)
			setThreadPoolSizesDoMulti(threadCount);
		else
			setThreadPoolSizesOneOff(threadCount);
			
		
		parIterator = ParIteratorFactory.createParIterator(dataCollection, numberOfMultiThreads, schedulePolicy, chunkSize);
		numOfComputationTasks = dataCollection.size();
	}
	
	public PtDynamicMapReducer(Reduction<T> reduction, FunctorOneArgWithReturn<T, E> functor, Collection<E> dataCollection, int threadCount, Schedule schedulePolicty, 
			int chunkSize, boolean ignoreBarrier, boolean multiTask){
		
		reductionOperations = new ArrayList<>();
		setReduction(reduction);
		userDefinedFunctor = functor;
		thisDataCollection = dataCollection;
		doMultiTask = multiTask;
		
		if(doMultiTask)
			setThreadPoolSizesDoMulti(threadCount);
		else
			setThreadPoolSizesOneOff(threadCount);
		
		parIterator = ParIteratorFactory.createParIterator(dataCollection, numberOfMultiThreads, schedulePolicty, chunkSize, ignoreBarrier);
		numOfComputationTasks = dataCollection.size();
	}
	
	protected boolean setThreadPoolSizesDoMulti(int numberOfThreads){
		
		if(numberOfThreads <= 2){
			numberOfMultiThreads = 1;
			numberOfOneOffThreads = 1;
		}
		else{
			numberOfOneOffThreads = (int) Math.ceil(numberOfThreads/10.0d);
			numberOfMultiThreads = numberOfThreads - numberOfOneOffThreads;
		}
		
		if(!ParaTask.setThreadPoolSize(ThreadPoolType.MULTI, numberOfMultiThreads))
			return false;
		if(!ParaTask.setThreadPoolSize(ThreadPoolType.ONEOFF, numberOfOneOffThreads))
			return false;
		
		return true;
	}
	
	protected boolean setThreadPoolSizesOneOff(int numberOfThreads){
		numberOfOneOffThreads = numberOfThreads;
		numberOfMultiThreads = 1;
		
		if(!ParaTask.setThreadPoolSize(ThreadPoolType.ONEOFF, numberOfOneOffThreads))
			return false;
		if(!ParaTask.setThreadPoolSize(ThreadPoolType.MULTI, numberOfMultiThreads))
			return false;
		return true;
	}
	
	public boolean setThreadPoolSizes (int oneOfThreadPoolSizse, int multiThreadPoolSizse){
		numberOfOneOffThreads = oneOfThreadPoolSizse;
		numberOfMultiThreads = multiThreadPoolSizse;
		
		if(!ParaTask.setThreadPoolSize(ThreadPoolType.ONEOFF, numberOfOneOffThreads))
			return false;
		if(!ParaTask.setThreadPoolSize(ThreadPoolType.MULTI, numberOfMultiThreads))
			return false;

		return true;
	}
	
	@Override
	protected void waitTillMapReduceFinished() {
		try{
			
			if(!doMultiTask || (numberOfMultiThreads > 1))
				((TaskIDGroup<Void>)computationGroup).waitTillFinished();
			else
				computationGroup.waitTillFinished();
			
			waitForReductionOperations();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void waitForReductionOperations(){
		while(reductionOperations.size() != (numOfComputationTasks-1)){
			try{
				waitingThread = Thread.currentThread();
				Thread.sleep(200);
			}catch(InterruptedException e){
				if(mapReducerInterrup){
					waitingThread = null;
					mapReducerInterrup = false;
				}
			}
		}
		
		TaskID<Void> taskID = reductionOperations.get(numOfComputationTasks-2); //indices start from 0;
		try{
			taskID.waitTillFinished();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void startMultiTask(){
		FunctorNoArgsNoReturn functor = () -> {
			while(parIterator.hasNext()){
				E element = parIterator.next();
				T result = userDefinedFunctor.exec(element);
				submitResult(result);
			}
		};
		
		TaskInfoNoArgs<Void> taskInfo = (TaskInfoNoArgs<Void>) ParaTask.asTask(TaskType.MULTI,  numberOfMultiThreads, functor);
		computationGroup = taskInfo.start();
	}
	
	@SuppressWarnings("unchecked")
	private void startOneOffTasks(){
		computationGroup = new TaskIDGroup<Void>();
		
		for(E element : thisDataCollection){
			FunctorNoArgsNoReturn functor = () -> {
				T result = userDefinedFunctor.exec(element);
				submitResult(result);
			};
			
			TaskInfoNoArgs<Void> taskInfo = (TaskInfoNoArgs<Void>) ParaTask.asTask(TaskType.ONEOFF, functor);
			((TaskIDGroup<E>)computationGroup).addInnerTask(taskInfo.start());
		}
	}
	
	@Override
	protected void map() {
		ParaTask.init();
		if(doMultiTask)
			startMultiTask();
		else
			startOneOffTasks();
	}
	

	@Override
	protected void parallelReduce(T t1, T t2) {
		TaskID<Void> taskID = null;
		FunctorNoArgsNoReturn functor = () -> {
			T result = reduction.reduce(t1, t2);
			submitResult(result);
		};
		
		TaskInfoNoArgs<Void> taskInfo = (TaskInfoNoArgs<Void>) ParaTask.asTask(TaskType.ONEOFF, functor);
		taskID = taskInfo.start();
		reductionOperations.add(taskID);		

		if(waitingThread != null){
			mapReducerInterrup = true;
			waitingThread.interrupt();
		}
	}

}
