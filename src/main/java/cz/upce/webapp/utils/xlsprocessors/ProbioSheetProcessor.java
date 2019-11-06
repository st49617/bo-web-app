package cz.upce.webapp.utils.xlsprocessors;

import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.dao.stock.model.Supplier;
import cz.upce.webapp.dao.stock.repository.SupplierRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Tomas Kodym
 */

@Component
public class ProbioSheetProcessor implements ISheetProcessor
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProbioSheetProcessor.class);
    private static final List<Integer> OMITTED_ROW_INDEXES = Arrays.asList(0, 1, 2);
    private static final int SUPPLIER_ID = 3;

    @Autowired
    private SupplierRepository supplierRepository;

    private static boolean isRowOmitted(int rowNumber)
    {
        return OMITTED_ROW_INDEXES.contains(rowNumber);
    }


    @Override
    public List<Item> iterateSheetValues(FormulaEvaluator formulaEvaluator, Iterator<Row> rowIterator, int maxRow)
    {
        Row row;
        List<Item> allItems = new ArrayList();

        Supplier supplier = supplierRepository.getOne(SUPPLIER_ID);

        //Iterate through all rows
        while (rowIterator.hasNext())
        {
            row = rowIterator.next();
            if (isRowEmpty(row) || isRowOmitted(row.getRowNum()))
                continue;

            List<String> rowData = new ArrayList<>();
            parseRow(row, formulaEvaluator, rowData, maxRow);

            List<Item> itemList = disintegrateIntoItem(rowData, supplier);
            for (Item item : itemList) {
                item.setItemQuantity(item.getItemQuantity()*1000);
                item.setItemPrice(item.getItemPrice()/1000);
            }
            allItems.addAll(itemList);
        }
        return allItems;
    }

    private List<Item> disintegrateIntoItem(List<String> sheetData, Supplier supplier) {
        List<Item> itemsList = new ArrayList<>();
        if (!sheetData.isEmpty()) {
            //split values from list to array
            String[] values = sheetData.toArray(new String[0]);
            if (StringUtils.isNumeric(values[6])) {
                String itemName = values[5];
                double itemQuantity = Double.parseDouble(values[6]);
                double itemPrice = Double.parseDouble(values[7]);
                int itemTax = Integer.parseInt(values[8]);
                itemsList.add(new Item(itemName, itemQuantity, itemPrice, itemTax, supplier));
            } else
                LOGGER.warn("Item was not created, because of non numeric value in quantity column!");
        }
        return itemsList;

    }

    private boolean validateCellValue(Cell cell)
    {
        return true;
    }

}
