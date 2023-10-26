package root.documents;

import root.Goods;
import root.billException.ParsingException;

import java.io.File;
import java.util.Map;

/**
 * Реальзующий интерфейс класс, должен уметь формировать чек и печатать его в файл
 */
public interface PrintToFile {
    public boolean printToFile(File file, Map<Integer, Goods> goodsDirectory) throws ParsingException;

}
