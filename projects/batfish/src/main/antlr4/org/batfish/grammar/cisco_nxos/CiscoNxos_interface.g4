parser grammar CiscoNxos_interface;

import CiscoNxos_common;

options {
  tokenVocab = CiscoNxosLexer;
}

s_interface
:
  INTERFACE
  (
      s_interface_nve
      | s_interface_regular
  )
;

s_interface_nve
:
  nverange = nve_interface_range NEWLINE
  (
    nve_global
    | nve_host_reachability
    | nve_member
    | nve_no
    | nve_source_interface
  )*
;

s_interface_regular
:
  irange = interface_range NEWLINE
  (
    i_bandwidth
    | i_channel_group
    | i_description
    | i_encapsulation
    | i_ip
    | i_mtu
    | i_no
    | i_shutdown
    | i_switchport
    | i_vrf_member
  )*
;

i_bandwidth
:
  BANDWIDTH bw = interface_bandwidth_kbps NEWLINE
;

interface_bandwidth_kbps
:
// 1-100000000, units are kbps (for effective range of 1kbps-100Gbps)
  UINT8
  | UINT16
  | UINT32
;

i_channel_group
:
  CHANNEL_GROUP id = channel_id force = FORCE? NEWLINE
;

channel_id
:
// 1-4096
  UINT8
  | UINT16
;

i_description
:
  DESCRIPTION desc = interface_description NEWLINE
;

interface_description
:
  REMARK_TEXT
;

i_encapsulation
:
  ENCAPSULATION DOT1Q vlan = vlan_id NEWLINE
;

i_ip
:
  IP
  (
    i_ip_address
    | i_ip_null
  )
;

i_ip_address
:
  ADDRESS addr = interface_address SECONDARY? (TAG tag = uint32)? NEWLINE
;

i_ip_null
:
  REDIRECTS null_rest_of_line
;

i_mtu
:
  MTU interface_mtu NEWLINE
;

interface_mtu
:
// range depends on interface type
  mtu = uint16
;

i_no
:
  NO
  (
    i_no_autostate
    | i_no_bfd
    | i_no_null
    | i_no_shutdown
    | i_no_switchport
  )
;

i_no_autostate
:
  AUTOSTATE NEWLINE
;

i_no_bfd
:
  BFD ECHO NEWLINE
;

i_no_shutdown
:
  SHUTDOWN NEWLINE
;

i_no_switchport
:
  SWITCHPORT NEWLINE
;

i_no_null
:
  (
    IP
  ) null_rest_of_line
;

i_shutdown
:
  SHUTDOWN NEWLINE
;

i_switchport
:
  SWITCHPORT
  (
    i_switchport_access
    | i_switchport_trunk_allowed
    | i_switchport_trunk
  )
;

i_switchport_access
:
  ACCESS VLAN vlan = vlan_id NEWLINE
;

i_switchport_trunk
:
  TRUNK
  (
    i_switchport_trunk_allowed
    | i_switchport_trunk_native
  )
;

i_switchport_trunk_allowed
:
  ALLOWED VLAN
  (
    (
      ADD
      | REMOVE
    )? vlans = vlan_id_range
    | EXCEPT except = vlan_id
    | NONE
  ) NEWLINE
;

i_switchport_trunk_native
:
  NATIVE VLAN vlan = vlan_id NEWLINE
;

i_vrf_member
:
  VRF MEMBER name = vrf_name NEWLINE
;

interface_range
:
  iname = interface_name
  (
    DASH last = uint16
  )?
;

nve_interface_range
:
  iname = nve_interface_name
  (
    DASH last = uint8
  )?
;

nve_global
:
  GLOBAL
  (
    nvg_ingress_replication
    | nvg_mcast_group
    | nvg_suppress_arp
  )
;

nvg_ingress_replication
:
  INGRESS_REPLICATION PROTOCOL BGP NEWLINE
;

nvg_mcast_group
:
  MCAST_GROUP ip_address (L2 | L3) NEWLINE
;

nvg_suppress_arp
:
  SUPPRESS_ARP NEWLINE
;

nve_host_reachability
:
  HOST_REACHABILITY PROTOCOL BGP NEWLINE
;

nve_member
:
// 1-16777214
  MEMBER VNI vni = vni_number ASSOCIATE_VRF? NEWLINE
  (
     nvm_ingress_replication
     | nvm_mcast_group
     | nvm_peer_vtep
     | nvm_spine_anycast_gateway
     | nvm_suppress_arp
  )*
;

nvm_ingress_replication
:
   INGRESS_REPLICATION PROTOCOL (BGP | STATIC) NEWLINE
;

nvm_mcast_group
:
   MCAST_GROUP first = ip_address second = ip_address? NEWLINE
;

nvm_peer_vtep
:
   PEER_VTEP ip_address NEWLINE
;

nvm_spine_anycast_gateway
:
   SPINE_ANYCAST_GATEWAY NEWLINE
;

nvm_suppress_arp
:
   SUPPRESS_ARP DISABLE? NEWLINE
;

nve_no
:
   NO nve_no_shutdown
;

nve_no_shutdown
:
   SHUTDOWN NEWLINE
;

nve_source_interface
:
   SOURCE_INTERFACE name = interface_name NEWLINE
;