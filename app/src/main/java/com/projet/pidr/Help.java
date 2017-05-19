package com.projet.pidr;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Jama.Matrix;
import no.uib.cipr.matrix.AbstractMatrix;
import no.uib.cipr.matrix.DenseMatrix;

public class Help {
	public static Integer[][] matrix_M(int num, Maillage mesh){
		Integer[][] M = new Integer[4][4];
		Integer[][] L_coor = mesh.get_coor_vertex(num);
		
		for (int i = 0; i<4; i++){
			Integer[] L = new Integer[4];
			L[0] = 1;
			for (int j = 0; j<3; j++){

				L[j+1] = L_coor[i][j];
			}
			M[i] = L;
		}
		return M;
	}
	
	public static Integer[][] matrix_T(int num, Maillage mesh){
		Integer[][] mat_m = matrix_M(num, mesh);
		double[][] val = new double[4][4];
		for (int i = 0; i<4; i++){
			for (int j = 0; j<4; j++){
				val[i][j] = mat_m[i][j];
			}
		}
		Matrix M = new Matrix(val);
		Matrix Mm1 = M.inverse();
		double[][] id0 = {{0.,1.,0.,0.},{0.,0.,1.,0.},{0.,0.,0.,1.}};
		Matrix id = new Matrix(id0);
		Matrix T = id.times(Mm1);
		double[][] t = new double[4][4];
		double[][] t2 = T.getArray();
		Integer[][] val2 = new Integer[3][4];
		for (int i = 0; i<3; i++){
			for (int j = 0; j<4; j++){
				val2[i][j] = (int) t2[i][j];
			}
		}
		return val2;
		
	}
	
	public static Integer[] voisins(int num, Maillage mesh){
		ArrayList<Integer> L_voisins = new ArrayList<Integer>();
		Integer[] L_index = mesh.get_liste_indexes(num);
		//System.out.println(num);
		//System.out.println("L_index : " + Arrays.toString(L_index));
		switch(L_index[L_index.length-1]){
		case 0:
			add(L_voisins,num+1, mesh);
			add(L_voisins,num+5, mesh);
			add(L_voisins,num+2-(6*(mesh.getLongu()-1)*(mesh.getProf()-1)), mesh);
			add(L_voisins,num+4-6*(mesh.getLongu()-1), mesh);
			
			if (L_index[2] == 0)
				L_voisins.remove(2);
			if (L_index[1] == 0)
				L_voisins.remove(L_voisins.size()-1);
			break;
		case 1:
			add(L_voisins,num+1, mesh);
			add(L_voisins,num-1, mesh);
			add(L_voisins,num+3-6, mesh);
			add(L_voisins,num+2-6*(mesh.getLongu()-1), mesh);
			//System.out.println(L_voisins);
			if (L_index[1] == 0)
				L_voisins.remove(2);
			if (L_index[0] == 0)
				L_voisins.remove(L_voisins.size()-1);
			break;
		case 2:
			add(L_voisins,num+1, mesh);
			add(L_voisins,num-1, mesh);
			add(L_voisins,num+2+(6*(mesh.getLongu()-1)*(mesh.getProf()-1)), mesh);
			add(L_voisins,num+2-6, mesh);
			if (L_index[2] == mesh.getHaut()-1)
				L_voisins.remove(2);
			if (L_index[0] == 0)
				L_voisins.remove(L_voisins.size()-1);
			break;
		case 3:
			add(L_voisins,num+1, mesh);
			add(L_voisins,num-1, mesh);
			add(L_voisins,num-2+(6*(mesh.getLongu()-1)*(mesh.getProf()-1)), mesh);
			add(L_voisins,num-2+6*(mesh.getLongu()-1), mesh);
			if (L_index[2] == mesh.getHaut()-1)
				L_voisins.remove(2);
			if (L_index[1] == mesh.getProf()-1)
				L_voisins.remove(L_voisins.size()-1);
			break;
		case 4:
			add(L_voisins, num+1, mesh);
			add(L_voisins,num-1, mesh);
			add(L_voisins,num-2+6, mesh);
			add(L_voisins,num-4+6*(mesh.getLongu()-1), mesh);
			if (L_index[0] == mesh.getLongu()-1)
				L_voisins.remove(2);
			if (L_index[1] == mesh.getProf()-1)
				L_voisins.remove(L_voisins.size()-1);
			break;
		case 5:
			add(L_voisins,num-5, mesh);
			add(L_voisins,num-1, mesh);
			add(L_voisins,num-4+6, mesh);
			add(L_voisins,num-2-6*(mesh.getLongu()-1)*(mesh.getProf()-1), mesh);
			if (L_index[0] == mesh.getLongu()-1)
				L_voisins.remove(2);
			if (L_index[2] == 0)
				L_voisins.remove(L_voisins.size()-1);
			break;
		}
		Integer[] tab = new Integer[4];
		//System.out.println(l2.toArray(tab).length);
		return L_voisins.toArray(tab);
	}
	
	
	
	
	
	
	private static void add(ArrayList<Integer> l_voisins, int i, Maillage mesh) {
		int nb_tetra = mesh.get_elem()*mesh.getProf()*mesh.getHaut()*mesh.getLongu();
		if (i  < 0){
			l_voisins.add(null);
			return;
		}
		if (i >= nb_tetra){
			l_voisins.add(null);
			return;
		}
		l_voisins.add(i);
		return;
		
	}

	public static Integer[][] shared_nod(int num_tetra1, int num_tetra2, Maillage mesh){
		Integer[] tetra1 = mesh.get_index_from_tetra(num_tetra1);
		Integer[] tetra2 = mesh.get_index_from_tetra(num_tetra2);
		
		//System.out.println("t1 : " + Arrays.toString(tetra1));
		//System.out.println("t2 : " + Arrays.toString(tetra2));
		
		Integer[] m1 = new Integer[4];
		Integer[] m2 = new Integer[4];
		
		Boolean[] matched_vertices_tetra1 = {false, false, false, false};
		Boolean[] matched_vertices_tetra2 = {false, false, false, false};
		
		int k = 0;
		
		for (int i = 0 ; i < 4; i++){
			for (int j = 0 ; j < 4; j++){
				if (tetra1[i] == tetra2[j]){
					m1[k] = tetra1[i];
					m2[k] = tetra1[i];
					matched_vertices_tetra1[i] = true;
					matched_vertices_tetra2[j] = true;
					k++;
				}
			}
		}
		for (int l = 0; l < 4; l++){
			if (matched_vertices_tetra1[l] == false){
				m1[m1.length-1] = tetra1[l];
			}
			if (matched_vertices_tetra2[l] == false){
				m2[m1.length-1] = tetra2[l];
			}
		}
		//System.out.println("m1 : " + Arrays.toString(m1));
		//System.out.println("m2 : " + Arrays.toString(m2));
		return new Integer[][]{m1,m2};
	}
	
	public static Integer[][] add_constraint(int num_tetra1, int num_tetra2, Maillage mesh){
		Integer[] tetra1 = mesh.get_index_from_tetra(num_tetra1);
		Integer[] tetra2 = mesh.get_index_from_tetra(num_tetra2);
		
		Integer[][] tab = shared_nod(num_tetra1, num_tetra2, mesh);
		Integer[] m1 = tab[0];
		Integer[] m2 = tab[1];
		Integer[][] Ac = new Integer[3][mesh.nb_noeuds()];
		
		Integer[][] T1 = matrix_T(num_tetra1, mesh);
		Integer[][] T2 = matrix_T(num_tetra2, mesh);
		for (int i =0;i<3; i++){
			Integer[] Ac_int = new Integer[mesh.nb_noeuds()];
			int indice = -1;
			
			for (int k = 0; k < 4; k++){
				if (m1[m1.length-1]==tetra1[k]){
					indice = k;
				}
			}
			Ac_int[m1[m1.length-1]] = T1[i][indice];
			
			indice = -1;
			for (int k = 0; k < 4; k++){
				if (m2[m2.length-1]==tetra2[k]){
					indice = k;
				}
			}
			Ac_int[m2[m2.length-1]] = -T2[i][indice];
			
			for (int j = 0; j<3; j++){
				int indice1 = -1;
				int indice2 = -1;
				for (int k1 = 0; k1<4; k1++){
					if (m1[j]==tetra1[k1]){
						indice1 = k1;
					}
				}
				
				for (int k2 = 0; k2<4; k2++){
					if (m2[j]==tetra2[k2]){
						indice2 = k2;
					}
				}
				//System.out.println(Arrays.toString(m1));
				if (m1[j] != null)
					Ac_int[m1[j]] = T1[i][indice1] - T2[i][indice2];
				
			}
			for (int j = 0; j < Ac_int.length; j++){
				if (Ac_int[j] == null)
					Ac_int[j] = 0;
			}
			Ac[i] = Ac_int;

			//System.out.println(Arrays.toString(Ac_int));
		}
		return Ac;
		
	}
	
	public static Boolean contains(Integer[] tab , Integer e){
		for (Integer i : tab){
			if (i == e){
				return true;
			}
		}
		return false;
	}
	
	public static Integer index(Integer[] tab , Integer e){
		for (int i = 0; i<tab.length; i++){
			if (tab[i] == e){
				return i;
			}
		}
		return -1;
	}
	
	public static Double[] add_control_pt(Maillage mesh, Double[] doubles){
		int num_tetra = mesh.point_vertex_tetra(doubles);
		Integer[] L_vertices = mesh.get_index_from_tetra(num_tetra);
		int nb_noeuds = mesh.nb_noeuds();
		Double[] Ac = new Double[nb_noeuds];
		double[] coor = mesh.bar_coor(doubles);
		//System.out.println(Arrays.toString(coor));
		//System.out.println(Arrays.toString(l_vertex_cube));
		int s = 0;
		for (int i =0; i < nb_noeuds; i++){
			if (contains(L_vertices, i)){
				Ac[i] = coor[s];
				s++;
			}
		}
		return Ac;
		
	}
	
	public static Double[][][] data_point(Maillage mesh, Double[][] l_data_pt){
		ArrayList<Double[]> A_data_pt = new ArrayList<Double[]>();
		ArrayList<Double> B_data_pt = new ArrayList<Double>();
		for( Double[] i : l_data_pt){
			A_data_pt.add(add_control_pt(mesh, Arrays.copyOfRange(i,0,3)));
			B_data_pt.add(i[i.length-1]);
		}
		Double[][] a = new Double[l_data_pt.length][mesh.nb_noeuds()];
		a = A_data_pt.toArray(a);
		Double[] b = new Double[l_data_pt.length];
		return new Double[][][]{a, new Double[][]{B_data_pt.toArray(b)}};
	}
	
	public static Integer[][][] gradient(Maillage mesh){
		int nb_tetra = mesh.get_elem()*mesh.getProf()*mesh.getHaut()*mesh.getLongu();
		ArrayList<Integer[]> A_grad = new ArrayList<Integer[]>();
		ArrayList<Integer> B_grad = new ArrayList<Integer>();
		for (int i = 0; i<nb_tetra; i++){
			Integer[] L_voisins = voisins(i, mesh);
			//System.out.println(Arrays.toString(L_voisins));
			for (int m =0; m < 4; m++){
				Integer j = L_voisins[m];
				if (j !=null){
					Integer[][] tab = add_constraint(i, j, mesh);
					
					for (Integer[] i2 : tab){
						A_grad.add(i2);
						B_grad.add(0);
					}
				}
			}
		}
		Integer[][] a = new Integer[(nb_tetra-1)*3][mesh.nb_noeuds()];
		Integer[] b = new Integer[(nb_tetra-1)*3];
		return new Integer[][][]{A_grad.toArray(a), new Integer[][]{B_grad.toArray(b)}};
	}
	
	public static double[][][] system(Maillage mesh, Double[][] L_data_pt){
		Double[][][] tab = data_point(mesh, L_data_pt);
		Integer[][][] tab2 = gradient(mesh);
		Double[][] A_data_pt = tab[0];
		Double[] B_data_pt = tab[1][0];
		Integer[][] A_grad = tab2[0];
		Integer[] B_grad = tab2[1][0];
		int size = A_data_pt.length + A_grad.length;
		double[][] A = new double[size][mesh.nb_noeuds()];
		for (int i = 0; i < size; i++){
			if (i < L_data_pt.length){
				for (int j = 0; j < mesh.nb_noeuds();j++){
					if (A_data_pt[i][j] != null)
						A[i][j] = A_data_pt[i][j];
					else
						A[i][j] = 0;
				}
			}
			else{
				for (int j = 0; j < mesh.nb_noeuds();j++)
					A[i][j] = A_grad[i-L_data_pt.length][j];
			}
		}
		
		double[] B = new double[size];
		for (int i = 0; i < size; i++){
			if (i < L_data_pt.length){
				B[i] = B_data_pt[i];
			}
			else{
				if (B_grad[i-L_data_pt.length] != null)
					B[i] = B_grad[i-L_data_pt.length];
			}
		}
		
		return new double[][][]{A,new double[][]{B}};
		
	}
	
	public static double[][] transpose(double[][] b){
		int s = b[0].length;
		double[][] c = new double[s][1];
		for (int i = 0; i < s; i++)
			c[i][0] = b[0][i];
		System.out.println(b.length +"VS" + c.length);
		return c;
	}
	
	private static double[] mul_tab(double x, double[] tab){
		double[] tabres = new double[tab.length];
		for (int i = 0; i < tab.length; i++){
			tabres[i] = x * tab[i];
		}
		return tabres;
	}
	
	private static double[] sub_tabs(double[] tab1, double[] tab2){
		double[] tabres = new double[tab1.length];
		if (tab1.length != tab2.length){
			Log.e("sub_tabs", "Length");
		}
		for (int i = 0; i < tab1.length; i++){
			tabres[i] = tab1[i] - tab2[i];
		}
		return tabres;
	}
	
	
	public static double[][][] get_triangle(double[][][] tetra, int nbphi){
		Integer[] val = new Integer[nbphi];
		Arrays.fill(val, 0);
		for (double[][] t : tetra){
			System.out.println((int)t[0][0]);
			val[(int)t[0][0]]++;
		}
		ArrayList<Integer> z = new ArrayList<Integer>();
		System.out.println(Arrays.toString(val));
		for (int t : val){
			if (t != 0){
				z.add(t);
			}
		}
		
		if (z.size() == 1)
			return null;
		if (z.size() == 2){
			ArrayList<double[]> tri = new ArrayList<double[]>();
			if (val[z.get(0)] == 1){
				double[][] t2 = new double[2][4];
				for (double[][] t : tetra){
					if (t[0][0] == z.get(0)){
						t2 = t;
					}
				}
				ArrayList<double[]> vect = new ArrayList<double[]>();
				double[] pt = t2[1];
				for (double[][] t : tetra){
					if (t!=t2){
						vect.add(new double[]{pt[0]-t[1][0], pt[1]-t[1][1], pt[2]-t[1][2]});
					}
				}
				for (int i=0; i<3; i++){
					tri.add(sub_tabs(pt, mul_tab(1./2., vect.get(i))));
				}
				
				double[][] a = new double[tri.size()][tri.get(0).length];
				return new double[][][]{tri.toArray(a)};
			}
			// Cas o� c'est la seconde valeur des deux
			if (val[z.get(1)] == 1){
				double[][] t2 = new double[2][3];
				for (double[][] t : tetra){
					if (t[0][0] == z.get(1)){
						t2 = t;
					}
				}
				ArrayList<double[]> vect = new ArrayList<double[]>();
				double[] pt = t2[1];
				for (double[][] t : tetra){
					if (t!=t2){
						vect.add(new double[]{pt[0]-t[1][0], pt[1]-t[1][1], pt[2]-t[1][2]});
					}
				}
				for (int i=0; i<3; i++){
					tri.add(sub_tabs(pt, mul_tab(1./2., vect.get(i))));
				}
				
				double[][] a = new double[tri.size()][tri.get(0).length];
				return new double[][][]{tri.toArray(a)};
			}
			//Cas o� les deux valeurs sont � �galit�
			if (val[z.get(0)] == 2){
				 
				ArrayList<double[][]> vect = new ArrayList<double[][]>();
				ArrayList<double[][]> tetrabis = new ArrayList<double[][]>();
				ArrayList<double[][]> tetrathys = new ArrayList<double[][]>();
				
				for (double[][] t : tetra){
					if (t[0][0] == z.get(0)){
						tetrabis.add(t);
					}
					else{
						tetrathys.add(t);
					}
				} 
				
				for (double[][] t : tetrabis){
					ArrayList<double[]> v = new ArrayList<double[]>();
					double[] pt = t[1];
					for (double[][] t2 : tetrathys){
						double[] pt2 = t2[1];
						v.add(new double[]{pt[0]-pt2[0], pt[1]-pt2[1], pt[2]-pt[2]});
					}
					double[][] a = new double[2][3];
					vect.add(v.toArray(a));
				}
				
				ArrayList<double[]> tri1 = new 	ArrayList<double[]>();
				ArrayList<double[]> tri2 = new 	ArrayList<double[]>();
				double[] npt0 = tetrabis.get(0)[1];
				double[] npt1 = tetrabis.get(1)[1];
				double[] npv00 = vect.get(0)[0];
				double[] npv11 = vect.get(1)[1];
				double[] npv01 = vect.get(0)[1];
				double[] npv10 = vect.get(1)[0];
				tri1.add(sub_tabs(npt0, mul_tab(1./2., npv00)));
				tri2.add(sub_tabs(npt0, mul_tab(1./2., npv00)));
				tri1.add(sub_tabs(npt1, mul_tab(1./2., npv11)));
				tri2.add(sub_tabs(npt1, mul_tab(1./2., npv11)));
				tri1.add(sub_tabs(npt0, mul_tab(1./2., npv01)));
				tri2.add(sub_tabs(npt1, mul_tab(1./2., npv10)));
				double[][] a = new double[3][tri1.get(0).length];
				double[][] b = new double[3][tri1.get(0).length];
				return new double[][][]{tri1.toArray(a), tri2.toArray(b)};
				
			}
		}
		return null;
	}
	
	public static ArrayList<double[][]> get_real_triangle(Double[][] l_data_pt){
		double maxx = 0;
		double maxy = 0;
		double maxz = 0;
		for (Double[] t : l_data_pt){
			if (t[0] > maxx){
				maxx = t[0];
			}
			if (t[1] > maxy){
				maxy = t[1];
			}
			if (t[2] > maxz){
				maxz = t[2];
			}
		}
		
		Maillage mesh = new Maillage((int) Math.floor(maxx)+1, (int) Math.floor(maxy)+1, (int) Math.floor(maxz)+1);
		int nb_tetra = mesh.get_elem()*mesh.getProf()*mesh.getHaut()*mesh.getLongu();
		DenseMatrix phi = solve(mesh, l_data_pt);
		ArrayList<Integer> phi2 = new ArrayList<Integer>();
		for (int i = 0; i<phi.numRows();i++){
			phi2.add((int) Math.floor(phi.get(i, 0)));
		}
		int maxphi = 0;
		int minphi = 0;
		for (int k : phi2){
			if (k> maxphi){
				maxphi = k;
			}
			if (k < minphi){
				minphi = k;
			}
		}

		Log.d("Debu tri"," "+ Math.abs(minphi));

		for (int i = 0; i < phi2.size(); i++){
			Integer o = phi2.get(i);
			phi2.remove(i);
			phi2.add(i,o+Math.abs(minphi));
		}

		ArrayList<double[][][]> res = new ArrayList<double[][][]>();
		for (int i = 0; i < nb_tetra; i++){
			ArrayList<double[][]> reslist = new ArrayList<double[][]>();
			Integer[][] coor = mesh.get_coor_vertex(i);
			Integer[] sommet = mesh.get_index_from_tetra(i);
			for (int j = 0; j <4; j++){
				double[][] temp = new double[2][3];
				temp[0] = new double[]{phi2.get(sommet[j]),0,0};
				temp[1] = new double[]{coor[j][0],coor[j][1],coor[j][2]};
				reslist.add(temp);
			}
			double[][][] a = new double[4][2][3];
			double[][][] restab = reslist.toArray(a);
			double[][][] tri = get_triangle(restab, maxphi+Math.abs(minphi)+1);
			res.add(tri);
		}
		ArrayList<double[][]> resfl = new ArrayList<double[][]>();
		System.out.println("On est ici ? :D");
		for (double[][][] i : res){
			for (double[][] t : i){
				resfl.add(t);
			}
		}
		return resfl;
	}
	
	public static DenseMatrix solve(Maillage mesh,  Double[][]L_data_pt){
		double[][][] tab = system(mesh, L_data_pt);
		//Matrix B = new Matrix(tab[1]).transpose(); //Correspond � jama
		//Matrix A  = new Matrix(tab[0]);
		DenseMatrix B2 = new DenseMatrix(transpose(tab[1])); //mtj
		DenseMatrix A2 = (DenseMatrix) new DenseMatrix(tab[0]);
		DenseMatrix X = new DenseMatrix(A2.numColumns(), B2.numColumns());
		//System.out.println(A2.numColumns());
		DenseMatrix phi2 = (DenseMatrix) A2.solve(B2, X);
		//Matrix phi =A.solve(B);
		return phi2;
		
	}
	
	public static void main(String[] args){
		/*Maillage mesh = new Maillage(1,1,1);
		Double[][] L_data_pt = {{0.23,0.23,0.23, 2.}, {0.73, 0.23, 0.23, 0.} ,{0.73,0.23,0.73, 1.}, {0.23,0.23,0.73, 2.}, {0.23,0.73,0.23, 1.}, {0.73, 0.73, 0.23, 0.} ,{0.73,0.73,0.73, 1.}, {0.23,0.73,0.73, 2.}};
		DenseMatrix phi = solve(mesh, L_data_pt);
		int indice_i = 0;
		int indice_j = 0;*/
		
		/*for (int i = 0; i<phi.numRows() ; i++){
			for (int j = 0; j<phi.numColumns();j++){
				System.out.print(phi.get(i, j));
			}
			System.out.println();
		}*/
		/*double[] val1 = new double[]{1,0,0};
		double[] val2 = new double[]{2,0,0};
		double[][][] tetra = new double[][][]{{val1, {0,1,0}},{val1,{1,1,0}},{val2,{1,1,1}},{val2,{0,2,0}}};
		double[][][] tri = get_triangle(tetra, 3);
		for (double[][] t1 : tri){
			for (double[] t2 : t1){
				System.out.print("[");
				for (double t3 : t2){
					System.out.print(t3+",");
				}
				System.out.print("],");
			}
			System.out.println();
		}*/
	}
	
}
