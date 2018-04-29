package sort;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

public class Sort{

  int[] numeros;

  public Sort(String archivo, int framerate, String metodo){
    EventQueue.invokeLater(new Runnable(){
      @Override
      public void run(){
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame frame = new JFrame("Ordenamientos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(new Contenedor(archivo, framerate, metodo));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
      }catch(Exception e){
        System.out.println("\t:(");
      }
      }
    });
  }

  public class Contenedor extends JPanel{

    private JLabel etiqueta;

    public Contenedor(String archivo, int framerate, String metodo){
      setLayout(new BorderLayout());
      etiqueta = new JLabel(new ImageIcon(createImage(archivo)));
      add(etiqueta);
      JButton botonOrdenar = new JButton("Ordenar");
      add(botonOrdenar, BorderLayout.SOUTH);
      botonOrdenar.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
          BufferedImage imagen = (BufferedImage) ((ImageIcon) etiqueta.getIcon()).getImage();
          new UpdateWorker(imagen, etiqueta, archivo, framerate, metodo).execute();
        }
      });

    }

    public BufferedImage createImage(String archivo){
      BufferedImage imagen = null;
      try{
        imagen = ImageIO.read(new File("resource/"+archivo));
        ataqueHackerman(imagen);
        Graphics2D g = imagen.createGraphics();
        g.dispose();
      }catch(Exception e){
        System.err.println("(-)\tAsegurate de estar en el directorio 'src'");
        System.err.println("\ty de haber escrito bien el nombre de imagen (la cual debe estar en la carpeta resource)");
      }
      return imagen;
    }

    public void ataqueHackerman(BufferedImage imagen){
      int length = imagen.getHeight()*imagen.getWidth();
      numeros = new int[length];
      for(int i = 0; i < numeros.length; i++)
        numeros[i] = i;
      Random r = new Random();
      for(int i = 0; i < length; i++){
        int j = r.nextInt(length);
        swapImagen(imagen, i, j);
      }
    }

    public void swapImagen(BufferedImage imagen, int i, int j){
      int colI = i%imagen.getWidth();
      int renI = i/imagen.getWidth();
      int colJ = j%imagen.getWidth();
      int renJ = j/imagen.getWidth();
      int aux = imagen.getRGB(colI, renI);
      imagen.setRGB(colI, renI, imagen.getRGB(colJ, renJ));
      imagen.setRGB(colJ, renJ, aux);
      aux = numeros[i];
      numeros[i] = numeros[j];
      numeros[j] = aux;
    }

  }

  public class UpdateWorker extends SwingWorker<BufferedImage, BufferedImage>{

    private BufferedImage referencia;
    private BufferedImage copia;
    private JLabel target;
    int framerate;
    int n;
    String metodo;
    int iteracion;

    public UpdateWorker(BufferedImage master, JLabel target, String archivo, int speed, String algoritmo){
      this.target = target;
      try{
        referencia = ImageIO.read(new File("resource/"+archivo));
        copia = master;
        n = copia.getHeight()*copia.getWidth();
      }catch(Exception e){
        System.err.println(":c Esto no deberia ocurrir");
      }
      framerate = speed; // Indica cada cuantas iteraciones se actualizara la imagen
      metodo = algoritmo;
      iteracion = 0;
    }

    public BufferedImage updateImage(){
      Graphics2D g = copia.createGraphics();
      g.drawImage(copia, 0, 0, null);
      g.dispose();
      return copia;
    }

    @Override
    protected void process(List<BufferedImage> chunks){
      target.setIcon(new ImageIcon(chunks.get(chunks.size() - 1)));
    }

    public void update(){
      for(int i = 0; i < n; i++){
        int indiceDeOriginal = numeros[i];
        int colOriginal = indiceDeOriginal%copia.getWidth();
        int renOriginal = indiceDeOriginal/copia.getWidth();
        int colI = i%copia.getWidth();
        int renI = i/copia.getWidth();
        copia.setRGB(colI, renI, referencia.getRGB(colOriginal, renOriginal));
      }
      publish(updateImage());
    }

    @Override
    protected BufferedImage doInBackground() throws Exception{
      if(metodo.equals("bubble"))
        bubbleSort();
      if(metodo.equals("selection"))
        selectionSort();
      if(metodo.equals("insertion"))
        insertionSort();
      if(metodo.equals("merge"))
        mergeSort();
      if(metodo.equals("quick"))
        quickSort();
      update();
      return null;
    }

    private void bubbleSort(){
      for(int i = 0; i < n-1; i++){
        for(int j = 0; j < n-i-1; j++){
          if(numeros[j] > numeros[j+1])
          swap(j, j+1);
        }
        if(iteracion%framerate == 0) update(); // Actualizamos la interfaz grafica solo si han pasado el numero de iteraciones deseadas
        iteracion = (iteracion+1)%framerate; // Aumentamos el numero de iteraciones
      }
    }
	/**
	 * Metodo que implementa el algorito de ordenamiento "Selection-Sort"
	 * Este recorre uno a uno los elementos del arreglo y los compara con
	 * todos los elementos restantes.
	 */
    private void selectionSort(){
		int aux;
		for (int i=0; i<numeros.length-1; i++) {
			for (int j=i+1; j<numeros.length; j++) {
				if (numeros[i] > numeros[j]) {
					aux = numeros[i];
					numeros[i] = numeros[j];
					numeros[j] =  aux;
				}
			}
			if(iteracion%framerate == 0) update(); // Actualizamos la interfaz grafica solo si han pasado el numero de iteraciones deseadas
			iteracion = (iteracion+1)%framerate; // Aumentamos el numero de iteraciones
		}
    }

	/**
	 * Método que implemente el algoritmo de ordenamiento "Insertion-Sort"
	 * Este inicia con un elemento y lo compara con el elemento anterior, 
	 * si el que selecciona es mayor que el anterior, lo cambia, esto para
	 * cada elemento del arreglo.
	 */
    private void insertionSort(){
		 int aux;
        for (int i = 1; i < numeros.length; i++) {
            for(int j = i ; j > 0 ; j--){
                if(numeros[j] < numeros[j-1]){
                    aux = numeros[j];
                    numeros[j] = numeros[j-1];
                    numeros[j-1] = aux;
                }
            }
            if(iteracion%framerate == 0) update(); // Actualizamos la interfaz grafica solo si han pasado el numero de iteraciones deseadas
			iteracion = (iteracion+1)%framerate; // Aumentamos el numero de iteraciones
          }
    }
	/**
	 * Método que implementa el algoritmo de ordenamiento "Merge-Sort"
	 * A partir del centro crea un subarreglo para ordenarlo y al final
	 * ordenar el arreglo general a partir de sus subarrgelos.
	 */
    private void mergeSort(){
		
		int[] auxiliar = new int [numeros.length];
		mergeSort(numeros, auxiliar,  0,  numeros.length - 1);
    }

	/**
	 * Auxiliar mergeSort para realizar el algoritmo de forma recursiva
	 * y mezclar los subarreglos partidos a las mitad recursivamente
	 */
	private void mergeSort(int [ ] a, int [ ] tmp, int izq, int der){
		if( izq < der ){
			int centro = (izq + der) / 2;
			mergeSort(a, tmp, izq, centro); //partimos y aplicamos mergeSort a la primer mitad del arreglo
			mergeSort(a, tmp, centro + 1, der); // partimos y aplicamos mergeSort a la segunda mitad del arreglo
			merge(a, tmp, izq, centro + 1, der); //Juntamos y realizamos la mezcla general del arreglo
			if(iteracion%framerate == 0) update(); // Actualizamos la interfaz grafica solo si han pasado el numero de iteraciones deseadas
			iteracion = (iteracion+1)%framerate; // Aumentamos el numero de iteraciones
		}
	}
	
	/**
	 * Método auxiliar que ordena los subarreglos
	 */
    private void merge(int[ ] a, int[ ] tmp, int izq, int der, int limDer ){
        int limIzq = der - 1;
        int k = izq;
        int num = limDer - izq + 1;
        while(izq <= limIzq && der <= limDer)
            if(a[izq]<=(a[der]))
                tmp[k++] = a[izq++];
            else
                tmp[k++] = a[der++];
        while(izq <= limIzq)  
            tmp[k++] = a[izq++];
        while(der <= limDer)  
            tmp[k++] = a[der++];
        for(int i = 0; i < num; i++, limDer--)
            a[limDer] = tmp[limDer];
    }
    
    
    
	/**
	 * Método que implementa el algoritmo de rodenamiento "Quicksort"
	 * Utiliza un ṕivote para comparar a partir de ahí los elementtos
	 * mayores a él o menores a él e intercambiarlos.
	 */
    private void quickSort(){
        quickSort(numeros,0, numeros.length - 1);
    }
    
	/**
	 * Método auxiliar que implementa el algorimo
	 * Se aplica recursivamente el mismo para cada seccion del
	 * arreglo que es separado por el pivote
	 */
	public void quickSort(int A[], int izq, int der) {

		int pivote=A[izq]; // tomamos primer elemento como pivote
		int i=izq; 
		int j=der; 
		int aux;
 
		while(i<j){  // mientras no se crucen las búsquedas
			while(A[i]<=pivote && i<j) i++; // busca elemento mayor que pivote
			while(A[j]>pivote) j--;         // busca elemento menor que pivote
			if (i<j) { //Intercambia los encontrados    
				aux= A[i];                 
				A[i]=A[j];
				A[j]=aux;
			}
			
        }
		A[izq]=A[j];
		A[j]=pivote; 
		if(izq<j-1){
			quickSort(A,izq,j-1); // ordenamos subarray izquierdo
		}
		if(j+1 <der){
			quickSort(A,j+1,der); // ordenamos subarray derecho
		}
		if(iteracion%framerate == 0) update(); // Actualizamos la interfaz grafica solo si han pasado el numero de iteraciones deseadas
		iteracion = (iteracion+1)%framerate; // Aumentamos el numero de iteraciones

	}
	

    public void swap(int i, int j){
      int aux = numeros[i];
      numeros[i] = numeros[j];
      numeros[j] = aux;
    }

  }

}
