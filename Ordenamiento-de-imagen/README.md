Nombre: Tadeo Guillen Diana Guadalupe


Esta aplicación permite ordenar una imagen descompuesta pixel por pixel consideranco la aplicación 
de los algoritmos de ordenamiento:
-Merge Sort
-Quick Sort
-Insertion Sort
-Selection Sort
-Bubble Sort



/***********************
  Utilizando terminal
************************/

Desde el directorio 'src':

-)  Compilar con:
      javac sort/Main.java

-)  Correr con:
      java sort.Main <archivo de resource> <velocidad> <algoritmo>

    <archivo de resource> = Nombre de archivo de imagen a procesar, debe encontrarse en la carpeta 'resource'
    <velocidad> = Numero de iteraciones que ocurrirán antes de hacer un update a la interfaz grafica
    <algoritmo> = Algoritmo de ordenamiento a utilizar, puede ser 'bubble', 'selection', 'insertion', 'merge', 'quick'

    Por ejemplo:
      java sort.Main imagen 300 bubble
      java sort.Main imagen 100 quick


VELOCIDAD PREFERENTE PARA CADA ALGORITMO

Para que se pueda apreciar el funcionamiento de cada algoritmo adecuadamente y sin que tarde demasiado

selection: 500 - 800
insertion: 100 -1000
bubble: 200 - 800
merge: 100 - 800
quick: 50 - 800
