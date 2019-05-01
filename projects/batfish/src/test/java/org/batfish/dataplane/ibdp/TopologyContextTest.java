package org.batfish.dataplane.ibdp;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.NetworkBuilder;
import com.google.common.graph.ValueGraphBuilder;
import com.google.common.testing.EqualsTester;
import org.batfish.common.topology.Layer2Node;
import org.batfish.common.topology.Layer2Topology;
import org.batfish.datamodel.BgpPeerConfigId;
import org.batfish.datamodel.BgpSessionProperties;
import org.batfish.datamodel.Edge;
import org.batfish.datamodel.Topology;
import org.batfish.datamodel.eigrp.EigrpEdge;
import org.batfish.datamodel.eigrp.EigrpInterface;
import org.batfish.datamodel.isis.IsisEdge;
import org.batfish.datamodel.isis.IsisNode;
import org.batfish.datamodel.ospf.OspfNeighborConfigId;
import org.batfish.datamodel.ospf.OspfSessionProperties;
import org.batfish.datamodel.ospf.OspfTopology;
import org.batfish.datamodel.vxlan.VxlanNode;
import org.batfish.datamodel.vxlan.VxlanTopology;
import org.junit.Test;

/** Test of {@link TopologyContext}. */
public final class TopologyContextTest {

  @Test
  public void testEquals() {
    TopologyContext.Builder builder = TopologyContext.builder();
    TopologyContext base = builder.build();
    MutableValueGraph<BgpPeerConfigId, BgpSessionProperties> bgpTopology =
        ValueGraphBuilder.directed().build();
    bgpTopology.addNode(new BgpPeerConfigId("a", "b", "c"));
    MutableNetwork<EigrpInterface, EigrpEdge> eigrpTopology = NetworkBuilder.directed().build();
    eigrpTopology.addNode(new EigrpInterface("a", "b", "c"));
    MutableNetwork<IsisNode, IsisEdge> isisTopology = NetworkBuilder.directed().build();
    isisTopology.addNode(new IsisNode("a", "b"));
    MutableValueGraph<OspfNeighborConfigId, OspfSessionProperties> ospfTopology =
        ValueGraphBuilder.directed().build();
    ospfTopology.addNode(new OspfNeighborConfigId("a", "b", "c", "d"));
    MutableGraph<VxlanNode> vxlanTopology =
        GraphBuilder.undirected().allowsSelfLoops(false).build();
    vxlanTopology.addNode(new VxlanNode("a", 5));

    new EqualsTester()
        .addEqualityGroup(new Object())
        .addEqualityGroup(base, base, builder.build())
        .addEqualityGroup(builder.setBgpTopology(bgpTopology).build())
        .addEqualityGroup(builder.setEigrpTopology(eigrpTopology).build())
        .addEqualityGroup(builder.setIsisTopology(isisTopology).build())
        .addEqualityGroup(
            builder
                .setLayer2Topology(
                    Layer2Topology.fromDomains(
                        ImmutableList.of(ImmutableSet.of(new Layer2Node("a", "b", 5)))))
                .build())
        .addEqualityGroup(
            builder
                .setLayer3Topology(new Topology(ImmutableSortedSet.of(Edge.of("a", "b", "c", "d"))))
                .build())
        .addEqualityGroup(builder.setOspfTopology(new OspfTopology(ospfTopology)).build())
        .addEqualityGroup(builder.setVxlanTopology(new VxlanTopology(vxlanTopology)).build())
        .testEquals();
  }

  @Test
  public void testToBuilder() {
    TopologyContext.Builder builder = TopologyContext.builder();
    MutableValueGraph<BgpPeerConfigId, BgpSessionProperties> bgpTopology =
        ValueGraphBuilder.directed().build();
    bgpTopology.addNode(new BgpPeerConfigId("a", "b", "c"));
    MutableNetwork<EigrpInterface, EigrpEdge> eigrpTopology = NetworkBuilder.directed().build();
    eigrpTopology.addNode(new EigrpInterface("a", "b", "c"));
    MutableNetwork<IsisNode, IsisEdge> isisTopology = NetworkBuilder.directed().build();
    isisTopology.addNode(new IsisNode("a", "b"));
    MutableValueGraph<OspfNeighborConfigId, OspfSessionProperties> ospfTopology =
        ValueGraphBuilder.directed().build();
    ospfTopology.addNode(new OspfNeighborConfigId("a", "b", "c", "d"));
    MutableGraph<VxlanNode> vxlanTopology =
        GraphBuilder.undirected().allowsSelfLoops(false).build();
    vxlanTopology.addNode(new VxlanNode("a", 5));
    builder
        .setBgpTopology(bgpTopology)
        .setEigrpTopology(eigrpTopology)
        .setIsisTopology(isisTopology)
        .setLayer2Topology(
            Layer2Topology.fromDomains(
                ImmutableList.of(ImmutableSet.of(new Layer2Node("a", "b", 5)))))
        .setLayer3Topology(new Topology(ImmutableSortedSet.of(Edge.of("a", "b", "c", "d"))))
        .setOspfTopology(new OspfTopology(ospfTopology))
        .setVxlanTopology(new VxlanTopology(vxlanTopology));

    assertThat(builder.build(), equalTo(builder.build().toBuilder().build()));
  }
}