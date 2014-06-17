package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import pt.benchmarks.wrapper.ParaTaskWrapper;
import pt.benchmarks.wrapper.BenchmarksWrapper;


@State(Scope.Thread)
public class MyBenchmarkThread12 {

    @Setup
    public void init() {
    	VarianceConfig.init();
        ParaTaskWrapper.setThreadPoolSize(12);
    }
    
    @GenerateMicroBenchmark
    public double testVarianceParaTaskWithLambda() {
        return BenchmarksWrapper.varianceParaTaskWithLambda(VarianceConfig.population, VarianceConfig.THRESHOLD);
    }
    

}
