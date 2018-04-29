import java.util.*;
import java.io.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;

/**
 * Clase Maximal para resolver el problema de obtener un conjunto maximal independiente
 * @author Diana Tadeo
 */
public class Maximal{
	
	/**
	 *Clase Nodo como auxiliar para crear la gráfica
	 */
	public class Nodo{
		public LinkedList<Nodo> vecinos;
		public boolean marca;
		public String nombre="";

		public Nodo(String nombre){
			this.nombre=nombre;
			vecinos=new LinkedList<Nodo>();
			marca=false;
		}
	}



	public LinkedList<Nodo> grafica= new LinkedList<Nodo>();
	Graph graph = new SingleGraph("Grafica");


	/**
	 * Método que crea la gráfica a partir del archivo de entrada
	 * la gráfica está representada por una lista de Nodos 
	 *  @param archivo será el nombre del archivo donde obtendremos los datos de la gráfica
	 */
	public void generaGrafica(String archivo){
		try{
			//Comenzamos a leer el archivo
			File file = new File(archivo);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			//La primer linea es la lista de vértices
			String vertices= br.readLine();
			StringTokenizer tokens=new StringTokenizer(vertices, ", ");
			String nombre="";
			//se inicia la creacion de graficas añadiendo los vértices
			while(tokens.hasMoreTokens()){
				nombre=tokens.nextToken();
				grafica.add(new Nodo(nombre));
				graph.addNode(nombre);	
			}
			/*
			 * A partir de aquí comenzamos a crear las "adyacencias" a través de
			 * la lista de vecinos de cada nodo.
			 * También generamos las aristas en GraphStream por cada iteración
			 * */
			Nodo aux=null;
			Nodo vecino=null;
			String nom, nomv;
			String linea;
			while((linea = br.readLine()) != null){
				ListIterator<Nodo> iter= grafica.listIterator();
			    ListIterator<Nodo> itervecino= grafica.listIterator();
				tokens=new StringTokenizer(linea, ", ");
				nom= tokens.nextToken();
				nomv=tokens.nextToken();
				//Buscamos ambos vértices obtenidos en una linea del archivo
				while(iter.hasNext()){
					aux= iter.next();
					if(aux.nombre.equals(nom)) break;
				}
				while(itervecino.hasNext()){
					vecino= itervecino.next();
					if(vecino.nombre.equals(nomv)) break;
				}
				//Agregamos un vértice a la lista de vecinos del otro y viseversa
				aux.vecinos.add(vecino);
				vecino.vecinos.add(aux);
				graph.addEdge(nom+nomv, nom, nomv);
			}
		}catch(Exception e){
			System.out.println("La grafica no fue creada");
		}	    
	}
	
	/**
	 * Método que genera el conjunto independiente maximal de forma aleatoria
	 * y colorea los vértices de la grafica para resaltar este conjunto.
	 * @param mar será el nombre del vértice en donde se comenzará la 
	 *        búsqueda del conjunto
	 */
	public void maximal(String mar){
		Nodo centro= null;
		//Se marca el Nodo inicial donde se va a comenzar la busqueda del conjunto
		for(Nodo n: grafica){
			if(mar.equals(n.nombre)){
				centro=n;
				break;
			}
		}
		//Se marca el nodo inicial
		centro.marca=true;
		//aux servirá para cambiar el color del nodo en la interfaz gráfica
		Node aux = graph.getNode(centro.nombre);
		aux.addAttribute("ui.style", "fill-color: rgb(0,100,255);");
		LinkedList<Nodo> grafica2= new LinkedList<Nodo>();
		grafica2.addAll(grafica);
		grafica2.remove(centro);
		/*
		 * Itero la gráfica excepto el nodo inicial y reviso si sus vecinos ya están marcados
		 * Si existe un nodo marcado, no marco el nodo.
		 * Si no hay ni un solo vecino marcado, marco el nodo.
		 */ 
		for(Nodo n: grafica2){
			aux=graph.getNode(n.nombre);
			boolean marcar= true;
			for(Nodo m : n.vecinos){
				if(m.marca==true){
					marcar=false;
					break;
				}	
			}
			//Marco en la interfaz gráfica el Nodo marcado
			if(marcar==true)aux.addAttribute("ui.style", "fill-color: rgb(0,100,255);");
			n.marca=marcar;
		}
	}
	/**
	 * Método que genera el archivo de salida
	 * @param el nombre del archivo de salida
	 */
	public void salida(String nombre){
		try{
		File archivoS = new File("Salida"+ nombre+".csv");
		archivoS.createNewFile();
		FileWriter fw= new FileWriter(archivoS);
		BufferedWriter bw= new BufferedWriter(fw);
		String aux="";
		//Recorre todos los vértices y si se encuentra marcado lo imprime
		for(Nodo n: grafica){
			if(n.marca==true)
			aux+=n.nombre+", ";
		}
		fw.write(aux);
		fw.close();
	}catch(Exception e){
		
	}
		
	}
	
	public static void main(String[] args){
		
		//Se inicializan los objetos necesarios
		Maximal g= new Maximal();
		Scanner entrada= new Scanner(System.in);
		
		System.out.print("Ingresa el nombre del archivo: ");
		String file= entrada.nextLine();
		//Se genera la gráfica
		g.generaGrafica(file);
		int tam=file.length()-4;
		file= file.substring(0, tam);
		int inicio = (int) (Math.random() * (g.grafica.size()-1)) + 1;
		String inicial= "v"+inicio;
		//Se obtiene el conjunto
		g.maximal(inicial);
		g.salida(file);
		//Imprime gráficamenta la gráfica resultante
		g.graph.display();
	}

}
