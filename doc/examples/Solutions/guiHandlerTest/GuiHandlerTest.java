

package guiHandlerTest;


public class GuiHandlerTest {
    pt.runtime.TaskIDGroup<java.lang.Integer> __array1PtTaskIDGroup__ = new pt.runtime.TaskIDGroup<>();

    volatile boolean __array1PtTaskIDGroup__Synchronized = false;

    volatile java.util.concurrent.locks.Lock __array1PtTaskIDGroup__Lock = new java.util.concurrent.locks.ReentrantLock();

    int[] array1 = new int[5];

    private int foo(int x) throws java.lang.InterruptedException {
        java.util.Random rand = new java.util.Random();
        int randNo = rand.nextInt(x);
        java.lang.Thread.sleep((randNo * 1000));
        return randNo;
    }

    private java.lang.Void updateGui(int x, int y) throws java.lang.Exception {
        return null;
    }

    private java.lang.Void updateDB(int x, int y) {
        return null;
    }

    public void handleException() {
        java.lang.System.out.println("Exception occurred");
    }

    public void taskRun() {
        pt.runtime.ParaTask.init(pt.runtime.ParaTask.PTSchedulingPolicy.MixedSchedule, java.lang.Runtime.getRuntime().availableProcessors());
        int[] array2 = new int[5];
        pt.runtime.TaskIDGroup<java.lang.Integer> __array2PtTaskIDGroup__ = new pt.runtime.TaskIDGroup<>(5);
        try {
            int x = 25;
            int y = 13;
            pt.runtime.TaskInfoOneArg<java.lang.Integer, java.lang.Integer> __aPtTask__ = ((pt.runtime.TaskInfoOneArg<java.lang.Integer, java.lang.Integer>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, ((pt.functionalInterfaces.FunctorOneArgWithReturn<java.lang.Integer, java.lang.Integer>) (( __xPtNonLambdaArg__) -> foo(__xPtNonLambdaArg__))))));
            pt.runtime.ParaTask.registerSlotToNotify(__aPtTask__, ()->updateDB(x, y));
            pt.runtime.ParaTask.registerAsyncCatch(__aPtTask__, java.io.IOException.class, () -> handleException());
            pt.runtime.TaskID<java.lang.Integer> __aPtTaskID__ = __aPtTask__.start(x);
            pt.runtime.TaskInfoOneArg<java.lang.Integer, java.lang.Integer> __bPtTask__ = ((pt.runtime.TaskInfoOneArg<java.lang.Integer, java.lang.Integer>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, ((pt.functionalInterfaces.FunctorOneArgWithReturn<java.lang.Integer, java.lang.Integer>) (( __yPtNonLambdaArg__) -> foo(__yPtNonLambdaArg__))))));
            pt.runtime.ParaTask.registerSlotToNotify(__bPtTask__, ()->updateDB(x, y));
            pt.runtime.ParaTask.registerSlotToNotify(__bPtTask__, (__bPtNonLambdaArg__)->{try{updateGui(__bPtNonLambdaArg__, x);}catch(Throwable e){e.printStackTrace();}});
            pt.runtime.TaskID<java.lang.Integer> __bPtTaskID__ = __bPtTask__.start(y);
            int j = __aPtTaskID__.getReturnResult();
            int k = __bPtTaskID__.getReturnResult();
            pt.runtime.TaskInfoNoArgs<java.lang.Integer> __cPtTask__ = ((pt.runtime.TaskInfoNoArgs<java.lang.Integer>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.MULTI, ((pt.functionalInterfaces.FunctorNoArgsWithReturn<java.lang.Integer>) (() -> foo(24))))));
            pt.runtime.ParaTask.registerSlotToNotify(__cPtTask__, ()->updateDB(j, k));
            pt.runtime.TaskIDGroup<java.lang.Integer> __cPtTaskID__ = ((pt.runtime.TaskIDGroup<java.lang.Integer>) (__cPtTask__.start()));
            pt.runtime.ParaTask.registerSlotToNotify(__array1PtTaskIDGroup__, ()->updateDB(j, k));
            pt.runtime.ParaTask.registerSlotToNotify(__array2PtTaskIDGroup__, ()->updateDB(j, k));
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }
}

