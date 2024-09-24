package network.common.generator.utils;

public class ROUAlgorithm {

	/**
	 * Function creates routing table
	 * for circulant net on chip
	 * using ROU algorithm.
	 */
	public static int[][] createRouting(int[][] netlist,
			                            int vertexAmount,
			                            int iterAmount,
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
					nextVertex = findNext(from, to, iterAmount, vertexAmount, s1, s2);
					
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
	
	/**
	 * Function finds next vertex on route.
	 * @param src
	 * @param dst
	 * @param iters
	 * @param vertices
	 * @param s1
	 * @param s2
	 * @return next vertex index
	 */
	public static int findNext(int src, int dst, int iters, int vertices, int s1, int s2) {
		int next = -1;
		
		if (src > dst) {
			next = src - stepCicles(dst, src, iters, vertices, s1, s2);
		}
		else {
			next = src + stepCicles(src, dst, iters, vertices, s1, s2);
		}
		
		if (next >= vertices) {
			next = next - vertices;
		}
		else {
			if (next < 0) {
				next = next + vertices;
			}
		}
				
		return next;
	}
	
	/**
	 * Function finds next step.
	 * @param src
	 * @param dst
	 * @param iters
	 * @param vertices
	 * @param s1
	 * @param s2
	 * @return the best step
	 */
	public static int stepCicles(int src, int dst, int iters, int vertices, int s1, int s2) {
		int bestWayR = 0;
		int stepR = 0;
		int bestWayL = 0;
		int stepL = 0;

		int s = dst - src;

		int R1 = s / s2 + s % s2;
		int R2 = s / s2 - s % s2 + s2 + 1;

		if (s % s2 == 0) {
			bestWayR = R1;
			stepR = s2;
		}
		else {
			if (R1 < R2) {
				bestWayR = R1;
				stepR = s1;
			}
			else {
				bestWayR = R2;
				stepR = s2;
			}
		}

		if (iters > 0) {
			for (int i = 1; i < iters + 1; i++) {
				int Ri1 = (s + vertices * i) / s2 + (s + vertices * i) % s2;
				int Ri2 = (s + vertices * i) / s2 - (s + vertices * i) % s2 + s2 + 1;
				if (Ri1 < bestWayR) {
					bestWayR = Ri1;
					stepR = s2;
					}
				if (Ri2 < bestWayR) {
					bestWayR = Ri2;
					stepR = s2;
				}
			}
		}

		s = src - dst + vertices;
		int L1 = s / s2 + s % s2;
		int L2 = s / s2 - s % s2 + s2 + 1;
		
		if (s % s2 == 0) {
			bestWayL = L1;
			stepL = -s2;
		}
		else {
			if (L1 < L2) {
				bestWayL = L1;
				stepL = -s1;
			}
			else {
				bestWayL = L2;
				stepL = -s2;
			}
		}
		
		if (iters > 0) {
			for (int i = 1; i < iters + 1; i++) {
				int Ri1 = (s + vertices * i)/ s2 + (s + vertices * i) % s2;
				int Ri2 = (s + vertices * i) / s2 - (s + vertices * i) % s2 + s2 + 1;
				if (Ri1 < bestWayL) {
					bestWayL = Ri1;
					stepL = -s2;
				}
				if (Ri2 < bestWayL) {
					bestWayL = Ri2;
					stepL = -s2;
				}
			}
		}
		
		if (bestWayR < bestWayL) {
			return stepR;
		}
		else {
			return stepL;
		}
	}
	
}
