package com.vsu.ru.cg.objreader;

import com.vsu.ru.cg.math.vector.Vector2f;
import com.vsu.ru.cg.math.vector.Vector3f;
import com.vsu.ru.cg.model.Model;
import com.vsu.ru.cg.model.model_components.Polygon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ObjReader {

	private static final String OBJ_VERTEX_TOKEN = "v";
	private static final String OBJ_TEXTURE_TOKEN = "vt";
	private static final String OBJ_NORMAL_TOKEN = "vn";
	private static final String OBJ_FACE_TOKEN = "f";

	public static Model read(String fileContent) {
		List<Vector3f> vertexList = new ArrayList<>();
		List<Vector2f> textureVertexList = new ArrayList<>();
		List<Vector3f> normalList = new ArrayList<>();
		List<Polygon> polygonList = new ArrayList<>();
		Model result = new Model(vertexList, textureVertexList, normalList, polygonList);

		int lineInd = 0;
		Scanner scanner = new Scanner(fileContent);
		while (scanner.hasNextLine()) {
			final String line = scanner.nextLine();
			ArrayList<String> wordsInLine = new ArrayList<String>(Arrays.asList(line.split("\\s+")));
			if (wordsInLine.isEmpty()) {
				continue;
			}

			final String token = wordsInLine.get(0);
			wordsInLine.remove(0);

			++lineInd;
			switch (token) {
				// Для структур типа вершин методы написаны так, чтобы ничего не знать о внешней среде.
				// Они принимают только то, что им нужно для работы, а возвращают только то, что могут создать.
				// Исключение - индекс строки. Он прокидывается, чтобы выводить сообщение об ошибке.
				// Могло быть иначе. Например, метод parseVertex мог вместо возвращения вершины принимать вектор вершин
				// модели или сам класс модели, работать с ним.
				// Но такой подход может привести к большему количеству ошибок в коде. Например, в нем что-то может
				// тайно сделаться с классом модели.
				// А еще это портит читаемость
				// И не стоит забывать про тесты. Чем проще вам задать данные для теста, проверить, что метод рабочий,
				// тем лучше.
				case OBJ_VERTEX_TOKEN:
					result.getVertices().add(parseVertex(wordsInLine, lineInd));
					result.setVertices(result.getVertices()); // it can be deleted
					break;
				case OBJ_TEXTURE_TOKEN:
					result.getTextureVertices().add(parseTextureVertex(wordsInLine, lineInd));
					result.setTextureVertices(result.getTextureVertices()); // it can be deleted
					break;
				case OBJ_NORMAL_TOKEN:
					result.getNormals().add(parseNormal(wordsInLine, lineInd));
					result.setNormals(result.getNormals()); // it can be deleted
					break;
				case OBJ_FACE_TOKEN:
					result.getPolygons().add(parseFace(wordsInLine, lineInd));
					result.setPolygons(result.getPolygons()); // it can be deleted
					break;
				default : {}
			}
		}

		return result;
	}

	// Всем методам кроме основного я поставил модификатор доступа protected, чтобы обращаться к ним в тестах
	protected static Vector3f parseVertex(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
		try {
			return new Vector3f(
					Float.parseFloat(wordsInLineWithoutToken.get(0)),
					Float.parseFloat(wordsInLineWithoutToken.get(1)),
					Float.parseFloat(wordsInLineWithoutToken.get(2))
			);

		} catch(NumberFormatException e) {
			throw new ObjReaderException("Failed to parse float value.", lineInd);

		} catch(IndexOutOfBoundsException e) {
			throw new ObjReaderException("Too few vertex arguments.", lineInd);
		}
	}

	protected static Vector2f parseTextureVertex(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
		try {
			return new Vector2f(
					Float.parseFloat(wordsInLineWithoutToken.get(0)),
					Float.parseFloat(wordsInLineWithoutToken.get(1))
			);

		} catch(NumberFormatException e) {
			throw new ObjReaderException("Failed to parse float value.", lineInd);

		} catch(IndexOutOfBoundsException e) {
			throw new ObjReaderException("Too few texture vertex arguments.", lineInd);
		}
	}

	protected static Vector3f parseNormal(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
		try {
			return new Vector3f(
					Float.parseFloat(wordsInLineWithoutToken.get(0)),
					Float.parseFloat(wordsInLineWithoutToken.get(1)),
					Float.parseFloat(wordsInLineWithoutToken.get(2))
			);

		} catch(NumberFormatException e) {
			throw new ObjReaderException("Failed to parse float value.", lineInd);

		} catch(IndexOutOfBoundsException e) {
			throw new ObjReaderException("Too few normal arguments.", lineInd);
		}
	}

	protected static Polygon parseFace(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
		ArrayList<Integer> onePolygonVertexIndices = new ArrayList<Integer>();
		ArrayList<Integer> onePolygonTextureVertexIndices = new ArrayList<Integer>();
		ArrayList<Integer> onePolygonNormalIndices = new ArrayList<Integer>();

		for (String s : wordsInLineWithoutToken) {
			parseFaceWord(s, onePolygonVertexIndices, onePolygonTextureVertexIndices, onePolygonNormalIndices, lineInd);
		}

		Polygon result = new Polygon();
		result.setVertexIndices(onePolygonVertexIndices);
		result.setTextureVertexIndices(onePolygonTextureVertexIndices);
		result.setNormalIndices(onePolygonNormalIndices);
		return result;
	}

	// Обратите внимание, что для чтения полигонов я выделил еще один вспомогательный метод.
	// Это бывает очень полезно и с точки зрения структурирования алгоритма в голове, и с точки зрения тестирования.
	// В радикальных случаях не бойтесь выносить в отдельные методы и тестировать код из одной-двух строчек.
	protected static void parseFaceWord(
			String wordInLine,
			ArrayList<Integer> onePolygonVertexIndices,
			ArrayList<Integer> onePolygonTextureVertexIndices,
			ArrayList<Integer> onePolygonNormalIndices,
			int lineInd) {
		try {
			String[] wordIndices = wordInLine.split("/");
			switch (wordIndices.length) {
				case 1 -> {
					onePolygonVertexIndices.add(Integer.parseInt(wordIndices[0]) - 1);
				}
				case 2 -> {
					onePolygonVertexIndices.add(Integer.parseInt(wordIndices[0]) - 1);
					onePolygonTextureVertexIndices.add(Integer.parseInt(wordIndices[1]) - 1);
				}
				case 3 -> {
					onePolygonVertexIndices.add(Integer.parseInt(wordIndices[0]) - 1);
					onePolygonNormalIndices.add(Integer.parseInt(wordIndices[2]) - 1);
					if (!wordIndices[1].equals("")) {
						onePolygonTextureVertexIndices.add(Integer.parseInt(wordIndices[1]) - 1);
					}
				}
				default -> {
					throw new ObjReaderException("Invalid element size.", lineInd);
				}
			}

		} catch(NumberFormatException e) {
			throw new ObjReaderException("Failed to parse int value.", lineInd);

		} catch(IndexOutOfBoundsException e) {
			throw new ObjReaderException("Too few arguments.", lineInd);
		}
	}
}
