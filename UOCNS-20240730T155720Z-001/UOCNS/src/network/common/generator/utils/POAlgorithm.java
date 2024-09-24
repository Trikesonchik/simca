package network.common.generator.utils;

public class POAlgorithm {

	/**
	 * Function creates routing table
	 * for circulant net on chip
	 * using PO algorithm.
	 */
	public static int[][] createRouting(int[][] netlist,
			                            int vertexAmount,
			                            int s1,
			                            int s2)
	{
		int portAmount = network.common.generator.Network.PORT_NUMBER;
		int[][] routing = new int[vertexAmount][vertexAmount];
		
		//for each source
		for (int from = 0; from < vertexAmount; from++) {
			// for each destination
			for (int to = 0; to < vertexAmount; to++) {
				// from vertex to itself
				if (from == to) {
					routing[from][to] = portAmount;
				}
				//from vertex to another vertex
				else {
					// next vertex index
					int nextVertex = -1;
					
					int s = to - from;
					
					if (s < 0) {
						s = s + vertexAmount;
					}
					
					// going clockwise
					if (s <= vertexAmount / 2.0) {
						// big step
						if (s >= s2) {
							nextVertex = (s2 + from) % vertexAmount;
						}
						// small step
						else {
							nextVertex = (s1 + from) % vertexAmount;
						}
					}
					// going counterclockwise
					else {
						s = vertexAmount - s;
						// big step
						if (s >= s2) {
							nextVertex = (vertexAmount - s2 + from) % vertexAmount;
						}
						// small step
						else {
							nextVertex = (vertexAmount - s1 + from) % vertexAmount;
						}
					}
					
					// source port connected with nextVertex
					int port = -1;
					for (int p = 0; p < portAmount; p++) {
						if (netlist[from][p] == nextVertex) {
							port = p;
						}
					}
					
					// fill routing
					routing[from][to] = port;
				}
			}
		}
		
		return routing;
	}
	
}
