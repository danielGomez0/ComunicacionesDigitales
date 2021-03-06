package mundo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Imagen {

	/**
	 * Declaraci�n de las variables a utilizar
	 */

	public static final int anchoPermitido = 120;
	public static final int altoPermitido = 120;
	private Color arreglo[][];
	private String matrizRGB[][];
	private int matrizBin[][];
	private int matrizRed[][];
	private int matrizGreen[][];
	private int matrizBlue[][];
	private int matrizPromRGB[][];

	private int ancho;
	private int alto;
	private ImageIcon icon;

	/**
	 * Contructor de la clase Imagen Inicializa las matrices a utilizar.
	 *  Carga la imagen que se utilizar�.
	 * 
	 * @param archivo
	 * 
	 */
	public Imagen(String archivo) {
		arreglo = new Color[anchoPermitido][anchoPermitido];
		matrizRGB = new String[ancho][alto];
		cargarImagen(archivo);
		matrizBin = new int[ancho][alto];
		matrizRed = new int[ancho][alto];
		matrizGreen = new int[ancho][alto];
		matrizBlue = new int[ancho][alto];
		matrizPromRGB = new int[ancho][alto];
	}

	/**
	 * Convierte la imagen en un archivo entendible y manejable por java.
	 * 
	 * @apiNote inicializa y llena la matriz de tipo Color, RGB
	 * @apiNote inicializa y llena la matriz de tipo String con los colores RGB
	 *          separados por comas
	 * @param archivo
	 */
	public void cargarImagen(String archivo) {

		BufferedImage bf = null;
		try {
			bf = ImageIO.read(new File(archivo));
		} catch (IOException ex) {
			Logger.getLogger(Imagen.class.getName()).log(Level.SEVERE, null, ex);
		}
		if (bf.getWidth() < anchoPermitido) {
			ancho = bf.getWidth();
		} else
			ancho = anchoPermitido;

		ancho = anchoPermitido;
		if (bf.getHeight() < altoPermitido) {
			alto = bf.getHeight();

		} else
			alto = altoPermitido;

		int cont = 0;
		arreglo = new Color[anchoPermitido][anchoPermitido];
		matrizRGB = new String[ancho][alto];

		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {
				cont++;
				arreglo[i][j] = new Color(bf.getRGB(j, i));
				Color colorRGB = arreglo[i][j];
				String colorRGBString = "";
				colorRGBString = colorRGB.getRed() + "," + colorRGB.getGreen() + "," + colorRGB.getBlue() + ",";
				matrizRGB[i][j] = colorRGBString;

			}
		}
	}


	/**
	 * Este metodo llena las matrices con la informacion rgb de los pixeles de la imagen
	 * @apiNote private int matrizRed[][]; private int matrizGreen[][]; private int
	 *          matrizBlue[][]; private int matrizPromRGB[][];
	 *
	 */
	public void crearMatricesRGB() {

		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {

				Color rgb = arreglo[i][j];
				int prom = (rgb.getBlue() + rgb.getRed() + rgb.getGreen()) / 3;

				matrizRed[i][j] = rgb.getRed();
				matrizGreen[i][j] = rgb.getGreen();
				matrizBlue[i][j] = rgb.getBlue();
				matrizPromRGB[i][j] = prom;

			}
		}
	}


	/**
	 * Binariza la imagen (a blanco y negro) utilizando un umbral a elecci�n.
	 * 
	 * @exceptionSe debe primero realizar el m�todo cargarImagen(String archivo)
	 * @param umbral
	 */
	public void binarizarImagen(double umbral) {

		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {

				if (matrizPromRGB[i][j] < umbral) {
					arreglo[i][j] = Color.BLUE;

				} else {
					arreglo[i][j] = Color.PINK;

				}
			}
		}
	}

	/**
	 * Este metodo comprime la informacion de cada fila de la matriz que entra por
	 * parametro. Cada matriz que entre por parametro tendra informacion de pixeles
	 * ; solamente rojo, solamente verdes o solamente azules. La informacion se
	 * compremira utilizando el algorithmo RLE.
	 * 
	 * @param matriz
	 * @return Una lista de String la cual contiene comprimida la informacion RLE
	 *         por cada fila de la matriz que entro por parametro
	 */
	
	public ArrayList<String> CompresionRLE(int matriz[][]){
		
		Queue<Integer> numeros = new LinkedList<Integer>();

		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {
				numeros.add(matriz[i][j]);

			}
		}
		int contadorVecesAparece = 1;
		int numeroSiguiente = 0;
		int numeroActual=0;
		String cantApareceNum ="";
		int contadorFila = 0;
		boolean seAgrego=false;
		ArrayList<String> filas = new ArrayList<String>();
		
		while (!numeros.isEmpty()) {
			contadorFila++;

			if(contadorFila==120) {

				if(seAgrego) {
				
					numeroActual=numeros.remove();
					cantApareceNum+=contadorVecesAparece+"-"+numeroActual+"-";
					seAgrego=false;
					
				}
				
				else {
					cantApareceNum+=contadorVecesAparece+"-"+numeroActual+"-";
					numeros.remove();

				}
				
				filas.add(cantApareceNum);
				cantApareceNum="";
				contadorFila=0;
				contadorVecesAparece=1;
			
				
			}
			
			
			else {

				numeroActual=numeros.remove();

				
				if(!numeros.isEmpty()) {
				
					numeroSiguiente=numeros.peek();
					
					if(numeroActual==numeroSiguiente) {
						contadorVecesAparece++;
						
					}
					
					else {
						cantApareceNum+=contadorVecesAparece + "-" + numeroActual +"-";
						contadorVecesAparece=1;
						if(contadorFila==119) {
							seAgrego=true;

						}
					}
					
					
				}
				
				
			}
			
		
			
		
			
			
			
		}
		
		
		
		return filas;
	}
	
	
	
	


	/**
	 * Este m�todo agrega los numeros que se encuentran en la matr�z que entra por
	   par�metro a una cola.
	 * 
	 * @param matriz
	 * @return Queue<Integer> numeros - Una cola de tipo Integer con todos los n�meros de la matr�z
	 */

	public Queue<Integer> colaNumerosInicial(int[][] matriz) {

		Queue<Integer> numeros = new LinkedList<Integer>();

		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {
				numeros.add(matriz[i][j]);

			}
		}

		return numeros;
	}

	
	/**
	 * Este m�todo convierte la informacion comprimida en RLE a informacion binaria
	 * 
	 * @param matrizRLE - Una lista de Strings la cual contiene la informacion comprimida usando el algoritmo RLE
	 * @return ArrayList<String> filaBinarizada - Una lista de Strings con la informacion convertida a binario
	 */
	
	public ArrayList<String> binarizarMatrizRLE(ArrayList<String> matrizRLE) {
		String cadenaBinarizada = "";
		String cadena = "";
		char guion = '-';
		String numerosActuales = "";
		ArrayList<String> filaBinarizada = new ArrayList<String>();
		File file = new File("./EncoderBinario.txt");
		FileWriter writer = null;
		  try {
				writer = new FileWriter(file);
			} catch (IOException e1) {
				System.out.println("Se encontr� un error en la escritura de la im�gen :/");
				e1.printStackTrace();
			}
		for (int i = 0; i < matrizRLE.size(); i++) {

			cadena = matrizRLE.get(i);

			for (int j = 0; j < cadena.length(); j++) {

				char actual = cadena.charAt(j);
				if (cadena.length() != (j + 1)) {

					char siguiente = cadena.charAt(j + 1);

					if (actual != guion) {
						numerosActuales += actual + "";

						if (siguiente != guion) {
//							  numerosActuales+=actual+"";
						} else {
							cadenaBinarizada += decimalToBinary(numerosActuales);
							numerosActuales = "";

						}

					}

					else {
						cadenaBinarizada += guionBinarizado();
					}

				} else {

					cadenaBinarizada += guionBinarizado();

				}
			}
			filaBinarizada.add(cadenaBinarizada);
			
			  try {

						writer.write(cadenaBinarizada+"\n");
						
					
				} catch (IOException e) {

					e.printStackTrace();
				}
			  
			cadenaBinarizada = "";

		}
		 if (writer != null) {
				
				try {
					writer.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		 
		return filaBinarizada;

	}

	/**
	 * Este m�todo convierte un n�mero de deciaml a binario
	 * 
	 * @param numeros - una cadena de tipo String con el n�mero decimal a binarizar
	 * @return String : Una cadena de tipo String con el n�mero binarizado
	 */

	public String decimalToBinary(String numeros) {
		String cadenaNumeroBin = "";
		String num = numeros;
		int numero = Integer.parseInt(num);
		String binario = "";
		if (numero > 0) {
			while (numero > 0) {
				if (numero % 2 == 0) {
					binario = "0" + binario;
				} else {
					binario = "1" + binario;
				}
				numero = (int) numero / 2;
			}
		} else if (numero == 0) {
			binario = "0";
		} else {
			binario = "No se pudo convertir el numero. Ingrese solo n�meros positivos";
		}

		cadenaNumeroBin = concatenarCerosFaltantes(binario);
		return cadenaNumeroBin;
	}

	/**
	 * Este m�todo convirte el caracter "-" a binario
	 * 
	 * @return Sring  :Una cadena de String con el valor binario del caracter
	 */
	public String guionBinarizado() {
		String guion = "-";
		String guionBinarizado;
		String binario = "";
		int x = 0;
		x = guion.charAt(0);
		guionBinarizado = Integer.toBinaryString(x);

		binario = concatenarCerosFaltantes(guionBinarizado);

		return binario;
	}

	/**
	 * Este m�todo le agrega los n�meros faltantes al n�mero binarizado del caracter "-" que deben ser 8 en base 2.
	 * 
	 * @param binario - Una cadena de tipo String con los n�meros en binario que corresponden al caracter "-"-
	 * @return String  :Una cadena de String que contiene la informaci�n que entr� c�mo par�metro y se le concaten� los ceros faltantes.
	 */
	public static String concatenarCerosFaltantes(String binario) {

		int base8 = 8;
		int cerosFaltantes = base8 - binario.length();
		String ceros = "";
		for (int i = 0; i < cerosFaltantes; i++) {
			ceros += 0;
		}
		return ceros + binario;
	}

	
	/**
	 * Retorna una matr�z que contiene s�lo los valores de color rojo del formao RGB de la im�gen.
	 * @return
	 */
	public int[][] getMatrizRed() {
		return matrizRed;
	}

	public void setMatrizRed(int[][] matrizRed) {
		this.matrizRed = matrizRed;
	}

	/**
	 * Retorna una matr�z que contiene s�lo los valores de color verde del formao RGB de la im�gen.
	 * @return
	 */
	public int[][] getMatrizGreen() {
		return matrizGreen;
	}

	public void setMatrizGreen(int[][] matrizGreen) {
		this.matrizGreen = matrizGreen;
	}

	/**
	 * Retorna una matr�z que contiene s�lo los valores de color azul del formao RGB de la im�gen.
	 * @return
	 */
	public int[][] getMatrizBlue() {
		return matrizBlue;
	}

	public void setMatrizBlue(int[][] matrizBlue) {
		this.matrizBlue = matrizBlue;
	}

	/**
	 * Este m�todo verifica si la cadena que le entra por parametro con n�meros
	 * binarios: es un n�mero o corresponde en binario al caracter "-".
	 * 
	 * @param cadena - Un cadena de String el cual contiene el valor binario de un
	 *               n�mero o del caracter "-".
	 * @return Boolean  :un valor booleano el cual es true si la cadena de par�metro es un numero, false en caso contrario.
	 */
	public boolean esNumero(String cadena) {
		char indicadorFila = '/';
		boolean encontro = true;
		for (int i = 0; i < cadena.length() && encontro == true; i++) {

			char caracterActual = cadena.charAt(i);

			if (caracterActual == indicadorFila) {
				encontro = false;

			}

		}
		return encontro;
	}



	/**
	 * M�todo que realiza el promedio de un Color RGB
	 * @param Una variable tipo Color 
	 * @return int : El promedio de los tres valores RGB que contiene la varable tipo Color.
	 */

	public int promedioColorRGB(Color rgb) {

		return (rgb.getRed() + rgb.getGreen() + rgb.getBlue()) / 3;
	}

	/**
	 * Metodo que permite imprimir la imagen despues de haber
	 * 
	 * @return
	 */

	public BufferedImage imprimirImagen2(int[][] matrizRed, int[][] matrizGreen, int[][] matrizBlue) {
		BufferedImage salida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {
				Color color = new Color(matrizRed[i][j], matrizGreen[i][j], matrizBlue[i][j]);
				salida.setRGB(j, i, color.getRGB());
			}
		}
		return salida;
	}

	/**
	 * Este m�todo decodifica la informaci�n que se encuentra binarizada y lo convierte al formato incial , RLE.
	 * 
	 * @param filas - Una lista de Strings la cual se encuentra en formato binario
	 * @return ArrayList<String> filasDecodificadas - Una lista de tipo String con n�meros en formato decimal y caracteres "-".
	 */
	public ArrayList<String> decodificarRLEBinarizado(ArrayList<String> filas) {
		int base2 = 8;
		int contador = 0;
		String cadenaDecodificada = "";
		String cadenaADecodificar = "";
		String valoresActuales = "";
		String valorAnterior = "";
		int contadorBina = 0;
		File file = new File("./decodificarRLEBinarizado.txt");
		FileWriter writer = null;
		  try {
				writer = new FileWriter(file);
			} catch (IOException e1) {
				System.out.println("Se produjo un error al escribir el archivo :/");
				e1.printStackTrace();
			}
		ArrayList<String> filasDecoficadas = new ArrayList<String>();
		for (int i = 0; i < filas.size(); i++) {

			cadenaADecodificar = filas.get(i);

			for (int j = 0; j < cadenaADecodificar.length(); j++) {
				contador++;
				if (contador < base2) {
					valoresActuales += cadenaADecodificar.charAt(j);
				}

				else if (contador == 8) {
					valoresActuales += cadenaADecodificar.charAt(j);
					if (esGuion(valoresActuales)) {

						if (valorAnterior.equals("00101101")) {
							contadorBina++;

							if (contadorBina == 1) {
								cadenaDecodificada += binarioADecimal(valoresActuales);
								valorAnterior = valoresActuales;
								valoresActuales = "";
								contador = 0;
							}

							if (contadorBina == 2) {

								cadenaDecodificada += "-";
								valorAnterior = "00101101";
								valoresActuales = "";
								contador = 0;
								contadorBina = 0;

							}

						}

						else {
							cadenaDecodificada += "-";
							valorAnterior = "00101101";
							valoresActuales = "";
							contador = 0;
						}

					}

					else {
						cadenaDecodificada += binarioADecimal(valoresActuales);
						valorAnterior = valoresActuales;
						valoresActuales = "";
						contador = 0;
					}
				} else {

					valoresActuales += cadenaADecodificar.charAt(j);

				}

			}

			filasDecoficadas.add(cadenaDecodificada);
			  try {

					writer.write(cadenaDecodificada+"\n");
					
				
			} catch (IOException e) {

				e.printStackTrace();
			}
			cadenaDecodificada = "";

		}
		if (writer != null) {
			
			try {
				writer.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return filasDecoficadas;

	}

	/**
	 * Este m�todo verifica si la cadena que entra por parametro corresponde al
	 * valor en binario del caracter "-"
	 * 
	 * @param cadena - una cadena de tipo String con informaci�n en binario
	 * @return boolean  : true si la cadena que entra por par�metro
	 *         corresponde al valor binario del "-", false en caso contrario.
	 */
	public boolean esGuion(String cadena) {
		String guion = "00101101";
		if (cadena.equals(guion)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Este m�todo convierte los n�meros que esten en binario a formato decimal.
	 * 
	 * @param numeroBinario - Una cadena de String con n�meros binarios
	 * @return int resultado - un n�mero decimal.
	 */
	public int binarioADecimal(String numeroBinario) {
		int longitud = numeroBinario.length();// Numero de digitos que tiene nuestro binario
		int resultado = 0;// Aqui almacenaremos nuestra respuesta final
		int potencia = longitud - 1;
		for (int i = 0; i < longitud; i++) {// recorremos la cadena de numeros
			if (numeroBinario.charAt(i) == '1') {
				resultado += Math.pow(2, potencia);
			}
			potencia--;// drecremantamos la potencia
		}
		return resultado;
	}

	/**
	 * Este metodo descomprime la informaci�n que se encuentra comprimida por el algoritmo RLE.
	 * 
	 * @param filasRLE - Una lista de String que contienene la informacion
	 *                 comprimida
	 * @return numeros - una cola con todos los numeros que se descomprimieron.
	 */
	public Queue<Integer> descomprimirRLE(ArrayList<String> filasRLE) {
		Queue<Integer> numeros = new LinkedList<Integer>();

		String cadenaRLE = "";
		int contador = 0;

		for (int i = 0; i < filasRLE.size(); i++) {

			cadenaRLE = filasRLE.get(i);

			String arreglo[] = cadenaRLE.split("-");

			for (int k = 0; k < arreglo.length; k++) {
				int actualNumero = Integer.parseInt(arreglo[k]);

				if (k % 2 == 0) {
					contador += actualNumero;

					int numeroSiguiente = Integer.parseInt(arreglo[k + 1]);

					for (int l = 0; l < actualNumero; l++) {
						numeros.add(numeroSiguiente);

					}

				}

			}
			contador = 0;

		}

		return numeros;
	}

	/**
	 * Este m�todo construye una matr�z de 120 de alto x 120 de ancho,
	 * llena la matriz a partir de los numeros que se encuentran en la cola de tipo Integer.
	 * 
	 * @param Queue<Integer>  colaNumeros - Una cola de tipo Integer
	 * @return int[][]  matrizRGB - Una matr�z con todos los n�meros que se encontraban en la
	 *         cola que entr� por par�metro
	 */
	public int[][] matrizReconstruidaRLE(Queue<Integer> colaNumeros) {

		int[][] matrizRGB = new int[alto][ancho];

		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {
				if (!colaNumeros.isEmpty()) {
					matrizRGB[i][j] = colaNumeros.remove();

				}

			}
		}

		return matrizRGB;

	}

	/**
	 * Este m�todo llena una cola con la informaci�n que le entra por par�metro
	 * 
	 * @param matriz una matriz de tipo int
	 * @return colaNumeros - una cola llena de la informaci�n que se encontraba
	 */

	public Queue<Integer> numerosMatriz(int[][] matriz) {

		Queue<Integer> colaNumeros = new LinkedList<Integer>();

		for (int i = 0; i < 120; i++) {
			for (int j = 0; j < 120; j++) {

				colaNumeros.add(matriz[i][j]);

			}
		}

		return colaNumeros;
	}

}
