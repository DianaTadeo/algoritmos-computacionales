import java.util.*;
import java.io.*;
import java.lang.Math;

/**
 * Clase Prectica03 para la implementación de los algoritmos de busqueda
 * vistos en clase.
 * @author Diana Tadeo
 */
public class Busqueda{
	
	static int resultado;
	
	
	/**
	 * Implementación del algorítmo de busqueda secuencial.
	 * @param arrreglo es el conjunto de elementos ordenados donde se realizará la búsqueda
	 * @param buscado es el elemento buscado dentro del conjunto
	 * @return pos es la posición del arreglo donde se encuentra el elemento.
	 *  si este no se encuentra en arreglo, se devolverá -1
	 */
	public static int busquedaSecuencial(int[] arreglo,int buscado){
		int pos = -1;
		for(int i = 0; i < arreglo.length; i++){//recorremos todo el arreglo
			if(arreglo[i] == buscado){//comparamos el elemento en el arreglo con el buscado
				pos = i;//Si es verdadero guardamos la posicion
				break;
			}
		}
		return pos;
	}
	/**
	 * Implementación del algorítmo de búsqueda binaria.
	 * @param arrreglo es el conjunto de elementos ordenados donde se realizará la búsqueda
	 * @param buscado es el elemento buscado dentro del conjunto
	 * @return pos es la posición del arreglo donde se encuentra el elemento.
	 *  si este no se encuentra en arreglo, se devolverá -1
	 */
	public static int busquedaBinaria(int[] arreglo, int buscado){
		int n = arreglo.length;
		int centro;
		int inf=0;
		int sup=n-1;
		while(inf<=sup){
			centro=(sup+inf)/2; //Se elige el nuevo centro para realizar la búsqueda 
			if(arreglo[centro]==buscado) return centro;
			else if(buscado<arreglo[centro]){
				sup=centro-1; //Como el elemento es menor que el del centro, se define un nuevo superior
			}else {
				inf=centro+1; //Como el elemento es mayor que el centro, se define un nuevo inferior.
			}
		}
		return -1;
	}
	/**
	 * Implementación del algoritmo de búsqueda por Interpolación.
	 * @param arrreglo es el conjunto de elementos ordenados donde se realizará la búsqueda
	 * @param buscado es el elemento buscado dentro del conjunto
	 * @return pos es la posición del arreglo donde se encuentra el elemento.
	 *  si este no se encuentra en arreglo, se devolverá -1
	*/
	public static int busquedaInterpolacion(int[] arreglo, int buscado){
		int inf = 0;
		int sup = arreglo.length-1;
		int pos=0;
		while((arreglo[sup]>=buscado) && (arreglo[inf]<buscado)){
			pos = inf+(((sup-inf)*(buscado - arreglo[inf]))/(arreglo[sup] - arreglo[inf])); //Fórmua de interpolación
			if(buscado > arreglo[pos]){
				inf = pos+1; //En caso de que sea menor al encontrado
			}else if(buscado > arreglo[pos]){
				sup = pos-1; //En caso de que sea mayor al encontrado
			}else{
				inf = pos; //Encaso de que ya llo hayamos encontrado
			}
		}
		if(arreglo[inf] == buscado) pos = inf;
		else{
			pos = -1;
		}
		return pos;
	}
	
	
	/**	 
	 * Implementación del algorítmo de búsqueda exponencial.
	 * @param arrreglo es el conjunto de elementos ordenados donde se realizará la búsqueda
	 * @param buscado es el elemento buscado dentro del conjunto
	 * @return pos es la posición del arreglo donde se encuentra el elemento.
	 *  si este no se encuentra en arreglo, se devolverá -1
	 */
	public static int busquedaExponencial(int[] arreglo, int buscado){
		int posant=0;
		int exponente= 0;
		
		while(arreglo[exponente]< buscado){
			if(exponente==0) exponente=1; //Condición para el primer elemento del arreglo
			else{
				posant= exponente;//Se guarda el exponente.
				exponente*=2; //Se de fine un nuevo exponente potencia de 2.
				if(exponente> arreglo.length)break; //Si se ha superado el tamaño del arreglo, salimos del ciclo
			}
		}
		//Si el elemento de la posición del exponente es el buscado, lo regresamos
		if((exponente<arreglo.length)&&(arreglo[exponente]==buscado)) return exponente;
		//En otro caso, se realiza la busqueda binaria entre posant y exponente.
		else{
			int	posicionF=Math.min(exponente,arreglo.length);
			int [] nuevo= Arrays.copyOfRange(arreglo,posant,posicionF);
			int encontradoBin=busquedaBinaria(nuevo, buscado);
			//Se le suma a la sección del arreglo donde ya se había buscado, la posición obtenida
			//en la búsqueda binaria para obtener la posición original.
			return posant+encontradoBin; 
		}
	}
	
	/**
	 * Método que permite ejecutar las búsqeudas mediante el procesamiento de la entrada 
	 * y el archivo.
	 * @param archivo es el nombre del archivo de donde se leerá el conjunto ordenado.
	 * @param tipo es el tipo de busqueda que se realizará (el algoritmmo)
	 * @param buscado es el elemento buscado dentro del conjunto.
	 */
	public static void procesa(String archivo, String tipo, int buscado){
		String line;
		String[] numLinea;
		int[] conjunto;
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(archivo));
			LinkedList<String> aux= new LinkedList<String>();
			//SE comienza a leer linea a linea el archivo.
			//si la linea contiene más de una linea, se leen todas
			for(int i = -1; (line = br.readLine()) != null; i++){
				if(line=="") break;
				numLinea = line.trim().split(" ");
				for(String n: numLinea){ //SE obtienen los números por linea y se guardan en una lista.
					aux.add(n);
				}	
			}				
			conjunto = new int[aux.size()-1];
			//se pasan los elementos de String a int y se guardan en nun arreglo.
			for(int i = 0; i<conjunto.length; i++){
				conjunto[i] = Integer.parseInt(aux.poll());
			}
				
			//Se elige entre los algoritmos.
			switch(tipo.trim().toLowerCase()){
				case "binaria":	
					resultado=busquedaBinaria(conjunto, buscado);
					break;
				case "secuencial": 
					resultado=busquedaSecuencial(conjunto, buscado);
					break;
				case "exponencial": 
					resultado= busquedaExponencial(conjunto, buscado);
					break;
				case "interpolacion": 
					resultado=busquedaInterpolacion(conjunto,buscado);
					break;
				default: 
					System.out.println("No se eligió ninguna opción válida. Saliendo.");
					System.exit(1);
				}
				
		}catch(FileNotFoundException e){
			System.err.println("No existe el archivo o directorio.\n");
			System.exit(1);
		}catch(NumberFormatException e){
			System.err.println("Los elementos del arreglo deben ser números enteros.\n");
			System.exit(1);
		}catch(IOException e){
			System.err.println(e);
			System.exit(1);
		}
	}	
	
	
	
	public static void main(String[] args){
		
		try{
			if(args.length == 3){
				String nombreArchivo = args[0];
				int buscado = Integer.parseInt(args[1]);
				String tipoBusqueda = args[2];
				
				procesa(nombreArchivo, tipoBusqueda,buscado);
				if(resultado!=-1)
					System.out.println("La posición es: "+ resultado);
				else
					System.out.println("El elemento no está en el arreglo");
					
			}else{
				System.out.println("Ingreso de parámetros incorrectos.");
				System.exit(1);
			}
		}catch(Exception e){
			System.err.println("No se pudo realizar la busqueda. Saliendo.");
			System.exit(1);
		}
		
	}
}
