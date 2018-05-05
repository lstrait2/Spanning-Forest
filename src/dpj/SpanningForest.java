
import dpj.tasks.*;
import dpj.tasks.wrappers.*;
import java.util.*;
import java.io.*;

public class SpanningForest extends Msg0Arg {
    
    public SpanningForest() {
        super(dpj.runtimeeffects.Effects.PURE);
            readGraph = new Task0Arg<Graph>(new dpj.runtimeeffects.Effect[]{new dpj.runtimeeffects.Effect.WriteEffect(dpj.runtimeeffects.RPLs.ROOT_STAR)}){     
                public Graph run() {
                    try {
                        return Graph.readEdgeGraph(TaskMain.args[0]);
                    } catch (IOException e) {
                        System.err.println("IO error reading graph: " + e);
                        System.exit(1);
                        throw new Error(e);
                    }
                }
            };
            getParallelism = new Task0Arg<java.lang.Integer>(new dpj.runtimeeffects.Effect[]{new dpj.runtimeeffects.Effect.ReadEffect($DPJ_rpl_1)}){
                
                
                @SuppressWarnings(value = "unknowneffects")
                public Integer run() {
                    if (TaskMain.args.length >= 2) {
                        return Integer.parseInt(TaskMain.args[1]);
                    } else {
                        return Integer.MAX_VALUE;
                    }
                }
            };
            getTaskSize = new Task0Arg<java.lang.Integer>(new dpj.runtimeeffects.Effect[]{new dpj.runtimeeffects.Effect.ReadEffect($DPJ_rpl_1)}){
                
                
                @SuppressWarnings(value = "unknowneffects")
                public Integer run() {
                    if (TaskMain.args.length >= 3) {
                        return Integer.parseInt(TaskMain.args[2]);
                    } else {
                        return 1;
                    }
                }
            };
        }
    }
    
    public void process() {
        Graph g = readGraph.execute();
        int parallelism = getParallelism.execute();
        int taskSize = getTaskSize.execute();
        buildSF(g, parallelism, taskSize).execute();
        getSF(g).execute();
    }
    
    private final Msg0Arg buildSF(final Graph g, final int parallelismParam, final int taskSize) {
        return new Msg0Arg(new dpj.runtimeeffects.Effect[]{new dpj.runtimeeffects.Effect.ReadEffect(new dpj.runtimeeffects.RPLElement[]{new dpj.runtimeeffects.RPLElement.ReferenceRPLElement(g)})}){
            
            
            @SuppressWarnings(value = "unknowneffects")
            public void process() {
                final dpj.runtimeeffects.RPLElement $DPJ_rgn_F = new dpj.runtimeeffects.RPLElement.NameRPLElement();
                final dpj.runtimeeffects.RPLElement $DPJ_rgn_E = new dpj.runtimeeffects.RPLElement.NameRPLElement();
                int size = g.edges.size();
                int parallelism = Math.min(parallelismParam, size);
                TaskFuture[] futures = new TaskFuture[parallelism];
                long t1 = System.nanoTime();
                for (int groupStart = 0; groupStart < size; groupStart += parallelism * taskSize) {
                    int groupLimit = Math.min(groupStart + parallelism * taskSize, size);
                    for (int taskNum = 0; taskNum < parallelism; taskNum++) {
                        int taskStart = groupStart + taskNum * taskSize;
                        int taskLimit = Math.min(taskStart + taskSize, groupLimit);
                        if (taskLimit <= taskStart) {
                            futures[taskNum] = null;
                            continue;
                        }
                        Graph.Edge[] edges = new Graph.Edge[taskLimit - taskStart];
                        for (int i = taskStart; i < taskLimit; i++) {
                            edges[i - taskStart] = g.edges.get(i);
                        }
                        futures[taskNum] = dpj.tasks.TaskMain.scheduler.executeLater(new Check(new dpj.runtimeeffects.RPLElement[]{$DPJ_rgn_E}), edges);
                    }
                    for (int taskNum = 0; taskNum < parallelism; taskNum++) {
                        if (futures[taskNum] == null) break;
                        dpj.tasks.TaskMain.scheduler.getValue(futures[taskNum]);
                    }
                }
                long t2 = System.nanoTime();
                System.out.println("SF build time = " + ((double)(t2 - t1)) / 1000000000 + " s");
            }
        };
    }

    private final Msg0Arg getSF(final Graph g) {
        return new Msg0Arg(new dpj.runtimeeffects.Effect[]{new dpj.runtimeeffects.Effect.ReadEffect(dpj.runtimeeffects.RPLs.ROOT_STAR)}){
            
            
            @SuppressWarnings(value = "unknowneffects")
            public void process() {
                int inSF = 0;
                for (int i = 0; i < g.edges.size(); i++) {
                    if (g.edges.get(i).inSF) {
                        inSF++;
                    }
                }
                System.out.println(inSF + " edges in spanning forest");
            }
        };
    }
    private final Task0Arg<Graph> readGraph;
    private final Task0Arg<Integer> getParallelism;
    private final Task0Arg<Integer> getTaskSize;
    private static final dpj.runtimeeffects.RPLElement[] $DPJ_rpl_1 = new dpj.runtimeeffects.RPLElement[]{dpj.tasks.TaskMain.$DPJ_rgn_Args};
}
