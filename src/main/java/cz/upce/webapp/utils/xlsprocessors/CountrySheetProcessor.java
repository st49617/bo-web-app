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
    private static final List<String> QUANTITY_ALLOWED_VALUES = Arrays.asList("kg", "g");


    @Autowired
    SupplierRepository supplierRepository;
    @Autowired
    ItemRepository itemRepository;

    private static boolean isRowOmitted(int rowNumber)
    {
        return PARSING_STARTER_ROW > rowNumber;
    }

    @Override
    public List<Item> iterateSheetValues(FormulaEvaluator formulaEvaluator, Iterator<Row> rowIterator, int maxRow)
    {
        LOGGER.info("Started parsing the values from the file with:" + this.getClass().getName());

        List<Item> allItems = new ArrayList<>();
        Row row;

        Supplier supplier = supplierRepository.getOne(SUPPLIER_ID);

        //Iterate through all rows
        int rowIdx = 0;
        while (rowIterator.hasNext())
        {
            rowIdx++;

            row = rowIterator.next();
            if (isRowEmpty(row) || isRowOmitted(row.getRowNum()))
                continue;

            List<String> rowData = new ArrayList<>();

            parseRow(row, formulaEvaluator, rowData, maxRow);
            String sheetData = String.join(DELIMITER, rowData);

            if (!rowData.get(0).isEmpty())
            {
                Item item = disintegrateIntoItemCountyLife(sheetData, rowIdx, supplier);

                //save object to the database
                if (!validateImportedObject(item))
                {
                    LOGGER.warn("Item: " + item + " was not validated and was not persisted!");
                } else {
                    allItems.add(item);
                }
            }
        }
        return allItems;
    }

    private Item disintegrateIntoItemCountyLife(String string, int rowIdx, Supplier supplier)
    {
        try {
            String[] values = Arrays.stream(string.split(DELIMITER))
                    .map(String::trim)
                    .toArray(String[]::new);
            int itemTax = Integer.parseInt(values[17]);
            String quantityValue = values[13];
            double priceValue = Double.parseDouble(values[15]);
            Item item = null;

            if (checkIfQuantityValueIsPermitted(quantityValue)) {
                double itemPrice =  priceValue / 1000;
                Double itemQuantity = Double.valueOf(values[14]);
                if (quantityValue.equalsIgnoreCase("kg")) {
                    itemQuantity *= 1000;
                }
                item = new Item(values[8], itemQuantity, itemPrice, itemTax, supplier);
            } else {
                String quantity = values[12];
                if (checkIfQuantityValueIsPermitted(quantity))
                {
                    Double weightCof = checkValuesInQuantityColumn(quantity);
                    Double itemPrice = priceValue;
                    if (quantity.toUpperCase().endsWith(" KG")) {
                        weightCof*=1000;
                        itemPrice/=1000;
                    }
                    item = new Item(values[8], weightCof, itemPrice, itemTax, supplier);
                }
            }
            if (item!=null) {
                boolean bio = "BIO".equalsIgnoreCase(values[9]);
                item.rowIdx = rowIdx;
                item.bio = bio;
            }
            return item;
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

        for (String s : QUANTITY_ALLOWED_VALUES)
        {
            if (quantityValue.contains(s))
                return true;
        }

        return false;
    }
}
