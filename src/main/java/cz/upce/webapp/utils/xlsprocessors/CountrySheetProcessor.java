package cz.upce.webapp.utils.xlsprocessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.dao.stock.model.Supplier;
import cz.upce.webapp.dao.stock.repository.ItemRepository;
import cz.upce.webapp.dao.stock.repository.SupplierRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Tomas Kodym
 */
@Component
public class CountrySheetProcessor implements ISheetProcessor
{

    private static final Logger LOGGER = LoggerFactory.getLogger(CountrySheetProcessor.class);

    private static final int PARSING_STARTER_ROW = 13;
    private static final int KILO_VALUE = 1000;
    private static final int SUPPLIER_ID = 2;
    private static final String KILOS_UPPER = "Kg";
    private static final String KILOS_LOWER = "kg";
    private static final List<String> QUANTITY_FORBIDDEN_VALUES = Arrays.asList("kg", "g");


    @Autowired
    SupplierRepository supplierRepository;
    @Autowired
    ItemRepository itemRepository;

    private static boolean isRowOmitted(int rowNumber)
    {
        return PARSING_STARTER_ROW > rowNumber;
    }

    @Override
    public void iterateSheetValues(FormulaEvaluator formulaEvaluator, Iterator<Row> rowIterator, int maxRow)
    {
        LOGGER.info("Started parsing the values from the file with:" + this.getClass().getName());
        StringBuilder sheetData = new StringBuilder();


        List<Item> toSave = new ArrayList<>();
        Row row;
        //Iterate through all rows
        while (rowIterator.hasNext())
        {
            row = rowIterator.next();
            if (isRowEmpty(row) || isRowOmitted(row.getRowNum()))
                continue;

            List<String> rowData = new ArrayList<>();

            parseRow(row, formulaEvaluator, rowData, maxRow);
            sheetData.append(String.join(DELIMITER, rowData));

            if (!rowData.get(0).isEmpty())
            {
                Supplier supplier = supplierRepository.getOne(SUPPLIER_ID);
                Item item = disintegrateIntoItemCountyLife(sheetData.toString(), supplier);
                //save object to the database
                if (!validateImportedObject(item))
                {
                    LOGGER.warn("Item: " + item + " was not validated and was not persisted!");
                    cleanStringBuilder(sheetData);
                } else {
                    toSave.add(item);
                }
                persistLoadedObject(item, sheetData, itemRepository);
            }
            cleanStringBuilder(sheetData);
        }
        itemRepository.saveAll(toSave);
    }

    private Item disintegrateIntoItemCountyLife(String string, Supplier supplier)
    {
        try {
            String[] values = Arrays.stream(string.split(DELIMITER))
                    .map(String::trim)
                    .toArray(String[]::new);
            if (checkIfQuantityValueIsPermitted(values[13])) {
                return new Item(values[8], Double.valueOf(values[14]), Double.parseDouble(values[15])/1000, Integer.parseInt(values[17]), supplier);
            } else if (checkIfQuantityValueIsPermitted(values[12]))
            {
                Double weightCof = checkValuesInQuantityColumn(values[12]);
                return new Item(values[8], weightCof, countValueForOneGram(Double.parseDouble(values[15]), weightCof), Integer.parseInt(values[17]), supplier);
            }
            else
            {
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error:" + e.getMessage());
            return null;
        }
    }

    private void parseRow(Row row, FormulaEvaluator formulaEvaluator, List<String> rowData, int maxRow)
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

    private Double checkValuesInQuantityColumn(String quantity)
    {
        if (quantity.contains(KILOS_LOWER) || quantity.contains(KILOS_UPPER))
        {
            double value = Double.parseDouble(StringUtils.getDigits(quantity));
            return value * KILO_VALUE;
        }
        else
            return Double.parseDouble(StringUtils.getDigits(quantity));
    }

    private boolean checkIfQuantityValueIsPermitted(String quantityValue)
    {
        if (quantityValue.isEmpty())
            return false;

        for (String s : QUANTITY_FORBIDDEN_VALUES)
        {
            if (quantityValue.contains(s))
                return true;
        }

        return false;
    }
}
