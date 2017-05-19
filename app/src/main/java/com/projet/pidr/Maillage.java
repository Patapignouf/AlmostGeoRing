package com.projet.pidr;

import android.util.Log;

import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;

public class Maillage {
	private int longu;
	private int haut;
	private int prof;
	private Integer[][] L_vertex = {{0,0,0}, {1,0,0},{1,1,0},{0,1,0},{0,0,1},{1,0,1},{1,1,1},{0,1,1}};
	private int elem = 6;
	private Integer[][] L_decoupe_elem = {{0,1,3,5},{0,3,4,5},{3,4,5,7},{3,5,6,7},{2,3,5,6},{1,2,3,5}};
	
	public Maillage(int longu, int haut, int prof){
		this.longu = longu;
		this.haut = haut;
		this.prof = prof;
	}

	public int getLongu() {
		return longu;
	}

	public void setLongu(int longu) {
		this.longu = longu;
	}

	public int getHaut() {
		return haut;
	}

	public void setHaut(int haut) {
		this.haut = haut;
	}

	public int getProf() {
		return prof;
	}

	public void setProf(int prof) {
		this.prof = prof;
	}
	
	public int nb_noeuds() {
		return (prof+1)*(longu+1)*(haut+1);
	}
	
	public Integer[] get_liste_indexes(int num){
		int t = num % elem;
		int temp = num / elem;
		int z = temp / (longu*prof);
		int y = (temp - (z*longu*prof)) / longu;
		int x = (temp - (z*longu*prof)) % longu;
		return(new Integer[]{x,y,z,t});
	}

	public Integer[] get_vertex_tetra_from_num(int num){
		Integer[] Lindex = get_liste_indexes(num);
		Integer t = Lindex[Lindex.length-1];
		Integer[] L_vertex_tetra = L_decoupe_elem[t];
		return(L_vertex_tetra);
	}
	
	public Integer[][] get_coor_vertex(int num){
		//System.out.println(num);
		Integer[] Lindex = get_liste_indexes(num);
		Integer t = Lindex[Lindex.length-1];
		//System.out.println("Lindex : " + Arrays.toString(Lindex));
		Integer[][] L_coor_elem = new Integer[4][3];
		Integer[][] L_vertex_tetra = new Integer[4][3];
		for (int k = 0; k<4; k++){
			L_vertex_tetra[k] = L_vertex[L_decoupe_elem[t][k]];
		}
		for (int i = 0; i<4; i++){
			for (int j = 0; j<3; j++){
				L_coor_elem[i][j] = L_vertex_tetra[i][j] + Lindex[j];
			}
		}
		return L_coor_elem;
	}
	 
	public Integer[] get_index_from_tetra(int num_tetra){
		ArrayList<Integer> index_vertices = new ArrayList<Integer>();
		Integer[][] coor_tetra = get_coor_vertex(num_tetra);
	
		for (Integer[] coor : coor_tetra){
			//System.out.println(Arrays.toString(coor));
			index_vertices.add(coor[0] + coor[1] * (longu+1)+coor[2]*(longu+1)*(prof+1));
		}
		Object[] tab = index_vertices.toArray();
		return (Integer[]) Arrays.copyOf(tab, tab.length, Integer[].class) ;
		
	}
	
	public int point_vertex_tetra(Double[] doubles) {
		Double x = doubles[0];
		Double y = doubles[1];
		Double z = doubles[2];
		
		if (x > longu || y>prof || z>haut){
			Log.e("point_vertex_tetra", "Point hors du répère");
		}
		else{
			Double x_cube = x - Math.floor(x);
			Double y_cube = y - Math.floor(y);
			Double z_cube = z - Math.floor(z);
			
			double[] dic_score_tetra = new double[L_decoupe_elem.length];
			for (int i = 0; i < L_decoupe_elem.length; i++){
				double score = 0;
				for (Integer j : L_decoupe_elem[i]){
					Integer x_vertex = L_vertex[j][0];
					Integer y_vertex = L_vertex[j][1];
					Integer z_vertex = L_vertex[j][2];
					
					score += Math.sqrt(Math.pow(x_cube - x_vertex, 2)+Math.pow(y_cube - y_vertex, 2)+Math.pow(z_cube - z_vertex, 2));
				}
				dic_score_tetra[i] = score;
			}
			double min = 1000.;
			
			Integer tetra = 0;
			//System.out.println("Dic_score" + Arrays.toString(dic_score_tetra));
			for (int cle = 0; cle < dic_score_tetra.length; cle++){
				if (dic_score_tetra[cle] < min){
					min = dic_score_tetra[cle];
					tetra = cle;
				}
			}
			//System.out.println("Tetra : " + tetra);
			//System.out.println(min);
			int num = (int) (tetra + Math.floor(z)*prof*longu*elem + Math.floor(y)*longu*elem + Math.floor(x)*elem);
			return num;
		}
	}
	
	public double prod_scal(double[] vpb, double[] v2){
		double x = vpb[0];
		double x2 = v2[0];
		double y = vpb[1];
		double y2 = v2[1];
		double z = vpb[2];
		double z2 = v2[2];
		
		return x*x2 + y*y2 + z*z2;
	}
	
	public double[] prod_vect(double[] v1, double[] v2){
		double x = v1[0];
		double x2 = v2[0];
		double y = v1[1];
		double y2 = v2[1];
		double z = v1[2];
		double z2 = v2[2];
		
		double[] res = new double[3];
		res[0] = y*z2 - y2*z;
		res[1] = z*x2 - x*z2;
		res[2] = x*y2 - y*x2;
			
		return res;
	}
	
	public double tri_scal(double[] vpb, double[] v2, double[] v3){
		return prod_scal(vpb, prod_vect(v2,v3));
	}
	
	public double[] bar_coor(Double[] es) {
		//System.out.println("Point : " + Arrays.toString(es));
		int num_tetra = point_vertex_tetra(es);
		//System.out.println(num_tetra);
		Integer[][] vertex_tetra = get_coor_vertex(num_tetra);
		
		Integer[] a = vertex_tetra[0];
		Integer[] b = vertex_tetra[1];
		Integer[] c = vertex_tetra[2];
		Integer[] d = vertex_tetra[3];
		
		double[] vpa = {es[0]-a[0], es[1]-a[1], es[2]-a[2]};
		double[] vpb = {es[0]-b[0], es[1]-b[1], es[2]-b[2]};
		
		
		double[] vba = {b[0]-a[0], b[1]-a[1], b[2]-a[2]};
		double[] vca = {c[0]-a[0], c[1]-a[1], c[2]-a[2]};
		double[] vda = {d[0]-a[0], d[1]-a[1], d[2]-a[2]};
		
		double[] vcb = {c[0]-b[0], c[1]-b[1], c[2]-b[2]};
		double[] vdb = {d[0]-b[0], d[1]-b[1], d[2]-b[2]};
		
		double va6 = tri_scal(vpb, vdb, vcb);
		double vb6 = tri_scal(vpa, vca, vda);
		double vc6 = tri_scal(vpa, vda, vba);
		double vd6 = tri_scal(vpa, vba, vca);
		
		double v6 = 1./(tri_scal(vba, vca, vda));
		
		return(new double[]{va6*v6, vb6*v6, vc6*v6, vd6*v6});
		
	}
	
	
	
	public static void main(String[] args){
		Maillage m = new Maillage(1,1,1);
		//int num = m.point_vertex_tetra(new Double[]{0.,0.,0.9});
		System.out.println(Arrays.toString(m.get_coor_vertex(1)));
		//System.out.println(m.nb_noeuds());
		//System.out.println(Arrays.toString(m.get_index_from_tetra(num)));
	}

	public int get_elem() {
		return elem;
	}
	
	
}
