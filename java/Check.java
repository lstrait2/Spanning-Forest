
import dpj.tasks.*;
import dpj.tasks.wrappers.*;
import java.util.*;
import java.io.*;

class Check extends Msg1Arg<Graph.Edge[]> {
    private static final dpj.runtimeeffects.RPLElement.ReferenceSetRPLElement rdNodes_$DPJ_rpl_element = new dpj.runtimeeffects.RPLElement.ReferenceSetRPLElement("rdNodes");
    private static final dpj.runtimeeffects.RPLElement.ReferenceSetRPLElement wrNodes_$DPJ_rpl_element = new dpj.runtimeeffects.RPLElement.ReferenceSetRPLElement("wrNodes");
    private final dpj.runtimeeffects.RPLElement[] $DPJ_rgn_REdges;
    
    Check(final dpj.runtimeeffects.RPLElement[] $DPJ_rgn_REdges) {
        super(new dpj.runtimeeffects.Effect[]{new dpj.runtimeeffects.Effect.WriteEffect(new dpj.runtimeeffects.RPLElement[]{wrNodes_$DPJ_rpl_element}), new dpj.runtimeeffects.Effect.ReadEffect($DPJ_rgn_REdges), new dpj.runtimeeffects.Effect.ReadEffect(new dpj.runtimeeffects.RPLElement[]{rdNodes_$DPJ_rpl_element})});
        {
            this.$DPJ_rgn_REdges = $DPJ_rgn_REdges;
        }
        {
        }
        {
        }
    }
    
    private static Graph.Node ancestorOf(final Graph.Node u, HashMap<Graph.Node, Graph.Node> newAncestors) {
        Graph.Node newAncestor;
        if (u.ancestor != u) {
            return u.ancestor;
        } else if ((newAncestor = newAncestors.get(u)) != null) {
            return newAncestor;
        }
        return u;
    }
    
    public void process(Graph.Edge[] edges) {
        final dpj.tasks.DynamicReferenceSet<java.lang.Object> rdNodes = dpj.tasks.DynamicReferenceSet.referenceSetFor(rdNodes_$DPJ_rpl_element);
        final dpj.tasks.DynamicReferenceSet<java.lang.Object> wrNodes = dpj.tasks.DynamicReferenceSet.referenceSetFor(wrNodes_$DPJ_rpl_element);
        final dpj.runtimeeffects.RPLElement $DPJ_rgn_R = new dpj.runtimeeffects.RPLElement.NameRPLElement();
        Graph.Node[] u_ancestors = new Graph.Node[edges.length];
        Graph.Node[] v_ancestors = new Graph.Node[edges.length];
        HashMap<Graph.Node, Graph.Node> newAncestors = new HashMap<Graph.Node,Graph.Node>();
        for (int i = 0; i < edges.length; i++) {
            Graph.Edge e = edges[i];
            Graph.Node u_ancestor = e.u;
            Graph.Node v_ancestor = e.v;
            rdNodes.addAccess(u_ancestor);
            rdNodes.addAccess(v_ancestor);
            Graph.Node nextAncestor;
            while ((nextAncestor = Check.ancestorOf(u_ancestor, newAncestors)) != u_ancestor) {
                u_ancestor = nextAncestor;
                rdNodes.addAccess(u_ancestor);
            }
            while ((nextAncestor = Check.ancestorOf(v_ancestor, newAncestors)) != v_ancestor) {
                v_ancestor = nextAncestor;
                rdNodes.addAccess(v_ancestor);
            }
            if (u_ancestor == v_ancestor) continue;
            if (u_ancestor.index < v_ancestor.index) {
                Graph.Node temp = u_ancestor;
                u_ancestor = v_ancestor;
                v_ancestor = temp;
            }
            wrNodes.addAccess(u_ancestor);
            wrNodes.addAccess(e);
            u_ancestors[i] = u_ancestor;
            v_ancestors[i] = v_ancestor;
            newAncestors.put(u_ancestor, v_ancestor);
        }
        for (int i = 0; i < edges.length; i++) {
            final Graph.Node u_ancestor = u_ancestors[i];
            final Graph.Edge edge = edges[i];
            if (u_ancestor != null && wrNodes.contains(u_ancestor) && wrNodes.contains(edge)) {
                u_ancestor.ancestor = v_ancestors[i];
                edge.inSF = true;
            }
        }
    }
}
