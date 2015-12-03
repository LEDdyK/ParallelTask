package pt.runtime;

import pt.functionalInterfaces.FunctorTenArgsNoReturn;
import pt.functionalInterfaces.FunctorTenArgsWithReturn;

public class TaskInfoTenArgs<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> extends TaskInfo<R> {

	private FunctorTenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> functorNoReturn = null;
	private FunctorTenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> functorWithReturn = null;
	
	private T1 arg1; private T2 arg2; private T3 arg3; private T4 arg4;
	private T5 arg5; private T6 arg6; private T7 arg7; private T8 arg8;
	private T9 arg9; private T10 arg10;
	
	TaskInfoTenArgs(FunctorTenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> functorNoReturn, TaskType taskType, int taskCount){
		this.functorNoReturn = functorNoReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoTenArgs(FunctorTenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> functorNoReturn, TaskType taskType){
		this(functorNoReturn, taskType, STAR);
	}
	
	TaskInfoTenArgs(FunctorTenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> functorWithReturn, TaskType taskType, int taskCount){
		this.functorWithReturn = functorWithReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoTenArgs(FunctorTenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> functorWithReturn, TaskType taskType) {
		this(functorWithReturn, taskType, STAR);
	}
	
	public TaskID<R> start(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10) {
		try{
			this.arg1 = arg1; this.arg2 = arg2; this.arg3 = arg3; this.arg4 = arg4;
			this.arg5 = arg5; this.arg6 = arg6; this.arg7 = arg7; this.arg8 = arg8;
			this.arg9 = arg9; this.arg10=arg10;
			
			if(this.taskCount == 1)
				return TaskpoolFactory.getTaskpool().enqueue(this);
			else{
				TaskIDGroup<R> taskGroup = TaskpoolFactory.getTaskpool().enqueueMulti(this);
				return taskGroup;
			}
		}catch(IllegalArgumentException e){
			System.out.println("An exception occurred in TaskInfoTenArgs::start method!");
			System.out.println("The error might have been caused by passing unexpected parameters!");
			e.printStackTrace();
			return null;
		}
	}
	
	R execute(){
		if (this.functorWithReturn!=null)
			return this.functorWithReturn.exec(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		this.functorNoReturn.exec(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		return null;
	}
}
