package cz.upce.webapp.utils.xlsprocessors;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.dao.stock.repository.ItemRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author Tomas Kodym
 */

public interface ISheetProcessor
{
    String XLS_EXTENSIONS = ".xls";
    String DELIMITER = ";";
    String EMPTY_SPACE = "";
    Logger LOGGER = LoggerFactory.getLogger(ISheetProcessor.class);

    void iterateSheetValues(FormulaEvaluator formulaEvaluator, Iterator<Row> rowIterator, int maxRowIndex);

    default void parseExcelFile(MultipartFile fileName, String filePath) throws IOException
    {
        Path path = Paths.get(filePath + File.separator + fileName.getOriginalFilename());
        File fileToParse = new File(path.toUri());
        parseExcelFile(fileToParse);
    }

    default void parseExcelFile(File fileToParse) throws IOException {
        FileInputStream excelFile = new FileInputStream(fileToParse);
        Workbook workbook;

        if (fileToParse.getName().contains(XLS_EXTENSIONS))
            workbook = new HSSFWorkbook(excelFile);
        else
            workbook = new XSSFWorkbook(excelFile);

        workbook.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        int maxRowIndex = workbook.getSheetAt(0).getRow(5).getPhysicalNumberOfCells();
        Iterator<Row> iterator = datatypeSheet.iterator();
        FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
        iterateSheetValues(formulaEvaluator, iterator, maxRowIndex);
    }

    default boolean isRowEmpty(Row row)
    {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++)
        {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
                return false;
        }

        return true;
    }

    default boolean validateImportedObject(Item item)
    {
        return item != null && !item.getItemName().isEmpty() && item.getItemName() != null
                && !item.getItemQuantity().isNaN() && item.getItemQuantity() != null && item.getItemQuantity() >= 500
                && !item.getItemPrice().isNaN() && item.getItemPrice() != null && item.getItemName() != null;
    }

    default double countValueForOneGram(Double priceForKilos, Double weightCof)
    {
        return priceForKilos / weightCof;
    }

    default void cleanStringBuilder(StringBuilder stringBuilder)
    {
        stringBuilder.setLength(0);
    }

    default void persistLoadedObject(Item item, StringBuilder sheetData, ItemRepository itemRepository)
    {
        if (!validateImportedObject(item))
        {
            LOGGER.warn("Item: " + item + " was not validated and was not persisted!");
            cleanStringBuilder(sheetData);
            return;
        }

        itemRepository.save(item);
        LOGGER.info("Item: " + item + " has been written into the database successfully!");
        cleanStringBuilder(sheetData);
    }
}