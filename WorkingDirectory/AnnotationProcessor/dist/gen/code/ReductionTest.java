

package code;


public class ReductionTest {
    public ReductionTest(int size) {
    }

    public int foo(int x) {
        return x * 10;
    }

    public int multiTask(pu.loopScheduler.LoopScheduler scheduler) {
        pt.runtime.TaskThread taskThread = ((pt.runtime.TaskThread) (java.lang.Thread.currentThread()));
        pu.loopScheduler.LoopRange range = scheduler.getChunk(taskThread.getThreadID());
        java.util.Random rand = new java.util.Random();
        int result = 0;
        for (int i = range.loopStart; i < (range.loopEnd); i += range.localStride) {
            int randomNo = rand.nextInt(i);
            result += foo(randomNo);
        }
        return result;
    }

    public java.util.Map<java.lang.String, java.lang.Integer> mapMaker(java.lang.String str) {
        java.util.Map<java.lang.String, java.lang.Integer> newMap = new java.util.HashMap<>();
        newMap.put(str, 20);
        return newMap;
    }

    public void process(int range) {
        pu.loopScheduler.LoopScheduler scheduler = pu.loopScheduler.LoopSchedulerFactory.createLoopScheduler(0, range, 1, java.lang.Runtime.getRuntime().availableProcessors(), pu.loopScheduler.AbstractLoopScheduler.LoopCondition.LessThan, pu.loopScheduler.LoopSchedulerFactory.LoopSchedulingType.Static);
        pt.runtime.TaskInfoOneArg<Integer, pu.loopScheduler.LoopScheduler> __taskPtTask__ = ((pt.runtime.TaskInfoOneArg<Integer, pu.loopScheduler.LoopScheduler>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.MULTI, 
			(pt.functionalInterfaces.FunctorOneArgWithReturn<Integer, pu.loopScheduler.LoopScheduler>)(__schedulerPtNonLambdaArg__) -> multiTask(__schedulerPtNonLambdaArg__))));
        pt.runtime.TaskID<Integer> __taskPtTaskID__ = __taskPtTask__.start(scheduler);
        java.lang.System.out.println(("The result of task is: " + __taskPtTaskID__.getReturnResult()));
        pt.runtime.TaskInfoNoArgs<java.util.Map<java.lang.String> __task2PtTask__ = ((pt.runtime.TaskInfoNoArgs<java.util.Map<java.lang.String>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.MULTI, 
			(pt.functionalInterfaces.FunctorNoArgsWithReturn<java.util.Map<java.lang.String>)() -> mapMaker("HI"))));
        pt.runtime.TaskID<java.util.Map<java.lang.String> __task2PtTaskID__ = __task2PtTask__.start();
        java.lang.System.out.println(("The result for task2 is: " + (task2.get("HI"))));
    }
}

