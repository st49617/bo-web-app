package cz.upce.webapp.utils.xlsprocessors;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    List<Item> iterateSheetValues(FormulaEvaluator formulaEvaluator, Iterator<Row> rowIterator, int maxRowIndex);

    default void importItemsFromFile(MultipartFile fileName, String filePath, ItemRepository itemRepository) throws IOException
    {
        Path path = Paths.get(filePath + File.separator + fileName.getOriginalFilename());
        File fileToParse = new File(path.toUri());
        importItemsFromFile(fileToParse, itemRepository);
    }

    default void importItemsFromFile(File fileToParse, ItemRepository itemRepository) throws IOException {
        List<Item> items = parseItems(fileToParse);

        if (!items.isEmpty())
        {
            for (Item item : items)
            {
                //save object to the database
                persistLoadedObject(item, itemRepository);
            }
        }
    }

    default Map<String, Item> parseItemsAsMap(File fileToParse) throws IOException {
        List<Item> items = parseItems(fileToParse);
        Map<String, Item> map = new HashMap<>();
        for (Item item : items) {
            String key = item.getItemName() + "_" + item.getItemQuantity().intValue() + (item.bio ? "_BIO" : "");
            map.put(key, item);
        }
        return map;
    }
    default List<Item> parseItems(File fileToParse) throws IOException {
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

        LOGGER.info("Started parsing the values from the file with:" + this.getClass().getName());

        List<Item> items = iterateSheetValues(formulaEvaluator, iterator, maxRowIndex);

        List<Item> validatedItems = items.stream().filter(i -> validateImportedObject(i)).collect(Collectors.toList());
        return validatedItems;
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

    default void persistLoadedObject(Item item, ItemRepository itemRepository)
    {
        if (!validateImportedObject(item))
        {
            LOGGER.warn("Item: " + item + " was not validated and was not persisted!");
            return;
        }

        itemRepository.save(item);
        LOGGER.info("Item: " + item + " has been written into the database successfully!");
    }


    default void parseRow(Row row, FormulaEvaluator formulaEvaluator, List<String> rowData, int maxRow)
    {
        Cell cell;
        for (int i = 1; i < maxRow; i++)
        {
            cell = row.getCell(i);
            //Parse towards the cell type
            switch (cell.getCellType())
            {
                case Cell.CELL_TYPE_NUMERIC:
                    rowData.add(String.valueOf(cell.getNumericCellValue()).replaceFirst("\\.0+$", EMPTY_SPACE));
                    break;
                case Cell.CELL_TYPE_STRING:
                    rowData.add(cell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BLANK:
                    rowData.add(EMPTY_SPACE);
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    rowData.add(formulaEvaluator.evaluate(cell).formatAsString().replaceFirst("\\.0+$", EMPTY_SPACE));
                    break;
                default:
                    rowData.add(String.valueOf(cell));
            }
        }
    }
}